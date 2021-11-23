package com.bespectacled.modernbeta.api.world.gen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bespectacled.modernbeta.api.world.gen.noise.BaseNoiseProvider;
import com.bespectacled.modernbeta.api.world.gen.noise.NoiseProvider;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.chunk.ChunkCache;
import com.bespectacled.modernbeta.util.chunk.HeightmapChunk;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;
import com.bespectacled.modernbeta.world.gen.blocksource.BlockSource;
import com.bespectacled.modernbeta.world.gen.blocksource.BlockSourceRules;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;

public abstract class NoiseChunkProvider extends BaseChunkProvider {
    protected final int verticalNoiseResolution;   // Number of blocks in a vertical subchunk
    protected final int horizontalNoiseResolution; // Number of blocks in a horizontal subchunk 
    
    protected final int noiseSizeX; // Number of horizontal subchunks along x
    protected final int noiseSizeZ; // Number of horizontal subchunks along z
    protected final int noiseSizeY; // Number of vertical subchunks
    protected final int noiseMinY;  // Subchunk index of bottom of the world
    protected final int noiseTopY;  // Number of positive (y >= 0) vertical subchunks

    protected final double xzScale;
    protected final double yScale;
    
    protected final double xzFactor;
    protected final double yFactor;
    
    protected final int topSlideTarget;
    protected final int topSlideSize;
    protected final int topSlideOffset;
    
    protected final int bottomSlideTarget;
    protected final int bottomSlideSize;
    protected final int bottomSlideOffset;

    protected final ChunkCache<NoiseProvider> noiseProviderCache;
    protected final ChunkCache<HeightmapChunk> heightmapChunkCache;

    public NoiseChunkProvider(OldChunkGenerator chunkGenerator) {
        this(
            chunkGenerator,
            0,
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().getHeight(),
            chunkGenerator.getGeneratorSettings().get().getSeaLevel(),
            0,
            chunkGenerator.getGeneratorSettings().get().getBedrockFloorY(),
            chunkGenerator.getGeneratorSettings().get().getBedrockCeilingY(),
            chunkGenerator.getGeneratorSettings().get().getDefaultBlock(),
            chunkGenerator.getGeneratorSettings().get().getDefaultFluid(),
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().getSizeVertical(),
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().getSizeHorizontal(),
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().getSampling().getXZScale(),
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().getSampling().getYScale(),
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().getSampling().getXZFactor(),
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().getSampling().getYFactor(),
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().getTopSlide().getTarget(),
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().getTopSlide().getSize(),
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().getTopSlide().getOffset(),
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().getBottomSlide().getTarget(),
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().getBottomSlide().getSize(),
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().getBottomSlide().getOffset()
        );
    }

    public NoiseChunkProvider(
        OldChunkGenerator chunkGenerator,
        int worldMinY, 
        int worldHeight, 
        int seaLevel,
        int surfaceMinY,
        int bedrockFloor,
        int bedrockCeiling,
        BlockState defaultBlock,
        BlockState defaultFluid,
        int sizeVertical, 
        int sizeHorizontal,
        double xzScale, 
        double yScale,
        double xzFactor, 
        double yFactor,
        int topSlideTarget,
        int topSlideSize,
        int topSlideOffset,
        int bottomSlideTarget,
        int bottomSlideSize,
        int bottomSlideOffset
    ) {
        super(chunkGenerator, worldMinY, worldHeight, seaLevel, surfaceMinY, bedrockFloor, bedrockCeiling, defaultBlock, defaultFluid);
        
        this.verticalNoiseResolution = sizeVertical * 4;
        this.horizontalNoiseResolution = sizeHorizontal * 4;
        
        this.noiseSizeX = 16 / this.horizontalNoiseResolution;
        this.noiseSizeZ = 16 / this.horizontalNoiseResolution;
        this.noiseSizeY = MathHelper.floorDiv(this.worldHeight, this.verticalNoiseResolution);
        this.noiseMinY = MathHelper.floorDiv(this.worldMinY, this.verticalNoiseResolution);
        this.noiseTopY = MathHelper.floorDiv(this.worldMinY + this.worldHeight, this.verticalNoiseResolution);
        
        this.xzScale = xzScale;
        this.yScale = yScale;
        
        this.xzFactor = xzFactor;
        this.yFactor = yFactor;
        
        this.topSlideTarget = topSlideTarget;
        this.topSlideSize = topSlideSize;
        this.topSlideOffset = topSlideOffset;
        
        this.bottomSlideTarget = bottomSlideTarget;
        this.bottomSlideSize = bottomSlideSize;
        this.bottomSlideOffset = bottomSlideOffset;
        
        this.noiseProviderCache = new ChunkCache<>(
            "noise_provider",
            1536,
            true,
            (chunkX, chunkZ) -> {
                NoiseProvider baseNoiseProvider = new BaseNoiseProvider(
                    this.noiseSizeX,
                    this.noiseSizeY,
                    this.noiseSizeZ,
                    this::sampleNoiseColumn
                );
                
                baseNoiseProvider.sampleInitialNoise(chunkX * this.noiseSizeX, chunkZ * this.noiseSizeZ);
                
                return baseNoiseProvider;
            }
        );
        
        this.heightmapChunkCache = new ChunkCache<>(
            "heightmap", 
            1536, 
            true, 
            this::sampleHeightmap
        );
    }

