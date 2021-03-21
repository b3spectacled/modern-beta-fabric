package com.bespectacled.modernbeta.gen.provider;

import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.compat.Compat;
import com.bespectacled.modernbeta.decorator.OldDecorators;
import com.bespectacled.modernbeta.gen.OldGeneratorSettings;
import com.bespectacled.modernbeta.mixin.MixinAquiferSamplerInvoker;
import com.bespectacled.modernbeta.mixin.MixinChunkGeneratorSettingsInvoker;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BlockStates;

import net.minecraft.block.Block;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.math.noise.NoiseSampler;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.AquiferSampler;
import net.minecraft.world.gen.BlockInterpolator;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.DeepslateInterpolator;
import net.minecraft.world.gen.NoiseCaveSampler;
import net.minecraft.world.gen.SimpleRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.StructureWeightSampler;
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
    
    protected static final Object2ObjectLinkedOpenHashMap<BlockPos, Integer> HEIGHTMAP_CACHE = new Object2ObjectLinkedOpenHashMap<>(512);
    protected static final int[][] HEIGHTMAP_CHUNK = new int[16][16];
    
    protected final Supplier<ChunkGeneratorSettings> generatorSettings;
    protected final CompoundTag providerSettings;
    
    protected final long seed;
    
    protected final int minY;
    protected final int worldHeight;
    protected final int seaLevel;
    
    protected final int bedrockFloor;
    protected final int bedrockCeiling;
    
    protected final int verticalNoiseResolution;   // Number of blocks in a vertical subchunk
    protected final int horizontalNoiseResolution; // Number of blocks in a horizontal subchunk 
    
    protected final int noiseSizeX; // Number of horizontal subchunks along x
    protected final int noiseSizeZ; // Number of horizontal subchunks along z
    protected final int noiseSizeY; // Number of vertical subchunks
    protected final int noiseMinY;  // Subchunk index of bottom of the world
    protected final int noisePosY;  // Number of positive (y >= 0) vertical subchunks

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

    protected final boolean generateDeepOceans;
    protected final boolean generateNoiseCaves;
    protected final boolean generateAquifers;
    protected final boolean generateDeepslate;
    
    protected final BlockState defaultBlock;
    protected final BlockState defaultFluid;
    
    protected final NoiseCaveSampler noiseCaveSampler;
    
    protected final DoublePerlinNoiseSampler edgeDensityNoise;
    protected final DoublePerlinNoiseSampler waterLevelNoise;
    
    protected final BlockInterpolator deepslateInterpolator;
    protected final NoiseSampler surfaceDepthNoise; // For vanilla surface builders
    
    public AbstractChunkProvider(long seed, OldGeneratorSettings settings) {
        this(
            seed,
            settings.generatorSettings.get().getGenerationShapeConfig().getMinimumY(),
            settings.generatorSettings.get().getGenerationShapeConfig().getHeight(),
            settings.generatorSettings.get().getSeaLevel(),
            settings.generatorSettings.get().getBedrockFloorY(),
            settings.generatorSettings.get().getBedrockCeilingY(),
            settings.generatorSettings.get().getGenerationShapeConfig().getSizeVertical(),
            settings.generatorSettings.get().getGenerationShapeConfig().getSizeHorizontal(),
            settings.generatorSettings.get().getGenerationShapeConfig().getSampling().getXZScale(),
            settings.generatorSettings.get().getGenerationShapeConfig().getSampling().getYScale(),
            settings.generatorSettings.get().getGenerationShapeConfig().getSampling().getXZFactor(),
            settings.generatorSettings.get().getGenerationShapeConfig().getSampling().getYFactor(),
            settings.generatorSettings.get().getGenerationShapeConfig().getTopSlide().getTarget(),
            settings.generatorSettings.get().getGenerationShapeConfig().getTopSlide().getSize(),
            settings.generatorSettings.get().getGenerationShapeConfig().getTopSlide().getOffset(),
            settings.generatorSettings.get().getGenerationShapeConfig().getBottomSlide().getTarget(),
            settings.generatorSettings.get().getGenerationShapeConfig().getBottomSlide().getSize(),
            settings.generatorSettings.get().getGenerationShapeConfig().getBottomSlide().getOffset(),
            ((MixinChunkGeneratorSettingsInvoker)(Object)settings.generatorSettings.get()).invokeHasNoiseCaves(),
            ((MixinChunkGeneratorSettingsInvoker)(Object)settings.generatorSettings.get()).invokeHasAquifers(),
            ((MixinChunkGeneratorSettingsInvoker)(Object)settings.generatorSettings.get()).invokeHasDeepslate(),
            settings.generatorSettings.get().getDefaultBlock(),
            settings.generatorSettings.get().getDefaultFluid(),
            settings
        );
    }
    
    public AbstractChunkProvider(
        long seed,
        int minY, 
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
        boolean generateNoiseCaves,
        boolean generateAquifers,
        boolean generateDeepslate,
        BlockState defaultBlock,
        BlockState defaultFluid,
        OldGeneratorSettings settings
    ) {
        this.generatorSettings = settings.generatorSettings;
        this.providerSettings = settings.providerSettings;
        
        this.seed = seed;
        
        this.minY = minY;
        this.worldHeight = worldHeight;
        this.seaLevel = seaLevel;
        
        this.bedrockFloor = bedrockFloor;
        this.bedrockCeiling = bedrockCeiling;
        
        this.verticalNoiseResolution = sizeVertical * 4;
        this.horizontalNoiseResolution = sizeHorizontal * 4;
        
        this.noiseSizeX = 16 / this.horizontalNoiseResolution;
        this.noiseSizeZ = 16 / this.horizontalNoiseResolution;
        this.noiseSizeY = this.worldHeight / this.verticalNoiseResolution;
        this.noiseMinY = this.minY / this.verticalNoiseResolution;
        this.noisePosY = (this.worldHeight + this.minY) / this.verticalNoiseResolution;
        
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
        
        this.generateDeepOceans = this.providerSettings.contains("generateDeepOceans") ? this.providerSettings.getBoolean("generateDeepOceans") : false;
        this.generateNoiseCaves = (this.providerSettings.contains("generateNoiseCaves") && this.providerSettings.getBoolean("generateNoiseCaves")) ? generateNoiseCaves : false;
        this.generateAquifers = (this.providerSettings.contains("generateAquifers") && this.providerSettings.getBoolean("generateAquifers")) ? generateAquifers : false;
        this.generateDeepslate = (this.providerSettings.contains("generateDeepslate") && this.providerSettings.getBoolean("generateDeepslate")) ? generateDeepslate : false;
        
        this.defaultBlock = defaultBlock;
        this.defaultFluid = defaultFluid;
        
        ChunkRandom chunkRandom = new ChunkRandom(seed);
        this.edgeDensityNoise = DoublePerlinNoiseSampler.create(new SimpleRandom(chunkRandom.nextLong()), -3, 1.0);
        this.waterLevelNoise = DoublePerlinNoiseSampler.create(new SimpleRandom(chunkRandom.nextLong()), -3, 1.0, 0.0, 2.0);
        
        this.noiseCaveSampler = this.generateNoiseCaves ? new NoiseCaveSampler(chunkRandom, this.noiseMinY) : null;
        this.deepslateInterpolator = new DeepslateInterpolator(seed, this.defaultBlock, this.generateDeepslate ? Blocks.DEEPSLATE.getDefaultState() : BlockStates.STONE);
        
        this.surfaceDepthNoise = this.generatorSettings.get().getGenerationShapeConfig().hasSimplexSurfaceNoise() ? 
            new OctaveSimplexNoiseSampler(new ChunkRandom(seed), IntStream.rangeClosed(-3, 0)) : 
            new OctavePerlinNoiseSampler(new ChunkRandom(seed), IntStream.rangeClosed(-3, 0));
        
        RAND.setSeed(seed);
        HEIGHTMAP_CACHE.clear();
    }
    
    public abstract Chunk provideChunk(StructureAccessor structureAccessor, Chunk chunk, OldBiomeSource biomeSource);
    public abstract void provideSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource);
    public abstract int getHeight(int x, int z, Heightmap.Type type);
    public abstract PerlinOctaveNoise getBeachNoiseOctaves();
    
    public boolean skipChunk(int chunkX, int chunkZ) {
        return false;
    }
    
    public int getWorldHeight() {
        return this.worldHeight;
    }
    
    public int getMinimumY() {
        return this.minY;
    }
    
    public int getSeaLevel() {
        return this.seaLevel;
    }
    
    public int getVerticalNoiseResolution() {
        return this.verticalNoiseResolution;
    }
    
    protected BlockState getBlockStateSky(StructureWeightSampler weightSampler, int x, int y, int z, double density) {
        double clampedDensity = MathHelper.clamp(density / 200.0, -1.0, 1.0);
        clampedDensity = clampedDensity / 2.0 - clampedDensity * clampedDensity * clampedDensity / 24.0;
        clampedDensity += weightSampler.getWeight(x, y, z);
        
        BlockState blockStateToSet = BlockStates.AIR;
        
        if (clampedDensity > 0.0) {
            blockStateToSet = this.defaultBlock;
        }
        
        return blockStateToSet;
    }
    
    protected BlockState getBlockState(StructureWeightSampler weightSampler, AquiferSampler aquiferSampler, int x, int y, int z, double density) {
        return this.getBlockState(weightSampler, aquiferSampler, x, y, z, density, 1.0D);
    }
    
    protected BlockState getBlockState(StructureWeightSampler weightSampler, AquiferSampler aquiferSampler, int x, int y, int z, double density, double temp) {
        double clampedDensity = MathHelper.clamp(density / 200.0, -1.0, 1.0);
        clampedDensity = clampedDensity / 2.0 - clampedDensity * clampedDensity * clampedDensity / 24.0;
        clampedDensity += weightSampler.getWeight(x, y, z);
        
        if (aquiferSampler != null) {
            ((MixinAquiferSamplerInvoker)aquiferSampler).invokeApply(x, y, z);
            clampedDensity += aquiferSampler.getDensityAddition();
        }
        
        BlockState blockStateToSet = BlockStates.AIR;
        
        if (clampedDensity > 0.0) {
            blockStateToSet = this.deepslateInterpolator.sample(x, y, z, this.generatorSettings.get());
        } else if (this.generateAquifers && y < this.minY + 9) { 
            blockStateToSet = BlockStates.LAVA;
        } else {
            int localSeaLevel = (aquiferSampler == null) ? this.getSeaLevel() : aquiferSampler.getWaterLevel();
            
            if (y < localSeaLevel) {
                blockStateToSet = this.defaultFluid;
                
                if (temp < 0.5D && y == this.getSeaLevel() - 1)
                    blockStateToSet = BlockStates.ICE;
            }
        }
        
        return blockStateToSet;
    }
    
    protected BlockState getBlockState(int x, int y, int z, Block blockToSet) {
        BlockState blockState = BlockStates.getBlockState(blockToSet);
        
        if (blockToSet == this.defaultBlock.getBlock() && y <= 0) {
            return this.deepslateInterpolator.sample(x, y, z, this.generatorSettings.get());
        }
        
        return blockState;
    }
    
    protected double sampleNoiseCave(int x, int y, int z, double noise) {
        if (this.noiseCaveSampler != null) {
            return this.noiseCaveSampler.sample(x, y, z, noise);
        }
        
        return noise;
    }
    
    protected AquiferSampler createAquiferSampler(int chunkX, int chunkZ) {
        return this.generateAquifers ? 
            new AquiferSampler(
                chunkX,
                chunkZ, 
                this.edgeDensityNoise, 
                this.waterLevelNoise, 
                this.generatorSettings.get(), 
                null, // NoiseColumnSampler, unused?
                this.noiseSizeY * this.verticalNoiseResolution
            ) : 
            null;     
    }
    
    protected void scheduleFluidTick(Chunk chunk, AquiferSampler aquiferSampler, BlockPos pos, BlockState blockState) {
        if (aquiferSampler != null && aquiferSampler.needsFluidTick() && !blockState.getFluidState().isEmpty()) {
            chunk.getFluidTickScheduler().schedule(pos, blockState.getFluidState().getFluid(), 0);
        }
    }
    
    protected double applyTopSlide(double density, int noiseY, int initialOffset) {
        int topSlideStart = (this.noiseSizeY + this.noiseMinY + 1) - initialOffset - this.topSlideOffset;
        if (noiseY > topSlideStart) {
            double topSlideDelta = (float) (noiseY - topSlideStart) / (float) this.topSlideSize;
            density = density * (1.0D - topSlideDelta) + this.topSlideTarget * topSlideDelta;
        }
        
        return density;
    }
    
    protected double applyBottomSlide(double density, int noiseY, int initialOffset) {
        int bottomSlideStart = this.noiseMinY - initialOffset - this.bottomSlideOffset;
        if (noiseY < bottomSlideStart) {
            double bottomSlideDelta = (float) (bottomSlideStart - noiseY) / ((float) this.bottomSlideSize);
            density = density * (1.0D - bottomSlideDelta) + this.bottomSlideTarget * bottomSlideDelta;
        }
        
        return density;
    }
    
    protected ChunkRandom createChunkRand(int chunkX, int chunkZ) {
        ChunkRandom chunkRand = new ChunkRandom();
        chunkRand.setTerrainSeed(chunkX, chunkZ);
        
        return chunkRand;
    }
    
    protected boolean useCustomSurfaceBuilder(Biome biome, Identifier biomeId, ChunkRegion region, Chunk chunk, ChunkRandom random, BlockPos.Mutable mutable) {
        int x = mutable.getX();
        int y = mutable.getY();
        int z = mutable.getZ();
        
        if (Compat.BIOMES_WITH_CUSTOM_SURFACES.contains(biomeId)) {
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
    
    public static Biome getBiomeForSurfaceGen(BlockPos pos, ChunkRegion region, OldBiomeSource biomeSource) {
        if (biomeSource.isBeta()) {
            return biomeSource.getBiomeForSurfaceGen(pos.getX(), 0, pos.getZ());
        }
        
        return region.getBiome(pos);
    }
    
    public static void setForestOctaves(PerlinOctaveNoise forestOctaves) {
        OldDecorators.COUNT_BETA_NOISE_DECORATOR.setOctaves(forestOctaves);
        OldDecorators.COUNT_ALPHA_NOISE_DECORATOR.setOctaves(forestOctaves);
        OldDecorators.COUNT_INFDEV_NOISE_DECORATOR.setOctaves(forestOctaves);
    }
}
