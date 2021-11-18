package com.bespectacled.modernbeta.util.noise;

public final class PerlinOctaveNoiseCombined {
    private PerlinOctaveNoise noiseGenerator0;
    private PerlinOctaveNoise noiseGenerator1;
    
    public PerlinOctaveNoiseCombined(PerlinOctaveNoise noiseGenerator0, PerlinOctaveNoise noiseGenerator1) {
        this.noiseGenerator0 = noiseGenerator0;
        this.noiseGenerator1 = noiseGenerator1;
    }
    
    public final double sample(double x, double y) {
        return this.noiseGenerator0.sample(x + this.noiseGenerator1.sample(x, y), y);
    }
}
