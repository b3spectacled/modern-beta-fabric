package com.bespectacled.modernbeta.world.feature;

import java.util.HashMap;
import java.util.Map;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.world.decorator.CountOldNoiseDecoratorConfig;
import com.bespectacled.modernbeta.world.decorator.OldDecorators;
import com.google.common.collect.ImmutableList;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.ConfiguredDecorator;
import net.minecraft.world.gen.decorator.CountExtraDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.RandomFeatureConfig;
import net.minecraft.world.gen.feature.RandomFeatureEntry;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.SimpleBlockFeatureConfig;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

public class OldConfiguredFeatures {
    private static final class BetaRandomPatchConfigs {
        private static final int TRIES = 64;
        
        public static final RandomPatchFeatureConfig GRASS_CONFIG;
        public static final RandomPatchFeatureConfig LUSH_GRASS_CONFIG;
        
        static {
            // # of tries in Beta equivalent is 128, but here it seems to generate too much grass,
            // so keep # of tries at 64 for now.
            GRASS_CONFIG = new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(BlockStates.GRASS), SimpleBlockPlacer.INSTANCE).tries(TRIES).build();
            LUSH_GRASS_CONFIG = new RandomPatchFeatureConfig.Builder(new WeightedBlockStateProvider(method_35926().add(BlockStates.GRASS, 1).add(BlockStates.FERN, 3)), SimpleBlockPlacer.INSTANCE).tries(TRIES).build();
        }
    }
    
    private static final Map<Identifier, ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = new HashMap<Identifier, ConfiguredFeature<?, ?>>();
    
    // Heightmap World Surface Decorator
    private static final ConfiguredDecorator<?> OLD_HEIGHTMAP = Decorator.RANGE.configure(new RangeDecoratorConfig(UniformHeightProvider.create(YOffset.getBottom(), YOffset.getTop()))).spreadHorizontally();
    
    // Custom Features
    public static final ConfiguredFeature<?, ?> BETA_FREEZE_TOP_LAYER = register("beta_freeze_top_layer", OldFeatures.BETA_FREEZE_TOP_LAYER.configure(FeatureConfig.DEFAULT));
    public static final ConfiguredFeature<?, ?> OLD_FANCY_OAK = register("old_fancy_oak", OldFeatures.OLD_FANCY_OAK.configure(FeatureConfig.DEFAULT));
    
    // Ores
    public static final ConfiguredFeature<?, ?> ORE_CLAY = register("ore_clay", ((OldFeatures.ORE_CLAY.configure(new OreFeatureConfig(new BlockMatchRuleTest(Blocks.SAND), Blocks.CLAY.getDefaultState(), 33)).uniformRange(YOffset.fixed(0), YOffset.fixed(127))).spreadHorizontally().repeat(10)));
    public static final ConfiguredFeature<?, ?> ORE_EMERALD_Y95 = register("ore_emerald_y95", Feature.SCATTERED_ORE.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, Blocks.EMERALD_ORE.getDefaultState(), 8)).decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(UniformHeightProvider.create(YOffset.fixed(95), YOffset.fixed(256))))).spreadHorizontally().repeat(11));
    
    // Shrubs
    public static final ConfiguredFeature<?, ?> PATCH_CACTUS_ALPHA = register("patch_cactus", ConfiguredFeatures.PATCH_CACTUS.decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(1));
    public static final ConfiguredFeature<?, ?> PATCH_CACTUS_PE = register("patch_cactus_pe", ConfiguredFeatures.PATCH_CACTUS.decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(5));
    public static final ConfiguredFeature<?, ?> MUSHROOM_HELL = register("mushroom_hell", Feature.FLOWER.configure(new RandomPatchFeatureConfig.Builder(new WeightedBlockStateProvider(method_35926().add(Blocks.BROWN_MUSHROOM.getDefaultState(), 2).add(Blocks.RED_MUSHROOM.getDefaultState(), 1)), SimpleBlockPlacer.INSTANCE).tries(64).build()).decorate(ConfiguredFeatures.Decorators.SPREAD_32_ABOVE).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).repeat(2));

    // Flowers
    public static final ConfiguredFeature<?, ?> PATCH_DANDELION_2 = register("patch_dandelion_2", Feature.FLOWER.configure(new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.DANDELION.getDefaultState()), SimpleBlockPlacer.INSTANCE).tries(64).build()).decorate(ConfiguredFeatures.Decorators.SPREAD_32_ABOVE).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).repeat(2));
    public static final ConfiguredFeature<?, ?> PATCH_DANDELION_3 = register("patch_dandelion_3", Feature.FLOWER.configure(new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.DANDELION.getDefaultState()), SimpleBlockPlacer.INSTANCE).tries(64).build()).decorate(ConfiguredFeatures.Decorators.SPREAD_32_ABOVE).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).repeat(3));
    public static final ConfiguredFeature<?, ?> PATCH_DANDELION_4 = register("patch_dandelion_4", Feature.FLOWER.configure(new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.DANDELION.getDefaultState()), SimpleBlockPlacer.INSTANCE).tries(64).build()).decorate(ConfiguredFeatures.Decorators.SPREAD_32_ABOVE).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).repeat(4));
    public static final ConfiguredFeature<?, ?> PATCH_DANDELION = register("patch_dandelion", Feature.FLOWER.configure(new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.DANDELION.getDefaultState()), SimpleBlockPlacer.INSTANCE).tries(64).build()).decorate(ConfiguredFeatures.Decorators.SPREAD_32_ABOVE).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, 0.5f, 1)))); 
    public static final ConfiguredFeature<?, ?> PATCH_POPPY = register("patch_poppy", Feature.FLOWER.configure(new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.POPPY.getDefaultState()), SimpleBlockPlacer.INSTANCE).tries(64).build()).decorate(ConfiguredFeatures.Decorators.SPREAD_32_ABOVE).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, 0.5f, 1))));
    public static final ConfiguredFeature<?, ?> FLOWER_PARADISE = register("flower_paradise", Feature.FLOWER.configure(ConfiguredFeatures.Configs.DEFAULT_FLOWER_CONFIG).decorate(ConfiguredFeatures.Decorators.SPREAD_32_ABOVE).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).repeat(20));
    
    // Grass
    public static final ConfiguredFeature<?, ?> PATCH_GRASS_PLAINS_10 = register("patch_grass_plains_10", Feature.RANDOM_PATCH.configure(BetaRandomPatchConfigs.GRASS_CONFIG).decorate(OLD_HEIGHTMAP).repeat(10));
    public static final ConfiguredFeature<?, ?> PATCH_GRASS_TAIGA_1 = register("patch_grass_taiga_1", Feature.RANDOM_PATCH.configure(BetaRandomPatchConfigs.GRASS_CONFIG).decorate(OLD_HEIGHTMAP).repeat(1));
    public static final ConfiguredFeature<?, ?> PATCH_GRASS_RAINFOREST_10 = register("patch_grass_rainforest_10", Feature.RANDOM_PATCH.configure(BetaRandomPatchConfigs.LUSH_GRASS_CONFIG).decorate(OLD_HEIGHTMAP).repeat(10));
    public static final ConfiguredFeature<?, ?> PATCH_GRASS_ALPHA_2 = register("patch_grass_alpha_2", Feature.RANDOM_PATCH.configure(ConfiguredFeatures.Configs.GRASS_CONFIG).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).repeat(1).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, 0.1f, 1))));
    
    // Classic Trees
    public static final ConfiguredFeature<?, ?> TREES_ALPHA = register("trees_alpha", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(OLD_FANCY_OAK.withChance(0.1f)), ConfiguredFeatures.OAK)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(OldDecorators.COUNT_ALPHA_NOISE.configure(new CountOldNoiseDecoratorConfig(0, 0.1f, 1))));
    public static final ConfiguredFeature<?, ?> TREES_INFDEV_611 = register("trees_infdev_611", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(), ConfiguredFeatures.OAK)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(OldDecorators.COUNT_INFDEV_611_NOISE.configure(new CountOldNoiseDecoratorConfig(0, 0.1f, 1))));
    public static final ConfiguredFeature<?, ?> TREES_INFDEV_420 = register("trees_infdev_420", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(ConfiguredFeatures.OAK.withChance(0.1f)), OLD_FANCY_OAK)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(OldDecorators.COUNT_INFDEV_420_NOISE.configure(new CountOldNoiseDecoratorConfig(0, 0.01f, 1))));
    public static final ConfiguredFeature<?, ?> TREES_INFDEV_415 = register("trees_infdev_415", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(ConfiguredFeatures.OAK.withChance(0.1f)), OLD_FANCY_OAK)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(OldDecorators.COUNT_INFDEV_415_NOISE.configure(new CountOldNoiseDecoratorConfig(0, 0, 0))));
    public static final ConfiguredFeature<?, ?> TREES_INFDEV_227 = register("trees_infdev_227", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(ConfiguredFeatures.OAK.withChance(0.1f)), ConfiguredFeatures.OAK)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, 0.1f, 1))));
    
    // Classic Trees w/ bees
    public static final ConfiguredFeature<?, ?> TREES_ALPHA_BEES = register("trees_alpha_bees", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(OLD_FANCY_OAK.withChance(0.1f)), ConfiguredFeatures.OAK_BEES_0002)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(OldDecorators.COUNT_ALPHA_NOISE.configure(new CountOldNoiseDecoratorConfig(0, 0.1f, 1))));
    public static final ConfiguredFeature<?, ?> TREES_INFDEV_611_BEES = register("trees_infdev_611_bees", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(ConfiguredFeatures.OAK_BEES_0002.withChance(0.1f)), ConfiguredFeatures.OAK)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(OldDecorators.COUNT_INFDEV_611_NOISE.configure(new CountOldNoiseDecoratorConfig(0, 0.1f, 1))));
    public static final ConfiguredFeature<?, ?> TREES_INFDEV_420_BEES = register("trees_infdev_420_bees", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(ConfiguredFeatures.OAK_BEES_0002.withChance(0.1f)), OLD_FANCY_OAK)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(OldDecorators.COUNT_INFDEV_420_NOISE.configure(new CountOldNoiseDecoratorConfig(0, 0.01f, 1))));
    public static final ConfiguredFeature<?, ?> TREES_INFDEV_415_BEES = register("trees_infdev_415_bees", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(ConfiguredFeatures.OAK_BEES_0002.withChance(0.1f)), OLD_FANCY_OAK)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(OldDecorators.COUNT_INFDEV_415_NOISE.configure(new CountOldNoiseDecoratorConfig(0, 0, 0))));
    public static final ConfiguredFeature<?, ?> TREES_INFDEV_227_BEES = register("trees_infdev_227_bees", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(ConfiguredFeatures.OAK_BEES_0002.withChance(0.1f)), ConfiguredFeatures.OAK_BEES_0002)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, 0.1f, 1))));

    // Beta Trees
    public static final ConfiguredFeature<?, ?> TREES_BETA_FOREST = register("trees_beta_forest", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(ConfiguredFeatures.BIRCH.withChance(0.2f), OLD_FANCY_OAK.withChance(0.33333334f)), ConfiguredFeatures.OAK)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(OldDecorators.COUNT_BETA_NOISE.configure(new CountOldNoiseDecoratorConfig(5, 0.1f, 1))));
    public static final ConfiguredFeature<?, ?> TREES_BETA_RAINFOREST = register("trees_beta_rainforest", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(OLD_FANCY_OAK.withChance(0.33333334f)), ConfiguredFeatures.OAK)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(OldDecorators.COUNT_BETA_NOISE.configure(new CountOldNoiseDecoratorConfig(5, 0.1f, 1))));
    public static final ConfiguredFeature<?, ?> TREES_BETA_SEASONAL_FOREST = register("trees_beta_seasonal_forest", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(OLD_FANCY_OAK.withChance(0.1f)), ConfiguredFeatures.OAK)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(OldDecorators.COUNT_BETA_NOISE.configure(new CountOldNoiseDecoratorConfig(2, 0.1f, 1))));
    public static final ConfiguredFeature<?, ?> TREES_BETA_TAIGA = register("trees_beta_taiga", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(ConfiguredFeatures.PINE.withChance(0.33333334f)), ConfiguredFeatures.SPRUCE)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(OldDecorators.COUNT_BETA_NOISE.configure(new CountOldNoiseDecoratorConfig(5, 0.1f, 1))));
    public static final ConfiguredFeature<?, ?> TREES_BETA_SPARSE = register("trees_beta_sparse", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(OLD_FANCY_OAK.withChance(0.1f)), ConfiguredFeatures.OAK)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, 0.1f, 1))));
    
    // Beta Trees w/ bees
    public static final ConfiguredFeature<?, ?> TREES_BETA_FOREST_BEES = register("trees_beta_forest_bees", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(ConfiguredFeatures.BIRCH_BEES_0002.withChance(0.2f), OLD_FANCY_OAK.withChance(0.33333334f)), ConfiguredFeatures.OAK_BEES_0002)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(OldDecorators.COUNT_BETA_NOISE.configure(new CountOldNoiseDecoratorConfig(5, 0.1f, 1))));
    public static final ConfiguredFeature<?, ?> TREES_BETA_RAINFOREST_BEES = register("trees_beta_rainforest_bees", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(OLD_FANCY_OAK.withChance(0.33333334f)), ConfiguredFeatures.OAK_BEES_0002)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(OldDecorators.COUNT_BETA_NOISE.configure(new CountOldNoiseDecoratorConfig(5, 0.1f, 1))));
    public static final ConfiguredFeature<?, ?> TREES_BETA_SEASONAL_FOREST_BEES = register("trees_beta_seasonal_forest_bees", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(OLD_FANCY_OAK.withChance(0.1f)), ConfiguredFeatures.OAK_BEES_0002)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(OldDecorators.COUNT_BETA_NOISE.configure(new CountOldNoiseDecoratorConfig(2, 0.1f, 1))));
    public static final ConfiguredFeature<?, ?> TREES_BETA_SPARSE_BEES = register("trees_beta_sparse_bees", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(OLD_FANCY_OAK.withChance(0.1f)), ConfiguredFeatures.OAK_BEES_0002)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, 0.1f, 1))));
    
    // PE Trees
    public static final ConfiguredFeature<?, ?> TREES_PE_TAIGA = register("trees_pe_taiga", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(ConfiguredFeatures.PINE.withChance(0.33333334f)), ConfiguredFeatures.SPRUCE)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(OldDecorators.COUNT_BETA_NOISE.configure(new CountOldNoiseDecoratorConfig(1, 0.1f, 1))));
    public static final ConfiguredFeature<?, ?> TREES_PE_SPARSE = register("trees_pe_sparse", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(), ConfiguredFeatures.OAK)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, 0.1f, 1))));
    
    // PE Trees w/ bees
    public static final ConfiguredFeature<?, ?> TREES_PE_FOREST_BEES = register("trees_pe_forest_bees", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(ConfiguredFeatures.BIRCH_BEES_0002.withChance(0.2f)), ConfiguredFeatures.OAK_BEES_0002)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(OldDecorators.COUNT_BETA_NOISE.configure(new CountOldNoiseDecoratorConfig(2, 0.1f, 1))));
    public static final ConfiguredFeature<?, ?> TREES_PE_RAINFOREST_BEES = register("trees_pe_rainforest_bees", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(), ConfiguredFeatures.OAK_BEES_0002)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(OldDecorators.COUNT_BETA_NOISE.configure(new CountOldNoiseDecoratorConfig(2, 0.1f, 1))));
    public static final ConfiguredFeature<?, ?> TREES_PE_SEASONAL_FOREST_BEES = register("trees_pe_seasonal_forest_bees", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(), ConfiguredFeatures.OAK_BEES_0002)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(OldDecorators.COUNT_BETA_NOISE.configure(new CountOldNoiseDecoratorConfig(1, 0.1f, 1))));
    public static final ConfiguredFeature<?, ?> TREES_PE_SPARSE_BEES = register("trees_pe_sparse_bees", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(), ConfiguredFeatures.OAK_BEES_0002)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, 0.1f, 1))));
    
    
    // Indev Trees w/ bees
    public static final ConfiguredFeature<?, ?> TREES_INDEV = register("trees_indev", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(ConfiguredFeatures.OAK.withChance(0.1f)), ConfiguredFeatures.OAK)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(5, 0.1f, 1)).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, 0.334f, 1)))));
    public static final ConfiguredFeature<?, ?> TREES_INDEV_WOODS = register("trees_indev_woods", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(ConfiguredFeatures.OAK.withChance(0.1f)), ConfiguredFeatures.OAK)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(30, 0.1f, 1))));
    
    // Indev Trees w/ bees
    public static final ConfiguredFeature<?, ?> TREES_INDEV_BEES = register("trees_indev_bees", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(ConfiguredFeatures.OAK_BEES_0002.withChance(0.1f)), ConfiguredFeatures.OAK_BEES_0002)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(5, 0.1f, 1)).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, 0.334f, 1)))));
    public static final ConfiguredFeature<?, ?> TREES_INDEV_WOODS_BEES = register("trees_indev_woods_bees", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.<RandomFeatureEntry>of(ConfiguredFeatures.OAK_BEES_0002.withChance(0.1f)), ConfiguredFeatures.OAK_BEES_0002)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(30, 0.1f, 1))));
    
    // Infdev 227 Flowers
    public static final ConfiguredFeature<?, ?> FLOWER_INFDEV_227 = register("flower_infdev_227", Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(new SimpleBlockStateProvider(Blocks.DANDELION.getDefaultState()))).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).repeatRandomly(10));
    
    private static <F extends FeatureConfig> ConfiguredFeature<F, ?> register(String id, ConfiguredFeature<F, ?> feature) {
        CONFIGURED_FEATURES.put(ModernBeta.createId(id), feature);
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, ModernBeta.createId(id), feature);
    }
    
    private static DataPool.Builder<BlockState> method_35926() {
        return DataPool.<BlockState>builder();
    }
}
