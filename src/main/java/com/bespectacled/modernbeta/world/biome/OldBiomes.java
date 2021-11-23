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
import net.minecraft.world.biome.Biome;

public class OldBiomes { 
    public static final Map<Identifier, Biome> MODERN_BETA_BIOME_MAP = new HashMap<Identifier, Biome>();
    
    public static Biome register(Identifier id, Biome biome) {
        MODERN_BETA_BIOME_MAP.put(id, biome);
        return Registry.register(BuiltinRegistries.BIOME, id, biome);
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
