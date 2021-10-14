package com.bespectacled.modernbeta.world.biome.provider.climate;

import com.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import net.minecraft.world.biome.source.HorizontalVoronoiBiomeAccessType;

public class VanillaClimateSampler implements ClimateSampler, BiomeAccess.Storage {
    private final long seed;
    private final BiomeLayerSampler biomeSampler;
    private final Registry<Biome> biomeRegistry;
    
    public VanillaClimateSampler(long seed, BiomeLayerSampler biomeSampler, Registry<Biome> biomeRegistry) {
        this.seed = seed;
        this.biomeSampler = biomeSampler;
        this.biomeRegistry = biomeRegistry;
    }
    
    @Override
    public double sampleTemp(int x, int z) {
        return HorizontalVoronoiBiomeAccessType.INSTANCE.getBiome(this.seed, x, 0, z, this).getTemperature();
    }

    @Override
    public double sampleRain(int x, int z) {
        return HorizontalVoronoiBiomeAccessType.INSTANCE.getBiome(this.seed, x, 0, z, this).getDownfall();
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.biomeSampler.sample(biomeRegistry, biomeX, biomeZ);
    }
    
    public BiomeLayerSampler getBiomeSampler() {
        return this.biomeSampler;
    }
}
