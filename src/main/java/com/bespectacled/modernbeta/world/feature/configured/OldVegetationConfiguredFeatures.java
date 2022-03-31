package com.bespectacled.modernbeta.world.feature.configured;

import java.util.List;

import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.world.feature.placed.OldTreePlacedFeatures;
import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.RandomFeatureConfig;
import net.minecraft.world.gen.feature.RandomFeatureEntry;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.SimpleBlockFeatureConfig;
import net.minecraft.world.gen.feature.TreePlacedFeatures;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

public class OldVegetationConfiguredFeatures {
    
    private static final RegistryEntry<PlacedFeature> OLD_FANCY_OAK = OldTreePlacedFeatures.OLD_FANCY_OAK;
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
        
        public static final PlacedFeature DANDELION_PLACED_FEATURE;
        public static final PlacedFeature POPPY_PLACED_FEATURE;
        
        public static final RegistryEntry<ConfiguredFeature<?, ?>> GRASS_FEATURE;
        public static final RegistryEntry<ConfiguredFeature<?, ?>> LUSH_GRASS_FEATURE;
        
        public static final RegistryEntry<ConfiguredFeature<?, ?>> MUSHROOM_HELL_FEATURE;
        
        public static final RandomPatchFeatureConfig GRASS_CONFIG;
        public static final RandomPatchFeatureConfig LUSH_GRASS_CONFIG;
        
        public static final RandomPatchFeatureConfig DANDELION_CONFIG;
        public static final RandomPatchFeatureConfig POPPY_CONFIG;
        
        public static final RandomPatchFeatureConfig MUSHROOM_HELL;
        
        private static BlockPredicate createBlockPredicate(List<Block> validGround) {
            BlockPredicate predicate = !validGround.isEmpty() ? 
                BlockPredicate.bothOf(BlockPredicate.IS_AIR, BlockPredicate.matchingBlocks(validGround, new BlockPos(0, -1, 0))) : 
                BlockPredicate.IS_AIR;
            
            return predicate;
        }
        
        public static RandomPatchFeatureConfig createRandomPatchFeatureConfig(int tries, ConfiguredFeature<?, ?> feature) {
            return createRandomPatchFeatureConfig(
                feature,
                List.of(),
                tries
            );
        }
        
        public static RandomPatchFeatureConfig createRandomPatchFeatureConfig(int tries, PlacedFeature feature) {
            return new RandomPatchFeatureConfig(tries, XZ_SPREAD, Y_SPREAD, () -> feature);
        }
        
        public static RandomPatchFeatureConfig createRandomPatchFeatureConfig(ConfiguredFeature<?, ?> feature, List<Block> validGround, int tries) {
            return createRandomPatchFeatureConfig(tries, feature.withBlockPredicateFilter(createBlockPredicate(validGround)));
        }
        
