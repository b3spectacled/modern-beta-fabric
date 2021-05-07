package com.bespectacled.modernbeta.world.cavebiome.provider.settings;

import net.minecraft.nbt.NbtCompound;

public class CaveBiomeProviderSettings {
    public static NbtCompound createSettingsBase(String caveBiomeType) {
        NbtCompound settings = new NbtCompound();
        
        settings.putString("caveBiomeType", caveBiomeType);
        
        return settings;
    }
}
