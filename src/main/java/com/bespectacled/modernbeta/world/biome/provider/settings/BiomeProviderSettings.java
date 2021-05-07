package com.bespectacled.modernbeta.world.biome.provider.settings;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.biome.BiomeKeys;

public class BiomeProviderSettings {
    public static NbtCompound createSettingsBase(String biomeType) {
        NbtCompound settings = new NbtCompound();
        
        settings.putString("biomeType", biomeType);
        
        return settings;
    }
    
    public static NbtCompound createSettingsAll(String biomeType) {
        NbtCompound settings = createSettingsBase(biomeType);
        
        settings.putString("desert", ModernBeta.BIOME_CONFIG.betaDesertBiome);
        settings.putString("forest", ModernBeta.BIOME_CONFIG.betaForestBiome);
        settings.putString("ice_desert", ModernBeta.BIOME_CONFIG.betaIceDesertBiome);
        settings.putString("plains", ModernBeta.BIOME_CONFIG.betaPlainsBiome);
        settings.putString("rainforest", ModernBeta.BIOME_CONFIG.betaRainforestBiome);
        settings.putString("savanna", ModernBeta.BIOME_CONFIG.betaSavannaBiome);
        settings.putString("shrubland", ModernBeta.BIOME_CONFIG.betaShrublandBiome);
        settings.putString("seasonal_forest", ModernBeta.BIOME_CONFIG.betaSeasonalForestBiome);
        settings.putString("swampland", ModernBeta.BIOME_CONFIG.betaSwamplandBiome);
        settings.putString("taiga", ModernBeta.BIOME_CONFIG.betaTaigaBiome);
        settings.putString("tundra", ModernBeta.BIOME_CONFIG.betaTundraBiome);
        
        settings.putString("ocean", ModernBeta.BIOME_CONFIG.betaOceanBiome);
        settings.putString("cold_ocean", ModernBeta.BIOME_CONFIG.betaColdOceanBiome);
        settings.putString("frozen_ocean", ModernBeta.BIOME_CONFIG.betaFrozenOceanBiome);
        settings.putString("lukewarm_ocean", ModernBeta.BIOME_CONFIG.betaLukewarmOceanBiome);
        settings.putString("warm_ocean", ModernBeta.BIOME_CONFIG.betaWarmOceanBiome);
        
        settings.putString("singleBiome", ModernBeta.BIOME_CONFIG.singleBiome);
        
        settings.putInt("vanillaBiomeSize", ModernBeta.BIOME_CONFIG.vanillaBiomeSize);
        settings.putInt("vanillaOceanBiomeSize", ModernBeta.BIOME_CONFIG.vanillaOceanBiomeSize);
        
        return settings;
    }
    
    /* Beta Biome Presets */
    
    public static NbtCompound createBetaSettingsBase() {
        return new NbtCompound();
    }
    
    public static NbtCompound createBetaSettingsPlus() {
        NbtCompound settings = createBetaSettingsBase();
        
        settings.putString("desert", BiomeKeys.DESERT.getValue().toString());
        settings.putString("forest", BiomeKeys.FOREST.getValue().toString());
        settings.putString("ice_desert", BiomeKeys.ICE_SPIKES.getValue().toString());
        settings.putString("plains", BiomeKeys.PLAINS.getValue().toString());
        settings.putString("rainforest", BiomeKeys.JUNGLE.getValue().toString());
        settings.putString("savanna", BiomeKeys.SAVANNA.getValue().toString());
        settings.putString("shrubland", BiomeKeys.PLAINS.getValue().toString());
        settings.putString("seasonal_forest", BiomeKeys.DARK_FOREST.getValue().toString());
        settings.putString("swampland", BiomeKeys.SWAMP.getValue().toString());
        settings.putString("taiga", BiomeKeys.SNOWY_TAIGA.getValue().toString());
        settings.putString("tundra", BiomeKeys.SNOWY_TUNDRA.getValue().toString());
        
        settings.putString("ocean", BiomeKeys.OCEAN.getValue().toString());
        settings.putString("cold_ocean", BiomeKeys.COLD_OCEAN.getValue().toString());
        settings.putString("frozen_ocean", BiomeKeys.FROZEN_OCEAN.getValue().toString());
        settings.putString("lukewarm_ocean", BiomeKeys.LUKEWARM_OCEAN.getValue().toString());
        settings.putString("warm_ocean", BiomeKeys.WARM_OCEAN.getValue().toString());
        
        return settings;
    }
}
