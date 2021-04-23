package com.bespectacled.modernbeta.api;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import com.bespectacled.modernbeta.mixin.MixinChunkGeneratorSettingsInvoker;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.DoubleArrayPool;
import com.bespectacled.modernbeta.util.IntArrayPool;
import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.class_6350;
import net.minecraft.class_6353;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.BlockSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.DeepslateBlockSource;
import net.minecraft.world.gen.NoiseCaveSampler;
import net.minecraft.world.gen.NoiseInterpolator;
import net.minecraft.world.gen.SimpleRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.StructureWeightSampler;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator.class_6352;

public abstract class NoiseChunkProvider extends ChunkProvider {
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

    protected final boolean generateNoiseCaves;
    protected final boolean generateAquifers;
    protected final boolean generateDeepslate;
    protected final boolean generateOreVeins;
    
    protected final Object2ObjectLinkedOpenHashMap<BlockPos, Integer> heightmapCache;
    protected final IntArrayPool heightmapPool;
    
    protected final DoubleArrayPool heightNoisePool;
    protected final DoubleArrayPool surfaceNoisePool;
    
    protected final DoublePerlinNoiseSampler edgeDensityNoise;
    protected final DoublePerlinNoiseSampler waterLevelNoise;
    protected final DoublePerlinNoiseSampler lavaNoise;

    protected final NoiseCaveSampler noiseCaveSampler;
    protected final class_6353 oreVeinSampler;
    
    
    public NoiseChunkProvider(
        long seed, 
        ChunkGenerator chunkGenerator, 
        Supplier<ChunkGeneratorSettings> generatorSettings, 
        NbtCompound providerSettings
    ) {
        this(
            seed, 
            chunkGenerator, 
            generatorSettings, 
            providerSettings,
            generatorSettings.get().getGenerationShapeConfig().getMinimumY(),
            generatorSettings.get().getGenerationShapeConfig().getHeight(),
            generatorSettings.get().getSeaLevel(),
            generatorSettings.get().getMinSurfaceLevel(),
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
            generatorSettings.get().getGenerationShapeConfig().getBottomSlide().getOffset(),
            ((MixinChunkGeneratorSettingsInvoker)(Object)generatorSettings.get()).invokeHasNoiseCaves(),
            ((MixinChunkGeneratorSettingsInvoker)(Object)generatorSettings.get()).invokeHasAquifers(),
            ((MixinChunkGeneratorSettingsInvoker)(Object)generatorSettings.get()).invokeHasDeepslate(),
            ((MixinChunkGeneratorSettingsInvoker)(Object)generatorSettings.get()).invokeHasOreVeins()
        );
    }

