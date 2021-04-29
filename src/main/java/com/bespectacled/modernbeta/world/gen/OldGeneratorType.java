package com.bespectacled.modernbeta.world.gen;

import java.util.Optional;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.api.world.WorldProvider;
import com.bespectacled.modernbeta.api.registry.ProviderRegistries;
import com.bespectacled.modernbeta.mixin.client.MixinGeneratorTypeAccessor;
import com.bespectacled.modernbeta.mixin.client.MixinMoreOptionsDialogInvoker;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
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
        Registry<ChunkGeneratorSettings> registryChunkGenSettings = registryManager.<ChunkGeneratorSettings>get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
        Registry<Biome> registryBiome = registryManager.<Biome>get(Registry.BIOME_KEY);
        
        Optional<ChunkGeneratorSettings> chunkGenSettings = registryChunkGenSettings.getOrEmpty(new Identifier(ProviderRegistries.WORLD.get(chunkProviderType).getChunkGenSettings()));
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
                    registryChunkGenSettings.get(new Identifier(ProviderRegistries.WORLD.get(BuiltInTypes.Chunk.BETA.name).getChunkGenSettings()));
                
                NbtCompound chunkProviderSettings = ProviderRegistries.CHUNK_SETTINGS.get(BuiltInTypes.Chunk.BETA.name).get();
                NbtCompound biomeProviderSettings = ProviderRegistries.BIOME_SETTINGS.get(BuiltInTypes.Biome.BETA.name).get();
                                
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
                            ProviderRegistries.BIOME_SETTINGS.get(BuiltInTypes.Biome.BETA.name).get();
                        
                        NbtCompound chunkSettings = chunkGenerator instanceof OldChunkGenerator ?
                            (new NbtCompound()).copyFrom(((OldChunkGenerator)chunkGenerator).getProviderSettings()) :
                            ProviderRegistries.CHUNK_SETTINGS.get(BuiltInTypes.Chunk.BETA.name).get();
                        
                        String chunkProviderType = chunkSettings.getString("worldType");
                        WorldProvider worldProvider = ProviderRegistries.WORLD.get(chunkProviderType);
                        
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
