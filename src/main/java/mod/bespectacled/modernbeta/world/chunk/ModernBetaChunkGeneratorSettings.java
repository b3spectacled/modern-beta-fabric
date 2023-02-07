package mod.bespectacled.modernbeta.world.chunk;

import java.util.List;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.util.BlockStates;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.noise.NoiseParametersKeys;
import net.minecraft.world.gen.noise.NoiseRouter;
import net.minecraft.world.gen.surfacebuilder.MaterialRules.SequenceMaterialRule;

public class ModernBetaChunkGeneratorSettings {
    public static final RegistryKey<ChunkGeneratorSettings> MODERN_BETA = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModernBeta.createId(ModernBeta.MOD_ID));
    
    public static void bootstrap(Registerable<ChunkGeneratorSettings> settingsRegisterable) {
        GenerationShapeConfig shapeConfig = GenerationShapeConfig.create(-64, 192, 1, 2);
        ChunkGeneratorSettings settings = new ChunkGeneratorSettings(
            shapeConfig,
            BlockStates.STONE,
            BlockStates.WATER,
            createDensityFunctions(settingsRegisterable.getRegistryLookup(RegistryKeys.NOISE_PARAMETERS)),
            new SequenceMaterialRule(List.of()),
            List.of(),
            64,
            false,
            true,
            false,
            true
        );
        
        settingsRegisterable.register(MODERN_BETA, settings);
    }
    
    private static NoiseRouter createDensityFunctions(RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> noiseParametersLookup) {
        DensityFunction aquiferBarrier = DensityFunctionTypes.noise(noiseParametersLookup.getOrThrow(NoiseParametersKeys.AQUIFER_BARRIER), 0.5);
        DensityFunction aquiferFloodedness = DensityFunctionTypes.noise(noiseParametersLookup.getOrThrow(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_FLOODEDNESS), 0.67);
        DensityFunction aquiferSpread = DensityFunctionTypes.noise(noiseParametersLookup.getOrThrow(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_SPREAD), 0.7142857142857143);
        DensityFunction aquiferLava = DensityFunctionTypes.noise(noiseParametersLookup.getOrThrow(NoiseParametersKeys.AQUIFER_LAVA));
        
        return new NoiseRouter(
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
}
