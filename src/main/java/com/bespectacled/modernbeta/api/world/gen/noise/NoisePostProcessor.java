package com.bespectacled.modernbeta.api.world.gen.noise;

public interface NoisePostProcessor {
    double sample(double noise, int noiseX, int noiseY, int noiseZ);
}
