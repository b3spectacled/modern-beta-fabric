package com.bespectacled.modernbeta.config;

import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.world.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.world.biome.classic.ClassicBiomes;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "biome_config")
public class ModernBetaBiomeConfig implements ConfigData {
    @ConfigEntry.Gui.Excluded
    public String biomeType = BuiltInTypes.Biome.BETA.name;
    
    @ConfigEntry.Gui.Excluded
    public String singleBiome = ClassicBiomes.ALPHA_ID.toString();
    
    @ConfigEntry.Gui.Excluded
    public int vanillaBiomeSize = 4;
    
    @ConfigEntry.Gui.Excluded
    public int vanillaOceanBiomeSize = 6;
    
    @ConfigEntry.Gui.Excluded
    public String betaDesertBiome = BetaBiomes.DESERT_ID.toString();
    
    @ConfigEntry.Gui.Excluded
    public String betaForestBiome = BetaBiomes.FOREST_ID.toString();
    
    @ConfigEntry.Gui.Excluded
    public String betaIceDesertBiome = BetaBiomes.TUNDRA_ID.toString();
    
    @ConfigEntry.Gui.Excluded
    public String betaPlainsBiome = BetaBiomes.PLAINS_ID.toString();
    
    @ConfigEntry.Gui.Excluded
    public String betaRainforestBiome = BetaBiomes.RAINFOREST_ID.toString();
    
    @ConfigEntry.Gui.Excluded
    public String betaSavannaBiome = BetaBiomes.SAVANNA_ID.toString();
    
    @ConfigEntry.Gui.Excluded
    public String betaShrublandBiome = BetaBiomes.SHRUBLAND_ID.toString();
    
    @ConfigEntry.Gui.Excluded
    public String betaSeasonalForestBiome = BetaBiomes.SEASONAL_FOREST_ID.toString();
    
    @ConfigEntry.Gui.Excluded
    public String betaSwamplandBiome = BetaBiomes.SWAMPLAND_ID.toString();
    
    @ConfigEntry.Gui.Excluded
    public String betaTaigaBiome = BetaBiomes.TAIGA_ID.toString();
    
    @ConfigEntry.Gui.Excluded
    public String betaTundraBiome = BetaBiomes.TUNDRA_ID.toString();
    
    @ConfigEntry.Gui.Excluded
    public String betaOceanBiome = BetaBiomes.OCEAN_ID.toString();
    
    @ConfigEntry.Gui.Excluded
    public String betaColdOceanBiome = BetaBiomes.COLD_OCEAN_ID.toString();
    
    @ConfigEntry.Gui.Excluded
    public String betaFrozenOceanBiome = BetaBiomes.FROZEN_OCEAN_ID.toString();
    
    @ConfigEntry.Gui.Excluded
    public String betaLukewarmOceanBiome = BetaBiomes.LUKEWARM_OCEAN_ID.toString();
    
    @ConfigEntry.Gui.Excluded
    public String betaWarmOceanBiome = BetaBiomes.WARM_OCEAN_ID.toString();
    
}
