package com.bespectacled.modernbeta.world.gen;

import java.util.Optional;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.api.registry.Registries;
import com.bespectacled.modernbeta.api.world.WorldProvider;
import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.api.world.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.mixin.client.MixinGeneratorTypeAccessor;
import com.bespectacled.modernbeta.mixin.client.MixinMoreOptionsDialogInvoker;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.cavebiome.provider.settings.CaveBiomeProviderSettings;
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
    private static final String DEFAULT_WORLD_TYPE = BuiltInTypes.Chunk.BETA.name;
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
        WorldSettings worldSettings
    ) {
        NbtCompound chunkSettings = worldSettings.getNbt(WorldSetting.CHUNK);
        NbtCompound biomeSettings = worldSettings.getNbt(WorldSetting.BIOME);
        NbtCompound caveBiomeSettings = worldSettings.getNbt(WorldSetting.CAVE_BIOME);
        
        WorldProvider worldProvider = Registries.WORLD.get(chunkSettings.getString(NbtTags.WORLD_TYPE));
        
        Registry<DimensionType> registryDimensionType = registryManager.<DimensionType>get(Registry.DIMENSION_TYPE_KEY);
        Registry<ChunkGeneratorSettings> registryChunkGenSettings = registryManager.<ChunkGeneratorSettings>get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
        Registry<Biome> registryBiome = registryManager.<Biome>get(Registry.BIOME_KEY);
        
        Optional<ChunkGeneratorSettings> chunkGenSettings = registryChunkGenSettings.getOrEmpty(new Identifier(worldProvider.getChunkGenSettings()));
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
                    new OldBiomeSource(generatorOptions.getSeed(), registryBiome, biomeSettings, Optional.ofNullable(caveBiomeSettings)), 
                    generatorOptions.getSeed(), 
                    chunkGenSettingsSupplier, 
                    chunkSettings
                )
            )
        );
    }

    static {
        OLD = new GeneratorType("old") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> registryChunkGenSettings, long seed) {
                Supplier<ChunkGeneratorSettings> chunkGenSettingsSupplier = () -> 
                    registryChunkGenSettings.get(new Identifier(Registries.WORLD.get(DEFAULT_WORLD_TYPE).getChunkGenSettings()));
                    
                WorldProvider worldProvider = Registries.WORLD.get(DEFAULT_WORLD_TYPE);
                
                NbtCompound chunkSettings = Registries.CHUNK_SETTINGS.get(worldProvider.getChunkProvider()).get();
                NbtCompound biomeSettings = Registries.BIOME_SETTINGS.get(worldProvider.getBiomeProvider()).get();
                NbtCompound caveBiomeSettings = Registries.CAVE_BIOME_SETTINGS.get(worldProvider.getCaveBiomeProvider()).get();
                
                return new OldChunkGenerator(
                    new OldBiomeSource(seed, biomes, biomeSettings, Optional.ofNullable(caveBiomeSettings)), 
                    seed, 
                    chunkGenSettingsSupplier, 
                    chunkSettings
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
                        
                        WorldProvider worldProvider = Registries.WORLD.get(DEFAULT_WORLD_TYPE);
                        
                        // In the case that settings have been set, and the world edit screen is opened again:
                        // If settings already present, create new compound tag and copy from source,
                        // otherwise, not copying will modify original settings.
                        NbtCompound chunkSettings = chunkGenerator instanceof OldChunkGenerator oldChunkGenerator ?
                            oldChunkGenerator.getProviderSettings() :
                            Registries.CHUNK_SETTINGS.get(worldProvider.getChunkProvider()).get();
                        
                        NbtCompound biomeSettings = biomeSource instanceof OldBiomeSource oldBiomeSource ? 
                            oldBiomeSource.getBiomeSettings() : 
                            Registries.BIOME_SETTINGS.get(worldProvider.getBiomeProvider()).get();

                        NbtCompound caveSettings = biomeSource instanceof OldBiomeSource oldBiomeSource ? 
                            oldBiomeSource.getCaveBiomeSettings() :
                            Registries.CAVE_BIOME_SETTINGS.get(worldProvider.getCaveBiomeProvider()).get();
                        
                        return Registries.WORLD.get(chunkSettings.getString(NbtTags.WORLD_TYPE)).createWorldScreen(
                            screen,
                            new WorldSettings(chunkSettings, biomeSettings, caveSettings),
                            modifiedWorldSettings -> ((MixinMoreOptionsDialogInvoker)screen.moreOptionsDialog).invokeSetGeneratorOptions(
                                createNewGeneratorOptions(
                                    screen.moreOptionsDialog.getRegistryManager(),
                                    generatorOptions,
                                    modifiedWorldSettings
                            ))
                        );
                    }
                )
                .build()
        );
    }
}
