package com.bespectacled.modernbeta.biome;

import java.util.Arrays;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.types.templates.List;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.DefaultBiomeCreator;

public class IndevBiomes {
    public static final ImmutableList<Identifier> BIOMES = ImmutableList.of(
        new Identifier(ModernBeta.ID, "indev_edge"),
        new Identifier(ModernBeta.ID, "indev_hell_edge"),
        new Identifier(ModernBeta.ID, "indev_paradise_edge"),
        new Identifier(ModernBeta.ID, "indev_woods_edge"),
        new Identifier(ModernBeta.ID, "indev_snowy_edge"),
        
        new Identifier(ModernBeta.ID, "indev_normal"),
        new Identifier(ModernBeta.ID, "indev_hell"),
        new Identifier(ModernBeta.ID, "indev_paradise"),
        new Identifier(ModernBeta.ID, "indev_woods"),
        new Identifier(ModernBeta.ID, "indev_snowy")
    );

    public static void reserveBiomeIDs() {
        for (Identifier i : BIOMES) {
            Registry.register(BuiltinRegistries.BIOME, i, DefaultBiomeCreator.createNormalOcean(false));
        }

        ModernBeta.LOGGER.log(Level.INFO, "Reserved Indev biome IDs.");
    }

}
