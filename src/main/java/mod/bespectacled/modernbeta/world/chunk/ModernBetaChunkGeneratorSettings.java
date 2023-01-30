package mod.bespectacled.modernbeta.world.chunk;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.util.BlockStates;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.noise.NoiseParametersKeys;
import net.minecraft.world.gen.noise.SimpleNoiseRouter;
import net.minecraft.world.gen.random.ChunkRandom;

public class ModernBetaChunkGeneratorSettings {
    public static final ChunkRandom.RandomProvider RANDOM_TYPE = ChunkRandom.RandomProvider.LEGACY;
    
    public static final Identifier MODERN_BETA;
    public static final ChunkGeneratorSettings MODERN_BETA_GENERATOR_SETTINGS;

    public static void register() {
        register(MODERN_BETA, MODERN_BETA_GENERATOR_SETTINGS);
    }
    
    private static ChunkGeneratorSettings register(Identifier id, ChunkGeneratorSettings settings) {
        BuiltinRegistries.add(BuiltinRegistries.CHUNK_GENERATOR_SETTINGS, id, settings);
        return settings;
    }
    
    private static RegistryEntry<DoublePerlinNoiseSampler.NoiseParameters> getNoiseParameter(RegistryKey<DoublePerlinNoiseSampler.NoiseParameters> arg) {
        return BuiltinRegistries.NOISE_PARAMETERS.entryOf(arg);
    }
    
    public static SimpleNoiseRouter createDensityFunctions(GenerationShapeConfig shapeConfig, boolean amplified) {
        DensityFunction aquiferBarrier = DensityFunctionTypes.noise(getNoiseParameter(NoiseParametersKeys.AQUIFER_BARRIER), 0.5);
        DensityFunction aquiferFloodedness = DensityFunctionTypes.noise(getNoiseParameter(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_FLOODEDNESS), 0.67);
        DensityFunction aquiferSpread = DensityFunctionTypes.noise(getNoiseParameter(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_SPREAD), 0.7142857142857143);
        DensityFunction aquiferLava = DensityFunctionTypes.noise(getNoiseParameter(NoiseParametersKeys.AQUIFER_LAVA));
        
        return new SimpleNoiseRouter(
            aquiferBarrier,              // Barrier noise
            aquiferFloodedness,          // Fluid level floodedness noise
            aquiferSpread,               // Fluid level spread noise
            aquiferLava,                 // Lava noise
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
    
    static {
        MODERN_BETA = ModernBeta.createId(ModernBeta.MOD_ID);
        
        MODERN_BETA_GENERATOR_SETTINGS = new ChunkGeneratorSettings(
            ModernBetaChunkGeneratorConfig.MODERN_BETA_SHAPE_CONFIG,
            BlockStates.STONE,
            BlockStates.WATER,
            createDensityFunctions(ModernBetaChunkGeneratorConfig.MODERN_BETA_SHAPE_CONFIG, false),
            ModernBetaSurfaceRules.createVanilla(true),
            64,
            false,
            true,
            true,
            true
        );
    }
}
