package com.bespectacled.modernbeta.world.biome.provider.climate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bespectacled.modernbeta.api.world.biome.climate.ClimateType;
import com.bespectacled.modernbeta.world.biome.beta.BetaBiomes;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class BetaClimateMap {
    @SuppressWarnings("unused")
    private static final Set<String> OLD_OCEAN_IDS = Set.of(
        "ocean", "cold_ocean", "frozen_ocean", "lukewarm_ocean", "warm_ocean"
    );
    
    private final NbtCompound biomeProviderSettings;
    
    private final Identifier landBiomeTable[] = new Identifier[4096];
    private final Identifier oceanBiomeTable[] = new Identifier[4096];
    
    private final Map<String, Identifier> landBiomeMap;
    private final Map<String, Identifier> oceanBiomeMap;
    
    public BetaClimateMap(NbtCompound biomeProviderSettings) {
        this.biomeProviderSettings = biomeProviderSettings;
        this.landBiomeMap = new LinkedHashMap<>();
        this.oceanBiomeMap = new LinkedHashMap<>();
        
        this.loadBiomeId(this.landBiomeMap, "desert", BetaBiomes.DESERT_ID);
        this.loadBiomeId(this.landBiomeMap, "forest", BetaBiomes.FOREST_ID);
        this.loadBiomeId(this.landBiomeMap, "ice_desert", BetaBiomes.TUNDRA_ID);
        this.loadBiomeId(this.landBiomeMap, "plains", BetaBiomes.PLAINS_ID);
        this.loadBiomeId(this.landBiomeMap, "rainforest", BetaBiomes.RAINFOREST_ID);
        this.loadBiomeId(this.landBiomeMap, "savanna", BetaBiomes.SAVANNA_ID);
        this.loadBiomeId(this.landBiomeMap, "shrubland", BetaBiomes.SHRUBLAND_ID);
        this.loadBiomeId(this.landBiomeMap, "seasonal_forest", BetaBiomes.SEASONAL_FOREST_ID);
        this.loadBiomeId(this.landBiomeMap, "swampland", BetaBiomes.SWAMPLAND_ID);
        this.loadBiomeId(this.landBiomeMap, "taiga", BetaBiomes.TAIGA_ID);
        this.loadBiomeId(this.landBiomeMap, "tundra", BetaBiomes.TUNDRA_ID);
        
        this.loadBiomeId(this.oceanBiomeMap, "ocean", BetaBiomes.OCEAN_ID);
        this.loadBiomeId(this.oceanBiomeMap, "cold_ocean", BetaBiomes.COLD_OCEAN_ID);
        this.loadBiomeId(this.oceanBiomeMap, "frozen_ocean", BetaBiomes.FROZEN_OCEAN_ID);
        this.loadBiomeId(this.oceanBiomeMap, "lukewarm_ocean", BetaBiomes.LUKEWARM_OCEAN_ID);
        this.loadBiomeId(this.oceanBiomeMap, "warm_ocean", BetaBiomes.WARM_OCEAN_ID);
        
        this.generateBiomeLookup();
    }
    
    public BetaClimateMap() {
        this(new NbtCompound());
    }
    
    public Map<String, Identifier> getMap(ClimateType climateType) {
        return switch(climateType) {
            case LAND -> new LinkedHashMap<>(this.landBiomeMap);
            case OCEAN -> new LinkedHashMap<>(this.oceanBiomeMap);
            default -> new LinkedHashMap<>(this.landBiomeMap);
        };
    }
    
    public Identifier getBiome(double temp, double rain, ClimateType type) {
        int t = (int) (temp * 63D);
        int r = (int) (rain * 63D);
        
        Identifier biomeId;

        switch(type) {
            case OCEAN -> biomeId = this.oceanBiomeTable[t + r * 64];
            default -> biomeId = this.landBiomeTable[t + r * 64];
        }

        return biomeId;
    }
    
    public List<Identifier> getBiomeIds() {
        List<Identifier> biomeIds = new ArrayList<>();
        biomeIds.addAll(this.landBiomeMap.values());
        biomeIds.addAll(this.oceanBiomeMap.values());
        
        return biomeIds;
    }
    
    private void loadBiomeId(Map<String, Identifier> biomeMap, String key, Identifier defaultId) {
        biomeMap.put(
            key, 
            this.biomeProviderSettings.contains(key) ? 
                new Identifier(this.biomeProviderSettings.getString(key)) : 
                defaultId
        );
    }
    
    private void generateBiomeLookup() {
        for (int t = 0; t < 64; t++) {
            for (int r = 0; r < 64; r++) {
                this.landBiomeTable[t + r * 64] = getBiome((float) t / 63F, (float) r / 63F);
                this.oceanBiomeTable[t + r * 64] = getOceanBiome((float) t / 63F, (float) r / 63F);
            }
        }
    }
    
    private Identifier getBiome(float temp, float rain) {
        rain *= temp;

        if (temp < 0.1F) {
            return landBiomeMap.get("ice_desert");
        }

        if (rain < 0.2F) {
            if (temp < 0.5F) {
                return landBiomeMap.get("tundra");
            }
            if (temp < 0.95F) {
                return landBiomeMap.get("savanna");
            } else {
                return landBiomeMap.get("desert");
            }
        }

        if (rain > 0.5F && temp < 0.7F) {
            return landBiomeMap.get("swampland");
        }

        if (temp < 0.5F) {
            return landBiomeMap.get("taiga");
        }

        if (temp < 0.97F) {
            if (rain < 0.35F) {
                return landBiomeMap.get("shrubland");
            } else {
                return landBiomeMap.get("forest");
            }
        }

        if (rain < 0.45F) {
            return landBiomeMap.get("plains");
        }

        if (rain < 0.9F) {
            return landBiomeMap.get("seasonal_forest");
        } else {
            return landBiomeMap.get("rainforest");
        }

    }

    private Identifier getOceanBiome(float temp, float rain) {
        rain *= temp;

        if (temp < 0.1F) {
            return oceanBiomeMap.get("frozen_ocean");
        }

        if (rain < 0.2F) {
            if (temp < 0.5F) {
                return oceanBiomeMap.get("frozen_ocean");
            }
            if (temp < 0.95F) {
                return oceanBiomeMap.get("ocean");
            } else {
                return oceanBiomeMap.get("ocean");
            }
        }

        if (rain > 0.5F && temp < 0.7F) {
            return oceanBiomeMap.get("cold_ocean");
        }

        if (temp < 0.5F) {
            return oceanBiomeMap.get("frozen_ocean");
        }

        if (temp < 0.97F) {
            if (rain < 0.35F) {
                return oceanBiomeMap.get("ocean");
            } else {
                return oceanBiomeMap.get("ocean");
            }
        }

        if (rain < 0.45F) {
            return oceanBiomeMap.get("ocean");
        }

        if (rain < 0.9F) {
            return oceanBiomeMap.get("lukewarm_ocean");
        } else {
            return oceanBiomeMap.get("warm_ocean");
        }

    }
}
