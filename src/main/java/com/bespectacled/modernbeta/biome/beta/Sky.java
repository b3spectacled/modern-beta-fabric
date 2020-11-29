package com.bespectacled.modernbeta.biome.beta;

import com.bespectacled.modernbeta.carver.BetaCarver;
import com.bespectacled.modernbeta.feature.BetaConfiguredFeature;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.SpawnSettings.SpawnEntry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.carver.ConfiguredCarvers;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;

public class Sky {
    public static final Biome BIOME = create();
    
    private static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addFarmAnimals(spawnSettings);
        DefaultBiomeFeatures.addBatsAndMonsters(spawnSettings);
        
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.WOLF, 8, 4, 4));
        
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        genSettings.surfaceBuilder(ConfiguredSurfaceBuilders.GRASS);
        
        DefaultBiomeFeatures.addDefaultUndergroundStructures(genSettings);
        DefaultBiomeFeatures.addDefaultLakes(genSettings);
        DefaultBiomeFeatures.addDungeons(genSettings);
        DefaultBiomeFeatures.addMineables(genSettings);
        DefaultBiomeFeatures.addDefaultOres(genSettings);
        DefaultBiomeFeatures.addDefaultMushrooms(genSettings);
        DefaultBiomeFeatures.addSprings(genSettings);
        
        genSettings.structureFeature(ConfiguredStructureFeatures.PILLAGER_OUTPOST);
        genSettings.structureFeature(ConfiguredStructureFeatures.VILLAGE_PLAINS);
        genSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL);
        
        genSettings.feature(Feature.UNDERGROUND_ORES, BetaConfiguredFeature.ORE_CLAY);
        genSettings.feature(Feature.UNDERGROUND_ORES, BetaConfiguredFeature.ORE_EMERALD_Y95);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, BetaConfiguredFeature.PATCH_DANDELION_2);
        genSettings.feature(Feature.VEGETAL_DECORATION, BetaConfiguredFeature.PATCH_POPPY);
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.TREES_WATER);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_SUGAR_CANE);
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_PUMPKIN);
        
        genSettings.feature(Feature.TOP_LAYER_MODIFICATION, BetaConfiguredFeature.BETA_FREEZE_TOP_LAYER);
        
        genSettings.carver(GenerationStep.Carver.AIR, BetaCarver.CONF_BETA_CAVE_CARVER);
        
        return (new Biome.Builder())
            .precipitation(Biome.Precipitation.NONE)
            .category(Biome.Category.TAIGA)
            .depth(0.37F)
            .scale(0.5F)
            .temperature(0.5F)
            .downfall(0.0F)
            .effects((new BiomeEffects.Builder())
                .skyColor(12632319)
                .fogColor(9605819)
                .waterColor(4159204)
                .waterFogColor(329011)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
