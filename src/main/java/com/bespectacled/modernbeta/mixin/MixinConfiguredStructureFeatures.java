package com.bespectacled.modernbeta.mixin;

import java.util.function.BiConsumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.world.biome.OldBiomeStructures;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;

@Mixin(ConfiguredStructureFeatures.class)
public class MixinConfiguredStructureFeatures {
    @Inject(method = "registerAll", at = @At("TAIL"))
    private static void addStructuresToBiomes(BiConsumer<ConfiguredStructureFeature<?, ?>, RegistryKey<Biome>> consumer, CallbackInfo info) {
        /* Beta Biomes */
        
        OldBiomeStructures.addOceanStructures(consumer, ModernBeta.createId("cold_ocean"), false);
        OldBiomeStructures.addDesertStructures(consumer, ModernBeta.createId("desert"), true);
        OldBiomeStructures.addForestStructures(consumer, ModernBeta.createId("forest"));
        OldBiomeStructures.addOceanStructures(consumer, ModernBeta.createId("frozen_ocean"), false);
        OldBiomeStructures.addDesertStructures(consumer, ModernBeta.createId("ice_desert"), false);
        OldBiomeStructures.addOceanStructures(consumer, ModernBeta.createId("lukewarm_ocean"), true);
        OldBiomeStructures.addOceanStructures(consumer, ModernBeta.createId("ocean"), false);
        OldBiomeStructures.addPlainsStructures(consumer, ModernBeta.createId("plains"));
        OldBiomeStructures.addRainforestStructures(consumer, ModernBeta.createId("rainforest"));
        OldBiomeStructures.addLowlandStructures(consumer, ModernBeta.createId("savanna"));
        OldBiomeStructures.addSeasonalForest(consumer, ModernBeta.createId("seasonal_forest"));
        OldBiomeStructures.addLowlandStructures(consumer, ModernBeta.createId("shrubland"));
        OldBiomeStructures.addSwamplandStructures(consumer, ModernBeta.createId("swampland"));
        OldBiomeStructures.addTaigaStructures(consumer, ModernBeta.createId("taiga"));
        OldBiomeStructures.addTundraStructures(consumer, ModernBeta.createId("tundra"));
        OldBiomeStructures.addOceanStructures(consumer, ModernBeta.createId("warm_ocean"), true);
        
        OldBiomeStructures.addCommonStructures(consumer, ModernBeta.createId("sky"));
        
        /* PE Biomes */
        
        OldBiomeStructures.addOceanStructures(consumer, ModernBeta.createId("pe_cold_ocean"), false);
        OldBiomeStructures.addDesertStructures(consumer, ModernBeta.createId("pe_desert"), true);
        OldBiomeStructures.addForestStructures(consumer, ModernBeta.createId("pe_forest"));
        OldBiomeStructures.addOceanStructures(consumer, ModernBeta.createId("pe_frozen_ocean"), false);
        OldBiomeStructures.addDesertStructures(consumer, ModernBeta.createId("pe_ice_desert"), false);
        OldBiomeStructures.addOceanStructures(consumer, ModernBeta.createId("pe_lukewarm_ocean"), true);
        OldBiomeStructures.addOceanStructures(consumer, ModernBeta.createId("pe_ocean"), false);
        OldBiomeStructures.addPlainsStructures(consumer, ModernBeta.createId("pe_plains"));
        OldBiomeStructures.addRainforestStructures(consumer, ModernBeta.createId("pe_rainforest"));
        OldBiomeStructures.addLowlandStructures(consumer, ModernBeta.createId("pe_savanna"));
        OldBiomeStructures.addSeasonalForest(consumer, ModernBeta.createId("pe_seasonal_forest"));
        OldBiomeStructures.addLowlandStructures(consumer, ModernBeta.createId("pe_shrubland"));
        OldBiomeStructures.addSwamplandStructures(consumer, ModernBeta.createId("pe_swampland"));
        OldBiomeStructures.addTaigaStructures(consumer, ModernBeta.createId("pe_taiga"));
        OldBiomeStructures.addTundraStructures(consumer, ModernBeta.createId("pe_tundra"));
        OldBiomeStructures.addOceanStructures(consumer, ModernBeta.createId("pe_warm_ocean"), true);
        
        /* Inf Biomes */
        
        OldBiomeStructures.addCommonStructures(consumer, ModernBeta.createId("alpha"));
        OldBiomeStructures.addCommonStructures(consumer, ModernBeta.createId("alpha_winter"));
        
        OldBiomeStructures.addCommonStructures(consumer, ModernBeta.createId("infdev_227"));
        OldBiomeStructures.addCommonStructures(consumer, ModernBeta.createId("infdev_227_winder"));
        
        OldBiomeStructures.addCommonStructures(consumer, ModernBeta.createId("infdev_415"));
        OldBiomeStructures.addCommonStructures(consumer, ModernBeta.createId("infdev_415_winter"));

        OldBiomeStructures.addCommonStructures(consumer, ModernBeta.createId("infdev_420"));
        OldBiomeStructures.addCommonStructures(consumer, ModernBeta.createId("infdev_420_winter"));

        OldBiomeStructures.addCommonStructures(consumer, ModernBeta.createId("infdev_611"));
        OldBiomeStructures.addCommonStructures(consumer, ModernBeta.createId("infdev_611_winter"));
        
        
        /* Indev Biomes */
        
        OldBiomeStructures.addCommonStructures(consumer, ModernBeta.createId("indev_normal"));
        OldBiomeStructures.addCommonStructures(consumer, ModernBeta.createId("indev_hell"));
        OldBiomeStructures.addCommonStructures(consumer, ModernBeta.createId("indev_paradise"));
        OldBiomeStructures.addCommonStructures(consumer, ModernBeta.createId("indev_snowy"));
        OldBiomeStructures.addCommonStructures(consumer, ModernBeta.createId("indev_woods"));
    }
    
    
}
