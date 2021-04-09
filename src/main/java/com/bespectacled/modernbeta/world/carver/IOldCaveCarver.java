package com.bespectacled.modernbeta.world.carver;

import java.util.Random;

import net.minecraft.world.chunk.Chunk;

public interface IOldCaveCarver {
    public boolean carve(
        Chunk chunk,
        Random random,
        int chunkX,
        int chunkZ,
        int mainChunkX, 
        int mainChunkZ
    );
}
