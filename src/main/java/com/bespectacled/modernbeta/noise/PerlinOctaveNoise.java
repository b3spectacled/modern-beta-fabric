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
    
    public double[] sampleOctavesArrBeta(
        double arr[], 
        int x, int z, 
        int sizeX, int sizeZ, 
        double scaleX, double scaleZ, 
        double unused
    ) {
        return sampleOctavesArrBeta(arr, x, 10D, z, sizeX, 1, sizeZ, scaleX, 1.0D, scaleZ);
    }

    public double[] sampleOctavesArrBeta(
        double arr[], 
        double x, double y, double z, 
        int sizeX, int sizeY, int sizeZ, 
        double scaleX, double scaleY, double scaleZ
    ) {
        if (arr == null)
            arr = new double[sizeX * sizeY * sizeZ];
        else
            for (int l = 0; l < arr.length; l++) {
                arr[l] = 0.0D;
            }

        double frequency = 1.0;
        for (int i1 = 0; i1 < octaves; i1++) {
            generatorCollection[i1].samplePerlinArrBeta(
                arr, 
                x, y, z, 
                sizeX, sizeY, sizeZ, 
                scaleX * frequency, scaleY * frequency, scaleZ * frequency, 
                frequency
            );
            frequency /= 2.0;
        }

        return arr;
    }
    
    public double[] sampleOctavesArr(
        double arr[],
        double x, double y, double z, 
        int sizeX, int sizeY, int sizeZ, 
        double scaleX, double scaleY, double scaleZ
    ) {
        if (arr == null)
            arr = new double[sizeX * sizeY * sizeZ];
        else
            for (int l = 0; l < arr.length; l++) {
                arr[l] = 0.0D;
            }

        double frequency = 1.0;
        for (int i1 = 0; i1 < octaves; i1++) {
            generatorCollection[i1].samplePerlinArr(
                arr, 
                x, y, z, 
                sizeX, sizeY, sizeZ, 
                scaleX * frequency, scaleY * frequency, scaleZ * frequency, 
                frequency
            );
            frequency /= 2.0;
        }

        return arr;
    }
    
    public final double sampleOctaves(double x, double y, double z) {
        double total = 0.0;
        double frequency = 1.0;
        
        for (int i = 0; i < this.octaves; ++i) {
            total += this.generatorCollection[i].samplePerlin(x / frequency, y / frequency, z / frequency) * frequency;
            frequency *= 2.0;
        }
        
        return total;
    }
    
    public final double sampleOctaves(double x, double y) {
        double total = 0.0;
        double frequency = 1.0;
        
        for (int i = 0; i < this.octaves; ++i) {
            total += this.generatorCollection[i].samplePerlin(x / frequency, y / frequency) * frequency;
            frequency *= 2.0;
        }
        
        return total;
    }
    
}
