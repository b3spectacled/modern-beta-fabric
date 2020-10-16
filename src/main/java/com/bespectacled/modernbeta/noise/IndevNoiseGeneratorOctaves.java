package com.bespectacled.modernbeta.noise;

import java.util.Random;

public final class IndevNoiseGeneratorOctaves extends IndevNoiseGenerator {
    private IndevNoiseGeneratorPerlin[] generatorCollection;
    private int octaves;
    
    public IndevNoiseGeneratorOctaves(Random random, int integer) {
        this.octaves = integer;
        this.generatorCollection = new IndevNoiseGeneratorPerlin[integer];
        for (int i = 0; i < integer; ++i) {
            this.generatorCollection[i] = new IndevNoiseGeneratorPerlin(random);
        }
    }
    
    @Override
    public final double IndevNoiseGenerator(double double2, double double4) {
        double double6 = 0.0;
        double double8 = 1.0;
        for (int i = 0; i < this.octaves; ++i) {
            double6 += this.generatorCollection[i].IndevNoiseGenerator(double2 / double8, double4 / double8) * double8;
            double8 *= 2.0;
        }
        return double6;
    }
}
