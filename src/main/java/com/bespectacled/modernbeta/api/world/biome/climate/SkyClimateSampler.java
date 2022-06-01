package com.bespectacled.modernbeta.api.world.biome.climate;

public interface SkyClimateSampler {
    /**
     * Sample temperature value from given coordinates, 
     * to use to provide sky color.
     * 
     * @param x x-coordinate in block coordinates.
     * @param z z-coordinate in block coordinates.
     * 
     * @return A temperature value sampled at position.
     */
    double sampleSkyTemp(int x, int z);
    
    /**
     * Indicate to client world mixin whether to sample climate values for sky color.
     * 
     * @return Supplier for whether to use climate values for biome tinting.
     */
    default boolean sampleSkyColor() {
        return false;
    }
}
