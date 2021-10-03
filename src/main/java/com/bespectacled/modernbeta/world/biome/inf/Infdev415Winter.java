package com.bespectacled.modernbeta.world.biome.inf;

import com.bespectacled.modernbeta.world.biome.OldBiomeColors;
import com.bespectacled.modernbeta.world.biome.OldBiomeFeatures;
import com.bespectacled.modernbeta.world.biome.OldBiomeMobs;
import com.bespectacled.modernbeta.world.feature.OldConfiguredFeatures;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;

public class Infdev415Winter {
    public static final Biome BIOME = create();
    
    private static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        OldBiomeMobs.addCommonMobs(spawnSettings);
        OldBiomeMobs.addSquid(spawnSettings);
        OldBiomeMobs.addWolves(spawnSettings);
        
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        genSettings.surfaceBuilder(ConfiguredSurfaceBuilders.GRASS);
        
        OldBiomeFeatures.addDefaultFeatures(genSettings, false, InfBiomes.ADD_LAKES_INF_415, InfBiomes.ADD_SPRINGS_INF_415);
        OldBiomeFeatures.addOres(genSettings);
        DefaultBiomeFeatures.addFrozenTopLayer(genSettings);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_CACTUS_ALPHA);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_INFDEV_415_BEES);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_ALPHA_2);
        
        OldBiomeFeatures.addVegetalPatches(genSettings);
        
        OldBiomeFeatures.addCarvers(genSettings, false);
        
        return (new Biome.Builder())
            .precipitation(Biome.Precipitation.SNOW)
            .category(Biome.Category.FOREST)
            .temperature(0.0F)
            .downfall(0.0F)
            .effects((new BiomeEffects.Builder())
                .grassColor(OldBiomeColors.OLD_GRASS_COLOR)
                .foliageColor(OldBiomeColors.OLD_FOLIAGE_COLOR)
                .skyColor(OldBiomeColors.INFDEV_415_SKY_COLOR)
                .fogColor(OldBiomeColors.INFDEV_415_FOG_COLOR)
                .waterColor(OldBiomeColors.OLD_WATER_COLOR)
                .waterFogColor(OldBiomeColors.OLD_WATER_FOG_COLOR)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
