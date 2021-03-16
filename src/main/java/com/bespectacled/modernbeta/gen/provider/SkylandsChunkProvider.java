package com.bespectacled.modernbeta.gen.provider;

import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.gen.OldGeneratorSettings;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.DoubleArrayPool;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.StructureWeightSampler;

public class SkylandsChunkProvider extends AbstractChunkProvider {
    private final PerlinOctaveNoise minLimitNoiseOctaves;
    private final PerlinOctaveNoise maxLimitNoiseOctaves;
    private final PerlinOctaveNoise mainNoiseOctaves;
    private final PerlinOctaveNoise stoneNoiseOctaves;
    private final PerlinOctaveNoise forestNoiseOctaves;
    
    private final DoubleArrayPool heightNoisePool;
    private final DoubleArrayPool beachNoisePool;
    
    public SkylandsChunkProvider(long seed, OldGeneratorSettings settings) {
        //super(seed, settings);
        super(seed, 0, 128, 64, 0, -10, 1, 2, 2.0, 1.0, 80, 160, -30, 31, 0, -30, 7, 0, false, false, false, BlockStates.STONE, BlockStates.WATER, settings);
        
        // Noise Generators
        this.minLimitNoiseOctaves = new PerlinOctaveNoise(RAND, 16, true);
        this.maxLimitNoiseOctaves = new PerlinOctaveNoise(RAND, 16, true);
        this.mainNoiseOctaves = new PerlinOctaveNoise(RAND, 8, true);
        new PerlinOctaveNoise(RAND, 4, true);
        this.stoneNoiseOctaves = new PerlinOctaveNoise(RAND, 4, true);
        new PerlinOctaveNoise(RAND, 10, true);
        new PerlinOctaveNoise(RAND, 16, true);
        this.forestNoiseOctaves = new PerlinOctaveNoise(RAND, 8, true);
        
        // Noise array pools
        this.heightNoisePool = new DoubleArrayPool(64, (this.noiseSizeX + 1) * (this.noiseSizeZ + 1) * (this.noiseSizeY + 1));
        this.beachNoisePool = new DoubleArrayPool(64, 16 * 16);

        setForestOctaves(forestNoiseOctaves);
    }

    @Override
    public Chunk provideChunk(StructureAccessor structureAccessor, Chunk chunk, OldBiomeSource biomeSource) {
        generateTerrain(chunk, structureAccessor);
        return chunk;
    }