        static {
            DANDELION_PLACED_FEATURE = Feature.SIMPLE_BLOCK,new SimpleBlockFeatureConfig(BlockStateProvider.of(Blocks.DANDELION))).withInAirFilter();
            POPPY_PLACED_FEATURE = Feature.SIMPLE_BLOCK,new SimpleBlockFeatureConfig(BlockStateProvider.of(Blocks.POPPY))).withInAirFilter();
            
            GRASS_FEATURE = Feature.SIMPLE_BLOCK,new SimpleBlockFeatureConfig(BlockStateProvider.of(Blocks.GRASS)));
            LUSH_GRASS_FEATURE = Feature.SIMPLE_BLOCK,new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(pool().add(BlockStates.GRASS, 1).add(BlockStates.FERN, 4))));
            
            MUSHROOM_HELL_FEATURE = Feature.SIMPLE_BLOCK,new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(pool().add(Blocks.BROWN_MUSHROOM.getDefaultState(), 2).add(Blocks.RED_MUSHROOM.getDefaultState(), 1))));
            
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
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> MUSHROOM_HELL = OldConfiguredFeatures.register(
        "mushroom_hell",
        Feature.FLOWER,
        OldRandomPatchConfigs.MUSHROOM_HELL
    );

    // Flowers
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> PATCH_DANDELION = OldConfiguredFeatures.register(
        "patch_dandelion",
        Feature.FLOWER,
        OldRandomPatchConfigs.DANDELION_CONFIG
    ); 
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> PATCH_POPPY = OldConfiguredFeatures.register(
        "patch_poppy",
        Feature.FLOWER,
        OldRandomPatchConfigs.POPPY_CONFIG
    );
    public static final RegistryEntry<ConfiguredFeature<SimpleBlockFeatureConfig, ?>> PATCH_DANDELION_INFDEV_227 = OldConfiguredFeatures.register(
        "patch_dandelion_infdev_227",
        Feature.SIMPLE_BLOCK,
        new SimpleBlockFeatureConfig(BlockStateProvider.of(Blocks.DANDELION))
    );
    
    // Grass
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> PATCH_GRASS = OldConfiguredFeatures.register(
        "patch_grass",
        Feature.RANDOM_PATCH,
        OldRandomPatchConfigs.GRASS_CONFIG
    );
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> PATCH_GRASS_LUSH = OldConfiguredFeatures.register(
        "patch_grass_lush",
        Feature.RANDOM_PATCH,
        OldRandomPatchConfigs.LUSH_GRASS_CONFIG
    );
    
    // Classic Trees
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_ALPHA = OldConfiguredFeatures.register(
        "trees_alpha",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OLD_FANCY_OAK, 0.1f)), OAK_CHECKED)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_INFDEV_611 = OldConfiguredFeatures.register(
        "trees_infdev_611",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(), OAK_CHECKED)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_INFDEV_420 = OldConfiguredFeatures.register(
        "trees_infdev_420",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OAK_CHECKED, 0.1f)), OLD_FANCY_OAK)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_INFDEV_415 = OldConfiguredFeatures.register(
        "trees_infdev_415",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OAK_CHECKED, 0.1f)), OLD_FANCY_OAK)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_INFDEV_227 = OldConfiguredFeatures.register(
        "trees_infdev_227",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OAK_CHECKED, 0.1f)), OAK_CHECKED)
    );
    
    // Classic Trees w/ bees
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_ALPHA_BEES = OldConfiguredFeatures.register(
        "trees_alpha_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OLD_FANCY_OAK, 0.1f)), OAK_BEES_0002)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_INFDEV_611_BEES = OldConfiguredFeatures.register(
        "trees_infdev_611_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OAK_BEES_0002, 0.1f)), OAK_CHECKED)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_INFDEV_420_BEES = OldConfiguredFeatures.register(
        "trees_infdev_420_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OAK_BEES_0002, 0.1f)), OLD_FANCY_OAK)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_INFDEV_415_BEES = OldConfiguredFeatures.register(
        "trees_infdev_415_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OAK_BEES_0002, 0.1f)), OLD_FANCY_OAK)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_INFDEV_227_BEES = OldConfiguredFeatures.register(
        "trees_infdev_227_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OAK_BEES_0002, 0.1f)), OAK_BEES_0002)
    );
    
    // Beta Trees
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_BETA_FOREST = OldConfiguredFeatures.register(
        "trees_beta_forest",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(BIRCH_CHECKED, 0.2f), withChance(OLD_FANCY_OAK, 0.33333334f)), OAK_CHECKED)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_BETA_RAINFOREST = OldConfiguredFeatures.register(
        "trees_beta_rainforest",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OLD_FANCY_OAK, 0.33333334f)), OAK_CHECKED)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_BETA_SEASONAL_FOREST = OldConfiguredFeatures.register(
        "trees_beta_seasonal_forest",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OLD_FANCY_OAK, 0.1f)), OAK_CHECKED)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_BETA_TAIGA = OldConfiguredFeatures.register(
        "trees_beta_taiga",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(PINE_CHECKED, 0.33333334f)), SPRUCE_CHECKED)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_BETA_SPARSE = OldConfiguredFeatures.register(
        "trees_beta_sparse",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OLD_FANCY_OAK, 0.1f)), OAK_CHECKED)
    );
    
    // Beta Trees w/ bees
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_BETA_FOREST_BEES = OldConfiguredFeatures.register(
        "trees_beta_forest_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(BIRCH_BEES_0002, 0.2f), withChance(OLD_FANCY_OAK, 0.33333334f)), OAK_BEES_0002)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_BETA_RAINFOREST_BEES = OldConfiguredFeatures.register(
        "trees_beta_rainforest_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OLD_FANCY_OAK, 0.33333334f)), OAK_BEES_0002)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_BETA_SEASONAL_FOREST_BEES = OldConfiguredFeatures.register(
        "trees_beta_seasonal_forest_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OLD_FANCY_OAK, 0.1f)), OAK_BEES_0002)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_BETA_SPARSE_BEES = OldConfiguredFeatures.register(
        "trees_beta_sparse_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OLD_FANCY_OAK, 0.1f)), OAK_BEES_0002)
    );
    
    // PE Trees
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_PE_TAIGA = OldConfiguredFeatures.register(
        "trees_pe_taiga",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(PINE_CHECKED, 0.33333334f)), SPRUCE_CHECKED)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_PE_SPARSE = OldConfiguredFeatures.register(
        "trees_pe_sparse",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(), OAK_CHECKED)
    );
    
    // PE Trees w/ bees
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_PE_FOREST_BEES = OldConfiguredFeatures.register(
        "trees_pe_forest_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(BIRCH_BEES_0002, 0.2f)), OAK_BEES_0002)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_PE_RAINFOREST_BEES = OldConfiguredFeatures.register(
        "trees_pe_rainforest_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(), OAK_BEES_0002)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_PE_SEASONAL_FOREST_BEES = OldConfiguredFeatures.register(
        "trees_pe_seasonal_forest_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(), OAK_BEES_0002)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_PE_SPARSE_BEES = OldConfiguredFeatures.register(
        "trees_pe_sparse_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(), OAK_BEES_0002)
    );
    
    // Indev Trees w/ bees
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_INDEV = OldConfiguredFeatures.register(
        "trees_indev",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OAK_CHECKED, 0.1f)), OAK_CHECKED)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_INDEV_WOODS = OldConfiguredFeatures.register(
        "trees_indev_woods",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OAK_CHECKED, 0.1f)), OAK_CHECKED)
    );
    
    // Indev Trees w/ bees
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_INDEV_BEES = OldConfiguredFeatures.register(
        "trees_indev_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OAK_BEES_0002, 0.1f)), OAK_BEES_0002)
    );
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> TREES_INDEV_WOODS_BEES = OldConfiguredFeatures.register(
        "trees_indev_woods_bees",
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfig(List.of(withChance(OAK_BEES_0002, 0.1f)), OAK_BEES_0002)
    );
}
