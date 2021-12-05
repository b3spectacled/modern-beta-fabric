package com.bespectacled.modernbeta.util.noise;

import java.util.Random;

import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;

public class PerlinOctaveNoise {
    private final PerlinNoise noises[];
    private final int octaves;
    private final boolean maintainPrecision;
    
    public PerlinOctaveNoise(Random random, int octaves, boolean useOffset) {
        this.noises = new PerlinNoise[octaves];
        this.octaves = octaves;
        this.maintainPrecision = false;
        
        for (int i = 0; i < octaves; i++) {
            this.noises[i] = new PerlinNoise(random, useOffset);
        }
    }

    /*
     * Beta 3D array noise sampler.
     */
    public double[] sampleBeta(
        double x,
        double y,
        double z, 
        int sizeX,
        int sizeY,
        int sizeZ, 
        double scaleX,
        double scaleY,
        double scaleZ
    ) {
        double[] noise = new double[sizeX * sizeY * sizeZ];
        double frequency = 1.0;
        
        for (int i = 0; i < octaves; i++) {
            this.noises[i].sampleBeta(
                noise, 
                x,
                y,
                z, 
                sizeX,
                sizeY,
                sizeZ,
                scaleX * frequency,
                scaleY * frequency,
                scaleZ * frequency,
                frequency
            );
            
            frequency /= 2.0;
        }
        
        return noise;
    }
    
    /*
     * Alpha 3D array noise sampler.
     */
    public double[] sampleAlpha(
        double x,
        double y,
        double z, 
        int sizeX,
        int sizeY,
        int sizeZ, 
        double scaleX,
        double scaleY,
        double scaleZ
    ) {
        double[] noise = new double[sizeX * sizeY * sizeZ];
        double frequency = 1.0;
        
        for (int i = 0; i < octaves; i++) {
            this.noises[i].sampleAlpha(
                noise, 
                x,
                y,
                z, 
                sizeX,
                sizeY,
                sizeZ,
                scaleX * frequency,
                scaleY * frequency,
                scaleZ * frequency,
                frequency
            );
            
            frequency /= 2.0;
        }

        return noise;
    }
    
    /*
     * Standard 2D Perlin noise sampler.
     */
    public final double sampleXY(double x, double y) {
        double total = 0.0;
        double frequency = 1.0;
        
        for (int i = 0; i < this.octaves; ++i) {
            total += this.noises[i].sample(x / frequency, y / frequency) * frequency;
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
            total += this.noises[i].sample(x / frequency, y / frequency, z / frequency) * frequency;
            frequency *= 2.0;
        }
        
        return total;
    }
    
    /*
     * Beta 2D noise sampler.
     * This functions like sample(x, 0.0, z), except yOrigin is ignored.
     */
    public final double sampleXZ(double x, double z, double scaleX, double scaleZ) {
        double total = 0.0;
        double frequency = 1.0;
        
        for (int i = 0; i < this.octaves; ++i) {
            total += this.noises[i].sampleXZ(
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
            total += this.noises[i].sample(
                this.maintainPrecision(x * scaleX * frequency), 
                this.maintainPrecision(y * scaleY * frequency), 
                this.maintainPrecision(z * scaleZ * frequency), 
                scaleY * frequency, 
                y * scaleY * frequency
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
