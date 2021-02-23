package com.bespectacled.modernbeta.gen;

import java.util.Optional;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.BiomeType;
import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.gui.IndevCustomizeLevelScreen;
import com.bespectacled.modernbeta.gui.InfCustomizeLevelScreen;
import com.bespectacled.modernbeta.gui.InfdevOldCustomizeLevelScreen;
import com.bespectacled.modernbeta.mixin.client.MixinGeneratorTypeAccessor;
import com.google.common.collect.ImmutableMap;

import net.minecraft.client.world.GeneratorType;
import net.minecraft.client.world.GeneratorType.ScreenProvider;
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
    private static final GeneratorType NETHER;
    private static final GeneratorType FLAT;
    
    public static void register() {
        register(BETA);
        register(SKYLANDS);
        register(ALPHA);
        register(INFDEV);
        register(INFDEV_OLD);
        register(INDEV);
        //register(NETHER);
        //register(FLAT);
    }
    
    private static void register(GeneratorType type) {
        MixinGeneratorTypeAccessor.getValues().add(type);
    }
    
    static {
        BETA = new GeneratorType("beta") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
                OldGeneratorSettings.BETA_SETTINGS.providerSettings = OldGeneratorSettings.createInfSettings(WorldType.BETA, BiomeType.BETA, ModernBeta.BETA_CONFIG.generateOceans);
                return new OldChunkGenerator(new OldBiomeSource(seed, biomes, OldGeneratorSettings.BETA_SETTINGS.providerSettings), seed, OldGeneratorSettings.BETA_SETTINGS);
            }
        };
        
        SKYLANDS = new GeneratorType("skylands") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
                OldGeneratorSettings.BETA_SETTINGS.providerSettings = OldGeneratorSettings.createInfSettings(WorldType.SKYLANDS, BiomeType.SKY, false);
                return new OldChunkGenerator(new OldBiomeSource(seed, biomes, OldGeneratorSettings.SKYLANDS_SETTINGS.providerSettings), seed, OldGeneratorSettings.SKYLANDS_SETTINGS);
            }
        };
        
        ALPHA = new GeneratorType("alpha") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
                OldGeneratorSettings.BETA_SETTINGS.providerSettings = OldGeneratorSettings.createInfSettings(WorldType.ALPHA, BiomeType.CLASSIC, ModernBeta.BETA_CONFIG.generateOceans);
                return new OldChunkGenerator(new OldBiomeSource(seed, biomes, OldGeneratorSettings.ALPHA_SETTINGS.providerSettings), seed, OldGeneratorSettings.ALPHA_SETTINGS);
            }
        };
        
        INFDEV = new GeneratorType("infdev") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
                OldGeneratorSettings.BETA_SETTINGS.providerSettings = OldGeneratorSettings.createInfSettings(WorldType.INFDEV, BiomeType.CLASSIC, ModernBeta.BETA_CONFIG.generateOceans);
                return new OldChunkGenerator(new OldBiomeSource(seed, biomes, OldGeneratorSettings.INFDEV_SETTINGS.providerSettings), seed, OldGeneratorSettings.INFDEV_SETTINGS);
            }
        };
        
        INFDEV_OLD = new GeneratorType("infdev_old") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
                OldGeneratorSettings.BETA_SETTINGS.providerSettings = OldGeneratorSettings.createInfSettings(WorldType.INFDEV_OLD, BiomeType.CLASSIC, ModernBeta.BETA_CONFIG.generateOceans);
                return new OldChunkGenerator(new OldBiomeSource(seed, biomes, OldGeneratorSettings.INFDEV_OLD_SETTINGS.providerSettings), seed, OldGeneratorSettings.INFDEV_OLD_SETTINGS);
            }
        };
          
        INDEV = new GeneratorType("indev") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
                OldGeneratorSettings.BETA_SETTINGS.providerSettings = OldGeneratorSettings.createIndevSettings();
                return new OldChunkGenerator(new OldBiomeSource(seed, biomes, OldGeneratorSettings.INDEV_SETTINGS.providerSettings), seed, OldGeneratorSettings.INDEV_SETTINGS);
            }
        };
        
        NETHER = new GeneratorType("nether") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
                OldGeneratorSettings.BETA_SETTINGS.providerSettings = OldGeneratorSettings.createInfSettings(WorldType.NETHER, BiomeType.BETA, false);
                return new OldChunkGenerator(new OldBiomeSource(seed, biomes, OldGeneratorSettings.NETHER_SETTINGS.providerSettings), seed, OldGeneratorSettings.NETHER_SETTINGS);
            }
        };
        
        
        FLAT = new GeneratorType("flat") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
                OldGeneratorSettings.BETA_SETTINGS.providerSettings = OldGeneratorSettings.createInfSettings(WorldType.FLAT, BiomeType.BETA, false);
                return new OldChunkGenerator(new OldBiomeSource(seed, biomes, OldGeneratorSettings.FLAT_SETTINGS.providerSettings), seed, OldGeneratorSettings.FLAT_SETTINGS);
            }
        };
        
        
        MixinGeneratorTypeAccessor.setScreenProviders(
            new ImmutableMap.Builder<Optional<GeneratorType>, ScreenProvider>()
                .putAll(MixinGeneratorTypeAccessor.getScreenProviders())
                .put(
                    Optional.<GeneratorType>of(BETA), (createWorldScreen, generatorSettings) -> {
                        return new InfCustomizeLevelScreen(createWorldScreen, OldGeneratorSettings.BETA_SETTINGS, "createWorld.customize.beta.title", BiomeType.BETA, true);
                    }
                )
                .put(
                    Optional.<GeneratorType>of(SKYLANDS), (createWorldScreen, generatorSettings) -> {
                        return new InfCustomizeLevelScreen(createWorldScreen, OldGeneratorSettings.SKYLANDS_SETTINGS, "createWorld.customize.skylands.title", BiomeType.SKY, false);
                    }
                )
                .put(
                    Optional.<GeneratorType>of(ALPHA), (createWorldScreen, generatorSettings) -> {
                        return new InfCustomizeLevelScreen(createWorldScreen, OldGeneratorSettings.ALPHA_SETTINGS, "createWorld.customize.alpha.title", BiomeType.CLASSIC, true);
                    }
                )
                .put(
                    Optional.<GeneratorType>of(INFDEV), (createWorldScreen, generatorSettings) -> {
                        return new InfCustomizeLevelScreen(createWorldScreen, OldGeneratorSettings.INFDEV_SETTINGS, "createWorld.customize.infdev.title", BiomeType.CLASSIC, true);
                    }
                )
                .put(
                    Optional.<GeneratorType>of(INFDEV_OLD), (createWorldScreen, generatorSettings) -> {
                        return new InfdevOldCustomizeLevelScreen(createWorldScreen, OldGeneratorSettings.INFDEV_OLD_SETTINGS, "createWorld.customize.infdev.title", BiomeType.CLASSIC, true);
                    }
                )
                .put(
                    Optional.<GeneratorType>of(INDEV), (createWorldScreen, generatorSettings) -> {
                        return new IndevCustomizeLevelScreen(createWorldScreen, OldGeneratorSettings.INDEV_SETTINGS, "createWorld.customize.indev.title");
                    }
                )
                .put(
                    Optional.<GeneratorType>of(FLAT), (createWorldScreen, generatorSettings) -> {
                        return new InfCustomizeLevelScreen(createWorldScreen, OldGeneratorSettings.BETA_SETTINGS, "createWorld.customize.beta.title", BiomeType.BETA, false);
                    }
                )
                .put(
                    Optional.<GeneratorType>of(NETHER), (createWorldScreen, generatorSettings) -> {
                        return new InfCustomizeLevelScreen(createWorldScreen, OldGeneratorSettings.BETA_SETTINGS, "createWorld.customize.beta.title", BiomeType.BETA, false);
                    }
                )
                .build()
        );
    }
}
