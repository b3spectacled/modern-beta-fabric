package com.bespectacled.modernbeta.gen;

import java.util.Optional;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.gui.CustomizeAlphaLevelScreen;
import com.bespectacled.modernbeta.gui.CustomizeBetaLevelScreen;
import com.bespectacled.modernbeta.gui.CustomizeIndevLevelScreen;
import com.bespectacled.modernbeta.gui.CustomizeInfdevLevelScreen;
import com.bespectacled.modernbeta.gui.CustomizeInfdevOldLevelScreen;
import com.bespectacled.modernbeta.gui.CustomizeSkylandsLevelScreen;
import com.bespectacled.modernbeta.mixin.client.MixinGeneratorTypeAccessor;
import com.bespectacled.modernbeta.util.WorldEnum.BiomeType;
import com.bespectacled.modernbeta.util.WorldEnum.WorldType;
import com.google.common.collect.ImmutableMap;

import net.minecraft.client.world.GeneratorType;
import net.minecraft.client.world.GeneratorType.ScreenProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class OldGeneratorType {

    private static final GeneratorType BETA;
    private static final GeneratorType SKYLANDS;
    private static final GeneratorType ALPHA;
    private static final GeneratorType INFDEV;
    private static final GeneratorType INFDEV_OLD;
    private static final GeneratorType INDEV;
    
    private static OldGeneratorSettings INF_SETTINGS = new OldGeneratorSettings(new CompoundTag(), false);
    private static OldGeneratorSettings INDEV_SETTINGS = new OldGeneratorSettings(new CompoundTag(), true);
    
    public static void register() {
        register(BETA);
        register(SKYLANDS);
        register(ALPHA);
        register(INFDEV);
        register(INFDEV_OLD);
        register(INDEV);
    }
    
    private static void register(GeneratorType type) {
        MixinGeneratorTypeAccessor.getValues().add(type);
    }
    
    static {
        BETA = new GeneratorType("beta") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
                INF_SETTINGS.providerSettings = OldGeneratorSettings.createInfSettings(WorldType.BETA, BiomeType.BETA, ModernBeta.BETA_CONFIG.generateOceans);
                return new OldChunkGenerator(new OldBiomeSource(seed, biomes, INF_SETTINGS.providerSettings), seed, INF_SETTINGS);
            }
        };
        
        SKYLANDS = new GeneratorType("skylands") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
                INF_SETTINGS.providerSettings = OldGeneratorSettings.createInfSettings(WorldType.SKYLANDS, BiomeType.SKY, false);
                return new OldChunkGenerator(new OldBiomeSource(seed, biomes, INF_SETTINGS.providerSettings), seed, INF_SETTINGS);
            }
        };
        
        ALPHA = new GeneratorType("alpha") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
                INF_SETTINGS.providerSettings = OldGeneratorSettings.createInfSettings(WorldType.ALPHA, BiomeType.CLASSIC, ModernBeta.BETA_CONFIG.generateOceans);
                return new OldChunkGenerator(new OldBiomeSource(seed, biomes, INF_SETTINGS.providerSettings), seed, INF_SETTINGS);
            }
        };
        
        INFDEV = new GeneratorType("infdev") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
                INF_SETTINGS.providerSettings = OldGeneratorSettings.createInfSettings(WorldType.INFDEV, BiomeType.CLASSIC, ModernBeta.BETA_CONFIG.generateOceans);
                return new OldChunkGenerator(new OldBiomeSource(seed, biomes, INF_SETTINGS.providerSettings), seed, INF_SETTINGS);
            }
        };
        
        INFDEV_OLD = new GeneratorType("infdev_old") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
                INF_SETTINGS.providerSettings = OldGeneratorSettings.createInfSettings(WorldType.INFDEV_OLD, BiomeType.CLASSIC, ModernBeta.BETA_CONFIG.generateOceans);
                return new OldChunkGenerator(new OldBiomeSource(seed, biomes, INF_SETTINGS.providerSettings), seed, INF_SETTINGS);
            }
        };
          
        INDEV = new GeneratorType("indev") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
                INDEV_SETTINGS.providerSettings = OldGeneratorSettings.createIndevSettings();
                return new OldChunkGenerator(new OldBiomeSource(seed, biomes, INDEV_SETTINGS.providerSettings), seed, INDEV_SETTINGS);
            }
        };
        
        MixinGeneratorTypeAccessor.setScreenProviders(
            new ImmutableMap.Builder<Optional<GeneratorType>, ScreenProvider>()
                .putAll(MixinGeneratorTypeAccessor.getScreenProviders())
                .put(
                    Optional.<GeneratorType>of(BETA), (createWorldScreen, generatorSettings) -> {
                        return new CustomizeBetaLevelScreen(createWorldScreen, INF_SETTINGS);
                    }
                )
                .put(
                    Optional.<GeneratorType>of(SKYLANDS), (createWorldScreen, generatorSettings) -> {
                        return new CustomizeSkylandsLevelScreen(createWorldScreen, INF_SETTINGS);
                    }
                )
                .put(
                    Optional.<GeneratorType>of(ALPHA), (createWorldScreen, generatorSettings) -> {
                        return new CustomizeAlphaLevelScreen(createWorldScreen, INF_SETTINGS);
                    }
                )
                .put(
                    Optional.<GeneratorType>of(INFDEV), (createWorldScreen, generatorSettings) -> {
                        return new CustomizeInfdevLevelScreen(createWorldScreen, INF_SETTINGS);
                    }
                )
                .put(
                    Optional.<GeneratorType>of(INFDEV_OLD), (createWorldScreen, generatorSettings) -> {
                        return new CustomizeInfdevOldLevelScreen(createWorldScreen, INF_SETTINGS);
                    }
                )
                .put(
                    Optional.<GeneratorType>of(INDEV), (createWorldScreen, generatorSettings) -> {
                        return new CustomizeIndevLevelScreen(createWorldScreen, INDEV_SETTINGS);
                    }
                )
                .build()
        );
    }
}
