package com.bespectacled.modernbeta.api.world.biome;

/**
 * Exposes Beta climate sampler as a 'trait' to be implemented by whatever needs it,
 * without exposing the climate sampler singleton completely.
 * 
 * Most important motivation is to allow easy type check when implemented by biome provider
 * and checked by client world mixin to initialize biome colors.
 */
public interface BetaClimateResolver {
    default void setSeed(long seed) {
        BetaClimateSampler.INSTANCE.setSeed(seed);
    }
    
    default double sampleTemp(int x, int z) {
        return BetaClimateSampler.INSTANCE.sampleTemp(x, z);
    }
    
    default double sampleRain(int x, int z) {
        return BetaClimateSampler.INSTANCE.sampleRain(x, z);
    }
    
    default double sampleSkyTemp(int x, int z) {
        return BetaClimateSampler.INSTANCE.sampleSkyTemp(x, z);
    }
    
    default int sampleSkyColor(int x, int z) {
        return BetaClimateSampler.INSTANCE.sampleSkyColor(x, z);
    }
}
