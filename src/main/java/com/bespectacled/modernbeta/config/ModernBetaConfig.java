package com.bespectacled.modernbeta.config;

import com.bespectacled.modernbeta.ModernBeta;

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

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
    public ModernBetaConfigCompat compatConfig = new ModernBetaConfigCompat();
}
