package com.bespectacled.modernbeta.biome;

import net.minecraft.world.biome.Biome;

public interface IOldBiomeSource {
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ);
    public Biome getOceanBiomeForNoiseGen(int biomeX, int biomeZ);
    
    public boolean generateVanillaBiomes();
}
