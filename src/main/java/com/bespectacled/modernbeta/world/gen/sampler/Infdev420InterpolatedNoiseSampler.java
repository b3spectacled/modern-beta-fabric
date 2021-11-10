package com.bespectacled.modernbeta.world.gen.sampler;

import java.util.Random;

public class Infdev420InterpolatedNoiseSampler extends InterpolatedNoiseSampler {

    public Infdev420InterpolatedNoiseSampler(
        Random random,
        double coordinateScale,
        double heightScale,
        double mainNoiseScaleXZ,
        double mainNoiseScaleY,
        double limitScale,
        double densityScale,
        int noiseSizeX,
        int noiseSizeY,
        int noiseSizeZ,
        int noiseMinY
    ) {
        super(
            random,
            coordinateScale,
            heightScale,
            mainNoiseScaleXZ,
            mainNoiseScaleY,
            limitScale,
            densityScale,
            noiseSizeX,
            noiseSizeY,
            noiseSizeZ, noiseMinY
        );
    }

    @Override
    protected double sampleNoise(int noiseX, int noiseY, int noiseZ) {
        double density;
        
        double mainNoise = (this.mainNoiseOctaves.sample(
            noiseX, noiseY, noiseZ, 
            this.coordinateScale / this.mainNoiseScaleXZ, 
            this.heightScale / this.mainNoiseScaleY, 
            this.coordinateScale / this.mainNoiseScaleXZ
        ) / 10D + 1.0D) / 2D;
        
        if (mainNoise < 0.0D) {
            density = this.minLimitNoiseOctaves.sample(
                noiseX, noiseY, noiseZ, 
                this.coordinateScale, 
                this.heightScale, 
                this.coordinateScale
            ) / this.limitScale;
            
        } else if (mainNoise > 1.0D) {
            density = this.maxLimitNoiseOctaves.sample(
                noiseX, noiseY, noiseZ, 
                this.coordinateScale, 
                this.heightScale, 
                this.coordinateScale
            ) / this.limitScale;
            
        } else {
            double minLimitNoise = this.minLimitNoiseOctaves.sample(
                noiseX, noiseY, noiseZ, 
                this.coordinateScale, 
                this.heightScale, 
                this.coordinateScale
            ) / this.limitScale;
            
            double maxLimitNoise = this.maxLimitNoiseOctaves.sample(
                noiseX, noiseY, noiseZ, 
                this.coordinateScale, 
                this.heightScale, 
                this.coordinateScale
            ) / this.limitScale;
            
            density = minLimitNoise + (maxLimitNoise - minLimitNoise) * mainNoise;
        }
        
        return density / this.densityScale;
    }
}
