package com.bespectacled.modernbeta.api.world.gen;

import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;

public interface BeachSpawnable {
    /**
     * Samples topmost block at x/z coordinate to check if it is sand, for beach spawns.
     * This does not account for added variance from random.nextDouble() if simulating beaches, 
     * so it is not exact.
     * 
     * @param x x-coordinate in block coordinates.
     * @param z z-coordinate in block coordinates.
     * 
     * @return If block at x/z coordinate is sand.
     */
    boolean isSandAt(int x, int z, HeightLimitView world);
    
    /**
     * Sample height at given x/z coordinate.
     * Keep in sync with {@link ChunkProvider#getHeight(int, int, net.minecraft.world.Heightmap.Type, HeightLimitView)}.
     *
     * @param x x-coordinate in block coordinates.
     * @param z z-coordinate in block coordinates.
     * @param type Vanilla heightmap type.
     * @param world
     * 
     * @return The y-coordinate of top block at x/z.
     */
    int getHeight(int x, int z, Heightmap.Type type, HeightLimitView world);
}
