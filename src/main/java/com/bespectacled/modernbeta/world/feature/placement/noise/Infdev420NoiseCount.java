package com.bespectacled.modernbeta.world.feature.placement.noise;

import java.util.Random;

import com.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;

public class Infdev420NoiseCount implements OldNoiseCount {
    private final PerlinOctaveNoise noiseSampler;
    
    public Infdev420NoiseCount(Random random) {
        this.noiseSampler = new PerlinOctaveNoise(random, 5, true);
    }
    
    public Infdev420NoiseCount(PerlinOctaveNoise noiseSampler) {
        this.noiseSampler = noiseSampler;
    }

    @Override
    public int sample(int chunkX, int chunkZ, Random random) {
        int startX = chunkX << 4;
        int startZ = chunkZ << 4;
        
        double scale = 0.05D;
        
        int noiseCount = (int) (this.noiseSampler.sample(startX * scale, startZ * scale) - random.nextDouble());
        
        if (noiseCount < 0) {
            noiseCount = 0;
        }
        
        return noiseCount;
    }
}
