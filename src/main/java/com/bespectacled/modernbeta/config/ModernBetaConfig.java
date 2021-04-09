package com.bespectacled.modernbeta.config;

import com.bespectacled.modernbeta.ModernBeta;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

//import me.sargunvohra.mcmods.autoconfig1u

@Config(name = ModernBeta.MOD_ID)
public class ModernBetaConfig extends PartitioningSerializer.GlobalData {
    @ConfigEntry.Category("rendering_config")
    @ConfigEntry.Gui.TransitiveObject
    public ModernBetaRenderingConfig renderingConfig = new ModernBetaRenderingConfig();
    
    @ConfigEntry.Category("generation_config")
    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Gui.Excluded
    public ModernBetaGenerationConfig generationConfig = new ModernBetaGenerationConfig();
 
    @ConfigEntry.Category("biome_config")
    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Gui.Excluded
    public ModernBetaBiomeConfig biomeConfig = new ModernBetaBiomeConfig();
}




