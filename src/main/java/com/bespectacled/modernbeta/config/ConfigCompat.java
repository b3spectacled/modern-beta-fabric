package com.bespectacled.modernbeta.config;

import java.util.Arrays;
import java.util.List;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.world.biome.BiomeKeys;

@Config(name = "compat_config")
public class ConfigCompat implements ConfigData {
    @ConfigEntry.Gui.Excluded
    public List<String> biomesWithCustomSurfaces = Arrays.asList(
        // Badlands
        BiomeKeys.BADLANDS.getValue().toString(),
        BiomeKeys.WOODED_BADLANDS.getValue().toString(),
        BiomeKeys.ERODED_BADLANDS.getValue().toString(),
        
        // Mountains
        BiomeKeys.WINDSWEPT_HILLS.getValue().toString(),
        BiomeKeys.WINDSWEPT_GRAVELLY_HILLS.getValue().toString(),
        
        // Giant Taigas
        BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA.getValue().toString(),
        BiomeKeys.OLD_GROWTH_PINE_TAIGA.getValue().toString(),
        
        // Savanna
        BiomeKeys.WINDSWEPT_SAVANNA.getValue().toString(),
        
        // Swamp
        BiomeKeys.SWAMP.getValue().toString(),
        
        // Nether
        BiomeKeys.NETHER_WASTES.getValue().toString(),
        BiomeKeys.WARPED_FOREST.getValue().toString(),
        BiomeKeys.CRIMSON_FOREST.getValue().toString(),
        BiomeKeys.BASALT_DELTAS.getValue().toString(),
        BiomeKeys.SOUL_SAND_VALLEY.getValue().toString()
    );
}
