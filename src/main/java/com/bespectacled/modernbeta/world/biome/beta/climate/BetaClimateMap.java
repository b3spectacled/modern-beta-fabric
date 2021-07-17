package com.bespectacled.modernbeta.world.biome.beta.climate;

import com.bespectacled.modernbeta.world.biome.beta.BetaBiomes;

import net.minecraft.util.Identifier;

public class BetaClimateMap {
    private static final Identifier LAND_BIOME_TABLE[] = new Identifier[4096];
    private static final Identifier OCEAN_BIOME_TABLE[] = new Identifier[4096];
    
    public static Identifier getBiome(double temp, double humid, BetaClimateType type) {
        int t = (int) (temp * 63D);
        int r = (int) (humid * 63D);
        
        Identifier biomeId;
        
        switch(type) {
            case OCEAN -> biomeId = OCEAN_BIOME_TABLE[t + r * 64];
            default -> biomeId = LAND_BIOME_TABLE[t + r * 64];
        }

        return biomeId;
    }
    
    private static void generateBiomeLookup() {
        for (int t = 0; t < 64; t++) {
            for (int r = 0; r < 64; r++) {
                LAND_BIOME_TABLE[t + r * 64] = getBiome((float) t / 63F, (float) r / 63F);
                OCEAN_BIOME_TABLE[t + r * 64] = getOceanBiome((float) t / 63F, (float) r / 63F);
            }
        }
    }
    
    private static Identifier getBiome(float temp, float rain) {
        rain *= temp;

        if (temp < 0.1F) {
            return BetaBiomes.TUNDRA_ID;
        }

        if (rain < 0.2F) {
            if (temp < 0.5F) {
                return BetaBiomes.TUNDRA_ID;
            }
            if (temp < 0.95F) {
                return BetaBiomes.SAVANNA_ID;
            } else {
                return BetaBiomes.DESERT_ID;
            }
        }

        if (rain > 0.5F && temp < 0.7F) {
            return BetaBiomes.SWAMPLAND_ID;
        }

        if (temp < 0.5F) {
            return BetaBiomes.TAIGA_ID;
        }

        if (temp < 0.97F) {
            if (rain < 0.35F) {
                return BetaBiomes.SHRUBLAND_ID;
            } else {
                return BetaBiomes.FOREST_ID;
            }
        }

        if (rain < 0.45F) {
            return BetaBiomes.PLAINS_ID;
        }

        if (rain < 0.9F) {
            return BetaBiomes.SEASONAL_FOREST_ID;
        } else {
            return BetaBiomes.RAINFOREST_ID;
        }

    }

    private static Identifier getOceanBiome(float temp, float rain) {
        rain *= temp;

        if (temp < 0.1F) {
            return BetaBiomes.FROZEN_OCEAN_ID;
        }

        if (rain < 0.2F) {
            if (temp < 0.5F) {
                return BetaBiomes.FROZEN_OCEAN_ID;
            }
            if (temp < 0.95F) {
                return BetaBiomes.OCEAN_ID;
            } else {
                return BetaBiomes.OCEAN_ID;
            }
        }

        if (rain > 0.5F && temp < 0.7F) {
            return BetaBiomes.COLD_OCEAN_ID;
        }

        if (temp < 0.5F) {
            return BetaBiomes.FROZEN_OCEAN_ID;
        }

        if (temp < 0.97F) {
            if (rain < 0.35F) {
                return BetaBiomes.OCEAN_ID;
            } else {
                return BetaBiomes.OCEAN_ID;
            }
        }

        if (rain < 0.45F) {
            return BetaBiomes.OCEAN_ID;
        }

        if (rain < 0.9F) {
            return BetaBiomes.LUKEWARM_OCEAN_ID;
        } else {
            return BetaBiomes.WARM_OCEAN_ID;
        }
    }
    
    static {
        generateBiomeLookup();
    }
}
