package com.bespectacled.modernbeta.world.biome.indev;

import com.bespectacled.modernbeta.world.biome.OldBiomeColors;
import com.bespectacled.modernbeta.world.biome.OldBiomeFeatures;
import com.bespectacled.modernbeta.world.biome.OldBiomeMobs;
import com.bespectacled.modernbeta.world.feature.OldConfiguredFeatures;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep.Feature;

public class IndevWoods {
    public static final Biome BIOME = create();

    private static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        OldBiomeMobs.addCommonMobs(spawnSettings);
        OldBiomeMobs.addSquid(spawnSettings);
        OldBiomeMobs.addWolves(spawnSettings);
        
        spawnSettings.playerSpawnFriendly();;
        
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        
        OldBiomeFeatures.addDefaultFeatures(genSettings, false, IndevBiomes.ADD_LAKES, IndevBiomes.ADD_SPRINGS);
        OldBiomeFeatures.addOres(genSettings);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_CACTUS_ALPHA);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_INDEV_WOODS_BEES);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_ALPHA_2);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.MUSHROOM_HELL);
        
        OldBiomeFeatures.addCarvers(genSettings, false);
        
        return (new Biome.Builder())
            .precipitation(Biome.Precipitation.NONE)
            .category(Biome.Category.FOREST)
            .temperature(0.6F)
            .downfall(0.6F)
            .effects((new BiomeEffects.Builder())
                .grassColor(OldBiomeColors.OLD_GRASS_COLOR)
                .foliageColor(OldBiomeColors.OLD_FOLIAGE_COLOR)
                .skyColor(OldBiomeColors.INDEV_WOODS_SKY_COLOR)
                .fogColor(OldBiomeColors.INDEV_WOODS_FOG_COLOR)
                .waterColor(OldBiomeColors.OLD_WATER_COLOR)
                .waterFogColor(OldBiomeColors.OLD_WATER_FOG_COLOR)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
