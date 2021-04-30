package com.bespectacled.modernbeta.world.biome.provider;

import java.util.List;
import java.util.stream.Collectors;

import com.bespectacled.modernbeta.api.world.biome.BetaClimateResolver;
import com.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import com.bespectacled.modernbeta.api.world.biome.BiomeResolver;
import com.bespectacled.modernbeta.world.biome.beta.BetaClimateMapCustomizable;
import com.bespectacled.modernbeta.world.biome.beta.BetaClimateMap.BetaBiomeType;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class BetaBiomeProvider extends BiomeProvider implements BiomeResolver, BetaClimateResolver {
    private final BetaClimateMapCustomizable betaClimateMap;
    
    public BetaBiomeProvider(long seed, NbtCompound settings) {
        super(seed, settings);
        
        this.setSeed(seed);
        this.betaClimateMap = new BetaClimateMapCustomizable(settings);
    }

    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        double temp = this.sampleTemp(absX, absZ);
        double humid = this.sampleHumid(absX, absZ);
        
        return registry.get(betaClimateMap.getBiomeFromLookup(temp, humid, BetaBiomeType.LAND));
    }
 
    @Override
    public Biome getOceanBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;

        double temp = this.sampleTemp(absX, absZ);
        double humid = this.sampleHumid(absX, absZ);
        
        return registry.get(betaClimateMap.getBiomeFromLookup(temp, humid, BetaBiomeType.OCEAN));
    }
    
    @Override
    public Biome getBiome(Registry<Biome> registry, int x, int y, int z) {
        double temp = this.sampleTemp(x, z);
        double humid = this.sampleHumid(x, z);
        
        return registry.get(betaClimateMap.getBiomeFromLookup(temp, humid, BetaBiomeType.LAND));
    }

    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        return this.betaClimateMap.getBiomeIds().stream().map(i -> RegistryKey.of(Registry.BIOME_KEY, i)).collect(Collectors.toList());
    }

}
