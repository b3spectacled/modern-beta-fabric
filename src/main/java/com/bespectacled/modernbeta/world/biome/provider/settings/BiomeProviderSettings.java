package com.bespectacled.modernbeta.world.biome.provider.settings;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.config.ModernBetaConfigBiome;
import com.bespectacled.modernbeta.util.NbtCompoundBuilder;
import com.bespectacled.modernbeta.util.NbtTags;

import net.minecraft.nbt.NbtCompound;

public class BiomeProviderSettings {
    private static final ModernBetaConfigBiome CONFIG = ModernBeta.BIOME_CONFIG;
    
    private static NbtCompoundBuilder createSettingsBase(String biomeType) {
        return new NbtCompoundBuilder().putString(NbtTags.BIOME_TYPE, biomeType);
    }
    
    private static NbtCompoundBuilder createSettingsOceans(String biomeType) {
        return createSettingsBase(biomeType).putBoolean(NbtTags.GEN_OCEANS, CONFIG.generateOceans);
    }
    
    public static NbtCompound createSettingsDefault(String biomeType) {
        return createSettingsBase(biomeType).build();
    }
    
    public static NbtCompound createSettingsSingle() {
        return createSettingsBase(BuiltInTypes.Biome.SINGLE.name)
            .putString(NbtTags.SINGLE_BIOME, CONFIG.singleBiome)
            .build();
    }
    
    public static NbtCompound createSettingsBeta() {
        NbtCompoundBuilder builder = new NbtCompoundBuilder();
        CONFIG.betaClimates.entrySet().forEach(e -> builder.putCompound(e.getKey(), e.getValue().toCompound()));
        
        return createSettingsOceans(BuiltInTypes.Biome.BETA.name)
            .putCompound(NbtTags.BIOMES, builder.build())
            .build();
    }
    
    public static NbtCompound createSettingsPE() {
        NbtCompoundBuilder builder = new NbtCompoundBuilder();
        CONFIG.peClimates.entrySet().forEach(e -> builder.putCompound(e.getKey(), e.getValue().toCompound()));
        
        return createSettingsOceans(BuiltInTypes.Biome.PE.name)
            .putCompound(NbtTags.BIOMES, builder.build())
            .build();
    }
    
    public static NbtCompound createSettingsVanilla() {
        return createSettingsOceans(BuiltInTypes.Biome.VANILLA.name)
            .putBoolean(NbtTags.VANILLA_LARGE_BIOMES, CONFIG.vanillaLargeBiomes)
            .build();
    }
}
