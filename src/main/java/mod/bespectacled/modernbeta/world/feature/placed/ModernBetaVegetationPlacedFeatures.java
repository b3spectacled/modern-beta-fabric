package mod.bespectacled.modernbeta.world.feature.placed;

import java.util.List;

import com.google.common.collect.ImmutableList;

import mod.bespectacled.modernbeta.world.feature.configured.ModernBetaVegetationConfiguredFeatures;
import mod.bespectacled.modernbeta.world.feature.placement.HeightmapSpreadDoublePlacementModifier;
import mod.bespectacled.modernbeta.world.feature.placement.NoiseBasedCountPlacementModifier;
import mod.bespectacled.modernbeta.world.feature.placement.NoiseBasedCountPlacementModifierAlpha;
import mod.bespectacled.modernbeta.world.feature.placement.NoiseBasedCountPlacementModifierBeta;
import mod.bespectacled.modernbeta.world.feature.placement.NoiseBasedCountPlacementModifierInfdev415;
import mod.bespectacled.modernbeta.world.feature.placement.NoiseBasedCountPlacementModifierInfdev420;
import mod.bespectacled.modernbeta.world.feature.placement.NoiseBasedCountPlacementModifierInfdev611;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.VegetationConfiguredFeatures;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import net.minecraft.world.gen.placementmodifier.SurfaceWaterDepthFilterPlacementModifier;

