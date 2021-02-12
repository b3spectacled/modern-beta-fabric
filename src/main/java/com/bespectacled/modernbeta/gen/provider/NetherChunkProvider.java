package com.bespectacled.modernbeta.gen.provider;

import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.gen.OldGeneratorSettings;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BlockStates;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;

public class NetherChunkProvider extends AbstractChunkProvider {
    private final PerlinOctaveNoise minLimitNoiseOctaves;
    private final PerlinOctaveNoise maxLimitNoiseOctaves;
    private final PerlinOctaveNoise mainNoiseOctaves;
    private final PerlinOctaveNoise beachNoiseOctaves;
    private final PerlinOctaveNoise stoneNoiseOctaves;
    private final PerlinOctaveNoise scaleNoiseOctaves;
    private final PerlinOctaveNoise depthNoiseOctaves;
    private final PerlinOctaveNoise forestNoiseOctaves;
    
    public NetherChunkProvider(long seed, OldGeneratorSettings settings) {
        //super(seed, settings);
        super(seed, 0, 128, 32, 0, 128, 2, 1, 1.0, 1.0, 80, 60, false, false, BlockStates.STONE, BlockStates.WATER, settings);
        
        // Noise Generators
        minLimitNoiseOctaves = new PerlinOctaveNoise(RAND, 16, true);
        maxLimitNoiseOctaves = new PerlinOctaveNoise(RAND, 16, true);
        mainNoiseOctaves = new PerlinOctaveNoise(RAND, 8, true);
        beachNoiseOctaves = new PerlinOctaveNoise(RAND, 4, true);
        stoneNoiseOctaves = new PerlinOctaveNoise(RAND, 4, true);
        scaleNoiseOctaves = new PerlinOctaveNoise(RAND, 10, true);
        depthNoiseOctaves = new PerlinOctaveNoise(RAND, 16, true);
        forestNoiseOctaves = new PerlinOctaveNoise(RAND, 8, true);

        setForestOctaves(forestNoiseOctaves);
    }

    @Override
    public Chunk provideChunk(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk, OldBiomeSource biomeSource) {
        generateTerrain(chunk, structureAccessor);
        return chunk;
    }
    
