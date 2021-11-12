package com.bespectacled.modernbeta.world.biome.injector;

import net.minecraft.world.biome.Biome;

@FunctionalInterface
public interface BiomeInjectionResolver {
    public Biome apply(int biomeX, int biomeY, int biomeZ);
}
