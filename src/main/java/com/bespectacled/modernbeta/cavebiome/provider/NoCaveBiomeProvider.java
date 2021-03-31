package com.bespectacled.modernbeta.cavebiome.provider;

import com.bespectacled.modernbeta.api.biome.AbstractCaveBiomeProvider;

import net.minecraft.nbt.NbtCompound;

public class NoCaveBiomeProvider extends AbstractCaveBiomeProvider {
    public NoCaveBiomeProvider(long seed, NbtCompound settings) {
        super(seed, settings);
    }

}
