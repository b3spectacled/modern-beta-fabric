package com.bespectacled.modernbeta.mixin;

import java.util.Properties;
import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.registry.Registries;
import com.bespectacled.modernbeta.api.world.WorldProvider;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.biome.provider.settings.BiomeProviderSettings;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;
import com.bespectacled.modernbeta.world.gen.provider.settings.ChunkProviderSettings;
import com.google.common.base.MoreObjects;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
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
        DynamicRegistryManager dynamicRegistryManager, 
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

            // get other misc data
            Registry<DimensionType> registryDimensionType = dynamicRegistryManager.get(Registry.DIMENSION_TYPE_KEY);
            Registry<ChunkGeneratorSettings> registryChunkGenSettings = dynamicRegistryManager.get(Registry.NOISE_SETTINGS_WORLDGEN);
            Registry<Biome> registryBiome = dynamicRegistryManager.get(Registry.BIOME_KEY);
            
            SimpleRegistry<DimensionOptions> dimensionOptions = DimensionType.createDefaultDimensionOptions(
                registryDimensionType,
                registryBiome, 
                registryChunkGenSettings, 
                seed
            );

            String generate_structures = (String) properties.get("generate-structures");
            boolean generateStructures = generate_structures == null || Boolean.parseBoolean(generate_structures);
            
            String worldType = ModernBeta.GEN_CONFIG.generalGenConfig.worldType;
            String biomeType = ModernBeta.BIOME_CONFIG.generalBiomeConfig.biomeType;
            
            NbtCompound chunkProviderSettings = Registries.CHUNK_SETTINGS.getOrElse(
                worldType, 
                () -> ChunkProviderSettings.createSettingsBase(worldType)
            ).get();
            
            NbtCompound biomeProviderSettings = Registries.BIOME_SETTINGS.getOrElse(
                biomeType, 
                () -> BiomeProviderSettings.createSettingsBase(biomeType)
            ).get();
            
            WorldProvider worldProvider = Registries.WORLD.getOrDefault(ModernBeta.GEN_CONFIG.generalGenConfig.worldType);
            
            ChunkGenerator chunkGenerator = new OldChunkGenerator(
                new OldBiomeSource(seed, registryBiome, biomeProviderSettings), 
                seed,
                () -> registryChunkGenSettings.get(new Identifier(worldProvider.getChunkGenSettings())), 
                chunkProviderSettings
            );
            
            // return our chunk generator
            cir.setReturnValue(new GeneratorOptions(
                seed, 
                generateStructures, 
                false,
                GeneratorOptions.method_28608(registryDimensionType, dimensionOptions, chunkGenerator)));
        }
    }
}