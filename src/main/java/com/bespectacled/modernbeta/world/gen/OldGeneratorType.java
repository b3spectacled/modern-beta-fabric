package com.bespectacled.modernbeta.world.gen;

import java.util.Optional;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.api.registry.Registries;
import com.bespectacled.modernbeta.api.world.WorldProvider;
import com.bespectacled.modernbeta.client.gui.WorldSettings;
import com.bespectacled.modernbeta.client.gui.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.mixin.client.MixinGeneratorTypeAccessor;
import com.bespectacled.modernbeta.mixin.client.MixinMoreOptionsDialogInvoker;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.biome.provider.settings.BiomeProviderSettings;
import com.bespectacled.modernbeta.world.cavebiome.provider.settings.CaveBiomeProviderSettings;
import com.bespectacled.modernbeta.world.gen.provider.settings.ChunkProviderSettings;
import com.google.common.collect.ImmutableMap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.client.world.GeneratorType.ScreenProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

@Environment(EnvType.CLIENT)
public class OldGeneratorType {
    private static final String DEFAULT_WORLD_TYPE = BuiltInTypes.Chunk.BETA.name;
    private static final Optional<Integer> MODERN_BETA_VERSION = Optional.of(ModernBeta.MOD_VERSION);
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
        Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry = registryManager.get(Registry.NOISE_WORLDGEN);
        Registry<DimensionType> dimensionRegistry = registryManager.get(Registry.DIMENSION_TYPE_KEY);
        Registry<ChunkGeneratorSettings> chunkGenSettingsRegistry = registryManager.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
        Registry<Biome> biomeRegistry = registryManager.get(Registry.BIOME_KEY);
        
        NbtCompound chunkSettings = worldSettings.getNbt(WorldSetting.CHUNK);
        NbtCompound biomeSettings = worldSettings.getNbt(WorldSetting.BIOME);
        NbtCompound caveBiomeSettings = worldSettings.getNbt(WorldSetting.CAVE_BIOME);
        
        String worldType = NbtUtil.readStringOrThrow(NbtTags.WORLD_TYPE, chunkSettings);
        
        Optional<ChunkGeneratorSettings> chunkGenSettings = chunkGenSettingsRegistry.getOrEmpty(ModernBeta.createId(worldType));
        Supplier<ChunkGeneratorSettings> chunkGenSettingsSupplier = chunkGenSettings.isPresent() ?
            () -> chunkGenSettings.get() :
            () -> chunkGenSettingsRegistry.getOrThrow(ChunkGeneratorSettings.OVERWORLD);
        
        return new GeneratorOptions(
            generatorOptions.getSeed(),
            generatorOptions.shouldGenerateStructures(),
            generatorOptions.hasBonusChest(),
            GeneratorOptions.getRegistryWithReplacedOverworldGenerator(
                dimensionRegistry, 
                generatorOptions.getDimensions(), 
                new OldChunkGenerator(
                    noiseRegistry,
                    new OldBiomeSource(
                        generatorOptions.getSeed(),
                        biomeRegistry,
                        biomeSettings,
                        caveBiomeSettings,
                        MODERN_BETA_VERSION
                    ), 
                    generatorOptions.getSeed(), 
                    chunkGenSettingsSupplier, 
                    chunkSettings,
                    MODERN_BETA_VERSION
                )
            )
        );
    }

    static {
        OLD = new GeneratorType("old") {
            @Override
            protected ChunkGenerator getChunkGenerator(DynamicRegistryManager registryManager, long seed) {
                Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry = registryManager.get(Registry.NOISE_WORLDGEN);
                Registry<ChunkGeneratorSettings> chunkGenSettingsRegistry = registryManager.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
                Registry<Biome> biomeRegistry = registryManager.get(Registry.BIOME_KEY);
                
                Supplier<ChunkGeneratorSettings> chunkGenSettingsSupplier = () -> 
                    chunkGenSettingsRegistry.get(ModernBeta.createId(Registries.WORLD.get(DEFAULT_WORLD_TYPE).getChunkProvider()));
                    
                WorldProvider worldProvider = Registries.WORLD.get(DEFAULT_WORLD_TYPE);
                String worldType = worldProvider.getChunkProvider();
                String biomeType = worldProvider.getBiomeProvider();
                String caveBiomeType = worldProvider.getCaveBiomeProvider();
                
                NbtCompound chunkSettings = Registries.CHUNK_SETTINGS.getOrElse(worldType, () -> ChunkProviderSettings.createSettingsDefault(worldType)).get();
                NbtCompound biomeSettings = Registries.BIOME_SETTINGS.getOrElse(biomeType, () -> BiomeProviderSettings.createSettingsDefault(biomeType)).get();
                NbtCompound caveBiomeSettings = Registries.CAVE_BIOME_SETTINGS.getOrElse(caveBiomeType, () -> CaveBiomeProviderSettings.createSettingsDefault(caveBiomeType)).get();
                
                return new OldChunkGenerator(
                    noiseRegistry,
                    new OldBiomeSource(
                        seed,
                        biomeRegistry,
                        biomeSettings,
                        caveBiomeSettings,
                        MODERN_BETA_VERSION
                    ), 
                    seed, 
                    chunkGenSettingsSupplier, 
                    chunkSettings,
                    MODERN_BETA_VERSION
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
                            oldChunkGenerator.getChunkSettings() :
                            Registries.CHUNK_SETTINGS.get(worldProvider.getChunkProvider()).get();
                        
                        NbtCompound biomeSettings = biomeSource instanceof OldBiomeSource oldBiomeSource ? 
                            oldBiomeSource.getBiomeSettings() : 
                            Registries.BIOME_SETTINGS.get(worldProvider.getBiomeProvider()).get();

                        NbtCompound caveSettings = biomeSource instanceof OldBiomeSource oldBiomeSource ? 
                            oldBiomeSource.getCaveBiomeSettings() :
                            Registries.CAVE_BIOME_SETTINGS.get(worldProvider.getCaveBiomeProvider()).get();
                        
                        return new WorldScreen(
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
                ).build()
        );
    }
}
