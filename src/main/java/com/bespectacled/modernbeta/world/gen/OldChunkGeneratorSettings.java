package com.bespectacled.modernbeta.world.gen;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import com.bespectacled.modernbeta.util.BlockStates;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.random.ChunkRandom;

public class OldChunkGeneratorSettings {
    public static final ChunkRandom.RandomProvider RANDOM_TYPE = ChunkRandom.RandomProvider.LEGACY;

    public static final Identifier BETA;
    public static final Identifier ALPHA;
    public static final Identifier SKYLANDS;
    public static final Identifier INFDEV_611;
    public static final Identifier INFDEV_420;
    public static final Identifier INFDEV_415;
    public static final Identifier INFDEV_227;
    public static final Identifier INDEV;
    public static final Identifier CLASSIC_0_30;
    public static final Identifier BETA_ISLANDS;
    public static final Identifier PE;
    
    public static final ChunkGeneratorSettings BETA_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings ALPHA_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings SKYLANDS_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings INFDEV_611_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings INFDEV_420_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings INFDEV_415_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings INFDEV_227_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings INDEV_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings CLASSIC_0_30_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings BETA_ISLANDS_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings PE_GENERATOR_SETTINGS;

    public static void register() {
        register(BETA, BETA_GENERATOR_SETTINGS);
        register(SKYLANDS, SKYLANDS_GENERATOR_SETTINGS);
        register(ALPHA, ALPHA_GENERATOR_SETTINGS);
        register(INFDEV_611, INFDEV_611_GENERATOR_SETTINGS);
        register(INFDEV_420, INFDEV_420_GENERATOR_SETTINGS);
        register(INFDEV_415, INFDEV_415_GENERATOR_SETTINGS);
        register(INFDEV_227, INFDEV_227_GENERATOR_SETTINGS);
        register(INDEV, INDEV_GENERATOR_SETTINGS);
        register(CLASSIC_0_30, CLASSIC_0_30_GENERATOR_SETTINGS);
        register(BETA_ISLANDS, BETA_ISLANDS_GENERATOR_SETTINGS);
        register(PE, PE_GENERATOR_SETTINGS);
    }
    
    private static ChunkGeneratorSettings register(Identifier id, ChunkGeneratorSettings settings) {
        BuiltinRegistries.add(BuiltinRegistries.CHUNK_GENERATOR_SETTINGS, id, settings);
        return settings;
    }
    
    static {
        BETA = ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.BETA.name);
        ALPHA = ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.ALPHA.name);
        SKYLANDS = ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.SKYLANDS.name);
        INFDEV_611 = ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.INFDEV_611.name);
        INFDEV_420 = ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.INFDEV_420.name);
        INFDEV_415 = ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.INFDEV_415.name);
        INFDEV_227 = ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.INFDEV_227.name);
        INDEV = ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.INDEV.name);
        CLASSIC_0_30 = ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.CLASSIC_0_30.name);
        BETA_ISLANDS = ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.BETA_ISLANDS.name);
        PE = ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.PE.name);

        BETA_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.BETA_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, OldSurfaceRules.createVanilla(true), 64, false, true, true, true, true, true);
        ALPHA_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.ALPHA_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, OldSurfaceRules.createVanilla(true), 64, false, true, true, true, true, true);
        SKYLANDS_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.SKYLANDS_SHAPE_CONFIG, BlockStates.STONE, BlockStates.AIR, OldSurfaceRules.createVanilla(false), 0, false, false, false, true, true, true);
        INFDEV_611_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.INFDEV_611_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, OldSurfaceRules.createVanilla(true), 64, false, true, true, true, true, true);
        INFDEV_420_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.INFDEV_420_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, OldSurfaceRules.createVanilla(true), 64, false, true, true, true, true, true);
        INFDEV_415_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.INFDEV_415_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, OldSurfaceRules.createVanilla(true), 64, false, true, true, true, true, true);
        INFDEV_227_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.BETA_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, OldSurfaceRules.createVanilla(true), 64, false, false, false, false, false, true);
        INDEV_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.INDEV_STRUCTURES, OldGeneratorConfig.INDEV_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, OldSurfaceRules.createVanilla(true), 64, false, false, false, false, false, true);
        CLASSIC_0_30_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.INDEV_STRUCTURES, OldGeneratorConfig.INDEV_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, OldSurfaceRules.createVanilla(true), 64, false, false, false, false, false, true);
        BETA_ISLANDS_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.BETA_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, OldSurfaceRules.createVanilla(true), 64, false, true, true, true, true, true);
        PE_GENERATOR_SETTINGS = new ChunkGeneratorSettings(OldGeneratorConfig.STRUCTURES, OldGeneratorConfig.PE_SHAPE_CONFIG, BlockStates.STONE, BlockStates.WATER, OldSurfaceRules.createVanilla(true), 64, false, true, true, true, true, true);
    }
}
