package com.bespectacled.modernbeta.world.cavebiome.provider.settings;

import com.bespectacled.modernbeta.api.world.WorldSettings;

import net.minecraft.nbt.CompoundTag;

public class CaveBiomeProviderSettings {
    public static CompoundTag createSettingsBase(String caveBiomeType) {
        CompoundTag settings = new CompoundTag();
        
        settings.putString(WorldSettings.TAG_CAVE_BIOME, caveBiomeType);
        
        return settings;
    }
}
