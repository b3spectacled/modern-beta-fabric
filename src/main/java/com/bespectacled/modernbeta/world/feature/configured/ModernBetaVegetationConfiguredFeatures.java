package com.bespectacled.modernbeta.world.feature.configured;

import java.util.List;

import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.world.feature.placed.ModernBetaTreePlacedFeatures;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.RandomFeatureConfig;
import net.minecraft.world.gen.feature.RandomFeatureEntry;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.SimpleBlockFeatureConfig;
import net.minecraft.world.gen.feature.TreePlacedFeatures;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

public class ModernBetaVegetationConfiguredFeatures {
    private static final RegistryEntry<PlacedFeature> OLD_FANCY_OAK = ModernBetaTreePlacedFeatures.OLD_FANCY_OAK;
    private static final RegistryEntry<PlacedFeature> OAK_CHECKED = TreePlacedFeatures.OAK_CHECKED;
    private static final RegistryEntry<PlacedFeature> BIRCH_CHECKED = TreePlacedFeatures.BIRCH_CHECKED;
    private static final RegistryEntry<PlacedFeature> OAK_BEES_0002 = TreePlacedFeatures.OAK_BEES_0002;
    private static final RegistryEntry<PlacedFeature> BIRCH_BEES_0002 = TreePlacedFeatures.BIRCH_BEES_0002;
    private static final RegistryEntry<PlacedFeature> PINE_CHECKED = TreePlacedFeatures.PINE_CHECKED;
    private static final RegistryEntry<PlacedFeature> SPRUCE_CHECKED = TreePlacedFeatures.SPRUCE_CHECKED;
    
    private static RandomFeatureEntry withChance(RegistryEntry<PlacedFeature> feature, float chance) {
        return new RandomFeatureEntry(feature, chance);
    }
    
    private static DataPool.Builder<BlockState> pool() {
        return DataPool.<BlockState>builder();
    }
    
    private static final class OldRandomPatchConfigs {
        private static final int XZ_SPREAD = 7;
        private static final int Y_SPREAD = 3;
        private static final int TRIES = 64;
        private static final int GRASS_TRIES = 64;
        
        public static final RegistryEntry<PlacedFeature> DANDELION_PLACED_FEATURE;
        public static final RegistryEntry<PlacedFeature> POPPY_PLACED_FEATURE;
        
        public static final RegistryEntry<PlacedFeature> GRASS_FEATURE;
        public static final RegistryEntry<PlacedFeature> LUSH_GRASS_FEATURE;
        
        public static final RegistryEntry<PlacedFeature> MUSHROOM_HELL_FEATURE;
        
        public static final RandomPatchFeatureConfig GRASS_CONFIG;
        public static final RandomPatchFeatureConfig LUSH_GRASS_CONFIG;
        
        public static final RandomPatchFeatureConfig DANDELION_CONFIG;
        public static final RandomPatchFeatureConfig POPPY_CONFIG;
        
        public static final RandomPatchFeatureConfig MUSHROOM_HELL;
        
        public static RandomPatchFeatureConfig createRandomPatchFeatureConfig(int tries, RegistryEntry<PlacedFeature> feature) {
            return new RandomPatchFeatureConfig(tries, XZ_SPREAD, Y_SPREAD, feature);
        }
        
        static {
            DANDELION_PLACED_FEATURE = PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(BlockStateProvider.of(Blocks.DANDELION)));
            POPPY_PLACED_FEATURE = PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(BlockStateProvider.of(Blocks.POPPY)));
            
