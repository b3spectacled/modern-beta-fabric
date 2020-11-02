package com.bespectacled.modernbeta.biome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeCreator;

public class BetaBiomes {
    
    public static final Identifier FOREST_ID = new Identifier(ModernBeta.ID, "forest");
    public static final Identifier SHRUBLAND_ID = new Identifier(ModernBeta.ID, "shrubland");
    public static final Identifier DESERT_ID = new Identifier(ModernBeta.ID, "desert");
    public static final Identifier SAVANNA_ID = new Identifier(ModernBeta.ID, "savanna");
    public static final Identifier PLAINS_ID = new Identifier(ModernBeta.ID, "plains");
    public static final Identifier SEASONAL_FOREST_ID = new Identifier(ModernBeta.ID, "seasonal_forest");
    public static final Identifier RAINFOREST_ID = new Identifier(ModernBeta.ID, "rainforest");
    public static final Identifier SWAMPLAND_ID = new Identifier(ModernBeta.ID, "swampland");
    public static final Identifier TAIGA_ID = new Identifier(ModernBeta.ID, "taiga");
    public static final Identifier TUNDRA_ID = new Identifier(ModernBeta.ID, "tundra");
    public static final Identifier ICE_DESERT_ID = new Identifier(ModernBeta.ID, "ice_desert");

    public static final Identifier OCEAN_ID = new Identifier(ModernBeta.ID, "ocean");
    public static final Identifier LUKEWARM_OCEAN_ID = new Identifier(ModernBeta.ID, "lukewarm_ocean");
    public static final Identifier WARM_OCEAN_ID = new Identifier(ModernBeta.ID, "warm_ocean");
    public static final Identifier COLD_OCEAN_ID = new Identifier(ModernBeta.ID, "cold_ocean");
    public static final Identifier FROZEN_OCEAN_ID = new Identifier(ModernBeta.ID, "frozen_ocean");
    
    public static final Identifier SKY_ID = new Identifier(ModernBeta.ID, "sky");
    
    public static final ImmutableList<Identifier> BIOMES = ImmutableList.of(
        FOREST_ID,
        SHRUBLAND_ID, 
        DESERT_ID,
        SAVANNA_ID, 
        PLAINS_ID,
        SEASONAL_FOREST_ID, 
        RAINFOREST_ID,
        SWAMPLAND_ID, 
        TAIGA_ID,
        TUNDRA_ID, 
        ICE_DESERT_ID,

        OCEAN_ID, 
        LUKEWARM_OCEAN_ID,
        WARM_OCEAN_ID, 
        COLD_OCEAN_ID,
        FROZEN_OCEAN_ID,

        SKY_ID
    );

    private static Map<String, Biome> biomeMappings = new HashMap();
    
    public static void reserveBiomeIDs() {
        for (Identifier i : BIOMES) {
            Registry.register(BuiltinRegistries.BIOME, i, DefaultBiomeCreator.createNormalOcean(false));
        }

        ModernBeta.LOGGER.log(Level.INFO, "Reserved Beta biome IDs.");
    }
    
    public static List<RegistryKey<Biome>> getBiomeList() {
        ArrayList<RegistryKey<Biome>> biomeList = new ArrayList<RegistryKey<Biome>>();
        
        for (Identifier i : BIOMES) {
            biomeList.add(RegistryKey.of(Registry.BIOME_KEY, i));
        }
        
        return Collections.unmodifiableList(biomeList);
    }
    
    public static Biome getMappedBiome(String biomeType) {
        return biomeMappings.get(biomeType);
    }
}
