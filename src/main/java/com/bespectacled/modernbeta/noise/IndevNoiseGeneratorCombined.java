package com.bespectacled.modernbeta.noise;

public final class IndevNoiseGeneratorCombined extends NoiseGenerator {
    private IndevNoiseGenerator noiseGenerator;
    private IndevNoiseGenerator noiseGenerator2;
    
    public IndevNoiseGeneratorCombined(IndevNoiseGenerator noiseGenerator2, IndevNoiseGenerator noiseGenerator3) {
        this.noiseGenerator = noiseGenerator2;
        this.noiseGenerator2 = noiseGenerator3;
    }
    
    public final double IndevNoiseGenerator(double double2, double double4) {
        return this.noiseGenerator.IndevNoiseGenerator(double2 + this.noiseGenerator2.IndevNoiseGenerator(double2, double4), double4);
    }
}
