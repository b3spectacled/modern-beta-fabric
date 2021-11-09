package com.bespectacled.modernbeta.world.biome.provider.settings;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.util.NbtTags;

import net.minecraft.nbt.NbtCompound;

public class BiomeProviderSettings {
    public static NbtCompound createSettingsBase(String biomeType) {
        NbtCompound settings = new NbtCompound();
        
        settings.putString(NbtTags.BIOME_TYPE, biomeType);
        
        return settings;
    }
    
    public static NbtCompound createSettingsBeta() {
        NbtCompound settings = createSettingsBase(BuiltInTypes.Biome.BETA.name);
        
        settings.putString("desert", ModernBeta.BIOME_CONFIG.betaBiomeConfig.betaDesertBiome);
        settings.putString("forest", ModernBeta.BIOME_CONFIG.betaBiomeConfig.betaForestBiome);
        settings.putString("ice_desert", ModernBeta.BIOME_CONFIG.betaBiomeConfig.betaIceDesertBiome);
        settings.putString("plains", ModernBeta.BIOME_CONFIG.betaBiomeConfig.betaPlainsBiome);
        settings.putString("rainforest", ModernBeta.BIOME_CONFIG.betaBiomeConfig.betaRainforestBiome);
        settings.putString("savanna", ModernBeta.BIOME_CONFIG.betaBiomeConfig.betaSavannaBiome);
        settings.putString("shrubland", ModernBeta.BIOME_CONFIG.betaBiomeConfig.betaShrublandBiome);
        settings.putString("seasonal_forest", ModernBeta.BIOME_CONFIG.betaBiomeConfig.betaSeasonalForestBiome);
        settings.putString("swampland", ModernBeta.BIOME_CONFIG.betaBiomeConfig.betaSwamplandBiome);
        settings.putString("taiga", ModernBeta.BIOME_CONFIG.betaBiomeConfig.betaTaigaBiome);
        settings.putString("tundra", ModernBeta.BIOME_CONFIG.betaBiomeConfig.betaTundraBiome);
        
        settings.putString("ocean", ModernBeta.BIOME_CONFIG.betaBiomeConfig.betaOceanBiome);
        settings.putString("cold_ocean", ModernBeta.BIOME_CONFIG.betaBiomeConfig.betaColdOceanBiome);
        settings.putString("frozen_ocean", ModernBeta.BIOME_CONFIG.betaBiomeConfig.betaFrozenOceanBiome);
        settings.putString("lukewarm_ocean", ModernBeta.BIOME_CONFIG.betaBiomeConfig.betaLukewarmOceanBiome);
        settings.putString("warm_ocean", ModernBeta.BIOME_CONFIG.betaBiomeConfig.betaWarmOceanBiome);
        
        return settings;
    }
    
    public static NbtCompound createSettingsSingle() {
        NbtCompound settings = createSettingsBase(BuiltInTypes.Biome.SINGLE.name);

        settings.putString(NbtTags.SINGLE_BIOME, ModernBeta.BIOME_CONFIG.singleBiomeConfig.singleBiome);
        
        return settings;
    }
    
    public static NbtCompound createSettingsPE() {
        NbtCompound settings = createSettingsBase(BuiltInTypes.Biome.PE.name);
        
        settings.putString("desert", ModernBeta.BIOME_CONFIG.peBiomeConfig.peDesertBiome);
        settings.putString("forest", ModernBeta.BIOME_CONFIG.peBiomeConfig.peForestBiome);
        settings.putString("ice_desert", ModernBeta.BIOME_CONFIG.peBiomeConfig.peIceDesertBiome);
        settings.putString("plains", ModernBeta.BIOME_CONFIG.peBiomeConfig.pePlainsBiome);
        settings.putString("rainforest", ModernBeta.BIOME_CONFIG.peBiomeConfig.peRainforestBiome);
        settings.putString("savanna", ModernBeta.BIOME_CONFIG.peBiomeConfig.peSavannaBiome);
        settings.putString("shrubland", ModernBeta.BIOME_CONFIG.peBiomeConfig.peShrublandBiome);
        settings.putString("seasonal_forest", ModernBeta.BIOME_CONFIG.peBiomeConfig.peSeasonalForestBiome);
        settings.putString("swampland", ModernBeta.BIOME_CONFIG.peBiomeConfig.peSwamplandBiome);
        settings.putString("taiga", ModernBeta.BIOME_CONFIG.peBiomeConfig.peTaigaBiome);
        settings.putString("tundra", ModernBeta.BIOME_CONFIG.peBiomeConfig.peTundraBiome);
        
        settings.putString("ocean", ModernBeta.BIOME_CONFIG.peBiomeConfig.peOceanBiome);
        settings.putString("cold_ocean", ModernBeta.BIOME_CONFIG.peBiomeConfig.peColdOceanBiome);
        settings.putString("frozen_ocean", ModernBeta.BIOME_CONFIG.peBiomeConfig.peFrozenOceanBiome);
        settings.putString("lukewarm_ocean", ModernBeta.BIOME_CONFIG.peBiomeConfig.peLukewarmOceanBiome);
        settings.putString("warm_ocean", ModernBeta.BIOME_CONFIG.peBiomeConfig.peWarmOceanBiome);
        
        return settings;
    }
    
    public static NbtCompound createSettingsVanilla() {
        return createSettingsBase(BuiltInTypes.Biome.VANILLA.name);
    }
}
