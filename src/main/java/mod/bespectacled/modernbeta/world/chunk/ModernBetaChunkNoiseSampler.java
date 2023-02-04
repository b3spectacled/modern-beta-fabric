package mod.bespectacled.modernbeta.world.chunk;

import mod.bespectacled.modernbeta.api.world.chunk.ChunkProvider;
import mod.bespectacled.modernbeta.api.world.chunk.ChunkProviderNoise;
import mod.bespectacled.modernbeta.util.chunk.ChunkHeightmap;
import mod.bespectacled.modernbeta.util.noise.SimpleDensityFunction;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.AquiferSampler.FluidLevelSampler;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.noise.NoiseConfig;

public class ModernBetaChunkNoiseSampler extends ChunkNoiseSampler {
    private static final int OCEAN_HEIGHT_OFFSET = -8;
    
    private final ChunkProvider chunkProvider;
    
    public static ModernBetaChunkNoiseSampler create(
        Chunk chunk,
        NoiseConfig noiseConfig,
        ChunkGeneratorSettings chunkGeneratorSettings,
        AquiferSampler.FluidLevelSampler fluidLevelSampler,
        ChunkProvider chunkProvider
    ) {
        GenerationShapeConfig shapeConfig = chunkGeneratorSettings.generationShapeConfig().trimHeight(chunk);
        ChunkPos chunkPos = chunk.getPos();
        
        int horizontalSize = 16 / shapeConfig.horizontalBlockSize();
        
        return new ModernBetaChunkNoiseSampler(
            horizontalSize,
            noiseConfig,
            chunkPos.getStartX(),
            chunkPos.getStartZ(),
            shapeConfig,
            SimpleDensityFunction.INSTANCE,
            chunkGeneratorSettings,
            fluidLevelSampler,
            Blender.getNoBlending(),
            chunkProvider
        );
    }
    
    private ModernBetaChunkNoiseSampler(
        int horizontalSize,
        NoiseConfig noiseConfig, 
        int startX,
        int startZ,
        GenerationShapeConfig shapeConfig,
        DensityFunctionTypes.Beardifying beardifying,
        ChunkGeneratorSettings settings,
        FluidLevelSampler fluidLevelSampler,
        Blender blender,
        ChunkProvider chunkProvider
    ) {
        super(
            horizontalSize,
            noiseConfig,
            startX,
            startZ,
            shapeConfig, 
            beardifying,
            settings,
            fluidLevelSampler,
            blender
        );
        
        this.chunkProvider = chunkProvider;
    }

    /*
     * Simulates a general y height at x/z block coordinates.
     * Replace vanilla noise implementation with plain height sampling.
     * 
     * Used to determine whether an aquifer should use sea level or local water level.
     * Also used in SurfaceBuilder to determine min surface y.
     * 
     * Reference: https://twitter.com/henrikkniberg/status/1432615996880310274
     * 
     */
    @Override
    public int estimateSurfaceHeight(int x, int z) {
        int height = (this.chunkProvider instanceof ChunkProviderNoise noiseChunkProvider) ?
            noiseChunkProvider.getHeight(x, z, ChunkHeightmap.Type.SURFACE_FLOOR) :
            this.chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG);
        
        int seaLevel = this.chunkProvider.getSeaLevel();
        
        // Fudge deeper oceans when at a body of water
        return (height < seaLevel) ? height + OCEAN_HEIGHT_OFFSET : height;
    }
}
