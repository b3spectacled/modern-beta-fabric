package mod.bespectacled.modernbeta.world.feature.configured;

import java.util.List;

import mod.bespectacled.modernbeta.util.BlockStates;
import mod.bespectacled.modernbeta.world.feature.placed.ModernBetaTreePlacedFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.collection.DataPool;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
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
    public static final RegistryKey<ConfiguredFeature<?, ?>> MUSHROOM_HELL = ModernBetaConfiguredFeatures.of("mushroom_hell");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_DANDELION = ModernBetaConfiguredFeatures.of("patch_dandelion");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_POPPY = ModernBetaConfiguredFeatures.of("patch_poppy");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_DANDELION_INFDEV_227 = ModernBetaConfiguredFeatures.of("patch_dandelion_infdev_227");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_GRASS = ModernBetaConfiguredFeatures.of("patch_grass");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_GRASS_LUSH = ModernBetaConfiguredFeatures.of("patch_grass_lush");

    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_ALPHA = ModernBetaConfiguredFeatures.of("trees_alpha");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_INFDEV_611 = ModernBetaConfiguredFeatures.of("trees_infdev_611");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_INFDEV_420 = ModernBetaConfiguredFeatures.of("trees_infdev_420");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_INFDEV_415 = ModernBetaConfiguredFeatures.of("trees_infdev_415");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_INFDEV_227 = ModernBetaConfiguredFeatures.of("trees_infdev_227");
    
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_ALPHA_BEES = ModernBetaConfiguredFeatures.of("trees_alpha_bees");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_INFDEV_611_BEES = ModernBetaConfiguredFeatures.of("trees_infdev_611_bees");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_INFDEV_420_BEES = ModernBetaConfiguredFeatures.of("trees_infdev_420_bees");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_INFDEV_415_BEES = ModernBetaConfiguredFeatures.of("trees_infdev_415_bees");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_INFDEV_227_BEES = ModernBetaConfiguredFeatures.of("trees_infdev_227_bees");

    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_BETA_FOREST = ModernBetaConfiguredFeatures.of("trees_beta_forest");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_BETA_RAINFOREST = ModernBetaConfiguredFeatures.of("trees_beta_rainforest");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_BETA_SEASONAL_FOREST = ModernBetaConfiguredFeatures.of("trees_beta_seasonal_forest");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_BETA_SPARSE = ModernBetaConfiguredFeatures.of("trees_beta_sparse");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_BETA_TAIGA = ModernBetaConfiguredFeatures.of("trees_beta_taiga");

    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_BETA_FOREST_BEES = ModernBetaConfiguredFeatures.of("trees_beta_forest_bees");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_BETA_RAINFOREST_BEES = ModernBetaConfiguredFeatures.of("trees_beta_rainforest_bees");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_BETA_SEASONAL_FOREST_BEES = ModernBetaConfiguredFeatures.of("trees_beta_seasonal_forest_bees");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_BETA_SPARSE_BEES = ModernBetaConfiguredFeatures.of("trees_beta_sparse_bees");
    
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_PE_FOREST = ModernBetaConfiguredFeatures.of("trees_pe_forest");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_PE_RAINFOREST = ModernBetaConfiguredFeatures.of("trees_pe_rainforest");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_PE_SEASONAL_FOREST = ModernBetaConfiguredFeatures.of("trees_pe_seasonal_forest");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_PE_SPARSE = ModernBetaConfiguredFeatures.of("trees_pe_sparse");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_PE_TAIGA = ModernBetaConfiguredFeatures.of("trees_pe_taiga");
    
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_PE_FOREST_BEES = ModernBetaConfiguredFeatures.of("trees_pe_forest_bees");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_PE_RAINFOREST_BEES = ModernBetaConfiguredFeatures.of("trees_pe_rainforest_bees");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_PE_SEASONAL_FOREST_BEES = ModernBetaConfiguredFeatures.of("trees_pe_seasonal_forest_bees");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_PE_SPARSE_BEES = ModernBetaConfiguredFeatures.of("trees_pe_sparse_bees");
    
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_INDEV = ModernBetaConfiguredFeatures.of("trees_indev");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_INDEV_WOODS = ModernBetaConfiguredFeatures.of("trees_indev_woods");
    
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_INDEV_BEES = ModernBetaConfiguredFeatures.of("trees_indev_bees");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_INDEV_WOODS_BEES = ModernBetaConfiguredFeatures.of("trees_indev_woods_bees");
    
    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> featureRegisterable) {
        RegistryEntryLookup<PlacedFeature> registryPlaced = featureRegisterable.getRegistryLookup(RegistryKeys.PLACED_FEATURE);
        
        ConfiguredFeatures.register(featureRegisterable, MUSHROOM_HELL, Feature.FLOWER, ModernBetaRandomPatchConfigs.MUSHROOM_HELL);
        ConfiguredFeatures.register(featureRegisterable, PATCH_DANDELION, Feature.FLOWER, ModernBetaRandomPatchConfigs.DANDELION_CONFIG);
        ConfiguredFeatures.register(featureRegisterable, PATCH_POPPY, Feature.FLOWER, ModernBetaRandomPatchConfigs.POPPY_CONFIG);
        ConfiguredFeatures.register(featureRegisterable, PATCH_DANDELION_INFDEV_227, Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(BlockStateProvider.of(Blocks.DANDELION)));
        ConfiguredFeatures.register(featureRegisterable, PATCH_GRASS, Feature.RANDOM_PATCH, ModernBetaRandomPatchConfigs.GRASS_CONFIG);
        ConfiguredFeatures.register(featureRegisterable, PATCH_GRASS_LUSH, Feature.RANDOM_PATCH, ModernBetaRandomPatchConfigs.LUSH_GRASS_CONFIG);
        
        ConfiguredFeatures.register(featureRegisterable, TREES_ALPHA, Feature.RANDOM_SELECTOR, createDefaultRandomTreeConfig(registryPlaced, false));
        ConfiguredFeatures.register(featureRegisterable, TREES_INFDEV_611, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, false));
        ConfiguredFeatures.register(featureRegisterable, TREES_INFDEV_420, Feature.RANDOM_SELECTOR, createDefaultRandomTreeConfig(registryPlaced, false));
        ConfiguredFeatures.register(featureRegisterable, TREES_INFDEV_415, Feature.RANDOM_SELECTOR, createDefaultRandomTreeConfig(registryPlaced, false));
        ConfiguredFeatures.register(featureRegisterable, TREES_INFDEV_227, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, false));
        
        ConfiguredFeatures.register(featureRegisterable, TREES_ALPHA_BEES, Feature.RANDOM_SELECTOR, createDefaultRandomTreeConfig(registryPlaced, true));
        ConfiguredFeatures.register(featureRegisterable, TREES_INFDEV_611_BEES, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, true));
        ConfiguredFeatures.register(featureRegisterable, TREES_INFDEV_420_BEES, Feature.RANDOM_SELECTOR, createDefaultRandomTreeConfig(registryPlaced, true));
        ConfiguredFeatures.register(featureRegisterable, TREES_INFDEV_415_BEES, Feature.RANDOM_SELECTOR, createDefaultRandomTreeConfig(registryPlaced, true));
        ConfiguredFeatures.register(featureRegisterable, TREES_INFDEV_227_BEES, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, true));
        
        ConfiguredFeatures.register(featureRegisterable, TREES_BETA_FOREST, Feature.RANDOM_SELECTOR, createForestRandomTreeConfig(registryPlaced, false));
        ConfiguredFeatures.register(featureRegisterable, TREES_BETA_RAINFOREST, Feature.RANDOM_SELECTOR, createRainforestRandomTreeConfig(registryPlaced, false));
        ConfiguredFeatures.register(featureRegisterable, TREES_BETA_SEASONAL_FOREST, Feature.RANDOM_SELECTOR, createSeasonalForestRandomTreeConfig(registryPlaced, false));
        ConfiguredFeatures.register(featureRegisterable, TREES_BETA_SPARSE, Feature.RANDOM_SELECTOR, createDefaultRandomTreeConfig(registryPlaced, false));
        ConfiguredFeatures.register(featureRegisterable, TREES_BETA_TAIGA, Feature.RANDOM_SELECTOR, createTaigaRandomTreeConfig(registryPlaced));
        
        ConfiguredFeatures.register(featureRegisterable, TREES_BETA_FOREST_BEES, Feature.RANDOM_SELECTOR, createForestRandomTreeConfig(registryPlaced, true));
        ConfiguredFeatures.register(featureRegisterable, TREES_BETA_RAINFOREST_BEES, Feature.RANDOM_SELECTOR, createRainforestRandomTreeConfig(registryPlaced, true));
        ConfiguredFeatures.register(featureRegisterable, TREES_BETA_SEASONAL_FOREST_BEES, Feature.RANDOM_SELECTOR, createSeasonalForestRandomTreeConfig(registryPlaced, true));
        ConfiguredFeatures.register(featureRegisterable, TREES_BETA_SPARSE_BEES, Feature.RANDOM_SELECTOR, createDefaultRandomTreeConfig(registryPlaced, true));
        
        ConfiguredFeatures.register(featureRegisterable, TREES_PE_FOREST, Feature.RANDOM_SELECTOR, createPEForestRandomTreeConfig(registryPlaced, false));
        ConfiguredFeatures.register(featureRegisterable, TREES_PE_RAINFOREST, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, false));
        ConfiguredFeatures.register(featureRegisterable, TREES_PE_SEASONAL_FOREST, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, false));
        ConfiguredFeatures.register(featureRegisterable, TREES_PE_SPARSE, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, false));
        ConfiguredFeatures.register(featureRegisterable, TREES_PE_TAIGA, Feature.RANDOM_SELECTOR, createTaigaRandomTreeConfig(registryPlaced));
        
        ConfiguredFeatures.register(featureRegisterable, TREES_PE_FOREST_BEES, Feature.RANDOM_SELECTOR, createPEForestRandomTreeConfig(registryPlaced, true));
        ConfiguredFeatures.register(featureRegisterable, TREES_PE_RAINFOREST_BEES, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, true));
        ConfiguredFeatures.register(featureRegisterable, TREES_PE_SEASONAL_FOREST_BEES, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, true));
        ConfiguredFeatures.register(featureRegisterable, TREES_PE_SPARSE_BEES, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, true));
        
        ConfiguredFeatures.register(featureRegisterable, TREES_INDEV, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, false));
        ConfiguredFeatures.register(featureRegisterable, TREES_INDEV_WOODS, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, false));
        
        ConfiguredFeatures.register(featureRegisterable, TREES_INDEV_BEES, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, true));
        ConfiguredFeatures.register(featureRegisterable, TREES_INDEV_WOODS_BEES, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, true));
    }

    private static RandomFeatureConfig createOakTreeConfig(RegistryEntryLookup<PlacedFeature> registryPlaced, boolean bees) {
        RegistryEntry.Reference<PlacedFeature> oakChecked = registryPlaced.getOrThrow(TreePlacedFeatures.OAK_CHECKED);
        RegistryEntry.Reference<PlacedFeature> oakBees = registryPlaced.getOrThrow(TreePlacedFeatures.OAK_BEES_0002);
        
        return new RandomFeatureConfig(List.of(), bees ? oakBees : oakChecked);
    }
    
    private static RandomFeatureConfig createDefaultRandomTreeConfig(RegistryEntryLookup<PlacedFeature> registryPlaced, boolean bees) {
        RegistryEntry.Reference<PlacedFeature> fancyOak = registryPlaced.getOrThrow(ModernBetaTreePlacedFeatures.FANCY_OAK);
        RegistryEntry.Reference<PlacedFeature> oak = registryPlaced.getOrThrow(TreePlacedFeatures.OAK_CHECKED);
        RegistryEntry.Reference<PlacedFeature> oakBees = registryPlaced.getOrThrow(TreePlacedFeatures.OAK_BEES_0002);
        
        return new RandomFeatureConfig(List.of(withChance(fancyOak, 0.1f)), bees ? oakBees : oak);
    }
    
    private static RandomFeatureConfig createForestRandomTreeConfig(RegistryEntryLookup<PlacedFeature> registryPlaced, boolean bees) {
        RegistryEntry.Reference<PlacedFeature> fancyOak = registryPlaced.getOrThrow(ModernBetaTreePlacedFeatures.FANCY_OAK);
        RegistryEntry.Reference<PlacedFeature> oak = registryPlaced.getOrThrow(TreePlacedFeatures.OAK_CHECKED);
        RegistryEntry.Reference<PlacedFeature> oakBees = registryPlaced.getOrThrow(TreePlacedFeatures.OAK_BEES_0002);
        
        RegistryEntry.Reference<PlacedFeature> birch = registryPlaced.getOrThrow(TreePlacedFeatures.BIRCH_CHECKED);
        RegistryEntry.Reference<PlacedFeature> birchBees = registryPlaced.getOrThrow(TreePlacedFeatures.BIRCH_BEES_0002);
        
        return new RandomFeatureConfig(List.of(withChance(bees ? birchBees : birch, 0.2f), withChance(fancyOak, 0.33333334f)), bees ? oakBees : oak);
    }
    
    private static RandomFeatureConfig createRainforestRandomTreeConfig(RegistryEntryLookup<PlacedFeature> registryPlaced, boolean bees) {
        RegistryEntry.Reference<PlacedFeature> fancyOak = registryPlaced.getOrThrow(ModernBetaTreePlacedFeatures.FANCY_OAK);
        RegistryEntry.Reference<PlacedFeature> oak = registryPlaced.getOrThrow(TreePlacedFeatures.OAK_CHECKED);
        RegistryEntry.Reference<PlacedFeature> oakBees = registryPlaced.getOrThrow(TreePlacedFeatures.OAK_BEES_0002);
        
        return new RandomFeatureConfig(List.of(withChance(fancyOak, 0.33333334f)), bees ? oakBees : oak);
    }
    
    private static RandomFeatureConfig createSeasonalForestRandomTreeConfig(RegistryEntryLookup<PlacedFeature> registryPlaced, boolean bees) {
        RegistryEntry.Reference<PlacedFeature> fancyOak = registryPlaced.getOrThrow(ModernBetaTreePlacedFeatures.FANCY_OAK);
        RegistryEntry.Reference<PlacedFeature> oak = registryPlaced.getOrThrow(TreePlacedFeatures.OAK_CHECKED);
        RegistryEntry.Reference<PlacedFeature> oakBees = registryPlaced.getOrThrow(TreePlacedFeatures.OAK_BEES_0002);
        
        return new RandomFeatureConfig(List.of(withChance(bees ? oakBees : oak, 0.1f)), fancyOak);
    }

    private static RandomFeatureConfig createTaigaRandomTreeConfig(RegistryEntryLookup<PlacedFeature> registryPlaced) {
        RegistryEntry.Reference<PlacedFeature> pine = registryPlaced.getOrThrow(TreePlacedFeatures.PINE_CHECKED);
        RegistryEntry.Reference<PlacedFeature> spruce = registryPlaced.getOrThrow(TreePlacedFeatures.SPRUCE_CHECKED);
        
        return new RandomFeatureConfig(List.of(withChance(pine, 0.33333334f)), spruce);
    }
    
    private static RandomFeatureConfig createPEForestRandomTreeConfig(RegistryEntryLookup<PlacedFeature> registryPlaced, boolean bees) {
        RegistryEntry.Reference<PlacedFeature> oak = registryPlaced.getOrThrow(TreePlacedFeatures.OAK_CHECKED);
        RegistryEntry.Reference<PlacedFeature> oakBees = registryPlaced.getOrThrow(TreePlacedFeatures.OAK_BEES_0002);
        
        RegistryEntry.Reference<PlacedFeature> birch = registryPlaced.getOrThrow(TreePlacedFeatures.BIRCH_CHECKED);
        RegistryEntry.Reference<PlacedFeature> birchBees = registryPlaced.getOrThrow(TreePlacedFeatures.BIRCH_BEES_0002);
        
        return new RandomFeatureConfig(List.of(withChance(bees ? birchBees : birch, 0.2f)), bees ? oak : oakBees);
    }
    
    private static RandomFeatureEntry withChance(RegistryEntry<PlacedFeature> feature, float chance) {
        return new RandomFeatureEntry(feature, chance);
    }
    
    private static final class ModernBetaRandomPatchConfigs {
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
        
        private static DataPool.Builder<BlockState> pool() {
            return DataPool.<BlockState>builder();
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
}
