package mod.bespectacled.modernbeta.world.preset;

import java.util.Map;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGeneratorSettings;
import net.minecraft.class_8197;
import net.minecraft.class_8198;
import net.minecraft.nbt.NbtCompound;
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
        
        RegistryEntryLookup<ChunkGeneratorSettings> settingsLookup = presetRegisterable.getRegistryLookup(RegistryKeys.CHUNK_GENERATOR_SETTINGS);
        RegistryEntryLookup<Biome> biomeLookup = presetRegisterable.getRegistryLookup(RegistryKeys.BIOME);
        RegistryEntryLookup<class_8197> parameterLookup = presetRegisterable.getRegistryLookup(RegistryKeys.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST_WORLDGEN);
        
        RegistryEntry.Reference<DimensionType> dimensionNether = registryDimensionType.getOrThrow(DimensionTypes.THE_NETHER);
        RegistryEntry.Reference<ChunkGeneratorSettings> settingsNether = settingsLookup.getOrThrow(ChunkGeneratorSettings.NETHER);
        RegistryEntry.Reference<class_8197> parameterNether = parameterLookup.getOrThrow(class_8198.NETHER);
        DimensionOptions nether = new DimensionOptions(dimensionNether, new NoiseChunkGenerator(MultiNoiseBiomeSource.method_49503(parameterNether), settingsNether));
        
        RegistryEntry.Reference<DimensionType> dimensionEnd = registryDimensionType.getOrThrow(DimensionTypes.THE_END);
        RegistryEntry.Reference<ChunkGeneratorSettings> settingsEnd = settingsLookup.getOrThrow(ChunkGeneratorSettings.END);
        DimensionOptions end = new DimensionOptions(dimensionEnd, new NoiseChunkGenerator(TheEndBiomeSource.createVanilla(biomeLookup), settingsEnd));
        
        RegistryEntry.Reference<DimensionType> dimensionOverworld = registryDimensionType.getOrThrow(DimensionTypes.OVERWORLD);
        RegistryEntry.Reference<ChunkGeneratorSettings> settingsOverworld = settingsLookup.getOrThrow(ModernBetaChunkGeneratorSettings.MODERN_BETA);
        DimensionOptions overworld = new DimensionOptions(
            dimensionOverworld,
            new ModernBetaChunkGenerator(
                new ModernBetaBiomeSource(
                    biomeLookup,
                    new NbtCompound(),
                    new NbtCompound()
                ),
                settingsOverworld,
                new NbtCompound()
            )
        );
        
        presetRegisterable.register(
            MODERN_BETA,
            new WorldPreset(Map.of(DimensionOptions.OVERWORLD, overworld, DimensionOptions.NETHER, nether, DimensionOptions.END, end))
        );
        
    }
    
    private static RegistryKey<WorldPreset> keyOf(Identifier id) {
        return RegistryKey.of(RegistryKeys.WORLD_PRESET, id);
    }
}
