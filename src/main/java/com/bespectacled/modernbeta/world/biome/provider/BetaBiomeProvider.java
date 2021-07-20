package com.bespectacled.modernbeta.world.biome.provider;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import com.bespectacled.modernbeta.api.world.biome.BiomeResolver;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.biome.beta.climate.BetaClimateMap;
import com.bespectacled.modernbeta.world.biome.beta.climate.BetaClimateResolver;
import com.bespectacled.modernbeta.world.biome.beta.climate.BetaClimateType;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class BetaBiomeProvider extends BiomeProvider implements BiomeResolver, BetaClimateResolver {
    private final BetaClimateMap climateMap;
    private final BetaClimateMap defaultClimateMap;
    
    public BetaBiomeProvider(OldBiomeSource biomeSource) {
        super(biomeSource);
        
        this.setSeed(seed);
        this.climateMap = new BetaClimateMap(settings);
        this.defaultClimateMap = new BetaClimateMap(new NbtCompound());
    }

    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> biomeRegistry, int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        double temp = this.sampleTemp(absX, absZ);
        double rain = this.sampleRain(absX, absZ);
        
        return this.getBiomeOrElse(
            biomeRegistry, 
            this.climateMap.getBiome(temp, rain, BetaClimateType.LAND), 
            this.defaultClimateMap.getBiome(temp, rain, BetaClimateType.LAND)
        );
    }
 
    @Override
    public Biome getOceanBiomeForNoiseGen(Registry<Biome> biomeRegistry, int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;

        double temp = this.sampleTemp(absX, absZ);
        double rain = this.sampleRain(absX, absZ);
        
        return this.getBiomeOrElse(
            biomeRegistry, 
            this.climateMap.getBiome(temp, rain, BetaClimateType.OCEAN), 
            this.defaultClimateMap.getBiome(temp, rain, BetaClimateType.OCEAN)
        );
    }
    
    @Override
    public Biome getBiome(Registry<Biome> biomeRegistry, int x, int y, int z) {
        double temp = this.sampleTemp(x, z);
        double rain = this.sampleRain(x, z);
        
        Optional<Biome> biome = biomeRegistry.getOrEmpty(this.climateMap.getBiome(temp, rain, BetaClimateType.LAND));

        // If custom biome is not present for whatever reason, fetch the default for the climate range.
        return biome.orElse(biomeRegistry.get(this.defaultClimateMap.getBiome(temp, rain, BetaClimateType.LAND)));
    }

    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        return this.climateMap.getBiomeIds().stream().map(i -> RegistryKey.of(Registry.BIOME_KEY, i)).collect(Collectors.toList());
    }

}
