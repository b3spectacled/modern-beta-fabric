package com.bespectacled.modernbeta.config;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.BiomeType;
import com.bespectacled.modernbeta.biome.indev.IndevUtil;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

@Config(name = "modernbeta")
public class ModernBetaConfig implements ConfigData {
    
    /* Render */
    
    @ConfigEntry.Gui.Tooltip(count = 4)
    public long fixedSeed = 0L;
    
    @ConfigEntry.Gui.Tooltip(count = 3)
    public boolean useFixedSeed = false;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean renderBetaSkyColor = true;

    @ConfigEntry.Gui.Tooltip(count = 3)
    public boolean renderBetaBiomeColor = true;

    @ConfigEntry.Gui.Tooltip(count = 1)
    public boolean renderAlphaSunset = false;
    
    @ConfigEntry.Gui.Tooltip(count = 1)
    public boolean renderGameVersion = false;
    
    /* Inf Generation */
    
    @ConfigEntry.Gui.Excluded
    public boolean generateOceans = true;
    
    @ConfigEntry.Gui.Excluded
    public boolean generateDeepOceans = false;
    
    @ConfigEntry.Gui.Excluded
    public boolean generateNoiseCaves = true;
    
    @ConfigEntry.Gui.Excluded
    public boolean generateAquifers = true;
    
    @ConfigEntry.Gui.Excluded
    public boolean generateDeepslate = true;
    
    @ConfigEntry.Gui.Excluded
    public String biomeType = BiomeType.BETA.getName();
    
    @ConfigEntry.Gui.Excluded
    public String singleBiome = ModernBeta.createId("alpha").toString();
    
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
    
}
