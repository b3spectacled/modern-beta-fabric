package com.bespectacled.modernbeta.world.biome;

import com.bespectacled.modernbeta.world.carver.OldCarvers;
import com.bespectacled.modernbeta.world.feature.OldConfiguredFeatures;

import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.carver.ConfiguredCarvers;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;

public class OldBiomeFeatures {
    public static void addDefaultFeatures(GenerationSettings.Builder genSettings, boolean isOcean, boolean addLakes, boolean addSprings) {
        if (addLakes) DefaultBiomeFeatures.addDefaultLakes(genSettings);
        DefaultBiomeFeatures.addDungeons(genSettings);
        DefaultBiomeFeatures.addDefaultOres(genSettings);
        DefaultBiomeFeatures.addDefaultMushrooms(genSettings);
        if (addSprings) DefaultBiomeFeatures.addSprings(genSettings);
        DefaultBiomeFeatures.addAmethystGeodes(genSettings);
    }
    
    public static void addOres(GenerationSettings.Builder genSettings) {
        genSettings.feature(Feature.UNDERGROUND_ORES, OldConfiguredFeatures.ORE_EMERALD_Y95);
    }

    public static void addMineables(GenerationSettings.Builder genSettings, boolean addAlternateStones, boolean addNewMineables) {
        genSettings.feature(Feature.UNDERGROUND_ORES, ConfiguredFeatures.ORE_DIRT);
        genSettings.feature(Feature.UNDERGROUND_ORES, ConfiguredFeatures.ORE_GRAVEL);
        genSettings.feature(Feature.UNDERGROUND_ORES, OldConfiguredFeatures.ORE_CLAY);
        
        if (addAlternateStones) {
            genSettings.feature(GenerationStep.Feature.UNDERGROUND_ORES, ConfiguredFeatures.ORE_GRANITE_LOWER);
            genSettings.feature(GenerationStep.Feature.UNDERGROUND_ORES, ConfiguredFeatures.ORE_DIORITE_LOWER);
            genSettings.feature(GenerationStep.Feature.UNDERGROUND_ORES, ConfiguredFeatures.ORE_ANDESITE_LOWER);
        }
        
        if (addNewMineables) {
            genSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.GLOW_LICHEN);
            genSettings.feature(GenerationStep.Feature.UNDERGROUND_ORES, ConfiguredFeatures.ORE_TUFF);
        }
    }

    public static void addCarvers(GenerationSettings.Builder genSettings, boolean addCanyons) {
        genSettings.carver(GenerationStep.Carver.AIR, OldCarvers.CONF_OLD_BETA_CAVE_CARVER);
        genSettings.carver(GenerationStep.Carver.AIR, OldCarvers.CONF_OLD_BETA_CAVE_CARVER_DEEP);
        
        if (addCanyons) {
            genSettings.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.CANYON);
        }
    }

    public static void addOceanCarvers(GenerationSettings.Builder genSettings) {
        genSettings.carver(GenerationStep.Carver.AIR, OldCarvers.CONF_OLD_BETA_CAVE_CARVER);
        genSettings.carver(GenerationStep.Carver.AIR, OldCarvers.CONF_OLD_BETA_CAVE_CARVER_DEEP);
        genSettings.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.CANYON);
    }

    public static void addVegetalPatches(GenerationSettings.Builder genSettings) {
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_SUGAR_CANE);
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_PUMPKIN);
    }

    public static void addBetaFrozenTopLayer(GenerationSettings.Builder genSettings) {
        genSettings.feature(Feature.TOP_LAYER_MODIFICATION, OldConfiguredFeatures.BETA_FREEZE_TOP_LAYER);
    }
}
