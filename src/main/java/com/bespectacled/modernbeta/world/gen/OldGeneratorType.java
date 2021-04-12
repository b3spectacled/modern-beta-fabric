package com.bespectacled.modernbeta.world.gen;

import java.util.Optional;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.api.registry.ChunkProviderSettingsRegistry;
import com.bespectacled.modernbeta.api.registry.ChunkProviderSettingsRegistry.BuiltInChunkSettingsType;
import com.bespectacled.modernbeta.api.registry.WorldProviderRegistry;
import com.bespectacled.modernbeta.mixin.client.MixinGeneratorTypeAccessor;
import com.bespectacled.modernbeta.mixin.client.MixinMoreOptionsDialogInvoker;
import com.bespectacled.modernbeta.api.registry.BiomeProviderRegistry.BuiltInBiomeType;
import com.bespectacled.modernbeta.api.registry.ChunkProviderRegistry.BuiltInChunkType;
import com.bespectacled.modernbeta.api.WorldProvider;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.biome.provider.settings.BiomeProviderSettings;
import com.google.common.collect.ImmutableMap;

import net.minecraft.client.world.GeneratorType;
import net.minecraft.client.world.GeneratorType.ScreenProvider;
import net.minecraft.nbt.CompoundTag;
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
        CompoundTag biomeProviderSettings,
        CompoundTag chunkProviderSettings
    ) {
        String chunkProviderType = chunkProviderSettings.getString("worldType");
    
        Registry<DimensionType> registryDimensionType = registryManager.<DimensionType>get(Registry.DIMENSION_TYPE_KEY);
        Registry<ChunkGeneratorSettings> registryChunkGenSettings = registryManager.<ChunkGeneratorSettings>get(Registry.NOISE_SETTINGS_WORLDGEN);
        Registry<Biome> registryBiome = registryManager.<Biome>get(Registry.BIOME_KEY);
        
        Optional<ChunkGeneratorSettings> chunkGenSettings = registryChunkGenSettings.getOrEmpty(new Identifier(WorldProviderRegistry.get(chunkProviderType).getChunkGenSettings()));
        Supplier<ChunkGeneratorSettings> chunkGenSettingsSupplier = chunkGenSettings.isPresent() ?
            () -> chunkGenSettings.get() :
            () -> registryChunkGenSettings.getOrThrow(ChunkGeneratorSettings.OVERWORLD);
        
        OldChunkGeneratorSettings settings = new OldChunkGeneratorSettings(chunkGenSettingsSupplier.get(), chunkProviderSettings);
            
        return new GeneratorOptions(
            generatorOptions.getSeed(),
            generatorOptions.shouldGenerateStructures(),
            generatorOptions.hasBonusChest(),
            GeneratorOptions.method_28608(
                registryDimensionType, 
                generatorOptions.getDimensions(), 
                new OldChunkGenerator(
                    new OldBiomeSource(generatorOptions.getSeed(), registryBiome, biomeProviderSettings), 
                    generatorOptions.getSeed(), 
                    settings
                )
            )
        );
    }

    static {
        OLD = new GeneratorType("old") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> registryChunkGenSettings, long seed) {
                Supplier<ChunkGeneratorSettings> chunkGenSettingsSupplier = () -> 
                    registryChunkGenSettings.get(new Identifier(WorldProviderRegistry.get(BuiltInChunkType.BETA.name).getChunkGenSettings()));
                
                CompoundTag biomeProviderSettings = BiomeProviderSettings.createBiomeSettings(
                    BuiltInBiomeType.BETA.name,
                    WorldProviderRegistry.get(BuiltInChunkType.BETA.name).getDefaultBiome()
                );
                
                CompoundTag chunkProviderSettings = ChunkProviderSettingsRegistry.get(BuiltInChunkSettingsType.BETA.name).get();
                
                OldChunkGeneratorSettings settings = new OldChunkGeneratorSettings(chunkGenSettingsSupplier.get(), chunkProviderSettings);
                
                return new OldChunkGenerator(
                    new OldBiomeSource(seed, biomes, biomeProviderSettings), 
                    seed, 
                    settings
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
                        CompoundTag biomeSettings = biomeSource instanceof OldBiomeSource ? 
                            (new CompoundTag()).copyFrom(((OldBiomeSource)biomeSource).getProviderSettings()) : 
                            BiomeProviderSettings.createBiomeSettings(
                                BuiltInBiomeType.BETA.name,
                                WorldProviderRegistry.get(BuiltInChunkType.BETA.name).getDefaultBiome()
                            );
                        
                        CompoundTag chunkSettings = chunkGenerator instanceof OldChunkGenerator ?
                            (new CompoundTag()).copyFrom(((OldChunkGenerator)chunkGenerator).getProviderSettings()) :
                            ChunkProviderSettingsRegistry.get(BuiltInChunkSettingsType.BETA.name).get();
                        
                        String chunkProviderType = chunkSettings.getString("worldType");
                        WorldProvider worldProvider = WorldProviderRegistry.get(chunkProviderType);
                        
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
