package com.bespectacled.modernbeta.biome;

import java.util.ArrayList;
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

public class BetaBiomes {
    public static final ImmutableList<Identifier> BIOMES = ImmutableList.of(
        new Identifier(ModernBeta.ID, "forest"),
        new Identifier(ModernBeta.ID, "shrubland"), 
        new Identifier(ModernBeta.ID, "desert"),
        new Identifier(ModernBeta.ID, "savanna"), 
        new Identifier(ModernBeta.ID, "plains"),
        new Identifier(ModernBeta.ID, "seasonal_forest"), 
        new Identifier(ModernBeta.ID, "rainforest"),
        new Identifier(ModernBeta.ID, "swampland"), 
        new Identifier(ModernBeta.ID, "taiga"),
        new Identifier(ModernBeta.ID, "tundra"), 
        new Identifier(ModernBeta.ID, "ice_desert"),

        new Identifier(ModernBeta.ID, "ocean"), 
        new Identifier(ModernBeta.ID, "lukewarm_ocean"),
        new Identifier(ModernBeta.ID, "warm_ocean"), 
        new Identifier(ModernBeta.ID, "cold_ocean"),
        new Identifier(ModernBeta.ID, "frozen_ocean"),

        new Identifier(ModernBeta.ID, "sky"));

    public static void reserveBiomeIDs() {
        for (Identifier i : BIOMES) {
            Registry.register(BuiltinRegistries.BIOME, i, DefaultBiomeCreator.createNormalOcean(false));
        }

        ModernBeta.LOGGER.log(Level.INFO, "Reserved Beta biome IDs.");
    }
    
    public static List<RegistryKey<Biome>> getBiomeList() {
        ArrayList<RegistryKey<Biome>> biomeList = new ArrayList<RegistryKey<Biome>>();
        
        for (Identifier i : BIOMES) {
            biomeList.add(RegistryKey.of(Registry.BIOME_KEY, i));
        }
        
        return Collections.unmodifiableList(biomeList);
    }

}
