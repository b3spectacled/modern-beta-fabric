package com.bespectacled.modernbeta.biome.provider;

import com.bespectacled.modernbeta.util.BiomeUtil;
import com.bespectacled.modernbeta.util.WorldEnum.BiomeType;
import com.bespectacled.modernbeta.util.WorldEnum.WorldType;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class BetaBiomeProvider implements IOldBiomeProvider {
    
    private final WorldType worldType;
    private final BiomeType biomeType;
    private final boolean generateOceans;
    
    private static final double[] TEMP_HUMID_POINT = new double[2];
    
    public BetaBiomeProvider(long seed, WorldType worldType, BiomeType biomeType) {
        this.worldType = worldType;
        this.biomeType = biomeType;
        this.generateOceans = false;
        
        BiomeUtil.setSeed(seed);
    }

    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        BiomeUtil.fetchTempHumidAtPoint(TEMP_HUMID_POINT, absX, absZ);
        
        // return BetaBiomes.fetchBiome(TEMP_HUMID_POINT[0], TEMP_HUMID_POINT[1], BiomeType.LAND);
        
        return null;
    }

    @Override
    public Biome getOceanBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        BiomeUtil.fetchTempHumidAtPoint(TEMP_HUMID_POINT, absX, absZ);

        /*
        if (this.generateOceans)
            return fetchBiome(TEMP_HUMID_POINT[0], TEMP_HUMID_POINT[1], BiomeType.OCEAN);
        else
            return fetchBiome(TEMP_HUMID_POINT[0], TEMP_HUMID_POINT[1], BiomeType.LAND);
        */
        
        
        // TODO Auto-generated method stub
        return null;
    }

}
