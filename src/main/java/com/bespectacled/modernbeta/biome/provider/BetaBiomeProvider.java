package com.bespectacled.modernbeta.biome.provider;

import java.util.List;

import com.bespectacled.modernbeta.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.biome.beta.BetaClimateMap;
import com.bespectacled.modernbeta.biome.beta.BetaClimateMap.BetaBiomeType;
import com.bespectacled.modernbeta.biome.beta.BetaClimateSampler;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class BetaBiomeProvider extends AbstractBiomeProvider {
    private static final double[] TEMP_HUMID_POINT = new double[2];
    
    private final BetaBiomeType betaBiomeType;
   
    public BetaBiomeProvider(long seed, BetaBiomeType betaBiomeType) {
        this.betaBiomeType = betaBiomeType;
        
        BetaClimateSampler.INSTANCE.setSeed(seed);
    }

    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        //int cutoffBiomeY = 128 >> 2;
        
        BetaClimateSampler.INSTANCE.sampleTempHumid(TEMP_HUMID_POINT, absX, absZ);
        return registry.get(BetaClimateMap.getBiomeFromLookup(TEMP_HUMID_POINT[0], TEMP_HUMID_POINT[1], this.betaBiomeType));
    }
 
    @Override
    public Biome getOceanBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        BetaClimateSampler.INSTANCE.sampleTempHumid(TEMP_HUMID_POINT, absX, absZ);
        return registry.get(BetaClimateMap.getBiomeFromLookup(TEMP_HUMID_POINT[0], TEMP_HUMID_POINT[1], BetaBiomeType.OCEAN));
    }
    
    @Override
    public Biome getBiomeForSurfaceGen(Registry<Biome> registry, int x, int y, int z) {
        BetaClimateSampler.INSTANCE.sampleTempHumid(TEMP_HUMID_POINT, x, z);
        return registry.get(BetaClimateMap.getBiomeFromLookup(TEMP_HUMID_POINT[0], TEMP_HUMID_POINT[1], this.betaBiomeType));
    }

    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        return BetaBiomes.BIOME_KEYS;
    }

}
