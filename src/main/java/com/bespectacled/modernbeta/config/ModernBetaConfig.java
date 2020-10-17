package com.bespectacled.modernbeta.config;

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
    
    /* Generation */

    @ConfigEntry.Gui.Excluded
    //@ConfigEntry.Category(value = "betaGen")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean generateOceans = false;

    @ConfigEntry.Gui.Excluded
    //@ConfigEntry.Category(value = "betaGen")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean generateIceDesert = false;

    @ConfigEntry.Gui.Excluded
    //@ConfigEntry.Category(value = "betaGen")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean alphaWinterMode = false;

    @ConfigEntry.Gui.Excluded
    //@ConfigEntry.Category(value = "betaGen")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean alphaPlus = false;
    
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
    
}
