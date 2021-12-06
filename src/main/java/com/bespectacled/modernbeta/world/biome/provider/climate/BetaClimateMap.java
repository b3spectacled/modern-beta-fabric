package com.bespectacled.modernbeta.world.biome.provider.climate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.biome.BiomeClimatePoint;
import com.bespectacled.modernbeta.api.world.biome.climate.ClimateType;
import com.bespectacled.modernbeta.util.NbtUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class BetaClimateMap {
    private final Map<String, BiomeClimatePoint> biomeMap;
    
    private final Identifier[] landBiomeTable;
    private final Identifier[] oceanBiomeTable;
    private final Identifier[] deepOceanBiomeTable;
    
    public BetaClimateMap(NbtCompound biomeProviderSettings) {
        this.biomeMap = new LinkedHashMap<>();
        
        this.landBiomeTable = new Identifier[4096];
        this.oceanBiomeTable = new Identifier[4096];
        this.deepOceanBiomeTable = new Identifier[4096];
        
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
    
    public Map<String, BiomeClimatePoint> getMap() {
        return new LinkedHashMap<>(this.biomeMap);
    }
    
    public Identifier getBiome(double temp, double rain, ClimateType type) {
        int t = (int) (temp * 63D);
        int r = (int) (rain * 63D);
        int ndx = t + r * 64;
        
        
        return switch(type) {
            case LAND -> this.landBiomeTable[ndx];
            case OCEAN -> this.oceanBiomeTable[ndx];
            case DEEP_OCEAN -> this.deepOceanBiomeTable[ndx];
        };
    }
    
    public List<Identifier> getBiomeIds() {
        List<Identifier> biomeIds = new ArrayList<>();
        biomeIds.addAll(this.biomeMap.values().stream().map(p -> new Identifier(p.landBiome())).toList());
        biomeIds.addAll(this.biomeMap.values().stream().map(p -> new Identifier(p.oceanBiome())).toList());
        biomeIds.addAll(this.biomeMap.values().stream().map(p -> new Identifier(p.deepOceanBiome())).toList());
        
        return biomeIds;
    }
    
    private void loadBiomePoint(String key, NbtCompound settings, BiomeClimatePoint alternate) {
        this.biomeMap.put(key, BiomeClimatePoint.fromCompound(NbtUtil.readCompound(key, settings, alternate.toCompound())));
    }
    
    private void generateBiomeLookup() {
        for (int t = 0; t < 64; t++) {
            for (int r = 0; r < 64; r++) {
                BiomeClimatePoint biomePoint = this.getBiome((float) t / 63F, (float) r / 63F);
                
                this.landBiomeTable[t + r * 64] = new Identifier(biomePoint.getByClimate(ClimateType.LAND));
                this.oceanBiomeTable[t + r * 64] = new Identifier(biomePoint.getByClimate(ClimateType.OCEAN));
                this.deepOceanBiomeTable[t + r * 64] = new Identifier(biomePoint.getByClimate(ClimateType.DEEP_OCEAN));
            }
        }
    }
    
    private BiomeClimatePoint getBiome(float temp, float rain) {
        rain *= temp;

        if (temp < 0.1F) {
            return this.biomeMap.get("ice_desert");
        }

        if (rain < 0.2F) {
            if (temp < 0.5F) {
                return this.biomeMap.get("tundra");
            }
            if (temp < 0.95F) {
                return this.biomeMap.get("savanna");
            } else {
                return this.biomeMap.get("desert");
            }
        }

        if (rain > 0.5F && temp < 0.7F) {
            return this.biomeMap.get("swampland");
        }

        if (temp < 0.5F) {
            return this.biomeMap.get("taiga");
        }

        if (temp < 0.97F) {
            if (rain < 0.35F) {
                return this.biomeMap.get("shrubland");
            } else {
                return this.biomeMap.get("forest");
            }
        }

        if (rain < 0.45F) {
            return this.biomeMap.get("plains");
        }

        if (rain < 0.9F) {
            return this.biomeMap.get("seasonal_forest");
        } else {
            return this.biomeMap.get("rainforest");
        }
    }
}
