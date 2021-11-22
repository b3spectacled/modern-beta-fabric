package com.bespectacled.modernbeta.world.feature.placement.noise;

import java.util.Random;

import com.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;

public class AlphaNoiseCount implements OldNoiseCount {
    private final PerlinOctaveNoise noiseSampler;
    
    public AlphaNoiseCount(Random random) {
        this.noiseSampler = new PerlinOctaveNoise(random, 8, true);
    }
    
    public AlphaNoiseCount(PerlinOctaveNoise noiseSampler) {
        this.noiseSampler = noiseSampler;
    }

    @Override
    public int sample(int chunkX, int chunkZ, Random random) {
        int startX = chunkX << 4;
        int startZ = chunkZ << 4;
        
        double scale = 0.5D;

        int noiseCount = (int) ((this.noiseSampler.sample(startX * scale, startZ * scale) / 8D + random.nextDouble() * 4D + 4D) / 3D);
        
        if (noiseCount < 0) {
            noiseCount = 0;
        }
        
        return noiseCount;
    }
}