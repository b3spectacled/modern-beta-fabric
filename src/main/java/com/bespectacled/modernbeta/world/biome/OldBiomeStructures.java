package com.bespectacled.modernbeta.world.biome;

import java.util.function.BiConsumer;

import com.bespectacled.modernbeta.mixin.MixinConfiguredStructureFeaturesAccessor;
import com.bespectacled.modernbeta.world.structure.OldStructures;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;

public class OldBiomeStructures {
    private static RegistryKey<Biome> biomeKeyOf(Identifier id) {
        return RegistryKey.of(Registry.BIOME_KEY, id);
    }
    
    private static void addDefaultUndergroundStructures(BiConsumer<ConfiguredStructureFeature<?, ?>, RegistryKey<Biome>> consumer, Identifier id) {
        consumer.accept(ConfiguredStructureFeatures.STRONGHOLD, biomeKeyOf(id));
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getMineshaft(), biomeKeyOf(id));
    }
    
    private static void addDefaultOceanStructures(BiConsumer<ConfiguredStructureFeature<?, ?>, RegistryKey<Biome>> consumer, Identifier id) {
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getShipwreck(), biomeKeyOf(id));
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getMineshaft(), biomeKeyOf(id));
    }
    
    public static void addCommonStructures(BiConsumer<ConfiguredStructureFeature<?, ?>, RegistryKey<Biome>> consumer, Identifier id) {
        addDefaultUndergroundStructures(consumer, id);
        
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getVillagePlains(), biomeKeyOf(id));
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getPillagerOutpost(), biomeKeyOf(id));
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getRuinedPortal(), biomeKeyOf(id));
    }
    
    public static void addOceanStructures(BiConsumer<ConfiguredStructureFeature<?, ?>, RegistryKey<Biome>> consumer, Identifier id, boolean warm) {
        if (warm) {
            consumer.accept(MixinConfiguredStructureFeaturesAccessor.getOceanRuinWarm(), biomeKeyOf(id));
        } else {
            consumer.accept(MixinConfiguredStructureFeaturesAccessor.getOceanRuinCold(), biomeKeyOf(id));
        }
        
        addDefaultOceanStructures(consumer, id);
        
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getBuriedTreasure(), biomeKeyOf(id));
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getRuinedPortalOcean(), biomeKeyOf(id));
        consumer.accept(OldStructures.CONF_OCEAN_SHRINE_STRUCTURE, biomeKeyOf(id));
    }
    
    public static void addDesertStructures(BiConsumer<ConfiguredStructureFeature<?, ?>, RegistryKey<Biome>> consumer, Identifier id, boolean addVillages) {
        if (addVillages) {
            consumer.accept(MixinConfiguredStructureFeaturesAccessor.getVillageDesert(), biomeKeyOf(id));
        }

        addDefaultUndergroundStructures(consumer, id);
        
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getRuinedPortalDesert(), biomeKeyOf(id));
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getDesertPyramid(), biomeKeyOf(id));
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getPillagerOutpost(), biomeKeyOf(id));
    }
    
    public static void addForestStructures(BiConsumer<ConfiguredStructureFeature<?, ?>, RegistryKey<Biome>> consumer, Identifier id) {
        addDefaultUndergroundStructures(consumer, id);
        
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getMansion(), biomeKeyOf(id));
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getRuinedPortal(), biomeKeyOf(id));
    }
    
    public static void addPlainsStructures(BiConsumer<ConfiguredStructureFeature<?, ?>, RegistryKey<Biome>> consumer, Identifier id) {
        addDefaultUndergroundStructures(consumer, id);
        
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getPillagerOutpost(), biomeKeyOf(id));
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getRuinedPortal(), biomeKeyOf(id));
    }
    
    public static void addRainforestStructures(BiConsumer<ConfiguredStructureFeature<?, ?>, RegistryKey<Biome>> consumer, Identifier id) {
        addDefaultUndergroundStructures(consumer, id);
        
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getJunglePyramid(), biomeKeyOf(id));
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getRuinedPortalJungle(), biomeKeyOf(id));
    }
    
    public static void addSeasonalForest(BiConsumer<ConfiguredStructureFeature<?, ?>, RegistryKey<Biome>> consumer, Identifier id) {
        addDefaultUndergroundStructures(consumer, id);
        
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getRuinedPortal(), biomeKeyOf(id));
    }
    
    public static void addLowlandStructures(BiConsumer<ConfiguredStructureFeature<?, ?>, RegistryKey<Biome>> consumer, Identifier id) {
        addDefaultUndergroundStructures(consumer, id);
        
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getVillagePlains(),biomeKeyOf(id));
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getPillagerOutpost(), biomeKeyOf(id));
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getRuinedPortal(), biomeKeyOf(id));
    }
    
    public static void addSwamplandStructures(BiConsumer<ConfiguredStructureFeature<?, ?>, RegistryKey<Biome>> consumer, Identifier id) {
        addDefaultUndergroundStructures(consumer, id);
        
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getSwampHut(), biomeKeyOf(id));
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getRuinedPortalSwamp(), biomeKeyOf(id));
    }
    
    public static void addTaigaStructures(BiConsumer<ConfiguredStructureFeature<?, ?>, RegistryKey<Biome>> consumer, Identifier id) {
        addDefaultUndergroundStructures(consumer, id);
        
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getVillageTaiga(), biomeKeyOf(id));
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getIgloo(),biomeKeyOf(id));
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getPillagerOutpost(), biomeKeyOf(id));
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getRuinedPortalMountain(), biomeKeyOf(id));
    }
    
    public static void addTundraStructures(BiConsumer<ConfiguredStructureFeature<?, ?>, RegistryKey<Biome>> consumer, Identifier id) {
        addDefaultUndergroundStructures(consumer, id);
        
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getVillageSnowy(), biomeKeyOf(id));
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getIgloo(),biomeKeyOf(id));
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getPillagerOutpost(), biomeKeyOf(id));
        consumer.accept(MixinConfiguredStructureFeaturesAccessor.getRuinedPortal(), biomeKeyOf(id));
    }
}
