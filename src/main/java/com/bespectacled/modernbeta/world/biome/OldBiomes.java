package com.bespectacled.modernbeta.world.biome;

import java.util.HashMap;
import java.util.Map;

import com.bespectacled.modernbeta.world.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.world.biome.indev.IndevBiomes;
import com.bespectacled.modernbeta.world.biome.inf.InfBiomes;
import com.bespectacled.modernbeta.world.biome.pe.PEBiomes;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class OldBiomes { 
    public static final Map<RegistryKey<Biome>, Biome> MODERN_BETA_BIOME_MAP = new HashMap<>();
    
    public static void register(RegistryKey<Biome> key, Biome biome) {
        MODERN_BETA_BIOME_MAP.put(key, biome);
        Registry.register(BuiltinRegistries.BIOME, key, biome);
    }
    
    public static RegistryKey<Biome> register(Identifier id) {
        return RegistryKey.of(Registry.BIOME_KEY, id);
    }
    
    public static void register() {
        BetaBiomes.registerBiomes();
        InfBiomes.registerAlphaBiomes();
        InfBiomes.registerInfdev415Biomes();
        InfBiomes.registerInfdev227Biomes();
        IndevBiomes.registerBiomes();
        PEBiomes.registerBiomes();
        InfBiomes.registerInfdev611Biomes();
        InfBiomes.registerInfdev420Biomes();
    }
}
