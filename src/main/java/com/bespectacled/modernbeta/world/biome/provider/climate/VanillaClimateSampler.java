package com.bespectacled.modernbeta.world.biome.provider.climate;

import com.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import com.bespectacled.modernbeta.api.world.biome.climate.Clime;

import net.minecraft.util.math.MathHelper;
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
    public Clime sampleClime(int x, int z) {
        Biome biome = HorizontalVoronoiBiomeAccessType.INSTANCE.getBiome(this.seed, x, 0, z, this);
        double temp = MathHelper.clamp(biome.getTemperature(), 0.0, 1.0);
        double rain = MathHelper.clamp(biome.getDownfall(), 0.0, 1.0);
        
        return new Clime(temp, rain);
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.biomeSampler.sample(biomeRegistry, biomeX, biomeZ);
    }
    
    public BiomeLayerSampler getBiomeSampler() {
        return this.biomeSampler;
    }
}
