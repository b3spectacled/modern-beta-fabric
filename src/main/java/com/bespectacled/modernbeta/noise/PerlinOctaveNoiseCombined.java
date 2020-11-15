package com.bespectacled.modernbeta.noise;

public final class PerlinOctaveNoiseCombined extends Noise {
    private PerlinOctaveNoise noiseGenerator;
    private PerlinOctaveNoise noiseGenerator2;
    
    public PerlinOctaveNoiseCombined(PerlinOctaveNoise noiseGenerator2, PerlinOctaveNoise noiseGenerator3) {
        this.noiseGenerator = noiseGenerator2;
        this.noiseGenerator2 = noiseGenerator3;
    }
    
    public final double sampleIndevOctavesCombined(double double2, double double4) {
        return this.noiseGenerator.sampleIndevOctaves(double2 + this.noiseGenerator2.sampleIndevOctaves(double2, double4), double4);
    }
}
