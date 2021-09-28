package com.bespectacled.modernbeta.world.biome.beta;

import com.bespectacled.modernbeta.world.biome.OldBiomeColors;
import com.bespectacled.modernbeta.world.biome.OldBiomeFeatures;
import com.bespectacled.modernbeta.world.biome.OldBiomeMobs;
import com.bespectacled.modernbeta.world.biome.OldBiomeStructures;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;

public class WarmOcean {
    public static final Biome BIOME = create();
    
    private static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        OldBiomeMobs.addWarmOceanMobs(spawnSettings);
        
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        genSettings.surfaceBuilder(ConfiguredSurfaceBuilders.FULL_SAND);
        
        OldBiomeFeatures.addDefaultFeatures(genSettings, true, BetaBiomes.ADD_LAKES, BetaBiomes.ADD_SPRINGS);
        OldBiomeFeatures.addMineables(genSettings, BetaBiomes.ADD_ALTERNATE_STONES, BetaBiomes.ADD_NEW_MINEABLES);
        OldBiomeFeatures.addOres(genSettings);
        
        OldBiomeStructures.addOceanStructures(genSettings, true);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.WARM_OCEAN_VEGETATION);
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.SEAGRASS_WARM);
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.SEA_PICKLE);
        
        OldBiomeFeatures.addVegetalPatches(genSettings);
        
        OldBiomeFeatures.addBetaFrozenTopLayer(genSettings);
        
        OldBiomeFeatures.addCarvers(genSettings, true);
        OldBiomeFeatures.addOceanCarvers(genSettings);
        
        return (new Biome.Builder())
            .precipitation(Biome.Precipitation.RAIN)
            .category(Biome.Category.OCEAN)
            .depth(-1.0F)
            .scale(0.1F)
            .temperature(1.0F)
            .downfall(1.0F)
            .effects((new BiomeEffects.Builder())
                .skyColor(OldBiomeColors.BETA_WARM_SKY_COLOR)
                .fogColor(OldBiomeColors.BETA_FOG_COLOR)
                .waterColor(OldBiomeColors.USE_DEBUG_OCEAN_COLOR ? 16777215 : OldBiomeColors.VANILLA_WARM_WATER_COLOR)
                .waterFogColor(OldBiomeColors.VANILLA_WARM_WATER_FOG_COLOR)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
