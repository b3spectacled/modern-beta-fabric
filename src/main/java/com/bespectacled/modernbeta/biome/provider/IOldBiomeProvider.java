package com.bespectacled.modernbeta.biome.provider;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public interface IOldBiomeProvider {
    public Biome getBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ);
    public Biome getOceanBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ);
}
