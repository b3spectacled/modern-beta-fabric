package com.bespectacled.modernbeta.world.biome.pe;

import com.bespectacled.modernbeta.world.biome.OldBiomeColors;
import com.bespectacled.modernbeta.world.biome.OldBiomeFeatures;
import com.bespectacled.modernbeta.world.feature.OldConfiguredFeatures;
import com.bespectacled.modernbeta.world.structure.OldStructures;

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

public class PEWarmOcean {
    public static final Biome BIOME = create();
    
    private static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addWarmOceanMobs(spawnSettings, 10, 4);
        
        spawnSettings.spawn(SpawnGroup.WATER_AMBIENT, new SpawnEntry(EntityType.PUFFERFISH, 15, 1, 3));
        
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        genSettings.surfaceBuilder(ConfiguredSurfaceBuilders.FULL_SAND);
        
        OldBiomeFeatures.addDefaultFeatures(genSettings, true, PEBiomes.ADD_LAKES, PEBiomes.ADD_SPRINGS);
        OldBiomeFeatures.addMineables(genSettings, PEBiomes.ADD_ALTERNATE_STONES, PEBiomes.ADD_NEW_MINEABLES);
        OldBiomeFeatures.addOres(genSettings);
        
        genSettings.structureFeature(ConfiguredStructureFeatures.BURIED_TREASURE);
        genSettings.structureFeature(ConfiguredStructureFeatures.OCEAN_RUIN_WARM);
        genSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL_OCEAN);
        genSettings.structureFeature(OldStructures.CONF_OCEAN_SHRINE_STRUCTURE);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_ALPHA_2);
        
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
