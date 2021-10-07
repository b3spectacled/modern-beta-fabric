package com.bespectacled.modernbeta.world.gen.sampler;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.gen.NoiseHelper;
import net.minecraft.world.gen.random.AbstractRandom;

public class NoiseCaveSampler {
    private final int minY;
    private final DoublePerlinNoiseSampler terrainAdditionNoise;
    private final DoublePerlinNoiseSampler pillarNoise;
    private final DoublePerlinNoiseSampler pillarFalloffNoise;
    private final DoublePerlinNoiseSampler pillarScaleNoise;
    private final DoublePerlinNoiseSampler scaledCaveScaleNoise;
    private final DoublePerlinNoiseSampler horizontalCaveNoise;
    private final DoublePerlinNoiseSampler caveScaleNoise;
    private final DoublePerlinNoiseSampler caveFalloffNoise;
    private final DoublePerlinNoiseSampler tunnelNoise1;
    private final DoublePerlinNoiseSampler tunnelNoise2;
    private final DoublePerlinNoiseSampler tunnelScaleNoise;
    private final DoublePerlinNoiseSampler tunnelFalloffNoise;
    private final DoublePerlinNoiseSampler offsetNoise;
    private final DoublePerlinNoiseSampler offsetScaleNoise;
    private final DoublePerlinNoiseSampler caveOpeningNoise;
    private final DoublePerlinNoiseSampler caveDensityNoise;

    public NoiseCaveSampler(AbstractRandom abstractRandom, int minY) {
        AbstractRandom random = abstractRandom.derive();
        
        this.pillarNoise = DoublePerlinNoiseSampler.create(random.derive(), -7, 1.0, 1.0);
        this.pillarFalloffNoise = DoublePerlinNoiseSampler.create(random.derive(), -8, 1.0);
        this.pillarScaleNoise = DoublePerlinNoiseSampler.create(random.derive(), -8, 1.0);
        
        this.scaledCaveScaleNoise = DoublePerlinNoiseSampler.create(random.derive(), -7, 1.0);
        this.horizontalCaveNoise = DoublePerlinNoiseSampler.create(random.derive(), -8, 1.0);
        this.caveScaleNoise = DoublePerlinNoiseSampler.create(random.derive(), -11, 1.0);
        this.caveFalloffNoise = DoublePerlinNoiseSampler.create(random.derive(), -11, 1.0);
        
        this.tunnelNoise1 = DoublePerlinNoiseSampler.create(random.derive(), -7, 1.0);
        this.tunnelNoise2 = DoublePerlinNoiseSampler.create(random.derive(), -7, 1.0);
        this.tunnelScaleNoise = DoublePerlinNoiseSampler.create(random.derive(), -11, 1.0);
        this.tunnelFalloffNoise = DoublePerlinNoiseSampler.create(random.derive(), -8, 1.0);
        
        this.offsetNoise = DoublePerlinNoiseSampler.create(random.derive(), -5, 1.0);
        this.offsetScaleNoise = DoublePerlinNoiseSampler.create(random.derive(), -8, 1.0);
        
        this.caveOpeningNoise = DoublePerlinNoiseSampler.create(random.derive(), -7, 0.4, 0.5, 1.0);
        
        this.terrainAdditionNoise = DoublePerlinNoiseSampler.create(random.derive(), -8, 1.0);
        this.caveDensityNoise = DoublePerlinNoiseSampler.create(random.derive(), -8, 0.5, 1.0, 2.0, 1.0, 2.0, 1.0, 0.0, 2.0, 0.0);
        
        this.minY = minY;
    }

    public double sample(double weight, double tunnelThreshold, int x, int y, int z) {
        // Weight decreases at higher y, 
        // so past a certain point, place only tunnels.
        boolean genTunnelsOnly = weight < tunnelThreshold;
        
        double caveOpeningNoise = this.getCaveOpeningNoise(x, y, z);
        double tunnelOffsetNoise = this.getTunnelOffsetNoise(x, y, z);
        double tunnelNoise = this.getTunnelNoise(x, y, z);
        
        double openingNoise = Math.min(caveOpeningNoise, tunnelNoise + tunnelOffsetNoise);
        //double openingNoise = tunnelNoise + tunnelOffsetNoise;
        
        if (genTunnelsOnly) {
            return Math.max(Math.min(weight, openingNoise * 128.0 * 5.0), -200.0);
        }
        
        double caveDensityNoise = this.caveDensityNoise.sample(x, (double)y / 1.5, z);
        double clampedCaveDensityNoise = MathHelper.clamp(caveDensityNoise + 0.25, -1.0, 1.0);
        
        double deltaY = (float)(30 - y) / 8.0f;
        double lerpedCaveDensityNoise = clampedCaveDensityNoise + MathHelper.clampedLerp(0.5, 0.0, deltaY);
        
        double terrainAdditionNoise = this.getTerrainAdditionNoise(x, y, z);
        double caveNoise = this.getCaveNoise(x, y, z);
        
        double terrainAddition = lerpedCaveDensityNoise + terrainAdditionNoise;
        double caveTypeSelector = Math.min(terrainAddition, Math.min(tunnelNoise, caveNoise) + tunnelOffsetNoise);
        double caveOrPillerSelector = Math.max(caveTypeSelector, this.getPillarNoise(x, y, z));
        
        return 128.0 * MathHelper.clamp(caveOrPillerSelector, -1.0, 1.0);
    }
    
