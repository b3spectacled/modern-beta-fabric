package com.bespectacled.modernbeta.noise;

import java.util.Random;

public class PerlinOctaveNoise extends Noise {
    private final PerlinNoise generatorCollection[];
    private final int octaves;
    
    public PerlinOctaveNoise(Random random, int octaves, boolean useOffset) {
        this.octaves = octaves;
        this.generatorCollection = new PerlinNoise[octaves];
        
        for (int j = 0; j < octaves; j++) {
            this.generatorCollection[j] = new PerlinNoise(random, useOffset);
        }
    }
    
    public double[] sampleArrBeta(
        double arr[], 
        int x, int z, 
        int sizeX, int sizeZ, 
        double scaleX, double scaleZ, 
        double unused
    ) {
        return sampleArrBeta(arr, x, 10D, z, sizeX, 1, sizeZ, scaleX, 1.0D, scaleZ);
    }

    public double[] sampleArrBeta(
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
            this.generatorCollection[i1].samplePerlinArrBeta(
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
    
    public double[] sampleArr(
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
            this.generatorCollection[i1].samplePerlinArr(
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
    
    public final double sample(double x, double y, double z) {
        double total = 0.0;
        double frequency = 1.0;
        
        for (int i = 0; i < this.octaves; ++i) {
            total += this.generatorCollection[i].samplePerlin(x / frequency, y / frequency, z / frequency) * frequency;
            frequency *= 2.0;
        }
        
        return total;
    }
    
    public final double sample(double x, double y) {
        double total = 0.0;
        double frequency = 1.0;
        
        for (int i = 0; i < this.octaves; ++i) {
            total += this.generatorCollection[i].samplePerlin(x / frequency, y / frequency) * frequency;
            frequency *= 2.0;
        }
        
        return total;
    }
}
