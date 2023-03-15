package mod.bespectacled.modernbeta.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import mod.bespectacled.modernbeta.ModernBeta;

@Config(name = ModernBeta.MOD_ID)
public class ModernBetaConfig implements ConfigData {
    @ConfigEntry.Category(value = "fixedSeed")
    @ConfigEntry.Gui.Tooltip(count = 4)
    public String fixedSeed = "";

    @ConfigEntry.Category(value = "fixedSeed")
    @ConfigEntry.Gui.Tooltip(count = 3)
    public boolean useFixedSeed = false;
    
    @ConfigEntry.Category(value = "betaBiomeColor")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean useBetaSkyColor = true;

    @ConfigEntry.Category(value = "betaBiomeColor")
    @ConfigEntry.Gui.Tooltip(count = 3)
    public boolean useBetaBiomeColor = true;

    @ConfigEntry.Category(value = "betaBiomeColor")
    @ConfigEntry.Gui.Tooltip(count = 3)
    public boolean useBetaWaterColor = false;

    @ConfigEntry.Category(value = "peBiomeColor")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean usePEBetaSkyColor = false;

    @ConfigEntry.Category(value = "peBiomeColor")
    @ConfigEntry.Gui.Tooltip(count = 3)
    public boolean usePEBetaBiomeColor = false;

    @ConfigEntry.Category(value = "peBiomeColor")
    @ConfigEntry.Gui.Tooltip(count = 3)
    public boolean usePEBetaWaterColor = false;

    @ConfigEntry.Category(value = "other")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean useOldFogColor = true;
}
