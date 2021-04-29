package com.bespectacled.modernbeta.world.cavebiome.provider;

import com.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;

import net.minecraft.nbt.NbtCompound;

public class SingleCaveBiomeProvider extends CaveBiomeProvider {
    public SingleCaveBiomeProvider(long seed, NbtCompound settings) {
        super(seed, settings);
    }
}
