package com.bespectacled.modernbeta.biome.beta;

import net.minecraft.util.Identifier;

public class BetaClimateMap {
    public enum BetaBiomeType {
        LAND, OCEAN, ICE_DESERT
    }
    
    private static final Identifier LAND_BIOME_TABLE[] = new Identifier[4096];
    private static final Identifier ICE_DESERT_BIOME_TABLE[] = new Identifier[4096];
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
            case ICE_DESERT:
                biomeId= ICE_DESERT_BIOME_TABLE[i + j * 64];
                break;
            default:
                biomeId = LAND_BIOME_TABLE[i + j * 64];
        }

        return biomeId;
    }
    
    private static void generateBiomeLookup() {
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 64; j++) {
                LAND_BIOME_TABLE[i + j * 64] = getBiome((float) i / 63F, (float) j / 63F, false);
                ICE_DESERT_BIOME_TABLE[i + j * 64] = getBiome((float) i / 63F, (float) j / 63F, true);
                OCEAN_BIOME_TABLE[i + j * 64] = getOceanBiome((float) i / 63F, (float) j / 63F);
            }
        }
    }
    
    private static Identifier getBiome(float temp, float humid, boolean genIceDesert) {
        humid *= temp;

        if (temp < 0.1F) {
            if (genIceDesert)
                return BetaBiomes.ICE_DESERT_ID;
            else
                return BetaBiomes.TUNDRA_ID;
        }

        if (humid < 0.2F) {
            if (temp < 0.5F) {
                return BetaBiomes.TUNDRA_ID;
            }
            if (temp < 0.95F) {
                return BetaBiomes.SAVANNA_ID;
            } else {
                return BetaBiomes.DESERT_ID;
            }
        }

        if (humid > 0.5F && temp < 0.7F) {
            return BetaBiomes.SWAMPLAND_ID;
        }

        if (temp < 0.5F) {
            return BetaBiomes.TAIGA_ID;
        }

        if (temp < 0.97F) {
            if (humid < 0.35F) {
                return BetaBiomes.SHRUBLAND_ID;
            } else {
                return BetaBiomes.FOREST_ID;
            }
        }

        if (humid < 0.45F) {
            return BetaBiomes.PLAINS_ID;
        }

        if (humid < 0.9F) {
            return BetaBiomes.SEASONAL_FOREST_ID;
        } else {
            return BetaBiomes.RAINFOREST_ID;
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
            return BetaBiomes.FROZEN_OCEAN_ID;
        }

        if (humid < 0.2F) {
            if (temp < 0.5F) {
                return BetaBiomes.FROZEN_OCEAN_ID;
            }
            if (temp < 0.95F) {
                return BetaBiomes.OCEAN_ID;
            } else {
                return BetaBiomes.OCEAN_ID;
            }
        }

        if (humid > 0.5F && temp < 0.7F) {
            return BetaBiomes.COLD_OCEAN_ID;
        }

        if (temp < 0.5F) {
            return BetaBiomes.FROZEN_OCEAN_ID;
        }

        if (temp < 0.97F) {
            if (humid < 0.35F) {
                return BetaBiomes.OCEAN_ID;
            } else {
                return BetaBiomes.OCEAN_ID;
            }
        }

        if (humid < 0.45F) {
            return BetaBiomes.OCEAN_ID;
        }

        if (humid < 0.9F) {
            return BetaBiomes.LUKEWARM_OCEAN_ID;
        } else {
            return BetaBiomes.WARM_OCEAN_ID;
        }

    }
}
