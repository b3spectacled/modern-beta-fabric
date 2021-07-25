package com.bespectacled.modernbeta.api.world.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.DoubleFunction;
import java.util.stream.IntStream;

import com.bespectacled.modernbeta.mixin.MixinChunkGeneratorSettingsInvoker;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.chunk.ChunkCache;
import com.bespectacled.modernbeta.util.chunk.HeightmapChunk;
import com.bespectacled.modernbeta.util.pool.DoubleArrayPool;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.BlockSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.DeepslateBlockSource;
import net.minecraft.world.gen.NoiseCaveSampler;
import net.minecraft.world.gen.NoiseInterpolator;
import net.minecraft.world.gen.OreVeinGenerator;
import net.minecraft.world.gen.SimpleRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.StructureWeightSampler;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator.NoodleCavesSampler;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator.OreVeinSource;
import net.minecraft.world.gen.chunk.NoodleCavesGenerator;
import net.minecraft.world.gen.chunk.WeightSampler;

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

    protected final boolean generateNoiseCaves;
    protected final boolean generateAquifers;
    protected final boolean generateDeepslate;
    protected final boolean generateOreVeins;
    protected final boolean generateNoodleCaves;
    
    protected final ChunkCache<double[]> noiseChunkCache;
    protected final ChunkCache<HeightmapChunk> heightmapChunkCache;
    
    protected final DoubleArrayPool surfaceNoisePool;
    
    protected final DoublePerlinNoiseSampler edgeDensityNoise;
    protected final DoublePerlinNoiseSampler waterLevelNoise;
    protected final DoublePerlinNoiseSampler lavaNoise;

    protected final WeightSampler noiseCaveSampler;
    protected final OreVeinGenerator oreVeinGenerator;
    protected final NoodleCavesGenerator noodleCaveGenerator;

    public NoiseChunkProvider(OldChunkGenerator chunkGenerator) {
        this(
            chunkGenerator,
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().getMinimumY(),
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().getHeight(),
            chunkGenerator.getGeneratorSettings().get().getSeaLevel(),
            chunkGenerator.getGeneratorSettings().get().getMinSurfaceLevel(),
            chunkGenerator.getGeneratorSettings().get().getBedrockFloorY(),
            chunkGenerator.getGeneratorSettings().get().getBedrockCeilingY(),
            chunkGenerator.getGeneratorSettings().get().getDefaultBlock(),
            chunkGenerator.getGeneratorSettings().get().getDefaultFluid(),
            new DeepslateBlockSource(
                chunkGenerator.getWorldSeed(), 
                chunkGenerator.getGeneratorSettings().get().getDefaultBlock(),
                ((MixinChunkGeneratorSettingsInvoker)(Object)chunkGenerator.getGeneratorSettings().get()).invokeHasDeepslate() ? 
                    BlockStates.DEEPSLATE : 
                    chunkGenerator.getGeneratorSettings().get().getDefaultBlock(), chunkGenerator.getGeneratorSettings().get()
            ),
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
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().getBottomSlide().getOffset(),
            ((MixinChunkGeneratorSettingsInvoker)(Object)chunkGenerator.getGeneratorSettings().get()).invokeHasNoiseCaves(),
            ((MixinChunkGeneratorSettingsInvoker)(Object)chunkGenerator.getGeneratorSettings().get()).invokeHasAquifers(),
            ((MixinChunkGeneratorSettingsInvoker)(Object)chunkGenerator.getGeneratorSettings().get()).invokeHasDeepslate(),
            ((MixinChunkGeneratorSettingsInvoker)(Object)chunkGenerator.getGeneratorSettings().get()).invokeHasOreVeins(),
            ((MixinChunkGeneratorSettingsInvoker)(Object)chunkGenerator.getGeneratorSettings().get()).invokeHasNoodleCaves()
        );
    }

    public NoiseChunkProvider(
        OldChunkGenerator chunkGenerator,
        int minY, 
        int worldHeight, 
        int seaLevel,
        int minSurfaceY,
        int bedrockFloor,
        int bedrockCeiling,
        BlockState defaultBlock,
        BlockState defaultFluid,
        BlockSource blockSource,
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
        int bottomSlideOffset,
        boolean generateNoiseCaves,
        boolean generateAquifers,
        boolean generateDeepslate,
        boolean generateOreVeins,
        boolean generateNoodleCaves
    ) {
        super(chunkGenerator, minY, worldHeight, seaLevel, minSurfaceY, bedrockFloor, bedrockCeiling, defaultBlock, defaultFluid, blockSource);
        
        this.verticalNoiseResolution = sizeVertical * 4;
        this.horizontalNoiseResolution = sizeHorizontal * 4;
        
        this.noiseSizeX = 16 / this.horizontalNoiseResolution;
        this.noiseSizeZ = 16 / this.horizontalNoiseResolution;
        this.noiseSizeY = MathHelper.floorDiv(this.worldHeight, this.verticalNoiseResolution);
        this.noiseMinY = MathHelper.floorDiv(this.minY, this.verticalNoiseResolution);
        this.noiseTopY = MathHelper.floorDiv(this.minY + this.worldHeight, this.verticalNoiseResolution);
        
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
        
        this.generateNoiseCaves = generateNoiseCaves;
        this.generateAquifers = generateAquifers;
        this.generateDeepslate = generateDeepslate;
        this.generateOreVeins = generateOreVeins;
        this.generateNoodleCaves = generateNoodleCaves;
        
        // Chunk caches
        this.noiseChunkCache = new ChunkCache<>(
            "noise", 
            1536, 
            true, 
            (chunkX, chunkZ) -> this.generateNoiseArr(chunkX * this.noiseSizeX, chunkZ * this.noiseSizeZ)
        );
        
        this.heightmapChunkCache = new ChunkCache<>(
            "heightmap", 
            1536, 
            true, 
            this::sampleHeightmap
        );
        
        // Noise array pools
        this.surfaceNoisePool = new DoubleArrayPool(64, arr -> arr.length == 256, 256);      
        
        // Aquifer samplers
        ChunkRandom chunkRandom = new ChunkRandom(seed);
        this.edgeDensityNoise = DoublePerlinNoiseSampler.create(new SimpleRandom(chunkRandom.nextLong()), -3, 1.0);
        this.waterLevelNoise = DoublePerlinNoiseSampler.create(new SimpleRandom(chunkRandom.nextLong()), -3, 1.0, 0.0, 2.0);
        this.lavaNoise = DoublePerlinNoiseSampler.create(new SimpleRandom(chunkRandom.nextLong()), -1, 1.0, 0.0);
        
        // Samplers
        this.noiseCaveSampler = this.generateNoiseCaves ? new NoiseCaveSampler(chunkRandom, this.noiseMinY) : WeightSampler.DEFAULT;
        this.oreVeinGenerator = new OreVeinGenerator(seed, this.defaultBlock, this.horizontalNoiseResolution, this.verticalNoiseResolution, this.generatorSettings.get().getGenerationShapeConfig().getMinimumY());
        this.noodleCaveGenerator = new NoodleCavesGenerator(seed);
    }
    
    /**
     * 
     * @return Number of blocks in a vertical subchunk.
     */
    public int getVerticalNoiseResolution() {
        return this.verticalNoiseResolution;
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
    @Override
    public Chunk provideChunk(StructureAccessor structureAccessor, Chunk chunk) {
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
     * @param world
     * 
     * @return The y-coordinate of top block at x/z.
     */
    @Override
    public int getHeight(int x, int z, Heightmap.Type type, HeightLimitView world) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        
        int groundHeight = this.heightmapChunkCache.get(chunkX, chunkZ).getHeight(x, z);
        
        // Not ideal
        if (type == Heightmap.Type.WORLD_SURFACE_WG && groundHeight < this.seaLevel)
            groundHeight = this.seaLevel;

        return groundHeight;
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
    protected abstract void generateNoiseColumn(double[] buffer, int startNoiseX, int startNoiseZ, int localNoiseX, int localNoiseZ);
    
    /**
     * Generates noise values for entire chunk starting at <code>noiseX</code> and <code>noiseZ</code>.
     * 
     * @param noiseX x-coordinate start of the chunk in noise coordinates.
     * @param noiseZ z-coordinate start of the chunk in noise coordinates.
     * 
     * @returns Populated noise array.
     */
    protected double[] generateNoiseArr(int noiseX, int noiseZ) {
        int noiseResolutionX = this.noiseSizeX + 1;
        int noiseResolutionZ = this.noiseSizeZ + 1;
        int noiseResolutionY = this.noiseSizeY + 1;
        
        double[] buffer = new double[this.noiseSizeY + 1];
        double[] noise = new double[(this.noiseSizeX + 1) * (this.noiseSizeZ + 1) * (this.noiseSizeY + 1)];
        
        int ndx = 0;
        for (int localNoiseX = 0; localNoiseX < noiseResolutionX; ++localNoiseX) {
            for (int localNoiseZ = 0; localNoiseZ < noiseResolutionZ; ++localNoiseZ) {
                this.generateNoiseColumn(buffer, noiseX, noiseZ, localNoiseX, localNoiseZ);
                
                for (int nY = 0; nY < noiseResolutionY; ++nY) {
                    noise[ndx++] = buffer[nY];
                }
            }
        }
        
        return noise;
    }
    
    /**
     * Generates the base terrain for a given chunk.
     * 
     * @param chunk
     * @param structureAccessor Collects structures within the chunk, so that terrain can be modified to accommodate them.
     */
    protected void generateTerrain(Chunk chunk, StructureAccessor structureAccessor) {
        int noiseResolutionY = this.noiseSizeY + 1;
        int noiseResolutionXZ = this.noiseSizeX + 1;
        
        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;
        int startX = chunkPos.getStartX();
        int startZ = chunkPos.getStartZ();
        
        Heightmap heightmapOCEAN = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSURFACE = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
        
        int minY = Math.max(this.minY, chunk.getBottomY());
        int topY = Math.min(this.minY + this.worldHeight, chunk.getTopY());
        
        int noiseMinY = MathHelper.floorDiv(minY, this.verticalNoiseResolution);
        int noiseTopY = MathHelper.floorDiv(topY - minY, this.verticalNoiseResolution);
        
        StructureWeightSampler structureWeightSampler = new StructureWeightSampler(structureAccessor, chunk);
        AquiferSampler aquiferSampler = this.createAquiferSampler(noiseMinY, noiseTopY, chunk.getPos());
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        // Get and populate primary noise array
        double[] noise = this.noiseChunkCache.get(chunkX, chunkZ);
        
        // Create ore vein and noodle cave samplers
        List<NoiseInterpolator> interpolatorList = new ArrayList<>();
        DoubleFunction<BlockSource> oreVeinSampler = this.createOreVeinSamplers(this.noiseMinY, chunkPos, interpolatorList::add);
        DoubleFunction<WeightSampler> noodleCaveSampler = this.createNoodleCaveSampler(this.noiseMinY, chunkPos, interpolatorList::add);
        
        interpolatorList.forEach(NoiseInterpolator::sampleStartNoise);
        for (int subChunkX = 0; subChunkX < this.noiseSizeX; ++subChunkX) {
            int noiseX = subChunkX;
            interpolatorList.forEach(noiseInterpolator -> noiseInterpolator.sampleEndNoise(noiseX));
            
            for (int subChunkZ = 0; subChunkZ < this.noiseSizeZ; ++ subChunkZ) {
                int noiseZ = subChunkZ;
                
                for (int subChunkY = 0; subChunkY < this.noiseSizeY; ++subChunkY) {
                    int noiseY = subChunkY;
                    interpolatorList.forEach(noiseInterpolator -> noiseInterpolator.sampleNoiseCorners(noiseY, noiseZ));
                    
                    double lowerNW = noise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 0)];
                    double lowerSW = noise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 0)];
                    double lowerNE = noise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 0)];
                    double lowerSE = noise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 0)];
                    
                    double upperNW = noise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 1)]; 
                    double upperSW = noise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 1)];
                    double upperNE = noise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 1)];
                    double upperSE = noise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 1)];
                    
                    for (int subY = 0; subY < this.verticalNoiseResolution; ++subY) {
                        int y = subChunkY * this.verticalNoiseResolution + subY;
                        y += this.minY;
                        
                        double deltaY = subY / (double)this.verticalNoiseResolution;
                        interpolatorList.forEach(noiseInterpolator -> noiseInterpolator.sampleNoiseY(deltaY));
                        
                        double nw = MathHelper.lerp(deltaY, lowerNW, upperNW);
                        double sw = MathHelper.lerp(deltaY, lowerSW, upperSW);
                        double ne = MathHelper.lerp(deltaY, lowerNE, upperNE);
                        double se = MathHelper.lerp(deltaY, lowerSE, upperSE);
                        
                        for (int subX = 0; subX < this.horizontalNoiseResolution; ++subX) {
                            int relX = subX + subChunkX * this.horizontalNoiseResolution;
                            int x = startX + relX;
                            
                            double deltaX = subX / (double)this.horizontalNoiseResolution;
                            interpolatorList.forEach(noiseInterpolator -> noiseInterpolator.sampleNoiseX(deltaX));
                            
                            double n = MathHelper.lerp(deltaX, nw, ne);
                            double s = MathHelper.lerp(deltaX, sw, se);
                            
                            for (int subZ = 0; subZ < this.horizontalNoiseResolution; ++subZ) {
                                int relZ = subZ + subChunkZ * this.horizontalNoiseResolution;
                                int z = startZ + relZ;
                                
                                double deltaZ = subZ / (double)this.horizontalNoiseResolution;
                                
                                double density = MathHelper.lerp(deltaZ, n, s);
                                
                                BlockState blockToSet = this.getBlockState(
                                    structureWeightSampler, 
                                    aquiferSampler, 
                                    oreVeinSampler.apply(deltaZ),
                                    noodleCaveSampler.apply(deltaZ),
                                    x, y, z, 
                                    density
                                );
                                
                                chunk.setBlockState(mutable.set(relX, y, relZ), blockToSet, false);
                                
                                heightmapOCEAN.trackUpdate(relX, y, relZ, blockToSet);
                                heightmapSURFACE.trackUpdate(relX, y, relZ, blockToSet);
                                this.scheduleFluidTick(chunk, aquiferSampler, mutable.set(x, y, z), blockToSet);
                            }
                        }
                    }
                }
            }
            interpolatorList.forEach(NoiseInterpolator::swapBuffers);
        }
    }
    
    /**
     * Generates a heightmap for the chunk containing the given x/z coordinates
     * and returns to {@link #getHeight(int, int, net.minecraft.world.Heightmap.Type, HeightLimitView)} 
     * to cache and return the height.
     * 
     * @param chunkX x-coordinate in chunk coordinates to sample all y-values for.
     * @param chunkZ z-coordinate in chunk coordinates to sample all y-values for.
     * 
     * @return A HeightmapChunk, containing an array of ints containing the heights for the entire chunk.
     */
    protected HeightmapChunk sampleHeightmap(int chunkX, int chunkZ) {
        int noiseResolutionY = this.noiseSizeY + 1;
        int noiseResolutionXZ = this.noiseSizeX + 1;
        
        double[] noise = this.noiseChunkCache.get(chunkX, chunkZ);
        
        int[] heightmap = new int[256];
        IntStream.range(0, heightmap.length).forEach(i -> heightmap[i] = 16);

        for (int subChunkX = 0; subChunkX < this.noiseSizeX; ++subChunkX) {
            for (int subChunkZ = 0; subChunkZ < this.noiseSizeZ; ++subChunkZ) {
                for (int subChunkY = 0; subChunkY < this.noiseSizeY; ++subChunkY) {
                    double lowerNW = noise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 0)];
                    double lowerSW = noise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 0)];
                    double lowerNE = noise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 0)];
                    double lowerSE = noise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 0)];
                    
                    double upperNW = noise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 1)]; 
                    double upperSW = noise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 1)];
                    double upperNE = noise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 1)];
                    double upperSE = noise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 1)];
                    
                    for (int subY = 0; subY < this.verticalNoiseResolution; ++subY) {
                        int y = subChunkY * this.verticalNoiseResolution + subY;
                        y += this.minY;
                        
                        double deltaY = subY / (double)this.verticalNoiseResolution;
                        
                        double nw = MathHelper.lerp(deltaY, lowerNW, upperNW);
                        double sw = MathHelper.lerp(deltaY, lowerSW, upperSW);
                        double ne = MathHelper.lerp(deltaY, lowerNE, upperNE);
                        double se = MathHelper.lerp(deltaY, lowerSE, upperSE);
                        
                        for (int subX = 0; subX < this.horizontalNoiseResolution; ++subX) {
                            int x = subX + subChunkX * this.horizontalNoiseResolution;
                            
                            double deltaX = subX / (double)this.horizontalNoiseResolution;
                            
                            double n = MathHelper.lerp(deltaX, nw, ne);
                            double s = MathHelper.lerp(deltaX, sw, se);
                            
                            for (int subZ = 0; subZ < this.horizontalNoiseResolution; ++subZ) {
                                int z = subZ + subChunkZ * this.horizontalNoiseResolution;
                                
                                double deltaZ = subZ / (double)this.horizontalNoiseResolution;
                                
                                double density = MathHelper.lerp(deltaZ, n, s);
                                
                                if (density > 0.0) {
                                    heightmap[z + x * 16] = y + 1;
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // Construct new heightmap cache from generated heightmap array
        return new HeightmapChunk(heightmap);
    }
    
    /**
     * Gets blockstate at block coordinates given noise density.
     * 
     * @param weightSampler Sampler used to add/subtract density if a structure start is at coordinate.
     * @param aquiferSampler Sampler used to adjust local water levels for noise caves.
     * @param x x-coordinate in absolute block coordinates.
     * @param y y-coordinate in absolute block coordinates.
     * @param z z-coordinate in absolute block coordinates.
     * @param density Calculated noise density.
     * 
     * @return A blockstate, usually air, stone, or water.
     */
    protected BlockState getBlockState(StructureWeightSampler weightSampler, AquiferSampler aquiferSampler, BlockSource blockSource, WeightSampler noodleCaveSampler, int x, int y, int z, double density) {
        double clampedDensity = MathHelper.clamp(density / 200.0, -1.0, 1.0);
        clampedDensity = clampedDensity / 2.0 - clampedDensity * clampedDensity * clampedDensity / 24.0;
        clampedDensity = noodleCaveSampler.sample(clampedDensity, x, y, z);
        clampedDensity += weightSampler.getWeight(x, y, z);
        
        return aquiferSampler.apply(blockSource, x, y, z, clampedDensity);
    }
    
    /**
     * Samples density for noise cave.
     * @param noise Base density.
     * @param noiseX x-coordinate in absolute noise coordinates.
     * @param noiseY y-coordinate in absolute noise coordinates.
     * @param noiseZ z-coordinate in absolute noise coordinates.
     * 
     * @return Modified noise density.
     */
    protected double sampleNoiseCave(double noise, int noiseX, int noiseY, int noiseZ) {
        if (this.noiseCaveSampler != null) {
            //return this.noiseCaveSampler.sample(noise, noiseX, noiseY, noiseZ);
            return this.noiseCaveSampler.sample(noise, noiseY, noiseZ, noiseX); // ????
        }
        
        return noise;
    }
    
    /**
     * Interpolates density to set terrain curve at bottom of the world.
     * 
     * @param density Base density.
     * @param noiseY y-coordinate in noise coordinates.
     * @param initialOffset Initial noise y-coordinate offset. Generator settings offset is subtracted from this.
     * 
     * @return Modified noise density.
     */
    protected double applyBottomSlide(double density, int noiseY, int initialOffset) {
        int bottomSlideStart = this.noiseMinY - initialOffset - this.bottomSlideOffset;
        if (noiseY < bottomSlideStart) {
            double bottomSlideDelta = (float) (bottomSlideStart - noiseY) / ((float) this.bottomSlideSize);
            density = MathHelper.lerp(bottomSlideDelta, density, this.bottomSlideTarget);
        }
        
        return density;
    }
    
    /**
     * Interpolates density to set terrain curve at top of the world.
     * 
     * @param density Base density.
     * @param noiseY y-coordinate in noise coordinates.
     * @param initialOffset Initial noise y-coordinate offset. Generator settings offset is subtracted from this.
     * 
     * @return Modified noise density.
     */
    protected double applyTopSlide(double density, int noiseY, int initialOffset) {
        int topSlideStart = (this.noiseSizeY + this.noiseMinY + 1) - initialOffset - this.topSlideOffset;
        if (noiseY > topSlideStart) {
            double topSlideDelta = (float) (noiseY - topSlideStart) / (float) this.topSlideSize;
            density = MathHelper.lerp(topSlideDelta, density, this.topSlideTarget);
        }
        
        return density;
    }
    
    /**
     * Interpolates density to set terrain curve at top of the world.
     * Height target is provided to set curve at a lower world height than the actual.
     * TODO: Check back later to see if this will work with new mountains.
     * 
     * @param density Base density.
     * @param noiseY y-coordinate in noise coordinates.
     * @param initialOffset Initial noise y-coordinate offset. Generator settings offset is subtracted from this.
     * @param heightTarget The world height the curve should target.
     * 
     * @return Modified noise density.
     */
    protected double applyTopSlide(double density, int noiseY, int initialOffset, int heightTarget) {
        int noiseSizeY = MathHelper.floorDiv(heightTarget, this.verticalNoiseResolution);
        int topSlideStart = (noiseSizeY + 1) - initialOffset - this.topSlideOffset;
        if (noiseY > topSlideStart) {
            // Clamp delta since difference of noiseY and slideStart can exceed slideSize if real world height is larger than provided target "height"
            double topSlideDelta = MathHelper.clamp((float) (noiseY - topSlideStart) / (float) this.topSlideSize, 0.0D, 1.0D);
            density = MathHelper.lerp(topSlideDelta, density, this.topSlideTarget);
        }
        
        return density;
    }
    
    private DoubleFunction<WeightSampler> createNoodleCaveSampler(int worldBottomNoiseY, ChunkPos chunkPos, Consumer<NoiseInterpolator> consumer) {
        if (!this.generateNoodleCaves) {
            return d -> WeightSampler.DEFAULT;
        }
        
        NoodleCavesSampler noodleCaveSampler = this.chunkGenerator.new NoodleCavesSampler(chunkPos, worldBottomNoiseY);
        noodleCaveSampler.feed(consumer);
        
        return noodleCaveSampler::setDeltaZ;
    }

    private DoubleFunction<BlockSource> createOreVeinSamplers(int worldBottomNoiseY, ChunkPos chunkPos, Consumer<NoiseInterpolator> consumer) {
        if (!this.generateOreVeins) {
            return noisePoint -> this.blockSource;
        }
        
        OreVeinSource oreVeinSampler = this.chunkGenerator.new OreVeinSource(chunkPos, worldBottomNoiseY, this.seed + 1L);
        oreVeinSampler.feed(consumer);
        
        BlockSource blockSource = (x, y, z) -> {
            BlockState blockState = oreVeinSampler.sample(x, y, z);
            if (blockState != this.defaultBlock) {
                return blockState;
            }
            
            return this.blockSource.sample(x, y, z);
            //return BlockStates.AIR;
        };
    
        return noisePoint -> {
            oreVeinSampler.setDeltaZ(noisePoint);
            return blockSource;
        };
    }

    private AquiferSampler createAquiferSampler(int noiseMinY, int noiseTopY, ChunkPos chunkPos) {
        if (!this.generateAquifers) {
            return AquiferSampler.seaLevel(this.getSeaLevel(), this.defaultFluid);
        }
        return AquiferSampler.aquifer(chunkPos, this.edgeDensityNoise, this.waterLevelNoise, this.lavaNoise, this.generatorSettings.get(), null, noiseMinY * this.verticalNoiseResolution, noiseTopY * this.verticalNoiseResolution);
    }

    /**
     * Schedules fluid tick for aquifer sampler, so water flows when generated.
     * 
     * @param chunk 
     * @param aquiferSampler
     * @param pos BlockPos in block coordinates.
     * @param blockState Blockstate at pos.
     */
    private void scheduleFluidTick(Chunk chunk, AquiferSampler aquiferSampler, BlockPos pos, BlockState blockState) {
        if (aquiferSampler.needsFluidTick() && !blockState.getFluidState().isEmpty()) {
            chunk.getFluidTickScheduler().schedule(pos, blockState.getFluidState().getFluid(), 0);
        }
    }
}

