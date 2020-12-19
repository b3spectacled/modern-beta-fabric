package com.bespectacled.modernbeta.biome.beta;

import com.bespectacled.modernbeta.ModernBeta;
import com.google.common.collect.ImmutableList;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;

public class BetaBiomes {
    public enum BetaBiomeType {
        LAND, OCEAN
    }
    
    public static final Identifier FOREST_ID = ModernBeta.createId("forest");
    public static final Identifier SHRUBLAND_ID = ModernBeta.createId("shrubland");
    public static final Identifier DESERT_ID = ModernBeta.createId("desert");
    public static final Identifier SAVANNA_ID = ModernBeta.createId("savanna");
    public static final Identifier PLAINS_ID = ModernBeta.createId("plains");
    public static final Identifier SEASONAL_FOREST_ID = ModernBeta.createId("seasonal_forest");
    public static final Identifier RAINFOREST_ID = ModernBeta.createId("rainforest");
    public static final Identifier SWAMPLAND_ID = ModernBeta.createId("swampland");
    public static final Identifier TAIGA_ID = ModernBeta.createId("taiga");
    public static final Identifier TUNDRA_ID = ModernBeta.createId("tundra");
    public static final Identifier ICE_DESERT_ID = ModernBeta.createId("ice_desert");

    public static final Identifier OCEAN_ID = ModernBeta.createId("ocean");
    public static final Identifier LUKEWARM_OCEAN_ID = ModernBeta.createId("lukewarm_ocean");
    public static final Identifier WARM_OCEAN_ID = ModernBeta.createId("warm_ocean");
    public static final Identifier COLD_OCEAN_ID = ModernBeta.createId("cold_ocean");
    public static final Identifier FROZEN_OCEAN_ID = ModernBeta.createId("frozen_ocean");
    
    public static final Identifier SKY_ID = ModernBeta.createId("sky");
    
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
    
    public static void registerBiomes() {
        Registry.register(BuiltinRegistries.BIOME, FOREST_ID, Forest.BIOME);
        Registry.register(BuiltinRegistries.BIOME, SHRUBLAND_ID, Shrubland.BIOME);
        Registry.register(BuiltinRegistries.BIOME, DESERT_ID, Desert.BIOME);
        Registry.register(BuiltinRegistries.BIOME, SAVANNA_ID, Savanna.BIOME);
        Registry.register(BuiltinRegistries.BIOME, PLAINS_ID, Plains.BIOME);
        Registry.register(BuiltinRegistries.BIOME, SEASONAL_FOREST_ID, SeasonalForest.BIOME);
        Registry.register(BuiltinRegistries.BIOME, RAINFOREST_ID, Rainforest.BIOME);
        Registry.register(BuiltinRegistries.BIOME, SWAMPLAND_ID, Swampland.BIOME);
        Registry.register(BuiltinRegistries.BIOME, TAIGA_ID, Taiga.BIOME);
        Registry.register(BuiltinRegistries.BIOME, TUNDRA_ID, Tundra.BIOME);
        Registry.register(BuiltinRegistries.BIOME, ICE_DESERT_ID, IceDesert.BIOME);
        
        Registry.register(BuiltinRegistries.BIOME, OCEAN_ID, Ocean.BIOME);
        Registry.register(BuiltinRegistries.BIOME, LUKEWARM_OCEAN_ID, LukewarmOcean.BIOME);
        Registry.register(BuiltinRegistries.BIOME, WARM_OCEAN_ID, WarmOcean.BIOME);
        Registry.register(BuiltinRegistries.BIOME, COLD_OCEAN_ID, ColdOcean.BIOME);
        Registry.register(BuiltinRegistries.BIOME, FROZEN_OCEAN_ID, FrozenOcean.BIOME);
        
        Registry.register(BuiltinRegistries.BIOME, SKY_ID, Sky.BIOME);
    }
    
    /*
     * Beta Biome Cache 
     */
    
