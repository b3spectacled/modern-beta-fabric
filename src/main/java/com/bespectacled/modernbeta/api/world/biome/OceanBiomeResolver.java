package com.bespectacled.modernbeta.api.world.biome;

import net.minecraft.world.biome.Biome;

public interface OceanBiomeResolver {
    
    /**
     * Gets an ocean biome to overwrite the original biome at given biome coordinates and sufficient depth.
     * 
     * @param biomeX x-coordinate in biome coordinates.
     * @param biomeY y-coordinate in biome coordinates.
     * @param biomeZ z-coordinate in biome coordinates.
     * 
     * @return A biome at given biome coordinates. May return null, in which case original biome is not replaced.
     */
    Biome getOceanBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ);
    
    /**
     * Gets a deep ocean biome to overwrite the original biome at given biome coordinates and sufficient depth.
     * 
     * @param biomeX x-coordinate in biome coordinates.
     * @param biomeY y-coordinate in biome coordinates.
     * @param biomeZ z-coordinate in biome coordinates.
     * 
     * @return A biome at given biome coordinates. May return null, in which case original biome is not replaced.
     */
    default Biome getDeepOceanBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.getOceanBiomeForNoiseGen(biomeX, biomeY, biomeZ);
    }
}
