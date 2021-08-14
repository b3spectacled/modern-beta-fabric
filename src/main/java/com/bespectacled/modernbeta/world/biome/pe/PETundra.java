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

public class PETundra {
    public static final Biome BIOME = create();
    
    private static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addSnowyMobs(spawnSettings);
        
        spawnSettings.spawn(SpawnGroup.WATER_CREATURE, new SpawnSettings.SpawnEntry(EntityType.SQUID, 10, 1, 4));
        
        // TODO: Move maybe later
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.GOAT, 10, 4, 6));
        
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        genSettings.surfaceBuilder(ConfiguredSurfaceBuilders.GRASS);
        
        DefaultBiomeFeatures.addDefaultUndergroundStructures(genSettings);
        DefaultBiomeFeatures.addDungeons(genSettings);
        DefaultBiomeFeatures.addDefaultOres(genSettings);
        DefaultBiomeFeatures.addDefaultMushrooms(genSettings);
        DefaultBiomeFeatures.addSprings(genSettings);
        DefaultBiomeFeatures.addAmethystGeodes(genSettings);
        
        genSettings.structureFeature(ConfiguredStructureFeatures.VILLAGE_SNOWY);
        genSettings.structureFeature(ConfiguredStructureFeatures.IGLOO);
        genSettings.structureFeature(ConfiguredStructureFeatures.PILLAGER_OUTPOST);
        genSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL);
        
        genSettings.feature(Feature.UNDERGROUND_ORES, ConfiguredFeatures.ORE_DIRT);
        genSettings.feature(Feature.UNDERGROUND_ORES, ConfiguredFeatures.ORE_GRAVEL);
        genSettings.feature(Feature.UNDERGROUND_ORES, OldConfiguredFeatures.ORE_CLAY);
        genSettings.feature(Feature.UNDERGROUND_ORES, OldConfiguredFeatures.ORE_EMERALD_Y95);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_SUGAR_CANE);
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_PUMPKIN);
        
        genSettings.feature(Feature.TOP_LAYER_MODIFICATION, OldConfiguredFeatures.BETA_FREEZE_TOP_LAYER);
        
        genSettings.carver(GenerationStep.Carver.AIR, OldCarvers.CONF_OLD_BETA_CAVE_CARVER);
        genSettings.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.CANYON);
        
        return (new Biome.Builder())
            .precipitation(Biome.Precipitation.SNOW)
            .category(Biome.Category.ICY)
            .depth(0.3F)
            .scale(0.2F)
            .temperature(0.0F)
            .downfall(0.5F)
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
