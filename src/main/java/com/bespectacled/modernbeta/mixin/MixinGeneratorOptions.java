package com.bespectacled.modernbeta.mixin;

import java.util.Optional;
import java.util.Properties;
import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.registry.Registries;
import com.bespectacled.modernbeta.util.settings.ImmutableSettings;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.biome.provider.settings.BiomeProviderSettings;
import com.bespectacled.modernbeta.world.cavebiome.provider.settings.CaveBiomeProviderSettings;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;
import com.bespectacled.modernbeta.world.gen.provider.settings.ChunkProviderSettings;
import com.google.common.base.MoreObjects;

import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

/**
 * @author SuperCoder7979
 */
@Mixin(GeneratorOptions.class)
public class MixinGeneratorOptions {
    @Unique private static final String MODERN_BETA_LEVEL_TYPE = ModernBeta.MOD_ID; // modern_beta
    @Unique private static final Optional<Integer> MODERN_BETA_VERSION = Optional.of(ModernBeta.MOD_VERSION);
    
    @Inject(method = "fromProperties", at = @At("HEAD"), cancellable = true)
    private static void injectServerGeneratorType(
        DynamicRegistryManager registryManager, 
        Properties properties,
        CallbackInfoReturnable<GeneratorOptions> info
    ) {
        // Exit if server.properties file not yet created
        if (properties.get("level-type") == null) {
            return;
        }

        String levelType = properties.get("level-type").toString().trim().toLowerCase();
        
        // Check for Modern Beta world type
        if (levelType.equals(MODERN_BETA_LEVEL_TYPE)) {
            
            // get or generate seed
            String seedField = (String) MoreObjects.firstNonNull(properties.get("level-seed"), "");
            long seed = new Random().nextLong();
            
            if (!seedField.isEmpty()) {
                try {
                    long parsedSeed = Long.parseLong(seedField);
                    if (parsedSeed != 0L) {
                        seed = parsedSeed;
                    }
                } catch (NumberFormatException var14) {
                    seed = seedField.hashCode();
                } 
            }
            
            Registry<DimensionType> dimensionRegistry = registryManager.get(Registry.DIMENSION_TYPE_KEY);
            Registry<ChunkGeneratorSettings> chunkGenSettingsRegistry = registryManager.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
            Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry = registryManager.get(Registry.NOISE_WORLDGEN);
            Registry<Biome> biomeRegistry = registryManager.get(Registry.BIOME_KEY);
            
            Registry<DimensionOptions> dimensionOptions = DimensionType.createDefaultDimensionOptions(registryManager, seed);

            String generate_structures = (String) properties.get("generate-structures");
            boolean generateStructures = generate_structures == null || Boolean.parseBoolean(generate_structures);
            
            String worldType = ModernBeta.GEN_CONFIG.worldType;
            String biomeType = ModernBeta.BIOME_CONFIG.biomeType;
            String caveBiomeType = ModernBeta.CAVE_BIOME_CONFIG.caveBiomeType;

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
            
            ChunkGenerator chunkGenerator = new OldChunkGenerator(
                noiseRegistry,
                new OldBiomeSource(
                    seed,
                    biomeRegistry,
                    biomeSettings,
                    caveBiomeSettings,
                    MODERN_BETA_VERSION
                ), 
                seed,
                chunkGenSettingsRegistry.getOrCreateEntry(RegistryKey.of(Registry.CHUNK_GENERATOR_SETTINGS_KEY, ModernBeta.createId(worldType))), 
                chunkSettings,
                MODERN_BETA_VERSION
            );
            
            // return our chunk generator
            info.setReturnValue(new GeneratorOptions(
                seed, 
                generateStructures, 
                false,
                GeneratorOptions.getRegistryWithReplacedOverworldGenerator(dimensionRegistry, dimensionOptions, chunkGenerator)));
        }
    }
}