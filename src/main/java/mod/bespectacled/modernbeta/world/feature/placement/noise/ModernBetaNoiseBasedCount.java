package mod.bespectacled.modernbeta.world.feature.placement.noise;

import net.minecraft.util.math.random.Random;

public interface ModernBetaNoiseBasedCount {
    int sample(int chunkX, int chunkZ, Random random);
}
