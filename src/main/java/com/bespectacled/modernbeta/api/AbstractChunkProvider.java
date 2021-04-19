package com.bespectacled.modernbeta.api;

import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import com.bespectacled.modernbeta.compat.CompatBiomes;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.IntArrayPool;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.decorator.OldDecorators;
import com.bespectacled.modernbeta.world.gen.StructureWeightSampler;

import net.minecraft.block.Block;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.noise.NoiseSampler;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

/*
 * Some vanilla settings, for reference:
 * 
 * sizeVertical = 2
 * sizeHorizontal = 1
 * height = 128 (or 256 in vanilla)
 * 
 * verticalNoiseResolution = sizeVertical * 4 (8)
 * horizontalNoiseResolution = sizeHorizontal * 4 (4)
 * 
 * noiseSizeX = 16 / horizontalNoiseResolution (4)
 * noiseSizeZ = 16 / horizontalNoiseResolution (4)
 * noiseSizeY = height / verticalNoiseResolution (16)
 * 
 */
public abstract class AbstractChunkProvider {
    protected static final Random RAND = new Random();
    
    protected final Object2ObjectLinkedOpenHashMap<BlockPos, Integer> heightmapCache;
    protected final IntArrayPool heightmapPool;
    
    protected final Supplier<ChunkGeneratorSettings> generatorSettings;
    protected final CompoundTag providerSettings;
    
    protected final long seed;
    
    protected final int worldHeight;
    protected final int seaLevel;
    
    protected final int bedrockFloor;
    protected final int bedrockCeiling;
    
    protected final int verticalNoiseResolution;   // Number of blocks in a vertical subchunk
    protected final int horizontalNoiseResolution; // Number of blocks in a horizontal subchunk 
    
    protected final int noiseSizeX; // Number of horizontal subchunks along x
    protected final int noiseSizeZ; // Number of horizontal subchunks along z
    protected final int noiseSizeY; // Number of vertical subchunks

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
    
    protected final BlockState defaultBlock;
    protected final BlockState defaultFluid;
    
    protected final NoiseSampler surfaceDepthNoise; // For vanilla surface builders
    
    /**
     * Construct a Modern Beta chunk provider with seed and settings.
     * 
     * @param seed Seed to initialize terrain generators. 
     * @param generatorSettings Vanilla settings used to control various terrain and noise settings.
     * @param providerSettings CompoundTag for additional settings not part of vanilla generator settings.
     */
    public AbstractChunkProvider(long seed, AbstractBiomeProvider biomeProvider, Supplier<ChunkGeneratorSettings> generatorSettings, CompoundTag providerSettings) {
        this(
            seed,
            generatorSettings.get().getGenerationShapeConfig().getHeight(),
            generatorSettings.get().getSeaLevel(),
            generatorSettings.get().getBedrockFloorY(),
            generatorSettings.get().getBedrockCeilingY(),
            generatorSettings.get().getGenerationShapeConfig().getSizeVertical(),
            generatorSettings.get().getGenerationShapeConfig().getSizeHorizontal(),
            generatorSettings.get().getGenerationShapeConfig().getSampling().getXZScale(),
            generatorSettings.get().getGenerationShapeConfig().getSampling().getYScale(),
            generatorSettings.get().getGenerationShapeConfig().getSampling().getXZFactor(),
            generatorSettings.get().getGenerationShapeConfig().getSampling().getYFactor(),
            generatorSettings.get().getGenerationShapeConfig().getTopSlide().getTarget(),
            generatorSettings.get().getGenerationShapeConfig().getTopSlide().getSize(),
            generatorSettings.get().getGenerationShapeConfig().getTopSlide().getOffset(),
            generatorSettings.get().getGenerationShapeConfig().getBottomSlide().getTarget(),
            generatorSettings.get().getGenerationShapeConfig().getBottomSlide().getSize(),
            generatorSettings.get().getGenerationShapeConfig().getBottomSlide().getOffset(),
            generatorSettings.get().getDefaultBlock(),
            generatorSettings.get().getDefaultFluid(),
            biomeProvider,
            generatorSettings,
            providerSettings
        );
    }
    
