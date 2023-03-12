package mod.bespectacled.modernbeta.config;

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import mod.bespectacled.modernbeta.ModernBeta;

@Config(name = ModernBeta.MOD_ID)
public class ModernBetaConfig extends PartitioningSerializer.GlobalData {
    @ConfigEntry.Gui.TransitiveObject
    public ModernBetaConfigRendering renderingConfig = new ModernBetaConfigRendering();
    
    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Gui.Excluded
    public ModernBetaConfigGeneration generationConfig = new ModernBetaConfigGeneration();
 
    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Gui.Excluded
    public ModernBetaConfigBiome biomeConfig = new ModernBetaConfigBiome();
    
    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Gui.Excluded
    public ModernBetaConfigCaveBiome caveBiomeConfig = new ModernBetaConfigCaveBiome();
    
    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Gui.Excluded
    public ModernBetaConfigCompat compatConfig = new ModernBetaConfigCompat();
}
