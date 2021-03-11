package com.bespectacled.modernbeta.biome.source;

import net.minecraft.world.biome.Biome;

public interface IOldBiomeSource {
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ);
    
    default Biome getOceanBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.getBiomeForNoiseGen(biomeX, biomeY, biomeZ);
    }
}
