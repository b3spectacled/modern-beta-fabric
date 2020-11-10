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

    public double[] sample(double noise[], double x, double z, int sizeX, int sizeZ, double d1, double d2, double d3) {
        return generateBetaNoiseOctaves2(noise, x, z, sizeX, sizeZ, d1, d2, d3, 0.5D);
    }

    public double[] generateBetaNoiseOctaves2(
        double noise[], 
        double x, 
        double z, 
        int sizeX, 
        int sizeZ, 
        double d1, 
        double d2, 
        double d3, 
        double d4
    ) {
        d1 /= 1.5D;
        d2 /= 1.5D;
        if (noise == null || noise.length < sizeX * sizeZ) {
            noise = new double[sizeX * sizeZ];
        } else {
            for (int k = 0; k < noise.length; k++) {
                noise[k] = 0.0D;
            }

        }
        double amp1 = 1.0D;
        double amp2 = 1.0D;
        for (int l = 0; l < octaves; l++) {
            generatorCollection[l].func_4157_a(noise, x, z, sizeX, sizeZ, d1 * amp2, d2 * amp2, 0.55000000000000004D / amp1);
            amp2 *= d3;
            amp1 *= d4;
        }

        return noise;
    }
}
