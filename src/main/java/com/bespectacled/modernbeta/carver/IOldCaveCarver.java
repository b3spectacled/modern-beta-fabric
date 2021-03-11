package com.bespectacled.modernbeta.carver;

import java.util.Random;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.carver.CarverContext;

public interface IOldCaveCarver {
    public boolean carve(
        CarverContext heightContext,
        Chunk chunk,
        Random random,
        int chunkX,
        int chunkZ,
        int mainChunkX, 
        int mainChunkZ
    );
}
