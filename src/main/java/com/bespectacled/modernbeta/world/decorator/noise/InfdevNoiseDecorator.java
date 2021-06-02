package com.bespectacled.modernbeta.world.decorator.noise;

import java.util.Random;

import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;

public class InfdevNoiseDecorator implements OldNoiseDecorator {
    private final PerlinOctaveNoise noiseSampler;
    
    public InfdevNoiseDecorator(Random random) {
        this.noiseSampler = new PerlinOctaveNoise(random, 5, true);
    }
    
    public InfdevNoiseDecorator(PerlinOctaveNoise noiseSampler) {
        this.noiseSampler = noiseSampler;
    }

    @Override
    public int sample(int chunkX, int chunkZ, Random random) {
        int startX = chunkX << 4;
        int startZ = chunkZ << 4;
        
        return (int) this.noiseSampler.sample(startX * 0.25D, startZ * 0.25D) << 3;
    }
}
