package com.bespectacled.modernbeta.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.BiomeColorCache;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.ColorResolver;

import com.bespectacled.modernbeta.util.BiomeMath;
import com.bespectacled.modernbeta.util.MathHelper;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.config.ModernBetaConfig;
import com.bespectacled.modernbeta.config.ModernBetaConfigOld;
import com.bespectacled.modernbeta.noise.BetaNoiseGeneratorOctaves2;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
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
    private BlockPos curBlockPos = null;
    
    @Unique
    private ModernBetaConfig BETA_CONFIG = ModernBeta.BETA_CONFIG;

    private MixinClientWorld() {
        super(null, null, null, null, false, false, 0L);
    }
    
    @Unique
    private static void setSeed(long seed) {
        BiomeMath.setSeed(seed);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(ClientPlayNetworkHandler netHandler, ClientWorld.Properties properties,
            RegistryKey<World> worldKey, DimensionType dimensionType, int loadDistance, Supplier<Profiler> profiler,
            WorldRenderer renderer, boolean debugWorld, long seed, CallbackInfo ci) {
        long skySeed = BETA_CONFIG.fixedSeed == 0L ? ModernBeta.SEED : BETA_CONFIG.fixedSeed;
        setSeed(skySeed);
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
        if (BETA_CONFIG.renderBetaSkyColor && this.getDimension().hasSkyLight() && this.curBlockPos != null) {
            skyColor = getBetaSkyColor(curBlockPos);
        }
        
        return skyColor;
    }

    private int getBetaSkyColor(BlockPos pos) {
        int x = pos.getX();
        int z = pos.getZ();

        float temp = (float) BiomeMath.fetchSkyTemp(x, z);
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
        return java.awt.Color.getHSBColor(0.6222222F - temp * 0.05F, 0.5F + temp * 0.1F, 1.0F).getRGB();
    }

}
