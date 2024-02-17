package mod.bespectacled.modernbeta.world.chunk;

import java.util.List;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import mod.bespectacled.modernbeta.util.BlockStates;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry.Reference;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler.NoiseParameters;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.noise.NoiseParametersKeys;
import net.minecraft.world.gen.noise.NoiseRouter;
import net.minecraft.world.gen.surfacebuilder.MaterialRules.SequenceMaterialRule;
import net.minecraft.world.gen.surfacebuilder.VanillaSurfaceRules;

public class ModernBetaChunkGeneratorSettings {
    @Deprecated
    public static final RegistryKey<ChunkGeneratorSettings> MODERN_BETA;
    public static final RegistryKey<ChunkGeneratorSettings> BETA;
    public static final RegistryKey<ChunkGeneratorSettings> ALPHA;
    public static final RegistryKey<ChunkGeneratorSettings> SKYLANDS;
    public static final RegistryKey<ChunkGeneratorSettings> INFDEV_611;
    public static final RegistryKey<ChunkGeneratorSettings> INFDEV_420;
    public static final RegistryKey<ChunkGeneratorSettings> INFDEV_415;
    public static final RegistryKey<ChunkGeneratorSettings> INFDEV_227;
    public static final RegistryKey<ChunkGeneratorSettings> INDEV;
    public static final RegistryKey<ChunkGeneratorSettings> CLASSIC_0_30;
    public static final RegistryKey<ChunkGeneratorSettings> PE;
    public static final RegistryKey<ChunkGeneratorSettings> EARLY_RELEASE;

    @SuppressWarnings("deprecation")
    public static void bootstrap(Registerable<ChunkGeneratorSettings> settingsRegisterable) {
        settingsRegisterable.register(MODERN_BETA, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.MODERN_BETA, 64, true));
        settingsRegisterable.register(BETA, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.BETA, 64, true));
        settingsRegisterable.register(ALPHA, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.ALPHA, 64, true));
        settingsRegisterable.register(SKYLANDS, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.SKYLANDS, 0, false));
        settingsRegisterable.register(INFDEV_611, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.INFDEV_611, 64, true));
        settingsRegisterable.register(INFDEV_420, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.INFDEV_420, 64, true));
        settingsRegisterable.register(INFDEV_415, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.INFDEV_415, 64, true));
        settingsRegisterable.register(INFDEV_227, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.INFDEV_227, 64, true));
        settingsRegisterable.register(INDEV, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.INDEV, 64, false));
        settingsRegisterable.register(CLASSIC_0_30, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.CLASSIC_0_30, 64, false));
        settingsRegisterable.register(PE, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.PE, 64, true));
        settingsRegisterable.register(EARLY_RELEASE, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.EARLY_RELEASE, 63, true));
    }
    
    private static NoiseRouter createDensityFunctions(
        RegistryEntryLookup<DensityFunction> densityFunctionLookup,
        RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> noiseParametersLookup
    ) {
        Reference<NoiseParameters> aquiferBarrier = noiseParametersLookup.getOrThrow(NoiseParametersKeys.AQUIFER_BARRIER);
        Reference<NoiseParameters> aquiferFloodedness = noiseParametersLookup.getOrThrow(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_FLOODEDNESS);
        Reference<NoiseParameters> aquiferSpread = noiseParametersLookup.getOrThrow(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_SPREAD);
        Reference<NoiseParameters> aquiferLava = noiseParametersLookup.getOrThrow(NoiseParametersKeys.AQUIFER_LAVA);
        
        DensityFunction functionAquiferBarrier = DensityFunctionTypes.noise(aquiferBarrier, 0.5);
        DensityFunction functionAquiferFloodedness = DensityFunctionTypes.noise(aquiferFloodedness, 0.67);
        DensityFunction functionAquiferSpread = DensityFunctionTypes.noise(aquiferSpread, 0.7142857142857143);
        DensityFunction functionAquiferLava = DensityFunctionTypes.noise(aquiferLava);
        
        return new NoiseRouter(
            functionAquiferBarrier,      // Barrier noise
            functionAquiferFloodedness,  // Fluid level floodedness noise
            functionAquiferSpread,       // Fluid level spread noise
            functionAquiferLava,         // Lava noise
            DensityFunctionTypes.zero(), // Temperature
            DensityFunctionTypes.zero(), // Vegetation
            DensityFunctionTypes.zero(), // Continents
            DensityFunctionTypes.zero(), // Erosion
            DensityFunctionTypes.zero(), // Depth
            DensityFunctionTypes.zero(), // Ridges
            DensityFunctionTypes.zero(), // Initial Density
            DensityFunctionTypes.zero(), // Final Density
            DensityFunctionTypes.zero(), // Vein Toggle
            DensityFunctionTypes.zero(), // Vein Ridged
            DensityFunctionTypes.zero()  // Vein Gap
        );
    }
    
    private static ChunkGeneratorSettings createGeneratorSettings(
        Registerable<ChunkGeneratorSettings> settingsRegisterable,
        GenerationShapeConfig shapeConfig,
        int seaLevel,
        boolean useAquifers
    ) {
        RegistryEntryLookup<DensityFunction> densityFunctionLookup = settingsRegisterable.getRegistryLookup(RegistryKeys.DENSITY_FUNCTION);
        RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> noiseParametersLookup = settingsRegisterable.getRegistryLookup(RegistryKeys.NOISE_PARAMETERS);
        
        return new ChunkGeneratorSettings(
            shapeConfig,
            BlockStates.STONE,
            BlockStates.WATER,
            createDensityFunctions(densityFunctionLookup, noiseParametersLookup),
            VanillaSurfaceRules.createDefaultRule(false, false, true),
            List.of(),
            seaLevel,
            false,
            useAquifers,
            false,
            true
        );
    }

    static {
        MODERN_BETA = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernBeta.createId(ModernBeta.MOD_ID));
        BETA = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.BETA.id));
        ALPHA = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.ALPHA.id));
        SKYLANDS = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.SKYLANDS.id));
        INFDEV_611 = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.INFDEV_611.id));
        INFDEV_420 = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.INFDEV_420.id));
        INFDEV_415 = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.INFDEV_415.id));
        INFDEV_227 = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.INFDEV_227.id));
        INDEV = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.INDEV.id));
        CLASSIC_0_30 = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.CLASSIC_0_30.id));
        PE = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.PE.id));
        EARLY_RELEASE = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.EARLY_RELEASE.id));
    }
}
