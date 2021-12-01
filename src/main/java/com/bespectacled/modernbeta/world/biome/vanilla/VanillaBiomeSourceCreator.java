package com.bespectacled.modernbeta.world.biome.vanilla;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;

public final class VanillaBiomeSourceCreator {
    public static VanillaBiomeSource buildLandBiomeSource(Registry<Biome> biomeRegistry, long seed, NbtCompound settings) {
        return new VanillaBiomeSource.Builder(biomeRegistry, seed)
            .writeMixedBiomes(MultiNoiseUtil.ParameterRange.of(-1.0f, -0.93333334f))
            .writePlainsBiomes(MultiNoiseUtil.ParameterRange.of(-0.93333334f, -0.7666667f))
            .writeMountainousBiomes(MultiNoiseUtil.ParameterRange.of(-0.7666667f, -0.56666666f))
            .writePlainsBiomes(MultiNoiseUtil.ParameterRange.of(-0.56666666f, -0.4f))
            .writeMixedBiomes(MultiNoiseUtil.ParameterRange.of(-0.4f, -0.4f))
            .writePlainsBiomes(MultiNoiseUtil.ParameterRange.of(0.4f, 0.56666666f))
            .writeMountainousBiomes(MultiNoiseUtil.ParameterRange.of(0.56666666f, 0.7666667f))
            .writePlainsBiomes(MultiNoiseUtil.ParameterRange.of(0.7666667f, 0.93333334f))
            .writeMixedBiomes(MultiNoiseUtil.ParameterRange.of(0.93333334f, 1.0f))
            .largeBiomes(useLargeBiomes(settings))
            .build();
    }
    
    public static VanillaBiomeSource buildOceanBiomeSource(Registry<Biome> biomeRegistry, long seed, NbtCompound settings) {
        return new VanillaBiomeSource.Builder(biomeRegistry, seed)
            .writeOceanBiomes()
            .largeBiomes(useLargeBiomes(settings))
            .build();
    }
    
    public static VanillaBiomeSource buildDeepOceanBiomeSource(Registry<Biome> biomeRegistry, long seed, NbtCompound settings) {
        return new VanillaBiomeSource.Builder(biomeRegistry, seed)
            .writeDeepOceanBiomes()
            .largeBiomes(useLargeBiomes(settings))
            .build();
    }
    
    private static boolean useLargeBiomes(NbtCompound settings) {
        return NbtUtil.readBoolean(NbtTags.VANILLA_LARGE_BIOMES, settings, ModernBeta.BIOME_CONFIG.vanillaLargeBiomes);
    }
}
