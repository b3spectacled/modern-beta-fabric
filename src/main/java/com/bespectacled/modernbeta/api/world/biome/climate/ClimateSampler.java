package com.bespectacled.modernbeta.api.world.biome.climate;

public interface ClimateSampler {
    double sampleTemp(int x, int z);
    
    double sampleRain(int x, int z);
}
