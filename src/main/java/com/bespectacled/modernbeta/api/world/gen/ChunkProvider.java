package com.bespectacled.modernbeta.api.world.gen;

import java.util.Optional;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.api.world.spawn.SpawnLocator;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public abstract class ChunkProvider {
    protected final OldChunkGenerator chunkGenerator;
    
    protected final long seed;
    protected final Supplier<ChunkGeneratorSettings> generatorSettings;
    protected final NbtCompound providerSettings;
    
    protected SpawnLocator spawnLocator;
    
    /**
     * Construct a Modern Beta chunk provider with seed and settings.
     * 
     * @param chunkGenerator Parent OldChunkGenerator object used to initialize fields.
     */
    public ChunkProvider(OldChunkGenerator chunkGenerator) {
        this.chunkGenerator = chunkGenerator;
        
        this.seed = chunkGenerator.getWorldSeed();
        this.generatorSettings = chunkGenerator.getGeneratorSettings();
        this.providerSettings = chunkGenerator.getChunkSettings();
        
        this.spawnLocator = SpawnLocator.DEFAULT;
    }
    
    /**
     * Generates base terrain for given chunk and returns it.
     * @param worldAccess TODO
     * @param structureAccessor
     * @param chunk
     * @param biomeSource
     * 
     * @return A completed chunk.
     */
    public abstract Chunk provideChunk(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk);
    
    /**
     * Generates biome-specific surface for given chunk.
     * 
     * @param region
     * @param chunk
     * @param biomeSource
     */
    public abstract void provideSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource);
    
    /**
     * Sample height at given x/z coordinate. Initially generates heightmap for entire chunk, 
     * if chunk containing x/z coordinates has never been sampled.
     *
     * @param x x-coordinate in block coordinates.
     * @param z z-coordinate in block coordinates.
     * @param type Vanilla heightmap type.
     * @return The y-coordinate of top block at x/z.
     */
    public abstract int getHeight(int x, int z, Heightmap.Type heightmap);
    
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
     * Get total world height in blocks, including minimum Y. 
     * (i.e. Returns 320 if bottomY is -64 and topY is 256.)
     * 
     * @return Total world height in blocks. 256 by default. 
     */
    public int getWorldHeight() {
        return 256;
    }
    
    /**
     * @return Minimum Y coordinate in block coordinates. 0 by default.
     */
    public int getWorldMinY() {
        return 0;
    }
    
    /**
     * @return World sea level in block coordinates. 64 by default.
     */
    public int getSeaLevel() {
        return 64;
    }
    
    /**
     * Locate initial spawn position for players.
     * If optional left empty, then vanilla spawn position is used.
     * 
     * @return Optional BlockPos player spawn position.
     */
    public Optional<BlockPos> locateSpawn() {
        return this.spawnLocator.locateSpawn();
    }
    
    /**
     * @return Chunk provider's spawn locator.
     */
    public SpawnLocator getSpawnLocator() {
        return this.spawnLocator;
    }
    
    /**
     * @return Parent OldChunkGenerator.
     */
    public OldChunkGenerator getChunkGenerator() {
        return this.chunkGenerator;
    }
    
    /**
     * Samples biome at given biome coordinates.
     * 
     * @param biomeX x-coordinate in biome coordinates.
     * @param biomeY y-coordinate in biome coordinates.
     * @param biomeZ z-coordinate in biome coordinates.
     * 
     * @return A biome.
     */
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.chunkGenerator.getBiomeSource().getBiomeForNoiseGen(biomeX, biomeY, biomeZ);
    }
}
