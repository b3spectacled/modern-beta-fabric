package com.bespectacled.modernbeta.api.world.gen;

import java.util.function.Supplier;
import java.util.stream.IntStream;

import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.DoubleArrayPool;
import com.bespectacled.modernbeta.util.IntArrayPool;
import com.bespectacled.modernbeta.world.gen.StructureWeightSampler;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public abstract class NoiseChunkProvider extends ChunkProvider {
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
    
    protected final Object2ObjectLinkedOpenHashMap<BlockPos, Integer> heightmapCache;
    protected final IntArrayPool heightmapPool;
    
    protected final DoubleArrayPool heightNoisePool;
    protected final DoubleArrayPool surfaceNoisePool;
    
    
    public NoiseChunkProvider(
        long seed, 
        ChunkGenerator chunkGenerator, 
        Supplier<ChunkGeneratorSettings> generatorSettings, 
        CompoundTag providerSettings
    ) {
        this(
            seed, 
            chunkGenerator, 
            generatorSettings, 
            providerSettings,
            generatorSettings.get().getGenerationShapeConfig().getHeight(),
            generatorSettings.get().getSeaLevel(),
            generatorSettings.get().getBedrockFloorY(),
            generatorSettings.get().getBedrockCeilingY(),
            generatorSettings.get().getDefaultBlock(),
            generatorSettings.get().getDefaultBlock(),
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
            generatorSettings.get().getGenerationShapeConfig().getBottomSlide().getOffset()
        );
    }

    public NoiseChunkProvider(
        long seed,
        ChunkGenerator chunkGenerator, 
        Supplier<ChunkGeneratorSettings> generatorSettings,
        CompoundTag providerSettings,
        int worldHeight, 
        int seaLevel,
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
        super(seed, chunkGenerator, generatorSettings, providerSettings, worldHeight, seaLevel, bedrockFloor, bedrockCeiling, defaultBlock, defaultFluid);
        
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
        
        // Heightmap cache
        this.heightmapCache = new Object2ObjectLinkedOpenHashMap<>(512);
        this.heightmapPool = new IntArrayPool(64, 16 * 16);
        
        // Noise array pools
        this.heightNoisePool = new DoubleArrayPool(64, (this.noiseSizeX + 1) * (this.noiseSizeZ + 1) * (this.noiseSizeY + 1));
        this.surfaceNoisePool = new DoubleArrayPool(64, 16 * 16);
        
    }
    
    protected abstract void generateHeightNoiseArr(int noiseX, int noiseZ, double[] heightNoise);
    
    /**
     * 
     * @return Number of blocks in a vertical subchunk.
     */
    public int getVerticalNoiseResolution() {
        return this.verticalNoiseResolution;
    }
    
    @Override
    protected void generateTerrain(Chunk chunk, StructureAccessor structureAccessor) {
        int noiseResolutionY = this.noiseSizeY + 1;
        int noiseResolutionXZ = this.noiseSizeX + 1;
        
        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;
        
        Heightmap heightmapOCEAN = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSURFACE = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
        
        StructureWeightSampler structureWeightSampler = new StructureWeightSampler(structureAccessor, chunk);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        // Get and populate primary noise array
        double[] heightNoise = this.heightNoisePool.borrowArr();
        this.generateHeightNoiseArr(chunkX * this.noiseSizeX, chunkZ * this.noiseSizeZ, heightNoise);
        
        for (int subChunkX = 0; subChunkX < this.noiseSizeX; ++subChunkX) {
            for (int subChunkZ = 0; subChunkZ < this.noiseSizeZ; ++ subChunkZ) {
                for (int subChunkY = 0; subChunkY < this.noiseSizeY; ++subChunkY) {
                    double lowerNW = heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 0)];
                    double lowerSW = heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 0)];
                    double lowerNE = heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 0)];
                    double lowerSE = heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 0)];
                    
                    double upperNW = heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 1)]; 
                    double upperSW = heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 1)];
                    double upperNE = heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 1)];
                    double upperSE = heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 1)];
                    
                    for (int subY = 0; subY < this.verticalNoiseResolution; ++subY) {
                        int y = subChunkY * this.verticalNoiseResolution + subY;
                        
                        double deltaY = subY / (double)this.verticalNoiseResolution;
                        
                        double nw = MathHelper.lerp(deltaY, lowerNW, upperNW);
                        double sw = MathHelper.lerp(deltaY, lowerSW, upperSW);
                        double ne = MathHelper.lerp(deltaY, lowerNE, upperNE);
                        double se = MathHelper.lerp(deltaY, lowerSE, upperSE);
                        
                        for (int subX = 0; subX < this.horizontalNoiseResolution; ++subX) {
                            int x = subX + subChunkX * this.horizontalNoiseResolution;
                            int absX = (chunk.getPos().x << 4) + x;
                            
                            double deltaX = subX / (double)this.horizontalNoiseResolution;
                            
                            double n = MathHelper.lerp(deltaX, nw, ne);
                            double s = MathHelper.lerp(deltaX, sw, se);
                            
                            for (int subZ = 0; subZ < this.horizontalNoiseResolution; ++subZ) {
                                int z = subZ + subChunkZ * this.horizontalNoiseResolution;
                                int absZ = (chunk.getPos().z << 4) + z;
                                
                                double deltaZ = subZ / (double)this.horizontalNoiseResolution;
                                
                                double density = MathHelper.lerp(deltaZ, n, s);
                                
                                BlockState blockToSet = this.getBlockState(structureWeightSampler, absX, y, absZ, density);
                                chunk.setBlockState(mutable.set(x, y, z), blockToSet, false);
                                
                                heightmapOCEAN.trackUpdate(x, y, z, blockToSet);
                                heightmapSURFACE.trackUpdate(x, y, z, blockToSet);
                            }
                        }
                    }
                }
            }
        }
        
        this.heightNoisePool.returnArr(heightNoise);
    }
    
    @Override
    protected int sampleHeightmap(int sampleX, int sampleZ) {
        int noiseResolutionY = this.noiseSizeY + 1;
        int noiseResolutionXZ = this.noiseSizeX + 1;

        int chunkX = sampleX >> 4;
        int chunkZ = sampleZ >> 4;
        
        int startX = chunkX << 4;
        int startZ = chunkZ << 4;

        double[] heightNoise = this.heightNoisePool.borrowArr();
        this.generateHeightNoiseArr(chunkX * this.noiseSizeX, chunkZ * this.noiseSizeZ, heightNoise);
        
        int[] heightmap = this.heightmapPool.borrowArr(); 
        IntStream.range(0, heightmap.length).forEach(i -> heightmap[i] = 16);

        for (int subChunkX = 0; subChunkX < this.noiseSizeX; subChunkX++) {
            for (int subChunkZ = 0; subChunkZ < this.noiseSizeZ; subChunkZ++) {
                for (int subChunkY = 0; subChunkY < this.noiseSizeY; subChunkY++) {
                    double lowerNW = heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 0)];
                    double lowerSW = heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 0)];
                    double lowerNE = heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 0)];
                    double lowerSE = heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 0)];
                    
                    double upperNW = heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 1)]; 
                    double upperSW = heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 1)];
                    double upperNE = heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 1)];
                    double upperSE = heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 1)];
                    
                    for (int subY = 0; subY < this.verticalNoiseResolution; ++subY) {
                        int y = subChunkY * this.verticalNoiseResolution + subY;
                        
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
                                    heightmap[z + x * 16] = y;
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                heightmapCache.put(new BlockPos(startX + x, 0, startZ + z), heightmap[z + x * 16] + 1);
            }
        }
        
        this.heightmapPool.returnArr(heightmap);
        this.heightNoisePool.returnArr(heightNoise);
        return heightmapCache.get(new BlockPos(sampleX, 0, sampleZ));
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
    protected BlockState getBlockState(StructureWeightSampler weightSampler, int x, int y, int z, double density) {
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
}
