package com.bespectacled.modernbeta.world.cavebiome.provider;

import java.util.List;

import com.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class NoCaveBiomeProvider extends CaveBiomeProvider {
    public NoCaveBiomeProvider(long seed, NbtCompound settings, Registry<Biome> biomeRegistry) {
        super(seed, settings, biomeRegistry);
    }

    @Override
    public Biome getBiome(int biomeX, int biomeY, int biomeZ) {
        return null;
    }

    @Override
    public List<Biome> getBiomesForRegistry() {
        return List.of();
    }
}