    @Override
    public void provideSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource) {
        double eighth = 0.03125D;

        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;

        // TODO: Really should be pooled or something
        ChunkRandom rand = this.createChunkRand(chunkX, chunkZ);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        double[] stoneNoise = this.beachNoisePool.borrowArr();
        
        stoneNoise = stoneNoiseOctaves.sampleArrBeta(stoneNoise, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1, eighth * 2D, eighth * 2D, eighth * 2D);

        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {

                int genStone = (int) (stoneNoise[z + x * 16] / 3D + 3D + rand.nextDouble() * 0.25D);
                int flag = -1;
                
                int absX = (chunkX << 4) + x; 
                int absZ = (chunkZ << 4) + z;

                Biome curBiome = getBiomeForSurfaceGen(mutable.set(absX, 0, absZ), region, biomeSource);
                
                BlockState biomeTopBlock = curBiome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                BlockState biomeFillerBlock = curBiome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();

                BlockState topBlock = biomeTopBlock;
                BlockState fillerBlock = biomeFillerBlock;

                // Generate from top to bottom of world
                for (int y = this.worldHeight - Math.abs(this.minY) - 1; y >= this.minY; y--) {

                    BlockState someBlock = chunk.getBlockState(mutable.set(x, y, z));
                    
                    if (someBlock.equals(BlockStates.AIR)) { // Skip if air block
                        flag = -1;
                        continue;
                    }

                    if (!someBlock.equals(this.defaultBlock)) { // Skip if not stone
                        continue;
                    }

                    if (flag == -1) {
                        if (genStone <= 0) { // Generate stone basin if noise permits
                            topBlock = BlockStates.AIR;
                            fillerBlock = this.defaultBlock;
                        }

                        flag = genStone;
                        if (y >= 0) {
                            chunk.setBlockState(mutable.set(x, y, z), topBlock, false);
                        } else {
                            chunk.setBlockState(mutable.set(x, y, z), fillerBlock, false);
                        }

                        continue;
                    }

                    if (flag <= 0) {
                        continue;
                    }

                    flag--;
                    chunk.setBlockState(mutable.set(x, y, z), fillerBlock, false);

                    // Generates layer of sandstone starting at lowest block of sand, of height 1 to 4.
                    if (flag == 0 && fillerBlock.equals(BlockStates.SAND)) {
                        flag = rand.nextInt(4);
                        fillerBlock = BlockStates.SANDSTONE;
                    }
                }
            }
        }
        
        this.beachNoisePool.returnArr(stoneNoise);
    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type type) {
        int groundHeight = sampleHeightmap(x, z);
        groundHeight = groundHeight >= 0 ? groundHeight : 16;
        
        return groundHeight;
    }
    
    @Override
    public PerlinOctaveNoise getBeachNoiseOctaves() {
        return null;
    }
    
    private void generateTerrain(Chunk chunk, StructureAccessor structureAccessor) {
        int noiseResolutionY = this.noiseSizeY + 1;
        int noiseResolutionXZ = this.noiseSizeX + 1;
        
        double lerpY = 1.0D / this.verticalNoiseResolution;
        double lerpXZ = 1.0D / this.horizontalNoiseResolution;
        
        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;

        Heightmap heightmapOCEAN = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSURFACE = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
        
        StructureWeightSampler structureWeightSampler = new StructureWeightSampler(structureAccessor, chunk);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        double[] heightNoise = this.heightNoisePool.borrowArr();
        this.generateHeightNoiseArr(chunkX * this.noiseSizeX, 0, chunkZ * this.noiseSizeZ, heightNoise);

        for (int subChunkX = 0; subChunkX < this.noiseSizeX; subChunkX++) {
            for (int subChunkZ = 0; subChunkZ < this.noiseSizeZ; subChunkZ++) {
                for (int subChunkY = 0; subChunkY < this.noiseSizeY; subChunkY++) {
                    
                    double lowerNW = heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 0)];
                    double lowerSW = heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 0)];
                    double lowerNE = heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 0)];
                    double lowerSE = heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 0)];

                    double upperNW = (heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 1)] - lowerNW) * lerpY; 
                    double upperSW = (heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 1)] - lowerSW) * lerpY;
                    double upperNE = (heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 1)] - lowerNE) * lerpY;
                    double upperSE = (heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 1)] - lowerSE) * lerpY;

                    for (int subY = 0; subY < this.verticalNoiseResolution; subY++) {
                        int y = subChunkY * this.verticalNoiseResolution + subY;
                        y += this.minY;
                        
                        double curNW = lowerNW;
                        double curSW = lowerSW;
                        double avgN = (lowerNE - lowerNW) * lerpXZ; // Lerp
                        double avgS = (lowerSE - lowerSW) * lerpXZ;
                        
                        for (int subX = 0; subX < this.horizontalNoiseResolution; subX++) {
                            int x = (subX + subChunkX * this.horizontalNoiseResolution);
                            int absX = (chunk.getPos().x << 4) + x;
                            
                            double density = curNW; // var15
                            double progress = (curSW - curNW) * lerpXZ; 

                            for (int subZ = 0; subZ < this.horizontalNoiseResolution; subZ++) {
                                int z = (subChunkZ * this.horizontalNoiseResolution + subZ);
                                int absZ = (chunk.getPos().z << 4) + z;

                                BlockState blockToSet = getBlockStateSky(structureWeightSampler, absX, y, absZ, density);
                                chunk.setBlockState(mutable.set(x, y, z), blockToSet, false);

                                heightmapOCEAN.trackUpdate(x, y, z, blockToSet);
                                heightmapSURFACE.trackUpdate(x, y, z, blockToSet);

                                density += progress;
                            }

                            curNW += avgN;
                            curSW += avgS;
                        }

                        lowerNW += upperNW;
                        lowerSW += upperSW;
                        lowerNE += upperNE;
                        lowerSE += upperSE;
                    }
                }
            }
        }
        
        this.heightNoisePool.returnArr(heightNoise);
    }
    
    private void generateHeightNoiseArr(int noiseX, int noiseY, int noiseZ, double[] heightNoise) {
        int noiseResolutionX = this.noiseSizeX + 1;
        int noiseResolutionZ = this.noiseSizeZ + 1;
        int noiseResolutionY = this.noiseSizeY + 1;
        
        int ndx = 0;
        for (int nX = 0; nX < noiseResolutionX; ++nX) {
            for (int nZ = 0; nZ < noiseResolutionZ; ++nZ) {
                for (int nY = this.noiseMinY; nY < noiseResolutionY + this.noiseMinY; ++nY) {
                    heightNoise[ndx] = this.generateHeightNoise(noiseX + nX, nY, noiseZ + nZ);
                    ndx++;
                }
            }
        }
    }
    
    private double generateHeightNoise(int noiseX, int noiseY, int noiseZ) {
        int noiseResolutionY = this.noiseSizeY + 1;
        
        // Var names taken from old customized preset names
        double coordinateScale = 684.41200000000003D * this.xzScale; 
        double heightScale = 684.41200000000003D * this.yScale;
        
        double mainNoiseScaleX = this.xzFactor; // Default: 80
        double mainNoiseScaleY = this.yFactor;  // Default: 160
        double mainNoiseScaleZ = this.xzFactor;

        double lowerLimitScale = 512D;
        double upperLimitScale = 512D;
        
        double density = 0.0D;
        double densityOffset = 8D;
        
        // Equivalent to current MC noise.sample() function, see NoiseColumnSampler.
        double mainNoise = (this.mainNoiseOctaves.sample(
            noiseX, noiseY, noiseZ, 
            coordinateScale / mainNoiseScaleX, 
            heightScale / mainNoiseScaleY, 
            coordinateScale / mainNoiseScaleZ
        ) / 10D + 1.0D) / 2D;
        
        if (mainNoise < 0.0D) {
            density = this.minLimitNoiseOctaves.sample(noiseX, noiseY, noiseZ, coordinateScale, heightScale, coordinateScale) / lowerLimitScale;
        } else if (mainNoise > 1.0D) {
            density = this.maxLimitNoiseOctaves.sample(noiseX, noiseY, noiseZ, coordinateScale, heightScale, coordinateScale) / upperLimitScale;
        } else {
            double minLimitNoise = this.minLimitNoiseOctaves.sample(noiseX, noiseY, noiseZ, coordinateScale, heightScale, coordinateScale) / lowerLimitScale;
            double maxLimitNoise = this.maxLimitNoiseOctaves.sample(noiseX, noiseY, noiseZ, coordinateScale, heightScale, coordinateScale) / upperLimitScale;
            density = minLimitNoise + (maxLimitNoise - minLimitNoise) * mainNoise;
        }
        
        // Equivalent to current MC addition of density offset, see NoiseColumnSampler.
        double densityWithOffset = density - densityOffset; 
        
        // Sample for noise caves
        densityWithOffset = this.sampleNoiseCave(
            noiseX * this.horizontalNoiseResolution, 
            noiseY * this.verticalNoiseResolution, 
            noiseZ * this.horizontalNoiseResolution,
            densityWithOffset
        );
        
        /*
        //int slideOffset = 32;
        int slideOffset = this.noiseSizeY;
        if (y > noiseResolutionY - slideOffset) {
            double topSlide = (float) (y - (noiseResolutionY - slideOffset)) / ((float) slideOffset - 1.0F);
            densityWithOffset = densityWithOffset * (1.0D - topSlide) + -30D * topSlide;
        }

        //slideOffset = 8;
        slideOffset = this.noiseSizeY / 4;
        if (y < slideOffset) {
            double bottomSlide = (float) (slideOffset - y) / ((float) slideOffset - 1.0F);
            densityWithOffset = densityWithOffset * (1.0D - bottomSlide) + -30D * bottomSlide;
        }*/
        
        int topSlideStart = noiseResolutionY - this.noiseSizeY - this.topSlideOffset;
        if (noiseY > topSlideStart) {
            double topSlideDelta = (float) (noiseY - topSlideStart) / ((float) this.topSlideSize);
            densityWithOffset = densityWithOffset * (1.0D - topSlideDelta) + this.topSlideTarget * topSlideDelta;
        }
        
        int bottomSlideStart = this.noiseMinY + 8 - this.bottomSlideOffset;
        if (noiseY < bottomSlideStart) {
            double bottomSlideDelta = (float) (bottomSlideStart - noiseY) / ((float) this.bottomSlideSize);
            densityWithOffset = densityWithOffset * (1.0D - bottomSlideDelta) + this.bottomSlideTarget * bottomSlideDelta;
        }
        
        return densityWithOffset;
    }
    
    private int sampleHeightmap(int sampleX, int sampleZ) {
        int noiseX = MathHelper.floorDiv(sampleX, this.horizontalNoiseResolution);
        int noiseZ = MathHelper.floorDiv(sampleZ, this.horizontalNoiseResolution);
        
        int modX = MathHelper.floorMod(sampleX, this.horizontalNoiseResolution);
        int modZ = MathHelper.floorMod(sampleZ, this.horizontalNoiseResolution);
        
        double lerpX = modX / (double)this.horizontalNoiseResolution;
        double lerpZ = modZ / (double)this.horizontalNoiseResolution;
        
        double[] heightNoise = this.heightNoisePool.borrowArr();

        for (int subChunkY = 0; subChunkY < this.noiseSizeY + 1; subChunkY++) {
            int offsetY = subChunkY + this.noiseMinY;
            
            heightNoise[subChunkY * this.noiseSizeX + 0] = this.generateHeightNoise(noiseX, offsetY, noiseZ);
            heightNoise[subChunkY * this.noiseSizeX + 1] = this.generateHeightNoise(noiseX, offsetY, noiseZ + 1);
            heightNoise[subChunkY * this.noiseSizeX + 2] = this.generateHeightNoise(noiseX + 1, offsetY, noiseZ);
            heightNoise[subChunkY * this.noiseSizeX + 3] = this.generateHeightNoise(noiseX + 1, offsetY, noiseZ + 1);    
        }
        
        for (int subChunkY = this.noiseSizeY - 1; subChunkY >= 0; --subChunkY) {
            double lowerNW = heightNoise[(subChunkY) * this.noiseSizeX + 0];
            double lowerSW = heightNoise[(subChunkY) * this.noiseSizeX + 1];
            double lowerNE = heightNoise[(subChunkY) * this.noiseSizeX + 2];
            double lowerSE = heightNoise[(subChunkY) * this.noiseSizeX + 3];
            
            double upperNW = heightNoise[(subChunkY + 1) * this.noiseSizeX + 0];
            double upperSW = heightNoise[(subChunkY + 1) * this.noiseSizeX + 1];
            double upperNE = heightNoise[(subChunkY + 1) * this.noiseSizeX + 2];
            double upperSE = heightNoise[(subChunkY + 1) * this.noiseSizeX + 3];
            
            for (int subY = this.verticalNoiseResolution - 1; subY >= 0; --subY) {
                int y = subChunkY * this.verticalNoiseResolution + subY;
                y += this.minY;

                double lerpY = subY / (double)this.verticalNoiseResolution;
                double density = MathHelper.lerp3(lerpY, lerpX, lerpZ, lowerNW, upperNW, lowerNE, upperNE, lowerSW, upperSW, lowerSE, upperSE);
                
                if (density > 0.0) {
                    this.heightNoisePool.returnArr(heightNoise);
                    return y + 1;
                }
            }
        }
        
        this.heightNoisePool.returnArr(heightNoise);
        return -1;
    }
}
