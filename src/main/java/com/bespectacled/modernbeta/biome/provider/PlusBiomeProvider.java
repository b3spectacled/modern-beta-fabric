package com.bespectacled.modernbeta.biome.provider;

import java.util.Map;

import com.bespectacled.modernbeta.biome.InfBiomes;
import com.bespectacled.modernbeta.util.BiomeUtil;
import com.bespectacled.modernbeta.util.WorldEnum.BiomeType;
import com.bespectacled.modernbeta.util.WorldEnum.WorldType;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class PlusBiomeProvider implements IOldBiomeProvider {
    
    private final Map<BiomeType, Identifier> biomeMapping;
    
    private static final double[] TEMP_HUMID_POINT = new double[2];
    
    public PlusBiomeProvider(long seed, Map<BiomeType, Identifier> biomeMapping) {
        this.biomeMapping = biomeMapping;
        BiomeUtil.setSeed(seed);
    }

    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        BiomeUtil.fetchTempHumidAtPoint(TEMP_HUMID_POINT, absX, absZ);
        return TEMP_HUMID_POINT[0] < 0.5f ?  registry.get(biomeMapping.get(BiomeType.WINTER)) : registry.get(biomeMapping.get(BiomeType.CLASSIC));
    }

    @Override
    public Biome getOceanBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        return this.getBiomeForNoiseGen(registry, biomeX, biomeY, biomeZ);
    }

}
