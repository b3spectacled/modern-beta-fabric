package com.bespectacled.modernbeta.compat;

import java.util.Arrays;
import java.util.List;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public class CompatBiomes {
    public static final List<RegistryKey<Biome>> BIOMES_WITH_CUSTOM_SURFACES = Arrays.asList(
        // Badlands
        BiomeKeys.BADLANDS,
        BiomeKeys.WOODED_BADLANDS,
        BiomeKeys.ERODED_BADLANDS,
        
        // Extreme Hills
        BiomeKeys.WINDSWEPT_HILLS,
        BiomeKeys.WINDSWEPT_GRAVELLY_HILLS,
        
        // Mountains
        BiomeKeys.GROVE,
        BiomeKeys.SNOWY_SLOPES,
        BiomeKeys.FROZEN_OCEAN,
        BiomeKeys.JAGGED_PEAKS,
        BiomeKeys.STONY_PEAKS,
        
        // Giant Taigas
        BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA,
        BiomeKeys.OLD_GROWTH_PINE_TAIGA,
        
        // Savanna
        BiomeKeys.WINDSWEPT_SAVANNA,
        
        // Swamp
        BiomeKeys.SWAMP,
        
        // Nether
        BiomeKeys.NETHER_WASTES,
        BiomeKeys.WARPED_FOREST,
        BiomeKeys.CRIMSON_FOREST,
        BiomeKeys.BASALT_DELTAS,
        BiomeKeys.SOUL_SAND_VALLEY
    );
}
