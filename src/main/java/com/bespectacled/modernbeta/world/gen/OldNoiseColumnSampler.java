package com.bespectacled.modernbeta.world.gen;

import com.bespectacled.modernbeta.api.world.gen.ChunkProvider;
import com.bespectacled.modernbeta.world.gen.provider.AlphaChunkProvider;
import com.bespectacled.modernbeta.world.gen.provider.BetaChunkProvider;
import com.bespectacled.modernbeta.world.gen.provider.PEChunkProvider;

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
    protected int method_38383(int x, int z, TerrainNoisePoint terrainNoisePoint) {
        int height = this.chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG, null);
        
        if (
            this.chunkProvider instanceof BetaChunkProvider || 
            this.chunkProvider instanceof AlphaChunkProvider || 
            this.chunkProvider instanceof PEChunkProvider
        ) {
            height -= 4;
        }
        
        return height;
    }
}
