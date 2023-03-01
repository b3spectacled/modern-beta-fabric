package mod.bespectacled.modernbeta.settings;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.config.ModernBetaConfigBiome;
import mod.bespectacled.modernbeta.config.ModernBetaConfigBiome.ConfigClimateMapping;
import mod.bespectacled.modernbeta.config.ModernBetaConfigBiome.ConfigVoronoiPoint;
import mod.bespectacled.modernbeta.util.NbtCompoundBuilder;
import mod.bespectacled.modernbeta.util.NbtListBuilder;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import net.minecraft.nbt.NbtCompound;

public class ModernBetaSettingsBiome implements ModernBetaSettings {
    private static final ModernBetaConfigBiome CONFIG = ModernBeta.BIOME_CONFIG;
    
    public final String biomeProvider;
    public final String singleBiome;
    public final boolean useOceanBiomes;
    
    public final float climateTempNoiseScale;
    public final float climateRainNoiseScale;
    public final float climateDetailNoiseScale;
    public final Map<String, ConfigClimateMapping> climateMappings;
    
    public final List<ConfigVoronoiPoint> voronoiPoints;
    
    public ModernBetaSettingsBiome() {
        this(new Builder());
    }
    
    public ModernBetaSettingsBiome(ModernBetaSettingsBiome.Builder builder) {
        this.biomeProvider = builder.biomeProvider;
        this.singleBiome = builder.singleBiome;
        this.useOceanBiomes = builder.useOceanBiomes;
        
        this.climateTempNoiseScale = builder.climateTempNoiseScale;
        this.climateRainNoiseScale = builder.climateRainNoiseScale;
        this.climateDetailNoiseScale = builder.climateDetailNoiseScale;
        
        this.climateMappings = builder.climateMappings;
        this.voronoiPoints = builder.voronoiPoints;
    }
    
    public static ModernBetaSettingsBiome fromString(String string) {
        Gson gson = new Gson();
        
        return gson.fromJson(string, ModernBetaSettingsBiome.class);
    }
    
    public static ModernBetaSettingsBiome fromCompound(NbtCompound compound) {
        return new Builder(compound).build();
    }
    
    public NbtCompound toCompound() {
        NbtCompound compound = new NbtCompound();
        
        compound.putString(NbtTags.BIOME_PROVIDER, this.biomeProvider);
        compound.putString(NbtTags.SINGLE_BIOME, this.singleBiome);
        compound.putBoolean(NbtTags.USE_OCEAN_BIOMES, this.useOceanBiomes);
        
        compound.putFloat(NbtTags.CLIMATE_TEMP_NOISE_SCALE, this.climateTempNoiseScale);
        compound.putFloat(NbtTags.CLIMATE_RAIN_NOISE_SCALE, this.climateRainNoiseScale);
        compound.putFloat(NbtTags.CLIMATE_DETAIL_NOISE_SCALE, this.climateDetailNoiseScale);
        
        NbtCompoundBuilder builder0 = new NbtCompoundBuilder();
        CONFIG.climateMappings.keySet().forEach(key -> {
            builder0.putCompound(key, this.climateMappings.get(key).toCompound());
        });
        compound.put(NbtTags.CLIMATE_MAPPINGS, builder0.build());
        
        NbtListBuilder builder1 = new NbtListBuilder();
        this.voronoiPoints.forEach(p -> builder1.add(p.toCompound()));
        compound.put(NbtTags.VORONOI_POINTS, builder1.build());
        
        return compound;
    }
    
    public static class Builder {
        public String biomeProvider;
        public String singleBiome;
        public boolean useOceanBiomes;
        
        public float climateTempNoiseScale;
        public float climateRainNoiseScale;
        public float climateDetailNoiseScale;
        public Map<String, ConfigClimateMapping> climateMappings;
        
        public List<ConfigVoronoiPoint> voronoiPoints;
        
        public Builder() {
            this(new NbtCompound());
        }
        
        public Builder(NbtCompound compound) {
            this.biomeProvider = NbtUtil.readString(NbtTags.BIOME_PROVIDER, compound, CONFIG.biomeProvider);
            this.singleBiome = NbtUtil.readString(NbtTags.SINGLE_BIOME, compound, CONFIG.singleBiome);
            this.useOceanBiomes = NbtUtil.readBoolean(NbtTags.USE_OCEAN_BIOMES, compound, CONFIG.useOceanBiomes);
            
            this.climateTempNoiseScale = NbtUtil.readFloat(NbtTags.CLIMATE_TEMP_NOISE_SCALE, compound, CONFIG.climateTempNoiseScale);
            this.climateRainNoiseScale = NbtUtil.readFloat(NbtTags.CLIMATE_RAIN_NOISE_SCALE, compound, CONFIG.climateRainNoiseScale);
            this.climateDetailNoiseScale = NbtUtil.readFloat(NbtTags.CLIMATE_DETAIL_NOISE_SCALE, compound, CONFIG.climateDetailNoiseScale);
            
            this.climateMappings = new LinkedHashMap<>();
            if (compound.contains(NbtTags.CLIMATE_MAPPINGS)) {
                NbtCompound biomes = NbtUtil.readCompoundOrThrow(NbtTags.CLIMATE_MAPPINGS, compound);
                
                biomes.getKeys().forEach(key -> {
                    this.climateMappings.put(key, ConfigClimateMapping.fromCompound(NbtUtil.readCompoundOrThrow(key, biomes)));
                });
                
            } else {
                this.climateMappings.putAll(CONFIG.climateMappings);
            }
            
            this.voronoiPoints = new ArrayList<>();
            if (compound.contains(NbtTags.VORONOI_POINTS)) {
                NbtUtil.toListOrThrow(compound.get(NbtTags.VORONOI_POINTS)).stream().forEach(e -> {
                    NbtCompound point = NbtUtil.toCompoundOrThrow(e);
                    
                    String biome = NbtUtil.readStringOrThrow(NbtTags.BIOME, point);
                    String oceanBiome = NbtUtil.readStringOrThrow(NbtTags.OCEAN_BIOME, point);
                    String deepOceanBiome = NbtUtil.readStringOrThrow(NbtTags.DEEP_OCEAN_BIOME, point);
                    double temp = NbtUtil.readDoubleOrThrow(NbtTags.TEMP, point);
                    double rain = NbtUtil.readDoubleOrThrow(NbtTags.RAIN, point);
                    
                    this.voronoiPoints.add(new ConfigVoronoiPoint(biome, oceanBiome, deepOceanBiome, temp, rain));
                });
            } else {
                this.voronoiPoints.addAll(CONFIG.voronoiPoints);
            }
            
            this.loadDeprecated(compound);
        }
        
        public ModernBetaSettingsBiome build() {
            return new ModernBetaSettingsBiome(this);
        }
        
        private void loadDeprecated(NbtCompound compound) {}
    }
}