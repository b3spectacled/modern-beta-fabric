package com.bespectacled.modernbeta.world.cavebiome.provider;

import java.util.List;

import com.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
import com.bespectacled.modernbeta.api.world.cavebiome.climate.CaveClimateSampler;
import com.bespectacled.modernbeta.api.world.cavebiome.climate.ClimateNoiseRule;
import com.bespectacled.modernbeta.api.world.cavebiome.climate.ClimateNoiseRules;
import com.bespectacled.modernbeta.world.cavebiome.provider.climate.BaseCaveClimateSampler;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public class NoiseCaveBiomeProvider extends CaveBiomeProvider implements CaveClimateSampler {
    private static final Identifier LUSH_CAVES = BiomeKeys.LUSH_CAVES.getValue();
    private static final Identifier DRIPSTONE_CAVES = BiomeKeys.DRIPSTONE_CAVES.getValue();
    
    private final CaveClimateSampler climateSampler;
    private final ClimateNoiseRules noiseRanges;
    
    public NoiseCaveBiomeProvider(long seed, NbtCompound settings, Registry<Biome> biomeRegistry) {
        super(seed, settings, biomeRegistry);
        
        this.climateSampler = new BaseCaveClimateSampler(seed, 4, 32);
        this.noiseRanges = new ClimateNoiseRules.Builder()
            .add(new ClimateNoiseRule(0.4, 0.8, LUSH_CAVES))
            .add(new ClimateNoiseRule(-0.8, -0.4, DRIPSTONE_CAVES))
            .build();
    }

    @Override
    public Biome getBiome(int biomeX, int biomeY, int biomeZ) {
        double noise = this.climateSampler.sample(biomeX, biomeY, biomeZ);
        
        return this.biomeRegistry.getOrEmpty(this.noiseRanges.sample(noise)).orElse(null);
    }
    
    @Override
    public List<Biome> getBiomesForRegistry() {
        return List.of(
            this.biomeRegistry.get(LUSH_CAVES),
            this.biomeRegistry.get(DRIPSTONE_CAVES)
        );
    }

    @Override
    public double sample(int x, int y, int z) {
        return this.climateSampler.sample(x, y, z);
    }
}
