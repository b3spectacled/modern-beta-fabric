package com.bespectacled.modernbeta.api.world.gen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.world.gen.blocksource.BlockSources;
import com.bespectacled.modernbeta.api.world.gen.noise.BaseNoiseProvider;
import com.bespectacled.modernbeta.api.world.gen.noise.NoiseProvider;
import com.bespectacled.modernbeta.api.world.gen.noise.NoodleCaveNoiseProvider;
import com.bespectacled.modernbeta.api.world.gen.noise.OreVeinNoiseProvider;
import com.bespectacled.modernbeta.mixin.MixinChunkGeneratorSettingsInvoker;
import com.bespectacled.modernbeta.mixin.MixinSlideConfig;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.chunk.ChunkCache;
import com.bespectacled.modernbeta.util.chunk.HeightmapChunk;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;
import com.bespectacled.modernbeta.world.gen.OldChunkNoiseSampler;
import com.bespectacled.modernbeta.world.gen.OldNoiseColumnSampler;
import com.bespectacled.modernbeta.world.gen.sampler.NoiseCaveSampler;
import com.bespectacled.modernbeta.world.gen.sampler.NoodleCaveSampler;
import com.bespectacled.modernbeta.world.gen.sampler.OreVeinSampler;
import com.bespectacled.modernbeta.world.gen.sampler.WeightSampler;
import com.google.common.collect.Sets;

