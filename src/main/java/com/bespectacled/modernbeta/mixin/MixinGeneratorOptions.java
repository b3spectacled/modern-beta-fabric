package com.bespectacled.modernbeta.mixin;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.BiomeType;
import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.gen.OldChunkGenerator;
import com.bespectacled.modernbeta.gen.OldGeneratorSettings;
import com.bespectacled.modernbeta.gen.WorldType;
import com.google.common.base.MoreObjects;
import net.minecraft.nbt.CompoundTag;
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
    @Inject(method = "fromProperties", at = @At("HEAD"), cancellable = true)
    private static void injectServerGeneratorType(DynamicRegistryManager dynamicRegistryManager, Properties properties,
            CallbackInfoReturnable<GeneratorOptions> cir) {

        // Exit if server.properties file not yet created
        if (properties.get("level-type") == null) {
            return;
        }

        String levelType = properties.get("level-type").toString().trim().toLowerCase();
        
        // Check for Modern Beta world type
        if (levelType.equals("beta") ||
            levelType.equals("skylands") ||
            levelType.equals("alpha") ||
            levelType.equals("infdev") ||
            levelType.equals("infdev_old") ||
            levelType.equals("indev")) {
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
            Registry<DimensionType> dimensions = dynamicRegistryManager.get(Registry.DIMENSION_TYPE_KEY);
            Registry<Biome> biomes = dynamicRegistryManager.get(Registry.BIOME_KEY);
            Registry<ChunkGeneratorSettings> chunkgens = dynamicRegistryManager.get(Registry.NOISE_SETTINGS_WORLDGEN);
            SimpleRegistry<DimensionOptions> dimensionOptions = DimensionType.createDefaultDimensionOptions(dimensions,
                    biomes, chunkgens, seed);

            String generate_structures = (String) properties.get("generate-structures");
            boolean generateStructures = generate_structures == null || Boolean.parseBoolean(generate_structures);
            
            WorldType worldType = WorldType.fromName(levelType);
            BiomeType biomeType = BiomeType.fromName(ModernBeta.BETA_CONFIG.biomeType);
            
            boolean genOceans = levelType.equals("skylands") ? false : ModernBeta.BETA_CONFIG.generateOceans;
            boolean isIndev = levelType.equals("indev");
            
            CompoundTag providerSettings = isIndev ? OldGeneratorSettings.createIndevSettings() : OldGeneratorSettings.createInfSettings(worldType, biomeType, genOceans);
            
            OldGeneratorSettings settings = new OldGeneratorSettings(OldGeneratorSettings.getChunkGeneratorSettings(worldType), providerSettings);
            ChunkGenerator generator = new OldChunkGenerator(new OldBiomeSource(seed, biomes, settings.providerSettings), seed, settings);

            // return our chunk generator
            cir.setReturnValue(new GeneratorOptions(seed, generateStructures, false,
                    GeneratorOptions.method_28608(dimensions, dimensionOptions, generator)));
        }
    }
}