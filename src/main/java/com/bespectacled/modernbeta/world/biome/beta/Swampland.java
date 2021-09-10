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
import net.minecraft.world.biome.SpawnSettings.SpawnEntry;
import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;

public class Swampland {
    public static final Biome BIOME = create();
    
    private static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addFarmAnimals(spawnSettings);
        DefaultBiomeFeatures.addBatsAndMonsters(spawnSettings);
        
        spawnSettings.spawn(SpawnGroup.WATER_CREATURE, new SpawnSettings.SpawnEntry(EntityType.SQUID, 10, 1, 4));
        
        spawnSettings.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.SLIME, 1, 1, 1));
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.TURTLE, 5, 2, 5));
        
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        genSettings.surfaceBuilder(ConfiguredSurfaceBuilders.SWAMP);
        
        DefaultBiomeFeatures.addFossils(genSettings);
        OldBiomeFeatures.addDefaultFeatures(genSettings, false);
        OldBiomeFeatures.addMineables(genSettings);
        OldBiomeFeatures.addOres(genSettings);
        
        genSettings.structureFeature(ConfiguredStructureFeatures.SWAMP_HUT);
        genSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL_SWAMP);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_BETA_SPARSE);
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.FLOWER_SWAMP);
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_WATERLILLY);
        
        OldBiomeFeatures.addVegetalPatches(genSettings);
        
        OldBiomeFeatures.addBetaFrozenTopLayer(genSettings);
        
        OldBiomeFeatures.addCarvers(genSettings, true);
        
        return (new Biome.Builder())
            .precipitation(Biome.Precipitation.RAIN)
            .category(Biome.Category.SWAMP)
            .depth(0.37F)
            .scale(0.5F)
            .temperature(0.5F)
            .downfall(1.0F)
            .effects((new BiomeEffects.Builder())
                .skyColor(OldBiomeColors.BETA_COOL_SKY_COLOR)
                .fogColor(OldBiomeColors.BETA_FOG_COLOR)
                .waterColor(OldBiomeColors.VANILLA_COLD_WATER_COLOR)
                .waterFogColor(OldBiomeColors.VANILLA_COLD_WATER_FOG_COLOR)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
