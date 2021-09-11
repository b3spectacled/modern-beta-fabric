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
import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;

public class Sky {
    public static final Biome BIOME = create();
    
    private static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addMonsters(spawnSettings, 95, 5, 20);
        spawnSettings.spawn(SpawnGroup.CREATURE,  new SpawnSettings.SpawnEntry(EntityType.CHICKEN, 10, 4, 4));
        
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        genSettings.surfaceBuilder(ConfiguredSurfaceBuilders.GRASS);
        
        OldBiomeFeatures.addDefaultFeatures(genSettings, false, true, true);
        OldBiomeFeatures.addMineables(genSettings, true);
        OldBiomeFeatures.addOres(genSettings);
        
        genSettings.structureFeature(ConfiguredStructureFeatures.PILLAGER_OUTPOST);
        genSettings.structureFeature(ConfiguredStructureFeatures.VILLAGE_PLAINS);
        genSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_BETA_SPARSE);
        OldBiomeFeatures.addVegetalPatches(genSettings);
        
        OldBiomeFeatures.addBetaFrozenTopLayer(genSettings);
        
        OldBiomeFeatures.addCarvers(genSettings, false);
        
        return (new Biome.Builder())
            .precipitation(Biome.Precipitation.NONE)
            .category(Biome.Category.TAIGA)
            .depth(0.37F)
            .scale(0.5F)
            .temperature(0.5F)
            .downfall(0.0F)
            .effects((new BiomeEffects.Builder())
                .skyColor(OldBiomeColors.SKYLANDS_SKY_COLOR)
                .fogColor(OldBiomeColors.SKYLANDS_FOG_COLOR)
                .waterColor(OldBiomeColors.VANILLA_WATER_COLOR)
                .waterFogColor(OldBiomeColors.VANILLA_WATER_FOG_COLOR)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
