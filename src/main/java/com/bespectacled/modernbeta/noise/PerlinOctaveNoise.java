package com.bespectacled.modernbeta.noise;

import java.util.Random;

public class PerlinOctaveNoise extends Noise {
    private PerlinNoise generatorCollection[];
    private int octaves;

    public PerlinOctaveNoise(Random random, int i, boolean isIndev) {
        octaves = i;
        generatorCollection = new PerlinNoise[i];
        for (int j = 0; j < i; j++) {
            generatorCollection[j] = new PerlinNoise(random, isIndev);
        }

    }

    public double sampleBetaOctaves(double d, double d1) {
        double d2 = 0.0D;
        double d3 = 1.0D;
        for (int i = 0; i < octaves; i++) {
            d2 += generatorCollection[i].sampleBeta(d * d3, d1 * d3) / d3;
            d3 /= 2D;
        }

        return d2;
    }

    public double[] sampleBetaOctaves(double ad[], int i, int j, int k, int l, double d, double d1, double d2) {
        return sampleBetaOctaves(ad, i, 10D, j, k, 1, l, d, 1.0D, d1);
    }
    
    public double[] sampleBetaOctaves(double ad[], double d, double d1, double d2, int i, int j, int k, double d3,
            double d4, double d5) {
        if (ad == null) {
            ad = new double[i * j * k];
        } else {
            for (int l = 0; l < ad.length; l++) {
                ad[l] = 0.0D;
            }

        }
        double d6 = 1.0D;
        for (int i1 = 0; i1 < octaves; i1++) {
            generatorCollection[i1].sampleBetaNoise(ad, d, d1, d2, i, j, k, d3 * d6, d4 * d6, d5 * d6, d6);
            d6 /= 2D;
        }

        return ad;
    }
    
    public double[] sampleAlphaOctaves(double ad[], double d, double d1, double d2, int i, int j, int k, double d3,
            double d4, double d5) {
        if (ad == null) {
            ad = new double[i * j * k];
        } else {
            for (int l = 0; l < ad.length; l++) {
                ad[l] = 0.0D;
            }

        }
        double d6 = 1.0D;
        for (int i1 = 0; i1 < octaves; i1++) {
            generatorCollection[i1].sampleAlphaNoise(ad, d, d1, d2, i, j, k, d3 * d6, d4 * d6, d5 * d6, d6);
            d6 /= 2D;
        }

        return ad;
    }
    
    public final double sampleIndevOctaves(double double2, double double4) {
        double double6 = 0.0;
        double double8 = 1.0;
        for (int i = 0; i < this.octaves; ++i) {
            double6 += this.generatorCollection[i].sampleIndevNoise(double2 * double8, double4 * double8) / double8;
            double8 /= 2.0;
        }
        return double6;
    }

    public final double sampleInfdevOctaves(double double2, double double4) {
        double double6 = 0.0;
        double double8 = 1.0;
        for (int i = 0; i < this.octaves; ++i) {
            double6 += this.generatorCollection[i].sampleInfdev(double2 / double8, double4 / double8) * double8;
            double8 *= 2.0;
        }
        return double6;
    }
    
    public final double sampleInfdevOctaves(double double2, double double4, double double6) {
        double double8 = 0.0;
        double double10 = 1.0;
        for (int i = 0; i < this.octaves; ++i) {
            double8 += this.generatorCollection[i].sampleInfdev(double2 / double10, double4 / double10, double6 / double10) * double10;
            double10 *= 2.0;
        }
        return double8;
    }
    
    public final double sampleOldInfdevOctaves(double double2, double double4, double double6) {
        double double8 = 0.0;
        double double10 = 1.0;
        for (int i = 0; i < this.octaves; ++i) {
            double8 += this.generatorCollection[i].sampleInfdev(double2 / double10, 0.0 / double10, double6 / double10) * double10;
            double10 *= 2.0;
        }
        return double8;
    }
}
