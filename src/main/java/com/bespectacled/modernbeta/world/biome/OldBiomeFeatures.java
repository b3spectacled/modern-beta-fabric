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
    
    public static void addDesertFeatures(GenerationSettings.Builder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        DefaultBiomeFeatures.addDesertFeatures(builder);
        DefaultBiomeFeatures.addFossils(builder);
        
        if (pe) {
            addPEVegetation(builder, false);
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_CACTUS_PE);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
            builder.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_DEAD_BUSH_2);
            builder.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_CACTUS_DESERT);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
    }
    
    public static void addForestFeatures(GenerationSettings.Builder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, true);
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_PE_FOREST_BEES);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_BETA_FOREST);
            
            builder.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.FOREST_FLOWER_VEGETATION);
            builder.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_GRASS_FOREST);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
    }
    
    public static void addIceDesertFeatures(GenerationSettings.Builder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, false);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
    }
    
    public static void addPlainsFeatures(GenerationSettings.Builder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, true);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_3);
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_PLAINS_10);
        }
        
        builder.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.FLOWER_PLAIN_DECORATED);
    }
    
    public static void addRainforestFeatures(GenerationSettings.Builder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, true);
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_PE_RAINFOREST_BEES);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_BETA_RAINFOREST);
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_RAINFOREST_10);
            builder.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.FOREST_FLOWER_VEGETATION);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
        
        builder.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_MELON);
    }
    
    public static void addSavannaFeatures(GenerationSettings.Builder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, false);
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_PE_SPARSE);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_BETA_SPARSE);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
    }
    
    public static void addSeasonalForestFeatures(GenerationSettings.Builder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, true);
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_PE_SEASONAL_FOREST_BEES);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_4);
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_BETA_SEASONAL_FOREST);
            builder.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_GRASS_FOREST);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
    }
    
    public static void addShrublandFeatures(GenerationSettings.Builder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, false);
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_PE_SPARSE);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_BETA_SPARSE);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
    }
    
    public static void addSkyFeatures(GenerationSettings.Builder builder) {
        addDefaultFeatures(builder, OldFeatureSettings.SKY);
        
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_BETA_SPARSE);
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
    }
    
    public static void addSwamplandFeatures(GenerationSettings.Builder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, false);
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_PE_SPARSE);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_BETA_SPARSE);
            builder.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.FLOWER_SWAMP);
            builder.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_WATERLILLY);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
    }
    
    public static void addTaigaFeatures(GenerationSettings.Builder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, true);
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_PE_TAIGA);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_BETA_TAIGA);
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_TAIGA_1);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
        if (!pe) DefaultBiomeFeatures.addSweetBerryBushes(builder);
    }
    
    public static void addTundraFeatures(GenerationSettings.Builder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, false);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
    }
    
    public static void addOceanFeatures(GenerationSettings.Builder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, false);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
        
        builder.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.SEAGRASS_NORMAL);
        builder.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.SEAGRASS_SIMPLE);
        builder.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.KELP_COLD);
    }
    
    public static void addColdOceanFeatures(GenerationSettings.Builder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, false);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
        
        builder.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.SEAGRASS_COLD);
        builder.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.SEAGRASS_SIMPLE);
        builder.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.KELP_COLD);
    }
    
    public static void addFrozenOceanFeatures(GenerationSettings.Builder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, false);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
    }
    
    public static void addLukewarmOceanFeatures(GenerationSettings.Builder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, false);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);

        builder.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.SEAGRASS_WARM);
        builder.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.KELP_WARM);
    }
    
    public static void addWarmOceanFeatures(GenerationSettings.Builder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? OldFeatureSettings.PE : OldFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, false);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
        
        builder.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.WARM_OCEAN_VEGETATION);
        builder.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.SEAGRASS_WARM);
        builder.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.SEA_PICKLE);
    }
    
    /* Inf Biomes */
    
    public static void addAlphaFeatures(GenerationSettings.Builder builder) {
        addDefaultFeatures(builder, OldFeatureSettings.ALPHA);
        
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_CACTUS_ALPHA);
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_ALPHA_BEES);
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_ALPHA_2);
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
    }
    
    public static void addInfdev611Features(GenerationSettings.Builder builder) {
        addDefaultFeatures(builder, OldFeatureSettings.INFDEV_611);
        
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_INFDEV_611_BEES);
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_ALPHA_2);
    }
    
    public static void addInfdev420Features(GenerationSettings.Builder builder) {
        addDefaultFeatures(builder, OldFeatureSettings.INFDEV_420);
        
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_INFDEV_420_BEES);
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_ALPHA_2);
    }
    
    public static void addInfdev415Features(GenerationSettings.Builder builder) {
        addDefaultFeatures(builder, OldFeatureSettings.INFDEV_415);
        
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_INFDEV_415_BEES);
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_ALPHA_2);
    }
    
    public static void addInfdev227Features(GenerationSettings.Builder builder) {
        addDefaultFeatures(builder, OldFeatureSettings.INFDEV_227);
        
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_ALPHA_2);
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.FLOWER_INFDEV_227);
    }
    
    /* Indev Biomes */
    
    public static void addIndevHellFeatures(GenerationSettings.Builder builder) {
        addDefaultFeatures(builder, OldFeatureSettings.INDEV);
        
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_INDEV_BEES);
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_ALPHA_2);
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.MUSHROOM_HELL);
    }
    
    public static void addIndevNormalFeatures(GenerationSettings.Builder builder) {
        addDefaultFeatures(builder, OldFeatureSettings.INDEV);
        
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_INDEV_BEES);
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_ALPHA_2);
    }
    
    public static void addIndevParadiseFeatures(GenerationSettings.Builder builder) {
        addDefaultFeatures(builder, OldFeatureSettings.INDEV);
        
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.FLOWER_PARADISE);
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_INDEV_BEES);
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_ALPHA_2);
    }
    
    public static void addIndevSnowyFeatures(GenerationSettings.Builder builder) {
        addIndevNormalFeatures(builder);
    }
    
    public static void addIndevWoodsFeatures(GenerationSettings.Builder builder) {
        addDefaultFeatures(builder, OldFeatureSettings.INDEV);
        
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION_2);
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.TREES_INDEV_WOODS_BEES);
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_ALPHA_2);
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.MUSHROOM_HELL);
    }
    
    public static void addDefaultFeatures(
        GenerationSettings.Builder builder,
        OldFeatureSettings featureSettings
    ) {
        addCarvers(builder, featureSettings.addCanyons);
        DefaultBiomeFeatures.addAmethystGeodes(builder);
        DefaultBiomeFeatures.addDungeons(builder);
        addMineables(builder, featureSettings.addAlternateStones, featureSettings.addNewMineables);
        
        if (featureSettings.addSprings) DefaultBiomeFeatures.addSprings(builder);
        if (featureSettings.useBetaFreezeTopLayer)
            OldBiomeFeatures.addBetaFrozenTopLayer(builder);
        else
            DefaultBiomeFeatures.addFrozenTopLayer(builder);
        
        DefaultBiomeFeatures.addDefaultOres(builder);
        addOres(builder);
    }
    
    public static void addDefaultFeatures(GenerationSettings.Builder builder, boolean isOcean, boolean addLakes, boolean addSprings) {
        if (addLakes) addLakes(builder);
        DefaultBiomeFeatures.addDungeons(builder);
        DefaultBiomeFeatures.addDefaultOres(builder);
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        if (addSprings) DefaultBiomeFeatures.addSprings(builder);
        DefaultBiomeFeatures.addAmethystGeodes(builder);
    }

    private static void addCarvers(GenerationSettings.Builder builder, boolean addCanyons) {
        builder.carver(GenerationStep.Carver.AIR, OldCarvers.CONF_OLD_BETA_CAVE_CARVER);
        builder.carver(GenerationStep.Carver.AIR, OldCarvers.CONF_OLD_BETA_CAVE_CARVER_DEEP);
        
        if (addCanyons) {
            builder.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.CANYON);
        }
    }
    
    private static void addLakes(GenerationSettings.Builder builder) {
        builder.feature(GenerationStep.Feature.LAKES, ConfiguredFeatures.LAKE_LAVA);
    }

    private static void addMineables(GenerationSettings.Builder builder, boolean addAlternateStones, boolean addNewMineables) {
        builder.feature(Feature.UNDERGROUND_ORES, ConfiguredFeatures.ORE_DIRT);
        builder.feature(Feature.UNDERGROUND_ORES, ConfiguredFeatures.ORE_GRAVEL);
        builder.feature(Feature.UNDERGROUND_ORES, OldConfiguredFeatures.ORE_CLAY);
        
        if (addAlternateStones) {
            builder.feature(GenerationStep.Feature.UNDERGROUND_ORES, ConfiguredFeatures.ORE_GRANITE_LOWER);
            builder.feature(GenerationStep.Feature.UNDERGROUND_ORES, ConfiguredFeatures.ORE_DIORITE_LOWER);
            builder.feature(GenerationStep.Feature.UNDERGROUND_ORES, ConfiguredFeatures.ORE_ANDESITE_LOWER);
        }
        
        if (addNewMineables) {
            builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.GLOW_LICHEN);
            builder.feature(GenerationStep.Feature.UNDERGROUND_ORES, ConfiguredFeatures.ORE_TUFF);
        }
    }
    
    private static void addOres(GenerationSettings.Builder builder) {
        builder.feature(Feature.UNDERGROUND_ORES, OldConfiguredFeatures.ORE_EMERALD_Y95);
    }

    private static void addBetaFrozenTopLayer(GenerationSettings.Builder builder) {
        builder.feature(Feature.TOP_LAYER_MODIFICATION, OldConfiguredFeatures.BETA_FREEZE_TOP_LAYER);
    }
    
    private static void addPEVegetation(GenerationSettings.Builder builder, boolean addGrass) {
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_DANDELION);
        builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_POPPY);
        
        if (addGrass)
            builder.feature(Feature.VEGETAL_DECORATION, OldConfiguredFeatures.PATCH_GRASS_ALPHA_2);
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
