package com.bespectacled.modernbeta.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.config.ModernBetaConfig;
import com.bespectacled.modernbeta.gen.OldChunkGenerator;
import com.bespectacled.modernbeta.util.BiomeUtil;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

/*
 * Based on Colormatic ClientWorldMixin
 */
@Mixin(value = ClientWorld.class, priority = 1)
public abstract class MixinClientWorld extends World {
    
    @Unique
    private BlockPos curBlockPos = new BlockPos(0, 0, 0);
    
    @Unique
    private ModernBetaConfig BETA_CONFIG = ModernBeta.BETA_CONFIG;
    
    @Unique
    private boolean isBetaWorld = true;
    
    @Unique
    private boolean isOverworld = false;
    
    @Shadow
    private MinecraftClient client;
    
    @Unique
    private long worldSeed = 0L;

    private MixinClientWorld() {
        super(null, null, null, null, false, false, 0L);
        
    }
    
    @Unique
    private static void setSeed(long seed) {
        BiomeUtil.setSeed(seed);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(ClientPlayNetworkHandler netHandler, ClientWorld.Properties properties,
            RegistryKey<World> worldKey, DimensionType dimensionType, int loadDistance, Supplier<Profiler> profiler,
            WorldRenderer renderer, boolean debugWorld, long seed, CallbackInfo ci) {

        if (client.getServer() != null) { // Server check
           ChunkGenerator generator = client.getServer().getOverworld().getChunkManager().getChunkGenerator();
           this.setWorldProperties(generator, generator.worldSeed);
        }

        this.isOverworld = worldKey.getValue().equals(DimensionType.OVERWORLD_REGISTRY_KEY.getValue());
        
        ModernBeta.setBlockColorsSeed(worldSeed, isBetaWorld);
    }
    
    @Unique
    private void setWorldProperties(ChunkGenerator gen, long seed) {
        this.worldSeed = seed;
        this.isBetaWorld = false;
        
        if (gen instanceof OldChunkGenerator && ((OldBiomeSource)gen.getBiomeSource()).isBeta()) {
            this.isBetaWorld = true;
            
            if (((OldBiomeSource)gen.getBiomeSource()).isVanilla()) this.isBetaWorld = false;
            
            this.worldSeed = BETA_CONFIG.fixedSeed == 0L ? worldSeed : BETA_CONFIG.fixedSeed;
            setSeed(this.worldSeed);
        }
    }
    
    @ModifyVariable(
        method = "method_23777",
        at = @At("HEAD"),
        index = 1
    )
    private BlockPos captureBlockPos(BlockPos pos) {
        curBlockPos = pos;
        
        return pos;
    }
    
    @ModifyVariable(
        method = "method_23777",
        at = @At(value = "INVOKE_ASSIGN",  target = "Lnet/minecraft/world/biome/Biome;getSkyColor()I"),
        index = 6  
    )
    private int injectBetaSkyColor(int skyColor) {
        if (isBetaWorld && BETA_CONFIG.renderBetaSkyColor && this.isOverworld) {
            skyColor = getBetaSkyColor(curBlockPos);
        }
        
        return skyColor;
    }

    private int getBetaSkyColor(BlockPos pos) {
        int x = pos.getX();
        int z = pos.getZ();

        float temp = (float) BiomeUtil.sampleSkyTemp(x, z);
        int skyColor = getSkyColorByTemp(temp);

        return skyColor;
    }

    @Unique
    private static int getSkyColorByTemp(float temp) {
        temp /= 3F;

        if (temp < -1F) {
            temp = -1F;
        }

        if (temp > 1.0F) {
            temp = 1.0F;
        }
        return MathHelper.hsvToRgb(0.6222222F - temp * 0.05F, 0.5F + temp * 0.1F, 1.0F);
    }

}
