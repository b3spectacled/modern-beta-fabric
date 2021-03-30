package com.bespectacled.modernbeta.biome.provider;

import java.util.List;
import java.util.stream.Collectors;

import com.bespectacled.modernbeta.api.biome.AbstractBiomeProvider;
import com.bespectacled.modernbeta.api.biome.IBiomeResolver;
import com.bespectacled.modernbeta.biome.beta.BetaClimateMap.BetaBiomeType;
import com.bespectacled.modernbeta.biome.beta.BetaClimateMapCustomizable;
import com.bespectacled.modernbeta.biome.beta.BetaClimateSampler;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class BetaBiomeProvider extends AbstractBiomeProvider implements IBiomeResolver {
    private static final double[] TEMP_HUMID_POINT = new double[2];
    private final BetaClimateMapCustomizable betaClimateMap;
    
    public BetaBiomeProvider(long seed, NbtCompound settings) {
        super(seed, settings);
        
        BetaClimateSampler.INSTANCE.setSeed(seed);
        this.betaClimateMap = new BetaClimateMapCustomizable(settings);
    }

    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        //int cutoffBiomeY = 128 >> 2;
        
        BetaClimateSampler.INSTANCE.sampleTempHumid(TEMP_HUMID_POINT, absX, absZ);
        return registry.get(betaClimateMap.getBiomeFromLookup(TEMP_HUMID_POINT[0], TEMP_HUMID_POINT[1], BetaBiomeType.LAND));
    }
 
    @Override
    public Biome getOceanBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        BetaClimateSampler.INSTANCE.sampleTempHumid(TEMP_HUMID_POINT, absX, absZ);
        return registry.get(betaClimateMap.getBiomeFromLookup(TEMP_HUMID_POINT[0], TEMP_HUMID_POINT[1], BetaBiomeType.OCEAN));
    }
    
    @Override
    public Biome getBiomeForSurfaceGen(Registry<Biome> registry, int x, int y, int z) {
        BetaClimateSampler.INSTANCE.sampleTempHumid(TEMP_HUMID_POINT, x, z);
        return registry.get(betaClimateMap.getBiomeFromLookup(TEMP_HUMID_POINT[0], TEMP_HUMID_POINT[1], BetaBiomeType.LAND));
    }

    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        return this.betaClimateMap.getBiomeIds().stream().map(i -> RegistryKey.of(Registry.BIOME_KEY, i)).collect(Collectors.toList());
    }

}
