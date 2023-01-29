package mod.bespectacled.modernbeta.world.feature.placement.noise;

import java.util.Random;

import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;

public class Infdev420NoiseBasedCount implements ModernBetaNoiseBasedCount {
    private final PerlinOctaveNoise noiseSampler;
    
    public Infdev420NoiseBasedCount(Random random) {
        this.noiseSampler = new PerlinOctaveNoise(random, 5, true);
    }
    
    public Infdev420NoiseBasedCount(PerlinOctaveNoise noiseSampler) {
        this.noiseSampler = noiseSampler;
    }

    @Override
    public int sample(int chunkX, int chunkZ, Random random) {
        int startX = chunkX << 4;
        int startZ = chunkZ << 4;
        
        double scale = 0.05D;
        
        int noiseCount = (int) (this.noiseSampler.sampleXY(startX * scale, startZ * scale) - random.nextDouble());
        
        if (noiseCount < 0) {
            noiseCount = 0;
        }
        
        return noiseCount;
    }
}
