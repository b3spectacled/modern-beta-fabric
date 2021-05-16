package com.bespectacled.modernbeta.world.cavebiome.provider.settings;

import com.bespectacled.modernbeta.api.world.WorldSettings;

import net.minecraft.nbt.NbtCompound;

public class CaveBiomeProviderSettings {
    public static NbtCompound createSettingsBase(String caveBiomeType) {
        NbtCompound settings = new NbtCompound();
        
        settings.putString(WorldSettings.TAG_CAVE_BIOME, caveBiomeType);
        
        return settings;
    }
}
