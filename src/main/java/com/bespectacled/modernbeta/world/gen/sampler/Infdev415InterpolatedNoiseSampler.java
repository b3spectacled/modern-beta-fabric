package com.bespectacled.modernbeta.world.gen.sampler;

import java.util.Random;
import java.util.function.Function;

public class Infdev415InterpolatedNoiseSampler extends InterpolatedNoiseSampler {
    private final Function<Double, Double> noiseClampFunc;
    private final Function<Integer, Double> offsetFunc;
    
    public Infdev415InterpolatedNoiseSampler(
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
        int noiseMinY,
        Function<Double, Double> noiseClampFunc,
        Function<Integer, Double> offsetFunc
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
            noiseSizeZ,
            noiseMinY
        );
        
        this.noiseClampFunc = noiseClampFunc;
        this.offsetFunc = offsetFunc;
    }
    
    @Override
    protected double sampleNoise(int noiseX, int noiseY, int noiseZ) {
        double density;
        double densityOffset = this.offsetFunc.apply(noiseY);
        
        // Default values: 8.55515, 1.71103, 8.55515
        double mainNoiseVal = this.mainNoiseOctaves.sample(
            noiseX * this.coordinateScale / this.mainNoiseScaleXZ,
            noiseY * this.coordinateScale / this.mainNoiseScaleY, 
            noiseZ * this.coordinateScale / this.mainNoiseScaleXZ
        ) / 2.0;
        
        // Do not clamp noise if generating with noise caves!
        if (mainNoiseVal < -1.0) {
            density = this.minLimitNoiseOctaves.sample(
                noiseX * this.coordinateScale, 
                noiseY * this.heightScale, 
                noiseZ * this.coordinateScale
            ) / this.limitScale;
            
            density -= densityOffset;
            density /= this.densityScale;
            
            density = this.noiseClampFunc.apply(density);
            
        } else if (mainNoiseVal > 1.0) {
            density = this.maxLimitNoiseOctaves.sample(
                noiseX * this.coordinateScale, 
                noiseY * this.heightScale, 
                noiseZ * this.coordinateScale
            ) / this.limitScale;

            density -= densityOffset;
            density /= this.densityScale;
            
            density = this.noiseClampFunc.apply(density);
            
        } else {
            double minLimitVal = this.minLimitNoiseOctaves.sample(
                noiseX * this.coordinateScale, 
                noiseY * this.heightScale, 
                noiseZ * this.coordinateScale
            ) / this.limitScale;
            
            double maxLimitVal = this.maxLimitNoiseOctaves.sample(
                noiseX * this.coordinateScale, 
                noiseY * this.heightScale, 
                noiseZ * this.coordinateScale
            ) / this.limitScale;
            
            minLimitVal -= densityOffset;
            maxLimitVal -= densityOffset;
            
            minLimitVal /= this.densityScale;
            maxLimitVal /= this.densityScale;
            
            minLimitVal = this.noiseClampFunc.apply(minLimitVal);
            maxLimitVal = this.noiseClampFunc.apply(maxLimitVal);
                            
            double delta = (mainNoiseVal + 1.0) / 2.0;
            density = minLimitVal + (maxLimitVal - minLimitVal) * delta;
        };
        
        return density;
    }
}
