package com.bespectacled.modernbeta.world.biome.provider.settings;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import com.bespectacled.modernbeta.config.ModernBetaConfigBiome;
import com.bespectacled.modernbeta.util.NbtCompoundBuilder;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.settings.ImmutableSettings;
import com.bespectacled.modernbeta.util.settings.Settings;

public class BiomeProviderSettings {
    private static final ModernBetaConfigBiome CONFIG = ModernBeta.BIOME_CONFIG;
    
    private static NbtCompoundBuilder createSettingsBase(String biomeType) {
        return new NbtCompoundBuilder().putString(NbtTags.BIOME_TYPE, biomeType);
    }
    
    private static NbtCompoundBuilder createSettingsOceans(String biomeType) {
        return createSettingsBase(biomeType).putBoolean(NbtTags.GEN_OCEANS, CONFIG.generateOceans);
    }
    
    public static Settings createSettingsDefault(String biomeType) {
        return new ImmutableSettings(createSettingsBase(biomeType).build());
    }
    
    public static Settings createSettingsSingle() {
        return new ImmutableSettings(
            createSettingsBase(ModernBetaBuiltInTypes.Biome.SINGLE.name)
                .putString(NbtTags.SINGLE_BIOME, CONFIG.singleBiome)
                .build()
        );
    }
    
    public static Settings createSettingsBeta() {
        NbtCompoundBuilder builder = new NbtCompoundBuilder();
        CONFIG.betaClimates.entrySet().forEach(e -> builder.putCompound(e.getKey(), e.getValue().toCompound()));
        
        return new ImmutableSettings(
            createSettingsOceans(ModernBetaBuiltInTypes.Biome.BETA.name)
                .putCompound(NbtTags.BIOMES, builder.build())
                .build()
        );
    }
    
    public static Settings createSettingsPE() {
        NbtCompoundBuilder builder = new NbtCompoundBuilder();
        CONFIG.peClimates.entrySet().forEach(e -> builder.putCompound(e.getKey(), e.getValue().toCompound()));
        
        return new ImmutableSettings(
            createSettingsOceans(ModernBetaBuiltInTypes.Biome.PE.name)
                .putCompound(NbtTags.BIOMES, builder.build())
                .build()
        );
    }
}
