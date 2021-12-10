package com.bespectacled.modernbeta.world.biome.provider.climate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.config.ModernBetaConfigBiome;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.world.biome.provider.climate.ClimateMapping.ClimateType;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class BetaClimateMap {
    private final Map<String, ClimateMapping> climateMap;
    private final ClimateMapping[] climateTable;
    
    public BetaClimateMap(NbtCompound biomeProviderSettings) {
        this.climateMap = new LinkedHashMap<>();
        this.climateTable = new ClimateMapping[4096];
        
        this.loadBiomePoint("desert", biomeProviderSettings, ModernBeta.BIOME_CONFIG.betaDesertBiome);
        this.loadBiomePoint("forest", biomeProviderSettings, ModernBeta.BIOME_CONFIG.betaForestBiome);
        this.loadBiomePoint("ice_desert", biomeProviderSettings, ModernBeta.BIOME_CONFIG.betaIceDesertBiome);
        this.loadBiomePoint("plains", biomeProviderSettings, ModernBeta.BIOME_CONFIG.betaPlainsBiome);
        this.loadBiomePoint("rainforest", biomeProviderSettings, ModernBeta.BIOME_CONFIG.betaRainforestBiome);
        this.loadBiomePoint("savanna", biomeProviderSettings, ModernBeta.BIOME_CONFIG.betaSavannaBiome);
        this.loadBiomePoint("shrubland", biomeProviderSettings, ModernBeta.BIOME_CONFIG.betaShrublandBiome);
        this.loadBiomePoint("seasonal_forest", biomeProviderSettings, ModernBeta.BIOME_CONFIG.betaSeasonalForestBiome);
        this.loadBiomePoint("swampland", biomeProviderSettings, ModernBeta.BIOME_CONFIG.betaSwamplandBiome);
        this.loadBiomePoint("taiga", biomeProviderSettings, ModernBeta.BIOME_CONFIG.betaTaigaBiome);
        this.loadBiomePoint("tundra", biomeProviderSettings, ModernBeta.BIOME_CONFIG.betaTundraBiome);
        
        this.generateBiomeLookup();
    }
    
    public Map<String, ClimateMapping> getMap() {
        return new LinkedHashMap<>(this.climateMap);
    }
    
    public Identifier getBiome(double temp, double rain, ClimateType type) {
        int t = (int) (temp * 63D);
        int r = (int) (rain * 63D);
        int ndx = t + r * 64;
        
        return this.climateTable[ndx].biomeByClimateType(type);
    }
    
    public List<Identifier> getBiomeIds() {
        List<Identifier> biomeIds = new ArrayList<>();
        biomeIds.addAll(this.climateMap.values().stream().map(p -> p.biome()).toList());
        biomeIds.addAll(this.climateMap.values().stream().map(p -> p.oceanBiome()).toList());
        biomeIds.addAll(this.climateMap.values().stream().map(p -> p.deepOceanBiome()).toList());
        
        return biomeIds;
    }
    
    private void loadBiomePoint(String key, NbtCompound settings, ModernBetaConfigBiome.ClimateMapping alternate) {
        this.climateMap.put(
            key,
            ClimateMapping.fromCompound(NbtUtil.readCompound(key, settings, alternate.toCompound()))
        );
    }
    
    private void generateBiomeLookup() {
        for (int t = 0; t < 64; t++) {
            for (int r = 0; r < 64; r++) {
                this.climateTable[t + r * 64] = this.getBiome((float) t / 63F, (float) r / 63F);
            }
        }
    }
    
    private ClimateMapping getBiome(float temp, float rain) {
        rain *= temp;

        if (temp < 0.1F) {
            return this.climateMap.get("ice_desert");
        }

        if (rain < 0.2F) {
            if (temp < 0.5F) {
                return this.climateMap.get("tundra");
            }
            if (temp < 0.95F) {
                return this.climateMap.get("savanna");
            } else {
                return this.climateMap.get("desert");
            }
        }

        if (rain > 0.5F && temp < 0.7F) {
            return this.climateMap.get("swampland");
        }

        if (temp < 0.5F) {
            return this.climateMap.get("taiga");
        }

        if (temp < 0.97F) {
            if (rain < 0.35F) {
                return this.climateMap.get("shrubland");
            } else {
                return this.climateMap.get("forest");
            }
        }

        if (rain < 0.45F) {
            return this.climateMap.get("plains");
        }

        if (rain < 0.9F) {
            return this.climateMap.get("seasonal_forest");
        } else {
            return this.climateMap.get("rainforest");
        }
    }
}
