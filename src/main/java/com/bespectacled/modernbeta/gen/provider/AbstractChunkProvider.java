package com.bespectacled.modernbeta.gen.provider;

import java.util.Random;

import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.decorator.OldDecorators;
import com.bespectacled.modernbeta.gen.OldGeneratorSettings;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BlockStates;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.StructureWeightSampler;

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
    protected static final Mutable POS = new Mutable();
    protected static final Random RAND = new Random();
    
    protected static final Object2ObjectLinkedOpenHashMap<BlockPos, Integer> HEIGHTMAP_CACHE = new Object2ObjectLinkedOpenHashMap<>(512);
    protected static final int[][] HEIGHTMAP_CHUNK = new int[16][16];
    
    protected final CompoundTag providerSettings;
    
    protected final int minY;
    protected final int worldHeight;
    protected final int seaLevel;
    
    protected final int bedrockFloor;
    protected final int bedrockCeiling;
    
    protected final int verticalNoiseResolution;
    protected final int horizontalNoiseResolution;
    
    protected final int noiseSizeX;
    protected final int noiseSizeZ;
    protected final int noiseSizeY;
    protected final int noiseMinY;

    protected final double xzScale;
    protected final double yScale;
    
    protected final double xzFactor;
    protected final double yFactor;
    
    protected final BlockState defaultBlock;
    protected final BlockState defaultFluid;
    
    public AbstractChunkProvider(long seed, OldGeneratorSettings settings) {
        this(
            seed,
            settings.generatorSettings.getGenerationShapeConfig().getMinimumY(),
            settings.generatorSettings.getGenerationShapeConfig().getHeight(),
            settings.generatorSettings.getSeaLevel(),
            settings.generatorSettings.getBedrockFloorY(),
            settings.generatorSettings.getBedrockCeilingY(),
            settings.generatorSettings.getGenerationShapeConfig().getSizeVertical(),
            settings.generatorSettings.getGenerationShapeConfig().getSizeHorizontal(),
            settings.generatorSettings.getGenerationShapeConfig().getSampling().getXZScale(),
            settings.generatorSettings.getGenerationShapeConfig().getSampling().getYScale(),
            settings.generatorSettings.getGenerationShapeConfig().getSampling().getXZFactor(),
            settings.generatorSettings.getGenerationShapeConfig().getSampling().getYFactor(),
            settings.generatorSettings.getDefaultBlock(),
            settings.generatorSettings.getDefaultFluid(),
            settings.providerSettings
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
        BlockState defaultBlock,
        BlockState defaultFluid,
        CompoundTag providerSettings
    ) {
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
        
        this.xzScale = xzScale;
        this.yScale = yScale;
        
        this.xzFactor = xzFactor;
        this.yFactor = yFactor;
        
        this.defaultBlock = defaultBlock;
        this.defaultFluid = defaultFluid;
        
        this.providerSettings = providerSettings;
        
        RAND.setSeed(seed);
        HEIGHTMAP_CACHE.clear();
    }
    
    public abstract void provideChunk(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk, OldBiomeSource biomeSource);
    public abstract void provideSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource);
    public abstract int getHeight(int x, int z, Heightmap.Type type);
    public abstract PerlinOctaveNoise getBeachNoiseOctaves();
    
    public BlockState getBlockState(double density, int y) {
        return this.getBlockState(density, y, 1.0);
    }
    
    public BlockState getBlockState(double density, int y, double temp) {
        BlockState blockStateToSet = BlockStates.AIR;
        
        if (density > 0.0) {
            blockStateToSet = this.defaultBlock;
        } else if (y < this.seaLevel) {
            if (temp < 0.5D && y >= this.seaLevel - 1) {
                // Does not work well with underwater structures
                //blockStateToSet = BlockStates.ICE;
                blockStateToSet = this.defaultFluid;
            } else {
                blockStateToSet = this.defaultFluid;
            }

        }
        return blockStateToSet;
    }
    
    protected BlockState getBlockState(StructureWeightSampler weightSampler, int x, int y, int z, double density) {
        double clampedDensity = MathHelper.clamp(density / 200.0, -1.0, 1.0);
        clampedDensity = clampedDensity / 2.0 - clampedDensity * clampedDensity * clampedDensity / 24.0;
        
        clampedDensity += weightSampler.getWeight(x, y, z);
        
        BlockState blockStateToSet = BlockStates.AIR;
        
        if (clampedDensity > 0.0) {
            blockStateToSet = this.defaultBlock;
        } else if (y < this.getSeaLevel()) {
            blockStateToSet = this.defaultFluid;
        }
        
        return blockStateToSet;
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
    
    public int getWorldHeight() {
        return this.worldHeight;
    }
    
    public int getSeaLevel() {
        return this.seaLevel;
    }
    
    protected ChunkRandom createChunkRand(int chunkX, int chunkZ) {
        ChunkRandom chunkRand = new ChunkRandom();
        chunkRand.setTerrainSeed(chunkX, chunkZ);
        
        return chunkRand;
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