    private static final Identifier LAND_BIOME_TABLE[] = new Identifier[4096];
    private static final Identifier OCEAN_BIOME_TABLE[] = new Identifier[4096];
    
    static {
        generateBiomeLookup();
    }
    
    public static Identifier getBiomeFromLookup(double temp, double humid, BetaBiomeType type) {
        int i = (int) (temp * 63D);
        int j = (int) (humid * 63D);
        
        Identifier biomeId;
        
        switch(type) {
            case OCEAN:
                biomeId = OCEAN_BIOME_TABLE[i + j * 64];
                break;
            default:
                biomeId = LAND_BIOME_TABLE[i + j * 64];
        }

        return biomeId;
    }
    
    public static void getBiomesFromLookup(double[] temps, double[] humids, Identifier[] landBiomes, Identifier[] oceanBiomes) {
        int sizeX = 16;
        int sizeZ = 16;
        
        for (int i = 0; i < sizeX * sizeZ; ++i) {
            if (landBiomes != null) landBiomes[i] = getBiomeFromLookup(temps[i], humids[i], BetaBiomeType.LAND);
            if (oceanBiomes != null) oceanBiomes[i] = getBiomeFromLookup(temps[i], humids[i], BetaBiomeType.OCEAN);
        }
    }
    
    private static void generateBiomeLookup() {
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 64; j++) {
                LAND_BIOME_TABLE[i + j * 64] = getBiome((float) i / 63F, (float) j / 63F);
                OCEAN_BIOME_TABLE[i + j * 64] = getOceanBiome((float) i / 63F, (float) j / 63F);
            }
        }
    }
    
    private static Identifier getBiome(float temp, float humid) {
        humid *= temp;

        if (temp < 0.1F) {
            //if (this.biomeType == BetaBiomeType.ICE_DESERT)
            //    return registry.get(mappings.get("ice_desert"));
            //else
            return TUNDRA_ID;
        }

        if (humid < 0.2F) {
            if (temp < 0.5F) {
                return TUNDRA_ID;
            }
            if (temp < 0.95F) {
                return SAVANNA_ID;
            } else {
                return DESERT_ID;
            }
        }

        if (humid > 0.5F && temp < 0.7F) {
            return SWAMPLAND_ID;
        }

        if (temp < 0.5F) {
            return TAIGA_ID;
        }

        if (temp < 0.97F) {
            if (humid < 0.35F) {
                return SHRUBLAND_ID;
            } else {
                return FOREST_ID;
            }
        }

        if (humid < 0.45F) {
            return PLAINS_ID;
        }

        if (humid < 0.9F) {
            return SEASONAL_FOREST_ID;
        } else {
            return RAINFOREST_ID;
        }

    }

    private static Identifier getOceanBiome(float temp, float humid) {
        humid *= temp;

        // == Vanilla Biome IDs ==
        // 0 = Ocean
        // 44 = Warm Ocean
        // 45 = Lukewarm Ocean
        // 46 = Cold Ocean
        // 10 = Frozen Ocean

        if (temp < 0.1F) {
            return FROZEN_OCEAN_ID;
        }

        if (humid < 0.2F) {
            if (temp < 0.5F) {
                return FROZEN_OCEAN_ID;
            }
            if (temp < 0.95F) {
                return OCEAN_ID;
            } else {
                return OCEAN_ID;
            }
        }

        if (humid > 0.5F && temp < 0.7F) {
            return COLD_OCEAN_ID;
        }

        if (temp < 0.5F) {
            return FROZEN_OCEAN_ID;
        }

        if (temp < 0.97F) {
            if (humid < 0.35F) {
                return OCEAN_ID;
            } else {
                return OCEAN_ID;
            }
        }

        if (humid < 0.45F) {
            return OCEAN_ID;
        }

        if (humid < 0.9F) {
            return LUKEWARM_OCEAN_ID;
        } else {
            return WARM_OCEAN_ID;
        }

    }
}
