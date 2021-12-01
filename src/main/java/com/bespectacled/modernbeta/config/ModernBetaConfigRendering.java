package com.bespectacled.modernbeta.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.client.render.DimensionEffects;

@Config(name = "config_rendering")
public class ModernBetaConfigRendering implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public ConfigFixedSeed configFixedSeed = new ConfigFixedSeed();
    
    @ConfigEntry.Gui.CollapsibleObject
    public ConfigBiomeColor configBiomeColor = new ConfigBiomeColor();
    
    @ConfigEntry.Gui.CollapsibleObject
    public ConfigOther configOther = new ConfigOther();
    
    public static class ConfigFixedSeed {
        @ConfigEntry.Gui.Tooltip(count = 4)
        public String fixedSeed = "";
        
        @ConfigEntry.Gui.Tooltip(count = 3)
        public boolean useFixedSeed = false;
    }
    
    public static class ConfigBiomeColor {
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
    
    public static class ConfigOther {
        @ConfigEntry.Gui.Tooltip(count = 1)
        public boolean renderAlphaSunset = false;

        @ConfigEntry.Gui.Tooltip(count = 1)
        public boolean renderGameVersion = false;
        
        @ConfigEntry.Gui.Tooltip(count = 1)
        @ConfigEntry.BoundedDiscrete(min = 0, max = 320)
        public int cloudHeight = DimensionEffects.Overworld.CLOUDS_HEIGHT;
    }
}
