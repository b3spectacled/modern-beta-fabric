package mod.bespectacled.modernbeta.world.chunk;

import java.util.function.Supplier;

import mod.bespectacled.modernbeta.api.world.chunk.ChunkProvider;
import mod.bespectacled.modernbeta.api.world.chunk.NoiseChunkProvider;
import mod.bespectacled.modernbeta.util.chunk.HeightmapChunk;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.chunk.AquiferSampler.FluidLevelSampler;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes.class_7050;
import net.minecraft.world.gen.noise.NoiseRouter;

public class ModernBetaChunkNoiseSampler extends ChunkNoiseSampler {
    private static final int OCEAN_HEIGHT_OFFSET = -8;
    
    private final ChunkProvider chunkProvider;
    
    public ModernBetaChunkNoiseSampler(
        int horizontalNoiseResolution, 
        int verticalNoiseResolution, 
        int horizontalSize,
        NoiseRouter noiseRouter, 
        int x,
        int z, 
        Supplier<class_7050> noiseType, 
        Supplier<ChunkGeneratorSettings> supplier,
        FluidLevelSampler fluidLevelSampler,
        Blender blender,
        ChunkProvider chunkProvider
    ) {
        super(
            horizontalNoiseResolution, 
            verticalNoiseResolution, 
            horizontalSize,
            noiseRouter,
            x, 
            z, 
            noiseType.get(), 
            supplier.get(), 
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
    public int method_39900(int x, int z) {
        int height = (this.chunkProvider instanceof NoiseChunkProvider noiseChunkProvider) ?
            noiseChunkProvider.getHeight(x, z, HeightmapChunk.Type.SURFACE_FLOOR) :
            this.chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG);
        
        int seaLevel = this.chunkProvider.getSeaLevel();
        
        // Fudge deeper oceans when at a body of water
        return (height < seaLevel) ? height + OCEAN_HEIGHT_OFFSET : height;
    }
}
