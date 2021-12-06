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
    
    public static NbtCompound createSettingsBeta() {
        return createSettingsOceans(BuiltInTypes.Biome.BETA.name)
            .putCompound("desert", CONFIG.betaDesertBiome.toCompound())
            .putCompound("forest", CONFIG.betaForestBiome.toCompound())
            .putCompound("ice_desert", CONFIG.betaIceDesertBiome.toCompound())
            .putCompound("plains", CONFIG.betaPlainsBiome.toCompound())
            .putCompound("rainforest", CONFIG.betaRainforestBiome.toCompound())
            .putCompound("savanna", CONFIG.betaSavannaBiome.toCompound())
            .putCompound("shrubland", CONFIG.betaShrublandBiome.toCompound())
            .putCompound("seasonal_forest", CONFIG.betaSeasonalForestBiome.toCompound())
            .putCompound("swampland", CONFIG.betaSwamplandBiome.toCompound())
            .putCompound("taiga", CONFIG.betaTaigaBiome.toCompound())
            .putCompound("tundra", CONFIG.betaTundraBiome.toCompound())
            .build();
    }
    
    public static NbtCompound createSettingsSingle() {
        return createSettingsBase(BuiltInTypes.Biome.SINGLE.name)
            .putString(NbtTags.SINGLE_BIOME, CONFIG.singleBiome)
            .build();
    }
    
    public static NbtCompound createSettingsPE() {
        return createSettingsOceans(BuiltInTypes.Biome.PE.name)
            .putCompound("desert", CONFIG.peDesertBiome.toCompound())
            .putCompound("forest", CONFIG.peForestBiome.toCompound())
            .putCompound("ice_desert", CONFIG.peIceDesertBiome.toCompound())
            .putCompound("plains", CONFIG.pePlainsBiome.toCompound())
            .putCompound("rainforest", CONFIG.peRainforestBiome.toCompound())
            .putCompound("savanna", CONFIG.peSavannaBiome.toCompound())
            .putCompound("shrubland", CONFIG.peShrublandBiome.toCompound())
            .putCompound("seasonal_forest", CONFIG.peSeasonalForestBiome.toCompound())
            .putCompound("swampland", CONFIG.peSwamplandBiome.toCompound())
            .putCompound("taiga", CONFIG.peTaigaBiome.toCompound())
            .putCompound("tundra", CONFIG.peTundraBiome.toCompound())
            .build();
    }
    
    public static NbtCompound createSettingsVanilla() {
        return createSettingsOceans(BuiltInTypes.Biome.VANILLA.name)
            .putBoolean(NbtTags.VANILLA_LARGE_BIOMES, CONFIG.vanillaLargeBiomes)
            .build();
    }
}