    @Override
    public void provideSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource) {
        double thirtysecond = 0.03125D; // eighth

        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        // TODO: Really should be pooled or something
        ChunkRandom rand = this.createChunkRand(chunkX, chunkZ);
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        double[] sandNoise = beachNoiseOctaves.sampleArrBeta(
            null, 
            chunkX * 16, chunkZ * 16, 0.0D, 
            16, 16, 1,
            thirtysecond, thirtysecond, 1.0D);
        
        double[] gravelNoise = beachNoiseOctaves.sampleArrBeta(
            null, 
            chunkX * 16, 109.0134D, chunkZ * 16, 
            16, 1, 16, 
            thirtysecond, 1.0D, thirtysecond);
        
        double[] stoneNoise = stoneNoiseOctaves.sampleArrBeta(
            null, 
            chunkX * 16, chunkZ * 16, 0.0D, 
            16, 16, 1,
            thirtysecond * 2D, thirtysecond * 2D, thirtysecond * 2D
        );

        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {

                boolean genSandBeach = sandNoise[z + x * 16] + rand.nextDouble() * 0.20000000000000001D > 0.0D;
                boolean genGravelBeach = gravelNoise[z + x * 16] + rand.nextDouble() * 0.20000000000000001D > 0.0D;

                int genStone = (int) (stoneNoise[z + x * 16] / 3D + 3D + rand.nextDouble() * 0.25D);
                int flag = -1;
                
                int absX = chunk.getPos().getStartX() + x;
                int absZ = chunk.getPos().getStartZ() + z;
                    
                Biome curBiome = getBiomeForSurfaceGen(mutable.set(absX, 0, absZ), region, biomeSource);

                BlockState biomeTopBlock = curBiome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                BlockState biomeFillerBlock = curBiome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();

                BlockState topBlock = biomeTopBlock;
                BlockState fillerBlock = biomeFillerBlock;

                // Generate from top to bottom of world
                for (int y = this.worldHeight - 1; y >= this.minY; y--) {
                    
                    // Randomly place bedrock from top of world to height - 4
                    if (y >= this.worldHeight - 1 - rand.nextInt(5)) {
                        chunk.setBlockState(mutable.set(x, y, z), BlockStates.BEDROCK, false);
                        continue;
                    }
                    
                    // Randomly place bedrock from y=0 (or minHeight) to y=4
                    if (y <= this.minY + rand.nextInt(5)) {
                        chunk.setBlockState(mutable.set(x, y, z), BlockStates.BEDROCK, false);
                        continue;
                    }

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
                            
                        } else if (y >= this.seaLevel - 4 && y <= this.seaLevel + 1) { // Generate beaches at this y range
                            topBlock = biomeTopBlock;
                            fillerBlock = biomeFillerBlock;

                            if (genGravelBeach) {
                                topBlock = BlockStates.GRAVEL; // This reduces gravel beach height by 1
                                fillerBlock = biomeFillerBlock;
                            }

                            if (genSandBeach) {
                                topBlock = BlockStates.SAND;
                                fillerBlock = BlockStates.SAND;
                            }
                        }

                        if (y < this.seaLevel && topBlock.equals(BlockStates.AIR)) { // Generate water bodies
                            topBlock = this.defaultFluid;
                        }

                        flag = genStone;
                        
                        if (y >= this.seaLevel) {
                            chunk.setBlockState(mutable.set(x, y, z), topBlock, false);
                        } else {
                            chunk.setBlockState(mutable.set(x, y, z), fillerBlock, false);
                        }
                        
                        continue;
                    }

                    if (flag > 0) {
                        flag--;
                        chunk.setBlockState(mutable.set(x, y, z), fillerBlock, false);
                    }

                    // Generates layer of sandstone starting at lowest block of sand, of height 1 to 4.
                    if (flag == 0 && fillerBlock.equals(BlockStates.SAND)) {
                        flag = rand.nextInt(4);
                        fillerBlock = BlockStates.SANDSTONE;
                    }
                }
            }
        }
    }

    @Override
    public int getHeight(int x, int z, Type type) {
        return Integer.MAX_VALUE;
    }
    
    @Override
    public PerlinOctaveNoise getBeachNoiseOctaves() {
        return this.beachNoiseOctaves;
    }
    
    private void generateTerrain(Chunk chunk, StructureAccessor structureAccessor) {
        int noiseResolutionY = this.noiseSizeY + 1;
        int noiseResolutionXZ = this.noiseSizeX + 1;
        
        double lerpY = 1.0D / this.verticalNoiseResolution;
        double lerpXZ = 1.0D / this.horizontalNoiseResolution;

        Heightmap heightmapOCEAN = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSURFACE = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
        
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        double[] heightNoise = generateHeightmap(chunk.getPos().x * this.noiseSizeX, 0, chunk.getPos().z * this.noiseSizeZ);

        for (int subChunkX = 0; subChunkX < this.noiseSizeX; subChunkX++) {
            for (int subChunkZ = 0; subChunkZ < this.noiseSizeZ; subChunkZ++) {
                for (int subChunkY = this.noiseMinY; subChunkY < this.noiseSizeY; subChunkY++) {

                    double lowerNW, lowerSW, lowerNE, lowerSE;
                    double upperNW, upperSW, upperNE, upperSE;

                    lowerNW = lowerSW = lowerNE = lowerSE = 1.0;
                    upperNW = upperSW = upperNE = upperSE = 1.0;

                    if (subChunkY >= 0) {
                        lowerNW = heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 0)];
                        lowerSW = heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 0)];
                        lowerNE = heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 0)];
                        lowerSE = heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 0)];

                        upperNW = (heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 1)] - lowerNW) * lerpY; 
                        upperSW = (heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 1)] - lowerSW) * lerpY;
                        upperNE = (heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 1)] - lowerNE) * lerpY;
                        upperSE = (heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 1)] - lowerSE) * lerpY;
                    }

                    for (int subY = 0; subY < this.verticalNoiseResolution; subY++) {
                        int y = subChunkY * this.verticalNoiseResolution + subY;
                        
                        double curNW = lowerNW;
                        double curSW = lowerSW;
                        double avgN = (lowerNE - lowerNW) * lerpXZ; // Lerp
                        double avgS = (lowerSE - lowerSW) * lerpXZ;
                        
                        for (int subX = 0; subX < this.horizontalNoiseResolution; subX++) {
                            int x = (subX + subChunkX * this.horizontalNoiseResolution);
                            
                            double density = curNW; // var15
                            double progress = (curSW - curNW) * lerpXZ; 

                            for (int subZ = 0; subZ < this.horizontalNoiseResolution; subZ++) {
                                int z = (subChunkZ * this.horizontalNoiseResolution + subZ); 
                                
                                BlockState blockToSet = BlockStates.AIR;
                                
                                if (subChunkY * this.verticalNoiseResolution + subY < this.seaLevel + 1) {
                                    blockToSet = this.defaultFluid;
                                }
                                
                                if (density > 0.0D) {
                                    blockToSet = this.defaultBlock;
                                }
                                
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
    }

    private double[] generateHeightmap(int x, int y, int z) {
        double[] heightNoise = new double[(this.noiseSizeX + 1) * (this.noiseSizeZ + 1) * (this.noiseSizeY + 1)];
        
        int noiseResolutionY = this.noiseSizeY + 1;
        int noiseResolutionX = this.noiseSizeX + 1;
        int noiseResolutionZ = this.noiseSizeZ + 1;
        
        // Var names taken from old customized preset names
        double coordinateScale = 684.41200000000003D * this.xzScale; 
        double heightScale = 684.41200000000003D * this.yScale;
        
        //double depthNoiseScaleX = 100D;
        //double depthNoiseScaleZ = 100D;

        double mainNoiseScaleX = this.xzFactor; // Default: 80
        double mainNoiseScaleY = this.yFactor;  // Default: 60
        double mainNoiseScaleZ = this.xzFactor;

        double lowerLimitScale = 512D;
        double upperLimitScale = 512D;

        /*
        scaleNoise = scaleNoiseOctaves.sampleArrBeta(
            scaleNoise, 
            x, y, z, 
            noiseResolutionX, 1, noiseResolutionZ, 
            1.0D, 
            0.0D, 
            1.0D
        );
        
        depthNoise = depthNoiseOctaves.sampleArrBeta(
            depthNoise, 
            x, y, z, 
            noiseResolutionX, 1, noiseResolutionZ, 
            depthNoiseScaleX, 
            0.0D,
            depthNoiseScaleZ
        );
        */

        double[] mainNoise = mainNoiseOctaves.sampleArrBeta(
            null, 
            x, y, z, 
            noiseResolutionX, noiseResolutionY, noiseResolutionZ,
            coordinateScale / mainNoiseScaleX, 
            heightScale / mainNoiseScaleY, 
            coordinateScale / mainNoiseScaleZ
        );

        double[] minLimitNoise = minLimitNoiseOctaves.sampleArrBeta(
            null, 
            x, y, z, 
            noiseResolutionX, noiseResolutionY, noiseResolutionZ,
            coordinateScale, 
            heightScale, 
            coordinateScale
        );

        double[] maxLimitNoise = maxLimitNoiseOctaves.sampleArrBeta(
            null, 
            x, y, z, 
            noiseResolutionX, noiseResolutionY, noiseResolutionZ,
            coordinateScale, 
            heightScale, 
            coordinateScale
        );

        int heightNoiseNdx = 0;
        //int flatNoiseNdx = 0;
        
        double heightOffSets[] = new double[noiseResolutionY];
        
        // Generates height offsets to create solid floor and ceiling
        for (int noiseY = 0; noiseY < noiseResolutionY; noiseY++) {
            heightOffSets[noiseY] = Math.cos(((double)noiseY * 3.1415926535897931D * 6D) / (double)noiseResolutionY) * 2D;
            
            double offset = noiseY;
            if (noiseY > noiseResolutionY / 2) {
                offset = noiseResolutionY - 1 - noiseY;
            }
            if (offset < this.noiseSizeY / 4D) {     // orig: mod < 4D
                offset = this.noiseSizeY / 4D - offset; // orig: mod = 4D - mod;
                heightOffSets[noiseY] -= offset * offset * offset * 10D;
            }
        }

        for (int noiseX = 0; noiseX < noiseResolutionX; noiseX++) {
            for (int noiseZ = 0; noiseZ < noiseResolutionZ; noiseZ++) {
                
                /*
                double scaleVal = (scaleNoise[flatNoiseNdx] + 256D) / 512D;

                if (scaleVal > 1.0D) {
                    scaleVal = 1.0D;
                }

                double depthVal = depthNoise[flatNoiseNdx] / 8000D;

                if (depthVal < 0.0D) {
                    depthVal = -depthVal;
                }

                depthVal = depthVal * 3D - 3D;

                if (depthVal < 0.0D) {
                    depthVal /= 2D;

                    if (depthVal < -1D) {
                        depthVal = -1D;
                    }

                    depthVal /= 1.3999999999999999D;
                    depthVal /= 2D;

                    scaleVal = 0.0D;

                } else {
                    if (depthVal > 1.0D) {
                        depthVal = 1.0D;
                    }
                    depthVal /= 6D;
                }

                scaleVal += 0.5D;
                depthVal = (depthVal * (double) noiseResolutionY) / 16D;
                
                flatNoiseNdx++;
                */             

                for (int noiseY = 0; noiseY < noiseResolutionY; noiseY++) {
                    double heightVal = 0.0D;
                    double heightOffset = heightOffSets[noiseY];
                    
                    double minLimitVal = minLimitNoise[heightNoiseNdx] / lowerLimitScale;
                    double maxLimitVal = maxLimitNoise[heightNoiseNdx] / upperLimitScale;
                    double mainNoiseVal = (mainNoise[heightNoiseNdx] / 10D + 1.0D) / 2D;

                    if (mainNoiseVal < 0.0D) {
                        heightVal = minLimitVal;
                    } else if (mainNoiseVal > 1.0D) {
                        heightVal = maxLimitVal;
                    } else {
                        heightVal = minLimitVal + (maxLimitVal - minLimitVal) * mainNoiseVal;
                    }
                    
                    heightVal -= heightOffset; 
                    
                    int slideOffset = 4;
                    if (noiseY > noiseResolutionY - slideOffset) {
                        double topSlide = (float) (noiseY - (noiseResolutionY - slideOffset)) / 3F;
                        heightVal = heightVal * (1.0D - topSlide) + -10D * topSlide;
                    }
                    
                    if (noiseY < 0.0D) {
                        double bottomSlide = (0.0 - (double)noiseY) / 4D;
                        
                        if (bottomSlide < 0.0D) {
                            bottomSlide = 0.0D;
                        }
                        if (bottomSlide > 1.0D) {
                            bottomSlide = 1.0D;
                        }
                        
                        heightVal = heightVal * (1.0D - bottomSlide) + -10D * bottomSlide;
                    }
                    
                    heightNoise[heightNoiseNdx] = heightVal;
                    heightNoiseNdx++;
                }
            }
        }
        
        return heightNoise;
    }
}
