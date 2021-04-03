package com.bespectacled.modernbeta.noise;

import net.minecraft.util.math.noise.PerlinNoiseSampler;
import net.minecraft.world.gen.WorldGenRandom;

public class PerlinOctaveNoiseNew extends Noise {
    private final PerlinNoiseSampler generatorCollection[];
    private final int octaves;
    
    public PerlinOctaveNoiseNew(WorldGenRandom random, int octaves) {
        this.octaves = octaves;
        this.generatorCollection = new PerlinNoiseSampler[octaves];

        for (int j = 0; j < octaves; j++) {
            this.generatorCollection[j] = new PerlinNoiseSampler(random);
        }
    }
    
    public final double testSample(double x, double z, double scaleX, double scaleZ) {
        return this.generatorCollection[0].sample(x * scaleX, -this.generatorCollection[0].originY, z * scaleZ, 1.0, 0.0);
    }
    
    /*
     * 2D Beta Perlin Sampler
     */
    public final double sample(double x, double z, double scaleX, double scaleZ) {
        double total = 0.0;
        double frequency = 1.0;
        
        for (int i = 0; i < this.octaves; ++i) {
            double originY = this.generatorCollection[i].originY;
            
            total += this.generatorCollection[i].sample(x * scaleX * frequency, -originY, z * scaleZ * frequency, 1.0, 0.0) / frequency;
            
            frequency /= 2.0;
        }
        
        return total;
    }
    
    /*
     * 3D Beta Perlin Sampler
     */
    public final double sample(double x, double y, double z, double scaleX, double scaleY, double scaleZ) {
        double total = 0.0;
        double frequency = 1.0;
        
        for (int i = 0; i < this.octaves; ++i) {
            double frequencyY = scaleY * frequency;
            total += this.generatorCollection[i].sample(
                x * scaleX * frequency, 
                y * scaleY * frequency, 
                z * scaleZ * frequency, 
                frequencyY, 
                y * frequencyY
            ) / frequency;
        
            frequency /= 2.0;
        }
        
        return total;
    }
}
