package com.bespectacled.modernbeta.feature;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.decorator.BetaDecorator;
import com.bespectacled.modernbeta.decorator.CountNoiseDecoratorConfig;
import com.bespectacled.modernbeta.decorator.CountBetaNoiseDecorator;
import com.google.common.collect.ImmutableList;

import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.DefaultBiomeCreator;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.decorator.ConfiguredDecorator;
import net.minecraft.world.gen.decorator.CountExtraDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorConfig;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.ConfiguredFeatures.Configs;
import net.minecraft.world.gen.feature.ConfiguredFeatures.Decorators;
import net.minecraft.world.gen.feature.ConfiguredFeatures.States;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.RandomFeatureConfig;
import net.minecraft.world.gen.feature.RandomFeatureEntry;
import net.minecraft.world.gen.feature.SimpleRandomFeatureConfig;

public class BetaFeature {
    public static final ImmutableList<Identifier> CONFIG_FEATURES = ImmutableList.of(
            new Identifier(ModernBeta.ID, "ore_clay"), 
            new Identifier(ModernBeta.ID, "patch_grass_rainforest_10"),
            new Identifier(ModernBeta.ID, "patch_grass_plains_10"),
            new Identifier(ModernBeta.ID, "patch_grass_taiga_1"),
            new Identifier(ModernBeta.ID, "trees_beta_forest_bees"),
            new Identifier(ModernBeta.ID, "trees_beta_rainforest_bees"),
            new Identifier(ModernBeta.ID, "trees_beta_seasonal_forest_bees"),
            
            new Identifier(ModernBeta.ID, "trees_beta_forest"), 
            new Identifier(ModernBeta.ID, "trees_beta_rainforest"),
            new Identifier(ModernBeta.ID, "trees_beta_seasonal_forest"),
            new Identifier(ModernBeta.ID, "trees_beta_taiga"), 
            
            new Identifier(ModernBeta.ID, "patch_dandelion_2"),
            new Identifier(ModernBeta.ID, "patch_dandelion_3"),
            new Identifier(ModernBeta.ID, "patch_dandelion_4"),
            new Identifier(ModernBeta.ID, "patch_poppy"), 
            
            new Identifier(ModernBeta.ID, "ore_emerald_y95"),
            new Identifier(ModernBeta.ID, "trees_alpha_bees"),
            new Identifier(ModernBeta.ID, "trees_alpha"), 
            new Identifier(ModernBeta.ID, "patch_grass_alpha_2"),
            new Identifier(ModernBeta.ID, "patch_cactus_alpha"),
            new Identifier(ModernBeta.ID, "trees_indev_bees"));

    public static final BetaFreezeTopLayerFeature BETA_FREEZE_TOP_LAYER = new BetaFreezeTopLayerFeature(
            DefaultFeatureConfig.CODEC);
    private static final ConfiguredFeature<?, ?> BETA_FREEZE_TOP_LAYER_CONF = BETA_FREEZE_TOP_LAYER
            .configure(FeatureConfig.DEFAULT);

    public static ConfiguredFeature<?, ?> getFeature(String name) {
        return BuiltinRegistries.CONFIGURED_FEATURE.get(new Identifier(ModernBeta.ID, name));
    }

    public static void reserveConfiguredFeatureIDs() {
        for (Identifier i : CONFIG_FEATURES) {
            Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, i,
                    Feature.FLOWER.configure(Configs.DEFAULT_FLOWER_CONFIG));
        }

        ModernBeta.LOGGER.log(Level.INFO, "Reserved feature IDs.");
    }

    public static void register() {
        Registry.register(Registry.FEATURE, new Identifier(ModernBeta.ID, "beta_freeze_top_layer"),
                BETA_FREEZE_TOP_LAYER);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(ModernBeta.ID, "beta_freeze_top_layer"),
                BETA_FREEZE_TOP_LAYER_CONF);

        ModernBeta.LOGGER.log(Level.INFO, "Registered features.");
    }
}
