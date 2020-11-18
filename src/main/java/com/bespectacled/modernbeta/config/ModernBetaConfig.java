package com.bespectacled.modernbeta.config;

import com.bespectacled.modernbeta.util.WorldEnum;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

@Config(name = "modernbeta")
public class ModernBetaConfig implements ConfigData {
    
    /* Render */
    
    //@ConfigEntry.Category(value = "betaRender")
    @ConfigEntry.Gui.Tooltip(count = 4)
    public long fixedSeed = 0L;

    //@ConfigEntry.Category(value = "betaRender")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean renderBetaSkyColor = true;

    //@ConfigEntry.Category(value = "betaRender")
    @ConfigEntry.Gui.Tooltip(count = 3)
    public boolean renderBetaBiomeColor = true;

    //@ConfigEntry.Category(value = "betaRender")
    @ConfigEntry.Gui.Tooltip(count = 1)
    public boolean renderAlphaSunset = false;
    
    /* Beta Generation */

    // Legacy option, replaced with generateBetaOceans
    @ConfigEntry.Gui.Excluded
    public boolean generateOceans = false; 
    
    @ConfigEntry.Gui.Excluded
    public boolean generateBetaOceans = true;
    
    @ConfigEntry.Gui.Excluded
    public int betaBiomeType = WorldEnum.BetaBiomeType.CLASSIC.ordinal();
    
    /* Alpha, Infdev, Old Infdev Generation */

    @ConfigEntry.Gui.Excluded
    public int preBetaBiomeType = WorldEnum.PreBetaBiomeType.CLASSIC.ordinal();

    /* Old Infdev Generation */
    
    @ConfigEntry.Gui.Excluded
    public boolean generateInfdevPyramid = true;
    
    @ConfigEntry.Gui.Excluded
    public boolean generateInfdevWall = true;
    
    /* Indev Generation */
    
    @ConfigEntry.Gui.Excluded
    public int indevLevelType = 0;
    
    @ConfigEntry.Gui.Excluded
    public int indevLevelTheme = 0;
    
    @ConfigEntry.Gui.Excluded
    public int indevLevelWidth = 256;
    
    @ConfigEntry.Gui.Excluded
    public int indevLevelLength = 256;
    
    @ConfigEntry.Gui.Excluded
    public int indevLevelHeight = 128;
    
    @ConfigEntry.Gui.Excluded
    public float indevCaveRadius = 1.0f;
    
}
