package com.bespectacled.modernbeta.noise;

import java.util.Random;

public class SimplexOctaveNoise extends Noise {
    private SimplexNoise[] generators;
    private int octaves;
    
    public SimplexOctaveNoise(Random random, int integer) {
        this.octaves = integer;
        this.generators = new SimplexNoise[integer];
        for (int i = 0; i < integer; ++i) {
            this.generators[i] = new SimplexNoise(random);
        }
    }
    
    public double[] sample(double[] arr, double x, double z, int sizeX, int sizeZ, double double9, double double11, double double13) {
        return this.sample(arr, x, z, sizeX, sizeZ, double9, double11, double13, 0.5);
    }
    
    public double[] sample(double[] arr, double x, double z, int sizeX, int sizeZ, double double9, double double11, double double13, double double15) {
        double9 /= 1.5;
        double11 /= 1.5;
        if (arr == null || arr.length < sizeX * sizeZ) {
            arr = new double[sizeX * sizeZ];
        }
        else {
            for (int i = 0; i < arr.length; ++i) {
                arr[i] = 0.0;
            }
        }
        double double17 = 1.0;
        double double19 = 1.0;
        for (int j = 0; j < this.octaves; ++j) {
            this.generators[j].sample(arr, x, z, sizeX, sizeZ, double9 * double19, double11 * double19, 0.55 / double17);
            double19 *= double13;
            double17 *= double15;
        }
        return arr;
    }
}
