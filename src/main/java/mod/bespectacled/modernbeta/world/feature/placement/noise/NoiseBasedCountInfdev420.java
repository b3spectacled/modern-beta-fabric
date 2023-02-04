package mod.bespectacled.modernbeta.world.feature.placement.noise;

import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import net.minecraft.util.math.random.Random;

public class NoiseBasedCountInfdev420 implements NoiseBasedCount {
    private final PerlinOctaveNoise noiseSampler;
    
    public NoiseBasedCountInfdev420(Random random) {
        this.noiseSampler = new PerlinOctaveNoise(new java.util.Random(random.nextInt()), 5, true);
    }
    
    public NoiseBasedCountInfdev420(PerlinOctaveNoise noiseSampler) {
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
