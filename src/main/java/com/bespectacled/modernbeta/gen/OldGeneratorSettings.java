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
import com.bespectacled.modernbeta.biome.CaveBiomeType;
import com.bespectacled.modernbeta.config.ModernBetaBiomeConfig;
import com.bespectacled.modernbeta.config.ModernBetaGenerationConfig;
import com.bespectacled.modernbeta.util.BlockStates;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class OldGeneratorSettings {
    public static final ModernBetaGenerationConfig GEN_CONFIG = ModernBeta.BETA_CONFIG.generationConfig;
    public static final ModernBetaBiomeConfig BIOME_CONFIG = ModernBeta.BETA_CONFIG.biomeConfig;
    
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
            NbtCompound.CODEC.fieldOf("provider_settings").forGetter(settings -> settings.providerSettings)
        ).apply(instance, OldGeneratorSettings::new));

    public final Supplier<ChunkGeneratorSettings> generatorSettings;
    public NbtCompound providerSettings;

    public OldGeneratorSettings(Supplier<ChunkGeneratorSettings> generatorSettings, NbtCompound providerSettings) {
        this.generatorSettings = generatorSettings;
        this.providerSettings = providerSettings;
    }
    
    public static NbtCompound createBiomeSettings(BiomeType biomeType, CaveBiomeType caveBiomeType, Identifier singleBiome) {
        NbtCompound settings = new NbtCompound();
        
        settings.putString("biomeType", biomeType.getName());
        settings.putString("caveBiomeType", caveBiomeType.getName());
        settings.putString("singleBiome", singleBiome.toString());
        
        return settings;
    }
    
    public static NbtCompound createInfSettings(WorldType worldType) {
        NbtCompound settings = new NbtCompound();
        
        settings.putString("worldType", worldType.getName());
        settings.putBoolean("generateOceans", GEN_CONFIG.generateOceans);
        settings.putBoolean("generateNoiseCaves", GEN_CONFIG.generateNoiseCaves);
        settings.putBoolean("generateAquifers", GEN_CONFIG.generateAquifers);
        settings.putBoolean("generateDeepslate", GEN_CONFIG.generateDeepslate);
        
        return settings;
    }
    
    public static NbtCompound createIndevSettings() {
        NbtCompound settings = new NbtCompound();
        
        settings.putString("worldType", WorldType.INDEV.getName());
        settings.putString("levelType", GEN_CONFIG.indevLevelType);
        settings.putString("levelTheme", GEN_CONFIG.indevLevelTheme);
        settings.putInt("levelWidth", GEN_CONFIG.indevLevelWidth);
        settings.putInt("levelLength", GEN_CONFIG.indevLevelLength);
        settings.putInt("levelHeight", GEN_CONFIG.indevLevelHeight);
        settings.putFloat("caveRadius", GEN_CONFIG.indevCaveRadius);
        
        return settings;
    }
    
    public static NbtCompound addBetaBiomeSettings(NbtCompound settings) {
        settings.putString("desert", BIOME_CONFIG.betaDesertBiome);
        settings.putString("forest", BIOME_CONFIG.betaForestBiome);
        settings.putString("ice_desert", BIOME_CONFIG.betaIceDesertBiome);
        settings.putString("plains", BIOME_CONFIG.betaPlainsBiome);
        settings.putString("rainforest", BIOME_CONFIG.betaRainforestBiome);
        settings.putString("savanna", BIOME_CONFIG.betaSavannaBiome);
        settings.putString("shrubland", BIOME_CONFIG.betaShrublandBiome);
        settings.putString("seasonal_forest", BIOME_CONFIG.betaSeasonalForestBiome);
        settings.putString("swampland", BIOME_CONFIG.betaSwamplandBiome);
        settings.putString("taiga", BIOME_CONFIG.betaTaigaBiome);
        settings.putString("tundra", BIOME_CONFIG.betaTundraBiome);
        
        settings.putString("ocean", BIOME_CONFIG.betaOceanBiome);
        settings.putString("cold_ocean", BIOME_CONFIG.betaColdOceanBiome);
        settings.putString("frozen_ocean", BIOME_CONFIG.betaFrozenOceanBiome);
        settings.putString("lukewarm_ocean", BIOME_CONFIG.betaLukewarmOceanBiome);
        settings.putString("warm_ocean", BIOME_CONFIG.betaWarmOceanBiome);
        
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
        ALPHA_GENERATOR_SETTINGS = register(WorldType.ALPHA.getName(), new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.ALPHA_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false, true, true, false));
        SKYLANDS_GENERATOR_SETTINGS = register(WorldType.SKYLANDS.getName(), new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.SKYLANDS_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, -10, 0, false, true, false, false));
        INFDEV_415_GENERATOR_SETTINGS = register(WorldType.INFDEV_415.getName(), new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.INFDEV_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false, true, true, false));
        INFDEV_227_GENERATOR_SETTINGS = register(WorldType.INFDEV_227.getName(), new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.BETA_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false, false, false, false));
        INDEV_GENERATOR_SETTINGS = register(WorldType.INDEV.getName(), new ChunkGeneratorSettings(OldGeneratorConfig.INDEV_STRUCTURES, OldGeneratorConfig.INDEV_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false, false, false, false));
        //RELEASE_GENERATOR_SETTINGS = register(WorldType.RELEASE.getName(), new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.BETA_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false, true, true, true));
    }
}
