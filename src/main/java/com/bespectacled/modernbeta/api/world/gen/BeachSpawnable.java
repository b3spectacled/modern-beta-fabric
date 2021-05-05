package com.bespectacled.modernbeta.api.world.gen;

import net.minecraft.world.Heightmap;

public interface BeachSpawnable {
    /**
     * Samples topmost block at x/z coordinate to check if it is sand, for beach spawns.
     * This does not account for added variance from random.nextDouble(), so it is not exact.
     * 
     * @param x x-coordinate in block coordinates.
     * @param z z-coordinate in block coordinates.
     * 
     * @return If block at x/z coordinate is sand.
     */
    boolean isSandAt(int x, int z);
    
    /**
     * Samples height of block at x/z coordinate.
     * Keep in sync with ChunkProvider method to ensure implementation.
     * 
     * @param x x-coordinate in block coordinates.
     * @param z z-coordinate in block coordinates.
     * @param type Heightmap type.
     * 
     * @return Height of block at x/z in block coordinates.
     */
    int getHeight(int x, int z, Heightmap.Type type);
}
