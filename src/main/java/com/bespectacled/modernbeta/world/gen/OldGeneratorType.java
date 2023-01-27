package com.bespectacled.modernbeta.world.gen;

import java.util.Optional;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import com.bespectacled.modernbeta.api.registry.Registries;
import com.bespectacled.modernbeta.api.world.WorldProvider;
import com.bespectacled.modernbeta.mixin.client.MixinGeneratorTypeAccessor;
import com.bespectacled.modernbeta.util.settings.ImmutableSettings;
import com.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import com.bespectacled.modernbeta.world.biome.provider.settings.BiomeProviderSettings;
import com.bespectacled.modernbeta.world.cavebiome.provider.settings.CaveBiomeProviderSettings;
import com.bespectacled.modernbeta.world.gen.provider.settings.ChunkProviderSettings;
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
