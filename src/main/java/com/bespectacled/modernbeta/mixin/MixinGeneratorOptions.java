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
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.biome.provider.settings.BiomeProviderSettings;
import com.bespectacled.modernbeta.world.cavebiome.provider.settings.CaveBiomeProviderSettings;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;
import com.bespectacled.modernbeta.world.gen.provider.settings.ChunkProviderSettings;
import com.google.common.base.MoreObjects;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
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
    
    @Inject(method = "fromProperties", at = @At("HEAD"), cancellable = true)
    private static void injectServerGeneratorType(
        DynamicRegistryManager registryManager, 
        Properties properties,
        CallbackInfoReturnable<GeneratorOptions> cir
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
            
            SimpleRegistry<DimensionOptions> dimensionOptions = DimensionType.method_39540(registryManager, seed);

            String generate_structures = (String) properties.get("generate-structures");
            boolean generateStructures = generate_structures == null || Boolean.parseBoolean(generate_structures);
            
            String worldType = ModernBeta.GEN_CONFIG.generalGenConfig.worldType;
            String biomeType = ModernBeta.BIOME_CONFIG.generalBiomeConfig.biomeType;
            String caveBiomeType = ModernBeta.CAVE_BIOME_CONFIG.generalBiomeConfig.caveBiomeType;
            
            NbtCompound chunkSettings = Registries.CHUNK_SETTINGS.getOrElse(
                worldType, 
                () -> ChunkProviderSettings.createSettingsBase(worldType)
            ).get();
            
            NbtCompound biomeSettings = Registries.BIOME_SETTINGS.getOrElse(
                biomeType, 
                () -> BiomeProviderSettings.createSettingsBase(biomeType)
            ).get();
            
            NbtCompound caveBiomeSettings = Registries.CAVE_BIOME_SETTINGS.getOrElse(
                caveBiomeType, 
                () -> CaveBiomeProviderSettings.createSettingsBase(caveBiomeType)
            ).get();
            
            ChunkGenerator chunkGenerator = new OldChunkGenerator(
                noiseRegistry,
                new OldBiomeSource(seed, biomeRegistry, biomeSettings, Optional.of(caveBiomeSettings)), 
                seed,
                () -> chunkGenSettingsRegistry.get(ModernBeta.createId(worldType)), 
                chunkSettings
            );
            
            // return our chunk generator
            cir.setReturnValue(new GeneratorOptions(
                seed, 
                generateStructures, 
                false,
                GeneratorOptions.getRegistryWithReplacedOverworldGenerator(dimensionRegistry, dimensionOptions, chunkGenerator)));
        }
    }
}