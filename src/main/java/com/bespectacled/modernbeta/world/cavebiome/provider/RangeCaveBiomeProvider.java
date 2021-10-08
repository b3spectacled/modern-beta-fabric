package com.bespectacled.modernbeta.world.cavebiome.provider;

import java.util.Arrays;
import java.util.List;

import com.bespectacled.modernbeta.api.world.biome.climate.CaveClimateSampler;
import com.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
import com.bespectacled.modernbeta.world.cavebiome.climate.BaseCaveClimateSampler;
import com.google.common.collect.ImmutableList;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public class RangeCaveBiomeProvider extends CaveBiomeProvider implements CaveClimateSampler {
    private static final Identifier LUSH_CAVES = BiomeKeys.LUSH_CAVES.getValue();
    private static final Identifier DRIPSTONE_CAVES = BiomeKeys.DRIPSTONE_CAVES.getValue();
    
    private final CaveClimateSampler climateSampler;
    private final List<NoiseRange> noiseRange;
    
    public RangeCaveBiomeProvider(long seed, NbtCompound settings) {
        super(seed, settings);
        
        ImmutableList.Builder<NoiseRange> builder = new ImmutableList.Builder<>();
        builder.add(new NoiseRange(0.2, 0.8, LUSH_CAVES));
        builder.add(new NoiseRange(-0.8, -0.2, DRIPSTONE_CAVES));
        
        this.climateSampler = new BaseCaveClimateSampler(seed);
        this.noiseRange = builder.build();
        
    }

    @Override
    public Biome getBiome(Registry<Biome> biomeRegistry, int biomeX, int biomeY, int biomeZ) {
        double noise = this.climateSampler.sample(biomeX, biomeY, biomeZ);
        
        return this.sampleBiome(biomeRegistry, noise);
    }
    
    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        return Arrays.asList(
            BiomeKeys.LUSH_CAVES,
            BiomeKeys.DRIPSTONE_CAVES
        );
    }

    @Override
    public double sample(int x, int y, int z) {
        return this.climateSampler.sample(x, y, z);
    }
    
    private Biome sampleBiome(Registry<Biome> biomeRegistry, double noise) {
        for (NoiseRange range : this.noiseRange) {
            Identifier biomeId = range.getIdIfInRange(noise);
            
            if (biomeId != null)
                return biomeRegistry.get(biomeId);
        }
        
        return null;
    }

    private static class NoiseRange {
        private final double min;
        private final double max;
        private final Identifier id;
        
        private NoiseRange(double min, double max, Identifier id) {
            this.min = min;
            this.max = max;
            this.id = id;
            
            if (this.min > this.max)
                throw new IllegalArgumentException("[Modern Beta] Invalid noise ranges for: " + this.id.toString());
        }
        
        private Identifier getIdIfInRange(double noise) {
            return min <= noise && noise <= max ? this.id : null;
        }
    }
}
