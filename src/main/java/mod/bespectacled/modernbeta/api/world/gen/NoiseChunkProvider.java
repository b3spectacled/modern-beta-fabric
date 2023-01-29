package mod.bespectacled.modernbeta.api.world.gen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.google.common.collect.Sets;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.api.world.gen.noise.BaseNoiseProvider;
import mod.bespectacled.modernbeta.api.world.gen.noise.NoisePostProcessor;
import mod.bespectacled.modernbeta.api.world.gen.noise.NoiseProvider;
import mod.bespectacled.modernbeta.util.BlockStates;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import mod.bespectacled.modernbeta.util.chunk.ChunkCache;
import mod.bespectacled.modernbeta.util.chunk.HeightmapChunk;
import mod.bespectacled.modernbeta.util.noise.SimpleNoisePos;
import mod.bespectacled.modernbeta.util.settings.Settings;
import mod.bespectacled.modernbeta.world.gen.ModernBetaChunkGenerator;
import mod.bespectacled.modernbeta.world.gen.blocksource.BlockSourceRules;
import mod.bespectacled.modernbeta.world.gen.blocksource.SimpleBlockSource;
import net.minecraft.block.BlockState;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.StructureWeightSampler;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.chunk.SlideConfig;
import net.minecraft.world.gen.random.RandomDeriver;

public abstract class NoiseChunkProvider extends ChunkProvider {
    
    protected final int worldMinY;
    protected final int worldHeight;
    protected final int worldTopY;
    protected final int seaLevel;
    
    protected final int bedrockFloor;
    protected final int bedrockCeiling;
    
    protected final boolean generateDeepslate;
    
    protected final BlockState defaultBlock;
    protected final BlockState defaultFluid;
    
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
    
    protected final boolean generateAquifers;
    
    private final SlideConfig topSlide;
    private final SlideConfig bottomSlide;
    
    private final ChunkCache<BaseNoiseProvider> baseNoiseCache;
    private final ChunkCache<HeightmapChunk> heightmapCache;
    
    private final AquiferSamplerProvider aquiferSamplerProvider;
    private final NoisePostProcessor noisePostProcessor;
    
