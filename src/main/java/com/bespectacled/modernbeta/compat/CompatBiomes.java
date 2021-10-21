package com.bespectacled.modernbeta.compat;

import java.util.Arrays;
import java.util.List;

import net.minecraft.world.biome.BiomeKeys;

public class CompatBiomes {
    public static final List<String> BIOMES_WITH_CUSTOM_SURFACES = Arrays.asList(
        // Badlands
        BiomeKeys.BADLANDS.getValue().toString(),
        BiomeKeys.WOODED_BADLANDS.getValue().toString(),
        BiomeKeys.ERODED_BADLANDS.getValue().toString(),
        
        // Extreme Hills
        BiomeKeys.WINDSWEPT_HILLS.getValue().toString(),
        BiomeKeys.WINDSWEPT_GRAVELLY_HILLS.getValue().toString(),
        
        // Mountains
        BiomeKeys.GROVE.getValue().toString(),
        BiomeKeys.SNOWY_SLOPES.getValue().toString(),
        BiomeKeys.FROZEN_OCEAN.getValue().toString(),
        BiomeKeys.JAGGED_PEAKS.getValue().toString(),
        BiomeKeys.STONY_PEAKS.getValue().toString(),
        
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