import net.minecraft.block.BlockState;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.gen.BlockSource;
import net.minecraft.world.gen.LayerTransitionBlockSource;
import net.minecraft.world.gen.NoiseColumnSampler;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.StructureWeightSampler;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.random.AtomicSimpleRandom;
import net.minecraft.world.gen.random.ChunkRandom;
import net.minecraft.world.gen.random.RandomDeriver;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

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
    
    protected final double topSlideTarget;
    protected final int topSlideSize;
    protected final int topSlideOffset;
    
    protected final double bottomSlideTarget;
    protected final int bottomSlideSize;
    protected final int bottomSlideOffset;

    protected final boolean generateNoiseCaves;
    protected final boolean generateAquifers;
    protected final boolean generateDeepslate;
    protected final boolean generateOreVeins;
    protected final boolean generateNoodleCaves;

    protected final ChunkCache<BaseNoiseProvider> noiseProviderCache;
    protected final ChunkCache<HeightmapChunk> heightmapChunkCache;
    
    protected final AquiferSamplerProvider aquiferSamplerProvider;
    
    protected final NoiseCaveSampler noiseCaveSampler;
    protected final OreVeinSampler oreVeinSampler;
    protected final NoodleCaveSampler noodleCaveSampler;
    
    protected final NoiseColumnSampler noiseColumnSampler;
    protected final BlockSource deepslateSource;
    protected final ChunkNoiseSampler dummyNoiseChunkSampler;

    public NoiseChunkProvider(OldChunkGenerator chunkGenerator) {
        this(
            chunkGenerator,
            chunkGenerator.getNoiseRegistry(),
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().minimumY(),
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().height(),
            chunkGenerator.getGeneratorSettings().get().getSeaLevel(),
            chunkGenerator.getGeneratorSettings().get().getBedrockFloorY(),
            chunkGenerator.getGeneratorSettings().get().getBedrockCeilingY(),
            chunkGenerator.getGeneratorSettings().get().getDefaultBlock(),
            chunkGenerator.getGeneratorSettings().get().getDefaultFluid(),
            chunkGenerator.getGeneratorSettings().get().getRandomProvider(),
            chunkGenerator.getGeneratorSettings().get().getSurfaceRule(),
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().verticalSize(),
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().horizontalSize(),
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().sampling().getXZScale(),
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().sampling().getYScale(),
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().sampling().getXZFactor(),
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().sampling().getYFactor(),
            ((MixinSlideConfig)chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().topSlide()).getTarget(),
            ((MixinSlideConfig)chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().topSlide()).getSize(),
            ((MixinSlideConfig)chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().topSlide()).getOffset(),
            ((MixinSlideConfig)chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().bottomSlide()).getTarget(),
            ((MixinSlideConfig)chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().bottomSlide()).getSize(),
            ((MixinSlideConfig)chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().bottomSlide()).getOffset(),
            ((MixinChunkGeneratorSettingsInvoker)(Object)chunkGenerator.getGeneratorSettings().get()).invokeHasNoiseCaves(),
            ((MixinChunkGeneratorSettingsInvoker)(Object)chunkGenerator.getGeneratorSettings().get()).invokeHasAquifers(),
            ((MixinChunkGeneratorSettingsInvoker)(Object)chunkGenerator.getGeneratorSettings().get()).invokeHasDeepslate(),
            ((MixinChunkGeneratorSettingsInvoker)(Object)chunkGenerator.getGeneratorSettings().get()).invokeHasOreVeins(),
            ((MixinChunkGeneratorSettingsInvoker)(Object)chunkGenerator.getGeneratorSettings().get()).invokeHasNoodleCaves()
        );
    }

    public NoiseChunkProvider(
        OldChunkGenerator chunkGenerator,
        Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry,
        int worldMinY, 
        int worldHeight, 
        int seaLevel,
        int bedrockFloor,
        int bedrockCeiling,
        BlockState defaultBlock,
        BlockState defaultFluid,
        ChunkRandom.RandomProvider randomProvider,
        MaterialRules.MaterialRule surfaceRule,
        int sizeVertical, 
        int sizeHorizontal,
        double xzScale, 
        double yScale,
        double xzFactor, 
        double yFactor,
        double topSlideTarget,
        int topSlideSize,
        int topSlideOffset,
        double bottomSlideTarget,
        int bottomSlideSize,
        int bottomSlideOffset,
        boolean generateNoiseCaves,
        boolean generateAquifers,
        boolean generateDeepslate,
        boolean generateOreVeins,
        boolean generateNoodleCaves
    ) {
        super(
            chunkGenerator, 
            noiseRegistry, 
            worldMinY, 
            worldHeight, 
            seaLevel, 
            bedrockFloor, 
            bedrockCeiling, 
            defaultBlock, 
            defaultFluid, 
            randomProvider, 
            surfaceRule
        );
        
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
        
        this.generateNoiseCaves = generateNoiseCaves;
        this.generateAquifers = generateAquifers;
        this.generateDeepslate = generateDeepslate;
        this.generateOreVeins = generateOreVeins;
        this.generateNoodleCaves = generateNoodleCaves;
        
        this.noiseProviderCache = new ChunkCache<>(
            "noise_provider",
            1536,
            true,
            (chunkX, chunkZ) -> {
                BaseNoiseProvider baseNoiseProvider = new BaseNoiseProvider(
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

        // Modified NoiseColumnSampler
        this.noiseColumnSampler = new OldNoiseColumnSampler(this);
        
        // Dummy ChunkNoiseSampler
        this.dummyNoiseChunkSampler = new OldChunkNoiseSampler(
            this.horizontalNoiseResolution,
            this.verticalNoiseResolution,
            16 / this.horizontalNoiseResolution,
            this.noiseTopY,
            this.worldMinY,
            this.noiseColumnSampler,
            0, 
            0,
            null,
            this.generatorSettings,
            this.fluidLevelSampler,
            null // Blender
        );
        
        // Random deriver
        RandomDeriver randomDeriver = randomProvider.create(this.seed).createBlockPosRandomDeriver();
        
        // Aquifer Sampler Provider
        this.aquiferSamplerProvider = new AquiferSamplerProvider(
            noiseRegistry,
            randomDeriver,
            this.noiseColumnSampler,
            this.dummyNoiseChunkSampler,
            this.defaultFluid,
            this.seaLevel,
            ChunkProvider.LAVA_LEVEL,
            this.worldMinY,
            this.worldHeight,
            this.verticalNoiseResolution,
            this.generateAquifers
        );
        
        // Samplers
        this.noiseCaveSampler = new NoiseCaveSampler(noiseRegistry, randomDeriver, this.noiseMinY);
        this.oreVeinSampler = new OreVeinSampler(noiseRegistry, randomDeriver, this.horizontalNoiseResolution, this.verticalNoiseResolution);
        this.noodleCaveSampler = new NoodleCaveSampler(noiseRegistry, randomDeriver, this.horizontalNoiseResolution, this.verticalNoiseResolution);
        
        // Block Source
        AtomicSimpleRandom atomicSimpleRandom = new AtomicSimpleRandom(seed);
        this.deepslateSource = this.generateDeepslate ? 
            new LayerTransitionBlockSource(atomicSimpleRandom.createBlockPosRandomDeriver(), BlockStates.DEEPSLATE, null, -8, 0) :
            (sampler, x, y, z) -> null;
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
    public CompletableFuture<Chunk> provideChunk(Executor executor, StructureAccessor structureAccessor, Chunk chunk) {
        GenerationShapeConfig shapeConfig = this.generatorSettings.get().getGenerationShapeConfig();
        
        int minY = Math.max(shapeConfig.minimumY(), chunk.getBottomY());
        int topY = Math.min(shapeConfig.minimumY() + shapeConfig.height(), chunk.getTopY());
        
        @SuppressWarnings("unused")
        int noiseMinY = MathHelper.floorDiv(minY, this.verticalNoiseResolution);
        int noiseTopY = MathHelper.floorDiv(topY - minY, this.verticalNoiseResolution);
        
        if (noiseTopY <= 0) {
            return CompletableFuture.completedFuture(chunk);
        }
        
        int sectionTopY = chunk.getSectionIndex(noiseTopY * verticalNoiseResolution - 1 + minY);
        int sectionMinY = chunk.getSectionIndex(minY);
        
        HashSet<ChunkSection> sections = Sets.newHashSet();
        for (int sectionNdx = sectionTopY; sectionNdx >= sectionMinY; --sectionNdx) {
            ChunkSection section = chunk.getSection(sectionNdx);
            
            section.lock();
            sections.add(section);
        }
        
        this.generateTerrain(chunk, structureAccessor);
        
        return CompletableFuture.supplyAsync(() -> chunk, Util.getMainWorkerExecutor())
            .whenCompleteAsync((arg, throwable) -> {
                for (ChunkSection section : sections) {
                    section.unlock();
            }
        }, executor);
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
    
    @Override
    public AquiferSampler getAquiferSampler(Chunk chunk) {
        return this.aquiferSamplerProvider.provideAquiferSampler(chunk);
    }
    
    /**
     * Generates noise for a column at startNoiseX + localNoiseX / startNoiseZ + localNoiseZ.
     * @param primaryBuffer Primary heightmap buffer, with noise caves.
     * @param heightmapBuffer Heightmap buffer, identical to primaryBuffer sans noise caves.
     * @param startNoiseX x-coordinate start of chunk in noise coordinates.
     * @param startNoiseZ z-coordinate start of chunk in noise coordinates.
     * @param localNoiseX Current subchunk index along x-axis.
     * @param localNoiseZ Current subchunk index along z-axis.
     */
    protected abstract void sampleNoiseColumn(
        double[] primaryBuffer,
        double[] heightmapBuffer,
        int startNoiseX,
        int startNoiseZ,
        int localNoiseX,
        int localNoiseZ
    );

    /**
     * Samples density for noise cave.
     * @param noise Base density.
     * @param noiseX x-coordinate in absolute noise coordinates.
     * @param noiseY y-coordinate in absolute noise coordinates.
     * @param noiseZ z-coordinate in absolute noise coordinates.
     * 
     * @return Modified noise density.
     */
    protected double sampleNoiseCave(double noise, double tunnelThreshold, int noiseX, int noiseY, int noiseZ) {
        if (this.generateNoiseCaves) {
            return this.noiseCaveSampler.sample(
                noise,
                tunnelThreshold,
                noiseX * this.horizontalNoiseResolution, 
                noiseY * this.verticalNoiseResolution, 
                noiseZ * this.horizontalNoiseResolution
            );
        }
        
        return noise;
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
     * Schedules fluid tick for aquifer sampler, so water flows when generated.
     * 
     * @param chunk 
     * @param aquiferSampler
     * @param pos BlockPos in block coordinates.
     * @param blockState Blockstate at pos.
     */
    protected void scheduleFluidTick(Chunk chunk, AquiferSampler aquiferSampler, BlockPos pos, BlockState blockState) {
        if (aquiferSampler.needsFluidTick() && !blockState.getFluidState().isEmpty()) {
            // TODO: Fix later
            //chunk.getFluidTickScheduler().schedule(pos, blockState.getFluidState().getFluid(), 0);
        }
    }

    protected HeightmapChunk getHeightmapChunk(int chunkX, int chunkZ) {
        return this.heightmapChunkCache.get(chunkX, chunkZ);
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
        AquiferSampler aquiferSampler = this.getAquiferSampler(chunk);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        ChunkNoiseSampler chunkNoiseSampler = null;
        
        // Create and populate noise providers
        List<NoiseProvider> noiseProviders = new ArrayList<>();
        
        NoiseProvider baseNoiseProvider = this.noiseProviderCache.get(chunkX, chunkZ);
        WeightSampler noodleCaveSampler = this.createNoodleCaveNoiseProviders(chunkPos, noiseProviders::add);
        BlockSource oreVeinBlockSource = this.createOreVeinProviders(chunkPos, noiseProviders::add);
        BlockSource baseBlockSource = this.getBaseBlockSource(baseNoiseProvider, structureWeightSampler, aquiferSampler, noodleCaveSampler);
        
        // Create and populate block sources
        BlockSources blockSources = new BlockSources.Builder()
            .add(baseBlockSource)
            .add(oreVeinBlockSource)
            .add(this.deepslateSource)
            .build(this.defaultBlock);

        // Sample initial noise.
        // Base noise should be added after this,
        // since base noise is sampled when fetched from cache.
        noiseProviders.forEach(noiseProvider -> noiseProvider.sampleInitialNoise(chunkX * this.noiseSizeX, chunkZ * this.noiseSizeZ));
        noiseProviders.add(baseNoiseProvider);
        
        for (int subChunkX = 0; subChunkX < this.noiseSizeX; ++subChunkX) {
            int noiseX = subChunkX;
            
            for (int subChunkZ = 0; subChunkZ < this.noiseSizeZ; ++subChunkZ) {
                int noiseZ = subChunkZ;
                
                ChunkSection section = chunk.getSection(chunk.countVerticalSections() - 1);
                
                for (int subChunkY = 0; subChunkY < this.noiseSizeY; ++subChunkY) {
                    int noiseY = subChunkY;
                    
                    noiseProviders.forEach(noiseProvider -> noiseProvider.sampleNoiseCorners(noiseX, noiseY, noiseZ));
                    
                    for (int subY = 0; subY < this.verticalNoiseResolution; ++subY) {
                        int y = subY + (subChunkY + this.noiseMinY) * this.verticalNoiseResolution;
                        int localY = y & 0xF;
                        
                        int sectionNdx = chunk.getSectionIndex(y);
                        if (chunk.getSectionIndex(section.getYOffset()) != sectionNdx) {
                            section = chunk.getSection(sectionNdx);
                        }
                        
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
                                
                                BlockState blockState = blockSources.apply(chunkNoiseSampler, x, y, z);
                                if (blockState.equals(BlockStates.AIR)) continue;
                                
                                if (blockState.getLuminance() != 0 && chunk instanceof ProtoChunk protoChunk) {
                                    protoChunk.addLightSource(mutable.set(x, y, z));
                                }
                                
                                section.setBlockState(localX, localY, localZ, blockState, false);

                                heightmapOcean.trackUpdate(localX, y, localZ, blockState);
                                heightmapSurface.trackUpdate(localX, y, localZ, blockState);
                                
                                this.scheduleFluidTick(chunk, aquiferSampler, mutable.set(x, y, z), blockState);
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
        short minHeight = 16;
        
        BaseNoiseProvider baseNoiseProvider = this.noiseProviderCache.get(chunkX, chunkZ);
        
        short[] heightmapSurface = new short[256];
        short[] heightmapOcean = new short[256];
        short[] heightmapSurfaceFloor = new short[256];
        
        Arrays.fill(heightmapSurface, minHeight);
        Arrays.fill(heightmapOcean, minHeight);
        Arrays.fill(heightmapSurfaceFloor, Short.MIN_VALUE);
        
        for (int subChunkX = 0; subChunkX < this.noiseSizeX; ++subChunkX) {
            for (int subChunkZ = 0; subChunkZ < this.noiseSizeZ; ++subChunkZ) {
                for (int subChunkY = 0; subChunkY < this.noiseSizeY; ++subChunkY) {
                    baseNoiseProvider.sampleNoiseCornersHeightmap(subChunkX, subChunkY, subChunkZ);
                    
                    for (int subY = 0; subY < this.verticalNoiseResolution; ++subY) {
                        int y = subY + subChunkY * this.verticalNoiseResolution;
                        y += this.worldMinY;
                        
                        double deltaY = subY / (double)this.verticalNoiseResolution;
                        baseNoiseProvider.sampleNoiseYHeightmap(deltaY);
                        
                        for (int subX = 0; subX < this.horizontalNoiseResolution; ++subX) {
                            int x = subX + subChunkX * this.horizontalNoiseResolution;
                            
                            double deltaX = subX / (double)this.horizontalNoiseResolution;
                            baseNoiseProvider.sampleNoiseXHeightmap(deltaX);
                            
                            for (int subZ = 0; subZ < this.horizontalNoiseResolution; ++subZ) {
                                int z = subZ + subChunkZ * this.horizontalNoiseResolution;
                                
                                double deltaZ = subZ / (double)this.horizontalNoiseResolution;
                                baseNoiseProvider.sampleNoiseZHeightmap(deltaZ);
                                
                                double density = baseNoiseProvider.sampleHeightmap();
                                
                                short height = (short)(y + 1);
                                int ndx = z + x * 16;
                                
                                // Capture topmost solid/fluid block height.
                                if (y < this.seaLevel || density > 0.0) {
                                    heightmapOcean[ndx] = height;
                                }
                                
                                // Capture topmost solid block height.
                                if (density > 0.0) {
                                    heightmapSurface[ndx] = height;
                                }
                                
                                // Capture lowest solid block height.
                                if (density <= 0.0 && heightmapSurfaceFloor[ndx] == Short.MIN_VALUE) {
                                    heightmapSurfaceFloor[ndx] = (short)(height - 1);
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
        StructureWeightSampler weightSampler,
        AquiferSampler aquiferSampler,
        WeightSampler noodleCaveSampler
    ) {
        return (noiseSampler, x, y, z) -> {
            double density = baseNoiseProvider.sample();
            double clampedDensity = MathHelper.clamp(density / 200.0, -1.0, 1.0);
            
            clampedDensity = clampedDensity / 2.0 - clampedDensity * clampedDensity * clampedDensity / 24.0;
            clampedDensity = noodleCaveSampler.sample(clampedDensity, z, x, y);
            clampedDensity += weightSampler.calculateNoise(x, y, z);
            
            // Normalize and clamp density to vanilla -64 to 64 range before sending to aquifer.
            density = MathHelper.clamp(density / 200.0 * 64.0, -64.0, 64.0);
            
            return aquiferSampler.apply(x, y, z, density, clampedDensity);
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
        if (!this.generateNoodleCaves)
            return (weight, x, y, z) -> weight;
        
        NoodleCaveNoiseProvider frequencyNoiseProvider = new NoodleCaveNoiseProvider(
            this.noiseSizeX,
            this.noiseSizeY,
            this.noiseSizeZ,
            this.noiseMinY,
            this.noodleCaveSampler::sampleFrequencyNoise
        );
        
        NoodleCaveNoiseProvider reducingNoiseProvider = new NoodleCaveNoiseProvider(
            this.noiseSizeX,
            this.noiseSizeY,
            this.noiseSizeZ,
            this.noiseMinY,
            this.noodleCaveSampler::sampleWeightReducingNoise
        );
        
        NoodleCaveNoiseProvider firstNoiseProvider = new NoodleCaveNoiseProvider(
            this.noiseSizeX,
            this.noiseSizeY,
            this.noiseSizeZ,
            this.noiseMinY,
            this.noodleCaveSampler::sampleFirstWeightNoise
        );
        
        NoodleCaveNoiseProvider secondNoiseProvider = new NoodleCaveNoiseProvider(
            this.noiseSizeX,
            this.noiseSizeY,
            this.noiseSizeZ,
            this.noiseMinY,
            this.noodleCaveSampler::sampleSecondWeightNoise
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
            
            return this.noodleCaveSampler.sampleWeight(weight, x, y, z, frequencyNoise, reducingNoise, firstNoise, secondNoise, this.worldMinY);
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
        if (!this.generateOreVeins)
            return (noiseSampler, x, y, z) -> null;

        OreVeinNoiseProvider frequencyNoiseProvider = new OreVeinNoiseProvider(
            this.noiseSizeX,
            this.noiseSizeY,
            this.noiseSizeZ,
            this.noiseMinY,
            this.oreVeinSampler::sampleOreFrequencyNoise
        );
        
        OreVeinNoiseProvider firstOreNoiseProvider = new OreVeinNoiseProvider(
            this.noiseSizeX,
            this.noiseSizeY,
            this.noiseSizeZ,
            this.noiseMinY,
            this.oreVeinSampler::sampleFirstOrePlacementNoise
        );
        
        OreVeinNoiseProvider secondOreNoiseProvider = new OreVeinNoiseProvider(
            this.noiseSizeX,
            this.noiseSizeY,
            this.noiseSizeZ,
            this.noiseMinY,
            this.oreVeinSampler::sampleSecondOrePlacementNoise
        );
        
        consumer.accept(frequencyNoiseProvider);
        consumer.accept(firstOreNoiseProvider);
        consumer.accept(secondOreNoiseProvider);
        
        return (noiseSampler, x, y, z) -> {
            double frequencyNoise = frequencyNoiseProvider.sample();
            double firstOreNoise = firstOreNoiseProvider.sample();
            double secondOreNoise = secondOreNoiseProvider.sample();
            
            return this.oreVeinSampler.sample(x, y, z, frequencyNoise, firstOreNoise, secondOreNoise);
        };
    }
}

