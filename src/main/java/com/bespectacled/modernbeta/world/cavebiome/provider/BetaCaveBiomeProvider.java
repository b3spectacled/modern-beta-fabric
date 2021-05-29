package com.bespectacled.modernbeta.world.cavebiome.provider;

import java.util.List;
import java.util.stream.Collectors;

import com.bespectacled.modernbeta.api.world.biome.BetaClimateResolver;
import com.bespectacled.modernbeta.api.world.biome.BiomeResolver;
import com.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
import com.bespectacled.modernbeta.world.biome.beta.BetaClimateMapCustomizable;
import com.bespectacled.modernbeta.world.biome.beta.BetaClimateMap.BetaBiomeType;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class BetaCaveBiomeProvider extends CaveBiomeProvider implements BiomeResolver, BetaClimateResolver {
    private final BetaClimateMapCustomizable betaClimateMap;
    
    public BetaCaveBiomeProvider(long seed, NbtCompound settings) {
        super(seed, settings);
        
        this.setSeed(seed);
        this.betaClimateMap = new BetaClimateMapCustomizable(settings);
    }

    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> biomeRegistry, int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        double temp = this.sampleTemp(absX, absZ);
        double rain = this.sampleRain(absX, absZ);
        
        return biomeRegistry.get(betaClimateMap.getBiomeFromLookup(temp, rain, BetaBiomeType.LAND));
    }

    @Override
    public Biome getBiome(Registry<Biome> biomeRegistry, int x, int y, int z) {
        double temp = this.sampleTemp(x, z);
        double rain = this.sampleRain(x, z);
        
        return biomeRegistry.get(betaClimateMap.getBiomeFromLookup(temp, rain, BetaBiomeType.LAND));
    }

    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        return this.betaClimateMap.getBiomeIds().stream().map(i -> RegistryKey.of(Registry.BIOME_KEY, i)).collect(Collectors.toList());
    }
}
