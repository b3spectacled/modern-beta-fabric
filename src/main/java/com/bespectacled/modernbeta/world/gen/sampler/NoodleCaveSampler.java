package com.bespectacled.modernbeta.world.gen.sampler;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.gen.NoiseHelper;
import net.minecraft.world.gen.random.AbstractRandom;

public class NoodleCaveSampler {
    private final DoublePerlinNoiseSampler frequencyNoiseSampler;
    private final DoublePerlinNoiseSampler weightReducingNoiseSampler;
    private final DoublePerlinNoiseSampler firstWeightNoiseSampelr;
    private final DoublePerlinNoiseSampler secondWeightNoiseSampler;
    
    private final int horizontalNoiseResolution;
    private final int verticalNoiseResolution;
    
    public NoodleCaveSampler(AbstractRandom random, int horizontalNoiseResolution, int verticalNoiseResolution) {
        this.frequencyNoiseSampler = DoublePerlinNoiseSampler.create(random.derive(), -8, 1.0);
        this.weightReducingNoiseSampler = DoublePerlinNoiseSampler.create(random.derive(), -8, 1.0);
        this.firstWeightNoiseSampelr = DoublePerlinNoiseSampler.create(random.derive(), -7, 1.0);
        this.secondWeightNoiseSampler = DoublePerlinNoiseSampler.create(random.derive(), -7, 1.0);
        
        this.horizontalNoiseResolution = horizontalNoiseResolution;
        this.verticalNoiseResolution = verticalNoiseResolution;
    }
    
    public void sampleFrequencyNoise(double[] buffer, int x, int z, int minY, int noiseSizeY) {
        this.sample(buffer, x, z, minY, noiseSizeY, this.frequencyNoiseSampler, 1.0);
    }

    public void sampleWeightReducingNoise(double[] buffer, int x, int z, int minY, int noiseSizeY) {
        this.sample(buffer, x, z, minY, noiseSizeY, this.weightReducingNoiseSampler, 1.0);
    }

    public void sampleFirstWeightNoise(double[] buffer, int x, int z, int minY, int noiseSizeY) {
        this.sample(buffer, x, z, minY, noiseSizeY, this.firstWeightNoiseSampelr, 2.6666666666666665, 2.6666666666666665);
    }

    public void sampleSecondWeightNoise(double[] buffer, int x, int z, int minY, int noiseSizeY) {
        this.sample(buffer, x, z, minY, noiseSizeY, this.secondWeightNoiseSampler, 2.6666666666666665, 2.6666666666666665);
    }

    public void sample(double[] buffer, int x, int z, int minY, int noiseSizeY, DoublePerlinNoiseSampler sampler, double scale) {
        this.sample(buffer, x, z, minY, noiseSizeY, sampler, scale, scale);
    }
    
    public void sample(double[] buffer, int x, int z, int minY, int noiseSizeY, DoublePerlinNoiseSampler sampler, double horizontalScale, double verticalScale) {
        for (int y = 0; y < noiseSizeY; ++y) {
            double noise;
            int actualY = y + minY;
            
            int noiseX = x * this.horizontalNoiseResolution;
            int noiseY = actualY * this.verticalNoiseResolution;
            int noiseZ = z * this.horizontalNoiseResolution;
            
            noise = NoiseHelper.lerpFromProgress(
                sampler, 
                (double)noiseX * horizontalScale, 
                (double)noiseY * verticalScale, 
                (double)noiseZ * horizontalScale, 
                -1.0, 1.0
            );
            
            buffer[y] = noise;
        }
    }
    
    public double sampleWeight(double weight, int x, int y, int z, double frequencyNoise, double weightReducingNoise, double firstWeightNoise, double secondWeightNoise, int minY) {
        if (y > 130 && y < minY + 4) {
            return weight;
        }
        
        if (weight < 0.0) {
            return weight;
        }
        
        if (frequencyNoise < 0.0) { 
            return weight;
        }
        
        double lerpedReducingNoise = MathHelper.clampedLerpFromProgress(weightReducingNoise, -1.0, 1.0, 0.05, 0.1);
        double ridgedFirstNoise = Math.abs(1.5 * firstWeightNoise) - lerpedReducingNoise;
        double ridgedSecondNoise = Math.abs(1.5 * secondWeightNoise) - lerpedReducingNoise;
        double selectedNoise = Math.max(ridgedFirstNoise, ridgedSecondNoise);
        
        return Math.min(weight, selectedNoise);
        
    }
}