            GRASS_FEATURE = PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(BlockStateProvider.of(Blocks.GRASS)));
            LUSH_GRASS_FEATURE = PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(pool().add(BlockStates.GRASS, 1).add(BlockStates.FERN, 4))));
            
            MUSHROOM_HELL_FEATURE = PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(pool().add(Blocks.BROWN_MUSHROOM.getDefaultState(), 2).add(Blocks.RED_MUSHROOM.getDefaultState(), 1))));
            
            // # of tries in Beta equivalent is 128, but here it seems to generate too much grass,
            // so keep # of tries at 64 for now.
            GRASS_CONFIG = createRandomPatchFeatureConfig(GRASS_TRIES, GRASS_FEATURE);
            LUSH_GRASS_CONFIG = createRandomPatchFeatureConfig(GRASS_TRIES, LUSH_GRASS_FEATURE);
        
            DANDELION_CONFIG = createRandomPatchFeatureConfig(TRIES, DANDELION_PLACED_FEATURE);
            POPPY_CONFIG = createRandomPatchFeatureConfig(TRIES, POPPY_PLACED_FEATURE);
            
            MUSHROOM_HELL = createRandomPatchFeatureConfig(TRIES, MUSHROOM_HELL_FEATURE);
        }
    }
    
    // Shrubs
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> MUSHROOM_HELL = ModernBetaConfiguredFeatures.register(
        "mushroom_hell",
        Feature.FLOWER,
        OldRandomPatchConfigs.MUSHROOM_HELL
    );

    // Flowers
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> PATCH_DANDELION = ModernBetaConfiguredFeatures.register(
        "patch_dandelion",
        Feature.FLOWER,
        OldRandomPatchConfigs.DANDELION_CONFIG
    ); 
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> PATCH_POPPY = ModernBetaConfiguredFeatures.register(
        "patch_poppy",
        Feature.FLOWER,
        OldRandomPatchConfigs.POPPY_CONFIG
    );
    public static final RegistryEntry<ConfiguredFeature<SimpleBlockFeatureConfig, ?>> PATCH_DANDELION_INFDEV_227 = ModernBetaConfiguredFeatures.register(
        "patch_dandelion_infdev_227",
        Feature.SIMPLE_BLOCK,
        new SimpleBlockFeatureConfig(BlockStateProvider.of(Blocks.DANDELION))
    );
    
    // Grass
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> PATCH_GRASS = ModernBetaConfiguredFeatures.register(
        "patch_grass",
        Feature.RANDOM_PATCH,
        OldRandomPatchConfigs.GRASS_CONFIG
    );
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> PATCH_GRASS_LUSH = ModernBetaConfiguredFeatures.register(
        "patch_grass_lush",
        Feature.RANDOM_PATCH,
        OldRandomPatchConfigs.LUSH_GRASS_CONFIG
    );
    
    // Classic Trees
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_ALPHA = ModernBetaConfiguredFeatures.register(
        "trees_alpha",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OLD_FANCY_OAK, 0.1f)), OAK_CHECKED)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_INFDEV_611 = ModernBetaConfiguredFeatures.register(
        "trees_infdev_611",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(), OAK_CHECKED)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_INFDEV_420 = ModernBetaConfiguredFeatures.register(
        "trees_infdev_420",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OAK_CHECKED, 0.1f)), OLD_FANCY_OAK)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_INFDEV_415 = ModernBetaConfiguredFeatures.register(
        "trees_infdev_415",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OAK_CHECKED, 0.1f)), OLD_FANCY_OAK)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_INFDEV_227 = ModernBetaConfiguredFeatures.register(
        "trees_infdev_227",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OAK_CHECKED, 0.1f)), OAK_CHECKED)
    );
    
    // Classic Trees w/ bees
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_ALPHA_BEES = ModernBetaConfiguredFeatures.register(
        "trees_alpha_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OLD_FANCY_OAK, 0.1f)), OAK_BEES_0002)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_INFDEV_611_BEES = ModernBetaConfiguredFeatures.register(
        "trees_infdev_611_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OAK_BEES_0002, 0.1f)), OAK_CHECKED)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_INFDEV_420_BEES = ModernBetaConfiguredFeatures.register(
        "trees_infdev_420_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OAK_BEES_0002, 0.1f)), OLD_FANCY_OAK)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_INFDEV_415_BEES = ModernBetaConfiguredFeatures.register(
        "trees_infdev_415_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OAK_BEES_0002, 0.1f)), OLD_FANCY_OAK)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_INFDEV_227_BEES = ModernBetaConfiguredFeatures.register(
        "trees_infdev_227_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OAK_BEES_0002, 0.1f)), OAK_BEES_0002)
    );
    
    // Beta Trees
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_BETA_FOREST = ModernBetaConfiguredFeatures.register(
        "trees_beta_forest",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(BIRCH_CHECKED, 0.2f), withChance(OLD_FANCY_OAK, 0.33333334f)), OAK_CHECKED)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_BETA_RAINFOREST = ModernBetaConfiguredFeatures.register(
        "trees_beta_rainforest",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OLD_FANCY_OAK, 0.33333334f)), OAK_CHECKED)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_BETA_SEASONAL_FOREST = ModernBetaConfiguredFeatures.register(
        "trees_beta_seasonal_forest",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OLD_FANCY_OAK, 0.1f)), OAK_CHECKED)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_BETA_TAIGA = ModernBetaConfiguredFeatures.register(
        "trees_beta_taiga",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(PINE_CHECKED, 0.33333334f)), SPRUCE_CHECKED)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_BETA_SPARSE = ModernBetaConfiguredFeatures.register(
        "trees_beta_sparse",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OLD_FANCY_OAK, 0.1f)), OAK_CHECKED)
    );
    
    // Beta Trees w/ bees
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_BETA_FOREST_BEES = ModernBetaConfiguredFeatures.register(
        "trees_beta_forest_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(BIRCH_BEES_0002, 0.2f), withChance(OLD_FANCY_OAK, 0.33333334f)), OAK_BEES_0002)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_BETA_RAINFOREST_BEES = ModernBetaConfiguredFeatures.register(
        "trees_beta_rainforest_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OLD_FANCY_OAK, 0.33333334f)), OAK_BEES_0002)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_BETA_SEASONAL_FOREST_BEES = ModernBetaConfiguredFeatures.register(
        "trees_beta_seasonal_forest_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OLD_FANCY_OAK, 0.1f)), OAK_BEES_0002)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_BETA_SPARSE_BEES = ModernBetaConfiguredFeatures.register(
        "trees_beta_sparse_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OLD_FANCY_OAK, 0.1f)), OAK_BEES_0002)
    );
    
    // PE Trees
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_PE_TAIGA = ModernBetaConfiguredFeatures.register(
        "trees_pe_taiga",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(PINE_CHECKED, 0.33333334f)), SPRUCE_CHECKED)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_PE_SPARSE = ModernBetaConfiguredFeatures.register(
        "trees_pe_sparse",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(), OAK_CHECKED)
    );
    
    // PE Trees w/ bees
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_PE_FOREST_BEES = ModernBetaConfiguredFeatures.register(
        "trees_pe_forest_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(BIRCH_BEES_0002, 0.2f)), OAK_BEES_0002)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_PE_RAINFOREST_BEES = ModernBetaConfiguredFeatures.register(
        "trees_pe_rainforest_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(), OAK_BEES_0002)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_PE_SEASONAL_FOREST_BEES = ModernBetaConfiguredFeatures.register(
        "trees_pe_seasonal_forest_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(), OAK_BEES_0002)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_PE_SPARSE_BEES = ModernBetaConfiguredFeatures.register(
        "trees_pe_sparse_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(), OAK_BEES_0002)
    );
    
    // Indev Trees w/ bees
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_INDEV = ModernBetaConfiguredFeatures.register(
        "trees_indev",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OAK_CHECKED, 0.1f)), OAK_CHECKED)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_INDEV_WOODS = ModernBetaConfiguredFeatures.register(
        "trees_indev_woods",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OAK_CHECKED, 0.1f)), OAK_CHECKED)
    );
    
    // Indev Trees w/ bees
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_INDEV_BEES = ModernBetaConfiguredFeatures.register(
        "trees_indev_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OAK_BEES_0002, 0.1f)), OAK_BEES_0002)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_INDEV_WOODS_BEES = ModernBetaConfiguredFeatures.register(
        "trees_indev_woods_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OAK_BEES_0002, 0.1f)), OAK_BEES_0002)
    );
}
