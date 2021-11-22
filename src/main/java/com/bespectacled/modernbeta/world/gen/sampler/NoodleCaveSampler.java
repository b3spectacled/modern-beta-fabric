package com.bespectacled.modernbeta.world.gen.sampler;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.NoiseHelper;
import net.minecraft.world.gen.noise.NoiseParametersKeys;
import net.minecraft.world.gen.random.RandomDeriver;

public class NoodleCaveSampler {
    private final DoublePerlinNoiseSampler noodleNoiseSampler;
    private final DoublePerlinNoiseSampler noodleThicknessNoiseSampler;
    private final DoublePerlinNoiseSampler noodleRidgeFirstNoiseSampler;
    private final DoublePerlinNoiseSampler noodleRidgeSecondNoiseSampler;
    
    private final int horizontalNoiseResolution;
    private final int verticalNoiseResolution;
    
    public NoodleCaveSampler(
        Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry,
        RandomDeriver randomDeriver,
        int horizontalNoiseResolution,
        int verticalNoiseResolution
    ) {
        this.noodleNoiseSampler = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.NOODLE);
        this.noodleThicknessNoiseSampler = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.NOODLE_THICKNESS);
        this.noodleRidgeFirstNoiseSampler = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.NOODLE_RIDGE_A);
        this.noodleRidgeSecondNoiseSampler = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.NOODLE_RIDGE_B);
        
        this.horizontalNoiseResolution = horizontalNoiseResolution;
        this.verticalNoiseResolution = verticalNoiseResolution;
    }
    
    public void sampleFrequencyNoise(double[] buffer, int x, int z, int minY, int noiseSizeY) {
        this.sample(buffer, x, z, minY, noiseSizeY, this.noodleNoiseSampler, 1.0, 1.0);
    }

    public void sampleWeightReducingNoise(double[] buffer, int x, int z, int minY, int noiseSizeY) {
        this.sample(buffer, x, z, minY, noiseSizeY, this.noodleThicknessNoiseSampler, 1.0, 1.0);
    }

    public void sampleFirstWeightNoise(double[] buffer, int x, int z, int minY, int noiseSizeY) {
        this.sample(buffer, x, z, minY, noiseSizeY, this.noodleRidgeFirstNoiseSampler, 2.6666666666666665, 2.6666666666666665);
    }

    public void sampleSecondWeightNoise(double[] buffer, int x, int z, int minY, int noiseSizeY) {
        this.sample(buffer, x, z, minY, noiseSizeY, this.noodleRidgeSecondNoiseSampler, 2.6666666666666665, 2.6666666666666665);
    }
    
    private void sample(double[] buffer, int x, int z, int minY, int noiseSizeY, DoublePerlinNoiseSampler sampler, double horizontalScale, double verticalScale) {
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
        if (y > 130 || y < minY + 4) {
            return weight;
        }
        
        if (weight < 0.0) {
            return weight;
        }
        
        if (frequencyNoise < 0.0) { 
            return weight;
        }
        
        double weightOffset = MathHelper.clampedLerpFromProgress(weightReducingNoise, -1.0, 1.0, 0.05, 0.1);
        double ridgedFirstNoise = Math.abs(1.5 * firstWeightNoise) - weightOffset;
        double ridgedSecondNoise = Math.abs(1.5 * secondWeightNoise) - weightOffset;
        double selectedNoise = Math.max(ridgedFirstNoise, ridgedSecondNoise);
        
        return Math.min(weight, selectedNoise);
        
    }
}
