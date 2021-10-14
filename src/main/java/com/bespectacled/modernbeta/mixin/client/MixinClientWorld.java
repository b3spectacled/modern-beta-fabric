package com.bespectacled.modernbeta.mixin.client;

import java.util.Optional;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.biome.ClimateBiomeProvider;
import com.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import com.bespectacled.modernbeta.api.world.biome.climate.SkyClimateSampler;
import com.bespectacled.modernbeta.client.color.BetaBlockColors;
import com.bespectacled.modernbeta.util.OldClientWorld;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.biome.beta.climate.BetaClimateSampler;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;

@Environment(EnvType.CLIENT)
@Mixin(value = ClientWorld.class, priority = 1)
public abstract class MixinClientWorld implements OldClientWorld {
    @Shadow private MinecraftClient client;
    
    @Unique private Vec3d curPos;
    @Unique private boolean isOldWorld;
    @Unique private Optional<ClimateSampler> climateSampler;
    @Unique private Optional<SkyClimateSampler> skyClimateSampler;

    @Override
    public boolean isBetaBiomeWorld() {
        return this.climateSampler.isPresent();
    }
    
    @Override
    public boolean isOldWorld() {
        return this.isOldWorld;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(
        ClientPlayNetworkHandler netHandler, 
        ClientWorld.Properties properties,
        RegistryKey<World> worldKey, 
        DimensionType dimensionType, 
        int loadDistance,
        int j,
        Supplier<Profiler> profiler,
        WorldRenderer renderer, 
        boolean debugWorld, 
        long seed, 
        CallbackInfo ci
    ) {
        long worldSeed = this.parseFixedSeed(ModernBeta.RENDER_CONFIG.fixedSeedConfig.fixedSeed);
        boolean useFixedSeed = ModernBeta.RENDER_CONFIG.fixedSeedConfig.useFixedSeed;
        
        this.isOldWorld = false;
        this.climateSampler = Optional.ofNullable(useFixedSeed ? new BetaClimateSampler(worldSeed) : null);
        this.skyClimateSampler = Optional.ofNullable(useFixedSeed ? new BetaClimateSampler(worldSeed) : null);
        
        if (this.client.getServer() != null && worldKey != null) { // Server check
            ChunkGenerator chunkGenerator = this.client.getServer().getWorld(worldKey).getChunkManager().getChunkGenerator();
            BiomeSource biomeSource = chunkGenerator.getBiomeSource();
            
            worldSeed = this.client.getServer().getWorld(worldKey).getSeed();
            
            this.isOldWorld = chunkGenerator instanceof OldChunkGenerator || biomeSource instanceof OldBiomeSource;
            
            if (biomeSource instanceof OldBiomeSource oldBiomeSource) {
                if (oldBiomeSource.getBiomeProvider() instanceof ClimateBiomeProvider climateBiomeProvider)
                    this.climateSampler = Optional.ofNullable(climateBiomeProvider.getClimateSampler());
                if (oldBiomeSource.getBiomeProvider() instanceof ClimateBiomeProvider climateBiomeProvider)
                    this.skyClimateSampler = Optional.ofNullable(climateBiomeProvider.getSkyClimateSampler());
            }
        }
        
        // Set Beta block colors seed.
        BetaBlockColors.INSTANCE.setSeed(worldSeed, this.climateSampler);
    }
    
    @ModifyVariable(
        method = "method_23777",
        at = @At("HEAD"),
        index = 1
    )
    private Vec3d capturePos(Vec3d pos) {
        return curPos = pos;
    }
    
    @ModifyVariable(
        method = "method_23777",
        at = @At(
            value = "INVOKE_ASSIGN",  
            target = 
                "Lnet/minecraft/util/CubicSampler;sampleColor(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/CubicSampler$RgbFetcher;)Lnet/minecraft/util/math/Vec3d;"
        ),
        index = 6  
    )
    private Vec3d injectBetaSkyColor(Vec3d skyColorVec) {
        if (this.skyClimateSampler.isPresent() && this.skyClimateSampler.get().sampleSkyColor()) {
            int x = (int)curPos.getX();
            int z = (int)curPos.getZ();
            float temp = (float)this.skyClimateSampler.get().sampleSkyTemp(x, z);
            
            skyColorVec = Vec3d.unpackRgb(this.sampleBetaSkyColor(temp));
        }
        
        return skyColorVec;
    }
    
    @Unique
    private int sampleBetaSkyColor(float temp) {
        temp /= 3F;
        temp = MathHelper.clamp(temp, -1F, 1F);
        
        return MathHelper.hsvToRgb(0.6222222F - temp * 0.05F, 0.5F + temp * 0.1F, 1.0F);
    }
    
    @Unique
    private long parseFixedSeed(String stringSeed) {
        long seed = 0L;
        
        if (!stringSeed.isEmpty()) {
            try {
                seed = Long.parseLong(stringSeed);
            } catch (NumberFormatException e) {
                seed = stringSeed.hashCode();
            }
        }
        
        return seed;
    }
}

