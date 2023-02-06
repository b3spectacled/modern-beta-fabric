package mod.bespectacled.modernbeta.world.biome;

import mod.bespectacled.modernbeta.world.carver.configured.ModernBetaConfiguredCarvers;
import mod.bespectacled.modernbeta.world.feature.placed.ModernBetaMiscPlacedFeatures;
import mod.bespectacled.modernbeta.world.feature.placed.ModernBetaOrePlacedFeatures;
import mod.bespectacled.modernbeta.world.feature.placed.ModernBetaVegetationPlacedFeatures;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.carver.ConfiguredCarvers;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.MiscPlacedFeatures;
import net.minecraft.world.gen.feature.OceanPlacedFeatures;
import net.minecraft.world.gen.feature.OrePlacedFeatures;
import net.minecraft.world.gen.feature.UndergroundPlacedFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;

public class ModernBetaBiomeFeatures {
    /*
    private static final boolean BETA_ADD_LAKES = true;
    private static final boolean BETA_ADD_SPRINGS = true;
    
    private static final boolean BETA_ADD_ALTERNATE_STONES = true;
    private static final boolean BETA_ADD_NEW_MINEABLES = true;

    private static final boolean ALPHA_ADD_LAKES = false;
    private static final boolean ALPHA_ADD_SPRINGS = true;
    
    private static final boolean ALPHA_ADD_ALTERNATE_STONES = false;
    private static final boolean ALPHA_ADD_NEW_MINEABLES = false;

    private static final boolean INFDEV_611_ADD_LAKES = false;
    private static final boolean INFDEV_611_ADD_SPRINGS = false;
    
    private static final boolean INFDEV_420_ADD_LAKES = false;
    private static final boolean INFDEV_420_ADD_SPRINGS = false;
    
    private static final boolean INFDEV_415_ADD_LAKES = false;
    private static final boolean INFDEV_415_ADD_SPRINGS = false;
    
    private static final boolean INFDEV_227_ADD_LAKES = false;
    private static final boolean INFDEV_227_ADD_SPRINGS = false;
    
    private static final boolean INDEV_ADD_LAKES = false;
    private static final boolean INDEV_ADD_SPRINGS = false;
    */
    
    /* Beta Biomes */
    
    public static void addDesertFeatures(GenerationSettings.LookupBackedBuilder builder, boolean pe) {
        DefaultBiomeFeatures.addFossils(builder);
        
        addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);
        
        DefaultBiomeFeatures.addDesertFeatures(builder);
        
        if (pe) {
            addPEVegetation(builder, false);
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_CACTUS_PE);
            
            DefaultBiomeFeatures.addDefaultMushrooms(builder);
            DefaultBiomeFeatures.addDefaultVegetation(builder);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
            builder.feature(Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.PATCH_DEAD_BUSH_2);

            DefaultBiomeFeatures.addDefaultMushrooms(builder);
            DefaultBiomeFeatures.addDefaultVegetation(builder);
            
