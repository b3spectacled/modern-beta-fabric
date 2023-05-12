package mod.bespectacled.modernbeta.settings;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import mod.bespectacled.modernbeta.util.NbtCompoundBuilder;
import mod.bespectacled.modernbeta.util.NbtReader;
import mod.bespectacled.modernbeta.util.NbtTags;
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
    public final float climateWeirdNoiseScale;
    
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
        this.climateWeirdNoiseScale = builder.climateWeirdNoiseScale;
        
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
        return new NbtCompoundBuilder()
            .putString(NbtTags.BIOME_PROVIDER, this.biomeProvider)
            .putString(NbtTags.SINGLE_BIOME, this.singleBiome)
            .putBoolean(NbtTags.USE_OCEAN_BIOMES, this.useOceanBiomes)
            .putFloat(NbtTags.CLIMATE_TEMP_NOISE_SCALE, this.climateTempNoiseScale)
            .putFloat(NbtTags.CLIMATE_RAIN_NOISE_SCALE, this.climateRainNoiseScale)
            .putFloat(NbtTags.CLIMATE_DETAIL_NOISE_SCALE, this.climateDetailNoiseScale)
            .putFloat(NbtTags.CLIMATE_WEIRD_NOISE_SCALE, this.climateWeirdNoiseScale)
            .putCompound(NbtTags.CLIMATE_MAPPINGS, ClimateMapping.mapToNbt(this.climateMappings))
            .putList(NbtTags.VORONOI_POINTS, VoronoiPointBiome.listToNbt(this.voronoiPoints))
            .build();
    }
    
    public static class Builder {
        public String biomeProvider;
        public String singleBiome;
        public boolean useOceanBiomes;
        
        public float climateTempNoiseScale;
        public float climateRainNoiseScale;
        public float climateDetailNoiseScale;
        public float climateWeirdNoiseScale;
        public Map<String, ClimateMapping> climateMappings;
        
        public List<VoronoiPointBiome> voronoiPoints;
        
        public Builder() {
            this.biomeProvider = ModernBetaBuiltInTypes.Biome.BETA.id;
            this.singleBiome = ModernBetaBiomes.ALPHA.getValue().toString();
            this.useOceanBiomes = true;
            
            this.climateTempNoiseScale = 0.025f;
            this.climateRainNoiseScale = 0.05f;
            this.climateDetailNoiseScale = 0.25f;
            this.climateWeirdNoiseScale = 0.003125f;
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
                 // Standard Biomes

                new VoronoiPointBiome(
                    BiomeKeys.DESERT.getValue().toString(),
                    BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue().toString(),
                    0.9, 0.1, 0.5
                ),
                new VoronoiPointBiome(
                    BiomeKeys.PLAINS.getValue().toString(),
                    BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue().toString(),
                    0.9, 0.3, 0.5
                ),
                new VoronoiPointBiome(
                    BiomeKeys.PLAINS.getValue().toString(),
                    BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue().toString(),
                    0.9, 0.5, 0.5
                ),
                new VoronoiPointBiome(
                    BiomeKeys.FOREST.getValue().toString(),
                    BiomeKeys.WARM_OCEAN.getValue().toString(),
                    BiomeKeys.WARM_OCEAN.getValue().toString(),
                    0.9, 0.7, 0.5
                ),
                new VoronoiPointBiome(
                    BiomeKeys.JUNGLE.getValue().toString(),
                    BiomeKeys.WARM_OCEAN.getValue().toString(),
                    BiomeKeys.WARM_OCEAN.getValue().toString(),
                    0.9, 0.9, 0.5
                ),

                new VoronoiPointBiome(
                    BiomeKeys.SAVANNA.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.7, 0.1, 0.5
                ),
                new VoronoiPointBiome(
                    BiomeKeys.PLAINS.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.7, 0.3, 0.5
                ),
                new VoronoiPointBiome(
                    BiomeKeys.FOREST.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.7, 0.5, 0.5
                ),
                new VoronoiPointBiome(
                    BiomeKeys.FOREST.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.7, 0.7, 0.5
                ),
                new VoronoiPointBiome(
                    BiomeKeys.FOREST.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.7, 0.9, 0.5
                ),

                new VoronoiPointBiome(
                    BiomeKeys.PLAINS.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.5, 0.1, 0.5
                ),
                new VoronoiPointBiome(
                    BiomeKeys.PLAINS.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.5, 0.3, 0.5
                ),
                new VoronoiPointBiome(
                    BiomeKeys.BIRCH_FOREST.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.5, 0.5, 0.5
                ),
                new VoronoiPointBiome(
                    BiomeKeys.BIRCH_FOREST.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.5, 0.7, 0.5
                ),
                new VoronoiPointBiome(
                    BiomeKeys.SWAMP.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.5, 0.9, 0.5
                ),

                new VoronoiPointBiome(
                    BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                    BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                    0.3, 0.1, 0.5
                ),
                new VoronoiPointBiome(
                    BiomeKeys.TAIGA.getValue().toString(),
                    BiomeKeys.COLD_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_COLD_OCEAN.getValue().toString(),
                    0.3, 0.3, 0.5
                ),
                new VoronoiPointBiome(
                    BiomeKeys.TAIGA.getValue().toString(),
                    BiomeKeys.COLD_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_COLD_OCEAN.getValue().toString(),
                    0.3, 0.5, 0.5
                ),
                new VoronoiPointBiome(
                    BiomeKeys.SNOWY_TAIGA.getValue().toString(),
                    BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                    0.3, 0.7, 0.5
                ),
                new VoronoiPointBiome(
                    BiomeKeys.SNOWY_TAIGA.getValue().toString(),
                    BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                    0.3, 0.9, 0.5
                ),

                new VoronoiPointBiome(
                    BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                    BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                    0.1, 0.1, 0.5
                ),
                new VoronoiPointBiome(
                    BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                    BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                    0.1, 0.3, 0.5
                ),
                new VoronoiPointBiome(
                    BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                    BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                    0.1, 0.5, 0.5
                ),
                new VoronoiPointBiome(
                    BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                    BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                    0.1, 0.7, 0.5
                ),
                new VoronoiPointBiome(
                    BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                    BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                    0.1, 0.9, 0.5
                ),
                
                // Mutated Biomes

                new VoronoiPointBiome(
                    BiomeKeys.DESERT.getValue().toString(),
                    BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue().toString(),
                    0.9, 0.1, 0.2
                ),
                new VoronoiPointBiome(
                    BiomeKeys.SUNFLOWER_PLAINS.getValue().toString(),
                    BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue().toString(),
                    0.9, 0.3, 0.2
                ),
                new VoronoiPointBiome(
                    BiomeKeys.SUNFLOWER_PLAINS.getValue().toString(),
                    BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue().toString(),
                    0.9, 0.5, 0.2
                ),
                new VoronoiPointBiome(
                    BiomeKeys.DARK_FOREST.getValue().toString(),
                    BiomeKeys.WARM_OCEAN.getValue().toString(),
                    BiomeKeys.WARM_OCEAN.getValue().toString(),
                    0.9, 0.7, 0.2
                ),
                new VoronoiPointBiome(
                    BiomeKeys.BAMBOO_JUNGLE.getValue().toString(),
                    BiomeKeys.WARM_OCEAN.getValue().toString(),
                    BiomeKeys.WARM_OCEAN.getValue().toString(),
                    0.9, 0.9, 0.2
                ),

                new VoronoiPointBiome(
                    BiomeKeys.SAVANNA.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.7, 0.1, 0.2
                ),
                new VoronoiPointBiome(
                    BiomeKeys.MEADOW.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.7, 0.3, 0.2
                ),
                new VoronoiPointBiome(
                    BiomeKeys.FLOWER_FOREST.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.7, 0.5, 0.2
                ),
                new VoronoiPointBiome(
                    BiomeKeys.FLOWER_FOREST.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.7, 0.7, 0.2
                ),
                new VoronoiPointBiome(
                    BiomeKeys.FLOWER_FOREST.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.7, 0.9, 0.2
                ),

                new VoronoiPointBiome(
                    BiomeKeys.MEADOW.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.5, 0.1, 0.2
                ),
                new VoronoiPointBiome(
                    BiomeKeys.MEADOW.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.5, 0.3, 0.2
                ),
                new VoronoiPointBiome(
                    BiomeKeys.CHERRY_GROVE.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.5, 0.5, 0.2
                ),
                new VoronoiPointBiome(
                    BiomeKeys.CHERRY_GROVE.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.5, 0.7, 0.2
                ),
                new VoronoiPointBiome(
                    BiomeKeys.MANGROVE_SWAMP.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.5, 0.9, 0.2
                ),

                new VoronoiPointBiome(
                    BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                    BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                    0.3, 0.1, 0.2
                ),
                new VoronoiPointBiome(
                    BiomeKeys.OLD_GROWTH_PINE_TAIGA.getValue().toString(),
                    BiomeKeys.COLD_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_COLD_OCEAN.getValue().toString(),
                    0.3, 0.3, 0.2
                ),
                new VoronoiPointBiome(
                    BiomeKeys.OLD_GROWTH_PINE_TAIGA.getValue().toString(),
                    BiomeKeys.COLD_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_COLD_OCEAN.getValue().toString(),
                    0.3, 0.5, 0.2
                ),
                new VoronoiPointBiome(
                    BiomeKeys.GROVE.getValue().toString(),
                    BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                    0.3, 0.7, 0.2
                ),
                new VoronoiPointBiome(
                    BiomeKeys.GROVE.getValue().toString(),
                    BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                    0.3, 0.9, 0.2
                ),

                new VoronoiPointBiome(
                    BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                    BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                    0.1, 0.1, 0.2
                ),
                new VoronoiPointBiome(
                    BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                    BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                    0.1, 0.3, 0.2
                ),
                new VoronoiPointBiome(
                    BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                    BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                    0.1, 0.5, 0.2
                ),
                new VoronoiPointBiome(
                    BiomeKeys.SNOWY_SLOPES.getValue().toString(),
                    BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                    0.1, 0.7, 0.2
                ),
                new VoronoiPointBiome(
                    BiomeKeys.SNOWY_SLOPES.getValue().toString(),
                    BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                    0.1, 0.9, 0.2
                ),
                
                // Mutated Biomes 2

                new VoronoiPointBiome(
                    BiomeKeys.DESERT.getValue().toString(),
                    BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue().toString(),
                    0.9, 0.1, 0.8
                ),
                new VoronoiPointBiome(
                    BiomeKeys.PLAINS.getValue().toString(),
                    BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue().toString(),
                    0.9, 0.3, 0.8
                ),
                new VoronoiPointBiome(
                    BiomeKeys.PLAINS.getValue().toString(),
                    BiomeKeys.LUKEWARM_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue().toString(),
                    0.9, 0.5, 0.8
                ),
                new VoronoiPointBiome(
                    BiomeKeys.SPARSE_JUNGLE.getValue().toString(),
                    BiomeKeys.WARM_OCEAN.getValue().toString(),
                    BiomeKeys.WARM_OCEAN.getValue().toString(),
                    0.9, 0.7, 0.8
                ),
                new VoronoiPointBiome(
                    BiomeKeys.MUSHROOM_FIELDS.getValue().toString(),
                    BiomeKeys.WARM_OCEAN.getValue().toString(),
                    BiomeKeys.WARM_OCEAN.getValue().toString(),
                    0.9, 0.9, 0.8
                ),

                new VoronoiPointBiome(
                    BiomeKeys.SAVANNA.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.7, 0.1, 0.8
                ),
                new VoronoiPointBiome(
                    BiomeKeys.PLAINS.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.7, 0.3, 0.8
                ),
                new VoronoiPointBiome(
                    BiomeKeys.DARK_FOREST.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.7, 0.5, 0.8
                ),
                new VoronoiPointBiome(
                    BiomeKeys.DARK_FOREST.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.7, 0.7, 0.8
                ),
                new VoronoiPointBiome(
                    BiomeKeys.DARK_FOREST.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.7, 0.9, 0.8
                ),

                new VoronoiPointBiome(
                    BiomeKeys.PLAINS.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.5, 0.1, 0.8
                ),
                new VoronoiPointBiome(
                    BiomeKeys.PLAINS.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.5, 0.3, 0.8
                ),
                new VoronoiPointBiome(
                    BiomeKeys.OLD_GROWTH_BIRCH_FOREST.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.5, 0.5, 0.8
                ),
                new VoronoiPointBiome(
                    BiomeKeys.OLD_GROWTH_BIRCH_FOREST.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.5, 0.7, 0.8
                ),
                new VoronoiPointBiome(
                    BiomeKeys.MANGROVE_SWAMP.getValue().toString(),
                    BiomeKeys.OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_OCEAN.getValue().toString(),
                    0.5, 0.9, 0.8
                ),

                new VoronoiPointBiome(
                    BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                    BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                    0.3, 0.1, 0.8
                ),
                new VoronoiPointBiome(
                    BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA.getValue().toString(),
                    BiomeKeys.COLD_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_COLD_OCEAN.getValue().toString(),
                    0.3, 0.3, 0.8
                ),
                new VoronoiPointBiome(
                    BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA.getValue().toString(),
                    BiomeKeys.COLD_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_COLD_OCEAN.getValue().toString(),
                    0.3, 0.5, 0.8
                ),
                new VoronoiPointBiome(
                    BiomeKeys.GROVE.getValue().toString(),
                    BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                    0.3, 0.7, 0.8
                ),
                new VoronoiPointBiome(
                    BiomeKeys.GROVE.getValue().toString(),
                    BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                    0.3, 0.9, 0.8
                ),

                new VoronoiPointBiome(
                    BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                    BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                    0.1, 0.1, 0.8
                ),
                new VoronoiPointBiome(
                    BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                    BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                    0.1, 0.3, 0.8
                ),
                new VoronoiPointBiome(
                    BiomeKeys.SNOWY_PLAINS.getValue().toString(),
                    BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                    0.1, 0.5, 0.8
                ),
                new VoronoiPointBiome(
                    BiomeKeys.ICE_SPIKES.getValue().toString(),
                    BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                    0.1, 0.7, 0.8
                ),
                new VoronoiPointBiome(
                    BiomeKeys.ICE_SPIKES.getValue().toString(),
                    BiomeKeys.FROZEN_OCEAN.getValue().toString(),
                    BiomeKeys.DEEP_FROZEN_OCEAN.getValue().toString(),
                    0.1, 0.9, 0.8
                )
            );
        }
        
        public Builder fromCompound(NbtCompound compound) {
            NbtReader reader = new NbtReader(compound);
            
            this.biomeProvider = reader.readString(NbtTags.BIOME_PROVIDER, this.biomeProvider);
            this.singleBiome = reader.readString(NbtTags.SINGLE_BIOME, this.singleBiome);
            this.useOceanBiomes = reader.readBoolean(NbtTags.USE_OCEAN_BIOMES, this.useOceanBiomes);
            
            this.climateTempNoiseScale = reader.readFloat(NbtTags.CLIMATE_TEMP_NOISE_SCALE, this.climateTempNoiseScale);
            this.climateRainNoiseScale = reader.readFloat(NbtTags.CLIMATE_RAIN_NOISE_SCALE, this.climateRainNoiseScale);
            this.climateDetailNoiseScale = reader.readFloat(NbtTags.CLIMATE_DETAIL_NOISE_SCALE, this.climateDetailNoiseScale);
            this.climateWeirdNoiseScale = reader.readFloat(NbtTags.CLIMATE_WEIRD_NOISE_SCALE, this.climateWeirdNoiseScale);
            this.climateMappings = ClimateMapping.mapFromReader(reader, this.climateMappings);
            this.voronoiPoints = VoronoiPointBiome.listFromReader(reader, this.voronoiPoints);
            
            this.loadDatafix(reader);
            
            return this;
        }
        
        public ModernBetaSettingsBiome build() {
            return new ModernBetaSettingsBiome(this);
        }
        
        private void loadDatafix(NbtReader reader) {}
        
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