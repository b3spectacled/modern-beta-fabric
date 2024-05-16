package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import net.minecraft.util.math.noise.PerlinNoiseSampler;
import net.minecraft.util.math.random.CheckedRandom;

public class LayerOceanTemperature extends Layer {
    private PerlinNoiseSampler noiseSampler;

    public LayerOceanTemperature() {
        super(0);
    }

    @Override
    public void setWorldSeed(long seed) {
        super.setWorldSeed(seed);
        this.noiseSampler = new PerlinNoiseSampler(new CheckedRandom(seed));
    }

    @Override
    protected BiomeInfo[] getNewBiomes(int x, int z, int width, int length) {
        return forEach(x, z, width, length, (input, ix, iz) -> {
            double d = this.noiseSampler.sample((ix + x) / 8.0, (iz + z) / 8.0, 0.0);
            if (d > 0.4) return DummyBiome.WARM_OCEAN.biomeInfo;
            if (d > 0.2) return DummyBiome.LUKEWARM_OCEAN.biomeInfo;
            if (d < -0.4) return DummyBiome.FROZEN_OCEAN.biomeInfo;
            if (d < -0.2) return DummyBiome.COLD_OCEAN.biomeInfo;
            return DummyBiome.OCEAN.biomeInfo;
        });
    }
}
