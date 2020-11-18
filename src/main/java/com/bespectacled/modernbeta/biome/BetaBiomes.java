package com.bespectacled.modernbeta.biome;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.util.WorldEnum.BetaBiomeType;
import com.bespectacled.modernbeta.util.WorldEnum.PreBetaBiomeType;
import com.bespectacled.modernbeta.util.WorldEnum.WorldType;
import com.google.common.collect.ImmutableList;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.DefaultBiomeCreator;

public class BetaBiomes {
    public enum BiomeType {
        LAND, OCEAN
    }
    
    public static final Map<String, Identifier> BETA_MAPPINGS = new HashMap<String, Identifier>();
    
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
    
    public static void reserveBiomeIDs() {
        for (Identifier i : BIOMES) {
            Registry.register(BuiltinRegistries.BIOME, i, DefaultBiomeCreator.createNormalOcean(false));
        }

        //ModernBeta.LOGGER.log(Level.INFO, "Reserved Beta biome IDs.");
    }
    
    public static BetaBiomeType getBiomeType(CompoundTag settings) {
        int biomeTypeNdx = BetaBiomeType.CLASSIC.ordinal();
        
        if (settings.contains("betaBiomeType")) 
            biomeTypeNdx = settings.getInt("betaBiomeType");
        
        return BetaBiomeType.values()[biomeTypeNdx];
    }
    
    public static List<RegistryKey<Biome>> getBiomeRegistryList(CompoundTag settings) {
        boolean useVanillaBiomes = false;
        
        if (settings.contains("betaBiomeType")) 
            useVanillaBiomes = PreBetaBiomeType.values()[settings.getInt("betaBiomeType")] == PreBetaBiomeType.VANILLA;
        
        return getBiomeRegistryList(useVanillaBiomes);
    }

    private static List<RegistryKey<Biome>> getBiomeRegistryList(boolean useVanillaBiomes) {
        ArrayList<RegistryKey<Biome>> biomeList = new ArrayList<RegistryKey<Biome>>();

        for (Identifier i : BETA_MAPPINGS.values()) {
            biomeList.add(RegistryKey.of(Registry.BIOME_KEY, i));
        }
        
        return Collections.unmodifiableList(biomeList);
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
    }
}
