package com.bespectacled.modernbeta.world.carver;

import java.util.Random;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.carver.CarverContext;
import net.minecraft.world.gen.carver.CaveCarverConfig;

public interface IOldCaveCarver {
    public boolean carve(
        CarverContext heightContext,
        CaveCarverConfig config,
        Chunk chunk,
        Random random,
        int chunkX,
        int chunkZ,
        int mainChunkX, 
        int mainChunkZ
    );
}
