package com.bespectacled.modernbeta.api.world.biome.climate;

public interface CaveClimateSampler {
    /**
     * Sample 3D noise for cave climate from given coordinates.
     * 
     * @param x
     * @param y
     * @param z
     * 
     * @return
     */
    double sample(int x, int y, int z);
}
