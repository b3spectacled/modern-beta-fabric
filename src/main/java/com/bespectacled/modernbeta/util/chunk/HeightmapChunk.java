package com.bespectacled.modernbeta.util.chunk;

import java.util.Arrays;

import net.minecraft.world.Heightmap;

/**
 * A simple container for an array to hold height values for an entire chunk (256 blocks).
 * 
 * == Heightmap Reference, from https://minecraft.fandom.com/wiki/Chunk_format ==
 * MOTION_BLOCKING: The highest block that blocks motion or contains a fluid.
 * MOTION_BLOCKING_NO_LEAVES: The highest block that blocks motion or contains a fluid or is in the minecraft:leaves tag.
 * OCEAN_FLOOR: The highest non-air block, solid block.
 * OCEAN_FLOOR_WG: The highest block that is neither air nor contains a fluid, for worldgen.
 * WORLD_SURFACE: The highest non-air block.
 * WORLD_SURFACE_WG: The highest non-air block, for worldgen.
 * 
 */
public class HeightmapChunk {
    public enum Type {
        SURFACE_FLOOR,
        SURFACE,
        OCEAN
    }
    
    private final int heightmapSurface[];
    private final int heightmapOcean[];
    private final int heightmapSurfaceFloor[];

    public HeightmapChunk(int[] heightmapSurface, int[] heightmapOcean, int[] heightmapSurfaceFloor) {
        if (heightmapSurface.length != 256 || heightmapOcean.length != 256) 
            throw new IllegalArgumentException("[Modern Beta] Heightmap is an invalid size!");

        this.heightmapSurface = heightmapSurface;
        this.heightmapOcean = heightmapOcean;
        this.heightmapSurfaceFloor = heightmapSurfaceFloor;
    }
    
    public HeightmapChunk(int minHeight) {
        this.heightmapSurface = new int[256];
        this.heightmapOcean = new int[256];
        this.heightmapSurfaceFloor = new int[256];
        
        Arrays.fill(this.heightmapSurface, minHeight);
        Arrays.fill(this.heightmapOcean, minHeight);
        Arrays.fill(this.heightmapSurfaceFloor, minHeight);
    }
    
    public int getHeight(int x, int z, Heightmap.Type type) {
        int ndx = (z & 0xF) + (x & 0xF) * 16;
        int height;
        
        switch(type) {
            case MOTION_BLOCKING:
                height = this.heightmapOcean[ndx];
                break;
            case MOTION_BLOCKING_NO_LEAVES:
                height = this.heightmapOcean[ndx];
                break;
            case OCEAN_FLOOR:
                height = this.heightmapSurface[ndx];
                break;
            case OCEAN_FLOOR_WG:
                height = this.heightmapSurface[ndx];
                break;
            case WORLD_SURFACE:
                height = this.heightmapOcean[ndx];
                break;
            case WORLD_SURFACE_WG:
                height = this.heightmapOcean[ndx];
                break;
            default:
                height = this.heightmapSurface[ndx];
        }

        return height;
    }
    
    public int getHeight(int x, int z, HeightmapChunk.Type type) {
        int ndx = (z & 0xF) + (x & 0xF) * 16;
        int height;
        
        switch(type) {
            case SURFACE:
                height = this.heightmapSurface[ndx];
                break;
            case OCEAN:
                height = this.heightmapOcean[ndx];
                break;
            case SURFACE_FLOOR:
                height = this.heightmapSurfaceFloor[ndx];
                break;
            default:
                height = this.heightmapSurface[ndx];
        }
        
        return height;
    }
}