package com.bespectacled.modernbeta.biome;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public interface IOldBiomeSource {
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ);
    public Biome getOceanBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ);
    
    public Registry<Biome> getBiomeRegistry();
    
    public boolean generateOceans();
    public boolean isVanilla();
    public boolean isBeta();
    public boolean isSkyDim();
}
