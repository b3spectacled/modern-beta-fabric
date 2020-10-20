package com.bespectacled.modernbeta.noise;

import java.util.Random;

public class OldNoiseGeneratorOctaves2 extends OldNoiseGenerator {
    private OldNoiseGenerator2 generatorCollection[];
    private int octaves;

    public OldNoiseGeneratorOctaves2(Random random, int i) {
        octaves = i;
        generatorCollection = new OldNoiseGenerator2[i];
        for (int j = 0; j < i; j++) {
            generatorCollection[j] = new OldNoiseGenerator2(random);
        }

    }

    public double[] func_4112_a(double ad[], double d, double d1, int i, int j, double d2, double d3, double d4) {
        return generateBetaNoiseOctaves2(ad, d, d1, i, j, d2, d3, d4, 0.5D);
    }

    public double[] generateBetaNoiseOctaves2(double ad[], double d, double d1, int i, int j, double d2, double d3, double d4,
            double d5) {
        d2 /= 1.5D;
        d3 /= 1.5D;
        if (ad == null || ad.length < i * j) {
            ad = new double[i * j];
        } else {
            for (int k = 0; k < ad.length; k++) {
                ad[k] = 0.0D;
            }

        }
        double d6 = 1.0D;
        double d7 = 1.0D;
        for (int l = 0; l < octaves; l++) {
            generatorCollection[l].func_4157_a(ad, d, d1, i, j, d2 * d7, d3 * d7, 0.55000000000000004D / d6);
            d7 *= d4;
            d6 *= d5;
        }

        return ad;
    }
}
