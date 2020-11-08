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
    public static final Identifier INDEV_EDGE_ID = new Identifier(ModernBeta.ID, "indev_edge");
    public static final Identifier INDEV_HELL_EDGE_ID = new Identifier(ModernBeta.ID, "indev_hell_edge");
    public static final Identifier INDEV_PARADISE_EDGE_ID = new Identifier(ModernBeta.ID, "indev_paradise_edge");
    public static final Identifier INDEV_WOODS_EDGE_ID = new Identifier(ModernBeta.ID, "indev_woods_edge");
    public static final Identifier INDEV_SNOWY_EDGE_ID = new Identifier(ModernBeta.ID, "indev_snowy_edge");
    
    public static final Identifier INDEV_NORMAL_ID = new Identifier(ModernBeta.ID, "indev_normal");
    public static final Identifier INDEV_HELL_ID = new Identifier(ModernBeta.ID, "indev_hell");
    public static final Identifier INDEV_PARADISE_ID = new Identifier(ModernBeta.ID, "indev_paradise");
    public static final Identifier INDEV_WOODS_ID = new Identifier(ModernBeta.ID, "indev_woods");
    public static final Identifier INDEV_SNOWY_ID = new Identifier(ModernBeta.ID, "indev_snowy");
    
    public static final ImmutableList<Identifier> BIOMES = ImmutableList.of(
        INDEV_EDGE_ID,
        INDEV_HELL_EDGE_ID,
        INDEV_PARADISE_EDGE_ID,
        INDEV_WOODS_EDGE_ID,
        INDEV_SNOWY_EDGE_ID,
        
        INDEV_NORMAL_ID,
        INDEV_HELL_ID,
        INDEV_PARADISE_ID,
        INDEV_WOODS_ID,
        INDEV_SNOWY_ID
    );

    public static void reserveBiomeIDs() {
        for (Identifier i : BIOMES) {
            Registry.register(BuiltinRegistries.BIOME, i, DefaultBiomeCreator.createNormalOcean(false));
        }

        //ModernBeta.LOGGER.log(Level.INFO, "Reserved Indev biome IDs.");
    }
   
    public static List<RegistryKey<Biome>> getBiomeList() {
        ArrayList<RegistryKey<Biome>> biomeList = new ArrayList<RegistryKey<Biome>>();
        
        for (Identifier i : BIOMES) {
            biomeList.add(RegistryKey.of(Registry.BIOME_KEY, i));
        }
        
        return Collections.unmodifiableList(biomeList);
    }
}
