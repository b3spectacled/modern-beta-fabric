package com.bespectacled.modernbeta.api.world.gen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.world.gen.blocksource.BlockSources;
import com.bespectacled.modernbeta.api.world.gen.noise.BaseNoiseProvider;
import com.bespectacled.modernbeta.api.world.gen.noise.NoiseProvider;
import com.bespectacled.modernbeta.api.world.gen.noise.NoodleCaveNoiseProvider;
import com.bespectacled.modernbeta.api.world.gen.noise.OreVeinNoiseProvider;
import com.bespectacled.modernbeta.mixin.MixinChunkGeneratorSettingsInvoker;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.chunk.ChunkCache;
import com.bespectacled.modernbeta.util.chunk.HeightmapChunk;
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
import net.minecraft.world.gen.OreVeinGenerator;
import net.minecraft.world.gen.SimpleRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.StructureWeightSampler;
import net.minecraft.world.gen.chunk.AquiferSampler;
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

    protected final ChunkCache<NoiseProvider> noiseProviderCache;
    protected final ChunkCache<HeightmapChunk> heightmapChunkCache;
    
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
        
        this.noiseProviderCache = new ChunkCache<>(
            "noise_provider",
            1536,
            true,
            (chunkX, chunkZ) -> new BaseNoiseProvider(
                this.noiseSizeX,
                this.noiseSizeY,
                this.noiseSizeZ,
                chunkX * this.noiseSizeX,
                chunkZ * this.noiseSizeZ,
                this::generateNoiseColumn
            )
        );
        
        this.heightmapChunkCache = new ChunkCache<>(
            "heightmap", 
            1536, 
            true, 
            this::sampleHeightmap
        ); 
        
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
    protected abstract void generateNoiseColumn(double[] buffer, int startNoiseX, int startNoiseZ, int localNoiseX, int localNoiseZ);
    
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
        
        int minY = Math.max(this.minY, chunk.getBottomY());
        int topY = Math.min(this.minY + this.worldHeight, chunk.getTopY());
        
        int noiseMinY = MathHelper.floorDiv(minY, this.verticalNoiseResolution);
        int noiseTopY = MathHelper.floorDiv(topY - minY, this.verticalNoiseResolution);
        
        StructureWeightSampler structureWeightSampler = new StructureWeightSampler(structureAccessor, chunk);
        AquiferSampler aquiferSampler = this.createAquiferSampler(noiseMinY, noiseTopY, chunk.getPos());
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        // Create and populate noise providers
        List<NoiseProvider> noiseProviders = new ArrayList<>();
        
        NoiseProvider baseNoiseProvider = this.noiseProviderCache.get(chunkX, chunkZ);
        noiseProviders.add(baseNoiseProvider);
        
        WeightSampler noodleCaveSampler = this.createNoodleCaveNoiseProviders(chunkPos, noiseProviders::add);
        BlockSource oreVeinBlockSource = this.createOreVeinProviders(chunkPos, noiseProviders::add);
        BlockSource baseBlockSource = this.getBaseBlockSource(baseNoiseProvider, structureWeightSampler, aquiferSampler, this.blockSource, noodleCaveSampler);
        
        // Create and populate block sources
        BlockSources blockSources = new BlockSources.Builder()
            .add(oreVeinBlockSource)
            .add(baseBlockSource)
            .build();

        for (int subChunkX = 0; subChunkX < this.noiseSizeX; ++subChunkX) {
            int noiseX = subChunkX;
            
            for (int subChunkZ = 0; subChunkZ < this.noiseSizeZ; ++ subChunkZ) {
                int noiseZ = subChunkZ;
                
                for (int subChunkY = 0; subChunkY < this.noiseSizeY; ++subChunkY) {
                    int noiseY = subChunkY;
                    
                    noiseProviders.forEach(noiseProvider -> noiseProvider.sampleNoiseCorners(noiseX, noiseY, noiseZ));
                    
                    for (int subY = 0; subY < this.verticalNoiseResolution; ++subY) {
                        int y = subY + subChunkY * this.verticalNoiseResolution;
                        y += this.minY;
                        
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
                                
                                BlockState blockToSet = blockSources.sample(x, y, z);
                                chunk.setBlockState(mutable.set(localX, y, localZ), blockToSet, false);

                                heightmapOcean.trackUpdate(localX, y, localZ, blockToSet);
                                heightmapSurface.trackUpdate(localX, y, localZ, blockToSet);
                                
                                this.scheduleFluidTick(chunk, aquiferSampler, mutable.set(x, y, z), blockToSet);
                            }
                        }
                    }
                }
            }
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
    private HeightmapChunk sampleHeightmap(int chunkX, int chunkZ) {
        int minHeight = 16;
        
        NoiseProvider baseNoiseProvider = this.noiseProviderCache.get(chunkX, chunkZ);
        
        int[] heightmapSurface = new int[256];
        int[] heightmapOcean = new int[256];
        
        Arrays.fill(heightmapSurface, minHeight);
        Arrays.fill(heightmapOcean, minHeight);
        
        for (int subChunkX = 0; subChunkX < this.noiseSizeX; ++subChunkX) {
            for (int subChunkZ = 0; subChunkZ < this.noiseSizeZ; ++subChunkZ) {
                for (int subChunkY = 0; subChunkY < this.noiseSizeY; ++subChunkY) {
                    baseNoiseProvider.sampleNoiseCorners(subChunkX, subChunkY, subChunkZ);
                    
                    for (int subY = 0; subY < this.verticalNoiseResolution; ++subY) {
                        int y = subY + subChunkY * this.verticalNoiseResolution;
                        y += this.minY;
                        
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
                                
                                if (y < this.seaLevel || density > 0.0) {
                                    heightmapOcean[z + x * 16] = y + 1;
                                }
                                
                                if (density > 0.0) {
                                    heightmapSurface[z + x * 16] = y + 1;
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // Construct new heightmap cache from generated heightmap array
        return new HeightmapChunk(heightmapSurface, heightmapOcean);
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
        StructureWeightSampler weightSampler, 
        AquiferSampler aquiferSampler, 
        BlockSource blockSource, 
        WeightSampler noodleCaveSampler
    ) {
        return (x, y, z) -> {
            double density = baseNoiseProvider.sample();
            double clampedDensity = MathHelper.clamp(density / 200.0, -1.0, 1.0);
            
            clampedDensity = clampedDensity / 2.0 - clampedDensity * clampedDensity * clampedDensity / 24.0;
            clampedDensity = noodleCaveSampler.sample(clampedDensity, x, y, z);
            clampedDensity += weightSampler.getWeight(x, y, z);
            
            return aquiferSampler.apply(blockSource, x, y, z, clampedDensity);
        };
    }

    /**
     * Creates noise providers to interpolate noodle cave noise and adds to interpolator list. 
     * 
     * @param chunkPos
     * @param consumer
     * 
     * @return WeightSampler to sample density addition at x/y/z block coordinates.
     */
    private WeightSampler createNoodleCaveNoiseProviders(ChunkPos chunkPos, Consumer<NoodleCaveNoiseProvider> consumer) {
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;
        
        if (!this.generateNoodleCaves)
            return (weight, x, y, z) -> weight;
        
        NoodleCaveNoiseProvider frequencyNoiseProvider = new NoodleCaveNoiseProvider(
            this.noiseSizeX,
            this.noiseSizeY,
            this.noiseSizeZ,
            chunkX * this.noiseSizeX,
            chunkZ * this.noiseSizeZ,
            this.noiseMinY,
            this.noodleCaveGenerator::sampleFrequencyNoise
        );
        
        NoodleCaveNoiseProvider reducingNoiseProvider = new NoodleCaveNoiseProvider(
            this.noiseSizeX,
            this.noiseSizeY,
            this.noiseSizeZ,
            chunkX * this.noiseSizeX,
            chunkZ * this.noiseSizeZ,
            this.noiseMinY,
            this.noodleCaveGenerator::sampleWeightReducingNoise
        );
        
        NoodleCaveNoiseProvider firstNoiseProvider = new NoodleCaveNoiseProvider(
            this.noiseSizeX,
            this.noiseSizeY,
            this.noiseSizeZ,
            chunkX * this.noiseSizeX,
            chunkZ * this.noiseSizeZ,
            this.noiseMinY,
            this.noodleCaveGenerator::sampleFirstWeightNoise
        );
        
        NoodleCaveNoiseProvider secondNoiseProvider = new NoodleCaveNoiseProvider(
            this.noiseSizeX,
            this.noiseSizeY,
            this.noiseSizeZ,
            chunkX * this.noiseSizeX,
            chunkZ * this.noiseSizeZ,
            this.noiseMinY,
            this.noodleCaveGenerator::sampleSecondWeightNoise
        );
        
        consumer.accept(frequencyNoiseProvider);
        consumer.accept(reducingNoiseProvider);
        consumer.accept(firstNoiseProvider);
        consumer.accept(secondNoiseProvider);
        
        return (weight, x, y, z) -> {
            double frequencyNoise = frequencyNoiseProvider.sample();
            double reducingNoise = reducingNoiseProvider.sample();
            double firstNoise = firstNoiseProvider.sample();
            double secondNoise = secondNoiseProvider.sample();
            
            return this.noodleCaveGenerator.sampleWeight(weight, x, y, z, frequencyNoise, reducingNoise, firstNoise, secondNoise, this.minY);
        };
    }
    
    /**
     * Creates noise providers to interpolate ore vein noise and adds to interpolator list. 
     * 
     * @param chunkPos
     * @param consumer
     * 
     * @return BlockSource to sample alternate blockstate at x/y/z block coordinates.
     */
    private BlockSource createOreVeinProviders(ChunkPos chunkPos, Consumer<OreVeinNoiseProvider> consumer) {
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;
        
        if (!this.generateOreVeins)
            return (x, y, z) -> null;

        OreVeinNoiseProvider frequencyNoiseProvider = new OreVeinNoiseProvider(
            this.noiseSizeX,
            this.noiseSizeY,
            this.noiseSizeZ,
            chunkX * this.noiseSizeX,
            chunkZ * this.noiseSizeZ,
            this.noiseMinY,
            this.oreVeinGenerator::sampleOreFrequencyNoise
        );
        
        OreVeinNoiseProvider firstOreNoiseProvider = new OreVeinNoiseProvider(
            this.noiseSizeX,
            this.noiseSizeY,
            this.noiseSizeZ,
            chunkX * this.noiseSizeX,
            chunkZ * this.noiseSizeZ,
            this.noiseMinY,
            this.oreVeinGenerator::sampleFirstOrePlacementNoise
        );
        
        OreVeinNoiseProvider secondOreNoiseProvider = new OreVeinNoiseProvider(
            this.noiseSizeX,
            this.noiseSizeY,
            this.noiseSizeZ,
            chunkX * this.noiseSizeX,
            chunkZ * this.noiseSizeZ,
            this.noiseMinY,
            this.oreVeinGenerator::sampleSecondOrePlacementNoise
        );
        
        consumer.accept(frequencyNoiseProvider);
        consumer.accept(firstOreNoiseProvider);
        consumer.accept(secondOreNoiseProvider);
        
        return (x, y, z) -> {
            double frequencyNoise = frequencyNoiseProvider.sample();
            double firstOreNoise = firstOreNoiseProvider.sample();
            double secondOreNoise = secondOreNoiseProvider.sample();
            
            ChunkRandom random = new ChunkRandom();
            random.setDeepslateSeed(this.seed, x, y, z);
            
            BlockState blockState = this.oreVeinGenerator.sample(random, z, y, z, frequencyNoise, firstOreNoise, secondOreNoise);
            
            if (blockState != this.defaultBlock)
                return blockState;
            
            return null;
        };
    }

    /**
     * Creates aquifer sampler.
     * 
     * @param noiseMinY
     * @param noiseTopY
     * @param chunkPos
     * 
     * @return
     */
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