    public NoiseChunkProvider(
        long seed,
        ChunkGenerator chunkGenerator, 
        Supplier<ChunkGeneratorSettings> generatorSettings,
        NbtCompound providerSettings,
        int minY, 
        int worldHeight, 
        int seaLevel,
        int minSurfaceY,
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
        int bottomSlideOffset,
        boolean generateNoiseCaves,
        boolean generateAquifers,
        boolean generateDeepslate,
        boolean generateOreVeins
    ) {
        super(seed, chunkGenerator, generatorSettings, providerSettings, minY, worldHeight, seaLevel, minSurfaceY, bedrockFloor, bedrockCeiling, defaultBlock, defaultFluid);
        
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
        
        this.generateNoiseCaves = generateNoiseCaves;
        this.generateAquifers = generateAquifers;
        this.generateDeepslate = generateDeepslate;
        this.generateOreVeins = generateOreVeins;
        
        this.blockSource = new DeepslateBlockSource(seed, this.defaultBlock, this.generateDeepslate ? Blocks.DEEPSLATE.getDefaultState() : BlockStates.STONE, this.generatorSettings.get());
        
        // Heightmap cache
        this.heightmapCache = new Object2ObjectLinkedOpenHashMap<>(512);
        this.heightmapPool = new IntArrayPool(64, 16 * 16);
        
        // Noise array pools
        this.heightNoisePool = new DoubleArrayPool(64, (this.noiseSizeX + 1) * (this.noiseSizeZ + 1) * (this.noiseSizeY + 1));
        this.surfaceNoisePool = new DoubleArrayPool(64, 16 * 16);
        
        // Aquifer samplers
        ChunkRandom chunkRandom = new ChunkRandom(seed);
        this.edgeDensityNoise = DoublePerlinNoiseSampler.create(new SimpleRandom(chunkRandom.nextLong()), -3, 1.0);
        this.waterLevelNoise = DoublePerlinNoiseSampler.create(new SimpleRandom(chunkRandom.nextLong()), -3, 1.0, 0.0, 2.0);
        this.lavaNoise = DoublePerlinNoiseSampler.create(new SimpleRandom(chunkRandom.nextLong()), -1, 1.0, 0.0);
        
        // Samplers
        this.noiseCaveSampler = this.generateNoiseCaves ? new NoiseCaveSampler(chunkRandom, this.noiseMinY) : null;
        this.oreVeinSampler = new class_6353(seed, this.defaultBlock, this.horizontalNoiseResolution, this.verticalNoiseResolution, this.generatorSettings.get().getGenerationShapeConfig().getMinimumY());
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
        class_6350 aquiferSampler = this.createAquiferSampler(chunkX, chunkZ, chunk.getPos());
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        // Get and populate primary noise array
        double[] heightNoise = this.heightNoisePool.borrowArr();
        this.generateHeightNoiseArr(chunkX * this.noiseSizeX, chunkZ * this.noiseSizeZ, heightNoise);
        
        // Create ore vein samplers
        List<NoiseInterpolator> interpolatorList = new ArrayList<>();
        Pair<BlockSource, DoubleConsumer> pair = this.createOreVeinSamplers(this.noiseMinY, chunkPos, interpolatorList::add);
        BlockSource blockSource = pair.getFirst();
        DoubleConsumer doubleConsumer = pair.getSecond();
        
        interpolatorList.forEach(NoiseInterpolator::sampleStartNoise);
        
        for (int subChunkX = 0; subChunkX < this.noiseSizeX; ++subChunkX) {
            int noiseX = subChunkX;
            interpolatorList.forEach(noiseInterpolator -> noiseInterpolator.sampleEndNoise(noiseX));
            
            for (int subChunkZ = 0; subChunkZ < this.noiseSizeZ; ++ subChunkZ) {
                for (int subChunkY = 0; subChunkY < this.noiseSizeY; ++subChunkY) {
                    int noiseY = subChunkY;
                    int noiseZ = subChunkZ;
                    
                    interpolatorList.forEach(noiseInterpolator -> noiseInterpolator.sampleNoiseCorners(noiseY, noiseZ));
                    
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
                        y += this.minY;
                        
                        double deltaY = subY / (double)this.verticalNoiseResolution;
                        interpolatorList.forEach(noiseInterpolator -> noiseInterpolator.sampleNoiseY(deltaY));
                        
                        double nx1 = lowerNW + (upperNW - lowerNW) * deltaY;
                        double nx2 = lowerSW + (upperSW - lowerSW) * deltaY;
                        double nx3 = lowerNE + (upperNE - lowerNE) * deltaY;
                        double nx4 = lowerSE + (upperSE - lowerSE) * deltaY;
                        
                        for (int subX = 0; subX < this.horizontalNoiseResolution; ++subX) {
                            int x = subX + subChunkX * this.horizontalNoiseResolution;
                            int absX = (chunk.getPos().x << 4) + x;
                            
                            double deltaX = subX / (double)this.horizontalNoiseResolution;
                            interpolatorList.forEach(noiseInterpolator -> noiseInterpolator.sampleNoiseX(deltaX));
                            
                            double nz1 = nx1 + (nx3 - nx1) * deltaX;
                            double nz2 = nx2 + (nx4 - nx2) * deltaX;
                            
                            for (int subZ = 0; subZ < this.horizontalNoiseResolution; ++subZ) {
                                int z = subZ + subChunkZ * this.horizontalNoiseResolution;
                                int absZ = (chunk.getPos().z << 4) + z;
                                
                                double deltaZ = subZ / (double)this.horizontalNoiseResolution;
                                doubleConsumer.accept(deltaZ);
                                
                                double density = nz1 + (nz2 - nz1) * deltaZ;
                                
                                BlockState blockToSet = this.getBlockState(structureWeightSampler, aquiferSampler, blockSource, absX, y, absZ, density);
                                chunk.setBlockState(mutable.set(x, y, z), blockToSet, false);
                                
                                heightmapOCEAN.trackUpdate(x, y, z, blockToSet);
                                heightmapSURFACE.trackUpdate(x, y, z, blockToSet);
                                this.scheduleFluidTick(chunk, aquiferSampler, mutable.set(absX, y, absZ), blockToSet);
                            }
                        }
                    }
                }
            }
            interpolatorList.forEach(NoiseInterpolator::swapBuffers);
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
                        y += this.minY;
                        
                        double deltaY = subY / (double)this.verticalNoiseResolution;
                        
                        double nx1 = lowerNW + (upperNW - lowerNW) * deltaY;
                        double nx2 = lowerSW + (upperSW - lowerSW) * deltaY;
                        double nx3 = lowerNE + (upperNE - lowerNE) * deltaY;
                        double nx4 = lowerSE + (upperSE - lowerSE) * deltaY;
                        
                        for (int subX = 0; subX < this.horizontalNoiseResolution; ++subX) {
                            int x = subX + subChunkX * this.horizontalNoiseResolution;
                            
                            double deltaX = subX / (double)this.horizontalNoiseResolution;
                            
                            double nz1 = nx1 + (nx3 - nx1) * deltaX;
                            double nz2 = nx2 + (nx4 - nx2) * deltaX;
                            
                            for (int subZ = 0; subZ < this.horizontalNoiseResolution; ++subZ) {
                                int z = subZ + subChunkZ * this.horizontalNoiseResolution;
                                
                                double deltaZ = subZ / (double)this.horizontalNoiseResolution;
                                
                                double density = nz1 + (nz2 - nz1) * deltaZ;
                                
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
    protected BlockState getBlockState(StructureWeightSampler weightSampler, class_6350 aquiferSampler, BlockSource blockSource, int x, int y, int z, double density) {
        double clampedDensity = MathHelper.clamp(density / 200.0, -1.0, 1.0);
        clampedDensity = clampedDensity / 2.0 - clampedDensity * clampedDensity * clampedDensity / 24.0;
        clampedDensity += weightSampler.getWeight(x, y, z);
        
        return aquiferSampler.a(blockSource, x, y, z, clampedDensity);
    }
    
    /**
     * Samples density for noise cave.
     * 
     * @param noiseX x-coordinate in absolute noise coordinates.
     * @param noiseY y-coordinate in absolute noise coordinates.
     * @param noiseZ z-coordinate in absolute noise coordinates.
     * @param noise Base density.
     * 
     * @return Modified noise density.
     */
    protected double sampleNoiseCave(int noiseX, int noiseY, int noiseZ, double noise) {
        if (this.noiseCaveSampler != null) {
            return this.noiseCaveSampler.sample(noiseX, noiseY, noiseZ, noise);
        }
        
        return noise;
    }
    
    protected Pair<BlockSource, DoubleConsumer> createOreVeinSamplers(int worldBottomNoiseY, ChunkPos chunkPos, Consumer<NoiseInterpolator> consumer) {
        if (!this.generateOreVeins || !(this.chunkGenerator instanceof NoiseChunkGenerator)) {
            return Pair.of(this.blockSource, doubleConsumer -> {});
        }
        
        class_6352 oreVeinBlockSource = ((NoiseChunkGenerator)this.chunkGenerator).new class_6352(chunkPos, worldBottomNoiseY, this.seed + 1L);
        oreVeinBlockSource.method_36395(consumer);
        
        BlockSource blockSource = (x, y, z) -> {
            BlockState blockState = oreVeinBlockSource.sample(x, y, z);
            
            if (blockState != this.defaultBlock) {
                return blockState;
            } else {
                return this.blockSource.sample(x, y, z);
                //return BlockStates.AIR;
            }
            
        };
        
        return Pair.of(blockSource, oreVeinBlockSource::method_36394);
    }
    
    protected class_6350 createAquiferSampler(int chunkX, int chunkZ, ChunkPos chunkPos) {
        if (!this.generateAquifers) {
            return class_6350.method_36381(this.getSeaLevel(), this.defaultFluid);
        }
        return class_6350.method_36382(chunkPos, this.edgeDensityNoise, this.waterLevelNoise, this.lavaNoise, this.generatorSettings.get(), null, chunkX * this.verticalNoiseResolution, chunkZ * this.verticalNoiseResolution);
    }
    
    /**
     * Schedules fluid tick for aquifer sampler, so water flows when generated.
     * 
     * @param chunk 
     * @param aquiferSampler
     * @param pos BlockPos in block coordinates.
     * @param blockState Blockstate at pos.
     */
    protected void scheduleFluidTick(Chunk chunk, class_6350 aquiferSampler, BlockPos pos, BlockState blockState) {
        if (aquiferSampler.a() && !blockState.getFluidState().isEmpty()) {
            chunk.getFluidTickScheduler().schedule(pos, blockState.getFluidState().getFluid(), 0);
        }
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
        int bottomSlideStart = this.noiseMinY - initialOffset - this.bottomSlideOffset;
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
        int topSlideStart = (this.noiseSizeY + this.noiseMinY + 1) - initialOffset - this.topSlideOffset;
        if (noiseY > topSlideStart) {
            double topSlideDelta = (float) (noiseY - topSlideStart) / (float) this.topSlideSize;
            density = density * (1.0D - topSlideDelta) + this.topSlideTarget * topSlideDelta;
        }
        
        return density;
    }
}
