package com.bespectacled.modernbeta.api;

import java.util.function.Supplier;

import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public abstract class AbstractChunkProvider {
    protected final long seed;
    protected final ChunkGenerator chunkGenerator;
    protected final Supplier<ChunkGeneratorSettings> generatorSettings;
    protected final NbtCompound providerSettings;
    
    /**
     * Construct a Modern Beta chunk provider with seed and settings.
     * 
     * @param seed Seed to initialize terrain generators. 
     * @param chunkGenerator Parent vanilla chunk generator by which this chunk provider will be called.  Accessed for biome source primarily.
     * @param generatorSettings Vanilla settings used to control various terrain and noise settings.
     * @param providerSettings NbtCompound for additional settings not part of vanilla generator settings.
     */
    public AbstractChunkProvider(long seed, ChunkGenerator chunkGenerator, Supplier<ChunkGeneratorSettings> generatorSettings, NbtCompound providerSettings) {
        this.seed = seed;
        this.chunkGenerator = chunkGenerator;
        this.generatorSettings = generatorSettings;
        this.providerSettings = providerSettings;
    }
    
    /**
     * Generates base terrain for given chunk and returns it.
     * 
     * @param structureAccessor
     * @param chunk
     * @param biomeSource
     * 
     * @return A completed chunk.
     */
    public abstract Chunk provideChunk(StructureAccessor structureAccessor, Chunk chunk);
    
    /**
     * Generates biome-specific surface for given chunk.
     * 
     * @param region
     * @param chunk
     * @param biomeSource
     */
    public abstract void provideSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource);
    
    /**
     * Gets or calculates the height of the topmost block at given x/z coordinates.
     * 
     * @param x x-coordinate in block coordinates. 
     * @param z z-coordinate in block coordinates.
     * @param type The heightmap type
     * 
     * @return The y-coordinate of top block at x/z.
     */
    public abstract int getHeight(int x, int z, Heightmap.Type type);
    
    /**
     * Determines whether to skip the chunk for some chunk generation step, depending on the x/z chunk coordinates.
     * 
     * @param chunkX x-coordinate in chunk coordinates.
     * @param chunkZ z-coordinate in chunk coordinates.
     * @param chunkStatus Chunk generation step used for skip context.
     * 
     * @return Whether to skip the chunk.
     */
    public boolean skipChunk(int chunkX, int chunkZ, ChunkStatus chunkStatus) {
        return false;
    }
    
    /**
     * @return Total world height including minimum y coordinate. 
     */
    public int getWorldHeight() {
        return 256;
    }
    
    /**
     * @return Minimum Y coordinate.
     */
    public int getMinimumY() {
        return 0;
    }
    
    /**
     * @return World sea level.
     */
    public int getSeaLevel() {
        return 64;
    }
    
    /**
     * Get the PerlinOctaveNoise object used for beach surface generation for determining beach spawn location.
     * 
     * @return PerlinOctaveNoise object used for beach surface generation.
     */
    public PerlinOctaveNoise getBeachNoise() {
        return null;
    }
}
