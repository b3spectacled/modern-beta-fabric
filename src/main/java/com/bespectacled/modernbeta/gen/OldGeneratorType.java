package com.bespectacled.modernbeta.gen;

import java.util.Optional;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.BiomeType;
import com.bespectacled.modernbeta.biome.CaveBiomeType;
import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.mixin.client.MixinGeneratorTypeAccessor;
import com.bespectacled.modernbeta.mixin.client.MixinMoreOptionsDialogInvoker;
import com.google.common.collect.ImmutableMap;

import net.minecraft.client.world.GeneratorType;
import net.minecraft.client.world.GeneratorType.ScreenProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
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
        NbtCompound biomeProviderSettings,
        NbtCompound chunkProviderSettings
    ) {
        WorldType worldType = WorldType.fromName(chunkProviderSettings.getString("worldType"));
    
        Registry<DimensionType> registryDimensionType = registryManager.<DimensionType>get(Registry.DIMENSION_TYPE_KEY);
        Registry<ChunkGeneratorSettings> registryChunkGenSettings = registryManager.<ChunkGeneratorSettings>get(Registry.NOISE_SETTINGS_WORLDGEN);
        Registry<Biome> registryBiome = registryManager.<Biome>get(Registry.BIOME_KEY); 
        Supplier<ChunkGeneratorSettings> chunkGenSettingsSupplier = () -> registryChunkGenSettings.get(ModernBeta.createId(worldType.getName()));
       
        OldBiomeSource biomeSource = new OldBiomeSource(generatorOptions.getSeed(), registryBiome, biomeProviderSettings);
        OldGeneratorSettings oldGeneratorSettings = new OldGeneratorSettings(chunkGenSettingsSupplier, chunkProviderSettings); 
        
        return new GeneratorOptions(
            generatorOptions.getSeed(),
            generatorOptions.shouldGenerateStructures(),
            generatorOptions.hasBonusChest(),
            GeneratorOptions.getRegistryWithReplacedOverworldGenerator(
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
                NbtCompound biomeProviderSettings = OldGeneratorSettings.createBiomeSettings(BiomeType.BETA, CaveBiomeType.VANILLA, WorldType.BETA.getDefaultBiome());
                NbtCompound chunkProviderSettings = OldGeneratorSettings.createInfSettings(WorldType.BETA);
                
                OldGeneratorSettings oldGeneratorSettings = new OldGeneratorSettings(generatorSettings, chunkProviderSettings);
                
                return new OldChunkGenerator(new OldBiomeSource(seed, biomes, biomeProviderSettings), seed, oldGeneratorSettings);
            }
        };
        
        MixinGeneratorTypeAccessor.setScreenProviders(
            new ImmutableMap.Builder<Optional<GeneratorType>, ScreenProvider>()
                .putAll(MixinGeneratorTypeAccessor.getScreenProviders())
                .put(
                    Optional.<GeneratorType>of(OLD), (screen, generatorOptions) -> {
                        ChunkGenerator chunkGenerator = generatorOptions.getChunkGenerator();
                        BiomeSource biomeSource = chunkGenerator.getBiomeSource();
                        
                        // If settings already present, create new compound tag and copy from source,
                        // otherwise, not copying will modify original settings.
                        NbtCompound biomeSettings = biomeSource instanceof OldBiomeSource ? 
                            (new NbtCompound()).copyFrom(((OldBiomeSource)biomeSource).getProviderSettings()) : 
                            OldGeneratorSettings.createBiomeSettings(BiomeType.BETA, CaveBiomeType.VANILLA, WorldType.BETA.getDefaultBiome());
                        
                        NbtCompound chunkSettings = chunkGenerator instanceof OldChunkGenerator ?
                            (new NbtCompound()).copyFrom(((OldChunkGenerator)chunkGenerator).getProviderSettings()) :
                            OldGeneratorSettings.createInfSettings(WorldType.BETA);
                        
                        WorldType worldType = WorldType.fromName(chunkSettings.getString("worldType"));
                        
                        return worldType.createLevelScreen(
                            screen,
                            screen.moreOptionsDialog.getRegistryManager(),
                            biomeSettings,
                            chunkSettings,
                            ((biomeProviderSettings, chunkProviderSettings) -> ((MixinMoreOptionsDialogInvoker)screen.moreOptionsDialog).invokeSetGeneratorOptions(
                                createNewGeneratorOptions(
                                    screen.moreOptionsDialog.getRegistryManager(),
                                    generatorOptions,
                                    biomeProviderSettings,
                                    chunkProviderSettings
                            )))
                        );
                    }
                )
                .build()
        );
    }
}
