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

public class Desert {
    public static final Biome BIOME = create();
    
    private static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addDesertMobs(spawnSettings);
        
        spawnSettings.spawn(SpawnGroup.WATER_CREATURE, new SpawnEntry(EntityType.SQUID, 10, 1, 4));
        
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        genSettings.surfaceBuilder(ConfiguredSurfaceBuilders.DESERT);
        
        DefaultBiomeFeatures.addDefaultUndergroundStructures(genSettings);
        DefaultBiomeFeatures.addDesertLakes(genSettings);
        DefaultBiomeFeatures.addDungeons(genSettings);
        DefaultBiomeFeatures.addDesertFeatures(genSettings);
        DefaultBiomeFeatures.addFossils(genSettings);
        DefaultBiomeFeatures.addMineables(genSettings);
        DefaultBiomeFeatures.addDefaultOres(genSettings);
        DefaultBiomeFeatures.addDefaultMushrooms(genSettings);
        DefaultBiomeFeatures.addSprings(genSettings);
        DefaultBiomeFeatures.addAmethystGeodes(genSettings);
        
        genSettings.structureFeature(ConfiguredStructureFeatures.VILLAGE_DESERT);
        genSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL_DESERT);
        genSettings.structureFeature(ConfiguredStructureFeatures.DESERT_PYRAMID);
        genSettings.structureFeature(ConfiguredStructureFeatures.PILLAGER_OUTPOST);
        
        genSettings.feature(Feature.UNDERGROUND_ORES, OldConfiguredFeatures.ORE_CLAY);
        genSettings.feature(Feature.UNDERGROUND_ORES, OldConfiguredFeatures.ORE_EMERALD_Y95);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_DEAD_BUSH_2);
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_CACTUS_DESERT);
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_SUGAR_CANE);
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_PUMPKIN);
        
        genSettings.feature(Feature.TOP_LAYER_MODIFICATION, OldConfiguredFeatures.BETA_FREEZE_TOP_LAYER);
        
        genSettings.carver(GenerationStep.Carver.AIR, OldCarvers.CONF_OLD_BETA_CAVE_CARVER);
        genSettings.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.CANYON);
        genSettings.carver(GenerationStep.Carver.AIR, OldCarvers.CONF_DEEP_BETA_CAVE_CARVER);
        genSettings.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.CRACK);
        
        return (new Biome.Builder())
            .precipitation(Biome.Precipitation.NONE)
            .category(Biome.Category.DESERT)
            .depth(0.3F)
            .scale(0.2F)
            .temperature(1.0F)
            .downfall(0.0F)
            .effects((new BiomeEffects.Builder())
                .skyColor(6733055)
                .fogColor(12638463)
                .waterColor(4159204)
                .waterFogColor(329011)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
