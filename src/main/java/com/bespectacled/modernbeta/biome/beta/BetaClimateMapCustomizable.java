package com.bespectacled.modernbeta.biome.beta;

import java.util.ArrayList;
import java.util.List;

import com.bespectacled.modernbeta.biome.beta.BetaClimateMap.BetaBiomeType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

public class BetaClimateMapCustomizable {
    private final CompoundTag biomeProviderSettings;
    
    private final Identifier desertId;
    private final Identifier forestId;
    private final Identifier iceDesertId;
    private final Identifier plainsId;
    private final Identifier rainforestId;
    private final Identifier savannaId;
    private final Identifier shrublandId;
    private final Identifier seasonalForestId;
    private final Identifier swamplandId;
    private final Identifier taigaId;
    private final Identifier tundraId;
    
    private final Identifier oceanId;
    private final Identifier coldOceanId;
    private final Identifier frozenOceanId;
    private final Identifier lukewarmOceanId;
    private final Identifier warmOceanId;
    
    private final Identifier LAND_BIOME_TABLE[] = new Identifier[4096];
    private final Identifier OCEAN_BIOME_TABLE[] = new Identifier[4096];
    
    private final List<Identifier> biomeIds = new ArrayList<Identifier>();
    
    public BetaClimateMapCustomizable(CompoundTag biomeProviderSettings) {
        this.biomeProviderSettings = biomeProviderSettings;
        
        this.desertId = this.loadBiomeId("desert", BetaBiomes.DESERT_ID);
        this.forestId = this.loadBiomeId("forest", BetaBiomes.FOREST_ID);
        this.iceDesertId = this.loadBiomeId("ice_desert", BetaBiomes.TUNDRA_ID);
        this.plainsId = this.loadBiomeId("plains", BetaBiomes.PLAINS_ID);
        this.rainforestId = this.loadBiomeId("rainforest", BetaBiomes.RAINFOREST_ID);
        this.savannaId = this.loadBiomeId("savanna", BetaBiomes.SAVANNA_ID);
        this.shrublandId = this.loadBiomeId("shrubland", BetaBiomes.SHRUBLAND_ID);
        this.seasonalForestId = this.loadBiomeId("seasonal_forest", BetaBiomes.SEASONAL_FOREST_ID);
        this.swamplandId = this.loadBiomeId("swampland", BetaBiomes.SWAMPLAND_ID);
        this.taigaId = this.loadBiomeId("taiga", BetaBiomes.TAIGA_ID);
        this.tundraId = this.loadBiomeId("tundra", BetaBiomes.TUNDRA_ID);
        
        this.oceanId = this.loadBiomeId("ocean", BetaBiomes.OCEAN_ID);
        this.coldOceanId = this.loadBiomeId("cold_ocean", BetaBiomes.COLD_OCEAN_ID);
        this.frozenOceanId = this.loadBiomeId("frozen_ocean", BetaBiomes.FROZEN_OCEAN_ID);
        this.lukewarmOceanId = this.loadBiomeId("lukewarm_ocean",  BetaBiomes.LUKEWARM_OCEAN_ID);
        this.warmOceanId = this.loadBiomeId("warm_ocean",  BetaBiomes.WARM_OCEAN_ID);
        
        this.generateBiomeLookup();
    }
    
    public Identifier getBiomeFromLookup(double temp, double humid, BetaBiomeType type) {
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
    
    public List<Identifier> getBiomeIds() {
        return this.biomeIds;
    }
    
    private void generateBiomeLookup() {
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 64; j++) {
                LAND_BIOME_TABLE[i + j * 64] = getBiome((float) i / 63F, (float) j / 63F);
                OCEAN_BIOME_TABLE[i + j * 64] = getOceanBiome((float) i / 63F, (float) j / 63F);
            }
        }
    }
    
    private Identifier getBiome(float temp, float humid) {
        humid *= temp;

        if (temp < 0.1F) {
            return this.iceDesertId;
        }

        if (humid < 0.2F) {
            if (temp < 0.5F) {
                return this.tundraId;
            }
            if (temp < 0.95F) {
                return this.savannaId;
            } else {
                return this.desertId;
            }
        }

        if (humid > 0.5F && temp < 0.7F) {
            return this.swamplandId;
        }

        if (temp < 0.5F) {
            return this.taigaId;
        }

        if (temp < 0.97F) {
            if (humid < 0.35F) {
                return this.shrublandId;
            } else {
                return this.forestId;
            }
        }

        if (humid < 0.45F) {
            return this.plainsId;
        }

        if (humid < 0.9F) {
            return this.seasonalForestId;
        } else {
            return this.rainforestId;
        }

    }

    private Identifier getOceanBiome(float temp, float humid) {
        humid *= temp;

        if (temp < 0.1F) {
            return this.frozenOceanId;
        }

        if (humid < 0.2F) {
            if (temp < 0.5F) {
                return this.frozenOceanId;
            }
            if (temp < 0.95F) {
                return this.oceanId;
            } else {
                return this.oceanId;
            }
        }

        if (humid > 0.5F && temp < 0.7F) {
            return this.coldOceanId;
        }

        if (temp < 0.5F) {
            return this.frozenOceanId;
        }

        if (temp < 0.97F) {
            if (humid < 0.35F) {
                return this.oceanId;
            } else {
                return this.oceanId;
            }
        }

        if (humid < 0.45F) {
            return this.oceanId;
        }

        if (humid < 0.9F) {
            return this.lukewarmOceanId;
        } else {
            return this.warmOceanId;
        }

    }
    
    private Identifier loadBiomeId(String key, Identifier defaultId) {
        Identifier biomeId = (this.biomeProviderSettings.contains(key)) ? new Identifier(this.biomeProviderSettings.getString(key)) : defaultId;
        this.biomeIds.add(biomeId);
        
        return biomeId;
    }
}
