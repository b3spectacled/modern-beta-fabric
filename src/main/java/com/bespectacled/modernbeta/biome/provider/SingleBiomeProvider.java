package com.bespectacled.modernbeta.biome.provider;

import com.bespectacled.modernbeta.util.BiomeUtil;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class SingleBiomeProvider implements IOldBiomeProvider {
    
    private final Identifier biomeId;
    
    public SingleBiomeProvider(long seed, Identifier biomeId) {
        this.biomeId = biomeId;
        BiomeUtil.setSeed(seed);
    }

    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        return registry.get(biomeId);
    }

    @Override
    public Biome getOceanBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        return registry.get(biomeId);
    }
    
}
