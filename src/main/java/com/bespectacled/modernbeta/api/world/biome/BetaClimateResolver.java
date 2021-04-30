package com.bespectacled.modernbeta.api.world.biome;

public interface BetaClimateResolver {
    default void setSeed(long seed) {
        BetaClimateSampler.INSTANCE.setSeed(seed);
    }
    
    default double sampleTemp(int x, int z) {
        return BetaClimateSampler.INSTANCE.sampleTemp(x, z);
    }
    
    default double sampleHumid(int x, int z) {
        return BetaClimateSampler.INSTANCE.sampleHumid(x, z);
    }
    
    default double sampleSkyTemp(int x, int z) {
        return BetaClimateSampler.INSTANCE.sampleSkyTemp(x, z);
    }
    
    default int sampleSkyColor(int x, int z) {
        return BetaClimateSampler.INSTANCE.sampleSkyColor(x, z);
    }
}
