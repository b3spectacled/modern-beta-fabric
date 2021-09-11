package com.bespectacled.modernbeta.world.biome.beta;

import com.bespectacled.modernbeta.world.biome.OldBiomeColors;
import com.bespectacled.modernbeta.world.biome.OldBiomeFeatures;
import com.bespectacled.modernbeta.world.feature.OldConfiguredFeatures;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.SpawnSettings.SpawnEntry;
import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;

public class Rainforest {
    public static final Biome BIOME = create();
    
    private static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addFarmAnimals(spawnSettings);
        DefaultBiomeFeatures.addBatsAndMonsters(spawnSettings);
        
        spawnSettings.spawn(SpawnGroup.WATER_CREATURE, new SpawnSettings.SpawnEntry(EntityType.SQUID, 10, 1, 4));
        spawnSettings.spawn(SpawnGroup.MONSTER, new SpawnEntry(EntityType.OCELOT, 2, 1, 3));
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.PANDA, 2, 1, 2));
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.PARROT, 40, 1, 2));
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.TURTLE, 5, 2, 5));
        
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        genSettings.surfaceBuilder(ConfiguredSurfaceBuilders.GRASS);
        
        OldBiomeFeatures.addDefaultFeatures(genSettings, false, true, true);
        OldBiomeFeatures.addMineables(genSettings, true);
        OldBiomeFeatures.addOres(genSettings);
        
        genSettings.structureFeature(ConfiguredStructureFeatures.JUNGLE_PYRAMID);
        genSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL_JUNGLE);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_BETA_RAINFOREST);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_RAINFOREST_10);
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.FOREST_FLOWER_VEGETATION);
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_MELON);
        
        OldBiomeFeatures.addVegetalPatches(genSettings);
        
        OldBiomeFeatures.addBetaFrozenTopLayer(genSettings);
        
        OldBiomeFeatures.addCarvers(genSettings, true);
        
        return (new Biome.Builder())
            .precipitation(Biome.Precipitation.RAIN)
            .category(Biome.Category.JUNGLE)
            .depth(0.37F)
            .scale(0.5F)
            .temperature(1.0F)
            .downfall(1.0F)
            .effects((new BiomeEffects.Builder())
                .skyColor(OldBiomeColors.BETA_WARM_SKY_COLOR)
                .fogColor(OldBiomeColors.BETA_FOG_COLOR)
                .waterColor(OldBiomeColors.VANILLA_WARM_WATER_COLOR)
                .waterFogColor(OldBiomeColors.VANILLA_WARM_WATER_FOG_COLOR)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
