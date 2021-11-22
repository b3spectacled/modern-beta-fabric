package com.bespectacled.modernbeta.world.biome.provider.settings;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.config.ConfigBiome;
import com.bespectacled.modernbeta.util.NbtTags;

import net.minecraft.nbt.NbtCompound;

public class BiomeProviderSettings {
    private static final ConfigBiome CONFIG = ModernBeta.BIOME_CONFIG;
    
    public static NbtCompound createSettingsBase(String biomeType) {
        NbtCompound settings = new NbtCompound();
        
        settings.putString(NbtTags.BIOME_TYPE, biomeType);
        
        return settings;
    }
    
    public static NbtCompound createSettingsOceans(String biomeType) {
        NbtCompound settings = createSettingsBase(biomeType);
        
        settings.putBoolean(NbtTags.GEN_OCEANS, false);
        
        return settings;
    }
    
    public static NbtCompound createSettingsBeta() {
        NbtCompound settings = createSettingsOceans(BuiltInTypes.Biome.BETA.name);
        
        settings.putString("desert", CONFIG.betaBiomeConfig.betaDesertBiome);
        settings.putString("forest", CONFIG.betaBiomeConfig.betaForestBiome);
        settings.putString("ice_desert", CONFIG.betaBiomeConfig.betaIceDesertBiome);
        settings.putString("plains", CONFIG.betaBiomeConfig.betaPlainsBiome);
        settings.putString("rainforest", CONFIG.betaBiomeConfig.betaRainforestBiome);
        settings.putString("savanna", CONFIG.betaBiomeConfig.betaSavannaBiome);
        settings.putString("shrubland", CONFIG.betaBiomeConfig.betaShrublandBiome);
        settings.putString("seasonal_forest", CONFIG.betaBiomeConfig.betaSeasonalForestBiome);
        settings.putString("swampland", CONFIG.betaBiomeConfig.betaSwamplandBiome);
        settings.putString("taiga", CONFIG.betaBiomeConfig.betaTaigaBiome);
        settings.putString("tundra", CONFIG.betaBiomeConfig.betaTundraBiome);
        
        settings.putString("ocean", CONFIG.betaBiomeConfig.betaOceanBiome);
        settings.putString("cold_ocean", CONFIG.betaBiomeConfig.betaColdOceanBiome);
        settings.putString("frozen_ocean", CONFIG.betaBiomeConfig.betaFrozenOceanBiome);
        settings.putString("lukewarm_ocean", CONFIG.betaBiomeConfig.betaLukewarmOceanBiome);
        settings.putString("warm_ocean", CONFIG.betaBiomeConfig.betaWarmOceanBiome);
        
        return settings;
    }
    
    public static NbtCompound createSettingsSingle() {
        NbtCompound settings = createSettingsBase(BuiltInTypes.Biome.SINGLE.name);

        settings.putString(NbtTags.SINGLE_BIOME, CONFIG.singleBiomeConfig.singleBiome);
        
        return settings;
    }
    
    public static NbtCompound createSettingsPE() {
        NbtCompound settings = createSettingsOceans(BuiltInTypes.Biome.PE.name);
        
        settings.putString("desert", CONFIG.peBiomeConfig.peDesertBiome);
        settings.putString("forest", CONFIG.peBiomeConfig.peForestBiome);
        settings.putString("ice_desert", CONFIG.peBiomeConfig.peIceDesertBiome);
        settings.putString("plains", CONFIG.peBiomeConfig.pePlainsBiome);
        settings.putString("rainforest", CONFIG.peBiomeConfig.peRainforestBiome);
        settings.putString("savanna", CONFIG.peBiomeConfig.peSavannaBiome);
        settings.putString("shrubland", CONFIG.peBiomeConfig.peShrublandBiome);
        settings.putString("seasonal_forest", CONFIG.peBiomeConfig.peSeasonalForestBiome);
        settings.putString("swampland", CONFIG.peBiomeConfig.peSwamplandBiome);
        settings.putString("taiga", CONFIG.peBiomeConfig.peTaigaBiome);
        settings.putString("tundra", CONFIG.peBiomeConfig.peTundraBiome);
        
        settings.putString("ocean", CONFIG.peBiomeConfig.peOceanBiome);
        settings.putString("cold_ocean", CONFIG.peBiomeConfig.peColdOceanBiome);
        settings.putString("frozen_ocean", CONFIG.peBiomeConfig.peFrozenOceanBiome);
        settings.putString("lukewarm_ocean", CONFIG.peBiomeConfig.peLukewarmOceanBiome);
        settings.putString("warm_ocean", CONFIG.peBiomeConfig.peWarmOceanBiome);
        
        return settings;
    }
    
    public static NbtCompound createSettingsVanilla() {
        NbtCompound settings = createSettingsOceans(BuiltInTypes.Biome.VANILLA.name);
        
        settings.putBoolean(NbtTags.LARGE_BIOMES, CONFIG.vanillaBiomeConfig.largeBiomes);
        
        return settings;
    }
}
