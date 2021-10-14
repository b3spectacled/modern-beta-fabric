package com.bespectacled.modernbeta.world.biome.provider;

import java.util.List;
import java.util.stream.Collectors;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import com.bespectacled.modernbeta.api.world.biome.BiomeResolver;
import com.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import com.bespectacled.modernbeta.api.world.biome.climate.ClimateType;
import com.bespectacled.modernbeta.api.world.biome.climate.SkyClimateSampler;
import com.bespectacled.modernbeta.world.biome.beta.climate.BetaClimateMap;
import com.bespectacled.modernbeta.world.biome.beta.climate.BetaClimateSampler;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class BetaBiomeProvider extends BiomeProvider implements BiomeResolver, ClimateSampler, SkyClimateSampler {
    private final BetaClimateSampler climateSampler;
    private final BetaClimateMap climateMap;
    
    public BetaBiomeProvider(long seed, NbtCompound settings, Registry<Biome> biomeRegistry) {
        super(seed, settings, biomeRegistry);
        
        this.climateSampler = new BetaClimateSampler(seed);
        this.climateMap = new BetaClimateMap(settings);
    }

    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> biomeRegistry, int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        double temp = this.climateSampler.sampleTemp(absX, absZ);
        double rain = this.climateSampler.sampleRain(absX, absZ);
        
        return biomeRegistry.get(this.climateMap.getBiome(temp, rain, ClimateType.LAND));
    }
 
    @Override
    public Biome getOceanBiomeForNoiseGen(Registry<Biome> biomeRegistry, int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;

        double temp = this.climateSampler.sampleTemp(absX, absZ);
        double rain = this.climateSampler.sampleRain(absX, absZ);
        
        return biomeRegistry.get(this.climateMap.getBiome(temp, rain, ClimateType.OCEAN));
    }
    
    @Override
    public Biome getBiome(Registry<Biome> biomeRegistry, int x, int y, int z) {
        double temp = this.climateSampler.sampleTemp(x, z);
        double rain = this.climateSampler.sampleRain(x, z);
        
        return biomeRegistry.get(this.climateMap.getBiome(temp, rain, ClimateType.LAND));
    }

    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        return this.climateMap.getBiomeIds().stream().map(i -> RegistryKey.of(Registry.BIOME_KEY, i)).collect(Collectors.toList());
    }

    @Override
    public double sampleTemp(int x, int z) {
        return this.climateSampler.sampleTemp(x, z);
    }

    @Override
    public double sampleRain(int x, int z) {
        return this.climateSampler.sampleRain(x, z);
    }

    @Override
    public double sampleSkyTemp(int x, int z) {
        return this.climateSampler.sampleSkyTemp(x, z);
    }
    
    @Override
    public boolean sampleBiomeColor() {
        return ModernBeta.RENDER_CONFIG.biomeColorConfig.renderBetaBiomeColor;
    }
    
    @Override
    public boolean sampleSkyColor() {
        return ModernBeta.RENDER_CONFIG.biomeColorConfig.renderBetaSkyColor;
    }
}
