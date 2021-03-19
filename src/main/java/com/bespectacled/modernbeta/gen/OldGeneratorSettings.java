package com.bespectacled.modernbeta.gen;

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
import com.bespectacled.modernbeta.biome.BiomeType;
import com.bespectacled.modernbeta.util.BlockStates;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class OldGeneratorSettings {
    
    public static final ChunkGeneratorSettings BETA_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings ALPHA_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings SKYLANDS_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings INFDEV_415_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings INFDEV_227_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings INDEV_GENERATOR_SETTINGS;
    //public static final ChunkGeneratorSettings RELEASE_GENERATOR_SETTINGS;
    
    public static final Map<Identifier, ChunkGeneratorSettings> SETTINGS_MAP = new HashMap<Identifier, ChunkGeneratorSettings>();
    
    public static final Codec<OldGeneratorSettings> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("settings").forGetter(settings -> settings.generatorSettings),
            CompoundTag.CODEC.fieldOf("provider_settings").forGetter(settings -> settings.providerSettings)
        ).apply(instance, OldGeneratorSettings::new));

    public final Supplier<ChunkGeneratorSettings> generatorSettings;
    public CompoundTag providerSettings;

    public OldGeneratorSettings(Supplier<ChunkGeneratorSettings> generatorSettings, CompoundTag providerSettings) {
        this.generatorSettings = generatorSettings;
        this.providerSettings = providerSettings;
    }
    
    public static CompoundTag createBiomeSettings(BiomeType biomeType) {
        CompoundTag settings = new CompoundTag();
        
        settings.putString("biomeType", biomeType.getName());
        
        return settings;
    }
    
    public static CompoundTag createInfSettings(
        WorldType worldType,
        boolean generateOceans, 
        boolean generateNoiseCaves, 
        boolean generateAquifers, 
        boolean generateDeepslate
    ) {
        CompoundTag settings = new CompoundTag();
        
        settings.putString("worldType", worldType.getName());
        settings.putBoolean("generateOceans", generateOceans);
        settings.putBoolean("generateNoiseCaves", generateNoiseCaves);
        settings.putBoolean("generateAquifers", generateAquifers);
        settings.putBoolean("generateDeepslate", generateDeepslate);
        
        return settings;
    }
    
    public static CompoundTag createIndevSettings() {
        CompoundTag settings = new CompoundTag();
        
        settings.putString("worldType", WorldType.INDEV.getName());
        settings.putString("levelType", ModernBeta.BETA_CONFIG.indevLevelType);
        settings.putString("levelTheme", ModernBeta.BETA_CONFIG.indevLevelTheme);
        settings.putInt("levelWidth", ModernBeta.BETA_CONFIG.indevLevelWidth);
        settings.putInt("levelLength", ModernBeta.BETA_CONFIG.indevLevelLength);
        settings.putInt("levelHeight", ModernBeta.BETA_CONFIG.indevLevelHeight);
        settings.putFloat("caveRadius", ModernBeta.BETA_CONFIG.indevCaveRadius);
        
        return settings;
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
        BETA_GENERATOR_SETTINGS = register(WorldType.BETA.getName(), new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.BETA_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false, true, true, true));
        ALPHA_GENERATOR_SETTINGS = register(WorldType.ALPHA.getName(), new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.ALPHA_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false, true, true, true));
        SKYLANDS_GENERATOR_SETTINGS = register(WorldType.SKYLANDS.getName(), new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.SKYLANDS_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, -10, 0, false, true, false, false));
        INFDEV_415_GENERATOR_SETTINGS = register(WorldType.INFDEV_415.getName(), new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.INFDEV_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false, true, true, true));
        INFDEV_227_GENERATOR_SETTINGS = register(WorldType.INFDEV_227.getName(), new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.BETA_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false, true, true, true));
        INDEV_GENERATOR_SETTINGS = register(WorldType.INDEV.getName(), new ChunkGeneratorSettings(OldGeneratorConfig.INDEV_STRUCTURES, OldGeneratorConfig.INDEV_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false, true, true, false));
        //RELEASE_GENERATOR_SETTINGS = register(WorldType.RELEASE.getName(), new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.BETA_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false, true, true, true));
    }
}
