package com.bespectacled.modernbeta.world.gen.sampler;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.noise.NoiseParametersKeys;
import net.minecraft.world.gen.random.RandomDeriver;

public class NoiseCaveSampler {
    private final int minY;
    
    private final DoublePerlinNoiseSampler pillarNoise;
    private final DoublePerlinNoiseSampler pillarRarenessNoise;
    private final DoublePerlinNoiseSampler pillarThicknessNoise;
    
    private final DoublePerlinNoiseSampler spaghetti2dNoise;
    private final DoublePerlinNoiseSampler spaghetti2dElevationNoise;
    private final DoublePerlinNoiseSampler spaghetti2dModulatorNoise;
    private final DoublePerlinNoiseSampler spaghetti2dThicknessNoise;
    
    private final DoublePerlinNoiseSampler spaghetti3dFirstNoise;
    private final DoublePerlinNoiseSampler spaghetti3dSecondNoise;
    private final DoublePerlinNoiseSampler spaghetti3dRarityNoise;
    private final DoublePerlinNoiseSampler spaghetti3dThicknessNoise;
    
    private final DoublePerlinNoiseSampler spaghettiRoughnessNoise;
    private final DoublePerlinNoiseSampler spaghettiRoughnessModulatorNoise;
    
    private final DoublePerlinNoiseSampler caveEntranceNoise;
    private final DoublePerlinNoiseSampler caveLayerNoise;
    private final DoublePerlinNoiseSampler caveCheeseNoise;

