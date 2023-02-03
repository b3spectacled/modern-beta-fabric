package mod.bespectacled.modernbeta.world.feature.placement.noise;


import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import net.minecraft.util.math.random.Random;

public class Infdev415NoiseBasedCount implements ModernBetaNoiseBasedCount {
    private final PerlinOctaveNoise noiseSampler;
    
    public Infdev415NoiseBasedCount(Random random) {
        this.noiseSampler = new PerlinOctaveNoise(new java.util.Random(random.nextInt()), 5, true);
    }
    
    public Infdev415NoiseBasedCount(PerlinOctaveNoise noiseSampler) {
        this.noiseSampler = noiseSampler;
    }

    @Override
    public int sample(int chunkX, int chunkZ, Random random) {
        int startX = chunkX << 4;
        int startZ = chunkZ << 4;
        
        double scale = 0.25D;
        
        return (int) this.noiseSampler.sampleXY(startX * scale, startZ * scale) << 3;
    }
}
