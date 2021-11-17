package com.bespectacled.modernbeta.world.feature.placement.noise;

import java.util.Random;

import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;

public class Infdev611NoisePlacer implements OldNoisePlacer {
    private final PerlinOctaveNoise noiseSampler;
    
    public Infdev611NoisePlacer(Random random) {
        this.noiseSampler = new PerlinOctaveNoise(random, 8, true);
    }
    
    public Infdev611NoisePlacer(PerlinOctaveNoise noiseSampler) {
        this.noiseSampler = noiseSampler;
    }

    @Override
    public int sample(int chunkX, int chunkZ, Random random) {
        int startX = chunkX << 4;
        int startZ = chunkZ << 4;
        
        double scale = 0.5D;
        
        int noiseCount = (int) (this.noiseSampler.sample(startX * scale, startZ * scale) / 8.0 + random.nextDouble() * 4D + 4D);
        
        if (noiseCount < 0) {
            noiseCount = 0;
        }
        
        return noiseCount;
    }
}
