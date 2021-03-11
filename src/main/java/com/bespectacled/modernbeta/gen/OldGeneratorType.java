package com.bespectacled.modernbeta.gen;

import java.util.Optional;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.BiomeType;
import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.gui.InfCustomizeLevelScreen;
import com.bespectacled.modernbeta.mixin.client.MixinGeneratorTypeAccessor;
import com.bespectacled.modernbeta.mixin.client.MixinMoreOptionsDialogInvoker;
import com.google.common.collect.ImmutableMap;

import net.minecraft.client.world.GeneratorType;
import net.minecraft.client.world.GeneratorType.ScreenProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class OldGeneratorType {
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
        CompoundTag providerSettings
    ) {
        WorldType worldType = WorldType.fromName(providerSettings.getString("worldType"));
    
        Registry<DimensionType> registryDimensionType = registryManager.<DimensionType>get(Registry.DIMENSION_TYPE_KEY);
        Registry<ChunkGeneratorSettings> registryChunkGenSettings = registryManager.<ChunkGeneratorSettings>get(Registry.NOISE_SETTINGS_WORLDGEN);
        Registry<Biome> registryBiome = registryManager.<Biome>get(Registry.BIOME_KEY); 
        Supplier<ChunkGeneratorSettings> chunkGenSettingsSupplier = () -> registryChunkGenSettings.get(ModernBeta.createId(worldType.getName()));
       
        OldBiomeSource biomeSource = new OldBiomeSource(generatorOptions.getSeed(), registryBiome, providerSettings);
        OldGeneratorSettings oldGeneratorSettings = new OldGeneratorSettings(chunkGenSettingsSupplier, providerSettings);
        
        return new GeneratorOptions(
            generatorOptions.getSeed(),
            generatorOptions.shouldGenerateStructures(),
            generatorOptions.hasBonusChest(),
            GeneratorOptions.method_28608(
                registryDimensionType, 
                generatorOptions.getDimensions(), 
                new OldChunkGenerator(biomeSource, generatorOptions.getSeed(), oldGeneratorSettings))
        );
    }

    static {
        OLD = new GeneratorType("old") {
            @Override
            protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> registryChunkGenSettings, long seed) {
                Supplier<ChunkGeneratorSettings> generatorSettings = () -> registryChunkGenSettings.get(ModernBeta.createId(WorldType.BETA.getName()));
                CompoundTag providerSettings = OldGeneratorSettings.createInfSettings(
                    WorldType.BETA, 
                    BiomeType.BETA, 
                    ModernBeta.BETA_CONFIG.generateOceans,
                    ModernBeta.BETA_CONFIG.generateNoiseCaves,
                    ModernBeta.BETA_CONFIG.generateAquifers,
                    ModernBeta.BETA_CONFIG.generateDeepslate
                );
                
                OldGeneratorSettings oldGeneratorSettings = new OldGeneratorSettings(generatorSettings, providerSettings);
                
                return new OldChunkGenerator(new OldBiomeSource(seed, biomes, oldGeneratorSettings.providerSettings), seed, oldGeneratorSettings);
            }
        };
        
        MixinGeneratorTypeAccessor.setScreenProviders(
            new ImmutableMap.Builder<Optional<GeneratorType>, ScreenProvider>()
                .putAll(MixinGeneratorTypeAccessor.getScreenProviders())
                .put(
                    Optional.<GeneratorType>of(OLD), (screen, generatorOptions) -> {                        
                        return new InfCustomizeLevelScreen(
                            screen, 
                            OldGeneratorSettings.createInfSettings(
                                WorldType.BETA, 
                                BiomeType.BETA, 
                                ModernBeta.BETA_CONFIG.generateOceans,
                                ModernBeta.BETA_CONFIG.generateNoiseCaves,
                                ModernBeta.BETA_CONFIG.generateAquifers,
                                ModernBeta.BETA_CONFIG.generateDeepslate
                            ),
                            ((providerSettings) -> ((MixinMoreOptionsDialogInvoker)screen.moreOptionsDialog).invokeSetGeneratorOptions(
                                createNewGeneratorOptions(
                                    screen.moreOptionsDialog.getRegistryManager(),
                                    generatorOptions,
                                    providerSettings
                            )))
                        );
                    }
                )
                .build()
        );
    }
}
