package com.bespectacled.modernbeta.mixin;

import java.util.function.BiConsumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bespectacled.modernbeta.world.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.world.structure.OldStructures;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;

@Mixin(ConfiguredStructureFeatures.class)
public class MixinConfiguredStructureFeatures {
    @Inject(method = "registerAll", at = @At("TAIL"))
    private static void addStructuresToBiomes(BiConsumer<ConfiguredStructureFeature<?, ?>, RegistryKey<Biome>> consumer, CallbackInfo info) {
        //consumer.accept(StructureTest.CONFIGURED_STRUCTURE, BiomeKeys.PLAINS);
        
        /* Beta Biomes */
        
        // Cold Ocean
        /*
        consumer.accept(ConfiguredStructureFeatures.BURIED_TREASURE, BetaBiomes.COLD_OCEAN_ID);
        consumer.accept(ConfiguredStructureFeatures.OCEAN_RUIN_COLD, BetaBiomes.COLD_OCEAN_ID);
        consumer.accept(ConfiguredStructureFeatures.RUINED_PORTAL_OCEAN, BetaBiomes.COLD_OCEAN_ID);
        consumer.accept(OldStructures.CONF_OCEAN_SHRINE_STRUCTURE, BetaBiomes.COLD_OCEAN_ID);
        */
        
        // PE Biomes
        
        // Inf Biomes
        
        // Indev Biomes
    }
}
