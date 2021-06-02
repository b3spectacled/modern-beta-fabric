package com.bespectacled.modernbeta.config;

import com.bespectacled.modernbeta.ModernBeta;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

@Config(name = ModernBeta.MOD_ID)
public class ModernBetaConfig extends PartitioningSerializer.GlobalData {
    @ConfigEntry.Category("rendering_config")
    @ConfigEntry.Gui.TransitiveObject
    public ModernBetaRenderingConfig rendering_config = new ModernBetaRenderingConfig();
    
    // Workaround: Put generation and biome configs in renderingConfig categories so they don't show up in Mod Menu config.
    
    @ConfigEntry.Category("rendering_config") 
    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Gui.Excluded
    public ModernBetaGenerationConfig generation_config = new ModernBetaGenerationConfig();
 
    @ConfigEntry.Category("rendering_config")
    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Gui.Excluded
    public ModernBetaBiomeConfig biome_config = new ModernBetaBiomeConfig();
}
