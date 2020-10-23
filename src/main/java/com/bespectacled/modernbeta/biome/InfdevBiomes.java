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

public class InfdevBiomes {
    public static final ImmutableList<Identifier> BIOMES = ImmutableList.of(
        new Identifier(ModernBeta.ID, "infdev"),
        new Identifier(ModernBeta.ID, "infdev_winter")
    );

    public static void reserveBiomeIDs() {
        for (Identifier i : BIOMES) {
            Registry.register(BuiltinRegistries.BIOME, i, DefaultBiomeCreator.createNormalOcean(false));
        }

        ModernBeta.LOGGER.log(Level.INFO, "Reserved Infdev biome IDs.");
    }

}
