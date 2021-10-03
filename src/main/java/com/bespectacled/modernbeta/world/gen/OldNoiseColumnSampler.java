package com.bespectacled.modernbeta.world.gen;

import com.bespectacled.modernbeta.api.world.gen.ChunkProvider;

import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.source.util.TerrainNoisePoint;
import net.minecraft.world.gen.MultiNoiseParameters;
import net.minecraft.world.gen.NoiseColumnSampler;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;

public class OldNoiseColumnSampler extends NoiseColumnSampler  {
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
     */
    @Override
    protected int method_38383(int noiseX, int noiseZ, TerrainNoisePoint terrainNoisePoint) {
        //return this.chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG, null);
        //return this.chunkProvider.getSeaLevel();
        return 100;
    }
}
