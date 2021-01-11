package com.bespectacled.modernbeta.noise;

import java.util.Random;

/* 
 * Used for additional reference: https://www.reddit.com/r/proceduralgeneration/comments/6eubj7/how_can_i_add_octaves_persistence_lacunarity/ 
 * 
 * */
public class SimplexOctaveNoise extends Noise {
    private SimplexNoise[] generators;
    private int octaves;
    private double scaleDivisor;
    
    public SimplexOctaveNoise(Random random, int integer) {
        this.octaves = integer;
        this.generators = new SimplexNoise[integer];
        this.scaleDivisor = 1.5D;
        
        for (int i = 0; i < integer; ++i) {
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
