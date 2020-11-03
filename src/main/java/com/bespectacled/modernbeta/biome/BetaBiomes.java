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
import net.minecraft.world.biome.BiomeKeys;
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

    public static final Map<String, Identifier> BETA_MAPPINGS = new HashMap<String, Identifier>();
    public static final Map<String, Identifier> VANILLA_MAPPINGS = new HashMap<String, Identifier>();
    
    public static void reserveBiomeIDs() {
        for (Identifier i : BIOMES) {
            Registry.register(BuiltinRegistries.BIOME, i, DefaultBiomeCreator.createNormalOcean(false));
        }

        ModernBeta.LOGGER.log(Level.INFO, "Reserved Beta biome IDs.");
    }
    
    public static List<RegistryKey<Biome>> getBiomeList(boolean useVanillaBiomes) {
        ArrayList<RegistryKey<Biome>> biomeList = new ArrayList<RegistryKey<Biome>>();
        
        for (Identifier i : useVanillaBiomes ? VANILLA_MAPPINGS.values() : BETA_MAPPINGS.values()) {
            biomeList.add(RegistryKey.of(Registry.BIOME_KEY, i));
        }
        
        return Collections.unmodifiableList(biomeList);
    }
    
    public static Identifier getMappedBiome(String biomeType) {
        return BETA_MAPPINGS.get(biomeType);
    }
    
    static {
        BETA_MAPPINGS.put("ice_desert", ICE_DESERT_ID);
        BETA_MAPPINGS.put("tundra", TUNDRA_ID);
        BETA_MAPPINGS.put("savanna", SAVANNA_ID);
        BETA_MAPPINGS.put("desert", DESERT_ID);
        BETA_MAPPINGS.put("swampland", SWAMPLAND_ID);
        BETA_MAPPINGS.put("taiga", TAIGA_ID);
        BETA_MAPPINGS.put("shrubland", SHRUBLAND_ID);
        BETA_MAPPINGS.put("forest", FOREST_ID);
        BETA_MAPPINGS.put("plains", PLAINS_ID);
        BETA_MAPPINGS.put("seasonal_forest", SEASONAL_FOREST_ID);
        BETA_MAPPINGS.put("rainforest", RAINFOREST_ID);
        
        BETA_MAPPINGS.put("ocean", OCEAN_ID);
        BETA_MAPPINGS.put("cold_ocean", COLD_OCEAN_ID);
        BETA_MAPPINGS.put("frozen_ocean", FROZEN_OCEAN_ID);
        BETA_MAPPINGS.put("lukewarm_ocean", LUKEWARM_OCEAN_ID);
        BETA_MAPPINGS.put("warm_ocean", WARM_OCEAN_ID);
        
        BETA_MAPPINGS.put("sky", SKY_ID);
        
        VANILLA_MAPPINGS.put("ice_desert", BiomeKeys.ICE_SPIKES.getValue());
        VANILLA_MAPPINGS.put("tundra", BiomeKeys.SNOWY_TUNDRA.getValue());
        VANILLA_MAPPINGS.put("savanna", BiomeKeys.SAVANNA.getValue());
        VANILLA_MAPPINGS.put("desert", BiomeKeys.DESERT.getValue());
        VANILLA_MAPPINGS.put("swampland", BiomeKeys.SWAMP.getValue());
        VANILLA_MAPPINGS.put("taiga", BiomeKeys.SNOWY_TAIGA.getValue());
        VANILLA_MAPPINGS.put("shrubland", BiomeKeys.PLAINS.getValue());
        VANILLA_MAPPINGS.put("forest", BiomeKeys.FOREST.getValue());
        VANILLA_MAPPINGS.put("plains", BiomeKeys.SUNFLOWER_PLAINS.getValue());
        VANILLA_MAPPINGS.put("seasonal_forest", BiomeKeys.DARK_FOREST.getValue());
        VANILLA_MAPPINGS.put("rainforest", BiomeKeys.JUNGLE.getValue());
        
        VANILLA_MAPPINGS.put("ocean", BiomeKeys.OCEAN.getValue());
        VANILLA_MAPPINGS.put("cold_ocean", BiomeKeys.COLD_OCEAN.getValue());
        VANILLA_MAPPINGS.put("frozen_ocean", BiomeKeys.FROZEN_OCEAN.getValue());
        VANILLA_MAPPINGS.put("lukewarm_ocean", BiomeKeys.LUKEWARM_OCEAN.getValue());
        VANILLA_MAPPINGS.put("warm_ocean", BiomeKeys.WARM_OCEAN.getValue());
        
        VANILLA_MAPPINGS.put("beach", BiomeKeys.BEACH.getValue());
        VANILLA_MAPPINGS.put("snowy_beach", BiomeKeys.SNOWY_BEACH.getValue());
    }
}
