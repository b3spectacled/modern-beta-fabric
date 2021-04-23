package com.bespectacled.modernbeta.api;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public interface BiomeResolver {
    /**
     * Gets a biome at given block coordinates, for purpose of surface generation.
     * 
     * @param biomeRegistry
     * @param x x-coordinate in block coordinates.
     * @param y y-coordinate in block coordinates.
     * @param z z-coordinate in block coordinates.
     * 
     * @return A biome at given block coordinates.
     */
    public Biome getBiome(Registry<Biome> biomeRegistry, int x, int y, int z);
}