    /**
     * Generates base terrain for given chunk and returns it.
     * @param structureAccessor
     * @param chunk
     * @param biomeSource
     * 
     * @return A completed chunk.
     */
    @Override
    public Chunk provideChunk(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk) {
        this.generateTerrain(chunk, structureAccessor);
        return chunk;
    }
    
    /**
     * Sample height at given x/z coordinate. Initially generates heightmap for entire chunk, 
     * if chunk containing x/z coordinates has never been sampled.
     *
     * @param x x-coordinate in block coordinates.
     * @param z z-coordinate in block coordinates.
     * @param type Vanilla heightmap type.
     * @return The y-coordinate of top block at x/z.
     */
    @Override
    public int getHeight(int x, int z, Heightmap.Type type) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        
        return this.heightmapChunkCache.get(chunkX, chunkZ).getHeight(x, z, type);
    }
    
    /**
     * Sample height at given x/z coordinate. Initially generates heightmap for entire chunk, 
     * if chunk containing x/z coordinates has never been sampled.
     *
     * @param x x-coordinate in block coordinates.
     * @param z z-coordinate in block coordinates.
     * @param type HeightmapChunk heightmap type.
     * 
     * @return The y-coordinate of top block at x/z.
     */
    public int getHeight(int x, int z, HeightmapChunk.Type type) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        
        return this.heightmapChunkCache.get(chunkX, chunkZ).getHeight(x, z, type);
    }
    
    /**
     * Generates noise for a column at startNoiseX + localNoiseX / startNoiseZ + localNoiseZ.
     * 
     * @param buffer Buffer of size noiseSizeY + 1 to store noise column
     * @param startNoiseX x-coordinate start of chunk in noise coordinates.
     * @param startNoiseZ z-coordinate start of chunk in noise coordinates.
     * @param localNoiseX Current subchunk index along x-axis.
     * @param localNoiseZ Current subchunk index along z-axis.
     */
    protected abstract void sampleNoiseColumn(double[] buffer, int startNoiseX, int startNoiseZ, int localNoiseX, int localNoiseZ);
    
    /**
     * Interpolates density to set terrain curve at top and bottom of the world.
     * 
     * @param density Base density.
     * @param noiseY y-coordinate in noise coordinates.
     * 
     * @return Modified noise density.
     */
    protected double applySlides(double density, int noiseY) {
        noiseY -= this.noiseMinY;
        
        if (this.topSlideSize > 0.0) {
            double delta = ((double)(this.noiseSizeY - noiseY) - this.topSlideOffset) / this.topSlideSize;
            density = MathHelper.clampedLerp(this.topSlideTarget, density, delta);
        }
        
        if (this.bottomSlideSize > 0.0) {
            double delta = ((double)noiseY - this.bottomSlideOffset) / this.bottomSlideSize;
            density = MathHelper.clampedLerp(this.bottomSlideTarget, density, delta);
        }
        
        return density;
    }

    /**
     * Generates the base terrain for a given chunk.
     * 
     * @param chunk
     * @param structureAccessor Collects structures within the chunk, so that terrain can be modified to accommodate them.
     */
    private void generateTerrain(Chunk chunk, StructureAccessor structureAccessor) {
        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;
        int startX = chunkPos.getStartX();
        int startZ = chunkPos.getStartZ();
        
        Heightmap heightmapOcean = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSurface = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
        
        StructureWeightSampler structureWeightSampler = new StructureWeightSampler(structureAccessor, chunk);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        // Create and populate noise providers
        List<NoiseProvider> noiseProviders = new ArrayList<>();
        
        NoiseProvider baseNoiseProvider = this.noiseProviderCache.get(chunkX, chunkZ);
        BlockSource baseBlockSource = this.getBaseBlockSource(baseNoiseProvider, structureWeightSampler);
        
        // Create and populate block sources
        BlockSourceRules blockSources = new BlockSourceRules.Builder()
            .add(baseBlockSource)
            .build(this.defaultBlock);

        // Sample initial noise.
        // Base noise should be added after this,
        // since base noise is sampled when fetched from cache.
        noiseProviders.forEach(noiseProvider -> noiseProvider.sampleInitialNoise(chunkX * this.noiseSizeX, chunkZ * this.noiseSizeZ));
        noiseProviders.add(baseNoiseProvider);
        
        for (int subChunkX = 0; subChunkX < this.noiseSizeX; ++subChunkX) {
            int noiseX = subChunkX;
            
            for (int subChunkZ = 0; subChunkZ < this.noiseSizeZ; ++ subChunkZ) {
                int noiseZ = subChunkZ;
                
                for (int subChunkY = 0; subChunkY < this.noiseSizeY; ++subChunkY) {
                    int noiseY = subChunkY;
                    
                    noiseProviders.forEach(noiseProvider -> noiseProvider.sampleNoiseCorners(noiseX, noiseY, noiseZ));
                    
                    for (int subY = 0; subY < this.verticalNoiseResolution; ++subY) {
                        int y = subY + subChunkY * this.verticalNoiseResolution;
                        y += this.worldMinY;
                        
                        double deltaY = subY / (double)this.verticalNoiseResolution;
                        noiseProviders.forEach(noiseProvider -> noiseProvider.sampleNoiseY(deltaY));
                        
                        for (int subX = 0; subX < this.horizontalNoiseResolution; ++subX) {
                            int localX = subX + subChunkX * this.horizontalNoiseResolution;
                            int x = startX + localX;
                            
                            double deltaX = subX / (double)this.horizontalNoiseResolution;
                            noiseProviders.forEach(noiseProvider -> noiseProvider.sampleNoiseX(deltaX));
                            
                            for (int subZ = 0; subZ < this.horizontalNoiseResolution; ++subZ) {
                                int localZ = subZ + subChunkZ * this.horizontalNoiseResolution;
                                int z = startZ + localZ;
                                
                                double deltaZ = subZ / (double)this.horizontalNoiseResolution;
                                noiseProviders.forEach(noiseProvider -> noiseProvider.sampleNoiseZ(deltaZ));
                                
                                BlockState blockState = blockSources.sample(x, y, z);
                                chunk.setBlockState(mutable.set(localX, y, localZ), blockState, false);

                                heightmapOcean.trackUpdate(localX, y, localZ, blockState);
                                heightmapSurface.trackUpdate(localX, y, localZ, blockState);
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Generates a heightmap for the chunk containing the given x/z coordinates
     * and returns to {@link #getHeight(int, int, net.minecraft.world.Heightmap.Type)} 
     * to cache and return the height.
     * 
     * @param chunkX x-coordinate in chunk coordinates to sample all y-values for.
     * @param chunkZ z-coordinate in chunk coordinates to sample all y-values for.
     * 
     * @return A HeightmapChunk, containing an array of ints containing the heights for the entire chunk.
     */
    private HeightmapChunk sampleHeightmap(int chunkX, int chunkZ) {
        int minHeight = 16;
        
        NoiseProvider baseNoiseProvider = this.noiseProviderCache.get(chunkX, chunkZ);
        
        int[] heightmapSurface = new int[256];
        int[] heightmapOcean = new int[256];
        int[] heightmapSurfaceFloor = new int[256];
        
        Arrays.fill(heightmapSurface, minHeight);
        Arrays.fill(heightmapOcean, minHeight);
        Arrays.fill(heightmapSurfaceFloor, this.worldMinY);
        
        for (int subChunkX = 0; subChunkX < this.noiseSizeX; ++subChunkX) {
            for (int subChunkZ = 0; subChunkZ < this.noiseSizeZ; ++subChunkZ) {
                for (int subChunkY = 0; subChunkY < this.noiseSizeY; ++subChunkY) {
                    baseNoiseProvider.sampleNoiseCorners(subChunkX, subChunkY, subChunkZ);
                    
                    for (int subY = 0; subY < this.verticalNoiseResolution; ++subY) {
                        int y = subY + subChunkY * this.verticalNoiseResolution;
                        y += this.worldMinY;
                        
                        double deltaY = subY / (double)this.verticalNoiseResolution;
                        baseNoiseProvider.sampleNoiseY(deltaY);
                        
                        for (int subX = 0; subX < this.horizontalNoiseResolution; ++subX) {
                            int x = subX + subChunkX * this.horizontalNoiseResolution;
                            
                            double deltaX = subX / (double)this.horizontalNoiseResolution;
                            baseNoiseProvider.sampleNoiseX(deltaX);
                            
                            for (int subZ = 0; subZ < this.horizontalNoiseResolution; ++subZ) {
                                int z = subZ + subChunkZ * this.horizontalNoiseResolution;
                                
                                double deltaZ = subZ / (double)this.horizontalNoiseResolution;
                                baseNoiseProvider.sampleNoiseZ(deltaZ);
                                
                                double density = baseNoiseProvider.sample();
                                boolean isSolid = density > 0.0;
                                
                                int height = y + 1;
                                int ndx = z + x * 16;
                                
                                // Capture topmost solid/fluid block height.
                                if (y < this.seaLevel || isSolid) {
                                    heightmapOcean[ndx] = height;
                                }
                                
                                // Capture topmost solid block height.
                                if (isSolid) {
                                    heightmapSurface[ndx] = height;
                                }
                                
                                // Capture lowest solid block height.
                                if (density <= 0.0 && heightmapSurfaceFloor[ndx] == Integer.MIN_VALUE) {
                                    heightmapSurfaceFloor[ndx] = height - 1;
                                }
                                
                                // Capture lowest solid block height.
                                // First, set max world height as flag when hitting first solid layer
                                // then set the actual height value when hitting first non-solid layer.
                                // This handles situations where the bottom of the world may not be solid,
                                // i.e. Skylands-style world types.
                                if (isSolid && heightmapSurfaceFloor[ndx] == this.worldMinY) {
                                    heightmapSurfaceFloor[ndx] = this.worldTopY;
                                }
                                
                                if (!isSolid && heightmapSurfaceFloor[ndx] == this.worldTopY) {
                                    heightmapSurfaceFloor[ndx] = height - 1;
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // Construct new heightmap cache from generated heightmap array
        return new HeightmapChunk(heightmapSurface, heightmapOcean, heightmapSurfaceFloor);
    }
    
    /**
     * Creates block source to sample BlockState at block coordinates given base noise provider.
     * 
     * @param baseNoiseProvider Primary noise provider to sample density noise.
     * @param weightSampler Sampler used to add/subtract density if a structure start is at coordinate.
     * @param aquiferSampler Sampler used to adjust local water levels for noise caves.
     * @param blockSource Default block source
     * @param noodleCaveSampler Noodle cave density sampler.
     * 
     * @return BlockSource to sample blockstate at x/y/z block coordinates.
     */
    private BlockSource getBaseBlockSource(
        NoiseProvider baseNoiseProvider,
        StructureWeightSampler weightSampler
    ) {
        return (x, y, z) -> {
            double density = baseNoiseProvider.sample();
            double clampedDensity = MathHelper.clamp(density / 200.0, -1.0, 1.0);
            
            clampedDensity = clampedDensity / 2.0 - clampedDensity * clampedDensity * clampedDensity / 24.0;
            clampedDensity += weightSampler.getWeight(x, y, z);
            
            BlockState blockState = BlockStates.AIR;
            if (clampedDensity > 0.0) {
                blockState = this.defaultBlock;
            } else if (y < this.getSeaLevel()) {
                blockState = this.defaultFluid;
            }
            
            return blockState;
        };
    }
}

