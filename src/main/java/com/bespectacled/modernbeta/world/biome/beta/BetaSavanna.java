package com.bespectacled.modernbeta.world.biome.beta;

import com.bespectacled.modernbeta.world.biome.ModernBetaBiomeColors;
import com.bespectacled.modernbeta.world.biome.ModernBetaBiomeFeatures;
import com.bespectacled.modernbeta.world.biome.ModernBetaBiomeMobs;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;

public class BetaSavanna {
    public static final Biome BIOME = create();
    
    private static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        ModernBetaBiomeMobs.addPlainsMobs(spawnSettings);
        ModernBetaBiomeMobs.addSquid(spawnSettings);
        ModernBetaBiomeMobs.addTurtles(spawnSettings);
        
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        ModernBetaBiomeFeatures.addSavannaFeatures(genSettings, false);
        
        return (new Biome.Builder())
            .precipitation(Biome.Precipitation.RAIN)
            .category(Biome.Category.SAVANNA)
            .temperature(0.7F)
            .downfall(0.1F)
            .effects((new BiomeEffects.Builder())
                .skyColor(ModernBetaBiomeColors.BETA_TEMP_SKY_COLOR)
                .fogColor(ModernBetaBiomeColors.BETA_FOG_COLOR)
                .waterColor(ModernBetaBiomeColors.VANILLA_WATER_COLOR)
                .waterFogColor(ModernBetaBiomeColors.VANILLA_WATER_FOG_COLOR)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
