package com.bespectacled.modernbeta.world.gen;

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
import com.bespectacled.modernbeta.api.registry.ChunkProviderRegistry.BuiltInChunkType;
import com.bespectacled.modernbeta.util.BlockStates;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class OldChunkGeneratorSettings {
    public static final ChunkGeneratorSettings BETA_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings ALPHA_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings SKYLANDS_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings INFDEV_415_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings INFDEV_227_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings INDEV_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings RELEASE_GENERATOR_SETTINGS;
    
    public static final Map<Identifier, ChunkGeneratorSettings> SETTINGS_MAP = new HashMap<Identifier, ChunkGeneratorSettings>();

    public static void register() {
        register(BuiltInChunkType.BETA.id, BETA_GENERATOR_SETTINGS);
        register(BuiltInChunkType.SKYLANDS.id, SKYLANDS_GENERATOR_SETTINGS);
        register(BuiltInChunkType.ALPHA.id, ALPHA_GENERATOR_SETTINGS);
        register(BuiltInChunkType.INFDEV_415.id, INFDEV_415_GENERATOR_SETTINGS);
        register(BuiltInChunkType.INFDEV_227.id, INFDEV_227_GENERATOR_SETTINGS);
        register(BuiltInChunkType.INDEV.id, INDEV_GENERATOR_SETTINGS);
        register("release", RELEASE_GENERATOR_SETTINGS);
    }
    
    private static ChunkGeneratorSettings register(String id, ChunkGeneratorSettings settings) {
        SETTINGS_MAP.put(ModernBeta.createId(id), settings);
        BuiltinRegistries.<ChunkGeneratorSettings, ChunkGeneratorSettings>add(BuiltinRegistries.CHUNK_GENERATOR_SETTINGS, ModernBeta.createId(id), settings);
        return settings;
    }
    
    public static void export() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path dir = Paths.get("..\\src\\main\\resources\\data\\modern_beta\\noise_settings");
        
        for (Identifier i : SETTINGS_MAP.keySet()) {
            ChunkGeneratorSettings s = SETTINGS_MAP.get(i);
            Function<Supplier<ChunkGeneratorSettings>, DataResult<JsonElement>> toJson = JsonOps.INSTANCE.withEncoder(ChunkGeneratorSettings.REGISTRY_CODEC);
            
            try {
                JsonElement json = toJson.apply(() -> s).result().get();
                Files.write(dir.resolve(i.getPath() + ".json"), gson.toJson(json).getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                ModernBeta.LOGGER.error("[Modern Beta] Couldn't serialize chunk generator settings!");
                e.printStackTrace();
            }
        }
    }
    
    static {
        BETA_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.BETA_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false, true, true, true);
        ALPHA_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.ALPHA_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false, true, true, false);
        SKYLANDS_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.SKYLANDS_SHAPE_CONFIG, BlockStates.STONE, BlockStates.AIR, -10, -10, 0, false, true, false, false);
        INFDEV_415_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.INFDEV_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false, true, true, false);
        INFDEV_227_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.BETA_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false, false, false, false);
        INDEV_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.INDEV_STRUCTURES, OldGeneratorConfig.INDEV_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false, false, false, false);
        RELEASE_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.BETA_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false, true, true, true);
    }
}
