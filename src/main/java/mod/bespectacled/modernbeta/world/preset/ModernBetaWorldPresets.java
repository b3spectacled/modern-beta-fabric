package mod.bespectacled.modernbeta.world.preset;

import java.util.Map;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import mod.bespectacled.modernbeta.api.registry.ModernBetaRegistries;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsPreset;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGeneratorSettings;
import net.minecraft.class_8197;
import net.minecraft.class_8198;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;

public class ModernBetaWorldPresets {
    public static final RegistryKey<WorldPreset> MODERN_BETA = keyOf(ModernBeta.createId(ModernBeta.MOD_ID));
            
    public static void bootstrap(Registerable<WorldPreset> presetRegisterable) {
        RegistryEntryLookup<DimensionType> registryDimensionType = presetRegisterable.getRegistryLookup(RegistryKeys.DIMENSION_TYPE);
        RegistryEntryLookup<ChunkGeneratorSettings> registrySettings = presetRegisterable.getRegistryLookup(RegistryKeys.CHUNK_GENERATOR_SETTINGS);
        RegistryEntryLookup<Biome> registryBiome = presetRegisterable.getRegistryLookup(RegistryKeys.BIOME);
        RegistryEntryLookup<class_8197> registryParameters = presetRegisterable.getRegistryLookup(RegistryKeys.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST_WORLDGEN);

        DimensionOptions overworld = createOverworldOptions(registryDimensionType, registrySettings, registryBiome);
        DimensionOptions nether = createNetherOptions(registryDimensionType, registrySettings, registryParameters);
        DimensionOptions end = createEndOptions(registryDimensionType, registrySettings, registryBiome);
        
        presetRegisterable.register(
            MODERN_BETA,
            new WorldPreset(Map.of(DimensionOptions.OVERWORLD, overworld, DimensionOptions.NETHER, nether, DimensionOptions.END, end))
        );
    }
    
    private static DimensionOptions createOverworldOptions(
        RegistryEntryLookup<DimensionType> registryDimensionType,
        RegistryEntryLookup<ChunkGeneratorSettings> registrySettings,
        RegistryEntryLookup<Biome> registryBiome
    ) {
        RegistryEntry.Reference<DimensionType> dimensionType = registryDimensionType.getOrThrow(DimensionTypes.OVERWORLD);
        RegistryEntry.Reference<ChunkGeneratorSettings> settings = registrySettings.getOrThrow(ModernBetaChunkGeneratorSettings.BETA);
        
        ModernBetaSettingsPreset defaultPreset = ModernBetaRegistries.SETTINGS_PRESET.get(ModernBetaBuiltInTypes.Chunk.BETA.id);
        
        return new DimensionOptions(
            dimensionType,
            new ModernBetaChunkGenerator(
                new ModernBetaBiomeSource(
                    registryBiome,
                    defaultPreset.getNbtBiome(),
                    defaultPreset.getNbtCaveBiome()
                ),
                settings,
                defaultPreset.getNbtChunk()
            )
        );
    }
    
    private static DimensionOptions createNetherOptions(
        RegistryEntryLookup<DimensionType> registryDimensionType,
        RegistryEntryLookup<ChunkGeneratorSettings> registrySettings,
        RegistryEntryLookup<class_8197> registryParameters
    ) {
        RegistryEntry.Reference<DimensionType> dimensionType = registryDimensionType.getOrThrow(DimensionTypes.THE_NETHER);
        RegistryEntry.Reference<ChunkGeneratorSettings> settings = registrySettings.getOrThrow(ChunkGeneratorSettings.NETHER);
        RegistryEntry.Reference<class_8197> parameters = registryParameters.getOrThrow(class_8198.NETHER);
        
        return new DimensionOptions(dimensionType, new NoiseChunkGenerator(MultiNoiseBiomeSource.method_49503(parameters), settings));
    }
    
    private static DimensionOptions createEndOptions(
        RegistryEntryLookup<DimensionType> registryDimensionType,
        RegistryEntryLookup<ChunkGeneratorSettings> registrySettings,
        RegistryEntryLookup<Biome> registryBiome
    ) {
        RegistryEntry.Reference<DimensionType> dimensionType = registryDimensionType.getOrThrow(DimensionTypes.THE_END);
        RegistryEntry.Reference<ChunkGeneratorSettings> settings = registrySettings.getOrThrow(ChunkGeneratorSettings.END);

        return new DimensionOptions(dimensionType, new NoiseChunkGenerator(TheEndBiomeSource.createVanilla(registryBiome), settings));
    }
    
    private static RegistryKey<WorldPreset> keyOf(Identifier id) {
        return RegistryKey.of(RegistryKeys.WORLD_PRESET, id);
    }
}
