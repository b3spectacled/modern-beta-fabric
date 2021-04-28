package com.bespectacled.modernbeta.world.cavebiome.provider;

import com.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;

import net.minecraft.nbt.NbtCompound;

public class NoCaveBiomeProvider extends CaveBiomeProvider {
    public NoCaveBiomeProvider(long seed, NbtCompound settings) {
        super(seed, settings);
    }

}
