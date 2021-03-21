package com.bespectacled.modernbeta.compat;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.BiomeKeys;

public class Compat {
    // Set for specifying which biomes should use their vanilla surface builders.
    // Done on per-biome basis for best mod compatibility.
    public static final Set<Identifier> BIOMES_WITH_CUSTOM_SURFACES = new HashSet<Identifier>();
    
    public static void setupCompat() {
        try {
            if (FabricLoader.getInstance().isModLoaded("techreborn")) CompatTechReborn.addCompat();
            
        } catch (Exception e) {
            ModernBeta.LOGGER.log(Level.ERROR, "[Modern Beta] Something went wrong when attempting to add mod compatibility!");
            e.printStackTrace();
        }
    }
    
    static {
        // Badlands
        BIOMES_WITH_CUSTOM_SURFACES.add(BiomeKeys.BADLANDS.getValue());
        BIOMES_WITH_CUSTOM_SURFACES.add(BiomeKeys.BADLANDS_PLATEAU.getValue());
        BIOMES_WITH_CUSTOM_SURFACES.add(BiomeKeys.ERODED_BADLANDS.getValue());
        BIOMES_WITH_CUSTOM_SURFACES.add(BiomeKeys.MODIFIED_BADLANDS_PLATEAU.getValue());
        BIOMES_WITH_CUSTOM_SURFACES.add(BiomeKeys.MODIFIED_WOODED_BADLANDS_PLATEAU.getValue());
        BIOMES_WITH_CUSTOM_SURFACES.add(BiomeKeys.WOODED_BADLANDS_PLATEAU.getValue());
        
        // Mountains
        BIOMES_WITH_CUSTOM_SURFACES.add(BiomeKeys.MOUNTAINS.getValue());
        BIOMES_WITH_CUSTOM_SURFACES.add(BiomeKeys.GRAVELLY_MOUNTAINS.getValue());
        BIOMES_WITH_CUSTOM_SURFACES.add(BiomeKeys.MODIFIED_GRAVELLY_MOUNTAINS.getValue());
        
        // Giant Taigas
        BIOMES_WITH_CUSTOM_SURFACES.add(BiomeKeys.GIANT_TREE_TAIGA.getValue());
        BIOMES_WITH_CUSTOM_SURFACES.add(BiomeKeys.GIANT_TREE_TAIGA_HILLS.getValue());
        BIOMES_WITH_CUSTOM_SURFACES.add(BiomeKeys.GIANT_SPRUCE_TAIGA.getValue());
        BIOMES_WITH_CUSTOM_SURFACES.add(BiomeKeys.GIANT_SPRUCE_TAIGA_HILLS.getValue());
        
        // Savanna
        BIOMES_WITH_CUSTOM_SURFACES.add(BiomeKeys.SHATTERED_SAVANNA.getValue());
        BIOMES_WITH_CUSTOM_SURFACES.add(BiomeKeys.SHATTERED_SAVANNA_PLATEAU.getValue());
        
        // Swamp
        BIOMES_WITH_CUSTOM_SURFACES.add(BiomeKeys.SWAMP.getValue());
        BIOMES_WITH_CUSTOM_SURFACES.add(BiomeKeys.SWAMP_HILLS.getValue());
        
        // Nether
        BIOMES_WITH_CUSTOM_SURFACES.add(BiomeKeys.NETHER_WASTES.getValue());
        BIOMES_WITH_CUSTOM_SURFACES.add(BiomeKeys.WARPED_FOREST.getValue());
        BIOMES_WITH_CUSTOM_SURFACES.add(BiomeKeys.CRIMSON_FOREST.getValue());
        BIOMES_WITH_CUSTOM_SURFACES.add(BiomeKeys.BASALT_DELTAS.getValue());
        BIOMES_WITH_CUSTOM_SURFACES.add(BiomeKeys.SOUL_SAND_VALLEY.getValue());
    }
}