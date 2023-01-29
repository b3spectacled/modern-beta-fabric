package mod.bespectacled.modernbeta.mixin.client;

import java.util.Optional;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import mod.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import mod.bespectacled.modernbeta.api.world.biome.climate.SkyClimateSampler;
import mod.bespectacled.modernbeta.client.color.BetaBlockColors;
import mod.bespectacled.modernbeta.util.GenUtil;
import mod.bespectacled.modernbeta.util.ModernBetaClientWorld;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.biome.provider.BetaBiomeProvider;
import mod.bespectacled.modernbeta.world.biome.provider.settings.BiomeProviderSettings;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;

@Environment(EnvType.CLIENT)
@Mixin(value = ClientWorld.class, priority = 1)
public abstract class MixinClientWorld implements ModernBetaClientWorld {
    @Shadow private MinecraftClient client;
    
    @Unique private Vec3d capturedPos;
    @Unique private Optional<ClimateSampler> climateSampler;
    @Unique private Optional<SkyClimateSampler> skyClimateSampler;
    @Unique private boolean isModernBetaWorld;
    
    @Override
    public boolean isModernBetaWorld() {
        return this.isModernBetaWorld;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(
        ClientPlayNetworkHandler netHandler,
        ClientWorld.Properties properties,
        RegistryKey<World> registryRef,
        RegistryEntry<DimensionType> dimensionType,
        int loadDistance,
        int simulationDistance,
        Supplier<Profiler> profiler,
        WorldRenderer renderer,
        boolean debugWorld,
        long seed,
        CallbackInfo info
    ) {
        long worldSeed = GenUtil.parseSeed(ModernBeta.RENDER_CONFIG.configFixedSeed.fixedSeed);
        boolean useFixedSeed = ModernBeta.RENDER_CONFIG.configFixedSeed.useFixedSeed;
        
        // Init with default values
        this.climateSampler = Optional.ofNullable(useFixedSeed ? 
            new BetaBiomeProvider(worldSeed, BiomeProviderSettings.createSettingsBeta(), null) : 
            null
        );
        this.skyClimateSampler = Optional.ofNullable(useFixedSeed ? 
            new BetaBiomeProvider(worldSeed, BiomeProviderSettings.createSettingsBeta(), null) : 
            null
        );
        this.isModernBetaWorld = false;
        
        // Server check
        if (this.client.getServer() != null && registryRef != null) {
            ChunkGenerator chunkGenerator = this.client.getServer().getWorld(registryRef).getChunkManager().getChunkGenerator();
            BiomeSource biomeSource = chunkGenerator.getBiomeSource();
            
            worldSeed = this.client.getServer().getWorld(registryRef).getSeed();
            
            if (biomeSource instanceof ModernBetaBiomeSource oldBiomeSource) {
                BiomeProvider biomeProvider = oldBiomeSource.getBiomeProvider();
                
                if (biomeProvider instanceof ClimateSampler climateSampler)
                    this.climateSampler = Optional.ofNullable(climateSampler);
                
                if (biomeProvider instanceof SkyClimateSampler skyClimateSampler)
                    this.skyClimateSampler = Optional.ofNullable(skyClimateSampler);
            }
            
            this.isModernBetaWorld = chunkGenerator instanceof ModernBetaChunkGenerator || biomeSource instanceof ModernBetaBiomeSource;
        }
        
        // Set Beta block colors seed.
        BetaBlockColors.INSTANCE.setSeed(worldSeed, this.climateSampler);
    }
    
    @Inject(method = "getSkyColor", at = @At("HEAD"))
    private void capturePos(Vec3d cameraPos, float tickDelta, CallbackInfoReturnable<Vec3d> info) {
        this.capturedPos = cameraPos;
    }
    
    @ModifyVariable(
        method = "getSkyColor",
        at = @At(
            value = "INVOKE_ASSIGN",  
            target = "Lnet/minecraft/util/CubicSampler;sampleColor(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/CubicSampler$RgbFetcher;)Lnet/minecraft/util/math/Vec3d;"
        ),
        index = 6  
    )
    private Vec3d injectBetaSkyColor(Vec3d skyColorVec) {
        if (this.skyClimateSampler.isPresent() && this.skyClimateSampler.get().sampleSkyColor()) {
            int x = (int)capturedPos.getX();
            int z = (int)capturedPos.getZ();
            
            float temp = (float)this.skyClimateSampler.get().sampleSkyTemp(x, z);
            temp /= 3F;
            temp = MathHelper.clamp(temp, -1F, 1F);
            
            skyColorVec = Vec3d.unpackRgb(MathHelper.hsvToRgb(0.6222222F - temp * 0.05F, 0.5F + temp * 0.1F, 1.0F));
        }
        
        return skyColorVec;
    }
}

