package com.bespectacled.modernbeta.gen;

import java.util.Optional;

import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.gui.CustomizeAlphaLevelScreen;
import com.bespectacled.modernbeta.gui.CustomizeBetaLevelScreen;
import com.bespectacled.modernbeta.gui.CustomizeIndevLevelScreen;
import com.bespectacled.modernbeta.gui.CustomizeInfdevLevelScreen;
import com.bespectacled.modernbeta.gui.CustomizeInfdevOldLevelScreen;
import com.bespectacled.modernbeta.gui.CustomizeNetherLevelScreen;
import com.bespectacled.modernbeta.gui.CustomizeSkylandsLevelScreen;
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
    //private static final GeneratorType NETHER;
    
    public static void register() {
        register(BETA);
        register(SKYLANDS);
        register(ALPHA);
        register(INFDEV);
        register(INFDEV_OLD);
        register(INDEV);
        //register(NETHER);
    }
    
    private static void register(GeneratorType type) {
        MixinGeneratorTypeAccessor.getValues().add(type);
    }
    
    static {
        BETA = new GeneratorType("beta") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
                return new OldChunkGenerator(new OldBiomeSource(seed, biomes, OldGeneratorSettings.BETA_SETTINGS.providerSettings), seed, OldGeneratorSettings.BETA_SETTINGS);
            }
        };
        
        SKYLANDS = new GeneratorType("skylands") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
                return new OldChunkGenerator(new OldBiomeSource(seed, biomes, OldGeneratorSettings.SKYLANDS_SETTINGS.providerSettings), seed, OldGeneratorSettings.SKYLANDS_SETTINGS);
            }
        };
        
        ALPHA = new GeneratorType("alpha") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
                return new OldChunkGenerator(new OldBiomeSource(seed, biomes, OldGeneratorSettings.ALPHA_SETTINGS.providerSettings), seed, OldGeneratorSettings.ALPHA_SETTINGS);
            }
        };
        
        INFDEV = new GeneratorType("infdev") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
                return new OldChunkGenerator(new OldBiomeSource(seed, biomes, OldGeneratorSettings.INFDEV_SETTINGS.providerSettings), seed, OldGeneratorSettings.INFDEV_SETTINGS);
            }
        };
        
        INFDEV_OLD = new GeneratorType("infdev_old") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
                return new OldChunkGenerator(new OldBiomeSource(seed, biomes, OldGeneratorSettings.INFDEV_SETTINGS.providerSettings), seed, OldGeneratorSettings.INFDEV_SETTINGS);
            }
        };
          
        INDEV = new GeneratorType("indev") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
                return new OldChunkGenerator(new OldBiomeSource(seed, biomes, OldGeneratorSettings.INDEV_SETTINGS.providerSettings), seed, OldGeneratorSettings.INDEV_SETTINGS);
            }
        };
        
        //NETHER = new GeneratorType("nether") {
        //    @Override
        //    protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
        //        return new OldChunkGenerator(new OldBiomeSource(seed, biomes, OldGeneratorSettings.NETHER_SETTINGS.providerSettings), seed, OldGeneratorSettings.NETHER_SETTINGS);
        //    }
        //};
        
        MixinGeneratorTypeAccessor.setScreenProviders(
            new ImmutableMap.Builder<Optional<GeneratorType>, ScreenProvider>()
                .putAll(MixinGeneratorTypeAccessor.getScreenProviders())
                .put(
                    Optional.<GeneratorType>of(BETA), (createWorldScreen, generatorSettings) -> {
                        return new CustomizeBetaLevelScreen(createWorldScreen, OldGeneratorSettings.BETA_SETTINGS);
                    }
                )
                .put(
                    Optional.<GeneratorType>of(SKYLANDS), (createWorldScreen, generatorSettings) -> {
                        return new CustomizeSkylandsLevelScreen(createWorldScreen, OldGeneratorSettings.SKYLANDS_SETTINGS);
                    }
                )
                .put(
                    Optional.<GeneratorType>of(ALPHA), (createWorldScreen, generatorSettings) -> {
                        return new CustomizeAlphaLevelScreen(createWorldScreen, OldGeneratorSettings.ALPHA_SETTINGS);
                    }
                )
                .put(
                    Optional.<GeneratorType>of(INFDEV), (createWorldScreen, generatorSettings) -> {
                        return new CustomizeInfdevLevelScreen(createWorldScreen, OldGeneratorSettings.INFDEV_SETTINGS);
                    }
                )
                .put(
                    Optional.<GeneratorType>of(INFDEV_OLD), (createWorldScreen, generatorSettings) -> {
                        return new CustomizeInfdevOldLevelScreen(createWorldScreen, OldGeneratorSettings.INFDEV_SETTINGS);
                    }
                )
                .put(
                    Optional.<GeneratorType>of(INDEV), (createWorldScreen, generatorSettings) -> {
                        return new CustomizeIndevLevelScreen(createWorldScreen, OldGeneratorSettings.INDEV_SETTINGS);
                    }
                )
                //.put(
                //    Optional.<GeneratorType>of(NETHER), (createWorldScreen, generatorSettings) -> {
                //        return new CustomizeNetherLevelScreen(createWorldScreen, OldGeneratorSettings.NETHER_SETTINGS);
                //    }
                //)
                .build()
        );
    }
}
