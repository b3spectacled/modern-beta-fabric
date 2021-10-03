package com.bespectacled.modernbeta.world.biome;

import com.bespectacled.modernbeta.world.structure.OldStructures;

import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;

public class OldBiomeStructures {
    public static void addCommonStructures(GenerationSettings.Builder genSettings) {
        ////genSettings.structureFeature(ConfiguredStructureFeatures.VILLAGE_PLAINS);
        ////genSettings.structureFeature(ConfiguredStructureFeatures.PILLAGER_OUTPOST);
        ////genSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL);
    }
    
    public static void addOceanStructures(GenerationSettings.Builder genSettings, boolean warm) {
        if (warm); 
            //genSettings.structureFeature(ConfiguredStructureFeatures.OCEAN_RUIN_WARM);
        else;
            //genSettings.structureFeature(ConfiguredStructureFeatures.OCEAN_RUIN_COLD);
        
        //genSettings.structureFeature(ConfiguredStructureFeatures.BURIED_TREASURE);
        //genSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL_OCEAN);
        //genSettings.structureFeature(OldStructures.CONF_OCEAN_SHRINE_STRUCTURE);
    }
    
    public static void addDesertStructures(GenerationSettings.Builder genSettings, boolean addVillages) {
        if (addVillages);
            //genSettings.structureFeature(ConfiguredStructureFeatures.VILLAGE_DESERT);
        
        //genSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL_DESERT);
        //genSettings.structureFeature(ConfiguredStructureFeatures.DESERT_PYRAMID);
        //genSettings.structureFeature(ConfiguredStructureFeatures.PILLAGER_OUTPOST);
    }
    
    public static void addForestStructures(GenerationSettings.Builder genSettings) {
        //genSettings.structureFeature(ConfiguredStructureFeatures.MANSION);
        //genSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL);
    }
    
    public static void addPlainsStructures(GenerationSettings.Builder genSettings) {
        //genSettings.structureFeature(ConfiguredStructureFeatures.PILLAGER_OUTPOST);
        //genSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL);
    }
    
    public static void addRainforestStructures(GenerationSettings.Builder genSettings) {
        //genSettings.structureFeature(ConfiguredStructureFeatures.JUNGLE_PYRAMID);
        //genSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL_JUNGLE);
    }
    
    public static void addSeasonalForestStructures(GenerationSettings.Builder genSettings) {
        //genSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL);
    }
    
    public static void addLowlandStructures(GenerationSettings.Builder genSettings) {
        //genSettings.structureFeature(ConfiguredStructureFeatures.VILLAGE_PLAINS);
        //genSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL);
        //genSettings.structureFeature(ConfiguredStructureFeatures.PILLAGER_OUTPOST);
    }
    
    public static void addSwamplandStructures(GenerationSettings.Builder genSettings) {
        //genSettings.structureFeature(ConfiguredStructureFeatures.SWAMP_HUT);
        //genSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL_SWAMP);
    }
    
    public static void addTaigaStructures(GenerationSettings.Builder genSettings) {
        //genSettings.structureFeature(ConfiguredStructureFeatures.VILLAGE_TAIGA);
        //genSettings.structureFeature(ConfiguredStructureFeatures.IGLOO);
        //genSettings.structureFeature(ConfiguredStructureFeatures.PILLAGER_OUTPOST);
        //genSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL_MOUNTAIN);
    }
    
    public static void addTundraStructures(GenerationSettings.Builder genSettings) {
        //genSettings.structureFeature(ConfiguredStructureFeatures.VILLAGE_SNOWY);
        //genSettings.structureFeature(ConfiguredStructureFeatures.IGLOO);
        //genSettings.structureFeature(ConfiguredStructureFeatures.PILLAGER_OUTPOST);
        //genSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL);
    }
}
