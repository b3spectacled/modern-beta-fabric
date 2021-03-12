package com.bespectacled.modernbeta.biome;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.biome.classic.ClassicBiomes;
import com.bespectacled.modernbeta.biome.indev.IndevBiomes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class OldBiomes {
    public static Map<Identifier, Biome> MODERN_BETA_BIOME_MAP = new HashMap<Identifier, Biome>();
    
    public static Biome register(Identifier id, Biome biome) {
        MODERN_BETA_BIOME_MAP.put(id, biome);
        return Registry.register(BuiltinRegistries.BIOME, id, biome);
    }
    
    public static void register() {
        BetaBiomes.registerBiomes();
        ClassicBiomes.registerAlphaBiomes();
        ClassicBiomes.registerInfdev415Biomes();
        IndevBiomes.registerBiomes();
        ClassicBiomes.registerInfdev227Biomes();
    }
    
    public static void export() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path dir = Paths.get("..\\src\\main\\resources\\data\\modern_beta\\biome");
        
        for (Identifier i : MODERN_BETA_BIOME_MAP.keySet()) {
            Biome b = MODERN_BETA_BIOME_MAP.get(i);
            Function<Supplier<Biome>, DataResult<JsonElement>> toJson = JsonOps.INSTANCE.withEncoder(Biome.REGISTRY_CODEC);
            
            try {
                JsonElement json = toJson.apply(() -> b).result().get();
                Files.write(dir.resolve(i.getPath() + ".json"), gson.toJson(json).getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                ModernBeta.LOGGER.error("[Modern Beta] Couldn't serialize biomes!");
                e.printStackTrace();
            }
        }
    }
}