    private double getCaveOpeningNoise(int x, int y, int z) {
        double caveOpeningNoise = this.caveOpeningNoise.sample(x * 0.75, y * 0.5, z * 0.75) + 0.37;
        double deltaY = (y + 10) / 40.0;
        
        return caveOpeningNoise + MathHelper.clampedLerp(0.3, 0.0, deltaY);
    }

    private double getPillarNoise(int x, int y, int z) {
        double pillarFalloffNoise = NoiseHelper.lerpFromProgress(this.pillarFalloffNoise, x, y, z, 0.0, 2.0);
        double pillarScaleNoise = NoiseHelper.lerpFromProgress(this.pillarScaleNoise, x, y, z, 0.0, 1.1);
        
        pillarScaleNoise = Math.pow(pillarScaleNoise, 3.0);
        double pillarNoise = this.pillarNoise.sample((double)x * 25.0, (double)y * 0.3, (double)z * 25.0);
        pillarNoise = pillarScaleNoise * (pillarNoise * 2.0 - pillarFalloffNoise);
        
        if (pillarNoise > 0.03) {
            return pillarNoise;
        }
        
        return Double.NEGATIVE_INFINITY;
    }

    private double getTerrainAdditionNoise(int x, int y, int z) {
        double terrainAdditionNoise = this.terrainAdditionNoise.sample(x, y * 8, z);
        
        return MathHelper.square(terrainAdditionNoise) * 4.0;
    }

    private double getTunnelNoise(int x, int y, int z) {
        double tunnelScaleNoise = this.tunnelScaleNoise.sample(x * 2, y, z * 2);
        double tunnelNoise = NoiseCaveSampler.CaveScaler.scaleTunnels(tunnelScaleNoise);
        
        double lerpedTunnelFalloffNoise = NoiseHelper.lerpFromProgress(this.tunnelFalloffNoise, x, y, z, 0.065, 0.088);
        
        double tunnelNoise1 = sample(this.tunnelNoise1, x, y, z, tunnelNoise);
        double ridgedTunnelNoise1 = Math.abs(tunnelNoise * tunnelNoise1) - lerpedTunnelFalloffNoise;
        
        double tunnelNoise2 = sample(this.tunnelNoise2, x, y, z, tunnelNoise);
        double ridgedTunnelNoise2 = Math.abs(tunnelNoise * tunnelNoise2) - lerpedTunnelFalloffNoise;
        
        return clamp(Math.max(ridgedTunnelNoise1, ridgedTunnelNoise2));
    }

    private double getCaveNoise(int x, int y, int z) {
        double caveScaleNoise = this.caveScaleNoise.sample(x * 2, y, z * 2);
        double caveScale = CaveScaler.scaleCaves(caveScaleNoise);
        
        double caveFalloffNoise = NoiseHelper.lerpFromProgress(this.caveFalloffNoise, x * 2, y, z * 2, 0.6, 1.3);
        double scaledCaveScaleNoise = NoiseCaveSampler.sample(this.scaledCaveScaleNoise, x, y, z, caveScale);
        
        double ridgedCaveScaleNoise = Math.abs(caveScale * scaledCaveScaleNoise) - 0.083 * caveFalloffNoise;
        double lerpedHorizCaveNoise = NoiseHelper.lerpFromProgress(this.horizontalCaveNoise, x, 0.0, z, this.minY, 8.0);
        double ridgedHorizCaveNoise = Math.abs(lerpedHorizCaveNoise - (double)y / 8.0) - 1.0 * caveFalloffNoise;
        
        ridgedHorizCaveNoise = ridgedHorizCaveNoise * ridgedHorizCaveNoise * ridgedHorizCaveNoise;
        
        return clamp(Math.max(ridgedHorizCaveNoise, ridgedCaveScaleNoise));
    }

    private double getTunnelOffsetNoise(int x, int y, int z) {
        double offsetScaleNoise = NoiseHelper.lerpFromProgress(this.offsetScaleNoise, x, y, z, 0.0, 0.1);
        
        return (0.4 - Math.abs(this.offsetNoise.sample(x, y, z))) * offsetScaleNoise;
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
