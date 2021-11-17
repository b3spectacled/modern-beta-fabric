package com.bespectacled.modernbeta.world.feature.placement.noise;

import java.util.Random;

import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;

public class Infdev415NoisePlacer implements OldNoisePlacer {
    private final PerlinOctaveNoise noiseSampler;
    
    public Infdev415NoisePlacer(Random random) {
        this.noiseSampler = new PerlinOctaveNoise(random, 5, true);
    }
    
    public Infdev415NoisePlacer(PerlinOctaveNoise noiseSampler) {
        this.noiseSampler = noiseSampler;
    }

    @Override
    public int sample(int chunkX, int chunkZ, Random random) {
        int startX = chunkX << 4;
        int startZ = chunkZ << 4;
        
        double scale = 0.25D;
        
        return (int) this.noiseSampler.sample(startX * scale, startZ * scale) << 3;
    }
}
