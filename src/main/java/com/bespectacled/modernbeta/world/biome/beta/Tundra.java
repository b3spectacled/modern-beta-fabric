package com.bespectacled.modernbeta.world.biome.beta;

import com.bespectacled.modernbeta.world.biome.OldBiomeColors;
import com.bespectacled.modernbeta.world.biome.OldBiomeFeatures;
import com.bespectacled.modernbeta.world.biome.OldBiomeMobs;
import com.bespectacled.modernbeta.world.feature.OldConfiguredFeatures;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep.Feature;

public class Tundra {
    public static final Biome BIOME = create();
    
    private static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        OldBiomeMobs.addTundraMobs(spawnSettings);
        OldBiomeMobs.addSquid(spawnSettings);
        
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        
        OldBiomeFeatures.addDefaultFeatures(genSettings, false, BetaBiomes.ADD_LAKES, BetaBiomes.ADD_SPRINGS);
        OldBiomeFeatures.addMineables(genSettings, BetaBiomes.ADD_ALTERNATE_STONES, BetaBiomes.ADD_NEW_MINEABLES);
        OldBiomeFeatures.addOres(genSettings);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        
        OldBiomeFeatures.addVegetalPatches(genSettings);
        
        OldBiomeFeatures.addBetaFrozenTopLayer(genSettings);
        
        OldBiomeFeatures.addCarvers(genSettings, true);
        
        return (new Biome.Builder())
            .precipitation(Biome.Precipitation.SNOW)
            .category(Biome.Category.ICY)
            .temperature(0.0F)
            .downfall(0.5F)
            .effects((new BiomeEffects.Builder())
                .skyColor(OldBiomeColors.BETA_COLD_SKY_COLOR)
                .fogColor(OldBiomeColors.BETA_FOG_COLOR)
                .waterColor(OldBiomeColors.VANILLA_FROZEN_WATER_COLOR)
                .waterFogColor(OldBiomeColors.VANILLA_FROZEN_WATER_FOG_COLOR)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
