package mod.bespectacled.modernbeta.config;

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
        public boolean useBetaSkyColor = true;

        @ConfigEntry.Gui.Tooltip(count = 3)
        public boolean useBetaBiomeColor = true;
        
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean usePEBetaSkyColor = false;

        @ConfigEntry.Gui.Tooltip(count = 3)
        public boolean usePEBetaBiomeColor = false;
        
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean useOldFogColor = true;
    }
    
    public static class ConfigOther {
        @ConfigEntry.Gui.Tooltip(count = 1)
        public boolean useAlphaSunset = false;

        @ConfigEntry.Gui.Tooltip(count = 1)
        public boolean useGameVersion = false;
        
        @ConfigEntry.Gui.Tooltip(count = 1)
        @ConfigEntry.BoundedDiscrete(min = 0, max = 320)
        public int cloudHeight = DimensionEffects.Overworld.CLOUDS_HEIGHT;
    }
}