    /**
     * Construct a Modern Beta chunk provider with seed and settings.
     * 
     * @param seed Seed to initialize terrain generators.
     * @param minY Minimum Y coordinate.
     * @param worldHeight Total world height including minimum y coordinate.
     * @param seaLevel World sea level.
     * @param minSurfaceY Minimum Y coordinate for surface generation.
     * @param bedrockFloor Y offset of the bedrock floor above the bottom of the world.
     * @param bedrockCeiling Y offset of the bedrock roof below the top of the world.
     * @param sizeVertical Number of blocks in a vertical subchunk.
     * @param sizeHorizontal Number of blocks in a horizontal subchunk.
     * @param xzScale x/z scale of density noise (coordinate scale)
     * @param yScale y scale of density noise (height scale)
     * @param xzFactor x/z factor of density noise
     * @param yFactor y factor of density noise
     * @param topSlideTarget Density target for top slide.
     * @param topSlideSize Determines delta for top slide interpolation.
     * @param topSlideOffset Noise coordinate offset for top slide, positive values move the area down and negative values bring it up. 
     * @param bottomSlideTarget Density target for bottom slide.
     * @param bottomSlideSize Determines delta for bottom slide interpolation.
     * @param bottomSlideOffset Noise coordinate offset for bottom slide, positive values move the area up and negative values bring it down.
     * @param generateNoiseCaves Enables noise cave generation.
     * @param generateAquifers Enables aquifer generation.
     * @param generateDeepslate Enables deepslate generation.
     * @param defaultBlock Default block used for base terrain generation.
     * @param defaultFluid Default fluid used for base terrain generation.
     * @param generatorSettings Vanilla settings used to control various terrain and noise settings.
     * @param providerSettings CompoundTag for additional settings not part of vanilla generator settings.
     * 
     */
    public AbstractChunkProvider(
        long seed,
        int worldHeight, 
        int seaLevel,
        int bedrockFloor,
        int bedrockCeiling,
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
        BlockState defaultBlock,
        BlockState defaultFluid,
        AbstractBiomeProvider biomeProvider,
        Supplier<ChunkGeneratorSettings> generatorSettings,
        CompoundTag providerSettings
    ) {
        this.generatorSettings = generatorSettings;
        this.providerSettings = providerSettings;
        
        this.seed = seed;
        
        this.worldHeight = worldHeight;
        this.seaLevel = seaLevel;
        
        this.bedrockFloor = bedrockFloor;
        this.bedrockCeiling = bedrockCeiling;
        
        this.verticalNoiseResolution = sizeVertical * 4;
        this.horizontalNoiseResolution = sizeHorizontal * 4;
        
        this.noiseSizeX = 16 / this.horizontalNoiseResolution;
        this.noiseSizeZ = 16 / this.horizontalNoiseResolution;
        this.noiseSizeY = this.worldHeight / this.verticalNoiseResolution;
        
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
        
        this.defaultBlock = defaultBlock;
        this.defaultFluid = defaultFluid;
        
        this.surfaceDepthNoise = this.generatorSettings.get().getGenerationShapeConfig().hasSimplexSurfaceNoise() ? 
            new OctaveSimplexNoiseSampler(new ChunkRandom(seed), IntStream.rangeClosed(-3, 0)) : 
            new OctavePerlinNoiseSampler(new ChunkRandom(seed), IntStream.rangeClosed(-3, 0));
        
        this.heightmapCache = new Object2ObjectLinkedOpenHashMap<>(512);
        this.heightmapPool = new IntArrayPool(64, 16 * 16);
        RAND.setSeed(seed);
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
    public abstract void provideChunk(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk);
    
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
     * Get the PerlinOctaveNoise object used for beach surface generation for determining beach spawn location.
     * 
     * @return PerlinOctaveNoise object used for beach surface generation.
     */
    public abstract PerlinOctaveNoise getBeachNoise();
    
    /**
     * Determines whether to skip the chunk for some chunk generation step, depending on the x/z chunk coordinates.
     * 
     * @param chunkX x-coordinate in chunk coordinates.
     * @param chunkZ z-coordinate in chunk coordinates.
     * @param chunkStatus Chunk generation step used to determine skip context.
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
        return this.worldHeight;
    }
    
    /**
     * 
     * @return World sea level.
     */
    public int getSeaLevel() {
        return this.seaLevel;
    }
    
    /**
     * 
     * @return Number of blocks in a vertical subchunk.
     */
    public int getVerticalNoiseResolution() {
        return this.verticalNoiseResolution;
    }
    
    /**
     * Gets blockstate at block coordinates given noise density.
     * 
     * @param x x-coordinate in absolute block coordinates.
     * @param y y-coordinate in absolute block coordinates.
     * @param z z-coordinate in absolute block coordinates.
     * @param density Calculated noise density.
     * 
     * @return A blockstate, usually air, stone, or water.
     */
    protected BlockState getBlockState(StructureWeightSampler weightSampler, int x, int y, int z, double density) {
        return this.getBlockState(weightSampler, x, y, z, density, 1.0D);
    }
    
    /**
     * Gets blockstate at block coordinates given noise density.
     * 
     * @param x x-coordinate in absolute block coordinates.
     * @param y y-coordinate in absolute block coordinates.
     * @param z z-coordinate in absolute block coordinates.
     * @param density Calculated noise density.
     * @param temp Biome temperature, used for replacing water with ice when cold enough.
     * 
     * @return A blockstate, usually air, stone, or water.
     */
    protected BlockState getBlockState(StructureWeightSampler weightSampler, int x, int y, int z, double density, double temp) {
        double clampedDensity = weightSampler.sample(x, y, z, density);
        
        BlockState blockStateToSet = BlockStates.AIR;
        if (clampedDensity > 0.0) {
            blockStateToSet = this.defaultBlock;
        } else if (y < this.getSeaLevel()) {
            blockStateToSet = this.defaultFluid;
        }
        
        return blockStateToSet;
    }
    
    /**
     * Gets blockstate at block coordinates given block and default fluid block.
     * Simulates a noise density for the purpose of masking terrain around structures.
     * Used for 2D noise terrain generators (i.e. Infdev 20100227 and Indev terrain generators).
     * 
     * @param x x-coordinate in absolute block coordinates.
     * @param y y-coordinate in absolute block coordinates.
     * @param z z-coordinate in absolute block coordinates.
     * @param blockToSet Block to get blockstate for.
     * @oaran defaultFluid Default fluid block.
     * 
     * @return A blockstate.
     */
    protected BlockState getBlockState(StructureWeightSampler weightSampler, int x, int y, int z, Block blockToSet, Block defaultFluid) {
        boolean isFluid = blockToSet == Blocks.AIR || blockToSet == defaultFluid;
        double simDensity = isFluid ? -50D : 50D;
        
        double clampedDensity = weightSampler.sample(x, y, z, simDensity);
        
        BlockState blockState = blockToSet.getDefaultState();
        if (clampedDensity > 0.0 && isFluid) {
            blockState = BlockStates.STONE;
        } else if (clampedDensity < 0.0 && !isFluid) {
            blockState = BlockStates.AIR;
        }

        return blockState;
    }
    
    /**
     * Modifies density to set terrain curve at top of the world.
     * 
     * @param density Base density.
     * @param noiseY y-coordinate in noise coordinates.
     * @param initialOffset Initial noise y-coordinate offset. Generator settings offset is subtracted from this.
     * 
     * @return Modified noise density.
     */
    protected double applyTopSlide(double density, int noiseY, int initialOffset) {
        int topSlideStart = (this.noiseSizeY + 1) - initialOffset - this.topSlideOffset;
        if (noiseY > topSlideStart) {
            double topSlideDelta = (float) (noiseY - topSlideStart) / (float) this.topSlideSize;
            density = density * (1.0D - topSlideDelta) + this.topSlideTarget * topSlideDelta;
        }
        
        return density;
    }
    
    /**
     * Modifies density to set terrain curve at bottom of the world.
     * 
     * @param density Base density.
     * @param noiseY y-coordinate in noise coordinates.
     * @param initialOffset Initial noise y-coordinate offset. Generator settings offset is subtracted from this.
     * 
     * @return Modified noise density.
     */
    protected double applyBottomSlide(double density, int noiseY, int initialOffset) {
        int bottomSlideStart = 0 - initialOffset - this.bottomSlideOffset;
        if (noiseY < bottomSlideStart) {
            double bottomSlideDelta = (float) (bottomSlideStart - noiseY) / ((float) this.bottomSlideSize);
            density = density * (1.0D - bottomSlideDelta) + this.bottomSlideTarget * bottomSlideDelta;
        }
        
        return density;
    }
    
    /**
     * Get a new ChunkRandom object initialized with chunk coordinates for seed, for surface generation.
     * 
     * @param chunkX x-coordinate in chunk coordinates.
     * @param chunkZ z-coordinate in chunk coordinates.
     * 
     * @return New ChunkRandom object initialized with chunk coordinates for seed.
     */
    protected ChunkRandom createChunkRand(int chunkX, int chunkZ) {
        ChunkRandom chunkRand = new ChunkRandom();
        chunkRand.setTerrainSeed(chunkX, chunkZ);
        
        return chunkRand;
    }
    
    /**
     * Use a biome-specific surface builder, at a given x/z-coordinate and topmost y-coordinate.
     * Valid biomes are checked on per-biome basis using identifier from BIOMES_WITH_CUSTOM_SURFACES set. 
     * 
     * @param biome Biome with surface builder to use.
     * @param biomeId Biome identifier, used to check if it uses valid custom surface builder.
     * @param region
     * @param chunk
     * @param random
     * @param mutable Mutable BlockPos at block coordinates position.
     * 
     * @return True if biome is included in valid biomes set and has run surface builder. False if not included and not run.
     */
    protected boolean useCustomSurfaceBuilder(Biome biome, Identifier biomeId, ChunkRegion region, Chunk chunk, ChunkRandom random, BlockPos.Mutable mutable) {
        int x = mutable.getX();
        int y = mutable.getY();
        int z = mutable.getZ();
        
        if (CompatBiomes.hasCustomSurface(biomeId)) {
            double surfaceNoise = this.surfaceDepthNoise.sample(x * 0.0625, z * 0.0625, 0.0625, (x & 0xF) * 0.0625) * 15.0;
            biome.buildSurface(
                random, 
                chunk, 
                x, z, y, 
                surfaceNoise, 
                this.defaultBlock, 
                this.defaultFluid,
                this.seaLevel,
                region.getSeed()
            );
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Sets forest density using PerlinOctaveNoise object created using world seed.
     * 
     * @param forestOctaves PerlinOctaveNoise object used to set forest octaves.
     */
    protected void setForestOctaves(PerlinOctaveNoise forestOctaves) {
        OldDecorators.COUNT_BETA_NOISE_DECORATOR.setOctaves(forestOctaves);
        OldDecorators.COUNT_ALPHA_NOISE_DECORATOR.setOctaves(forestOctaves);
        OldDecorators.COUNT_INFDEV_NOISE_DECORATOR.setOctaves(forestOctaves);
    }
    
    protected void testHeightmap(int sampleX, int sampleZ) {
        int height = this.getHeight(sampleX, sampleZ, Heightmap.Type.OCEAN_FLOOR);
        System.out.println("Height at " + sampleX + "/" + sampleZ + ": " + height);
    }
}
