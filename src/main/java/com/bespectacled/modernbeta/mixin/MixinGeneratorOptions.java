package com.bespectacled.modernbeta.mixin;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.registry.Registries;
import com.bespectacled.modernbeta.api.world.WorldProvider;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;
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

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Properties;
import java.util.Random;

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
            Registry<ChunkGeneratorSettings> registryChunkGenSettings = dynamicRegistryManager.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
            Registry<Biome> registryBiome = dynamicRegistryManager.get(Registry.BIOME_KEY);
            
            SimpleRegistry<DimensionOptions> dimensionOptions = DimensionType.createDefaultDimensionOptions(
                registryDimensionType,
                registryBiome, 
                registryChunkGenSettings, 
                seed
            );

            String generate_structures = (String) properties.get("generate-structures");
            boolean generateStructures = generate_structures == null || Boolean.parseBoolean(generate_structures);
            
            NbtCompound chunkProviderSettings = Registries.CHUNK_SETTINGS.get(ModernBeta.GEN_CONFIG.worldType).get();
            NbtCompound biomeProviderSettings = Registries.BIOME_SETTINGS.get(ModernBeta.BIOME_CONFIG.biomeType).get();
            
            WorldProvider worldProvider = Registries.WORLD.get(ModernBeta.GEN_CONFIG.worldType);
            
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
                GeneratorOptions.getRegistryWithReplacedOverworldGenerator(registryDimensionType, dimensionOptions, chunkGenerator)));
        }
    }
}