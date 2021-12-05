package com.bespectacled.modernbeta.world.decorator.noise;

import java.util.Random;

import com.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;

public class AlphaNoiseDecorator implements OldNoiseDecorator {
    private final PerlinOctaveNoise noiseSampler;
    
    public AlphaNoiseDecorator(Random random) {
        this.noiseSampler = new PerlinOctaveNoise(random, 8, true);
    }
    
    public AlphaNoiseDecorator(PerlinOctaveNoise noiseSampler) {
        this.noiseSampler = noiseSampler;
    }

    @Override
    public int sample(int chunkX, int chunkZ, Random random) {
        int startX = chunkX << 4;
        int startZ = chunkZ << 4;
        
        double scale = 0.5D;

        int noiseCount = (int) ((this.noiseSampler.sampleXY(startX * scale, startZ * scale) / 8D + random.nextDouble() * 4D + 4D) / 3D);
        
        if (noiseCount < 0) {
            noiseCount = 0;
        }
        
        return noiseCount;
    }
}
