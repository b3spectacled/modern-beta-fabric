package com.bespectacled.modernbeta.world.gen;

import java.util.function.Supplier;

import net.minecraft.world.biome.source.util.TerrainNoisePoint;
import net.minecraft.world.gen.NoiseColumnSampler;
import net.minecraft.world.gen.chunk.AquiferSampler.FluidLevelSampler;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;

public class OldChunkNoiseSampler extends ChunkNoiseSampler {
    public OldChunkNoiseSampler(
        int horizontalNoiseResolution, 
        int verticalNoiseResolution, 
        int horizontalSize, 
        int height, 
        int minimumY, 
        NoiseColumnSampler noiseColumnSampler, 
        int x, 
        int z, 
        ColumnSampler columnSampler, 
        Supplier<ChunkGeneratorSettings> supplier,
        FluidLevelSampler fluidLevelSampler
    ) {
        super(
            horizontalNoiseResolution, 
            verticalNoiseResolution, 
            horizontalSize, 
            height, 
            minimumY, 
            noiseColumnSampler, 
            x, 
            z, 
            columnSampler, 
            supplier, 
            fluidLevelSampler
        );
    }
    
    /* 
     * Override this and return null so AquiferSampler doesn't complain.
     */
    @Override
    public TerrainNoisePoint getTerrainNoisePoint(NoiseColumnSampler columnSampler, int x, int z) {
        return null;
    }

}
