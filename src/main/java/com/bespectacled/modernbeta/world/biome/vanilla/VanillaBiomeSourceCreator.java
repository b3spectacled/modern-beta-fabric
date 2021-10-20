package com.bespectacled.modernbeta.world.biome.vanilla;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;

public final class VanillaBiomeSourceCreator {
    public static VanillaBiomeSource buildLandBiomeSource(Registry<Biome> biomeRegistry, long seed) {
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
            .build();
    }
    
    public static VanillaBiomeSource buildOceanBiomeSource(Registry<Biome> biomeRegistry, long seed) {
        return new VanillaBiomeSource.Builder(biomeRegistry, seed)
            .writeOceanBiomes()
            .build();
    }
    
    public static VanillaBiomeSource buildDeepOceanBiomeSource(Registry<Biome> biomeRegistry, long seed) {
        return new VanillaBiomeSource.Builder(biomeRegistry, seed)
            .writeDeepOceanBiomes()
            .build();
    }
}
