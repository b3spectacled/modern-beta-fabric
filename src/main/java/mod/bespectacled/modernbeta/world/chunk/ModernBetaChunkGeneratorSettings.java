package mod.bespectacled.modernbeta.world.chunk;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;
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

    public static final Identifier BETA;
    public static final Identifier ALPHA;
    public static final Identifier SKYLANDS;
    public static final Identifier INFDEV_611;
    public static final Identifier INFDEV_420;
    public static final Identifier INFDEV_415;
    public static final Identifier INFDEV_227;
    public static final Identifier INDEV;
    public static final Identifier CLASSIC_0_30;
    public static final Identifier BETA_ISLANDS;
    public static final Identifier PE;
    
    public static final ChunkGeneratorSettings BETA_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings ALPHA_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings SKYLANDS_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings INFDEV_611_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings INFDEV_420_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings INFDEV_415_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings INFDEV_227_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings INDEV_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings CLASSIC_0_30_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings BETA_ISLANDS_GENERATOR_SETTINGS;
    public static final ChunkGeneratorSettings PE_GENERATOR_SETTINGS;

    public static void register() {
        register(BETA, BETA_GENERATOR_SETTINGS);
        register(SKYLANDS, SKYLANDS_GENERATOR_SETTINGS);
        register(ALPHA, ALPHA_GENERATOR_SETTINGS);
        register(INFDEV_611, INFDEV_611_GENERATOR_SETTINGS);
        register(INFDEV_420, INFDEV_420_GENERATOR_SETTINGS);
        register(INFDEV_415, INFDEV_415_GENERATOR_SETTINGS);
        register(INFDEV_227, INFDEV_227_GENERATOR_SETTINGS);
        register(INDEV, INDEV_GENERATOR_SETTINGS);
        register(CLASSIC_0_30, CLASSIC_0_30_GENERATOR_SETTINGS);
        register(BETA_ISLANDS, BETA_ISLANDS_GENERATOR_SETTINGS);
        register(PE, PE_GENERATOR_SETTINGS);
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
        BETA = ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.BETA.name);
        ALPHA = ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.ALPHA.name);
        SKYLANDS = ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.SKYLANDS.name);
        INFDEV_611 = ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.INFDEV_611.name);
        INFDEV_420 = ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.INFDEV_420.name);
        INFDEV_415 = ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.INFDEV_415.name);
        INFDEV_227 = ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.INFDEV_227.name);
        INDEV = ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.INDEV.name);
        CLASSIC_0_30 = ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.CLASSIC_0_30.name);
        BETA_ISLANDS = ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.BETA_ISLANDS.name);
        PE = ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.PE.name);

        BETA_GENERATOR_SETTINGS = new ChunkGeneratorSettings(
            ModernBetaGeneratorConfig.BETA_SHAPE_CONFIG,
            BlockStates.STONE,
            BlockStates.WATER,
            createDensityFunctions(ModernBetaGeneratorConfig.BETA_SHAPE_CONFIG, false),
            ModernBetaSurfaceRules.createVanilla(true),
            64,
            false, true, true, true
        );
        
        ALPHA_GENERATOR_SETTINGS = new ChunkGeneratorSettings(
            ModernBetaGeneratorConfig.ALPHA_SHAPE_CONFIG,
            BlockStates.STONE,
            BlockStates.WATER,
            createDensityFunctions(ModernBetaGeneratorConfig.ALPHA_SHAPE_CONFIG, false),
            ModernBetaSurfaceRules.createVanilla(true),
            64,
            false, true, true, true
        );
        
        SKYLANDS_GENERATOR_SETTINGS = new ChunkGeneratorSettings(
            ModernBetaGeneratorConfig.SKYLANDS_SHAPE_CONFIG,
            BlockStates.STONE,
            BlockStates.AIR,
            createDensityFunctions(ModernBetaGeneratorConfig.SKYLANDS_SHAPE_CONFIG, false),
            ModernBetaSurfaceRules.createVanilla(false),
            0,
            false, false, false, true
        );
        
        INFDEV_611_GENERATOR_SETTINGS = new ChunkGeneratorSettings(
            ModernBetaGeneratorConfig.INFDEV_611_SHAPE_CONFIG,
            BlockStates.STONE,
            BlockStates.WATER,
            createDensityFunctions(ModernBetaGeneratorConfig.ALPHA_SHAPE_CONFIG, false),
            ModernBetaSurfaceRules.createVanilla(true),
            64,
            false, true, true, true
        );
        
        INFDEV_420_GENERATOR_SETTINGS = new ChunkGeneratorSettings(
            ModernBetaGeneratorConfig.INFDEV_420_SHAPE_CONFIG,
            BlockStates.STONE,
            BlockStates.WATER,
            createDensityFunctions(ModernBetaGeneratorConfig.INFDEV_420_SHAPE_CONFIG, false),
            ModernBetaSurfaceRules.createVanilla(true),
            64,
            false, true, true, true
        );
        
        INFDEV_415_GENERATOR_SETTINGS = new ChunkGeneratorSettings(
            ModernBetaGeneratorConfig.INFDEV_415_SHAPE_CONFIG,
            BlockStates.STONE,
            BlockStates.WATER,
            createDensityFunctions(ModernBetaGeneratorConfig.INFDEV_415_SHAPE_CONFIG, false),
            ModernBetaSurfaceRules.createVanilla(true),
            64,
            false, true, true, true
        );
        
        INFDEV_227_GENERATOR_SETTINGS = new ChunkGeneratorSettings(
            ModernBetaGeneratorConfig.BETA_SHAPE_CONFIG,
            BlockStates.STONE,
            BlockStates.WATER,
            createDensityFunctions(ModernBetaGeneratorConfig.INDEV_SHAPE_CONFIG, false),
            ModernBetaSurfaceRules.createVanilla(true),
            64,
            false, false, false, false
        );
        
        INDEV_GENERATOR_SETTINGS = new ChunkGeneratorSettings(
            ModernBetaGeneratorConfig.INDEV_SHAPE_CONFIG,
            BlockStates.STONE,
            BlockStates.WATER,
            createDensityFunctions(ModernBetaGeneratorConfig.INDEV_SHAPE_CONFIG, false),
            ModernBetaSurfaceRules.createVanilla(true),
            64,
            false, false, false, false
        );
        
        CLASSIC_0_30_GENERATOR_SETTINGS = new ChunkGeneratorSettings(
            ModernBetaGeneratorConfig.INDEV_SHAPE_CONFIG,
            BlockStates.STONE,
            BlockStates.WATER,
            createDensityFunctions(ModernBetaGeneratorConfig.INDEV_SHAPE_CONFIG, false),
            ModernBetaSurfaceRules.createVanilla(true),
            64,
            false, false, false, false
        );
        
        BETA_ISLANDS_GENERATOR_SETTINGS = new ChunkGeneratorSettings(
            ModernBetaGeneratorConfig.BETA_SHAPE_CONFIG,
            BlockStates.STONE,
            BlockStates.WATER,
            createDensityFunctions(ModernBetaGeneratorConfig.BETA_SHAPE_CONFIG, false),
            ModernBetaSurfaceRules.createVanilla(true),
            64,
            false, true, true, true
        );
        
        PE_GENERATOR_SETTINGS = new ChunkGeneratorSettings(
            ModernBetaGeneratorConfig.PE_SHAPE_CONFIG,
            BlockStates.STONE,
            BlockStates.WATER,
            createDensityFunctions(ModernBetaGeneratorConfig.PE_SHAPE_CONFIG, false),
            ModernBetaSurfaceRules.createVanilla(true),
            64,
            false, true, true, true
        );
    }
}
