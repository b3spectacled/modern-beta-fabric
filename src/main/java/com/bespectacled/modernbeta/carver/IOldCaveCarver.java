package com.bespectacled.modernbeta.carver;

import java.util.Random;

import net.minecraft.class_5873;
import net.minecraft.world.chunk.Chunk;

public interface IOldCaveCarver {
    public boolean carve(
        class_5873 heightContext,
        Chunk chunk,
        Random random,
        int chunkX,
        int chunkZ,
        int mainChunkX, 
        int mainChunkZ
    );
}
