package com.bespectacled.modernbeta.world.biome.pe;

import com.bespectacled.modernbeta.world.biome.OldBiomeColors;
import com.bespectacled.modernbeta.world.biome.OldBiomeFeatures;
import com.bespectacled.modernbeta.world.biome.OldBiomeMobs;
import com.bespectacled.modernbeta.world.biome.OldBiomeStructures;
import com.bespectacled.modernbeta.world.feature.OldConfiguredFeatures;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;

public class PEShrubland {
    public static final Biome BIOME = create();
    
    private static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        OldBiomeMobs.addPlainsMobs(spawnSettings);
        OldBiomeMobs.addSquid(spawnSettings);
        OldBiomeMobs.addTurtles(spawnSettings);
        
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        genSettings.surfaceBuilder(ConfiguredSurfaceBuilders.GRASS);
        
        OldBiomeFeatures.addDefaultFeatures(genSettings, false, PEBiomes.ADD_LAKES, PEBiomes.ADD_SPRINGS);
        OldBiomeFeatures.addMineables(genSettings, PEBiomes.ADD_ALTERNATE_STONES, PEBiomes.ADD_NEW_MINEABLES);
        OldBiomeFeatures.addOres(genSettings);
        
        OldBiomeStructures.addLowlandStructures(genSettings);
        
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
