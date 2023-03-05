package mod.bespectacled.modernbeta.settings;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomes;
import mod.bespectacled.modernbeta.world.biome.provider.climate.ClimateMapping;
import mod.bespectacled.modernbeta.world.biome.voronoi.VoronoiPointBiome;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.biome.BiomeKeys;

public class ModernBetaSettingsBiome implements ModernBetaSettings {
    public final String biomeProvider;
    public final String singleBiome;
    public final boolean useOceanBiomes;
    
    public final float climateTempNoiseScale;
    public final float climateRainNoiseScale;
    public final float climateDetailNoiseScale;
    public final Map<String, ClimateMapping> climateMappings;
    
    public final List<VoronoiPointBiome> voronoiPoints;
    
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
        return new Builder().fromCompound(compound).build();
    }
    
    public NbtCompound toCompound() {
        NbtCompound compound = new NbtCompound();
        
        compound.putString(NbtTags.BIOME_PROVIDER, this.biomeProvider);
        compound.putString(NbtTags.SINGLE_BIOME, this.singleBiome);
        compound.putBoolean(NbtTags.USE_OCEAN_BIOMES, this.useOceanBiomes);
        
        compound.putFloat(NbtTags.CLIMATE_TEMP_NOISE_SCALE, this.climateTempNoiseScale);
        compound.putFloat(NbtTags.CLIMATE_RAIN_NOISE_SCALE, this.climateRainNoiseScale);
        compound.putFloat(NbtTags.CLIMATE_DETAIL_NOISE_SCALE, this.climateDetailNoiseScale);
        
        compound.put(NbtTags.CLIMATE_MAPPINGS, ClimateMapping.mapToCompound(this.climateMappings));
        compound.put(NbtTags.VORONOI_POINTS, VoronoiPointBiome.listToNbt(this.voronoiPoints));
        
        return compound;
    }
    
    public static class Builder {
        public String biomeProvider;
        public String singleBiome;
        public boolean useOceanBiomes;
        
        public float climateTempNoiseScale;
        public float climateRainNoiseScale;
        public float climateDetailNoiseScale;
        public Map<String, ClimateMapping> climateMappings;
        
        public List<VoronoiPointBiome> voronoiPoints;
        
        public Builder() {
            this.biomeProvider = ModernBetaBuiltInTypes.Biome.BETA.id;
            this.singleBiome = ModernBetaBiomes.ALPHA.getValue().toString();
            this.useOceanBiomes = true;
            
            this.climateTempNoiseScale = 0.025f;
            this.climateRainNoiseScale = 0.05f;
            this.climateDetailNoiseScale = 0.25f;
            this.climateMappings = createClimateMapping(
                new ClimateMapping(
                    ModernBetaBiomes.BETA_DESERT.getValue().toString(),
                    ModernBetaBiomes.BETA_OCEAN.getValue().toString()
                ),
                new ClimateMapping(
                    ModernBetaBiomes.BETA_FOREST.getValue().toString(),
                    ModernBetaBiomes.BETA_OCEAN.getValue().toString()
                ),
                new ClimateMapping(
                    ModernBetaBiomes.BETA_TUNDRA.getValue().toString(),
                    ModernBetaBiomes.BETA_FROZEN_OCEAN.getValue().toString()
                ),
                new ClimateMapping(
                    ModernBetaBiomes.BETA_PLAINS.getValue().toString(),
                    ModernBetaBiomes.BETA_OCEAN.getValue().toString()
                ),
                new ClimateMapping(
                    ModernBetaBiomes.BETA_RAINFOREST.getValue().toString(),
                    ModernBetaBiomes.BETA_WARM_OCEAN.getValue().toString()
                ),
                new ClimateMapping(
                    ModernBetaBiomes.BETA_SAVANNA.getValue().toString(),
                    ModernBetaBiomes.BETA_OCEAN.getValue().toString()
                ),
                new ClimateMapping(
                    ModernBetaBiomes.BETA_SHRUBLAND.getValue().toString(),
                    ModernBetaBiomes.BETA_OCEAN.getValue().toString()
                ),
                new ClimateMapping(
                    ModernBetaBiomes.BETA_SEASONAL_FOREST.getValue().toString(),
                    ModernBetaBiomes.BETA_LUKEWARM_OCEAN.getValue().toString()
                ),
                new ClimateMapping(
                    ModernBetaBiomes.BETA_SWAMPLAND.getValue().toString(),
                    ModernBetaBiomes.BETA_COLD_OCEAN.getValue().toString()
                ),
                new ClimateMapping(
                    ModernBetaBiomes.BETA_TAIGA.getValue().toString(),
                    ModernBetaBiomes.BETA_FROZEN_OCEAN.getValue().toString()
                ),
                new ClimateMapping(
                    ModernBetaBiomes.BETA_TUNDRA.getValue().toString(),
                    ModernBetaBiomes.BETA_FROZEN_OCEAN.getValue().toString()
                )
            );
            this.voronoiPoints = List.of(
                new VoronoiPointBiome(
                    BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                    BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                    0.0, 0.0
                ),
                new VoronoiPointBiome(
                    BiomeKeys.SNOWY_TAIGA.getValue().toString(),
                    BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                    0.0, 0.5
                ),
                new VoronoiPointBiome(
                    BiomeKeys.SWAMP.getValue().toString(),
                    BiomeKeys.COLD_OCEAN.getValue().toString(),
                    0.0, 1.0
                ),
                new VoronoiPointBiome(
                    BiomeKeys.SAVANNA.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    0.5, 0.0
                ),
                new VoronoiPointBiome(
                    BiomeKeys.FOREST.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    0.5, 0.5
                ),
                new VoronoiPointBiome(
                    BiomeKeys.PLAINS.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    0.5, 1.0
                ),
                new VoronoiPointBiome(
                    BiomeKeys.DESERT.getValue().toString(),
                    BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                    1.0, 0.0
                ),
                new VoronoiPointBiome(
                    BiomeKeys.DARK_FOREST.getValue().toString(),
                    BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                    1.0, 0.5
                ),
                new VoronoiPointBiome(
                    BiomeKeys.JUNGLE.getValue().toString(),
                    BiomeKeys.WARM_OCEAN.getValue().toString(),
                    1.0, 1.0
                )
            );
        }
        
        public Builder fromCompound(NbtCompound compound) {
            this.biomeProvider = NbtUtil.readString(NbtTags.BIOME_PROVIDER, compound, this.biomeProvider);
            this.singleBiome = NbtUtil.readString(NbtTags.SINGLE_BIOME, compound, this.singleBiome);
            this.useOceanBiomes = NbtUtil.readBoolean(NbtTags.USE_OCEAN_BIOMES, compound, this.useOceanBiomes);
            
            this.climateTempNoiseScale = NbtUtil.readFloat(NbtTags.CLIMATE_TEMP_NOISE_SCALE, compound, this.climateTempNoiseScale);
            this.climateRainNoiseScale = NbtUtil.readFloat(NbtTags.CLIMATE_RAIN_NOISE_SCALE, compound, this.climateRainNoiseScale);
            this.climateDetailNoiseScale = NbtUtil.readFloat(NbtTags.CLIMATE_DETAIL_NOISE_SCALE, compound, this.climateDetailNoiseScale);
            
            this.climateMappings = ClimateMapping.mapFromCompound(compound, this.climateMappings);
            this.voronoiPoints = VoronoiPointBiome.listFromCompound(compound, this.voronoiPoints);
            
            this.loadDeprecated(compound);
            
            return this;
        }
        
        public ModernBetaSettingsBiome build() {
            return new ModernBetaSettingsBiome(this);
        }
        
        private void loadDeprecated(NbtCompound compound) {}
        
        public static Map<String, ClimateMapping> createClimateMapping(
            ClimateMapping desert,
            ClimateMapping forest,
            ClimateMapping iceDesert,
            ClimateMapping plains,
            ClimateMapping rainforest,
            ClimateMapping savanna,
            ClimateMapping shrubland,
            ClimateMapping seasonal_forest,
            ClimateMapping swampland,
            ClimateMapping taiga,
            ClimateMapping tundra
        ) {
            return Map.ofEntries(
                Map.entry("desert", desert),
                Map.entry("forest", forest),
                Map.entry("ice_desert", iceDesert),
                Map.entry("plains", plains),
                Map.entry("rainforest", rainforest),
                Map.entry("savanna", savanna),
                Map.entry("shrubland", shrubland),
                Map.entry("seasonal_forest", seasonal_forest),
                Map.entry("swampland", swampland),
                Map.entry("taiga", taiga),
                Map.entry("tundra", tundra)
            );
        }
    }
}