package com.bespectacled.modernbeta.api.world.biome;

import net.minecraft.world.biome.Biome;

public interface DeepOceanBiomeResolver {
    public Biome getDeepOceanBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ);
}
