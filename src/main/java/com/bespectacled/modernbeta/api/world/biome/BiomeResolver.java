package com.bespectacled.modernbeta.api.world.biome;

import net.minecraft.world.biome.Biome;

public interface BiomeResolver {
    /**
     * Gets a biome at given block coordinates, for purpose of surface generation.
     * 
     * @param x x-coordinate in block coordinates.
     * @param y y-coordinate in block coordinates.
     * @param z z-coordinate in block coordinates.
     * 
     * @return A biome at given block coordinates.
     */
    Biome getBiome(int x, int y, int z);
}
