package com.bespectacled.modernbeta.compat;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.biome.classic.ClassicBiomes;
import com.bespectacled.modernbeta.biome.indev.IndevBiomes;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class Compat {
    public static List<RegistryKey<Biome>> MODERN_BETA_BIOMES;
    
    public static void setupCompat() {
        try {
            if (FabricLoader.getInstance().isModLoaded("techreborn")) CompatTechReborn.addCompat();
            
        } catch (Exception e) {
            ModernBeta.LOGGER.log(Level.ERROR, "[Modern Beta] Something went wrong when attempting to add mod compatibility!");
            e.printStackTrace();
        }
    }
    
    static {
        MODERN_BETA_BIOMES = Stream.of(
            BetaBiomes.BIOME_KEYS, 
            ClassicBiomes.ALPHA_BIOME_KEYS, 
            ClassicBiomes.INFDEV_BIOME_KEYS,
            ClassicBiomes.INFDEV_OLD_BIOME_KEYS, 
            IndevBiomes.BIOME_KEYS
        ).flatMap(Collection::stream).collect(Collectors.toList());
    }
}
