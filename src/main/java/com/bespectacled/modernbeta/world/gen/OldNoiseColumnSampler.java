package com.bespectacled.modernbeta.world.gen;

import com.bespectacled.modernbeta.api.world.gen.ChunkProvider;

import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.source.util.TerrainNoisePoint;
import net.minecraft.world.gen.MultiNoiseParameters;
import net.minecraft.world.gen.NoiseColumnSampler;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;

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
        ChunkProvider chunkProvider
    ) {
        super(horizontalNoiseResolution, verticalNoiseResolution, noiseSizeY, generationShapeConfig, multiNoiseParameters, hasNoiseCaves, seed);
        
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
        int height = this.chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG, null);
        int seaLevel = this.chunkProvider.getSeaLevel();
        
        // Fudge deeper oceans when at a body of water
        return (height < seaLevel) ? height + OCEAN_HEIGHT_OFFSET : height;
    }
}
