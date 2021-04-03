package com.bespectacled.modernbeta.world.cavebiome.provider;

import com.bespectacled.modernbeta.api.AbstractCaveBiomeProvider;

import net.minecraft.nbt.NbtCompound;

public class NoCaveBiomeProvider extends AbstractCaveBiomeProvider {
    public NoCaveBiomeProvider(long seed, NbtCompound settings) {
        super(seed, settings);
    }

}
