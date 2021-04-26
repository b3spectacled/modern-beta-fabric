package com.bespectacled.modernbeta.world.cavebiome.provider;

import com.bespectacled.modernbeta.api.world.biome.AbstractCaveBiomeProvider;

import net.minecraft.nbt.NbtCompound;

public class VanillaCaveBiomeProvider extends AbstractCaveBiomeProvider {
    public VanillaCaveBiomeProvider(long seed, NbtCompound settings) {
        super(seed, settings);
    }
}
