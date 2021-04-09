package com.bespectacled.modernbeta.config;

import com.bespectacled.modernbeta.world.biome.indev.IndevUtil;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "generation_config")
public class ModernBetaGenerationConfig implements ConfigData {
    
    /* Inf Generation */
    
    @ConfigEntry.Gui.Excluded
    public boolean generateOceans = true;
    
    /* Old Infdev Generation */
    
    @ConfigEntry.Gui.Excluded
    public boolean generateInfdevPyramid = true;
    
    @ConfigEntry.Gui.Excluded
    public boolean generateInfdevWall = true;
    
    /* Indev Generation */
    
    @ConfigEntry.Gui.Excluded
    public String indevLevelType = IndevUtil.IndevType.ISLAND.getName();
    
    @ConfigEntry.Gui.Excluded
    public String indevLevelTheme = IndevUtil.IndevTheme.NORMAL.getName();
    
    @ConfigEntry.Gui.Excluded
    public int indevLevelWidth = 256;
    
    @ConfigEntry.Gui.Excluded
    public int indevLevelLength = 256;
    
    @ConfigEntry.Gui.Excluded
    public int indevLevelHeight = 128;
    
    @ConfigEntry.Gui.Excluded
    public float indevCaveRadius = 1.0f;
    
    /* Beta Islands Generation */
    
    @ConfigEntry.Gui.Excluded
    public int centerOceanLerpDistance = 16;
    
    @ConfigEntry.Gui.Excluded
    public int centerOceanRadius = 64;
    
    @ConfigEntry.Gui.Excluded
    public float centerIslandFalloff = 4.0F;
    
    @ConfigEntry.Gui.Excluded
    public float outerIslandNoiseScale = 300F;
    
    @ConfigEntry.Gui.Excluded
    public float outerIslandNoiseOffset = 0.25F;
    
}