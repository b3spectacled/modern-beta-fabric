package com.bespectacled.modernbeta.noise;

import java.util.Random;

/* 
 * Used for additional reference: https://www.reddit.com/r/proceduralgeneration/comments/6eubj7/how_can_i_add_octaves_persistence_lacunarity/ 
 * 
 * */
public class SimplexOctaveNoise extends Noise {
    private final SimplexNoise[] generators;
    private final int octaves;
    private final double scaleDivisor;
    
    public SimplexOctaveNoise(Random random, int octaves) {
        this.octaves = octaves;
        this.generators = new SimplexNoise[octaves];
        this.scaleDivisor = 1.5D;
        
        for (int i = 0; i < octaves; ++i) {
            this.generators[i] = new SimplexNoise(random);
        }
    }
    
    public void setOctaves(Random random) {
        for (int i = 0; i < this.octaves; ++i) {
            this.generators[i] = new SimplexNoise(random);
        }
    }
    
    public double sample(double x, double z, double scaleX, double scaleZ, double lacunarity) {
        return this.sample(x, z, scaleX, scaleZ, lacunarity, 0.5);
    }
    
    public double[] sample(double[] arr, double x, double z, int sizeX, int sizeZ, double scaleX, double scaleZ, double lacunarity) {
        return this.sample(arr, x, z, sizeX, sizeZ, scaleX, scaleZ, lacunarity, 0.5);
    }
    
    public double[] sample(double[] arr, double x, double z, int sizeX, int sizeZ, double scaleX, double scaleZ, double lacunarity, double persistence) {
        scaleX /= this.scaleDivisor;
        scaleZ /= this.scaleDivisor;
        
        if (arr == null || arr.length < sizeX * sizeZ) {
            arr = new double[sizeX * sizeZ];
        }
        else {
            for (int i = 0; i < arr.length; ++i) {
                arr[i] = 0.0;
            }
        }
        
        double amplitude = 1.0;
        double frequency = 1.0;
        
        for (int j = 0; j < this.octaves; ++j) {
            this.generators[j].sample(arr, x, z, sizeX, sizeZ, scaleX * frequency, scaleZ * frequency, 0.55 / amplitude);
            frequency *= lacunarity;
            amplitude *= persistence;
        }
        return arr;
    }
    
    
    public double sample(double x, double z, double scaleX, double scaleZ, double lacunarity, double persistence) {
        scaleX /= this.scaleDivisor;
        scaleZ /= this.scaleDivisor;
        
        double total = 0.0;
        double amplitude = 1.0;
        double frequency = 1.0;
        
        for (int j = 0; j < this.octaves; ++j) {
            total += this.generators[j].sample(x, z, scaleX * frequency, scaleZ * frequency) * (0.55 / amplitude);
            frequency *= lacunarity;
            amplitude *= persistence;
        }
        
        return total;
    }
}
