package com.bespectacled.modernbeta.world.gen;

import com.bespectacled.modernbeta.api.world.gen.ChunkProvider;
import com.bespectacled.modernbeta.api.world.gen.NoiseChunkProvider;
import com.bespectacled.modernbeta.util.chunk.HeightmapChunk;

import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.source.util.TerrainNoisePoint;
import net.minecraft.world.gen.MultiNoiseParameters;
import net.minecraft.world.gen.NoiseColumnSampler;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.random.ChunkRandom;

public class OldNoiseColumnSampler extends NoiseColumnSampler  {
    private static final int OCEAN_HEIGHT_OFFSET = -8;
    
    private final ChunkProvider chunkProvider;
    
    public OldNoiseColumnSampler(
        int horizontalNoiseResolution, 
        int verticalNoiseResolution, 
        int noiseSizeY, 
        GenerationShapeConfig generationShapeConfig, 
        MultiNoiseParameters multiNoiseParameters, 
        boolean hasNoiseCaves,
        long seed,
        ChunkRandom.class_6675 randomType,
        ChunkProvider chunkProvider
    ) {
        super(
            horizontalNoiseResolution, 
            verticalNoiseResolution, 
            noiseSizeY, 
            generationShapeConfig, 
            multiNoiseParameters, 
            hasNoiseCaves, 
            seed,
            randomType
        );
        
        this.chunkProvider = chunkProvider;
    }

    /*
     * Simulates a general y height at x/z block coordinates.
     * Replace vanilla noise implementation with plain height sampling.
     * 
     * Used to determine whether an aquifer should use sea level or local water level.
     * Reference: https://twitter.com/henrikkniberg/status/1432615996880310274
     * 
     */
    @Override
    protected int method_38383(int x, int z, TerrainNoisePoint terrainNoisePoint) {
        int height = (this.chunkProvider instanceof NoiseChunkProvider noiseChunkProvider) ?
            noiseChunkProvider.getHeight(x, z, HeightmapChunk.Type.SURFACE_FLOOR) :
            this.chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG, null);
        
        int seaLevel = this.chunkProvider.getSeaLevel();
        
        // Fudge deeper oceans when at a body of water
        return (height < seaLevel) ? height + OCEAN_HEIGHT_OFFSET : height;
    }
}