    public NoiseCaveSampler(Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry, RandomDeriver randomDeriver, int minY) {
        
        this.pillarNoise = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.PILLAR);
        this.pillarRarenessNoise = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.PILLAR_RARENESS);
        this.pillarThicknessNoise = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.PILLAR_THICKNESS);
        
        this.spaghetti2dNoise = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.SPAGHETTI_2D);
        this.spaghetti2dElevationNoise = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.SPAGHETTI_2D_ELEVATION);
        this.spaghetti2dModulatorNoise = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.SPAGHETTI_2D_MODULATOR);
        this.spaghetti2dThicknessNoise = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.SPAGHETTI_2D_THICKNESS);
        
        this.spaghetti3dFirstNoise = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.SPAGHETTI_3D_1);
        this.spaghetti3dSecondNoise = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.SPAGHETTI_3D_2);
        this.spaghetti3dRarityNoise = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.SPAGHETTI_3D_RARITY);
        this.spaghetti3dThicknessNoise = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.SPAGHETTI_3D_THICKNESS);
        
        this.spaghettiRoughnessNoise = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.SPAGHETTI_ROUGHNESS);
        this.spaghettiRoughnessModulatorNoise = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.SPAGHETTI_ROUGHNESS_MODULATOR);
        
        this.caveEntranceNoise = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.CAVE_ENTRANCE);
        this.caveLayerNoise = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.CAVE_LAYER);
        this.caveCheeseNoise = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.CAVE_CHEESE);
        
        this.minY = minY;
    }
    
    public double sample(double density, double tunnelOffset, int x, int y, int z) {
        //double tunnelOffset = 1.5625; // 200.0 / 128.0
        
        double offsetWeight;
        double noise;
        double minLimit;
        double maxLimit;
        
        if (density < -64.0) {
            noise = density;
            maxLimit = 64.0;
            minLimit = -64.0;
        } else {
            offsetWeight = density - tunnelOffset;
            boolean genTunnelsOnly = offsetWeight < 0.0;
            
            double caveEntranceNoise = this.sampleCaveEntranceNoise(x, y, z);
            double spaghettiRoughnessNoise = this.sampleSpaghettiRoughnessNoise(x, y, z);
            double spaghetti3dNoise = this.sampleSpaghetti3dNoise(x, y, z);
            
            double entranceNoise = Math.min(caveEntranceNoise, spaghetti3dNoise + spaghettiRoughnessNoise);
            
            if (genTunnelsOnly) {
                noise = density;
                maxLimit = entranceNoise * 5.0;
                minLimit = -64.0;
            } else {
                double caveLayerNoise = this.sampleCaveLayerNoise(x, y, z);
                
                if (caveLayerNoise > 64.0) {
                    noise = 64.0;
                } else {
                    double caveCheeseNoise = this.caveCheeseNoise.sample(x, y / 1.5D, z);
                    double clampedCaveCheeseNoise = MathHelper.clamp(caveCheeseNoise + 0.27, -1.0, 1.0);
                    
                    double deltaY = offsetWeight * 1.28;
                    double offsetCaveCheeseNoise = clampedCaveCheeseNoise + MathHelper.clampedLerp(0.5, 0.0, deltaY);
                    
                    noise = offsetCaveCheeseNoise + caveLayerNoise;
                }
                
                double spaghetti2dNoise = this.sampleSpaghetti2dNoise(x, y, z);
                
                maxLimit = Math.min(entranceNoise, spaghetti2dNoise + spaghettiRoughnessNoise);
                minLimit = this.samplePillarNoise(x, y, z);
            }
        }
        
        offsetWeight = Math.max(Math.min(noise, maxLimit), minLimit);
        
        return offsetWeight;
    }
    
    private double sampleCaveEntranceNoise(int x, int y, int z) {
        double caveEntranceNoise = this.caveEntranceNoise.sample(x * 0.75, y * 0.5, z * 0.75) + 0.37;
        double deltaY = (y + 10) / 40.0;
        
        return caveEntranceNoise + MathHelper.clampedLerp(0.3, 0.0, deltaY);
    }

    private double samplePillarNoise(int x, int y, int z) {
        double pillarRarenessNoise = NoiseSampler.scaledSample(this.pillarRarenessNoise, x, y, z, 0.0, 2.0);
        double pillarThicknessNoise = NoiseSampler.scaledSample(this.pillarThicknessNoise, x, y, z, 0.0, 1.1);
        
        pillarThicknessNoise = Math.pow(pillarThicknessNoise, 3.0);
        double pillarNoise = this.pillarNoise.sample((double)x * 25.0, (double)y * 0.3, (double)z * 25.0);
        pillarNoise = pillarThicknessNoise * (pillarNoise * 2.0 - pillarRarenessNoise);
        
        if (pillarNoise > 0.03) {
            return pillarNoise;
        }
        
        return Double.NEGATIVE_INFINITY;
    }

    private double sampleCaveLayerNoise(int x, int y, int z) {
        double caveLayerNoise = this.caveLayerNoise.sample(x, y * 8, z);
        
        return MathHelper.square(caveLayerNoise) * 4.0;
    }

    private double sampleSpaghetti3dNoise(int x, int y, int z) {
        double spaghetti3dRarityNoise = this.spaghetti3dRarityNoise.sample(x * 2, y, z * 2);
        double scaledRarityNoise = NoiseCaveSampler.CaveScaler.scaleTunnels(spaghetti3dRarityNoise);
        
        double spaghetti3dThicknessNoise = NoiseSampler.scaledSample(this.spaghetti3dThicknessNoise, x, y, z, 0.065, 0.088);
        
        double firstNoise = sample(this.spaghetti3dFirstNoise, x, y, z, scaledRarityNoise);
        double ridgedFirstNoise = Math.abs(scaledRarityNoise * firstNoise) - spaghetti3dThicknessNoise;
        
        double secondNoise = sample(this.spaghetti3dSecondNoise, x, y, z, scaledRarityNoise);
        double ridgedSecondNoise = Math.abs(scaledRarityNoise * secondNoise) - spaghetti3dThicknessNoise;
        
        return clamp(Math.max(ridgedFirstNoise, ridgedSecondNoise));
    }

    private double sampleSpaghetti2dNoise(int x, int y, int z) {
        double spaghetti2dModulatorNoise = this.spaghetti2dModulatorNoise.sample(x * 2, y, z * 2);
        double scaledModulatorNoise = CaveScaler.scaleCaves(spaghetti2dModulatorNoise);
        
        double spaghetti2dThickness = NoiseSampler.scaledSample(this.spaghetti2dThicknessNoise, x * 2, y, z * 2, 0.6, 1.3);
        double spaghetti2dNoise = NoiseCaveSampler.sample(this.spaghetti2dNoise, x, y, z, scaledModulatorNoise);
        
        double ridgedSpaghetti2dNoise = Math.abs(scaledModulatorNoise * spaghetti2dNoise) - 0.083 * spaghetti2dThickness;
        double spaghetti2dElevationNoise = NoiseSampler.scaledSample(this.spaghetti2dElevationNoise, x, 0.0, z, this.minY, 8.0);
        double ridgedspaghetti2dElevationNoise = Math.abs(spaghetti2dElevationNoise - (double)y / 8.0) - 1.0 * spaghetti2dThickness;
        
        ridgedspaghetti2dElevationNoise = ridgedspaghetti2dElevationNoise * ridgedspaghetti2dElevationNoise * ridgedspaghetti2dElevationNoise;
        
        return clamp(Math.max(ridgedspaghetti2dElevationNoise, ridgedSpaghetti2dNoise));
    }

    private double sampleSpaghettiRoughnessNoise(int x, int y, int z) {
        double spaghettiRoughnessModulatorNoise = NoiseSampler.scaledSample(this.spaghettiRoughnessModulatorNoise, x, y, z, 0.0, 0.1);
        
        return (0.4 - Math.abs(this.spaghettiRoughnessNoise.sample(x, y, z))) * spaghettiRoughnessModulatorNoise;
    }

    private static double clamp(double value) {
        return MathHelper.clamp(value, -1.0, 1.0);
    }

    private static double sample(DoublePerlinNoiseSampler sampler, double x, double y, double z, double scale) {
        return sampler.sample(x / scale, y / scale, z / scale);
    }

    private static final class CaveScaler {
        static double scaleCaves(double value) {
            if (value < -0.75)
                return 0.5;
            
            if (value < -0.5)
                return 0.75;
            
            if (value < 0.5)
                return 1.0;
            
            if (value < 0.75)
                return 2.0;
            
            return 3.0;
        }

        static double scaleTunnels(double value) {
            if (value < -0.5)
                return 0.75;
            
            if (value < 0.0)
                return 1.0;
            
            if (value < 0.5)
                return 1.5;
           
            return 2.0;
        }
    }
}