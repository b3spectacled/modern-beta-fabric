package com.bespectacled.modernbeta.world.biome.beta;

import com.bespectacled.modernbeta.world.carver.OldCarvers;
import com.bespectacled.modernbeta.world.feature.OldConfiguredFeatures;

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

public class Taiga {
    public static final Biome BIOME = create();
    
    private static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addFarmAnimals(spawnSettings);
        DefaultBiomeFeatures.addBatsAndMonsters(spawnSettings);
        
        spawnSettings.spawn(SpawnGroup.WATER_CREATURE, new SpawnSettings.SpawnEntry(EntityType.SQUID, 10, 1, 4));
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.WOLF, 5, 4, 4));
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.RABBIT, 4, 2, 3));
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.LLAMA, 4, 4, 6));
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.FOX, 8, 2, 4));
        
        // TODO: Move maybe later
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.GOAT, 10, 4, 6));
        
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        genSettings.surfaceBuilder(ConfiguredSurfaceBuilders.GRASS);
        
        DefaultBiomeFeatures.addDefaultUndergroundStructures(genSettings);
        DefaultBiomeFeatures.addDefaultLakes(genSettings);
        DefaultBiomeFeatures.addDungeons(genSettings);
        DefaultBiomeFeatures.addMineables(genSettings, false);
        DefaultBiomeFeatures.addDefaultOres(genSettings);
        DefaultBiomeFeatures.addDefaultMushrooms(genSettings);
        DefaultBiomeFeatures.addSprings(genSettings);
        DefaultBiomeFeatures.addSweetBerryBushes(genSettings);
        DefaultBiomeFeatures.addAmethystGeodes(genSettings);
        
        genSettings.structureFeature(ConfiguredStructureFeatures.VILLAGE_TAIGA);
        genSettings.structureFeature(ConfiguredStructureFeatures.IGLOO);
        genSettings.structureFeature(ConfiguredStructureFeatures.PILLAGER_OUTPOST);
        genSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL_MOUNTAIN);
        
        genSettings.feature(Feature.UNDERGROUND_ORES, OldConfiguredFeatures.ORE_CLAY);
        genSettings.feature(Feature.UNDERGROUND_ORES, OldConfiguredFeatures.ORE_EMERALD_Y95);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_TAIGA_1);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_BETA_TAIGA);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_SUGAR_CANE);
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_PUMPKIN);
        
        genSettings.feature(Feature.TOP_LAYER_MODIFICATION, OldConfiguredFeatures.BETA_FREEZE_TOP_LAYER);
        
        genSettings.carver(GenerationStep.Carver.AIR, OldCarvers.CONF_OLD_BETA_CAVE_CARVER);
        genSettings.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.CANYON);
        
        return (new Biome.Builder())
            .precipitation(Biome.Precipitation.SNOW)
            .category(Biome.Category.TAIGA)
            .depth(0.37F)
            .scale(0.5F)
            //.temperature(0.4F) TODO: Re-add this later as it looks more accurate; for some reason precipitation currently does not work properly.
            //.downfall(0.8F)
            .temperature(0.0F)
            .downfall(0.5F)
            .effects((new BiomeEffects.Builder())
                .skyColor(8756991)
                .fogColor(12638463)
                .waterColor(3750089)
                .waterFogColor(329011)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
