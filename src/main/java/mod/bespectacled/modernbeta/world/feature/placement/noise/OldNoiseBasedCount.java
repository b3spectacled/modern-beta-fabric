package mod.bespectacled.modernbeta.world.feature.placement.noise;

import java.util.Random;

public interface OldNoiseBasedCount {
    int sample(int chunkX, int chunkZ, Random random);
}
