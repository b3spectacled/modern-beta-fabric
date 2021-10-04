package com.bespectacled.modernbeta.world.cavebiome.provider.settings;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.util.NbtTags;

import net.minecraft.nbt.NbtCompound;

public class CaveBiomeProviderSettings {
    public static NbtCompound createSettingsBase(String caveBiomeType) {
        NbtCompound settings = new NbtCompound();
        
        settings.putString(NbtTags.CAVE_BIOME_TYPE, caveBiomeType);
        
        return settings;
    }
    
    public static NbtCompound createSettingsNone() {
        return createSettingsBase(BuiltInTypes.CaveBiome.NONE.name);
    }
    
    public static NbtCompound createSettingsSingle() {
        NbtCompound settings = createSettingsBase(BuiltInTypes.CaveBiome.SINGLE.name);

        settings.putString(NbtTags.SINGLE_BIOME, ModernBeta.CAVE_BIOME_CONFIG.singleBiomeConfig.singleBiome);
        
        return settings;
    }
}
