package com.bespectacled.modernbeta.world.biome.beta.climate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bespectacled.modernbeta.world.biome.beta.BetaBiomes;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class BetaClimateMapCustomizable {
    private final NbtCompound biomeProviderSettings;
    
    private final Identifier landBiomeTable[] = new Identifier[4096];
    private final Identifier oceanBiomeTable[] = new Identifier[4096];
    
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
    
    public Identifier getBiome(double temp, double rain, BetaClimateType type) {
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
        return new ArrayList<Identifier>(this.biomeMap.values());
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
            return biomeMap.get("ice_desert");
        }

        if (rain < 0.2F) {
            if (temp < 0.5F) {
                return biomeMap.get("tundra");
            }
            if (temp < 0.95F) {
                return biomeMap.get("savanna");
            } else {
                return biomeMap.get("desert");
            }
        }

        if (rain > 0.5F && temp < 0.7F) {
            return biomeMap.get("swampland");
        }

        if (temp < 0.5F) {
            return biomeMap.get("taiga");
        }

        if (temp < 0.97F) {
            if (rain < 0.35F) {
                return biomeMap.get("shrubland");
            } else {
                return biomeMap.get("forest");
            }
        }

        if (rain < 0.45F) {
            return biomeMap.get("plains");
        }

        if (rain < 0.9F) {
            return biomeMap.get("seasonal_forest");
        } else {
            return biomeMap.get("rainforest");
        }

    }

    private Identifier getOceanBiome(float temp, float rain) {
        rain *= temp;

        if (temp < 0.1F) {
            return biomeMap.get("frozen_ocean");
        }

        if (rain < 0.2F) {
            if (temp < 0.5F) {
                return biomeMap.get("frozen_ocean");
            }
            if (temp < 0.95F) {
                return biomeMap.get("ocean");
            } else {
                return biomeMap.get("ocean");
            }
        }

        if (rain > 0.5F && temp < 0.7F) {
            return biomeMap.get("cold_ocean");
        }

        if (temp < 0.5F) {
            return biomeMap.get("frozen_ocean");
        }

        if (temp < 0.97F) {
            if (rain < 0.35F) {
                return biomeMap.get("ocean");
            } else {
                return biomeMap.get("ocean");
            }
        }

        if (rain < 0.45F) {
            return biomeMap.get("ocean");
        }

        if (rain < 0.9F) {
            return biomeMap.get("lukewarm_ocean");
        } else {
            return biomeMap.get("warm_ocean");
        }

    }
    
    private Identifier loadBiomeId(String key, Identifier defaultId) {
        Identifier biomeId = (this.biomeProviderSettings.contains(key)) ? 
            new Identifier(this.biomeProviderSettings.getString(key)) : 
            defaultId;
        
        this.biomeMap.put(key, biomeId);
        
        return biomeId;
    }
}
