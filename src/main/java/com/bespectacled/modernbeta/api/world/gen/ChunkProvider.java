package com.bespectacled.modernbeta.api.world.gen;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.api.world.biome.BiomeHeightSampler;
import com.bespectacled.modernbeta.api.world.spawn.SpawnLocator;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.AquiferSampler.FluidLevel;
import net.minecraft.world.gen.chunk.AquiferSampler.FluidLevelSampler;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public abstract class ChunkProvider implements BiomeHeightSampler {
    public static final int LAVA_LEVEL = -53; // Vanilla: -54;
    
    protected final OldChunkGenerator chunkGenerator;
    
    protected final long seed;
    protected final Supplier<ChunkGeneratorSettings> generatorSettings;
    protected final NbtCompound providerSettings;
    protected SpawnLocator spawnLocator;
    
    private final FluidLevelSampler carverFluidLevelSampler;
    
    /**
     * Construct a Modern Beta chunk provider with seed and settings.
     * 
     * @param chunkGenerator Parent OldChunkGenerator object used to initialize fields.
     */
    public ChunkProvider(OldChunkGenerator chunkGenerator) {
        this.chunkGenerator = chunkGenerator;
        
        this.seed = chunkGenerator.getWorldSeed();
        this.generatorSettings = chunkGenerator.getGeneratorSettings();
        this.providerSettings = chunkGenerator.getProviderSettings();
        this.spawnLocator = SpawnLocator.DEFAULT;

        this.carverFluidLevelSampler = (x, y, z) -> new FluidLevel(this.getSeaLevel(), BlockStates.AIR);
    }
    
    /**
     * Generates base terrain for given chunk and returns it.
     * 
     * @param executor
     * @param structureAccessor
     * @param chunk
     * 
     * @return A completed chunk.
     */
    public abstract CompletableFuture<Chunk> provideChunk(Executor executor, StructureAccessor structureAccessor, Chunk chunk);
    
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
     * @param world
     * 
     * @return The y-coordinate of top block at x/z.
     */
    public abstract int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world);
    
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
     * @return Minimum Y coordinate in block coordinates.
     */
    public int getWorldMinY() {
        return this.generatorSettings.get().getGenerationShapeConfig().minimumY();
    }
    
    /**
     * @return World sea level in block coordinates.
     */
    public int getSeaLevel() {
        return this.generatorSettings.get().getSeaLevel();
    }
    
    /**
     * Get aquifer sampler, for carving for now.
     * 
     * @return An aquifer sampler.
     */
    public AquiferSampler getAquiferSampler(Chunk chunk) {
        return AquiferSampler.seaLevel(this.carverFluidLevelSampler);
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
     * @param noiseSampler
     * 
     * @return A biome.
     */
    public Biome getBiome(int biomeX, int biomeY, int biomeZ, MultiNoiseUtil.MultiNoiseSampler noiseSampler) {
        return this.chunkGenerator.getBiomeSource().getBiome(biomeX, biomeY, biomeZ, noiseSampler);
    }
    
    public static class BiomeBlocks {
        private BlockState topBlock;
        private BlockState fillerBlock;
        
        private BiomeBlocks(BlockState topBlock, BlockState fillerBlock) {
            this.topBlock = topBlock;
            this.fillerBlock = fillerBlock;
        }
        
        public BlockState getTopBlock() {
            return this.topBlock;
        }
        
        public BlockState getFillerBlock() {
            return this.fillerBlock;
        }
        
        public static BiomeBlocks getBiomeBlocks(Biome biome) {
            if (biome.getCategory() == Category.DESERT)
                return DESERT;
            
            return DEFAULT;
        }
        
        public static final BiomeBlocks DESERT = new BiomeBlocks(BlockStates.SAND, BlockStates.SAND);
        public static final BiomeBlocks DEFAULT = new BiomeBlocks(BlockStates.GRASS_BLOCK, BlockStates.DIRT);
    }
}
