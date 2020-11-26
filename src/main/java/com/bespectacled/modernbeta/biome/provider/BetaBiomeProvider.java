package com.bespectacled.modernbeta.biome.provider;

import com.bespectacled.modernbeta.biome.BetaBiomes;
import com.bespectacled.modernbeta.biome.BetaBiomes.BiomeProviderType;
import com.bespectacled.modernbeta.util.BiomeUtil;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class BetaBiomeProvider extends AbstractBiomeProvider {
    
    private static final double[] TEMP_HUMID_POINT = new double[2];
   
    public BetaBiomeProvider(long seed) {
        BiomeUtil.setSeed(seed);
    }

    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        BiomeUtil.fetchTempHumidAtPoint(TEMP_HUMID_POINT, absX, absZ);
        return registry.get(BetaBiomes.getBiomeFromLookup(TEMP_HUMID_POINT[0], TEMP_HUMID_POINT[1], BiomeProviderType.LAND));
    }

    @Override
    public Biome getOceanBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        BiomeUtil.fetchTempHumidAtPoint(TEMP_HUMID_POINT, absX, absZ);
        return registry.get(BetaBiomes.getBiomeFromLookup(TEMP_HUMID_POINT[0], TEMP_HUMID_POINT[1], BiomeProviderType.OCEAN));
    }

}
