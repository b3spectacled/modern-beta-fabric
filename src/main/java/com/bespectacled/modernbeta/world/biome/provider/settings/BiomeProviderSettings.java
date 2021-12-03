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
            .putString("desert", CONFIG.betaDesertBiome)
            .putString("forest", CONFIG.betaForestBiome)
            .putString("ice_desert", CONFIG.betaIceDesertBiome)
            .putString("plains", CONFIG.betaPlainsBiome)
            .putString("rainforest", CONFIG.betaRainforestBiome)
            .putString("savanna", CONFIG.betaSavannaBiome)
            .putString("shrubland", CONFIG.betaShrublandBiome)
            .putString("seasonal_forest", CONFIG.betaSeasonalForestBiome)
            .putString("swampland", CONFIG.betaSwamplandBiome)
            .putString("taiga", CONFIG.betaTaigaBiome)
            .putString("tundra", CONFIG.betaTundraBiome)
            .putString("ocean", CONFIG.betaOceanBiome)
            .putString("cold_ocean", CONFIG.betaColdOceanBiome)
            .putString("frozen_ocean", CONFIG.betaFrozenOceanBiome)
            .putString("lukewarm_ocean", CONFIG.betaLukewarmOceanBiome)
            .putString("warm_ocean", CONFIG.betaWarmOceanBiome)
            .build();
    }
    
    public static NbtCompound createSettingsSingle() {
        return createSettingsBase(BuiltInTypes.Biome.SINGLE.name)
            .putString(NbtTags.SINGLE_BIOME, CONFIG.singleBiome)
            .build();
    }
    
    public static NbtCompound createSettingsPE() {
        return createSettingsOceans(BuiltInTypes.Biome.PE.name)
            .putString("desert", CONFIG.peDesertBiome)
            .putString("forest", CONFIG.peForestBiome)
            .putString("ice_desert", CONFIG.peIceDesertBiome)
            .putString("plains", CONFIG.pePlainsBiome)
            .putString("rainforest", CONFIG.peRainforestBiome)
            .putString("savanna", CONFIG.peSavannaBiome)
            .putString("shrubland", CONFIG.peShrublandBiome)
            .putString("seasonal_forest", CONFIG.peSeasonalForestBiome)
            .putString("swampland", CONFIG.peSwamplandBiome)
            .putString("taiga", CONFIG.peTaigaBiome)
            .putString("tundra", CONFIG.peTundraBiome)
            .putString("ocean", CONFIG.peOceanBiome)
            .putString("cold_ocean", CONFIG.peColdOceanBiome)
            .putString("frozen_ocean", CONFIG.peFrozenOceanBiome)
            .putString("lukewarm_ocean", CONFIG.peLukewarmOceanBiome)
            .putString("warm_ocean", CONFIG.peWarmOceanBiome)
            .build();
    }
    
    public static NbtCompound createSettingsVanilla() {
        return createSettingsOceans(BuiltInTypes.Biome.VANILLA.name)
            .putBoolean(NbtTags.VANILLA_LARGE_BIOMES, CONFIG.vanillaLargeBiomes)
            .build();
    }
}
