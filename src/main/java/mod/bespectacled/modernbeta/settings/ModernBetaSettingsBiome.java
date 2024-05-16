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
import mod.bespectacled.modernbeta.world.biome.provider.fractal.ClimaticBiomeList;
import mod.bespectacled.modernbeta.world.biome.provider.fractal.FractalSettings;
import mod.bespectacled.modernbeta.world.biome.voronoi.VoronoiPointBiome;
import net.minecraft.nbt.NbtCompound;

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

    public final List<String> fractalBiomes;
    public final List<ClimaticBiomeList<String>> fractalClimaticBiomes;
    public final Map<String, String> fractalHillVariants;
    public final Map<String, String> fractalEdgeVariants;
    public final Map<String, String> fractalMutatedVariants;
    public final Map<String, String> fractalVeryRareVariants;
    public final Map<String, List<String>> fractalSubVariants;
    public final String fractalPlains;
    public final String fractalIcePlains;
    public final int fractalBiomeScale;
    public final int fractalHillScale;
    public final int fractalSubVariantScale;
    public final int fractalSubVariantSeed;
    public final int fractalBeachShrink;
    public final int fractalOceanShrink;
    public final String fractalTerrainType;
    public final boolean fractalOceans;
    public final boolean fractalAddRivers;
    public final boolean fractalAddSnow;
    public final boolean fractalAddMushroomIslands;
    public final boolean fractalAddBeaches;
    public final boolean fractalAddHills;
    public final boolean fractalAddSwampRivers;
    public final boolean fractalAddDeepOceans;
    public final boolean fractalAddMutations;
    public final boolean fractalAddClimaticOceans;
    public final boolean fractalUseClimaticBiomes;

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

        this.fractalBiomes = builder.fractalBiomes;
        this.fractalClimaticBiomes = builder.fractalClimaticBiomes;
        this.fractalHillVariants = builder.fractalHillVariants;
        this.fractalEdgeVariants = builder.fractalEdgeVariants;
        this.fractalMutatedVariants = builder.fractalMutatedVariants;
        this.fractalVeryRareVariants = builder.fractalVeryRareVariants;
        this.fractalSubVariants = builder.fractalSubVariants;
        this.fractalPlains = builder.fractalPlains;
        this.fractalIcePlains = builder.fractalIcePlains;
        this.fractalBiomeScale = builder.fractalBiomeScale;
        this.fractalHillScale = builder.fractalHillScale;
        this.fractalSubVariantScale = builder.fractalSubVariantScale;
        this.fractalSubVariantSeed = builder.fractalSubVariantSeed;
        this.fractalBeachShrink = builder.fractalBeachShrink;
        this.fractalOceanShrink = builder.fractalOceanShrink;
        this.fractalTerrainType = builder.fractalTerrainType;
        this.fractalOceans = builder.fractalOceans;
        this.fractalAddRivers = builder.fractalAddRivers;
        this.fractalAddSnow = builder.fractalAddSnow;
        this.fractalAddMushroomIslands = builder.fractalAddMushroomIslands;
        this.fractalAddBeaches = builder.fractalAddBeaches;
        this.fractalAddHills = builder.fractalAddHills;
        this.fractalAddSwampRivers = builder.fractalAddSwampRivers;
        this.fractalAddDeepOceans = builder.fractalAddDeepOceans;
        this.fractalAddMutations = builder.fractalAddMutations;
        this.fractalAddClimaticOceans = builder.fractalAddClimaticOceans;
        this.fractalUseClimaticBiomes = builder.fractalUseClimaticBiomes;
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
            .putList(NbtTags.FRACTAL_BIOMES, FractalSettings.listToNbt(this.fractalBiomes))
            .putList(NbtTags.FRACTAL_CLIMATIC_BIOMES, ClimaticBiomeList.listToNbt(this.fractalClimaticBiomes))
            .putCompound(NbtTags.FRACTAL_HILL_VARIANTS, FractalSettings.mapToNbt(this.fractalHillVariants))
            .putCompound(NbtTags.FRACTAL_EDGE_VARIANTS, FractalSettings.mapToNbt(this.fractalEdgeVariants))
            .putCompound(NbtTags.FRACTAL_VERY_RARE_VARIANTS, FractalSettings.mapToNbt(this.fractalVeryRareVariants))
            .putCompound(NbtTags.FRACTAL_SUB_VARIANTS, FractalSettings.mapOfListToNbt(this.fractalSubVariants))
            .putString(NbtTags.FRACTAL_PLAINS, this.fractalPlains)
            .putString(NbtTags.FRACTAL_ICE_PLAINS, this.fractalIcePlains)
            .putInt(NbtTags.FRACTAL_BIOME_SCALE, this.fractalBiomeScale)
            .putInt(NbtTags.FRACTAL_HILL_SCALE, this.fractalHillScale)
            .putInt(NbtTags.FRACTAL_SUB_VARIANT_SCALE, this.fractalSubVariantScale)
            .putInt(NbtTags.FRACTAL_SUB_VARIANT_SEED, this.fractalSubVariantSeed)
            .putInt(NbtTags.FRACTAL_BEACH_SHRINK, this.fractalBeachShrink)
            .putInt(NbtTags.FRACTAL_OCEAN_SHRINK, this.fractalOceanShrink)
            .putString(NbtTags.FRACTAL_TERRAIN_TYPE, this.fractalTerrainType)
            .putBoolean(NbtTags.FRACTAL_OCEANS, this.fractalOceans)
            .putBoolean(NbtTags.FRACTAL_ADD_RIVERS, this.fractalAddRivers)
            .putBoolean(NbtTags.FRACTAL_ADD_SNOW, this.fractalAddSnow)
            .putBoolean(NbtTags.FRACTAL_ADD_MUSHROOM_ISLANDS, this.fractalAddMushroomIslands)
            .putBoolean(NbtTags.FRACTAL_ADD_BEACHES, this.fractalAddBeaches)
            .putBoolean(NbtTags.FRACTAL_ADD_HILLS, this.fractalAddHills)
            .putBoolean(NbtTags.FRACTAL_ADD_SWAMP_RIVERS, this.fractalAddSwampRivers)
            .putBoolean(NbtTags.FRACTAL_ADD_DEEP_OCEANS, this.fractalAddDeepOceans)
            .putBoolean(NbtTags.FRACTAL_ADD_MUTATIONS, this.fractalAddMutations)
            .putBoolean(NbtTags.FRACTAL_ADD_CLIMATIC_OCEANS, this.fractalAddClimaticOceans)
            .putBoolean(NbtTags.FRACTAL_USE_CLIMATIC_BIOMES, this.fractalUseClimaticBiomes)
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

        public List<String> fractalBiomes;
        public List<ClimaticBiomeList<String>> fractalClimaticBiomes;
        public Map<String, String> fractalHillVariants;
        public Map<String, String> fractalEdgeVariants;
        public Map<String, String> fractalMutatedVariants;
        public Map<String, String> fractalVeryRareVariants;
        public Map<String, List<String>> fractalSubVariants;
        public String fractalPlains;
        public String fractalIcePlains;
        public int fractalBiomeScale;
        public int fractalSubVariantScale;
        public int fractalSubVariantSeed;
        public int fractalHillScale;
        public int fractalBeachShrink;
        public int fractalOceanShrink;
        public String fractalTerrainType;
        public boolean fractalOceans;
        public boolean fractalAddRivers;
        public boolean fractalAddSnow;
        public boolean fractalAddMushroomIslands;
        public boolean fractalAddBeaches;
        public boolean fractalAddHills;
        public boolean fractalAddSwampRivers;
        public boolean fractalAddDeepOceans;
        public boolean fractalAddMutations;
        public boolean fractalAddClimaticOceans;
        public boolean fractalUseClimaticBiomes;

        public Builder() {
            this.biomeProvider = ModernBetaBuiltInTypes.Biome.BETA.id;
            this.singleBiome = ModernBetaBiomes.BETA_PLAINS.getValue().toString();
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
                new VoronoiPointBiome(
                    ModernBetaBiomes.BETA_PLAINS.getValue().toString(),
                    ModernBetaBiomes.BETA_OCEAN.getValue().toString(),
                    ModernBetaBiomes.BETA_OCEAN.getValue().toString(),
                    0.5, 0.5, 0.5
                ));

            this.fractalBiomes = List.of(
                "minecraft:desert",
                "minecraft:forest",
                "modern_beta:late_beta_extreme_hills",
                "modern_beta:late_beta_swampland",
                "modern_beta:late_beta_plains",
                "modern_beta:late_beta_taiga"
            );
            this.fractalClimaticBiomes = List.of(
                new ClimaticBiomeList<>(
                    List.of(
                        "minecraft:desert",
                        "minecraft:desert",
                        "minecraft:desert",
                        "minecraft:savanna",
                        "minecraft:savanna",
                        "minecraft:plains"
                    ),
                    List.of(
                        "*minecraft:badlands",
                        "*minecraft:wooded_badlands",
                        "*minecraft:wooded_badlands"
                    )
                ),
                new ClimaticBiomeList<>(
                    List.of(
                        "minecraft:forest",
                        "minecraft:dark_forest",
                        "minecraft:windswept_hills",
                        "minecraft:plains",
                        "minecraft:birch_forest",
                        "minecraft:swamp"
                    ),
                    List.of(
                        "minecraft:jungle"
                    )
                ),
                new ClimaticBiomeList<>(
                    List.of(
                        "minecraft:forest",
                        "minecraft:windswept_hills",
                        "minecraft:taiga",
                        "minecraft:plains"
                    ),
                    List.of(
                        "minecraft:old_growth_pine_taiga"
                    )
                ),
                new ClimaticBiomeList<>(
                    List.of(
                        "minecraft:snowy_plains",
                        "minecraft:snowy_plains",
                        "minecraft:snowy_plains",
                        "minecraft:snowy_taiga"
                    ),
                    List.of()
                )
            );
            this.fractalHillVariants = Map.ofEntries(
                Map.entry("minecraft:desert", "*minecraft:desert"),
                Map.entry("modern_beta:beta_desert", "*modern_beta:beta_desert"),
                Map.entry("modern_beta:pe_desert", "*modern_beta:pe_desert"),
                Map.entry("minecraft:forest", "*minecraft:forest"),
                Map.entry("minecraft:birch_forest", "*minecraft:birch_forest"),
                Map.entry("modern_beta:beta_forest", "*modern_beta:beta_forest"),
                Map.entry("modern_beta:pe_forest", "*modern_beta:pe_forest"),
                Map.entry("modern_beta:beta_seasonal_forest", "*modern_beta:beta_seasonal_forest"),
                Map.entry("modern_beta:pe_seasonal_forest", "*modern_beta:pe_seasonal_forest"),
                Map.entry("minecraft:taiga", "*minecraft:taiga"),
                Map.entry("modern_beta:late_beta_taiga", "*modern_beta:late_beta_taiga"),
                Map.entry("minecraft:snowy_taiga", "*minecraft:snowy_taiga"),
                Map.entry("modern_beta:early_release_taiga", "*modern_beta:early_release_taiga"),
                Map.entry("modern_beta:beta_taiga", "*modern_beta:beta_taiga"),
                Map.entry("modern_beta:pe_taiga", "*modern_beta:pe_taiga"),
                Map.entry("minecraft:plains", "minecraft:forest"),
                Map.entry("modern_beta:late_beta_plains", "minecraft:forest"),
                Map.entry("modern_beta:beta_plains", "modern_beta:beta_forest"),
                Map.entry("modern_beta:pe_plains", "modern_beta:pe_forest"),
                Map.entry("modern_beta:beta_savanna", "*modern_beta:beta_savanna"),
                Map.entry("modern_beta:beta_shrubland", "*modern_beta:beta_shrubland"),
                Map.entry("modern_beta:beta_swampland", "*modern_beta:beta_swampland"),
                Map.entry("minecraft:snowy_plains", "*minecraft:snowy_plains"),
                Map.entry("modern_beta:early_release_ice_plains", "*modern_beta:early_release_ice_plains"),
                Map.entry("modern_beta:beta_tundra", "*modern_beta:beta_tundra"),
                Map.entry("modern_beta:pe_tundra", "*modern_beta:pe_tundra"),
                Map.entry("minecraft:jungle", "*minecraft:jungle"),
                Map.entry("minecraft:bamboo_jungle", "*minecraft:bamboo_jungle"),
                Map.entry("minecraft:sparse_jungle", "*minecraft:sparse_jungle"),
                Map.entry("minecraft:ocean", "*minecraft:ocean"),
                Map.entry("minecraft:windswept_hills", "minecraft:windswept_forest"),
                Map.entry("minecraft:dark_forest", "minecraft:plains"),
                Map.entry("minecraft:old_growth_pine_taiga", "*minecraft:old_growth_pine_taiga"),
                Map.entry("minecraft:old_growth_spruce_taiga", "*minecraft:old_growth_spruce_taiga"),
                Map.entry("minecraft:savanna", "*minecraft:savanna"),
                Map.entry("*minecraft:badlands", "minecraft:badlands"),
                Map.entry("*minecraft:wooded_badlands", "minecraft:badlands"),
                Map.entry("minecraft:deep_ocean", "*minecraft:deep_ocean")
            );
            this.fractalEdgeVariants = Map.ofEntries(
                Map.entry("modern_beta:early_release_extreme_hills", "*modern_beta:early_release_extreme_hills"),
                Map.entry("*minecraft:badlands", "minecraft:badlands"),
                Map.entry("*minecraft:wooded_badlands", "minecraft:badlands"),
                Map.entry("minecraft:old_growth_pine_taiga", "minecraft:taiga"),
                Map.entry("minecraft:desert", "-1*minecraft:windswept_forest"),
                Map.entry("minecraft:swamp", "-1*minecraft:sparse_jungle")
            );
            this.fractalMutatedVariants = Map.ofEntries(
                Map.entry("minecraft:plains", "minecraft:sunflower_plains"),
                Map.entry("minecraft:desert", "2*minecraft:desert"),
                Map.entry("minecraft:forest", "minecraft:flower_forest"),
                Map.entry("minecraft:taiga", "2*minecraft:taiga"),
                Map.entry("minecraft:swamp", "*minecraft:swamp"),
                Map.entry("minecraft:jungle", "2*minecraft:jungle"),
                Map.entry("minecraft:sparse_jungle", "2*minecraft:sparse_jungle"),
                Map.entry("minecraft:snowy_taiga", "2*minecraft:snowy_taiga"),
                Map.entry("minecraft:savanna", "minecraft:windswept_savanna"),
                Map.entry("*minecraft:savanna", "*minecraft:windswept_savanna"),
                Map.entry("minecraft:badlands", "*minecraft:eroded_badlands"),
                Map.entry("*minecraft:wooded_badlands", "2*minecraft:wooded_badlands"),
                Map.entry("*minecraft:badlands", "2*minecraft:badlands"),
                Map.entry("minecraft:birch_forest", "minecraft:old_growth_birch_forest"),
                Map.entry("*minecraft:birch_forest", "*minecraft:old_growth_birch_forest"),
                Map.entry("minecraft:dark_forest", "*minecraft:dark_forest"),
                Map.entry("minecraft:old_growth_pine_taiga", "minecraft:old_growth_spruce_taiga"),
                Map.entry("minecraft:windswept_hills", "minecraft:windswept_gravelly_hills"),
                Map.entry("minecraft:windswept_forest", "minecraft:windswept_gravelly_hills")
            );
            this.fractalVeryRareVariants = Map.of();
            this.fractalSubVariants = Map.of();
            this.fractalPlains = "modern_beta:late_beta_plains";
            this.fractalIcePlains = "modern_beta:early_release_ice_plains";

            this.fractalBiomeScale = 4;
            this.fractalHillScale = 2;
            this.fractalSubVariantScale = 0;
            this.fractalSubVariantSeed = 3000;
            this.fractalBeachShrink = 1;
            this.fractalOceanShrink = 0;
            this.fractalTerrainType = FractalSettings.TerrainType.BETA.id;
            this.fractalOceans = true;
            this.fractalAddRivers = true;
            this.fractalAddSnow = false;
            this.fractalAddBeaches = false;
            this.fractalAddMushroomIslands = false;
            this.fractalAddHills = false;
            this.fractalAddSwampRivers = false;
            this.fractalAddDeepOceans = false;
            this.fractalAddMutations = false;
            this.fractalAddClimaticOceans = false;
            this.fractalUseClimaticBiomes = false;
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

            this.fractalBiomes = FractalSettings.listFromReader(NbtTags.FRACTAL_BIOMES, reader, this.fractalBiomes);
            this.fractalClimaticBiomes = ClimaticBiomeList.fromReader(NbtTags.FRACTAL_CLIMATIC_BIOMES, reader, this.fractalClimaticBiomes);
            this.fractalHillVariants = FractalSettings.mapFromReader(NbtTags.FRACTAL_HILL_VARIANTS, reader, this.fractalHillVariants);
            this.fractalVeryRareVariants = FractalSettings.mapFromReader(NbtTags.FRACTAL_VERY_RARE_VARIANTS, reader, this.fractalVeryRareVariants);
            this.fractalSubVariants = FractalSettings.mapOfListFromReader(NbtTags.FRACTAL_SUB_VARIANTS, reader, this.fractalSubVariants);
            this.fractalPlains = reader.readString(NbtTags.FRACTAL_PLAINS, this.fractalPlains);
            this.fractalIcePlains = reader.readString(NbtTags.FRACTAL_ICE_PLAINS, this.fractalIcePlains);
            this.fractalBiomeScale = reader.readInt(NbtTags.FRACTAL_BIOME_SCALE, this.fractalBiomeScale);
            this.fractalHillScale = reader.readInt(NbtTags.FRACTAL_HILL_SCALE, this.fractalHillScale);
            this.fractalSubVariantScale = reader.readInt(NbtTags.FRACTAL_SUB_VARIANT_SCALE, this.fractalSubVariantScale);
            this.fractalSubVariantSeed = reader.readInt(NbtTags.FRACTAL_SUB_VARIANT_SEED, this.fractalSubVariantSeed);
            this.fractalBeachShrink = reader.readInt(NbtTags.FRACTAL_BEACH_SHRINK, this.fractalBeachShrink);
            this.fractalOceanShrink = reader.readInt(NbtTags.FRACTAL_OCEAN_SHRINK, this.fractalOceanShrink);
            this.fractalTerrainType = reader.readString(NbtTags.FRACTAL_TERRAIN_TYPE, this.fractalTerrainType);
            this.fractalOceans = reader.readBoolean(NbtTags.FRACTAL_OCEANS, this.fractalOceans);
            this.fractalAddRivers = reader.readBoolean(NbtTags.FRACTAL_ADD_RIVERS, this.fractalAddRivers);
            this.fractalAddSnow = reader.readBoolean(NbtTags.FRACTAL_ADD_SNOW, this.fractalAddSnow);
            this.fractalAddMushroomIslands = reader.readBoolean(NbtTags.FRACTAL_ADD_MUSHROOM_ISLANDS, this.fractalAddMushroomIslands);
            this.fractalAddBeaches = reader.readBoolean(NbtTags.FRACTAL_ADD_BEACHES, this.fractalAddBeaches);
            this.fractalAddHills = reader.readBoolean(NbtTags.FRACTAL_ADD_HILLS, this.fractalAddHills);
            this.fractalAddSwampRivers = reader.readBoolean(NbtTags.FRACTAL_ADD_SWAMP_RIVERS, this.fractalAddSwampRivers);
            this.fractalAddDeepOceans = reader.readBoolean(NbtTags.FRACTAL_ADD_DEEP_OCEANS, this.fractalAddDeepOceans);
            this.fractalAddMutations = reader.readBoolean(NbtTags.FRACTAL_ADD_MUTATIONS, this.fractalAddMutations);
            this.fractalAddClimaticOceans = reader.readBoolean(NbtTags.FRACTAL_ADD_CLIMATIC_OCEANS, this.fractalAddClimaticOceans);
            this.fractalUseClimaticBiomes = reader.readBoolean(NbtTags.FRACTAL_USE_CLIMATIC_BIOMES, this.fractalUseClimaticBiomes);

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