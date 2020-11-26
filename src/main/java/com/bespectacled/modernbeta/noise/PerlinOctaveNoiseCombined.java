package com.bespectacled.modernbeta.noise;

public final class PerlinOctaveNoiseCombined extends Noise {
    private PerlinOctaveNoise noiseGenerator;
    private PerlinOctaveNoise noiseGenerator2;
    
    public PerlinOctaveNoiseCombined(PerlinOctaveNoise noiseGenerator, PerlinOctaveNoise noiseGenerator2) {
        this.noiseGenerator = noiseGenerator;
        this.noiseGenerator2 = noiseGenerator2;
    }
    
    public final double sampleIndevOctavesCombined(double x, double y) {
        return this.noiseGenerator.sampleOctaves(x + this.noiseGenerator2.sampleOctaves(x, y), y);
    }
}
