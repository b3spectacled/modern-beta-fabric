package com.bespectacled.modernbeta.api.world.biome;

public interface ClimateSampler {
    double sampleTemp(int x, int z);
    
    double sampleRain(int x, int z);
    
    double sampleSkyTemp(int x, int z);
}
