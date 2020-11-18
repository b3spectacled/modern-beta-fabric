package com.bespectacled.modernbeta.biome;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public interface IOldBiomeSource {
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ);
    public Biome getOceanBiomeForNoiseGen(int biomeX, int biomeZ);
    
    public Registry<Biome> getBiomeRegistry();
    public boolean generateVanillaBiomes();
    public boolean isSkyDim();
}
