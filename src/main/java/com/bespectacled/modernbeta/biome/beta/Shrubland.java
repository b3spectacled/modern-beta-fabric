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

public class Shrubland {
    public static final Biome BIOME = create();
    
    private static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addPlainsMobs(spawnSettings);
        
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.TURTLE, 5, 2, 5));
        spawnSettings.spawn(SpawnGroup.WATER_CREATURE, new SpawnSettings.SpawnEntry(EntityType.SQUID, 10, 1, 4));
        spawnSettings.spawn(SpawnGroup.WATER_AMBIENT, new SpawnSettings.SpawnEntry(EntityType.COD, 10, 3, 6));
        spawnSettings.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.DROWNED, 5, 1, 1));
        
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        genSettings.surfaceBuilder(ConfiguredSurfaceBuilders.GRASS);
        
        DefaultBiomeFeatures.addDefaultUndergroundStructures(genSettings);
        DefaultBiomeFeatures.addDefaultLakes(genSettings);
        DefaultBiomeFeatures.addDungeons(genSettings);
        DefaultBiomeFeatures.addMineables(genSettings);
        DefaultBiomeFeatures.addDefaultOres(genSettings);
        DefaultBiomeFeatures.addDefaultMushrooms(genSettings);
        DefaultBiomeFeatures.addSprings(genSettings);
        
        genSettings.structureFeature(ConfiguredStructureFeatures.VILLAGE_PLAINS);
        genSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL);
        
        genSettings.feature(Feature.UNDERGROUND_ORES, BetaConfiguredFeature.ORE_CLAY);
        genSettings.feature(Feature.UNDERGROUND_ORES, BetaConfiguredFeature.ORE_EMERALD_Y95);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, BetaConfiguredFeature.PATCH_POPPY);
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.TREES_WATER);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_SUGAR_CANE);
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_PUMPKIN);
        
        genSettings.feature(Feature.TOP_LAYER_MODIFICATION, BetaConfiguredFeature.BETA_FREEZE_TOP_LAYER);
        
        genSettings.carver(GenerationStep.Carver.AIR, BetaCarver.CONF_BETA_CAVE_CARVER);
        genSettings.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.CANYON);
        
        return (new Biome.Builder())
            .precipitation(Biome.Precipitation.RAIN)
            .category(Biome.Category.PLAINS)
            .depth(0.3F)
            .scale(0.2F)
            .temperature(0.7F)
            .downfall(0.4F)
            .effects((new BiomeEffects.Builder())
                .skyColor(9158143)
                .fogColor(12638463)
                .waterColor(4159204)
                .waterFogColor(329011)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
