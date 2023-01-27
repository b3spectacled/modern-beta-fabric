package com.bespectacled.modernbeta.world.biome.beta;

import com.bespectacled.modernbeta.world.biome.ModernBetaBiomeColors;
import com.bespectacled.modernbeta.world.biome.ModernBetaBiomeFeatures;
import com.bespectacled.modernbeta.world.biome.ModernBetaBiomeMobs;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;

public class BetaIceDesert {
    public static final Biome BIOME = create();
    
    private static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        ModernBetaBiomeMobs.addTundraMobs(spawnSettings);
        ModernBetaBiomeMobs.addSquid(spawnSettings);
        
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        ModernBetaBiomeFeatures.addIceDesertFeatures(genSettings, false);
        
        return (new Biome.Builder())
            .precipitation(Biome.Precipitation.SNOW)
            .category(Biome.Category.DESERT)
            .temperature(0.0F)
            .downfall(0.5F)
            .effects((new BiomeEffects.Builder())
                .skyColor(ModernBetaBiomeColors.BETA_COLD_SKY_COLOR)
                .fogColor(ModernBetaBiomeColors.BETA_FOG_COLOR)
                .waterColor(ModernBetaBiomeColors.VANILLA_FROZEN_WATER_COLOR)
                .waterFogColor(ModernBetaBiomeColors.VANILLA_FROZEN_WATER_FOG_COLOR)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
