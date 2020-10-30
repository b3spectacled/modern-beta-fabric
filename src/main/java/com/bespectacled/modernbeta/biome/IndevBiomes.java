package com.bespectacled.modernbeta.biome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;
import com.google.common.collect.ImmutableList;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
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
   
    public static List<RegistryKey<Biome>> getBiomeList() {
        ArrayList<RegistryKey<Biome>> biomeList = new ArrayList<RegistryKey<Biome>>();
        
        for (Identifier i : BIOMES) {
            biomeList.add(RegistryKey.of(Registry.BIOME_KEY, i));
        }
        
        return Collections.unmodifiableList(biomeList);
    }
}
