package com.bespectacled.modernbeta.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "rendering_config")
public class ModernBetaRenderingConfig implements ConfigData {
    
    @ConfigEntry.Gui.Tooltip(count = 4)
    public long fixedSeed = 0L;
    
    @ConfigEntry.Gui.Tooltip(count = 3)
    public boolean useFixedSeed = false;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean renderBetaSkyColor = true;

    @ConfigEntry.Gui.Tooltip(count = 3)
    public boolean renderBetaBiomeColor = true;
    
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean renderOldFogColor = true;

    @ConfigEntry.Gui.Tooltip(count = 1)
    public boolean renderAlphaSunset = false;
    
    @ConfigEntry.Gui.Tooltip(count = 1)
    public boolean renderGameVersion = false;
    
    
}
