package com.bespectacled.modernbeta.world.cavebiome.provider.settings;

import com.bespectacled.modernbeta.util.NbtTags;

import net.minecraft.nbt.NbtCompound;

public class CaveBiomeProviderSettings {
    public static NbtCompound createSettingsBase(String caveBiomeType) {
        NbtCompound settings = new NbtCompound();
        
        settings.putString(NbtTags.CAVE_BIOME_TYPE, caveBiomeType);
        
        return settings;
    }
}
