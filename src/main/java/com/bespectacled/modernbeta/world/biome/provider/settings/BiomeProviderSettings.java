package com.bespectacled.modernbeta.world.biome.provider.settings;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.config.ModernBetaConfigBiome;
import com.bespectacled.modernbeta.util.NbtTags;

import net.minecraft.nbt.NbtCompound;

public class BiomeProviderSettings {
    private static final ModernBetaConfigBiome CONFIG = ModernBeta.BIOME_CONFIG;
    
    public static NbtCompound createSettingsBase(String biomeType) {
        NbtCompound settings = new NbtCompound();
        
        settings.putString(NbtTags.BIOME_TYPE, biomeType);
        
        return settings;
    }
    
    public static NbtCompound createSettingsOceans(String biomeType) {
        NbtCompound settings = createSettingsBase(biomeType);

        settings.putBoolean(NbtTags.GEN_OCEANS, CONFIG.generateOceans);

        return settings;
    }
    
    public static NbtCompound createSettingsBeta() {
        NbtCompound settings = createSettingsOceans(BuiltInTypes.Biome.BETA.name);
        
        settings.putString("desert", CONFIG.betaDesertBiome);
        settings.putString("forest", CONFIG.betaForestBiome);
        settings.putString("ice_desert", CONFIG.betaIceDesertBiome);
        settings.putString("plains", CONFIG.betaPlainsBiome);
        settings.putString("rainforest", CONFIG.betaRainforestBiome);
        settings.putString("savanna", CONFIG.betaSavannaBiome);
        settings.putString("shrubland", CONFIG.betaShrublandBiome);
        settings.putString("seasonal_forest", CONFIG.betaSeasonalForestBiome);
        settings.putString("swampland", CONFIG.betaSwamplandBiome);
        settings.putString("taiga", CONFIG.betaTaigaBiome);
        settings.putString("tundra", CONFIG.betaTundraBiome);
        
        settings.putString("ocean", CONFIG.betaOceanBiome);
        settings.putString("cold_ocean", CONFIG.betaColdOceanBiome);
        settings.putString("frozen_ocean", CONFIG.betaFrozenOceanBiome);
        settings.putString("lukewarm_ocean", CONFIG.betaLukewarmOceanBiome);
        settings.putString("warm_ocean", CONFIG.betaWarmOceanBiome);
        
        return settings;
    }
    
    public static NbtCompound createSettingsSingle() {
        NbtCompound settings = createSettingsBase(BuiltInTypes.Biome.SINGLE.name);

        settings.putString(NbtTags.SINGLE_BIOME, CONFIG.singleBiome);
        
        return settings;
    }
    
    public static NbtCompound createSettingsPE() {
        NbtCompound settings = createSettingsOceans(BuiltInTypes.Biome.PE.name);
        
        settings.putString("desert", CONFIG.peDesertBiome);
        settings.putString("forest", CONFIG.peForestBiome);
        settings.putString("ice_desert", CONFIG.peIceDesertBiome);
        settings.putString("plains", CONFIG.pePlainsBiome);
        settings.putString("rainforest", CONFIG.peRainforestBiome);
        settings.putString("savanna", CONFIG.peSavannaBiome);
        settings.putString("shrubland", CONFIG.peShrublandBiome);
        settings.putString("seasonal_forest", CONFIG.peSeasonalForestBiome);
        settings.putString("swampland", CONFIG.peSwamplandBiome);
        settings.putString("taiga", CONFIG.peTaigaBiome);
        settings.putString("tundra", CONFIG.peTundraBiome);
        
        settings.putString("ocean", CONFIG.peOceanBiome);
        settings.putString("cold_ocean", CONFIG.peColdOceanBiome);
        settings.putString("frozen_ocean", CONFIG.peFrozenOceanBiome);
        settings.putString("lukewarm_ocean", CONFIG.peLukewarmOceanBiome);
        settings.putString("warm_ocean", CONFIG.peWarmOceanBiome);
        
        return settings;
    }
    
    public static NbtCompound createSettingsVanilla() {
        NbtCompound settings = createSettingsOceans(BuiltInTypes.Biome.VANILLA.name);
        
        settings.putBoolean(NbtTags.VANILLA_LARGE_BIOMES, CONFIG.vanillaLargeBiomes);
        
        return settings;
    }
}
