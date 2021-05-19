package com.bespectacled.modernbeta.world.biome.provider;

import java.util.List;
import java.util.Optional;
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
    private final BetaClimateMapCustomizable defaultClimateMap;
    
    public BetaBiomeProvider(long seed, NbtCompound settings) {
        super(seed, settings);
        
        this.setSeed(seed);
        this.betaClimateMap = new BetaClimateMapCustomizable(settings);
        this.defaultClimateMap = new BetaClimateMapCustomizable(new NbtCompound());
    }

    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> biomeRegistry, int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        double temp = this.sampleTemp(absX, absZ);
        double humid = this.sampleHumid(absX, absZ);
        
        Optional<Biome> biome = biomeRegistry.getOrEmpty(this.betaClimateMap.getBiomeFromLookup(temp, humid, BetaBiomeType.LAND));

        // If custom biome is not present for whatever reason, fetch the default for the climate range.
        return biome.orElse(biomeRegistry.get(this.defaultClimateMap.getBiomeFromLookup(temp, humid, BetaBiomeType.LAND)));
    }
 
    @Override
    public Biome getOceanBiomeForNoiseGen(Registry<Biome> biomeRegistry, int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;

        double temp = this.sampleTemp(absX, absZ);
        double humid = this.sampleHumid(absX, absZ);
        
        Optional<Biome> biome = biomeRegistry.getOrEmpty(this.betaClimateMap.getBiomeFromLookup(temp, humid, BetaBiomeType.OCEAN));

        // If custom biome is not present for whatever reason, fetch the default for the climate range.
        return biome.orElse(biomeRegistry.get(this.defaultClimateMap.getBiomeFromLookup(temp, humid, BetaBiomeType.OCEAN)));
    }
    
    @Override
    public Biome getBiome(Registry<Biome> biomeRegistry, int x, int y, int z) {
        double temp = this.sampleTemp(x, z);
        double humid = this.sampleHumid(x, z);
        
        Optional<Biome> biome = biomeRegistry.getOrEmpty(this.betaClimateMap.getBiomeFromLookup(temp, humid, BetaBiomeType.LAND));

        // If custom biome is not present for whatever reason, fetch the default for the climate range.
        return biome.orElse(biomeRegistry.get(this.defaultClimateMap.getBiomeFromLookup(temp, humid, BetaBiomeType.LAND)));
    }

    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        return this.betaClimateMap.getBiomeIds().stream().map(i -> RegistryKey.of(Registry.BIOME_KEY, i)).collect(Collectors.toList());
    }

}
