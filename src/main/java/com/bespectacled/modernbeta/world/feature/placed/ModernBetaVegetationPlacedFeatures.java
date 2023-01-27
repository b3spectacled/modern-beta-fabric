package com.bespectacled.modernbeta.world.feature.placed;

import java.util.List;

import com.bespectacled.modernbeta.world.feature.configured.ModernBetaVegetationConfiguredFeatures;
import com.bespectacled.modernbeta.world.feature.placement.AlphaNoiseBasedCountPlacementModifier;
import com.bespectacled.modernbeta.world.feature.placement.BetaNoiseBasedCountPlacementModifier;
import com.bespectacled.modernbeta.world.feature.placement.HeightmapSpreadDoublePlacementModifier;
import com.bespectacled.modernbeta.world.feature.placement.Infdev415NoiseBasedCountPlacementModifier;
import com.bespectacled.modernbeta.world.feature.placement.Infdev420NoiseBasedCountPlacementModifier;
import com.bespectacled.modernbeta.world.feature.placement.Infdev611NoiseBasedCountPlacementModifier;
import com.bespectacled.modernbeta.world.feature.placement.ModernBetaNoiseBasedCountPlacementModifier;
import com.google.common.collect.ImmutableList;

import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.YOffset;
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
    public static final PlacementModifier SURFACE_WATER_DEPTH_MODIFIER = SurfaceWaterDepthFilterPlacementModifier.of(0);
    public static final PlacementModifier HEIGHTMAP_SPREAD_DOUBLE_MODIFIER = HeightmapSpreadDoublePlacementModifier.of(Heightmap.Type.MOTION_BLOCKING);
    public static final PlacementModifier HEIGHT_RANGE_128 = HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(127));
    public static final PlacementModifier WORLD_SURFACE_WG_HEIGHTMAP = PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP;
    public static final PlacementModifier MOTION_BLOCKING_HEIGHTMAP = PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP;
    
    private static ImmutableList.Builder<PlacementModifier> withBaseTreeModifiers(PlacementModifier modifier) {
        return ImmutableList.<PlacementModifier>builder()
            .add(modifier)
            .add(SquarePlacementModifier.of())
            .add(SURFACE_WATER_DEPTH_MODIFIER)
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
    
    public static List<PlacementModifier> withNoiseBasedCountModifier(String id, ModernBetaNoiseBasedCountPlacementModifier modifier) {
        return withBaseTreeModifiers(modifier).build();
    }
    
    // Shrubs
    public static final RegistryEntry<PlacedFeature> PATCH_CACTUS_ALPHA = ModernBetaPlacedFeatures.register("patch_cactus", VegetationConfiguredFeatures.PATCH_CACTUS, CountPlacementModifier.of(2), SquarePlacementModifier.of(), HEIGHTMAP_SPREAD_DOUBLE_MODIFIER, BiomePlacementModifier.of());
    public static final RegistryEntry<PlacedFeature> PATCH_CACTUS_PE = ModernBetaPlacedFeatures.register("patch_cactus_pe", VegetationConfiguredFeatures.PATCH_CACTUS, CountPlacementModifier.of(5), SquarePlacementModifier.of(), HEIGHTMAP_SPREAD_DOUBLE_MODIFIER, BiomePlacementModifier.of());
    public static final RegistryEntry<PlacedFeature> MUSHROOM_HELL = ModernBetaPlacedFeatures.register("mushroom_hell", ModernBetaVegetationConfiguredFeatures.MUSHROOM_HELL, CountPlacementModifier.of(1), SquarePlacementModifier.of(), MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of());

    // Flowers
    public static final RegistryEntry<PlacedFeature> PATCH_DANDELION_2 = ModernBetaPlacedFeatures.register("patch_dandelion_2", ModernBetaVegetationConfiguredFeatures.PATCH_DANDELION, CountPlacementModifier.of(2), SquarePlacementModifier.of(), HEIGHT_RANGE_128, BiomePlacementModifier.of());
    public static final RegistryEntry<PlacedFeature> PATCH_DANDELION_3 = ModernBetaPlacedFeatures.register("patch_dandelion_3", ModernBetaVegetationConfiguredFeatures.PATCH_DANDELION, CountPlacementModifier.of(3), SquarePlacementModifier.of(), HEIGHT_RANGE_128, BiomePlacementModifier.of());
    public static final RegistryEntry<PlacedFeature> PATCH_DANDELION_4 = ModernBetaPlacedFeatures.register("patch_dandelion_4", ModernBetaVegetationConfiguredFeatures.PATCH_DANDELION, CountPlacementModifier.of(4), SquarePlacementModifier.of(), HEIGHT_RANGE_128, BiomePlacementModifier.of());
    public static final RegistryEntry<PlacedFeature> PATCH_DANDELION = ModernBetaPlacedFeatures.register("patch_dandelion", ModernBetaVegetationConfiguredFeatures.PATCH_DANDELION, withCountExtraModifier(0, 0.5f, 1), SquarePlacementModifier.of(), HEIGHT_RANGE_128, BiomePlacementModifier.of());
    public static final RegistryEntry<PlacedFeature> PATCH_POPPY = ModernBetaPlacedFeatures.register("patch_poppy", ModernBetaVegetationConfiguredFeatures.PATCH_POPPY, withCountExtraModifier(0, 0.5f, 1), SquarePlacementModifier.of(), HEIGHT_RANGE_128, BiomePlacementModifier.of());
    public static final RegistryEntry<PlacedFeature> PATCH_FLOWER_PARADISE = ModernBetaPlacedFeatures.register("patch_flower_paradise", VegetationConfiguredFeatures.FLOWER_DEFAULT, CountPlacementModifier.of(20), SquarePlacementModifier.of(), HEIGHT_RANGE_128, BiomePlacementModifier.of());
    public static final RegistryEntry<PlacedFeature> PATCH_DANDELION_INFDEV_227 = ModernBetaPlacedFeatures.register("patch_dandelion_infdev_227", ModernBetaVegetationConfiguredFeatures.PATCH_DANDELION_INFDEV_227, CountPlacementModifier.of(UniformIntProvider.create(0, 10)), SquarePlacementModifier.of(), SURFACE_WATER_DEPTH_MODIFIER, PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP, BiomePlacementModifier.of());
    
    // Grass
    public static final RegistryEntry<PlacedFeature> PATCH_GRASS_PLAINS_10 = ModernBetaPlacedFeatures.register("patch_grass_plains_10",ModernBetaVegetationConfiguredFeatures.PATCH_GRASS, CountPlacementModifier.of(10), SquarePlacementModifier.of(), WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of());
    public static final RegistryEntry<PlacedFeature> PATCH_GRASS_TAIGA_1 = ModernBetaPlacedFeatures.register("patch_grass_taiga_1", ModernBetaVegetationConfiguredFeatures.PATCH_GRASS, CountPlacementModifier.of(1), SquarePlacementModifier.of(), WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of());
    public static final RegistryEntry<PlacedFeature> PATCH_GRASS_RAINFOREST_10 = ModernBetaPlacedFeatures.register("patch_grass_rainforest_10", ModernBetaVegetationConfiguredFeatures.PATCH_GRASS_LUSH, CountPlacementModifier.of(10), SquarePlacementModifier.of(), WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of());
    public static final RegistryEntry<PlacedFeature> PATCH_GRASS_ALPHA_2 = ModernBetaPlacedFeatures.register("patch_grass_alpha_2", ModernBetaVegetationConfiguredFeatures.PATCH_GRASS, withCountExtraModifier(0, 0.1f, 1), SquarePlacementModifier.of(), WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of());
    
    // Classic Trees
    public static final RegistryEntry<PlacedFeature> TREES_ALPHA = ModernBetaPlacedFeatures.register("trees_alpha", ModernBetaVegetationConfiguredFeatures.TREES_ALPHA, withNoiseBasedCountModifier("trees_alpha", AlphaNoiseBasedCountPlacementModifier.of(0, 0.1f, 1)));
    public static final RegistryEntry<PlacedFeature> TREES_INFDEV_611 = ModernBetaPlacedFeatures.register("trees_infdev_611", ModernBetaVegetationConfiguredFeatures.TREES_INFDEV_611, withNoiseBasedCountModifier("trees_infdev_611", Infdev611NoiseBasedCountPlacementModifier.of(0, 0.1f, 1)));
    public static final RegistryEntry<PlacedFeature> TREES_INFDEV_420 = ModernBetaPlacedFeatures.register("trees_infdev_420", ModernBetaVegetationConfiguredFeatures.TREES_INFDEV_420, withNoiseBasedCountModifier("trees_infdev_420", Infdev420NoiseBasedCountPlacementModifier.of(0, 0.01f, 1)));
    public static final RegistryEntry<PlacedFeature> TREES_INFDEV_415 = ModernBetaPlacedFeatures.register("trees_infdev_415", ModernBetaVegetationConfiguredFeatures.TREES_INFDEV_415, withNoiseBasedCountModifier("trees_infdev_415", Infdev415NoiseBasedCountPlacementModifier.of(0, 0, 0)));
    public static final RegistryEntry<PlacedFeature> TREES_INFDEV_227 = ModernBetaPlacedFeatures.register("trees_infdev_227", ModernBetaVegetationConfiguredFeatures.TREES_INFDEV_227, withCountExtraAndTreeModifier(0, 0.1f, 1));
    
    // Classic Trees w/ bees
    public static final RegistryEntry<PlacedFeature> TREES_ALPHA_BEES = ModernBetaPlacedFeatures.register("trees_alpha_bees", ModernBetaVegetationConfiguredFeatures.TREES_ALPHA_BEES, withNoiseBasedCountModifier("trees_alpha_bees", AlphaNoiseBasedCountPlacementModifier.of(0, 0.1f, 1)));
    public static final RegistryEntry<PlacedFeature> TREES_INFDEV_611_BEES = ModernBetaPlacedFeatures.register("trees_infdev_611_bees", ModernBetaVegetationConfiguredFeatures.TREES_INFDEV_611_BEES, withNoiseBasedCountModifier("trees_infdev_611_bees", Infdev611NoiseBasedCountPlacementModifier.of(0, 0.1f, 1)));
    public static final RegistryEntry<PlacedFeature> TREES_INFDEV_420_BEES = ModernBetaPlacedFeatures.register("trees_infdev_420_bees", ModernBetaVegetationConfiguredFeatures.TREES_INFDEV_420_BEES, withNoiseBasedCountModifier("trees_infdev_420_bees", Infdev420NoiseBasedCountPlacementModifier.of(0, 0.01f, 1)));
    public static final RegistryEntry<PlacedFeature> TREES_INFDEV_415_BEES = ModernBetaPlacedFeatures.register("trees_infdev_415_bees", ModernBetaVegetationConfiguredFeatures.TREES_INFDEV_415_BEES, withNoiseBasedCountModifier("trees_infdev_415_bees", Infdev415NoiseBasedCountPlacementModifier.of(0, 0, 0)));
    public static final RegistryEntry<PlacedFeature> TREES_INFDEV_227_BEES = ModernBetaPlacedFeatures.register("trees_infdev_227_bees", ModernBetaVegetationConfiguredFeatures.TREES_INFDEV_227_BEES, withCountExtraAndTreeModifier(0, 0.1f, 1));
    
    // Beta Trees
    public static final RegistryEntry<PlacedFeature> TREES_BETA_FOREST = ModernBetaPlacedFeatures.register("trees_beta_forest", ModernBetaVegetationConfiguredFeatures.TREES_BETA_FOREST, withNoiseBasedCountModifier("trees_beta_forest", BetaNoiseBasedCountPlacementModifier.of(5, 0.1f, 1)));
    public static final RegistryEntry<PlacedFeature> TREES_BETA_RAINFOREST = ModernBetaPlacedFeatures.register("trees_beta_rainforest", ModernBetaVegetationConfiguredFeatures.TREES_BETA_RAINFOREST, withNoiseBasedCountModifier("trees_beta_rainforest", BetaNoiseBasedCountPlacementModifier.of(5, 0.1f, 1)));
    public static final RegistryEntry<PlacedFeature> TREES_BETA_SEASONAL_FOREST = ModernBetaPlacedFeatures.register("trees_beta_seasonal_forest", ModernBetaVegetationConfiguredFeatures.TREES_BETA_SEASONAL_FOREST, withNoiseBasedCountModifier("trees_beta_seasonal_forest", BetaNoiseBasedCountPlacementModifier.of(2, 0.1f, 1)));
    public static final RegistryEntry<PlacedFeature> TREES_BETA_TAIGA = ModernBetaPlacedFeatures.register("trees_beta_taiga", ModernBetaVegetationConfiguredFeatures.TREES_BETA_TAIGA, withNoiseBasedCountModifier("trees_beta_taiga", BetaNoiseBasedCountPlacementModifier.of(5, 0.1f, 1)));
    public static final RegistryEntry<PlacedFeature> TREES_BETA_SPARSE = ModernBetaPlacedFeatures.register("trees_beta_sparse", ModernBetaVegetationConfiguredFeatures.TREES_BETA_SPARSE, withCountExtraAndTreeModifier(0, 0.1f, 1));

    // Beta Trees w/ bees
    public static final RegistryEntry<PlacedFeature> TREES_BETA_FOREST_BEES = ModernBetaPlacedFeatures.register("trees_beta_forest_bees", ModernBetaVegetationConfiguredFeatures.TREES_BETA_FOREST_BEES, withNoiseBasedCountModifier("trees_beta_forest_bees", BetaNoiseBasedCountPlacementModifier.of(5, 0.1f, 1)));
    public static final RegistryEntry<PlacedFeature> TREES_BETA_RAINFOREST_BEES = ModernBetaPlacedFeatures.register("trees_beta_rainforest_bees", ModernBetaVegetationConfiguredFeatures.TREES_BETA_RAINFOREST_BEES, withNoiseBasedCountModifier("trees_beta_rainforest_bees", BetaNoiseBasedCountPlacementModifier.of(5, 0.1f, 1)));
    public static final RegistryEntry<PlacedFeature> TREES_BETA_SEASONAL_FOREST_BEES = ModernBetaPlacedFeatures.register("trees_beta_seasonal_forest_bees", ModernBetaVegetationConfiguredFeatures.TREES_BETA_SEASONAL_FOREST_BEES, withNoiseBasedCountModifier("trees_beta_seasonal_forest_bees", BetaNoiseBasedCountPlacementModifier.of(2, 0.1f, 1)));
    public static final RegistryEntry<PlacedFeature> TREES_BETA_SPARSE_BEES = ModernBetaPlacedFeatures.register("trees_beta_sparse_bees", ModernBetaVegetationConfiguredFeatures.TREES_BETA_SPARSE_BEES, withCountExtraAndTreeModifier(0, 0.1f, 1));

    // PE Trees
    public static final RegistryEntry<PlacedFeature> TREES_PE_TAIGA = ModernBetaPlacedFeatures.register("trees_pe_taiga", ModernBetaVegetationConfiguredFeatures.TREES_PE_TAIGA, withNoiseBasedCountModifier("trees_pe_taiga", BetaNoiseBasedCountPlacementModifier.of(1, 0.1f, 1)));
    public static final RegistryEntry<PlacedFeature> TREES_PE_SPARSE = ModernBetaPlacedFeatures.register("trees_pe_sparse", ModernBetaVegetationConfiguredFeatures.TREES_PE_SPARSE, withCountExtraAndTreeModifier(0, 0.1f, 1));
    
    // PE Trees w/ bees
    public static final RegistryEntry<PlacedFeature> TREES_PE_FOREST_BEES = ModernBetaPlacedFeatures.register("trees_pe_forest_bees", ModernBetaVegetationConfiguredFeatures.TREES_PE_FOREST_BEES, withNoiseBasedCountModifier("trees_pe_forest_bees", BetaNoiseBasedCountPlacementModifier.of(2, 0.1f, 1)));
    public static final RegistryEntry<PlacedFeature> TREES_PE_RAINFOREST_BEES = ModernBetaPlacedFeatures.register("trees_pe_rainforest_bees", ModernBetaVegetationConfiguredFeatures.TREES_PE_RAINFOREST_BEES, withNoiseBasedCountModifier("trees_pe_forest_bees", BetaNoiseBasedCountPlacementModifier.of(2, 0.1f, 1)));
    public static final RegistryEntry<PlacedFeature> TREES_PE_SEASONAL_FOREST_BEES = ModernBetaPlacedFeatures.register("trees_pe_seasonal_forest_bees", ModernBetaVegetationConfiguredFeatures.TREES_PE_SEASONAL_FOREST_BEES, withNoiseBasedCountModifier("trees_pe_forest_bees", BetaNoiseBasedCountPlacementModifier.of(1, 0.1f, 1)));
    public static final RegistryEntry<PlacedFeature> TREES_PE_SPARSE_BEES = ModernBetaPlacedFeatures.register("trees_pe_sparse_bees", ModernBetaVegetationConfiguredFeatures.TREES_PE_SPARSE_BEES, withCountExtraAndTreeModifier(0, 0.1f, 1));
   
    // Indev Trees
    public static final RegistryEntry<PlacedFeature> TREES_INDEV = ModernBetaPlacedFeatures.register("trees_indev", ModernBetaVegetationConfiguredFeatures.TREES_INDEV, RarityFilterPlacementModifier.of(3), withCountExtraModifier(5, 0.1f, 1), SquarePlacementModifier.of(), SURFACE_WATER_DEPTH_MODIFIER, PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP, BiomePlacementModifier.of());
    public static final RegistryEntry<PlacedFeature> TREES_INDEV_WOODS = ModernBetaPlacedFeatures.register("trees_indev_woods", ModernBetaVegetationConfiguredFeatures.TREES_INDEV_WOODS, withCountExtraModifier(30, 0.1f, 1), SquarePlacementModifier.of(), SURFACE_WATER_DEPTH_MODIFIER, PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP, BiomePlacementModifier.of());
    
    // Indev Trees w/ bees
    public static final RegistryEntry<PlacedFeature> TREES_INDEV_BEES = ModernBetaPlacedFeatures.register("trees_indev_bees", ModernBetaVegetationConfiguredFeatures.TREES_INDEV_BEES, RarityFilterPlacementModifier.of(3), withCountExtraModifier(5, 0.1f, 1), SquarePlacementModifier.of(), SURFACE_WATER_DEPTH_MODIFIER, PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP, BiomePlacementModifier.of());
    public static final RegistryEntry<PlacedFeature> TREES_INDEV_WOODS_BEES = ModernBetaPlacedFeatures.register("trees_indev_woods_bees", ModernBetaVegetationConfiguredFeatures.TREES_INDEV_WOODS_BEES, withCountExtraModifier(30, 0.1f, 1), SquarePlacementModifier.of(), SURFACE_WATER_DEPTH_MODIFIER, PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP, BiomePlacementModifier.of());    
}
