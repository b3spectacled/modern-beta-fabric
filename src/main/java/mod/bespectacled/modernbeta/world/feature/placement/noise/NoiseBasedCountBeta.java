package mod.bespectacled.modernbeta.world.feature.placement.noise;


import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import net.minecraft.util.math.random.Random;

public class NoiseBasedCountBeta implements NoiseBasedCount {
    private final PerlinOctaveNoise noiseSampler;
    
    public NoiseBasedCountBeta(Random random) {
        this.noiseSampler = new PerlinOctaveNoise(new java.util.Random(random.nextInt()), 8, true);
    }
    
    public NoiseBasedCountBeta(PerlinOctaveNoise noiseSampler) {
        this.noiseSampler = noiseSampler;
    }

    @Override
    public int sample(int chunkX, int chunkZ, Random random) {
        int startX = chunkX << 4;
        int startZ = chunkZ << 4;
        
        double scale = 0.5D;

        return (int) ((this.noiseSampler.sampleXY(startX * scale, startZ * scale) / 8D + random.nextDouble() * 4D + 4D) / 3D);
    }
}
