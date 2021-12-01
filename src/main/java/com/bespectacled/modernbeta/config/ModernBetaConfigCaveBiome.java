package com.bespectacled.modernbeta.config;

import com.bespectacled.modernbeta.api.registry.BuiltInTypes;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.minecraft.world.biome.BiomeKeys;

@Config(name = "config_cave_biome")
public class ModernBetaConfigCaveBiome implements ConfigData {
    // General
    public String caveBiomeType = BuiltInTypes.CaveBiome.SINGLE.name;
    
    // Noise
    public int horizontalNoiseScale = 32;
    public int verticalNoiseScale = 16;
    
    // Single
    public String singleBiome = BiomeKeys.LUSH_CAVES.getValue().toString();
    public boolean useNoise = true;
}
