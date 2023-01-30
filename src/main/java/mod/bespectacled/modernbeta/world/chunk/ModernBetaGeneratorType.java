package mod.bespectacled.modernbeta.world.chunk;

import java.util.Optional;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.mixin.client.MixinGeneratorTypeAccessor;
import mod.bespectacled.modernbeta.settings.ModernBetaBiomeSettings;
import mod.bespectacled.modernbeta.settings.ModernBetaCaveBiomeSettings;
import mod.bespectacled.modernbeta.settings.ModernBetaChunkSettings;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
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
    private static final Optional<Integer> MODERN_BETA_VERSION = Optional.of(ModernBeta.MOD_VERSION);
    private static final GeneratorType MODERN_BETA;
    
    public static void register() {
        register(MODERN_BETA);
    }
    
    private static void register(GeneratorType type) {
        MixinGeneratorTypeAccessor.getValues().add(type);
    }
    
    static {
        MODERN_BETA = new GeneratorType(ModernBeta.MOD_ID) {
            @Override
            protected ChunkGenerator getChunkGenerator(DynamicRegistryManager registryManager, long seed) {
                Registry<StructureSet> structuresRegistry = registryManager.get(Registry.STRUCTURE_SET_KEY);
                Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry = registryManager.get(Registry.NOISE_WORLDGEN);
                Registry<ChunkGeneratorSettings> chunkGenSettingsRegistry = registryManager.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
                Registry<Biome> biomeRegistry = registryManager.get(Registry.BIOME_KEY);
                
                RegistryKey<ChunkGeneratorSettings> worldTypeKey = RegistryKey.of(
                    Registry.CHUNK_GENERATOR_SETTINGS_KEY,
                    ModernBeta.createId(ModernBeta.MOD_ID)
                );
                RegistryEntry<ChunkGeneratorSettings> chunkGenSettings = chunkGenSettingsRegistry.getOrCreateEntry(worldTypeKey);
                
                return new ModernBetaChunkGenerator(
                    structuresRegistry,
                    noiseRegistry,
                    new ModernBetaBiomeSource(
                        seed,
                        biomeRegistry,
                        new ModernBetaBiomeSettings().toCompound(),
                        new ModernBetaCaveBiomeSettings().toCompound(),
                        MODERN_BETA_VERSION
                    ), 
                    seed, 
                    chunkGenSettings, 
                    new ModernBetaChunkSettings().toCompound(),
                    MODERN_BETA_VERSION
                );
            }
        };
    }
}
