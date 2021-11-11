package com.bespectacled.modernbeta.world.biome;

import com.bespectacled.modernbeta.world.carver.OldCarvers;
import com.bespectacled.modernbeta.world.feature.OldConfiguredFeatures;

import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.carver.ConfiguredCarvers;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;

public class OldBiomeFeatures {
    
    /* Beta Biomes */
    
    public static void addDesertFeatures(GenerationSettings.Builder genSettings, boolean pe) {
        addDefaultFeatures(genSettings, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        DefaultBiomeFeatures.addDesertFeatures(genSettings);
        DefaultBiomeFeatures.addFossils(genSettings);
        
        if (pe) {
            addPEVegetation(genSettings, false);
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_CACTUS_PE);
        } else {
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
            genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_DEAD_BUSH_2);
            genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_CACTUS_DESERT);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(genSettings);
        DefaultBiomeFeatures.addDefaultVegetation(genSettings);
    }
    
    public static void addForestFeatures(GenerationSettings.Builder genSettings, boolean pe) {
        addDefaultFeatures(genSettings, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(genSettings, true);
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_PE_FOREST_BEES);
        } else {
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_BETA_FOREST);
            
            genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.FOREST_FLOWER_VEGETATION);
            genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_GRASS_FOREST);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(genSettings);
        DefaultBiomeFeatures.addDefaultVegetation(genSettings);
    }
    
    public static void addIceDesertFeatures(GenerationSettings.Builder genSettings, boolean pe) {
        addDefaultFeatures(genSettings, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(genSettings, false);
        } else {
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(genSettings);
        DefaultBiomeFeatures.addDefaultVegetation(genSettings);
    }
    
    public static void addPlainsFeatures(GenerationSettings.Builder genSettings, boolean pe) {
        addDefaultFeatures(genSettings, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(genSettings, true);
        } else {
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_3);
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_PLAINS_10);
        }
        
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.FLOWER_PLAIN_DECORATED);
    }
    
    public static void addRainforestFeatures(GenerationSettings.Builder genSettings, boolean pe) {
        addDefaultFeatures(genSettings, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(genSettings, true);
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_PE_RAINFOREST_BEES);
        } else {
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_BETA_RAINFOREST);
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_RAINFOREST_10);
            genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.FOREST_FLOWER_VEGETATION);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(genSettings);
        DefaultBiomeFeatures.addDefaultVegetation(genSettings);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_MELON);
    }
    
    public static void addSavannaFeatures(GenerationSettings.Builder genSettings, boolean pe) {
        addDefaultFeatures(genSettings, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(genSettings, false);
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_PE_SPARSE);
        } else {
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_BETA_SPARSE);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(genSettings);
        DefaultBiomeFeatures.addDefaultVegetation(genSettings);
    }
    
    public static void addSeasonalForestFeatures(GenerationSettings.Builder genSettings, boolean pe) {
        addDefaultFeatures(genSettings, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(genSettings, true);
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_PE_SEASONAL_FOREST_BEES);
        } else {
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_4);
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_BETA_SEASONAL_FOREST);
            genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_GRASS_FOREST);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(genSettings);
        DefaultBiomeFeatures.addDefaultVegetation(genSettings);
    }
    
    public static void addShrublandFeatures(GenerationSettings.Builder genSettings, boolean pe) {
        addDefaultFeatures(genSettings, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(genSettings, false);
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_PE_SPARSE);
        } else {
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_BETA_SPARSE);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(genSettings);
        DefaultBiomeFeatures.addDefaultVegetation(genSettings);
    }
    
    public static void addSkyFeatures(GenerationSettings.Builder genSettings) {
        addDefaultFeatures(genSettings, OldFeatureSettings.SKY);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_BETA_SPARSE);
        
        DefaultBiomeFeatures.addDefaultMushrooms(genSettings);
        DefaultBiomeFeatures.addDefaultVegetation(genSettings);
    }
    
    public static void addSwamplandFeatures(GenerationSettings.Builder genSettings, boolean pe) {
        addDefaultFeatures(genSettings, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(genSettings, false);
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_PE_SPARSE);
        } else {
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_BETA_SPARSE);
            genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.FLOWER_SWAMP);
            genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_WATERLILLY);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(genSettings);
        DefaultBiomeFeatures.addDefaultVegetation(genSettings);
    }
    
    public static void addTaigaFeatures(GenerationSettings.Builder genSettings, boolean pe) {
        addDefaultFeatures(genSettings, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(genSettings, true);
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_PE_TAIGA);
        } else {
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_BETA_TAIGA);
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_TAIGA_1);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(genSettings);
        DefaultBiomeFeatures.addDefaultVegetation(genSettings);
        if (!pe) DefaultBiomeFeatures.addSweetBerryBushes(genSettings);
    }
    
    public static void addTundraFeatures(GenerationSettings.Builder genSettings, boolean pe) {
        addDefaultFeatures(genSettings, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(genSettings, false);
        } else {
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(genSettings);
        DefaultBiomeFeatures.addDefaultVegetation(genSettings);
    }
    
    public static void addOceanFeatures(GenerationSettings.Builder genSettings, boolean pe) {
        addDefaultFeatures(genSettings, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(genSettings, false);
        } else {
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(genSettings);
        DefaultBiomeFeatures.addDefaultVegetation(genSettings);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.SEAGRASS_NORMAL);
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.SEAGRASS_SIMPLE);
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.KELP_COLD);
    }
    
    public static void addColdOceanFeatures(GenerationSettings.Builder genSettings, boolean pe) {
        addDefaultFeatures(genSettings, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(genSettings, false);
        } else {
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(genSettings);
        DefaultBiomeFeatures.addDefaultVegetation(genSettings);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.SEAGRASS_COLD);
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.SEAGRASS_SIMPLE);
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.KELP_COLD);
    }
    
    public static void addFrozenOceanFeatures(GenerationSettings.Builder genSettings, boolean pe) {
        addDefaultFeatures(genSettings, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(genSettings, false);
        } else {
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(genSettings);
        DefaultBiomeFeatures.addDefaultVegetation(genSettings);
    }
    
    public static void addLukewarmOceanFeatures(GenerationSettings.Builder genSettings, boolean pe) {
        addDefaultFeatures(genSettings, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(genSettings, false);
        } else {
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(genSettings);
        DefaultBiomeFeatures.addDefaultVegetation(genSettings);

        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.SEAGRASS_WARM);
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.KELP_WARM);
    }
    
    public static void addWarmOceanFeatures(GenerationSettings.Builder genSettings, boolean pe) {
        addDefaultFeatures(genSettings, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(genSettings, false);
        } else {
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(genSettings);
        DefaultBiomeFeatures.addDefaultVegetation(genSettings);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.WARM_OCEAN_VEGETATION);
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.SEAGRASS_WARM);
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.SEA_PICKLE);
    }
    
    /* Inf Biomes */
    
    public static void addAlphaFeatures(GenerationSettings.Builder genSettings) {
        addDefaultFeatures(genSettings, OldFeatureSettings.ALPHA);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_CACTUS_ALPHA);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_ALPHA_BEES);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_ALPHA_2);
        
        DefaultBiomeFeatures.addDefaultMushrooms(genSettings);
        DefaultBiomeFeatures.addDefaultVegetation(genSettings);
    }
    
    public static void addInfdev611Features(GenerationSettings.Builder genSettings) {
        addDefaultFeatures(genSettings, OldFeatureSettings.INFDEV_611);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_INFDEV_611_BEES);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_ALPHA_2);
    }
    
    public static void addInfdev420Features(GenerationSettings.Builder genSettings) {
        addDefaultFeatures(genSettings, OldFeatureSettings.INFDEV_420);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_INFDEV_420_BEES);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_ALPHA_2);
    }
    
    public static void addInfdev415Features(GenerationSettings.Builder genSettings) {
        addDefaultFeatures(genSettings, OldFeatureSettings.INFDEV_415);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_INFDEV_415_BEES);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_ALPHA_2);
    }
    
    public static void addInfdev227Features(GenerationSettings.Builder genSettings) {
        addDefaultFeatures(genSettings, OldFeatureSettings.INFDEV_227);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_ALPHA_2);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.FLOWER_INFDEV_227);
    }
    
    /* Indev Biomes */
    
    public static void addIndevHellFeatures(GenerationSettings.Builder genSettings) {
        addDefaultFeatures(genSettings, OldFeatureSettings.INDEV);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_INDEV_BEES);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_ALPHA_2);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.MUSHROOM_HELL);
    }
    
    public static void addIndevNormalFeatures(GenerationSettings.Builder genSettings) {
        addDefaultFeatures(genSettings, OldFeatureSettings.INDEV);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_INDEV_BEES);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_ALPHA_2);
    }
    
    public static void addIndevParadiseFeatures(GenerationSettings.Builder genSettings) {
        addDefaultFeatures(genSettings, OldFeatureSettings.INDEV);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.FLOWER_PARADISE);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_INDEV_BEES);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_ALPHA_2);
    }
    
    public static void addIndevSnowyFeatures(GenerationSettings.Builder genSettings) {
        addIndevNormalFeatures(genSettings);
    }
    
    public static void addIndevWoodsFeatures(GenerationSettings.Builder genSettings) {
        addDefaultFeatures(genSettings, OldFeatureSettings.INDEV);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_INDEV_WOODS_BEES);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_ALPHA_2);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.MUSHROOM_HELL);
    }
    
    public static void addDefaultFeatures(
        GenerationSettings.Builder genSettings,
        OldFeatureSettings featureSettings
    ) {
        addCarvers(genSettings, featureSettings.addCanyons);
        DefaultBiomeFeatures.addAmethystGeodes(genSettings);
        DefaultBiomeFeatures.addDungeons(genSettings);
        addMineables(genSettings, featureSettings.addAlternateStones, featureSettings.addNewMineables);
        
        if (featureSettings.addSprings) DefaultBiomeFeatures.addSprings(genSettings);
        if (featureSettings.useBetaFreezeTopLayer)
            OldBiomeFeatures.addBetaFrozenTopLayer(genSettings);
        else
            DefaultBiomeFeatures.addFrozenTopLayer(genSettings);
        
        DefaultBiomeFeatures.addDefaultOres(genSettings);
        addOres(genSettings);
    }
    
    public static void addDefaultFeatures(GenerationSettings.Builder genSettings, boolean isOcean, boolean addLakes, boolean addSprings) {
        //if (addLakes) DefaultBiomeFeatures.addDefaultLakes(genSettings);
        DefaultBiomeFeatures.addDungeons(genSettings);
        DefaultBiomeFeatures.addDefaultOres(genSettings);
        DefaultBiomeFeatures.addDefaultMushrooms(genSettings);
        if (addSprings) DefaultBiomeFeatures.addSprings(genSettings);
        DefaultBiomeFeatures.addAmethystGeodes(genSettings);
    }

    public static void addCarvers(GenerationSettings.Builder genSettings, boolean addCanyons) {
        genSettings.carver(GenerationStep.Carver.AIR, OldCarvers.CONF_OLD_BETA_CAVE_CARVER);
        genSettings.carver(GenerationStep.Carver.AIR, OldCarvers.CONF_OLD_BETA_CAVE_CARVER_DEEP);
        
        if (addCanyons) {
            genSettings.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.CANYON);
        }
    }
    
    public static void addOceanCarvers(GenerationSettings.Builder genSettings) {
        addCarvers(genSettings, true);
    }

    public static void addMineables(GenerationSettings.Builder genSettings, boolean addAlternateStones, boolean addNewMineables) {
        genSettings.feature(Feature.UNDERGROUND_ORES, ConfiguredFeatures.ORE_DIRT);
        genSettings.feature(Feature.UNDERGROUND_ORES, ConfiguredFeatures.ORE_GRAVEL);
        genSettings.feature(Feature.UNDERGROUND_ORES, OldConfiguredFeatures.ORE_CLAY);
        
        if (addAlternateStones) {
            genSettings.feature(GenerationStep.Feature.UNDERGROUND_ORES, ConfiguredFeatures.ORE_GRANITE_LOWER);
            genSettings.feature(GenerationStep.Feature.UNDERGROUND_ORES, ConfiguredFeatures.ORE_DIORITE_LOWER);
            genSettings.feature(GenerationStep.Feature.UNDERGROUND_ORES, ConfiguredFeatures.ORE_ANDESITE_LOWER);
        }
        
        if (addNewMineables) {
            genSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.GLOW_LICHEN);
            genSettings.feature(GenerationStep.Feature.UNDERGROUND_ORES, ConfiguredFeatures.ORE_TUFF);
        }
    }
    
    public static void addOres(GenerationSettings.Builder genSettings) {
        genSettings.feature(Feature.UNDERGROUND_ORES, OldConfiguredFeatures.ORE_EMERALD_Y95);
    }

    public static void addBetaFrozenTopLayer(GenerationSettings.Builder genSettings) {
        genSettings.feature(Feature.TOP_LAYER_MODIFICATION, OldConfiguredFeatures.BETA_FREEZE_TOP_LAYER);
    }
    
    private static void addPEVegetation(GenerationSettings.Builder genSettings, boolean addGrass) {
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION);
        genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        
        if (addGrass)
            genSettings.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_ALPHA_2);
    }
    
    private record OldFeatureSettings(
        boolean addCanyons,
        boolean addLakes,
        boolean addSprings,
        boolean addAlternateStones,
        boolean addNewMineables,
        boolean useBetaFreezeTopLayer
    ) {
        private OldFeatureSettings(boolean setting) {
            this(setting, setting, setting, setting, setting, setting);
        }
        
        private static final OldFeatureSettings BETA = new OldFeatureSettings(true);
        private static final OldFeatureSettings PE = new OldFeatureSettings(true, false, true, false, true, true);
        private static final OldFeatureSettings SKY = new OldFeatureSettings(false, true, true, false, false, true);
        private static final OldFeatureSettings ALPHA = new OldFeatureSettings(false, false, true, false, false, false);
        private static final OldFeatureSettings INFDEV_611 = new OldFeatureSettings(false);
        private static final OldFeatureSettings INFDEV_420 = new OldFeatureSettings(false);
        private static final OldFeatureSettings INFDEV_415 = new OldFeatureSettings(false);
        private static final OldFeatureSettings INFDEV_227 = new OldFeatureSettings(false);
        private static final OldFeatureSettings INDEV = new OldFeatureSettings(false);
    }
}
