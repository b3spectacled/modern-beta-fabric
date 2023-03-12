package mod.bespectacled.modernbeta.world.gen;

import java.util.Optional;

import com.google.common.collect.ImmutableMap;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import mod.bespectacled.modernbeta.api.registry.Registries;
import mod.bespectacled.modernbeta.api.world.WorldProvider;
import mod.bespectacled.modernbeta.client.gui.screen.WorldScreen;
import mod.bespectacled.modernbeta.mixin.client.MixinGeneratorTypeAccessor;
import mod.bespectacled.modernbeta.mixin.client.MixinMoreOptionsDialogInvoker;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import mod.bespectacled.modernbeta.util.settings.ImmutableSettings;
import mod.bespectacled.modernbeta.util.settings.Settings;
import mod.bespectacled.modernbeta.util.settings.WorldSettings;
import mod.bespectacled.modernbeta.util.settings.WorldSettings.WorldSetting;
import mod.bespectacled.modernbeta.world.biome.OldBiomeSource;
import mod.bespectacled.modernbeta.world.biome.provider.settings.BiomeProviderSettings;
import mod.bespectacled.modernbeta.world.cavebiome.provider.settings.CaveBiomeProviderSettings;
import mod.bespectacled.modernbeta.world.gen.provider.settings.ChunkProviderSettings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.client.world.GeneratorType.ScreenProvider;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

@Environment(EnvType.CLIENT)
public class OldGeneratorType {
    private static final String DEFAULT_WORLD_TYPE = ModernBetaBuiltInTypes.Chunk.BETA.name;
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
        Registry<StructureSet> structuresRegistry = registryManager.get(Registry.STRUCTURE_SET_KEY);
        Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry = registryManager.get(Registry.NOISE_WORLDGEN);
        Registry<DimensionType> dimensionRegistry = registryManager.get(Registry.DIMENSION_TYPE_KEY);
        Registry<ChunkGeneratorSettings> chunkGenSettingsRegistry = registryManager.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
        Registry<Biome> biomeRegistry = registryManager.get(Registry.BIOME_KEY);
        
        ImmutableSettings chunkSettings = ImmutableSettings.copyOf(worldSettings.get(WorldSetting.CHUNK));
        ImmutableSettings biomeSettings = ImmutableSettings.copyOf(worldSettings.get(WorldSetting.BIOME));
        ImmutableSettings caveBiomeSettings = ImmutableSettings.copyOf(worldSettings.get(WorldSetting.CAVE_BIOME));
        
        String worldType = NbtUtil.toStringOrThrow(chunkSettings.get(NbtTags.WORLD_TYPE));
        RegistryKey<ChunkGeneratorSettings> worldTypeKey = RegistryKey.of(Registry.CHUNK_GENERATOR_SETTINGS_KEY, ModernBeta.createId(worldType));
        
        RegistryEntry<ChunkGeneratorSettings> chunkGenSettings = chunkGenSettingsRegistry.getOrCreateEntry(worldTypeKey);
        
        return new GeneratorOptions(
            generatorOptions.getSeed(),
            generatorOptions.shouldGenerateStructures(),
            generatorOptions.hasBonusChest(),
            GeneratorOptions.getRegistryWithReplacedOverworldGenerator(
                dimensionRegistry, 
                generatorOptions.getDimensions(), 
                new OldChunkGenerator(
                    structuresRegistry,
                    noiseRegistry,
                    new OldBiomeSource(
                        generatorOptions.getSeed(),
                        biomeRegistry,
                        biomeSettings,
                        caveBiomeSettings,
                        MODERN_BETA_VERSION
                    ), 
                    generatorOptions.getSeed(), 
                    chunkGenSettings, 
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
                Registry<StructureSet> structuresRegistry = registryManager.get(Registry.STRUCTURE_SET_KEY);
                Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry = registryManager.get(Registry.NOISE_WORLDGEN);
                Registry<ChunkGeneratorSettings> chunkGenSettingsRegistry = registryManager.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
                Registry<Biome> biomeRegistry = registryManager.get(Registry.BIOME_KEY);
                
                RegistryKey<ChunkGeneratorSettings> worldTypeKey = RegistryKey.of(
                    Registry.CHUNK_GENERATOR_SETTINGS_KEY,
                    ModernBeta.createId(Registries.WORLD.get(DEFAULT_WORLD_TYPE).getChunkProvider())
                );
                RegistryEntry<ChunkGeneratorSettings> chunkGenSettings = chunkGenSettingsRegistry.getOrCreateEntry(worldTypeKey);
                    
                WorldProvider worldProvider = Registries.WORLD.get(DEFAULT_WORLD_TYPE);
                String worldType = worldProvider.getChunkProvider();
                String biomeType = worldProvider.getBiomeProvider();
                String caveBiomeType = worldProvider.getCaveBiomeProvider();
                
                ImmutableSettings chunkSettings = ImmutableSettings.copyOf(Registries.CHUNK_SETTINGS
                    .getOrEmpty(worldType)
                    .orElse(() -> ChunkProviderSettings.createSettingsDefault(worldType))
                    .get()
                );
                
                ImmutableSettings biomeSettings = ImmutableSettings.copyOf(Registries.BIOME_SETTINGS
                    .getOrEmpty(biomeType)
                    .orElse(() -> BiomeProviderSettings.createSettingsDefault(biomeType))
                    .get()
                );
                    
                    ImmutableSettings caveBiomeSettings = ImmutableSettings.copyOf(Registries.CAVE_BIOME_SETTINGS
                    .getOrEmpty(caveBiomeType)
                    .orElse(() -> CaveBiomeProviderSettings.createSettingsDefault(caveBiomeType))
                    .get()
                );
                
                return new OldChunkGenerator(
                    structuresRegistry,
                    noiseRegistry,
                    new OldBiomeSource(
                        seed,
                        biomeRegistry,
                        biomeSettings,
                        caveBiomeSettings,
                        MODERN_BETA_VERSION
                    ), 
                    seed, 
                    chunkGenSettings, 
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
                        Settings chunkSettings = chunkGenerator instanceof OldChunkGenerator oldChunkGenerator ?
                            oldChunkGenerator.getChunkSettings() :
                            Registries.CHUNK_SETTINGS.get(worldProvider.getChunkProvider()).get();
                        
                        Settings biomeSettings = biomeSource instanceof OldBiomeSource oldBiomeSource ? 
                            oldBiomeSource.getBiomeSettings() : 
                            Registries.BIOME_SETTINGS.get(worldProvider.getBiomeProvider()).get();

                        Settings caveSettings = biomeSource instanceof OldBiomeSource oldBiomeSource ? 
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
