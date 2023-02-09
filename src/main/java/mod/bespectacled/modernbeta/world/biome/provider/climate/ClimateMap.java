package mod.bespectacled.modernbeta.world.biome.provider.climate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mod.bespectacled.modernbeta.config.ModernBetaConfigBiome.ConfigClimateMapping;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsBiome;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class ClimateMap {
    private final Map<String, ConfigClimateMapping> climateMap;
    private final ConfigClimateMapping[] climateTable;
    
    public ClimateMap(ModernBetaSettingsBiome settings) {
        this.climateMap = new LinkedHashMap<>();
        this.climateTable = new ConfigClimateMapping[4096];
        
        for (String key : settings.climateMappings.keySet()) {
            this.climateMap.put(key, settings.climateMappings.get(key));
        }
        
        this.generateBiomeLookup();
    }
    
    public Map<String, ConfigClimateMapping> getMap() {
        return new LinkedHashMap<>(this.climateMap);
    }
    
    public RegistryKey<Biome> getBiome(double temp, double rain, ClimateType type) {
        int t = (int) (temp * 63D);
        int r = (int) (rain * 63D);

        return this.climateTable[t + r * 64].biomeByClimateType(type);
    }
    
    public List<RegistryKey<Biome>> getBiomeKeys() {
        List<RegistryKey<Biome>> biomeKeys = new ArrayList<>();
        
        this.climateMap.values().forEach(mapping -> {
            biomeKeys.add(mapping.biome());
            biomeKeys.add(mapping.oceanBiome());
            biomeKeys.add(mapping.deepOceanBiome());
        });
        
        return biomeKeys;
    }
    
    private void generateBiomeLookup() {
        for (int t = 0; t < 64; t++) {
            for (int r = 0; r < 64; r++) {
                this.climateTable[t + r * 64] = this.getBiome((float) t / 63F, (float) r / 63F);
            }
        }
    }
    
    private ConfigClimateMapping getBiome(float temp, float rain) {
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
