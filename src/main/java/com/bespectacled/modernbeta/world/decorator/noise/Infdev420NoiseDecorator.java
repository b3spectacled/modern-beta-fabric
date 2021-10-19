package com.bespectacled.modernbeta.world.decorator.noise;

import java.util.Random;

import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;

public class Infdev420NoiseDecorator implements OldNoiseDecorator {
    private final PerlinOctaveNoise noiseSampler;
    
    public Infdev420NoiseDecorator(Random random) {
        this.noiseSampler = new PerlinOctaveNoise(random, 5, true);
    }
    
    public Infdev420NoiseDecorator(PerlinOctaveNoise noiseSampler) {
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
