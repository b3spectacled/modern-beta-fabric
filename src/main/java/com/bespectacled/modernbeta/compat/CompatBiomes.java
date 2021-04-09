package com.bespectacled.modernbeta.compat;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.util.Identifier;
import net.minecraft.world.biome.BiomeKeys;

public class CompatBiomes {
    // Set for specifying which biomes should use their vanilla surface builders.
    // Done on per-biome basis for best mod compatibility.
    private static final Set<Identifier> BIOMES_WITH_CUSTOM_SURFACES = new HashSet<Identifier>();
    
    public static void addBiomeWithCustomSurface(Identifier key) {
        if (BIOMES_WITH_CUSTOM_SURFACES.contains(key)) 
            throw new IllegalArgumentException("[Modern Beta] Custom surfaces already contains biome named " + key.toString());
        
        BIOMES_WITH_CUSTOM_SURFACES.add(key);
    }
    
    public static boolean hasCustomSurface(Identifier key) {
        if (BIOMES_WITH_CUSTOM_SURFACES.contains(key))
            return true;
        
        return false;
    }
    
    static {
        // Badlands
        addBiomeWithCustomSurface(BiomeKeys.BADLANDS.getValue());
        addBiomeWithCustomSurface(BiomeKeys.BADLANDS_PLATEAU.getValue());
        addBiomeWithCustomSurface(BiomeKeys.ERODED_BADLANDS.getValue());
        addBiomeWithCustomSurface(BiomeKeys.MODIFIED_BADLANDS_PLATEAU.getValue());
        addBiomeWithCustomSurface(BiomeKeys.MODIFIED_WOODED_BADLANDS_PLATEAU.getValue());
        addBiomeWithCustomSurface(BiomeKeys.WOODED_BADLANDS_PLATEAU.getValue());
        
        // Mountains
        addBiomeWithCustomSurface(BiomeKeys.MOUNTAINS.getValue());
        addBiomeWithCustomSurface(BiomeKeys.GRAVELLY_MOUNTAINS.getValue());
        addBiomeWithCustomSurface(BiomeKeys.MODIFIED_GRAVELLY_MOUNTAINS.getValue());
        
        // Giant Taigas
        addBiomeWithCustomSurface(BiomeKeys.GIANT_TREE_TAIGA.getValue());
        addBiomeWithCustomSurface(BiomeKeys.GIANT_TREE_TAIGA_HILLS.getValue());
        addBiomeWithCustomSurface(BiomeKeys.GIANT_SPRUCE_TAIGA.getValue());
        addBiomeWithCustomSurface(BiomeKeys.GIANT_SPRUCE_TAIGA_HILLS.getValue());
        
        // Savanna
        addBiomeWithCustomSurface(BiomeKeys.SHATTERED_SAVANNA.getValue());
        addBiomeWithCustomSurface(BiomeKeys.SHATTERED_SAVANNA_PLATEAU.getValue());
        
        // Swamp
        addBiomeWithCustomSurface(BiomeKeys.SWAMP.getValue());
        addBiomeWithCustomSurface(BiomeKeys.SWAMP_HILLS.getValue());
        
        // Nether
        addBiomeWithCustomSurface(BiomeKeys.NETHER_WASTES.getValue());
        addBiomeWithCustomSurface(BiomeKeys.WARPED_FOREST.getValue());
        addBiomeWithCustomSurface(BiomeKeys.CRIMSON_FOREST.getValue());
        addBiomeWithCustomSurface(BiomeKeys.BASALT_DELTAS.getValue());
        addBiomeWithCustomSurface(BiomeKeys.SOUL_SAND_VALLEY.getValue());
    }
}
