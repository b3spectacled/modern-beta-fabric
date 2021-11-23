package com.bespectacled.modernbeta.api.world.biome.climate;

/**
 * Implemented by a climate sampler to provide temperatures and rainfall values,
 * for use by a biome provider or chunk provider.
 *
 */
public interface ClimateSampler {
    /**
     * Sample temperature/rainfall values in range [0.0, 1.0] given block coordinates.
     * 
     * @param x x-coordinate in block coordinates.
     * @param z z-coordinate in block coordinates.
     * 
     * @return A Clime containing temperature/rainfall values in range [0.0, 1.0] sampled at position.
     */
    Clime sampleClime(int x, int z);
    
    /**
     * Indicate to block colors whether to sample climate values for biome tinting.
     * 
     * @return Supplier for whether to use climate values for biome tinting.
     */
    default boolean sampleBiomeColor() {
        return false;
    }
}