    public NoiseChunkProvider(ModernBetaChunkGenerator chunkGenerator) {
        super(chunkGenerator);
        
        Settings providerSettings = chunkGenerator.getChunkSettings();
        ChunkGeneratorSettings generatorSettings = chunkGenerator.getGeneratorSettings().value();
        GenerationShapeConfig shapeConfig = generatorSettings.generationShapeConfig();
        
        this.worldMinY = shapeConfig.minimumY();
        this.worldHeight = shapeConfig.height();
        this.worldTopY = worldHeight + worldMinY;
        this.seaLevel = generatorSettings.seaLevel();
        this.bedrockFloor = 0;
        this.bedrockCeiling = Integer.MIN_VALUE;
        this.generateDeepslate = NbtUtil.toBoolean(providerSettings.get(NbtTags.USE_DEEPSLATE), ModernBeta.CHUNK_CONFIG.useDeepslate);

        this.defaultBlock = generatorSettings.defaultBlock();
        this.defaultFluid = generatorSettings.defaultFluid();
        
        this.verticalNoiseResolution = shapeConfig.verticalSize() * 4;
        this.horizontalNoiseResolution = shapeConfig.horizontalSize() * 4;
        
        this.noiseSizeX = 16 / this.horizontalNoiseResolution;
        this.noiseSizeZ = 16 / this.horizontalNoiseResolution;
        this.noiseSizeY = MathHelper.floorDiv(this.worldHeight, this.verticalNoiseResolution);
        this.noiseMinY = MathHelper.floorDiv(this.worldMinY, this.verticalNoiseResolution);
        this.noiseTopY = MathHelper.floorDiv(this.worldMinY + this.worldHeight, this.verticalNoiseResolution);
        
        this.xzScale = shapeConfig.sampling().xzScale();
        this.yScale = shapeConfig.sampling().yScale();
        
        this.xzFactor = shapeConfig.sampling().xzFactor();
        this.yFactor = shapeConfig.sampling().yFactor();
        
        this.topSlide = shapeConfig.topSlide();
        this.bottomSlide = shapeConfig.bottomSlide();
        
        this.generateAquifers = generatorSettings.aquifers();

        this.baseNoiseCache = new ChunkCache<>(
            "base_noise",
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
        
        this.heightmapCache = new ChunkCache<>(
            "heightmap", 
            1536, 
            true, 
            this::sampleHeightmap
        );
        
        // Random deriver
        RandomDeriver randomDeriver = this.randomProvider.create(this.seed).createRandomDeriver();
        
        // Aquifer Sampler Provider
        this.aquiferSamplerProvider = new AquiferSamplerProvider(
            this.noiseRouter,
            randomDeriver,
            this.dummyNoiseChunkSampler,
            this.defaultFluid,
            this.seaLevel,
            this.worldMinY + 10,
            this.worldMinY,
            this.worldHeight,
            this.verticalNoiseResolution,
            this.generateAquifers
        );
        
        this.noisePostProcessor = NoisePostProcessor.DEFAULT;
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
    public CompletableFuture<Chunk> provideChunk(Executor executor, Blender blender, StructureAccessor structureAccessor, Chunk chunk) {
        GenerationShapeConfig shapeConfig = this.generatorSettings.value().generationShapeConfig();
        
        int minY = Math.max(shapeConfig.minimumY(), chunk.getBottomY());
        int topY = Math.min(shapeConfig.minimumY() + shapeConfig.height(), chunk.getTopY());
        
        @SuppressWarnings("unused")
        int noiseMinY = MathHelper.floorDiv(minY, this.verticalNoiseResolution);
        int noiseTopY = MathHelper.floorDiv(topY - minY, this.verticalNoiseResolution);
        
        if (noiseTopY <= 0) {
            return CompletableFuture.completedFuture(chunk);
        }
        
        int sectionTopY = chunk.getSectionIndex(noiseTopY * this.verticalNoiseResolution - 1 + minY);
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
     * @return The y-coordinate of top block at x/z.
     */
    @Override
    public int getHeight(int x, int z, Heightmap.Type type) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        
        return this.heightmapCache.get(chunkX, chunkZ).getHeight(x, z, type);
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
        
        return this.heightmapCache.get(chunkX, chunkZ).getHeight(x, z, type);
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
     * Samples density for noise post processor.
     * 
     * @param noise Base density.
     * @param noiseX x-coordinate in absolute noise coordinates.
     * @param noiseY y-coordinate in absolute noise coordinates.
     * @param noiseZ z-coordinate in absolute noise coordinates.
     * @return Modified noise density.
     */
    protected double sampleNoisePostProcessor(double noise, int noiseX, int noiseY, int noiseZ) {
        return this.noisePostProcessor.sample(noise, noiseX, noiseY, noiseZ);
    }
    
    /**
     * Check if default noise post processor (i.e. NONE) is being used.
     * 
     * @return Whether default noise post processor is being used.
     */
    protected boolean hasNoisePostProcessor() {
        return this.noisePostProcessor != NoisePostProcessor.DEFAULT;
    }
    
    /**
     * Interpolate density to set terrain curve at top and bottom of world.
     * 
     * @param density Base density.
     * @param noiseY y-coordinate in noise coordinates from [0, noiseSizeY]
     * 
     * @return Modified noise density.
     */
    protected double applySlides(double density, int noiseY) {
        density = this.topSlide.method_38414(density, this.noiseSizeY - noiseY);
        density = this.bottomSlide.method_38414(density, noiseY);
        
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
            chunk.markBlockForPostProcessing(pos);
        }
    }

    protected HeightmapChunk getHeightmapChunk(int chunkX, int chunkZ) {
        return this.heightmapCache.get(chunkX, chunkZ);
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
        
        // Create and populate noise providers
        List<NoiseProvider> noiseProviders = new ArrayList<>();
        
        NoiseProvider baseNoiseProvider = this.baseNoiseCache.get(chunkX, chunkZ);
        SimpleBlockSource baseBlockSource = this.getBaseBlockSource(
            baseNoiseProvider,
            structureWeightSampler,
            aquiferSampler,
            new SimpleNoisePos()
        );
        
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
                                
                                BlockState blockState = blockSources.apply(x, y, z);
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
     * and returns to {@link #getHeight(int, int, net.minecraft.world.Heightmap.Type)} 
     * to cache and return the height.
     * 
     * @param chunkX x-coordinate in chunk coordinates to sample all y-values for.
     * @param chunkZ z-coordinate in chunk coordinates to sample all y-values for.
     * 
     * @return A HeightmapChunk, containing an array of ints containing the heights for the entire chunk.
     */
    private HeightmapChunk sampleHeightmap(int chunkX, int chunkZ) {
        short minHeight = 16;
        short worldMinY = (short)this.worldMinY;
        short worldTopY = (short)this.worldTopY;
        
        BaseNoiseProvider baseNoiseProvider = this.baseNoiseCache.get(chunkX, chunkZ);
        
        short[] heightmapSurface = new short[256];
        short[] heightmapOcean = new short[256];
        short[] heightmapSurfaceFloor = new short[256];
        
        Arrays.fill(heightmapSurface, minHeight);
        Arrays.fill(heightmapOcean, minHeight);
        Arrays.fill(heightmapSurfaceFloor, worldMinY);
        
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
                                boolean isSolid = density > 0.0;
                                
                                short height = (short)(y + 1);
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
                                // First, set max world height as flag when hitting first solid layer
                                // then set the actual height value when hitting first non-solid layer.
                                // This handles situations where the bottom of the world may not be solid,
                                // i.e. Skylands-style world types.
                                if (isSolid && heightmapSurfaceFloor[ndx] == worldMinY) {
                                    heightmapSurfaceFloor[ndx] = worldTopY;
                                }
                                
                                if (!isSolid && heightmapSurfaceFloor[ndx] == worldTopY) {
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
    private SimpleBlockSource getBaseBlockSource(
        NoiseProvider baseNoiseProvider,
        StructureWeightSampler weightSampler,
        AquiferSampler aquiferSampler,
        SimpleNoisePos noisePos
    ) {
        return (x, y, z) -> {
            double density = baseNoiseProvider.sample();
            double clampedDensity = MathHelper.clamp(density * 0.64, -1.0, 1.0);
            
            clampedDensity = clampedDensity / 2.0 - clampedDensity * clampedDensity * clampedDensity / 24.0;
            clampedDensity += weightSampler.sample(noisePos.setBlockCoords(x, y, z));
            
            return aquiferSampler.apply(noisePos, clampedDensity);
        };
    }
    
    @Override
    public boolean skipChunk(int chunkX, int chunkZ, ChunkStatus chunkStatus) {
        if (chunkStatus == ChunkStatus.CARVERS)
            return false;
        
        return false;
    }
}

