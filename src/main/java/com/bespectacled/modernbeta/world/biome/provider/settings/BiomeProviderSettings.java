package com.bespectacled.modernbeta.world.biome.provider.settings;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.nbt.CompoundTag;

public class BiomeProviderSettings {
    public static CompoundTag createBiomeSettings(String biomeType, String singleBiome) {
        CompoundTag settings = new CompoundTag();
        
        settings.putString("biomeType", biomeType);
        settings.putString("singleBiome", singleBiome);
        
        return settings;
    }

    public static CompoundTag addBetaBiomeSettings(CompoundTag settings) {
        settings.putString("desert", ModernBeta.BETA_CONFIG.biome_config.betaDesertBiome);
        settings.putString("forest", ModernBeta.BETA_CONFIG.biome_config.betaForestBiome);
        settings.putString("ice_desert", ModernBeta.BETA_CONFIG.biome_config.betaIceDesertBiome);
        settings.putString("plains", ModernBeta.BETA_CONFIG.biome_config.betaPlainsBiome);
        settings.putString("rainforest", ModernBeta.BETA_CONFIG.biome_config.betaRainforestBiome);
        settings.putString("savanna", ModernBeta.BETA_CONFIG.biome_config.betaSavannaBiome);
        settings.putString("shrubland", ModernBeta.BETA_CONFIG.biome_config.betaShrublandBiome);
        settings.putString("seasonal_forest", ModernBeta.BETA_CONFIG.biome_config.betaSeasonalForestBiome);
        settings.putString("swampland", ModernBeta.BETA_CONFIG.biome_config.betaSwamplandBiome);
        settings.putString("taiga", ModernBeta.BETA_CONFIG.biome_config.betaTaigaBiome);
        settings.putString("tundra", ModernBeta.BETA_CONFIG.biome_config.betaTundraBiome);
        
        settings.putString("ocean", ModernBeta.BETA_CONFIG.biome_config.betaOceanBiome);
        settings.putString("cold_ocean", ModernBeta.BETA_CONFIG.biome_config.betaColdOceanBiome);
        settings.putString("frozen_ocean", ModernBeta.BETA_CONFIG.biome_config.betaFrozenOceanBiome);
        settings.putString("lukewarm_ocean", ModernBeta.BETA_CONFIG.biome_config.betaLukewarmOceanBiome);
        settings.putString("warm_ocean", ModernBeta.BETA_CONFIG.biome_config.betaWarmOceanBiome);
        
        return settings;
    }
    
    public static CompoundTag addVanillaBiomeSettings(CompoundTag settings) {
        settings.putInt("vanillaBiomeSize", ModernBeta.BETA_CONFIG.biome_config.vanillaBiomeSize);
        settings.putInt("vanillaOceanBiomeSize", ModernBeta.BETA_CONFIG.biome_config.vanillaOceanBiomeSize);
        
        return settings;
    }

}
