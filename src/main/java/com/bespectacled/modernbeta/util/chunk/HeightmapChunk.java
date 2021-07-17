package com.bespectacled.modernbeta.util.chunk;

/**
 * A simple container for an array to hold height values for an entire chunk (256 blocks).
 */
public class HeightmapChunk {
    private final int heightmap[];

    public HeightmapChunk(int[] heightmap) {
        if (heightmap.length != 256) 
            throw new IllegalArgumentException("[Modern Beta] Heightmap is an invalid size!");

        this.heightmap = heightmap;
    }

    public int getHeight(int x, int z) {
        return this.heightmap[(z & 0xF) + (x & 0xF) * 16];
    }
}