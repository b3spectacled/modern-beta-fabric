package com.bespectacled.modernbeta.config;

import com.bespectacled.modernbeta.ModernBeta;

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

@Config(name = ModernBeta.MOD_ID)
public class ModernBetaConfig extends PartitioningSerializer.GlobalData {
    @ConfigEntry.Gui.TransitiveObject
    public ConfigRendering renderingConfig = new ConfigRendering();
    
    // Workaround: Put generation and biome configs in renderingConfig categories so they don't show up in Mod Menu config.
    
    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Gui.Excluded
    public ConfigGeneration generationConfig = new ConfigGeneration();
 
    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Gui.Excluded
    public ConfigBiome biomeConfig = new ConfigBiome();
    
    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Gui.Excluded
    public ConfigCompat compatConfig = new ConfigCompat();
}
