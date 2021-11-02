package com.bespectacled.modernbeta.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "rendering_config")
public class ConfigRendering implements ConfigData {
    
    @ConfigEntry.Gui.CollapsibleObject
    public FixedSeedConfig fixedSeedConfig = new FixedSeedConfig();
    
    @ConfigEntry.Gui.CollapsibleObject
    public BiomeColorConfig biomeColorConfig = new BiomeColorConfig();
    
    @ConfigEntry.Gui.CollapsibleObject
    public OtherConfig otherConfig = new OtherConfig();
    
    public static class FixedSeedConfig {
        @ConfigEntry.Gui.Tooltip(count = 4)
        public String fixedSeed = "";
        
        @ConfigEntry.Gui.Tooltip(count = 3)
        public boolean useFixedSeed = false;
    }
    
    public static class BiomeColorConfig {
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean renderBetaSkyColor = true;

        @ConfigEntry.Gui.Tooltip(count = 3)
        public boolean renderBetaBiomeColor = true;
        
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean renderPEBetaSkyColor = false;

        @ConfigEntry.Gui.Tooltip(count = 3)
        public boolean renderPEBetaBiomeColor = false;
        
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean renderOldFogColor = true;
    }
    
    public static class OtherConfig {
        @ConfigEntry.Gui.Tooltip(count = 1)
        public boolean renderAlphaSunset = false;

        @ConfigEntry.Gui.Tooltip(count = 1)
        public boolean renderGameVersion = false;
        
        @ConfigEntry.Gui.Tooltip(count = 1)
        public boolean renderLowClouds = false;
    }
}