            builder.feature(Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.PATCH_CACTUS_DESERT);
        }
    }
    
    public static void addForestFeatures(GenerationSettings.LookupBackedBuilder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, true);
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_PE_FOREST_BEES);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_2);
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_BETA_FOREST);
            
            builder.feature(Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.FOREST_FLOWERS);
            builder.feature(Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.PATCH_GRASS_FOREST);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
    }
    
    public static void addIceDesertFeatures(GenerationSettings.LookupBackedBuilder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, false);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
    }
    
    public static void addPlainsFeatures(GenerationSettings.LookupBackedBuilder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, true);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_3);
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_GRASS_PLAINS_10);
        }
        
        builder.feature(Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.FLOWER_PLAIN);

        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
    }
    
    public static void addRainforestFeatures(GenerationSettings.LookupBackedBuilder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, true);
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_PE_RAINFOREST_BEES);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_BETA_RAINFOREST);
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_GRASS_RAINFOREST_10);
            builder.feature(Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.FOREST_FLOWERS);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
        
        builder.feature(Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.PATCH_MELON_SPARSE);
    }
    
    public static void addSavannaFeatures(GenerationSettings.LookupBackedBuilder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, false);
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_PE_SPARSE);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_BETA_SPARSE);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
    }
    
    public static void addSeasonalForestFeatures(GenerationSettings.LookupBackedBuilder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, true);
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_PE_SEASONAL_FOREST_BEES);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_4);
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_BETA_SEASONAL_FOREST);
            builder.feature(Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.PATCH_GRASS_FOREST);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
    }
    
    public static void addShrublandFeatures(GenerationSettings.LookupBackedBuilder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, false);
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_PE_SPARSE);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_BETA_SPARSE);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
    }
    
    public static void addSkyFeatures(GenerationSettings.LookupBackedBuilder builder) {
        addDefaultFeatures(builder, ModernBetaFeatureSettings.SKY);
        
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_2);
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_BETA_SPARSE);
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
    }
    
    public static void addSwamplandFeatures(GenerationSettings.LookupBackedBuilder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, false);
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_PE_SPARSE);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_BETA_SPARSE);
            builder.feature(Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.FLOWER_SWAMP);
            builder.feature(Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.PATCH_WATERLILY);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
    }
    
    public static void addTaigaFeatures(GenerationSettings.LookupBackedBuilder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, true);
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_PE_TAIGA);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_2);
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_BETA_TAIGA);
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_GRASS_TAIGA_1);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
        if (!pe) DefaultBiomeFeatures.addSweetBerryBushes(builder);
    }
    
    public static void addTundraFeatures(GenerationSettings.LookupBackedBuilder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, false);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
    }
    
    public static void addOceanFeatures(GenerationSettings.LookupBackedBuilder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, false);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_2);
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
        
        builder.feature(Feature.VEGETAL_DECORATION, OceanPlacedFeatures.SEAGRASS_NORMAL);
        builder.feature(Feature.VEGETAL_DECORATION, OceanPlacedFeatures.SEAGRASS_SIMPLE);
        builder.feature(Feature.VEGETAL_DECORATION, OceanPlacedFeatures.KELP_COLD);
    }
    
    public static void addColdOceanFeatures(GenerationSettings.LookupBackedBuilder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, false);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_2);
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
        
        builder.feature(Feature.VEGETAL_DECORATION, OceanPlacedFeatures.SEAGRASS_COLD);
        builder.feature(Feature.VEGETAL_DECORATION, OceanPlacedFeatures.SEAGRASS_SIMPLE);
        builder.feature(Feature.VEGETAL_DECORATION, OceanPlacedFeatures.KELP_COLD);
    }
    
    public static void addFrozenOceanFeatures(GenerationSettings.LookupBackedBuilder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, false);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_2);
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
    }
    
    public static void addLukewarmOceanFeatures(GenerationSettings.LookupBackedBuilder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, false);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_2);
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);

        builder.feature(Feature.VEGETAL_DECORATION, OceanPlacedFeatures.SEAGRASS_WARM);
        builder.feature(Feature.VEGETAL_DECORATION, OceanPlacedFeatures.KELP_WARM);
    }
    
    public static void addWarmOceanFeatures(GenerationSettings.LookupBackedBuilder builder, boolean pe) {
        addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);
        
        if (pe) {
            addPEVegetation(builder, false);
        } else {
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_2);
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
        }
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
        
        builder.feature(Feature.VEGETAL_DECORATION, OceanPlacedFeatures.WARM_OCEAN_VEGETATION);
        builder.feature(Feature.VEGETAL_DECORATION, OceanPlacedFeatures.SEAGRASS_WARM);
        builder.feature(Feature.VEGETAL_DECORATION, OceanPlacedFeatures.SEA_PICKLE);
    }
    
    /* Inf Biomes */
    
    public static void addAlphaFeatures(GenerationSettings.LookupBackedBuilder builder) {
        addDefaultFeatures(builder, ModernBetaFeatureSettings.ALPHA);
        
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_2);
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_CACTUS_ALPHA);
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_ALPHA_BEES);
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_GRASS_ALPHA_2);
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultVegetation(builder);
    }
    
    public static void addInfdev611Features(GenerationSettings.LookupBackedBuilder builder) {
        addDefaultFeatures(builder, ModernBetaFeatureSettings.INFDEV_611);
        
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_INFDEV_611_BEES);
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_GRASS_ALPHA_2);
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
    }
    
    public static void addInfdev420Features(GenerationSettings.LookupBackedBuilder builder) {
        addDefaultFeatures(builder, ModernBetaFeatureSettings.INFDEV_420);
        
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_INFDEV_420_BEES);
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_GRASS_ALPHA_2);
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
    }
    
    public static void addInfdev415Features(GenerationSettings.LookupBackedBuilder builder) {
        addDefaultFeatures(builder, ModernBetaFeatureSettings.INFDEV_415);
        
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_INFDEV_415_BEES);
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_GRASS_ALPHA_2);
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
    }
    
    public static void addInfdev227Features(GenerationSettings.LookupBackedBuilder builder) {
        addDefaultFeatures(builder, ModernBetaFeatureSettings.INFDEV_227);
        
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_GRASS_ALPHA_2);
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_INFDEV_227);
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
    }
    
    /* Indev Biomes */
    
    public static void addIndevHellFeatures(GenerationSettings.LookupBackedBuilder builder) {
        addDefaultFeatures(builder, ModernBetaFeatureSettings.INDEV);
        
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_2);
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_INDEV_BEES);
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_GRASS_ALPHA_2);
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.MUSHROOM_HELL);
        
        DefaultBiomeFeatures.addDefaultMushrooms(builder);
    }
    
    public static void addIndevNormalFeatures(GenerationSettings.LookupBackedBuilder builder) {
        addDefaultFeatures(builder, ModernBetaFeatureSettings.INDEV);
        
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_2);
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_INDEV_BEES);
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_GRASS_ALPHA_2);

        DefaultBiomeFeatures.addDefaultMushrooms(builder);
    }
    
    public static void addIndevParadiseFeatures(GenerationSettings.LookupBackedBuilder builder) {
        addDefaultFeatures(builder, ModernBetaFeatureSettings.INDEV);
        
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_FLOWER_PARADISE);
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_INDEV_BEES);
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_GRASS_ALPHA_2);

        DefaultBiomeFeatures.addDefaultMushrooms(builder);
    }
    
    public static void addIndevSnowyFeatures(GenerationSettings.LookupBackedBuilder builder) {
        addIndevNormalFeatures(builder);
    }
    
    public static void addIndevWoodsFeatures(GenerationSettings.LookupBackedBuilder builder) {
        addDefaultFeatures(builder, ModernBetaFeatureSettings.INDEV);
        
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_2);
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_INDEV_WOODS_BEES);
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_GRASS_ALPHA_2);
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.MUSHROOM_HELL);
    }
    
    public static void addDefaultFeatures(
        GenerationSettings.LookupBackedBuilder builder,
        ModernBetaFeatureSettings featureSettings
    ) {
        addCarvers(builder, featureSettings.addCanyons);
        DefaultBiomeFeatures.addAmethystGeodes(builder);
        DefaultBiomeFeatures.addDungeons(builder);
        addMineables(builder, featureSettings.addClay, featureSettings.addAlternateStones, featureSettings.addNewMineables);
        
        if (featureSettings.addSprings) DefaultBiomeFeatures.addSprings(builder);
        if (featureSettings.useBetaFreezeTopLayer)
            ModernBetaBiomeFeatures.addBetaFrozenTopLayer(builder);
        else
            DefaultBiomeFeatures.addFrozenTopLayer(builder);
        
        DefaultBiomeFeatures.addDefaultOres(builder);
        addOres(builder);
    }
    
    public static void addDefaultFeatures(GenerationSettings.LookupBackedBuilder builder, boolean isOcean, boolean addLakes, boolean addSprings) {
        if (addLakes) addLakes(builder);
        DefaultBiomeFeatures.addDungeons(builder);
        DefaultBiomeFeatures.addDefaultOres(builder);
        if (addSprings) DefaultBiomeFeatures.addSprings(builder);
        DefaultBiomeFeatures.addAmethystGeodes(builder);
    }

    private static void addCarvers(GenerationSettings.LookupBackedBuilder builder, boolean addCanyons) {
        builder.carver(GenerationStep.Carver.AIR, ModernBetaConfiguredCarvers.BETA_CAVE);
        builder.carver(GenerationStep.Carver.AIR, ModernBetaConfiguredCarvers.BETA_CAVE_DEEP);
        
        if (addCanyons) {
            builder.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.CANYON);
        }
    }
    
    private static void addLakes(GenerationSettings.LookupBackedBuilder builder) {
        builder.feature(GenerationStep.Feature.LAKES, MiscPlacedFeatures.LAKE_LAVA_UNDERGROUND);
        builder.feature(GenerationStep.Feature.LAKES, MiscPlacedFeatures.LAKE_LAVA_SURFACE);
    }

    private static void addMineables(GenerationSettings.LookupBackedBuilder builder, boolean addClay, boolean addAlternateStones, boolean addNewMineables) {
        builder.feature(Feature.UNDERGROUND_ORES, OrePlacedFeatures.ORE_DIRT);
        builder.feature(Feature.UNDERGROUND_ORES, OrePlacedFeatures.ORE_GRAVEL);
        
        if (addClay) {
            builder.feature(Feature.UNDERGROUND_ORES, ModernBetaOrePlacedFeatures.ORE_CLAY);
        }
        
        if (addAlternateStones) {
            builder.feature(GenerationStep.Feature.UNDERGROUND_ORES, OrePlacedFeatures.ORE_GRANITE_LOWER);
            builder.feature(GenerationStep.Feature.UNDERGROUND_ORES, OrePlacedFeatures.ORE_DIORITE_LOWER);
            builder.feature(GenerationStep.Feature.UNDERGROUND_ORES, OrePlacedFeatures.ORE_ANDESITE_LOWER);
        }
        
        if (addNewMineables) {
            builder.feature(GenerationStep.Feature.UNDERGROUND_ORES, OrePlacedFeatures.ORE_TUFF);
            builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, UndergroundPlacedFeatures.GLOW_LICHEN);
        }
    }
    
    private static void addOres(GenerationSettings.LookupBackedBuilder builder) {
        builder.feature(Feature.UNDERGROUND_ORES, ModernBetaOrePlacedFeatures.ORE_EMERALD_Y95);
    }

    private static void addBetaFrozenTopLayer(GenerationSettings.LookupBackedBuilder builder) {
        builder.feature(Feature.TOP_LAYER_MODIFICATION, ModernBetaMiscPlacedFeatures.FREEZE_TOP_LAYER);
    }
    
    private static void addPEVegetation(GenerationSettings.LookupBackedBuilder builder, boolean addGrass) {
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION);
        builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
        
        if (addGrass)
            builder.feature(Feature.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_GRASS_ALPHA_2);
    }
    
    private record ModernBetaFeatureSettings(
        boolean addCanyons,
        boolean addLakes,
        boolean addSprings,
        boolean addClay,
        boolean addAlternateStones,
        boolean addNewMineables,
        boolean useBetaFreezeTopLayer
    ) {
        private ModernBetaFeatureSettings(boolean setting) {
            this(setting, setting, setting, setting, setting, setting, setting);
        }
        
        private static final ModernBetaFeatureSettings BETA = new ModernBetaFeatureSettings(true);
        private static final ModernBetaFeatureSettings PE = new ModernBetaFeatureSettings(true, false, true, true, false, true, true);
        private static final ModernBetaFeatureSettings SKY = new ModernBetaFeatureSettings(false, true, true, true, false, false, true);
        private static final ModernBetaFeatureSettings ALPHA = new ModernBetaFeatureSettings(false, false, true, false, false, false, false);
        private static final ModernBetaFeatureSettings INFDEV_611 = new ModernBetaFeatureSettings(false);
        private static final ModernBetaFeatureSettings INFDEV_420 = new ModernBetaFeatureSettings(false);
        private static final ModernBetaFeatureSettings INFDEV_415 = new ModernBetaFeatureSettings(false);
        private static final ModernBetaFeatureSettings INFDEV_227 = new ModernBetaFeatureSettings(false);
        private static final ModernBetaFeatureSettings INDEV = new ModernBetaFeatureSettings(false);
    }
}
