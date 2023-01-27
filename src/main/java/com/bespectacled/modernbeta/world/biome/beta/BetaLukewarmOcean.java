package com.bespectacled.modernbeta.world.biome.beta;

import com.bespectacled.modernbeta.world.biome.ModernBetaBiomeColors;
import com.bespectacled.modernbeta.world.biome.ModernBetaBiomeFeatures;
import com.bespectacled.modernbeta.world.biome.ModernBetaBiomeMobs;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;

public class BetaLukewarmOcean {
    public static final Biome BIOME = create();
    
    private static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        ModernBetaBiomeMobs.addLukewarmOceanMobs(spawnSettings);
        
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        ModernBetaBiomeFeatures.addLukewarmOceanFeatures(genSettings, false);
        
        return (new Biome.Builder())
            .precipitation(Biome.Precipitation.RAIN)
            .category(Biome.Category.OCEAN)
            .temperature(1.0F)
            .downfall(0.7F)
            .effects((new BiomeEffects.Builder())
                .skyColor(ModernBetaBiomeColors.BETA_WARM_SKY_COLOR)
                .fogColor(ModernBetaBiomeColors.BETA_FOG_COLOR)
                .waterColor(ModernBetaBiomeColors.USE_DEBUG_OCEAN_COLOR ? 16777215 : ModernBetaBiomeColors.VANILLA_LUKEWARM_WATER_COLOR)
                .waterFogColor(ModernBetaBiomeColors.VANILLA_LUKEWARM_WATER_FOG_COLOR)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