public class ModernBetaVegetationPlacedFeatures {
    public static final PlacementModifier SURFACE_WATER_DEPTH = SurfaceWaterDepthFilterPlacementModifier.of(0);
    public static final PlacementModifier HEIGHTMAP_SPREAD_DOUBLE = HeightmapSpreadDoublePlacementModifier.of(Heightmap.Type.MOTION_BLOCKING);
    public static final PlacementModifier HEIGHT_RANGE_128 = HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(127));
    public static final PlacementModifier WORLD_SURFACE_WG_HEIGHTMAP = PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP;
    public static final PlacementModifier MOTION_BLOCKING_HEIGHTMAP = PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP;
    
    private static ImmutableList.Builder<PlacementModifier> withBaseTreeModifiers(PlacementModifier modifier) {
        return ImmutableList.<PlacementModifier>builder()
            .add(modifier)
            .add(SquarePlacementModifier.of())
            .add(SURFACE_WATER_DEPTH)
            .add(PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP)
            .add(BiomePlacementModifier.of());
    }
    
    private static ImmutableList.Builder<PlacementModifier> withBaseModifiers(PlacementModifier modifier) {
        return ImmutableList.<PlacementModifier>builder()
            .add(modifier)
            .add(SquarePlacementModifier.of())
            .add(BiomePlacementModifier.of());
    }

    public static List<PlacementModifier> withTreeModifier(PlacementModifier modifier) {
        return withBaseTreeModifiers(modifier).build();
    }
    
    public static List<PlacementModifier> withCountModifier(int count) {
        return withBaseModifiers(CountPlacementModifier.of(count)).build();
    }
    
    public static List<PlacementModifier> withCountExtraAndTreeModifier(int count, float extraChance, int extraCount) {
        return withTreeModifier(PlacedFeatures.createCountExtraModifier(count, extraChance, extraCount));
    }
    
    public static PlacementModifier withCountExtraModifier(int count, float extraChance, int extraCount) {
        return PlacedFeatures.createCountExtraModifier(count, extraChance, extraCount);
    }
    
    public static List<PlacementModifier> withNoiseBasedCountModifier(String id, NoiseBasedCountPlacementModifier modifier) {
        return withBaseTreeModifiers(modifier).build();
    }
    
    public static final RegistryKey<PlacedFeature> PATCH_CACTUS_ALPHA = ModernBetaPlacedFeatures.of("patch_cactus_alpha");
    public static final RegistryKey<PlacedFeature> PATCH_CACTUS_PE = ModernBetaPlacedFeatures.of("patch_cactus_pe");
    public static final RegistryKey<PlacedFeature> MUSHROOM_HELL = ModernBetaPlacedFeatures.of("mushroom_hell");
    
    public static final RegistryKey<PlacedFeature> PATCH_DANDELION_2 = ModernBetaPlacedFeatures.of("patch_dandelion_2");
    public static final RegistryKey<PlacedFeature> PATCH_DANDELION_3 = ModernBetaPlacedFeatures.of("patch_dandelion_3");
    public static final RegistryKey<PlacedFeature> PATCH_DANDELION_4 = ModernBetaPlacedFeatures.of("patch_dandelion_4");
    public static final RegistryKey<PlacedFeature> PATCH_DANDELION = ModernBetaPlacedFeatures.of("patch_dandelion");
    public static final RegistryKey<PlacedFeature> PATCH_POPPY = ModernBetaPlacedFeatures.of("patch_poppy");
    public static final RegistryKey<PlacedFeature> PATCH_FLOWER_PARADISE = ModernBetaPlacedFeatures.of("patch_flower_paradise");
    public static final RegistryKey<PlacedFeature> PATCH_DANDELION_INFDEV_227 = ModernBetaPlacedFeatures.of("patch_dandelion_infdev_227");

    public static final RegistryKey<PlacedFeature> PATCH_GRASS_PLAINS_10 = ModernBetaPlacedFeatures.of("patch_grass_plains_10");
    public static final RegistryKey<PlacedFeature> PATCH_GRASS_TAIGA_1 = ModernBetaPlacedFeatures.of("patch_grass_taiga_1");
    public static final RegistryKey<PlacedFeature> PATCH_GRASS_RAINFOREST_10 = ModernBetaPlacedFeatures.of("patch_grass_rainforest_10");
    public static final RegistryKey<PlacedFeature> PATCH_GRASS_ALPHA_2 = ModernBetaPlacedFeatures.of("patch_grass_alpha_2");
    
    public static final RegistryKey<PlacedFeature> TREES_ALPHA = ModernBetaPlacedFeatures.of("trees_alpha");
    public static final RegistryKey<PlacedFeature> TREES_INFDEV_611 = ModernBetaPlacedFeatures.of("trees_infdev_611");
    public static final RegistryKey<PlacedFeature> TREES_INFDEV_420 = ModernBetaPlacedFeatures.of("trees_infdev_420");
    public static final RegistryKey<PlacedFeature> TREES_INFDEV_415 = ModernBetaPlacedFeatures.of("trees_infdev_415");
    public static final RegistryKey<PlacedFeature> TREES_INFDEV_227 = ModernBetaPlacedFeatures.of("trees_infdev_227");

    public static final RegistryKey<PlacedFeature> TREES_ALPHA_BEES = ModernBetaPlacedFeatures.of("trees_alpha_bees");
    public static final RegistryKey<PlacedFeature> TREES_INFDEV_611_BEES = ModernBetaPlacedFeatures.of("trees_infdev_611_bees");
    public static final RegistryKey<PlacedFeature> TREES_INFDEV_420_BEES = ModernBetaPlacedFeatures.of("trees_infdev_420_bees");
    public static final RegistryKey<PlacedFeature> TREES_INFDEV_415_BEES = ModernBetaPlacedFeatures.of("trees_infdev_415_bees");
    public static final RegistryKey<PlacedFeature> TREES_INFDEV_227_BEES = ModernBetaPlacedFeatures.of("trees_infdev_227_bees");
    
    public static final RegistryKey<PlacedFeature> TREES_BETA_FOREST = ModernBetaPlacedFeatures.of("trees_beta_forest");
    public static final RegistryKey<PlacedFeature> TREES_BETA_RAINFOREST = ModernBetaPlacedFeatures.of("trees_beta_rainforest");
    public static final RegistryKey<PlacedFeature> TREES_BETA_SEASONAL_FOREST = ModernBetaPlacedFeatures.of("trees_beta_seasonal_forest");
    public static final RegistryKey<PlacedFeature> TREES_BETA_SPARSE = ModernBetaPlacedFeatures.of("trees_beta_sparse");
    public static final RegistryKey<PlacedFeature> TREES_BETA_TAIGA = ModernBetaPlacedFeatures.of("trees_beta_taiga");

    public static final RegistryKey<PlacedFeature> TREES_BETA_FOREST_BEES = ModernBetaPlacedFeatures.of("trees_beta_forest_bees");
    public static final RegistryKey<PlacedFeature> TREES_BETA_RAINFOREST_BEES = ModernBetaPlacedFeatures.of("trees_beta_rainforest_bees");
    public static final RegistryKey<PlacedFeature> TREES_BETA_SEASONAL_FOREST_BEES = ModernBetaPlacedFeatures.of("trees_beta_seasonal_forest_bees");
    public static final RegistryKey<PlacedFeature> TREES_BETA_SPARSE_BEES = ModernBetaPlacedFeatures.of("trees_beta_sparse_bees");
    
    public static final RegistryKey<PlacedFeature> TREES_PE_FOREST = ModernBetaPlacedFeatures.of("trees_pe_forest");
    public static final RegistryKey<PlacedFeature> TREES_PE_RAINFOREST = ModernBetaPlacedFeatures.of("trees_pe_rainforest");
    public static final RegistryKey<PlacedFeature> TREES_PE_SEASONAL_FOREST = ModernBetaPlacedFeatures.of("trees_pe_seasonal_forest");
    public static final RegistryKey<PlacedFeature> TREES_PE_SPARSE = ModernBetaPlacedFeatures.of("trees_pe_sparse");
    public static final RegistryKey<PlacedFeature> TREES_PE_TAIGA = ModernBetaPlacedFeatures.of("trees_pe_taiga");

    public static final RegistryKey<PlacedFeature> TREES_PE_FOREST_BEES = ModernBetaPlacedFeatures.of("trees_pe_forest_bees");
    public static final RegistryKey<PlacedFeature> TREES_PE_RAINFOREST_BEES = ModernBetaPlacedFeatures.of("trees_pe_rainforest_bees");
    public static final RegistryKey<PlacedFeature> TREES_PE_SEASONAL_FOREST_BEES = ModernBetaPlacedFeatures.of("trees_pe_seasonal_forest_bees");
    public static final RegistryKey<PlacedFeature> TREES_PE_SPARSE_BEES = ModernBetaPlacedFeatures.of("trees_pe_sparse_bees");

    public static final RegistryKey<PlacedFeature> TREES_INDEV = ModernBetaPlacedFeatures.of("trees_indev");
    public static final RegistryKey<PlacedFeature> TREES_INDEV_WOODS = ModernBetaPlacedFeatures.of("trees_indev_woods");
    
    public static final RegistryKey<PlacedFeature> TREES_INDEV_BEES = ModernBetaPlacedFeatures.of("trees_indev_bees");
    public static final RegistryKey<PlacedFeature> TREES_INDEV_WOODS_BEES = ModernBetaPlacedFeatures.of("trees_indev_woods_bees");
    
    public static void bootstrap(Registerable<PlacedFeature> featureRegisterable) {
        RegistryEntryLookup<ConfiguredFeature<?, ?>> registryConfigured = featureRegisterable.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        RegistryEntry.Reference<ConfiguredFeature<?, ?>> patchCactus = registryConfigured.getOrThrow(VegetationConfiguredFeatures.PATCH_CACTUS);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> mushroomHell = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.MUSHROOM_HELL);
        
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> patchDandelion = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.PATCH_DANDELION);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> patchPoppy = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.PATCH_POPPY);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> flowerDefault = registryConfigured.getOrThrow(VegetationConfiguredFeatures.FLOWER_DEFAULT);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> patchDandelionInfdev227 = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.PATCH_DANDELION_INFDEV_227);
        
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> patchGrass = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.PATCH_GRASS);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> patchGrassLush = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.PATCH_GRASS_LUSH);

        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesAlpha = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_ALPHA);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesInfdev611 = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_INFDEV_611);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesInfdev420 = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_INFDEV_420);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesInfdev415 = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_INFDEV_415);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesInfdev227 = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_INFDEV_227);

        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesAlphaBees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_ALPHA_BEES);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesInfdev611Bees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_INFDEV_611_BEES);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesInfdev420Bees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_INFDEV_420_BEES);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesInfdev415Bees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_INFDEV_415_BEES);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesInfdev227Bees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_INFDEV_227_BEES);

        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesBetaForest = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_BETA_FOREST);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesBetaRainforest = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_BETA_RAINFOREST);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesBetaSeasonalForest = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_BETA_SEASONAL_FOREST);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesBetaSparse = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_BETA_SPARSE);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesBetaTaiga = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_BETA_TAIGA);

        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesBetaForestBees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_BETA_FOREST_BEES);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesBetaRainforestBees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_BETA_RAINFOREST_BEES);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesBetaSeasonalForestBees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_BETA_SEASONAL_FOREST_BEES);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesBetaSparseBees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_BETA_SPARSE_BEES);
        
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesPEForest = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_PE_FOREST);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesPERainforest = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_PE_RAINFOREST);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesPESeasonalForest = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_PE_SEASONAL_FOREST);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesPESparse = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_PE_SPARSE);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesPETaiga = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_PE_TAIGA);

        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesPEForestBees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_PE_FOREST_BEES);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesPERainforestBees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_PE_RAINFOREST_BEES);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesPESeasonalForestBees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_PE_SEASONAL_FOREST_BEES);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesPESparseBees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_PE_SPARSE_BEES);

        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesIndev = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_INDEV);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesIndevWoods = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_INDEV_WOODS);
        
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesIndevBees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_INDEV_BEES);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> treesIndevWoodsBees = registryConfigured.getOrThrow(ModernBetaVegetationConfiguredFeatures.TREES_INDEV_WOODS_BEES);
        
        //PlacedFeatures.register(featureRegisterable, MUSHROOM_HELL, mushroomHell, null);
        
        PlacedFeatures.register(featureRegisterable, PATCH_CACTUS_ALPHA, patchCactus, CountPlacementModifier.of(2), SquarePlacementModifier.of(), HEIGHTMAP_SPREAD_DOUBLE, BiomePlacementModifier.of());
        PlacedFeatures.register(featureRegisterable, PATCH_CACTUS_PE, patchCactus, CountPlacementModifier.of(5), SquarePlacementModifier.of(), HEIGHTMAP_SPREAD_DOUBLE, BiomePlacementModifier.of());
        PlacedFeatures.register(featureRegisterable, MUSHROOM_HELL, mushroomHell, CountPlacementModifier.of(1), SquarePlacementModifier.of(), MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of());
        
        PlacedFeatures.register(featureRegisterable, PATCH_DANDELION_2, patchDandelion, CountPlacementModifier.of(2), SquarePlacementModifier.of(), HEIGHT_RANGE_128, BiomePlacementModifier.of());
        PlacedFeatures.register(featureRegisterable, PATCH_DANDELION_3, patchDandelion, CountPlacementModifier.of(3), SquarePlacementModifier.of(), HEIGHT_RANGE_128, BiomePlacementModifier.of());
        PlacedFeatures.register(featureRegisterable, PATCH_DANDELION_4, patchDandelion, CountPlacementModifier.of(4), SquarePlacementModifier.of(), HEIGHT_RANGE_128, BiomePlacementModifier.of());
        PlacedFeatures.register(featureRegisterable, PATCH_DANDELION, patchDandelion, withCountExtraModifier(0, 0.5f, 1), SquarePlacementModifier.of(), HEIGHT_RANGE_128, BiomePlacementModifier.of());
        PlacedFeatures.register(featureRegisterable, PATCH_POPPY, patchPoppy, withCountExtraModifier(0, 0.5f, 1), SquarePlacementModifier.of(), HEIGHT_RANGE_128, BiomePlacementModifier.of());
        PlacedFeatures.register(featureRegisterable, PATCH_FLOWER_PARADISE, flowerDefault, CountPlacementModifier.of(20), SquarePlacementModifier.of(), HEIGHT_RANGE_128, BiomePlacementModifier.of());
        PlacedFeatures.register(featureRegisterable, PATCH_DANDELION_INFDEV_227, patchDandelionInfdev227, CountPlacementModifier.of(UniformIntProvider.create(0, 10)), SquarePlacementModifier.of(), SURFACE_WATER_DEPTH, PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP, BiomePlacementModifier.of());
    
        PlacedFeatures.register(featureRegisterable, PATCH_GRASS_PLAINS_10, patchGrass, CountPlacementModifier.of(10), SquarePlacementModifier.of(), WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of());
        PlacedFeatures.register(featureRegisterable, PATCH_GRASS_TAIGA_1, patchGrass, CountPlacementModifier.of(1), SquarePlacementModifier.of(), WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of());
        PlacedFeatures.register(featureRegisterable, PATCH_GRASS_RAINFOREST_10, patchGrassLush, CountPlacementModifier.of(10), SquarePlacementModifier.of(), WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of());
        PlacedFeatures.register(featureRegisterable, PATCH_GRASS_ALPHA_2, patchGrass, withCountExtraModifier(0, 0.1f, 1), SquarePlacementModifier.of(), WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of());
        
        PlacedFeatures.register(featureRegisterable, TREES_ALPHA, treesAlpha, withNoiseBasedCountModifier("trees_alpha", NoiseBasedCountPlacementModifierAlpha.of(0, 0.1f, 1)));
        PlacedFeatures.register(featureRegisterable, TREES_INFDEV_611, treesInfdev611, withNoiseBasedCountModifier("trees_infdev_611", NoiseBasedCountPlacementModifierInfdev611.of(0, 0.1f, 1)));
        PlacedFeatures.register(featureRegisterable, TREES_INFDEV_420, treesInfdev420, withNoiseBasedCountModifier("trees_infdev_420", NoiseBasedCountPlacementModifierInfdev420.of(0, 0.01f, 1)));
        PlacedFeatures.register(featureRegisterable, TREES_INFDEV_415, treesInfdev415, withNoiseBasedCountModifier("trees_infdev_415", NoiseBasedCountPlacementModifierInfdev415.of(0, 0, 0)));
        PlacedFeatures.register(featureRegisterable, TREES_INFDEV_227, treesInfdev227, withCountExtraAndTreeModifier(0, 0.1f, 1));
        
        PlacedFeatures.register(featureRegisterable, TREES_ALPHA_BEES, treesAlphaBees, withNoiseBasedCountModifier("trees_alpha_bees", NoiseBasedCountPlacementModifierAlpha.of(0, 0.1f, 1)));
        PlacedFeatures.register(featureRegisterable, TREES_INFDEV_611_BEES, treesInfdev611Bees, withNoiseBasedCountModifier("trees_infdev_611_bees", NoiseBasedCountPlacementModifierInfdev611.of(0, 0.1f, 1)));
        PlacedFeatures.register(featureRegisterable, TREES_INFDEV_420_BEES, treesInfdev420Bees, withNoiseBasedCountModifier("trees_infdev_420_bees", NoiseBasedCountPlacementModifierInfdev420.of(0, 0.01f, 1)));
        PlacedFeatures.register(featureRegisterable, TREES_INFDEV_415_BEES, treesInfdev415Bees, withNoiseBasedCountModifier("trees_infdev_415_bees", NoiseBasedCountPlacementModifierInfdev415.of(0, 0, 0)));
        PlacedFeatures.register(featureRegisterable, TREES_INFDEV_227_BEES, treesInfdev227Bees, withCountExtraAndTreeModifier(0, 0.1f, 1));
             
        PlacedFeatures.register(featureRegisterable, TREES_BETA_FOREST, treesBetaForest, withNoiseBasedCountModifier("trees_beta_forest", NoiseBasedCountPlacementModifierBeta.of(5, 0.1f, 1)));
        PlacedFeatures.register(featureRegisterable, TREES_BETA_RAINFOREST, treesBetaRainforest, withNoiseBasedCountModifier("trees_beta_rainforest", NoiseBasedCountPlacementModifierBeta.of(5, 0.1f, 1)));
        PlacedFeatures.register(featureRegisterable, TREES_BETA_SEASONAL_FOREST, treesBetaSeasonalForest, withNoiseBasedCountModifier("trees_beta_seasonal_forest", NoiseBasedCountPlacementModifierBeta.of(2, 0.1f, 1)));
        PlacedFeatures.register(featureRegisterable, TREES_BETA_SPARSE, treesBetaSparse, withCountExtraAndTreeModifier(0, 0.1f, 1));
        PlacedFeatures.register(featureRegisterable, TREES_BETA_TAIGA, treesBetaTaiga, withNoiseBasedCountModifier("trees_beta_taiga", NoiseBasedCountPlacementModifierBeta.of(5, 0.1f, 1)));
        
        PlacedFeatures.register(featureRegisterable, TREES_BETA_FOREST_BEES, treesBetaForestBees, withNoiseBasedCountModifier("trees_beta_forest_bees", NoiseBasedCountPlacementModifierBeta.of(5, 0.1f, 1)));
        PlacedFeatures.register(featureRegisterable, TREES_BETA_RAINFOREST_BEES, treesBetaRainforestBees, withNoiseBasedCountModifier("trees_beta_rainforest_bees", NoiseBasedCountPlacementModifierBeta.of(5, 0.1f, 1)));
        PlacedFeatures.register(featureRegisterable, TREES_BETA_SEASONAL_FOREST_BEES, treesBetaSeasonalForestBees, withNoiseBasedCountModifier("trees_beta_seasonal_forest_bees", NoiseBasedCountPlacementModifierBeta.of(2, 0.1f, 1)));
        PlacedFeatures.register(featureRegisterable, TREES_BETA_SPARSE_BEES, treesBetaSparseBees, withCountExtraAndTreeModifier(0, 0.1f, 1));
        
        PlacedFeatures.register(featureRegisterable, TREES_PE_FOREST, treesPEForest, withNoiseBasedCountModifier("trees_pe_forest_bees", NoiseBasedCountPlacementModifierBeta.of(2, 0.1f, 1)));
        PlacedFeatures.register(featureRegisterable, TREES_PE_RAINFOREST, treesPERainforest, withNoiseBasedCountModifier("trees_pe_rainforest_bees", NoiseBasedCountPlacementModifierBeta.of(2, 0.1f, 1)));
        PlacedFeatures.register(featureRegisterable, TREES_PE_SEASONAL_FOREST, treesPESeasonalForest, withNoiseBasedCountModifier("trees_pe_seasonal_forest_bees", NoiseBasedCountPlacementModifierBeta.of(1, 0.1f, 1)));
        PlacedFeatures.register(featureRegisterable, TREES_PE_SPARSE, treesPESparse, withCountExtraAndTreeModifier(0, 0.1f, 1));
        PlacedFeatures.register(featureRegisterable, TREES_PE_TAIGA, treesPETaiga, withNoiseBasedCountModifier("trees_pe_taiga", NoiseBasedCountPlacementModifierBeta.of(1, 0.1f, 1)));

        PlacedFeatures.register(featureRegisterable, TREES_PE_FOREST_BEES, treesPEForestBees, withNoiseBasedCountModifier("trees_pe_forest_bees", NoiseBasedCountPlacementModifierBeta.of(2, 0.1f, 1)));
        PlacedFeatures.register(featureRegisterable, TREES_PE_RAINFOREST_BEES, treesPERainforestBees, withNoiseBasedCountModifier("trees_pe_rainforest_bees", NoiseBasedCountPlacementModifierBeta.of(2, 0.1f, 1)));
        PlacedFeatures.register(featureRegisterable, TREES_PE_SEASONAL_FOREST_BEES, treesPESeasonalForestBees, withNoiseBasedCountModifier("trees_pe_seasonal_forest_bees", NoiseBasedCountPlacementModifierBeta.of(1, 0.1f, 1)));
        PlacedFeatures.register(featureRegisterable, TREES_PE_SPARSE_BEES, treesPESparseBees, withCountExtraAndTreeModifier(0, 0.1f, 1));
        
        PlacedFeatures.register(featureRegisterable, TREES_INDEV, treesIndev, RarityFilterPlacementModifier.of(3), withCountExtraModifier(5, 0.1f, 1), SquarePlacementModifier.of(), SURFACE_WATER_DEPTH, PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP, BiomePlacementModifier.of());
        PlacedFeatures.register(featureRegisterable, TREES_INDEV_WOODS, treesIndevWoods, withCountExtraModifier(30, 0.1f, 1), SquarePlacementModifier.of(), SURFACE_WATER_DEPTH, PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP, BiomePlacementModifier.of());
        
        PlacedFeatures.register(featureRegisterable, TREES_INDEV_BEES, treesIndevBees, RarityFilterPlacementModifier.of(3), withCountExtraModifier(5, 0.1f, 1), SquarePlacementModifier.of(), SURFACE_WATER_DEPTH, PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP, BiomePlacementModifier.of());
        PlacedFeatures.register(featureRegisterable, TREES_INDEV_WOODS_BEES, treesIndevWoodsBees, withCountExtraModifier(30, 0.1f, 1), SquarePlacementModifier.of(), SURFACE_WATER_DEPTH, PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP, BiomePlacementModifier.of());
    }
}
