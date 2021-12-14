package com.bespectacled.modernbeta.world.gen.sampler;

import com.bespectacled.modernbeta.mixin.MixinNoiseColumnSamplerInvoker;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.NoiseColumnSampler;

public class NoiseCaveSampler {
    private final NoiseColumnSampler columnSampler;

    public NoiseCaveSampler(NoiseColumnSampler columnSampler) {
        this.columnSampler = columnSampler;
    }
    
    public double sample(double density, double tunnelOffset, int x, int y, int z) {
        //double tunnelOffset = 1.5625; // 200.0 / 128.0
        
        double offsetWeight;
        double noise;
        double minLimit;
        double maxLimit;
        
        MixinNoiseColumnSamplerInvoker invoker = (MixinNoiseColumnSamplerInvoker)this.columnSampler;
        
        if (density < -64.0) {
            noise = density;
            maxLimit = 64.0;
            minLimit = -64.0;
        } else {
            offsetWeight = density - tunnelOffset;
            boolean genTunnelsOnly = offsetWeight < 0.0;
            
            double caveEntranceNoise = invoker.invokeSampleCaveEntranceNoise(x, y, z);
            double spaghettiRoughnessNoise = invoker.invokeSampleSpaghettiRoughnessNoise(x, y, z);
            double spaghetti3dNoise = invoker.invokeSampleSpaghetti3dNoise(x, y, z);
            
            double entranceNoise = Math.min(caveEntranceNoise, spaghetti3dNoise + spaghettiRoughnessNoise);
            
            if (genTunnelsOnly) {
                noise = density;
                maxLimit = entranceNoise * 5.0;
                minLimit = -64.0;
            } else {
                double caveLayerNoise = invoker.invokeSampleCaveLayerNoise(x, y, z);
                
                if (caveLayerNoise > 64.0) {
                    noise = 64.0;
                } else {
                    double caveCheeseNoise = invoker.getCaveCheeseNoise().sample(x, y / 1.5D, z);
                    double clampedCaveCheeseNoise = MathHelper.clamp(caveCheeseNoise + 0.27, -1.0, 1.0);
                    
                    double deltaY = offsetWeight * 1.28;
                    double offsetCaveCheeseNoise = clampedCaveCheeseNoise + MathHelper.clampedLerp(0.5, 0.0, deltaY);
                    
                    noise = offsetCaveCheeseNoise + caveLayerNoise;
                }
                
                double spaghetti2dNoise = invoker.invokeSampleSpaghetti2dNoise(x, y, z);
                
                maxLimit = Math.min(entranceNoise, spaghetti2dNoise + spaghettiRoughnessNoise);
                minLimit = invoker.invokeSamplePillarNoise(x, y, z);
            }
        }
        
        offsetWeight = Math.max(Math.min(noise, maxLimit), minLimit);
        
        return offsetWeight;
    }
}
