package com.bespectacled.modernbeta.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.biome.beta.BetaClimateSampler;
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
    private Vec3d curPos = new Vec3d(0, 0, 0);
    
    @Unique
    private ModernBetaConfig BETA_CONFIG = ModernBeta.BETA_CONFIG;
    
    @Unique
    private boolean useBetaColors = false;
    
    @Unique
    private boolean isOverworld = false;
    
    @Shadow
    private MinecraftClient client;

    private MixinClientWorld() {
        super(null, null, null, null, false, false, 0L);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(ClientPlayNetworkHandler netHandler, ClientWorld.Properties properties,
            RegistryKey<World> worldKey, DimensionType dimensionType, int loadDistance, Supplier<Profiler> profiler,
            WorldRenderer renderer, boolean debugWorld, long seed, CallbackInfo ci) {
        
        boolean useBetaColors = BETA_CONFIG.useFixedSeed;
        long worldSeed = BETA_CONFIG.fixedSeed;
        
        if (client.getServer() != null) { // Server check
           ChunkGenerator gen = client.getServer().getOverworld().getChunkManager().getChunkGenerator();
           
           if (!BETA_CONFIG.useFixedSeed && gen instanceof OldChunkGenerator && ((OldBiomeSource)gen.getBiomeSource()).isBeta()) {
               useBetaColors = true;
               worldSeed = gen.worldSeed;
           }
        }
        
        this.isOverworld = worldKey.getValue().equals(DimensionType.OVERWORLD_REGISTRY_KEY.getValue());
        this.useBetaColors = useBetaColors;
        
        ModernBeta.setBlockColorsSeed(worldSeed, useBetaColors);
    }
    
    @ModifyVariable(
        method = "method_23777",
        at = @At("HEAD"),
        index = 1
    )
    private Vec3d captureBlockPos(Vec3d pos) {
        curPos = pos;
        
        return pos;
    }
    
    @ModifyVariable(
        method = "method_23777",
        at = @At(value = "INVOKE_ASSIGN",  target = "Lnet/minecraft/util/CubicSampler;sampleColor(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/CubicSampler$RgbFetcher;)Lnet/minecraft/util/math/Vec3d;"),
        index = 6  
    )
    
    private Vec3d injectBetaSkyColor(Vec3d skyColorVec) {
        if (useBetaColors && BETA_CONFIG.renderBetaSkyColor && this.isOverworld) {
            skyColorVec = Vec3d.unpackRgb(BetaClimateSampler.getInstance().getSkyColor((int)curPos.getX(), (int)curPos.getZ()));

        }
        
        return skyColorVec;
    }
}

