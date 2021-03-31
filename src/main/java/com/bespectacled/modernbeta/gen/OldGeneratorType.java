package com.bespectacled.modernbeta.gen;

import java.util.Optional;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.api.biome.BiomeProviderType.BuiltInBiomeType;
import com.bespectacled.modernbeta.api.biome.CaveBiomeProviderType.BuiltInCaveBiomeType;
import com.bespectacled.modernbeta.api.gen.ChunkProviderType.BuiltInChunkType;
import com.bespectacled.modernbeta.api.WorldProvider;
import com.bespectacled.modernbeta.api.WorldProviderType;
import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.mixin.client.MixinGeneratorTypeAccessor;
import com.bespectacled.modernbeta.mixin.client.MixinMoreOptionsDialogInvoker;
import com.google.common.collect.ImmutableMap;

import net.minecraft.client.world.GeneratorType;
import net.minecraft.client.world.GeneratorType.ScreenProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class OldGeneratorType {
    private static final GeneratorType OLD;
    
    public static void register() {
        register(OLD);
    }
    
    private static void register(GeneratorType type) {
        MixinGeneratorTypeAccessor.getValues().add(type);
    }
    
    private static GeneratorOptions createNewGeneratorOptions(
        DynamicRegistryManager registryManager, 
        GeneratorOptions generatorOptions,
        NbtCompound biomeProviderSettings,
        NbtCompound chunkProviderSettings
    ) {
        String chunkProviderType = chunkProviderSettings.getString("worldType");
    
        Registry<DimensionType> registryDimensionType = registryManager.<DimensionType>get(Registry.DIMENSION_TYPE_KEY);
        Registry<ChunkGeneratorSettings> registryChunkGenSettings = registryManager.<ChunkGeneratorSettings>get(Registry.NOISE_SETTINGS_WORLDGEN);
        Registry<Biome> registryBiome = registryManager.<Biome>get(Registry.BIOME_KEY);
        
        Optional<ChunkGeneratorSettings> chunkGenSettings = registryChunkGenSettings.getOrEmpty(new Identifier(WorldProviderType.getWorldProvider(chunkProviderType).getChunkGenSettings()));
        Supplier<ChunkGeneratorSettings> chunkGenSettingsSupplier = chunkGenSettings.isPresent() ?
            () -> chunkGenSettings.get() :
            () -> registryChunkGenSettings.getOrThrow(ChunkGeneratorSettings.OVERWORLD);
        
        return new GeneratorOptions(
            generatorOptions.getSeed(),
            generatorOptions.shouldGenerateStructures(),
            generatorOptions.hasBonusChest(),
            GeneratorOptions.getRegistryWithReplacedOverworldGenerator(
                registryDimensionType, 
                generatorOptions.getDimensions(), 
                new OldChunkGenerator(
                    new OldBiomeSource(generatorOptions.getSeed(), registryBiome, biomeProviderSettings), 
                    generatorOptions.getSeed(), 
                    chunkGenSettingsSupplier, 
                    chunkProviderSettings
                )
            )
        );
    }

    static {
        OLD = new GeneratorType("old") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> registryChunkGenSettings, long seed) {
                Supplier<ChunkGeneratorSettings> chunkGenSettingsSupplier = () -> 
                    registryChunkGenSettings.get(new Identifier(WorldProviderType.getWorldProvider(BuiltInChunkType.BETA.id).getChunkGenSettings()));
                
                NbtCompound biomeProviderSettings = OldGeneratorSettings.createBiomeSettings(
                    BuiltInBiomeType.BETA.id, 
                    BuiltInCaveBiomeType.VANILLA.id, 
                    WorldProviderType.getWorldProvider(BuiltInChunkType.BETA.id).getDefaultBiome()
                );
                
                NbtCompound chunkProviderSettings = OldGeneratorSettings.createInfSettings(BuiltInChunkType.BETA.id);
                
                return new OldChunkGenerator(
                    new OldBiomeSource(seed, biomes, biomeProviderSettings), 
                    seed, 
                    chunkGenSettingsSupplier, 
                    chunkProviderSettings
                );
            }
        };
        
        MixinGeneratorTypeAccessor.setScreenProviders(
            new ImmutableMap.Builder<Optional<GeneratorType>, ScreenProvider>()
                .putAll(MixinGeneratorTypeAccessor.getScreenProviders())
                .put(
                    Optional.<GeneratorType>of(OLD), (screen, generatorOptions) -> {
                        ChunkGenerator chunkGenerator = generatorOptions.getChunkGenerator();
                        BiomeSource biomeSource = chunkGenerator.getBiomeSource();
                        
                        // If settings already present, create new compound tag and copy from source,
                        // otherwise, not copying will modify original settings.
                        NbtCompound biomeSettings = biomeSource instanceof OldBiomeSource ? 
                            (new NbtCompound()).copyFrom(((OldBiomeSource)biomeSource).getProviderSettings()) : 
                            OldGeneratorSettings.createBiomeSettings(
                                BuiltInBiomeType.BETA.id, 
                                BuiltInCaveBiomeType.VANILLA.id, 
                                WorldProviderType.getWorldProvider(BuiltInChunkType.BETA.id).getDefaultBiome()
                            );
                        
                        NbtCompound chunkSettings = chunkGenerator instanceof OldChunkGenerator ?
                            (new NbtCompound()).copyFrom(((OldChunkGenerator)chunkGenerator).getProviderSettings()) :
                            OldGeneratorSettings.createInfSettings(BuiltInBiomeType.BETA.id);
                        
                        String chunkProviderType = chunkSettings.getString("worldType");
                        WorldProvider worldProvider = WorldProviderType.getWorldProvider(chunkProviderType);
                        
                        return worldProvider.createLevelScreen(
                            screen,
                            screen.moreOptionsDialog.getRegistryManager(),
                            biomeSettings,
                            chunkSettings,
                            ((biomeProviderSettings, chunkProviderSettings) -> ((MixinMoreOptionsDialogInvoker)screen.moreOptionsDialog).invokeSetGeneratorOptions(
                                createNewGeneratorOptions(
                                    screen.moreOptionsDialog.getRegistryManager(),
                                    generatorOptions,
                                    biomeProviderSettings,
                                    chunkProviderSettings
                            )))
                        );
                    }
                )
                .build()
        );
    }
}
