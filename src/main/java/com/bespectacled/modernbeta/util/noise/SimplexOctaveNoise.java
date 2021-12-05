package com.bespectacled.modernbeta.util.noise;

import java.util.Random;

/* 
 * Used for additional reference: https://www.reddit.com/r/proceduralgeneration/comments/6eubj7/how_can_i_add_octaves_persistence_lacunarity/ 
 * 
 * */
public class SimplexOctaveNoise {
    private final SimplexNoise[] noises;
    private final int octaves;
    private final double noiseScale;
    
    public SimplexOctaveNoise(Random random, int octaves) {
        this.noises = new SimplexNoise[octaves];
        this.octaves = octaves;
        this.noiseScale = 1.5D;
        
        for (int i = 0; i < octaves; ++i) {
            this.noises[i] = new SimplexNoise(random);
        }
    }
    
    public double sample(double x, double z, double scaleX, double scaleZ, double lacunarity) {
        return this.sample(x, z, scaleX, scaleZ, lacunarity, 0.5);
    }
    
    public double sample(double x, double z, double scaleX, double scaleZ, double lacunarity, double persistence) {
        scaleX /= this.noiseScale;
        scaleZ /= this.noiseScale;
        
        double total = 0.0;
        double amplitude = 1.0;
        double frequency = 1.0;
        
        for (int j = 0; j < this.octaves; ++j) {
            total += this.noises[j].sample(x, z, scaleX * frequency, scaleZ * frequency) * (0.55 / amplitude);
            frequency *= lacunarity;
            amplitude *= persistence;
        }
        
        return total;
    }
}
