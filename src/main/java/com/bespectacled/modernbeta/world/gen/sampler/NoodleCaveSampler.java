package com.bespectacled.modernbeta.world.gen.sampler;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.noise.NoiseParametersKeys;
import net.minecraft.world.gen.random.RandomDeriver;

public class NoodleCaveSampler extends NoiseSampler {
    private final DoublePerlinNoiseSampler noodleNoiseSampler;
    private final DoublePerlinNoiseSampler noodleThicknessNoiseSampler;
    private final DoublePerlinNoiseSampler noodleRidgeFirstNoiseSampler;
    private final DoublePerlinNoiseSampler noodleRidgeSecondNoiseSampler;
    
    public NoodleCaveSampler(
        Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry,
        RandomDeriver randomDeriver,
        int horizontalNoiseResolution,
        int verticalNoiseResolution
    ) {
        super(horizontalNoiseResolution, verticalNoiseResolution);
        
        this.noodleNoiseSampler = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.NOODLE);
        this.noodleThicknessNoiseSampler = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.NOODLE_THICKNESS);
        this.noodleRidgeFirstNoiseSampler = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.NOODLE_RIDGE_A);
        this.noodleRidgeSecondNoiseSampler = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.NOODLE_RIDGE_B);
    }
    
    public void sampleNoodleNoise(double[] buffer, int x, int z, int minY, int noiseSizeY) {
        this.sample(buffer, x, z, minY, noiseSizeY, this.noodleNoiseSampler, 1.0, 1.0);
    }

    public void sampleNoodleThicknessNoise(double[] buffer, int x, int z, int minY, int noiseSizeY) {
        this.sample(buffer, x, z, minY, noiseSizeY, this.noodleThicknessNoiseSampler, 1.0, 1.0);
    }

    public void sampleNoodleRidgeFirstNoise(double[] buffer, int x, int z, int minY, int noiseSizeY) {
        this.sample(buffer, x, z, minY, noiseSizeY, this.noodleRidgeFirstNoiseSampler, 2.6666666666666665, 2.6666666666666665);
    }

    public void sampleNoodleRidgeSecondNoise(double[] buffer, int x, int z, int minY, int noiseSizeY) {
        this.sample(buffer, x, z, minY, noiseSizeY, this.noodleRidgeSecondNoiseSampler, 2.6666666666666665, 2.6666666666666665);
    }
    
    public double sampleDensity(
        double density,
        int x,
        int y,
        int z,
        double noodleNoise,
        double noodleThicknessNoise,
        double noodleRidgeFirstNoise,
        double noodleRidgeSecondNoise,
        int minY
    ) {
        if (y > 130 || y < minY + 4) {
            return density;
        }
        
        if (density < 0.0) {
            return density;
        }
        
        if (noodleNoise < 0.0) { 
            return density;
        }
        
        double weightOffset = MathHelper.clampedLerpFromProgress(noodleThicknessNoise, -1.0, 1.0, 0.05, 0.1);
        double ridgedFirstNoise = Math.abs(1.5 * noodleRidgeFirstNoise) - weightOffset;
        double ridgedSecondNoise = Math.abs(1.5 * noodleRidgeSecondNoise) - weightOffset;
        double selectedNoise = Math.max(ridgedFirstNoise, ridgedSecondNoise);
        
        return Math.min(density, selectedNoise);
    }
}
