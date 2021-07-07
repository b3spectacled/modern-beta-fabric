package com.bespectacled.modernbeta.noise;

import java.util.Random;

import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;

public class PerlinOctaveNoise extends Noise {
    private final PerlinNoise generatorCollection[];
    private final int octaves;
    private final boolean maintainPrecision;
    
    public PerlinOctaveNoise(Random random, int octaves, boolean useOffset) {
        this.octaves = octaves;
        this.generatorCollection = new PerlinNoise[octaves];
        this.maintainPrecision = false;
        
        for (int j = 0; j < octaves; j++) {
            this.generatorCollection[j] = new PerlinNoise(random, useOffset);
        }
    }
    
    public final double testSample(double x, double z, double scaleX, double scaleZ) {
        double frequency = 1.0;
        
        return this.generatorCollection[0].sample2D(x * scaleX * frequency, z * scaleZ * frequency, frequency);
    }
    
    /*
     * Beta 2D array noise sampler.
     */
    public double[] sampleArrBeta(
        double arr[], 
        int x, int z, 
        int sizeX, int sizeZ, 
        double scaleX, double scaleZ, 
        double unused
    ) {
        return sampleArrBeta(arr, x, 10D, z, sizeX, 1, sizeZ, scaleX, 1.0D, scaleZ);
    }

    /*
     * Beta 3D array noise sampler.
     */
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
            this.generatorCollection[i1].sampleArrBeta(
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
    
    /*
     * Alpha array noise sampler.
     */
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
            this.generatorCollection[i1].sampleArr(
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
    
    /*
     * Standard 2D Perlin noise sampler.
     */
    public final double sample(double x, double y) {
        double total = 0.0;
        double frequency = 1.0;
        
        for (int i = 0; i < this.octaves; ++i) {
            total += this.generatorCollection[i].sample(x / frequency, y / frequency) * frequency;
            frequency *= 2.0;
        }
        
        return total;
    }
    
    /*
     * Standard 3D Perlin noise sampler.
     */
    public final double sample(double x, double y, double z) {
        double total = 0.0;
        double frequency = 1.0;
        
        for (int i = 0; i < this.octaves; ++i) {
            total += this.generatorCollection[i].sample(x / frequency, y / frequency, z / frequency) * frequency;
            frequency *= 2.0;
        }
        
        return total;
    }
    
    /*
     * Beta 2D noise sampler.
     */
    public final double sample(double x, double z, double scaleX, double scaleZ) {
        double total = 0.0;
        double frequency = 1.0;
        
        for (int i = 0; i < this.octaves; ++i) {
            total += this.generatorCollection[i].sample2D(
                this.maintainPrecision(x * scaleX * frequency), 
                this.maintainPrecision(z * scaleZ * frequency), 
                frequency
            );
            frequency /= 2.0;
        }
        
        return total;
    }

    /*
     * Alpha/Beta 3D noise sampler.
     */
    public final double sample(double x, double y, double z, double scaleX, double scaleY, double scaleZ) {
        double total = 0.0;
        double frequency = 1.0;
        
        for (int i = 0; i < this.octaves; ++i) {
            double frequencyY = scaleY * frequency;
            
            total += this.generatorCollection[i].sample3D(
                this.maintainPrecision(x * scaleX * frequency), 
                this.maintainPrecision(y * scaleY * frequency), 
                this.maintainPrecision(z * scaleZ * frequency), 
                frequencyY, 
                y * frequencyY
            ) / frequency;
            
            frequency /= 2.0;
        }

        return total;
    }
    
    private double maintainPrecision(double value) {
        if (this.maintainPrecision) 
            value = OctavePerlinNoiseSampler.maintainPrecision(value);
        
        return value;
    }
}
