package com.bespectacled.modernbeta.world.biome.pe;

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
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;

public class PEShrubland {
    public static final Biome BIOME = create();
    
    private static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addPlainsMobs(spawnSettings);
        
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.TURTLE, 5, 2, 5));
        spawnSettings.spawn(SpawnGroup.WATER_CREATURE, new SpawnSettings.SpawnEntry(EntityType.SQUID, 10, 1, 4));
        
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        genSettings.surfaceBuilder(ConfiguredSurfaceBuilders.GRASS);
        
        OldBiomeFeatures.addDefaultFeatures(genSettings, false, false, true);
        OldBiomeFeatures.addMineables(genSettings, true);
        OldBiomeFeatures.addOres(genSettings);
        
        genSettings.structureFeature(ConfiguredStructureFeatures.VILLAGE_PLAINS);
        genSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_PE_SPARSE);
        
        OldBiomeFeatures.addVegetalPatches(genSettings);
        
        OldBiomeFeatures.addBetaFrozenTopLayer(genSettings);
        
        OldBiomeFeatures.addCarvers(genSettings, true);
        
        return (new Biome.Builder())
            .precipitation(Biome.Precipitation.RAIN)
            .category(Biome.Category.PLAINS)
            .depth(0.3F)
            .scale(0.2F)
            .temperature(0.7F)
            .downfall(0.4F)
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
