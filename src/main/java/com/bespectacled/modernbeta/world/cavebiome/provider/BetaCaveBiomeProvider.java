package com.bespectacled.modernbeta.world.cavebiome.provider;

import java.util.List;
import java.util.stream.Collectors;

import com.bespectacled.modernbeta.api.world.biome.BiomeResolver;
import com.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import com.bespectacled.modernbeta.api.world.biome.climate.ClimateType;
import com.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
import com.bespectacled.modernbeta.world.biome.beta.climate.BetaClimateMap;
import com.bespectacled.modernbeta.world.biome.beta.climate.BetaClimateSampler;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class BetaCaveBiomeProvider extends CaveBiomeProvider implements BiomeResolver {
    private final ClimateSampler climateSampler;
    private final BetaClimateMap betaClimateMap;
    
    public BetaCaveBiomeProvider(long seed, NbtCompound settings) {
        super(seed, settings);
        
        this.climateSampler = new BetaClimateSampler(seed);
        this.betaClimateMap = new BetaClimateMap(settings);
    }

    @Override
    public Biome getBiome(Registry<Biome> biomeRegistry, int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        double temp = this.climateSampler.sampleTemp(absX, absZ);
        double rain = this.climateSampler.sampleRain(absX, absZ);
        
        return biomeRegistry.get(betaClimateMap.getBiome(temp, rain, ClimateType.LAND));
    }

    @Override
    public Biome getBiomeAtBlock(Registry<Biome> biomeRegistry, int x, int y, int z) {
        double temp = this.climateSampler.sampleTemp(x, z);
        double rain = this.climateSampler.sampleRain(x, z);
        
        return biomeRegistry.get(betaClimateMap.getBiome(temp, rain, ClimateType.LAND));
    }

    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        return this.betaClimateMap.getBiomeIds().stream().map(i -> RegistryKey.of(Registry.BIOME_KEY, i)).collect(Collectors.toList());
    }
}
