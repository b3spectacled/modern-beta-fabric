package com.bespectacled.modernbeta.noise;

public final class OldNoiseGeneratorCombined extends OldNoiseGenerator {
    private OldNoiseGeneratorOctaves noiseGenerator;
    private OldNoiseGeneratorOctaves noiseGenerator2;
    
    public OldNoiseGeneratorCombined(OldNoiseGeneratorOctaves noiseGenerator2, OldNoiseGeneratorOctaves noiseGenerator3) {
        this.noiseGenerator = noiseGenerator2;
        this.noiseGenerator2 = noiseGenerator3;
    }
    
    public final double generateCombinedIndevNoiseOctaves(double double2, double double4) {
        return this.noiseGenerator.generateIndevNoiseOctaves(double2 + this.noiseGenerator2.generateIndevNoiseOctaves(double2, double4), double4);
    }
}
