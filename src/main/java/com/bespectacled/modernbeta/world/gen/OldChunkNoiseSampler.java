package com.bespectacled.modernbeta.world.gen;

import java.util.function.Supplier;

import net.minecraft.class_6748;
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
        NoiseColumnSampler noiseColumnSampler, 
        int x, 
        int z, 
        ColumnSampler columnSampler, 
        Supplier<ChunkGeneratorSettings> supplier,
        FluidLevelSampler fluidLevelSampler,
        class_6748 blender
    ) {
        super(
            horizontalNoiseResolution, 
            verticalNoiseResolution, 
            horizontalSize,
            noiseColumnSampler,
            x, 
            z, 
            columnSampler, 
            supplier.get(), 
            fluidLevelSampler,
            blender
        );
    }
    
    /* 
     * Override this and return null so AquiferSampler doesn't complain.
     */
    @Override
    public TerrainNoisePoint getTerrainNoisePoint(NoiseColumnSampler columnSampler, int x, int z) {
        return null;
    }
    
    @Override
    public TerrainNoisePoint getInterpolatedTerrainNoisePoint(int x, int z) {
        return null;
    }
}
