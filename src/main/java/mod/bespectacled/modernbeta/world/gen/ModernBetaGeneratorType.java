package mod.bespectacled.modernbeta.world.gen;

import java.util.Optional;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import mod.bespectacled.modernbeta.api.registry.Registries;
import mod.bespectacled.modernbeta.mixin.client.MixinGeneratorTypeAccessor;
import mod.bespectacled.modernbeta.util.settings.ImmutableSettings;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.biome.provider.settings.BiomeProviderSettings;
import mod.bespectacled.modernbeta.world.cavebiome.provider.settings.CaveBiomeProviderSettings;
import mod.bespectacled.modernbeta.world.gen.provider.settings.ChunkProviderSettings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

@Environment(EnvType.CLIENT)
public class ModernBetaGeneratorType {
    private static final String DEFAULT_CHUNK_TYPE = ModernBetaBuiltInTypes.Chunk.BETA.name;
    private static final String DEFAULT_BIOME_TYPE = ModernBetaBuiltInTypes.Biome.BETA.name;
    private static final String DEFAULT_CAVE_BIOME_TYPE = ModernBetaBuiltInTypes.CaveBiome.VORONOI.name;
    
    private static final Optional<Integer> MODERN_BETA_VERSION = Optional.of(ModernBeta.MOD_VERSION);
    private static final GeneratorType MODERN_BETA;
    
    public static void register() {
        register(MODERN_BETA);
    }
    
    private static void register(GeneratorType type) {
        MixinGeneratorTypeAccessor.getValues().add(type);
    }
    
    static {
        MODERN_BETA = new GeneratorType("modern_beta") {
            @Override
            protected ChunkGenerator getChunkGenerator(DynamicRegistryManager registryManager, long seed) {
                Registry<StructureSet> structuresRegistry = registryManager.get(Registry.STRUCTURE_SET_KEY);
                Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry = registryManager.get(Registry.NOISE_WORLDGEN);
                Registry<ChunkGeneratorSettings> chunkGenSettingsRegistry = registryManager.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
                Registry<Biome> biomeRegistry = registryManager.get(Registry.BIOME_KEY);
                
                RegistryKey<ChunkGeneratorSettings> worldTypeKey = RegistryKey.of(
                    Registry.CHUNK_GENERATOR_SETTINGS_KEY,
                    ModernBeta.createId(DEFAULT_CHUNK_TYPE)
                );
                RegistryEntry<ChunkGeneratorSettings> chunkGenSettings = chunkGenSettingsRegistry.getOrCreateEntry(worldTypeKey);
                
                String chunkType = DEFAULT_CHUNK_TYPE;
                String biomeType = DEFAULT_BIOME_TYPE;
                String caveBiomeType = DEFAULT_CAVE_BIOME_TYPE;
                
                ImmutableSettings chunkSettings = ImmutableSettings.copyOf(Registries.CHUNK_SETTINGS
                    .getOrEmpty(chunkType)
                    .orElse(() -> ChunkProviderSettings.createSettingsDefault(chunkType))
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
                
                return new ModernBetaChunkGenerator(
                    structuresRegistry,
                    noiseRegistry,
                    new ModernBetaBiomeSource(
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
    }
}
