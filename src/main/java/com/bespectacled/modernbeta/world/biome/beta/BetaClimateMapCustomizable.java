package com.bespectacled.modernbeta.world.biome.beta;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bespectacled.modernbeta.world.biome.beta.BetaClimateMap.BetaBiomeType;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class BetaClimateMapCustomizable {
    private final NbtCompound biomeProviderSettings;
    
    private final Identifier LAND_BIOME_TABLE[] = new Identifier[4096];
    private final Identifier OCEAN_BIOME_TABLE[] = new Identifier[4096];
    
    private final Map<String, Identifier> biomeMap;
    
    public BetaClimateMapCustomizable(NbtCompound biomeProviderSettings) {
        this.biomeProviderSettings = biomeProviderSettings;
        this.biomeMap = new LinkedHashMap<String, Identifier>();
        
        this.loadBiomeId("desert", BetaBiomes.DESERT_ID);
        this.loadBiomeId("forest", BetaBiomes.FOREST_ID);
        this.loadBiomeId("ice_desert", BetaBiomes.TUNDRA_ID);
        this.loadBiomeId("plains", BetaBiomes.PLAINS_ID);
        this.loadBiomeId("rainforest", BetaBiomes.RAINFOREST_ID);
        this.loadBiomeId("savanna", BetaBiomes.SAVANNA_ID);
        this.loadBiomeId("shrubland", BetaBiomes.SHRUBLAND_ID);
        this.loadBiomeId("seasonal_forest", BetaBiomes.SEASONAL_FOREST_ID);
        this.loadBiomeId("swampland", BetaBiomes.SWAMPLAND_ID);
        this.loadBiomeId("taiga", BetaBiomes.TAIGA_ID);
        this.loadBiomeId("tundra", BetaBiomes.TUNDRA_ID);
        
        this.loadBiomeId("ocean", BetaBiomes.OCEAN_ID);
        this.loadBiomeId("cold_ocean", BetaBiomes.COLD_OCEAN_ID);
        this.loadBiomeId("frozen_ocean", BetaBiomes.FROZEN_OCEAN_ID);
        this.loadBiomeId("lukewarm_ocean",  BetaBiomes.LUKEWARM_OCEAN_ID);
        this.loadBiomeId("warm_ocean",  BetaBiomes.WARM_OCEAN_ID);
        
        this.generateBiomeLookup();
    }
    
    public Map<String, Identifier> getMap() {
        Map<String, Identifier> newBiomeMap = new LinkedHashMap<>();
        this.biomeMap.entrySet().forEach(e -> newBiomeMap.put(e.getKey(), e.getValue()));

        return newBiomeMap;
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
        return new ArrayList<Identifier>(this.biomeMap.values());
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
            return biomeMap.get("ice_desert");
        }

        if (humid < 0.2F) {
            if (temp < 0.5F) {
                return biomeMap.get("tundra");
            }
            if (temp < 0.95F) {
                return biomeMap.get("savanna");
            } else {
                return biomeMap.get("desert");
            }
        }

        if (humid > 0.5F && temp < 0.7F) {
            return biomeMap.get("swampland");
        }

        if (temp < 0.5F) {
            return biomeMap.get("taiga");
        }

        if (temp < 0.97F) {
            if (humid < 0.35F) {
                return biomeMap.get("shrubland");
            } else {
                return biomeMap.get("forest");
            }
        }

        if (humid < 0.45F) {
            return biomeMap.get("plains");
        }

        if (humid < 0.9F) {
            return biomeMap.get("seasonal_forest");
        } else {
            return biomeMap.get("rainforest");
        }

    }

    private Identifier getOceanBiome(float temp, float humid) {
        humid *= temp;

        if (temp < 0.1F) {
            return biomeMap.get("frozen_ocean");
        }

        if (humid < 0.2F) {
            if (temp < 0.5F) {
                return biomeMap.get("frozen_ocean");
            }
            if (temp < 0.95F) {
                return biomeMap.get("ocean");
            } else {
                return biomeMap.get("ocean");
            }
        }

        if (humid > 0.5F && temp < 0.7F) {
            return biomeMap.get("cold_ocean");
        }

        if (temp < 0.5F) {
            return biomeMap.get("frozen_ocean");
        }

        if (temp < 0.97F) {
            if (humid < 0.35F) {
                return biomeMap.get("ocean");
            } else {
                return biomeMap.get("ocean");
            }
        }

        if (humid < 0.45F) {
            return biomeMap.get("ocean");
        }

        if (humid < 0.9F) {
            return biomeMap.get("lukewarm_ocean");
        } else {
            return biomeMap.get("warm_ocean");
        }

    }
    
    private Identifier loadBiomeId(String key, Identifier defaultId) {
        Identifier biomeId = (this.biomeProviderSettings.contains(key)) ? new Identifier(this.biomeProviderSettings.getString(key)) : defaultId;
        this.biomeMap.put(key, biomeId);
        
        return biomeId;
    }
}
