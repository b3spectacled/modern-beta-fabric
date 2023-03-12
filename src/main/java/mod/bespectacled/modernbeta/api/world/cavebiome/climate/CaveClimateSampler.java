package mod.bespectacled.modernbeta.api.world.cavebiome.climate;

import mod.bespectacled.modernbeta.api.world.biome.climate.Clime;

public interface CaveClimateSampler {
    /**
     * Sample temperature/rainfall values in range [-1.0, 1.0] given block coordinates.
     * 
     * @param x x-coordinate in block coordinates.
     * @param y y-coordinate in block coordinates.
     * @param z z-coordinate in block coordinates.
     * 
     * @return A Clime containing temperature/rainfall values in range [-1.0, 1.0] sampled at position.
     */
    Clime sample(int x, int y, int z);
}
