package com.bespectacled.modernbeta.world.feature.placement.noise;

import java.util.Random;

public interface OldNoisePlacer {
    int sample(int chunkX, int chunkZ, Random random);
}
