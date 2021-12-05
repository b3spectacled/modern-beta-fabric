package com.bespectacled.modernbeta.world.feature.placement.noise;

import java.util.Random;

import com.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;

public class BetaNoiseBasedCount implements OldNoiseBasedCount {
    private final PerlinOctaveNoise noiseSampler;
    
    public BetaNoiseBasedCount(Random random) {
        this.noiseSampler = new PerlinOctaveNoise(random, 8, true);
    }
    
    public BetaNoiseBasedCount(PerlinOctaveNoise noiseSampler) {
        this.noiseSampler = noiseSampler;
    }

    @Override
    public int sample(int chunkX, int chunkZ, Random random) {
        int startX = chunkX << 4;
        int startZ = chunkZ << 4;
        
        double scale = 0.5D;

        return (int) ((this.noiseSampler.sampleXY(startX * scale, startZ * scale) / 8D + random.nextDouble() * 4D + 4D) / 3D);
    }
}
