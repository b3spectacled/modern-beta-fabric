package com.bespectacled.modernbeta.world.decorator.noise;

import java.util.Random;

public interface OldNoiseDecorator {
    int sample(int chunkX, int chunkZ, Random random);
}
