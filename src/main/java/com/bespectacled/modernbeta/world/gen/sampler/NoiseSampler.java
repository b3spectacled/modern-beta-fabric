package com.bespectacled.modernbeta.world.gen.sampler;

import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;

public abstract class NoiseSampler {
    private final int horizontalNoiseResolution;
    private final int verticalNoiseResolution;
    
    protected NoiseSampler(int horizontalNoiseResolution, int verticalNoiseResolution) {
        this.horizontalNoiseResolution = horizontalNoiseResolution;
        this.verticalNoiseResolution = verticalNoiseResolution;
    }
    
    protected void sample(
        double[] buffer,
        int x,
        int z,
        int minY,
        int noiseSizeY,
        DoublePerlinNoiseSampler sampler,
        double horizontalScale,
        double verticalScale
    ) {
        for (int y = 0; y < noiseSizeY; ++y) {
            double noise;
            int actualY = y + minY;
            
            int noiseX = x * this.horizontalNoiseResolution;
            int noiseY = actualY * this.verticalNoiseResolution;
            int noiseZ = z * this.horizontalNoiseResolution;
            
            /*
            noise = NoiseHelper.lerpFromProgress(
                sampler, 
                (double)noiseX * horizontalScale, 
                (double)noiseY * verticalScale, 
                (double)noiseZ * horizontalScale, 
                -1.0, 1.0
            );
            */
            
            noise = 0.0;
            
            buffer[y] = noise;
        }
    }
}
