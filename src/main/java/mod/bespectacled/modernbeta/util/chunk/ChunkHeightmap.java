package mod.bespectacled.modernbeta.util.chunk;

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
public class ChunkHeightmap {
    public enum Type {
        SURFACE_FLOOR,
        SURFACE,
        OCEAN
    }
    
    private final short heightmapSurface[];
    private final short heightmapOcean[];
    private final short heightmapSurfaceFloor[];

    public ChunkHeightmap(short[] heightmapSurface, short[] heightmapOcean, short[] heightmapSurfaceFloor) {
        if (heightmapSurface.length != 256 || heightmapOcean.length != 256) 
            throw new IllegalArgumentException("[Modern Beta] Heightmap is an invalid size!");

        this.heightmapSurface = heightmapSurface;
        this.heightmapOcean = heightmapOcean;
        this.heightmapSurfaceFloor = heightmapSurfaceFloor;
    }
    
    public ChunkHeightmap(int minHeight) {
        this.heightmapSurface = new short[256];
        this.heightmapOcean = new short[256];
        this.heightmapSurfaceFloor = new short[256];
        
        Arrays.fill(this.heightmapSurface, (short)minHeight);
        Arrays.fill(this.heightmapOcean, (short)minHeight);
        Arrays.fill(this.heightmapSurfaceFloor, (short)0);
    }
    
    public int getHeight(int x, int z, Heightmap.Type type) {
        int ndx = (z & 0xF) + (x & 0xF) * 16;
        
        return switch(type) {
            case MOTION_BLOCKING -> this.heightmapOcean[ndx];
            case MOTION_BLOCKING_NO_LEAVES -> this.heightmapOcean[ndx];
            case OCEAN_FLOOR -> this.heightmapSurface[ndx];
            case OCEAN_FLOOR_WG -> this.heightmapSurface[ndx];
            case WORLD_SURFACE -> this.heightmapOcean[ndx];
            case WORLD_SURFACE_WG -> this.heightmapOcean[ndx];
            default -> this.heightmapSurface[ndx];
        };
    }
    
    public int getHeight(int x, int z, ChunkHeightmap.Type type) {
        int ndx = (z & 0xF) + (x & 0xF) * 16;
        
        return switch(type) {
            case SURFACE -> this.heightmapSurface[ndx];
            case OCEAN -> this.heightmapOcean[ndx];
            case SURFACE_FLOOR -> this.heightmapSurfaceFloor[ndx];
            default -> this.heightmapSurface[ndx];
        };
    }
}