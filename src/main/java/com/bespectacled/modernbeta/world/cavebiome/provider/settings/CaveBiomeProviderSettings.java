package com.bespectacled.modernbeta.world.cavebiome.provider.settings;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.config.ModernBetaConfigCaveBiome;
import com.bespectacled.modernbeta.util.NbtTags;

import net.minecraft.nbt.NbtCompound;

public class CaveBiomeProviderSettings {
    private static final ModernBetaConfigCaveBiome CONFIG = ModernBeta.CAVE_BIOME_CONFIG;
    
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

        settings.putString(NbtTags.SINGLE_BIOME, CONFIG.singleBiome);
        settings.putBoolean(NbtTags.USE_NOISE, CONFIG.useNoise);
        
        settings.putInt(NbtTags.VERTICAL_NOISE_SCALE, CONFIG.verticalNoiseScale);
        settings.putInt(NbtTags.HORIZONTAL_NOISE_SCALE, CONFIG.horizontalNoiseScale);
        
        return settings;
    }
    
    public static NbtCompound createSettingsNoise() {
        NbtCompound settings = createSettingsBase(BuiltInTypes.CaveBiome.VANILLA.name);
        
        settings.putInt(NbtTags.VERTICAL_NOISE_SCALE, CONFIG.verticalNoiseScale);
        settings.putInt(NbtTags.HORIZONTAL_NOISE_SCALE, CONFIG.horizontalNoiseScale);
        
        return settings;
    }
}
