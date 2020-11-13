package com.bespectacled.modernbeta.biome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.util.BiomeUtil;
import com.google.common.collect.ImmutableList;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeCreator;

public class AlphaBiomes {
    public static final Identifier ALPHA_ID = new Identifier(ModernBeta.ID, "alpha");
    public static final Identifier ALPHA_WINTER_ID = new Identifier(ModernBeta.ID, "alpha_winter");
    
    public static final ImmutableList<Identifier> BIOMES = ImmutableList.of(
        ALPHA_ID,
        ALPHA_WINTER_ID
    );

    public static void reserveBiomeIDs() {
        for (Identifier i : BIOMES) {
            Registry.register(BuiltinRegistries.BIOME, i, DefaultBiomeCreator.createNormalOcean(false));
        }

        //ModernBeta.LOGGER.log(Level.INFO, "Reserved Alpha biome IDs.");
    }
    
    public static List<RegistryKey<Biome>> getBiomeList(boolean useVanillaBiomes) {
        ArrayList<RegistryKey<Biome>> biomeList = new ArrayList<RegistryKey<Biome>>();
        
        if (useVanillaBiomes) {
            return BiomeUtil.VANILLA_BIOMES;
        }
        
        for (Identifier i : BIOMES) {
            biomeList.add(RegistryKey.of(Registry.BIOME_KEY, i));
        }
        
        return Collections.unmodifiableList(biomeList);
    }

}
