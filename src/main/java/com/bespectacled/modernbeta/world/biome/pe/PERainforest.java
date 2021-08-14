package com.bespectacled.modernbeta.world.biome.pe;

import com.bespectacled.modernbeta.world.biome.OldBiomeColors;
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

public class PERainforest {
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
        
        DefaultBiomeFeatures.addDefaultUndergroundStructures(genSettings);
        DefaultBiomeFeatures.addDungeons(genSettings);
        DefaultBiomeFeatures.addDefaultOres(genSettings);
        DefaultBiomeFeatures.addDefaultMushrooms(genSettings);
        DefaultBiomeFeatures.addSprings(genSettings);
        DefaultBiomeFeatures.addAmethystGeodes(genSettings);
        
        genSettings.structureFeature(ConfiguredStructureFeatures.JUNGLE_PYRAMID);
        genSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL_JUNGLE);
        
        genSettings.feature(Feature.UNDERGROUND_ORES, ConfiguredFeatures.ORE_DIRT);
        genSettings.feature(Feature.UNDERGROUND_ORES, ConfiguredFeatures.ORE_GRAVEL);
        genSettings.feature(Feature.UNDERGROUND_ORES, OldConfiguredFeatures.ORE_CLAY);
        genSettings.feature(Feature.UNDERGROUND_ORES, OldConfiguredFeatures.ORE_EMERALD_Y95);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_PE_RAINFOREST_BEES);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_ALPHA_2);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_SUGAR_CANE);
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_PUMPKIN);
        
        genSettings.feature(Feature.TOP_LAYER_MODIFICATION, OldConfiguredFeatures.BETA_FREEZE_TOP_LAYER);
        
        genSettings.carver(GenerationStep.Carver.AIR, OldCarvers.CONF_OLD_BETA_CAVE_CARVER);
        genSettings.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.CANYON);
        
        return (new Biome.Builder())
            .precipitation(Biome.Precipitation.RAIN)
            .category(Biome.Category.JUNGLE)
            .depth(0.37F)
            .scale(0.5F)
            .temperature(1.0F)
            .downfall(1.0F)
            .effects((new BiomeEffects.Builder())
                .skyColor(OldBiomeColors.PE_SKY_COLOR)
                .fogColor(OldBiomeColors.PE_FOG_COLOR)
                .waterColor(OldBiomeColors.OLD_WATER_COLOR)
                .waterFogColor(OldBiomeColors.OLD_WATER_FOG_COLOR)
                .grassColor(OldBiomeColors.PE_GRASS_COLOR)
                .foliageColor(OldBiomeColors.PE_FOLIAGE_COLOR)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
