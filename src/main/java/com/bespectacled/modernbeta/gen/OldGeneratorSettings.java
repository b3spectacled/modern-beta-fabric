package com.bespectacled.modernbeta.gen;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.BiomeType;
import com.bespectacled.modernbeta.util.BlockStates;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class OldGeneratorSettings {
    
    public static final ChunkGeneratorSettings BETA_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings ALPHA_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings SKYLANDS_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings INFDEV_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings INDEV_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings NETHER_GENERATOR_SETTINGS;
    
    public static final OldGeneratorSettings BETA_SETTINGS;
    public static final OldGeneratorSettings SKYLANDS_SETTINGS;
    public static final OldGeneratorSettings ALPHA_SETTINGS;
    public static final OldGeneratorSettings INFDEV_SETTINGS;
    public static final OldGeneratorSettings INFDEV_OLD_SETTINGS;
    public static final OldGeneratorSettings INDEV_SETTINGS;
    public static final OldGeneratorSettings NETHER_SETTINGS;
    public static final OldGeneratorSettings FLAT_SETTINGS;
    
    public static final Codec<OldGeneratorSettings> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ChunkGeneratorSettings.CODEC.fieldOf("type").forGetter(settings -> settings.generatorSettings),
            CompoundTag.CODEC.fieldOf("settings").forGetter(settings -> settings.providerSettings)
        ).apply(instance, OldGeneratorSettings::new));

    public final ChunkGeneratorSettings generatorSettings;
    public CompoundTag providerSettings;

    public OldGeneratorSettings(ChunkGeneratorSettings generatorSettings, CompoundTag providerSettings) {
        this.generatorSettings = generatorSettings;
        this.providerSettings = providerSettings;
    }
    
    public static CompoundTag createInfSettings(WorldType worldType, BiomeType biomeType, boolean generateOceans) {
        CompoundTag settings = new CompoundTag();
        
        settings.putString("worldType", worldType.getName());
        settings.putString("biomeType", biomeType.getName());
        settings.putBoolean("generateOceans", generateOceans);
        
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
    
    public static ChunkGeneratorSettings getChunkGeneratorSettings(WorldType worldType) {
        switch(worldType) {
            case BETA:
                return BETA_GENERATOR_SETTINGS;
            case ALPHA:
                return ALPHA_GENERATOR_SETTINGS;
            case SKYLANDS:
                return SKYLANDS_GENERATOR_SETTINGS;
            case INFDEV:
                return INFDEV_GENERATOR_SETTINGS;
            case INFDEV_OLD:
                return INFDEV_GENERATOR_SETTINGS;
            case INDEV:
                return INDEV_GENERATOR_SETTINGS;
            //case NETHER:
            //    return NETHER_GENERATOR_SETTINGS;
            default:
                return BETA_GENERATOR_SETTINGS;
        }
    }
    
    static {
        boolean generateOceans = ModernBeta.BETA_CONFIG.generateOceans;
        
        BETA_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.BETA_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false);
        ALPHA_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.ALPHA_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false);
        SKYLANDS_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.SKYLANDS_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, -10, 0, false);
        INFDEV_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.INFDEV_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false);
        INDEV_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.INDEV_STRUCTURES, OldGeneratorConfig.INDEV_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false); 
        NETHER_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.NETHER_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, 128, 0, 32, false);

        BETA_SETTINGS = new OldGeneratorSettings(OldGeneratorSettings.BETA_GENERATOR_SETTINGS, OldGeneratorSettings.createInfSettings(WorldType.BETA, BiomeType.BETA, generateOceans));
        SKYLANDS_SETTINGS = new OldGeneratorSettings(OldGeneratorSettings.SKYLANDS_GENERATOR_SETTINGS, OldGeneratorSettings.createInfSettings(WorldType.SKYLANDS, BiomeType.SKY, false));
        ALPHA_SETTINGS = new OldGeneratorSettings(OldGeneratorSettings.ALPHA_GENERATOR_SETTINGS, OldGeneratorSettings.createInfSettings(WorldType.ALPHA, BiomeType.CLASSIC, generateOceans));
        INFDEV_SETTINGS = new OldGeneratorSettings(OldGeneratorSettings.INFDEV_GENERATOR_SETTINGS, OldGeneratorSettings.createInfSettings(WorldType.INFDEV, BiomeType.CLASSIC, generateOceans));
        INFDEV_OLD_SETTINGS = new OldGeneratorSettings(OldGeneratorSettings.INFDEV_GENERATOR_SETTINGS, OldGeneratorSettings.createInfSettings(WorldType.INFDEV_OLD, BiomeType.CLASSIC, generateOceans));
        INDEV_SETTINGS = new OldGeneratorSettings(OldGeneratorSettings.INDEV_GENERATOR_SETTINGS, OldGeneratorSettings.createIndevSettings());
        NETHER_SETTINGS = new OldGeneratorSettings(OldGeneratorSettings.NETHER_GENERATOR_SETTINGS, OldGeneratorSettings.createInfSettings(WorldType.NETHER, BiomeType.BETA, false));
        FLAT_SETTINGS = new OldGeneratorSettings(OldGeneratorSettings.BETA_GENERATOR_SETTINGS, OldGeneratorSettings.createInfSettings(WorldType.FLAT, BiomeType.BETA, false));
    }
}
