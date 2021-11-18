package com.bespectacled.modernbeta.world.feature.placement.noise;

import java.util.Random;

public interface OldNoiseCount {
    int sample(int chunkX, int chunkZ, Random random);
}
