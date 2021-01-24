package com.bespectacled.modernbeta.gen;

import java.util.Optional;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.WorldEnum;
import com.bespectacled.modernbeta.util.WorldEnum.BiomeType;
import com.bespectacled.modernbeta.util.WorldEnum.WorldType;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.StrongholdConfig;
import net.minecraft.world.gen.chunk.StructuresConfig;

public class OldGeneratorSettings {
    public static final StructuresConfig STRUCTURES;
    public static final Optional<StrongholdConfig> INDEV_STRONGHOLD;
    public static final StructuresConfig INDEV_STRUCTURES;
    
    public static final ChunkGeneratorSettings BETA_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings ALPHA_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings SKYLANDS_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings INFDEV_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings NETHER_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings INDEV_GENERATOR_SETTINGS;
    
    public static final OldGeneratorSettings BETA_SETTINGS;
    public static final OldGeneratorSettings SKYLANDS_SETTINGS;
    public static final OldGeneratorSettings ALPHA_SETTINGS;
    public static final OldGeneratorSettings INFDEV_SETTINGS;
    public static final OldGeneratorSettings INDEV_SETTINGS;
    public static final OldGeneratorSettings NETHER_SETTINGS;
    
    public static final Codec<OldGeneratorSettings> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(ChunkGeneratorSettings.CODEC.fieldOf("type").forGetter(settings -> settings.generatorSettings),
                    CompoundTag.CODEC.fieldOf("settings").forGetter(settings -> settings.providerSettings))
            .apply(instance, OldGeneratorSettings::new));

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
        
        settings.putString("worldType", WorldEnum.WorldType.INDEV.getName());
        settings.putString("levelType", ModernBeta.BETA_CONFIG.indevLevelType);
        settings.putString("levelTheme", ModernBeta.BETA_CONFIG.indevLevelTheme);
        settings.putInt("levelWidth", ModernBeta.BETA_CONFIG.indevLevelWidth);
        settings.putInt("levelLength", ModernBeta.BETA_CONFIG.indevLevelLength);
        settings.putInt("levelHeight", ModernBeta.BETA_CONFIG.indevLevelHeight);
        settings.putFloat("caveRadius", ModernBeta.BETA_CONFIG.indevCaveRadius);
        
        return settings;
    }
    
    public static ChunkGeneratorSettings getChunkGeneratorSettings(WorldType worldType) {
        ChunkGeneratorSettings settings;
        
        switch(worldType) {
            case BETA:
                settings = BETA_GENERATOR_SETTINGS;
                break;
            case ALPHA:
                settings = ALPHA_GENERATOR_SETTINGS;
                break;
            case SKYLANDS:
                settings = SKYLANDS_GENERATOR_SETTINGS;
                break;
            case INFDEV:
                settings = INFDEV_GENERATOR_SETTINGS;
                break;
            case INFDEV_OLD:
                settings = INFDEV_GENERATOR_SETTINGS;
                break;
            case INDEV:
                settings = INDEV_GENERATOR_SETTINGS;
                break;
            case NETHER:
                settings = NETHER_GENERATOR_SETTINGS;
                break;
            default:
                settings = BETA_GENERATOR_SETTINGS;
        }
        
        return settings;
    }
    
    static {
        boolean generateOceans = ModernBeta.BETA_CONFIG.generateOceans;
        
        STRUCTURES = new StructuresConfig(true);
        INDEV_STRONGHOLD = Optional.of(new StrongholdConfig(0, 0, 1));
        INDEV_STRUCTURES = new StructuresConfig(INDEV_STRONGHOLD, Maps.newHashMap(StructuresConfig.DEFAULT_STRUCTURES));

        BETA_GENERATOR_SETTINGS = new ChunkGeneratorSettings(STRUCTURES, OldGeneratorConfig.BETA_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false);
        ALPHA_GENERATOR_SETTINGS = new ChunkGeneratorSettings(STRUCTURES, OldGeneratorConfig.ALPHA_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false);
        SKYLANDS_GENERATOR_SETTINGS = new ChunkGeneratorSettings(STRUCTURES, OldGeneratorConfig.SKYLANDS_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, -10, 0, false);
        INFDEV_GENERATOR_SETTINGS = new ChunkGeneratorSettings(STRUCTURES, OldGeneratorConfig.INFDEV_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false);
        NETHER_GENERATOR_SETTINGS = new ChunkGeneratorSettings(STRUCTURES, OldGeneratorConfig.SKYLANDS_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, 0, 128, 32, false);
        INDEV_GENERATOR_SETTINGS = new ChunkGeneratorSettings(INDEV_STRUCTURES, OldGeneratorConfig.BETA_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, -10, 0, 64, false); 

        BETA_SETTINGS = new OldGeneratorSettings(OldGeneratorSettings.BETA_GENERATOR_SETTINGS, OldGeneratorSettings.createInfSettings(WorldType.BETA, BiomeType.BETA, generateOceans));
        SKYLANDS_SETTINGS = new OldGeneratorSettings(OldGeneratorSettings.SKYLANDS_GENERATOR_SETTINGS, OldGeneratorSettings.createInfSettings(WorldType.SKYLANDS, BiomeType.SKY, false));
        ALPHA_SETTINGS = new OldGeneratorSettings(OldGeneratorSettings.ALPHA_GENERATOR_SETTINGS, OldGeneratorSettings.createInfSettings(WorldType.ALPHA, BiomeType.CLASSIC, generateOceans));
        INFDEV_SETTINGS = new OldGeneratorSettings(OldGeneratorSettings.INFDEV_GENERATOR_SETTINGS, OldGeneratorSettings.createInfSettings(WorldType.INFDEV, BiomeType.CLASSIC, generateOceans));
        INDEV_SETTINGS = new OldGeneratorSettings(OldGeneratorSettings.INDEV_GENERATOR_SETTINGS, OldGeneratorSettings.createIndevSettings());
        NETHER_SETTINGS = new OldGeneratorSettings(OldGeneratorSettings.NETHER_GENERATOR_SETTINGS, OldGeneratorSettings.createInfSettings(WorldType.NETHER, BiomeType.BETA, false));
   
    }
}
