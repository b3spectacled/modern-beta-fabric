package com.bespectacled.modernbeta.world.biome;

import java.util.HashMap;
import java.util.Map;

import com.bespectacled.modernbeta.world.biome.alpha.AlphaBiomes;
import com.bespectacled.modernbeta.world.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.world.biome.indev.IndevBiomes;
import com.bespectacled.modernbeta.world.biome.infdev.InfdevBiomes;
import com.bespectacled.modernbeta.world.biome.pe.PEBiomes;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class ModernBetaBiomes { 
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
        AlphaBiomes.registerBiomes();
        InfdevBiomes.registerBiomes();
        IndevBiomes.registerBiomes();
        PEBiomes.registerBiomes();
    }
}
