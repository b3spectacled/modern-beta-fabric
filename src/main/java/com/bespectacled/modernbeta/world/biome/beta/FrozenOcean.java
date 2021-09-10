package com.bespectacled.modernbeta.world.biome.beta;

import com.bespectacled.modernbeta.world.biome.OldBiomeColors;
import com.bespectacled.modernbeta.world.biome.OldBiomeFeatures;
import com.bespectacled.modernbeta.world.structure.OldStructures;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.SpawnSettings.SpawnEntry;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;

public class FrozenOcean {
    public static final Biome BIOME = create();
    
    private static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addOceanMobs(spawnSettings, 10, 4, 10);
        
        spawnSettings.spawn(SpawnGroup.WATER_AMBIENT, new SpawnEntry(EntityType.SALMON, 15, 1, 5));
        spawnSettings.spawn(SpawnGroup.WATER_CREATURE, new SpawnEntry(EntityType.DOLPHIN, 1, 1, 2));
        
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        genSettings.surfaceBuilder(ConfiguredSurfaceBuilders.GRASS);
        
        OldBiomeFeatures.addDefaultFeatures(genSettings, true);
        OldBiomeFeatures.addMineables(genSettings);
        OldBiomeFeatures.addOres(genSettings);
        
        genSettings.structureFeature(ConfiguredStructureFeatures.BURIED_TREASURE);
        genSettings.structureFeature(ConfiguredStructureFeatures.OCEAN_RUIN_COLD);
        genSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL_OCEAN);
        genSettings.structureFeature(OldStructures.CONF_OCEAN_SHRINE_STRUCTURE);
        
        OldBiomeFeatures.addVegetalPatches(genSettings);
        
        OldBiomeFeatures.addBetaFrozenTopLayer(genSettings);
        
        OldBiomeFeatures.addCarvers(genSettings, true);
        OldBiomeFeatures.addOceanCarvers(genSettings);
        
        return (new Biome.Builder())
            .precipitation(Biome.Precipitation.SNOW)
            .category(Biome.Category.OCEAN)
            .depth(-1.0F)
            .scale(0.1F)
            .temperature(0.0F)
            .downfall(0.5F)
            .effects((new BiomeEffects.Builder())
                .skyColor(OldBiomeColors.BETA_COLD_SKY_COLOR)
                .fogColor(OldBiomeColors.BETA_FOG_COLOR)
                .waterColor(OldBiomeColors.USE_DEBUG_OCEAN_COLOR ? 16777215 : OldBiomeColors.VANILLA_FROZEN_WATER_COLOR)
                .waterFogColor(OldBiomeColors.VANILLA_FROZEN_WATER_FOG_COLOR)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
