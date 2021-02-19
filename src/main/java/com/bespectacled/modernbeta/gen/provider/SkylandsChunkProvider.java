package com.bespectacled.modernbeta.gen.provider;

import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.gen.OldGeneratorSettings;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.DoubleArrayPool;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
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
    private final PerlinOctaveNoise beachNoiseOctaves;
    private final PerlinOctaveNoise stoneNoiseOctaves;
    private final PerlinOctaveNoise scaleNoiseOctaves;
    private final PerlinOctaveNoise depthNoiseOctaves;
    private final PerlinOctaveNoise forestNoiseOctaves;
    
    private final DoubleArrayPool heightNoisePool;
    private final DoubleArrayPool beachNoisePool;
    
    public SkylandsChunkProvider(long seed, OldGeneratorSettings settings) {
        //super(seed, settings);
        super(seed, 0, 128, 64, 0, -10, 1, 2, 1.0, 1.0, 80, 160, false, false, false, BlockStates.STONE, BlockStates.WATER, settings);
        
        // Noise Generators
        minLimitNoiseOctaves = new PerlinOctaveNoise(RAND, 16, true);
        maxLimitNoiseOctaves = new PerlinOctaveNoise(RAND, 16, true);
        mainNoiseOctaves = new PerlinOctaveNoise(RAND, 8, true);
        beachNoiseOctaves = new PerlinOctaveNoise(RAND, 4, true);
        stoneNoiseOctaves = new PerlinOctaveNoise(RAND, 4, true);
        scaleNoiseOctaves = new PerlinOctaveNoise(RAND, 10, true);
        depthNoiseOctaves = new PerlinOctaveNoise(RAND, 16, true);
        forestNoiseOctaves = new PerlinOctaveNoise(RAND, 8, true);
        
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
        BlockPos structPos = new BlockPos(x, 0, z);
        fillChunkY(16);
        
        if (HEIGHTMAP_CACHE.get(structPos) == null) {
            sampleHeightmap(x, z);
        }

        int groundHeight = HEIGHTMAP_CACHE.get(structPos);

        return groundHeight;
    }
    
    @Override
    public PerlinOctaveNoise getBeachNoiseOctaves() {
        return null;
    }
    
    public void generateTerrain(Chunk chunk, StructureAccessor structureAccessor) {
        int noiseResolutionY = this.noiseSizeY + 1;
        int noiseResolutionXZ = this.noiseSizeX + 1;
        
        double lerpY = 1.0D / this.verticalNoiseResolution;
        double lerpXZ = 1.0D / this.horizontalNoiseResolution;

        Heightmap heightmapOCEAN = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSURFACE = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);

        StructureWeightSampler structureWeightSampler = new StructureWeightSampler(structureAccessor, chunk);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        double[] heightNoise = this.heightNoisePool.borrowArr();
        this.generateHeightmap(heightNoise, chunk.getPos().x * this.noiseSizeX, 0, chunk.getPos().z * this.noiseSizeZ);

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
                        
                        double curNW = lowerNW;
                        double curSW = lowerSW;
                        double avgN = (lowerNE - lowerNW) * lerpXZ; 
                        double avgS = (lowerSE - lowerSW) * lerpXZ;

                        for (int subX = 0; subX < this.horizontalNoiseResolution; subX++) {
                            int x = subX + subChunkX * this.horizontalNoiseResolution;
                            int absX = (chunk.getPos().x << 4) + x;
                            
                            double density = curNW; // var15
                            double progress = (curSW - curNW) * lerpXZ; 
                            
                            for (int subZ = 0; subZ < this.horizontalNoiseResolution; subZ++) {
                                int z = subZ + subChunkZ * this.horizontalNoiseResolution;
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
    
    private void generateHeightmap(double[] heightNoise, int x, int y, int z) {
        int noiseResolutionY = this.noiseSizeY + 1;
        int noiseResolutionX = this.noiseSizeX + 1;
        int noiseResolutionZ = this.noiseSizeZ + 1;
        
        //int startX = x / this.noiseSizeX * 16;
        //int startZ = z / this.noiseSizeZ * 16;
        
        //BetaClimateSampler climateSampler = BetaClimateSampler.INSTANCE;

        // Var names taken from old customized preset names
        double coordinateScale = 684.41200000000003D * this.xzScale; 
        double heightScale = 684.41200000000003D * this.yScale;

        //double depthNoiseScaleX = 200D;
        //double depthNoiseScaleZ = 200D;
        //double depthNoiseScaleExponent = 0.5D;

        double mainNoiseScaleX = this.xzFactor; // Default: 80
        double mainNoiseScaleY = this.yFactor;  // Default: 160
        double mainNoiseScaleZ = this.xzFactor;

        double lowerLimitScale = 512D;
        double upperLimitScale = 512D;
        
        //double heightStretch = 8D;

        double[] mainNoise = this.heightNoisePool.borrowArr();
        double[] minLimitNoise = this.heightNoisePool.borrowArr();
        double[] maxLimitNoise = this.heightNoisePool.borrowArr();
        
        /*
        double[] scaleNoise = scaleNoiseOctaves.sampleArrBeta(null, x, z, noiseResolutionX, noiseResolutionZ, 1.121D, 1.121D, 0.5D);
        double[] depthNoise = depthNoiseOctaves.sampleArrBeta(null, x, z, noiseResolutionX, noiseResolutionZ, depthNoiseScaleX, depthNoiseScaleZ,
                depthNoiseScaleExponent);
        */
        
        coordinateScale *= 2D;

        mainNoise = mainNoiseOctaves.sampleArrBeta(
            mainNoise, 
            x, y, z, 
            noiseResolutionX, noiseResolutionY, noiseResolutionZ,
            coordinateScale / mainNoiseScaleX, 
            heightScale / mainNoiseScaleY, 
            coordinateScale / mainNoiseScaleZ
        );

        minLimitNoise = minLimitNoiseOctaves.sampleArrBeta(
            minLimitNoise, 
            x, y, z, 
            noiseResolutionX, noiseResolutionY, noiseResolutionZ,
            coordinateScale, 
            heightScale, 
            coordinateScale
        );

        maxLimitNoise = maxLimitNoiseOctaves.sampleArrBeta(
            maxLimitNoise, 
            x, y, z, 
            noiseResolutionX, noiseResolutionY, noiseResolutionZ,
            coordinateScale, 
            heightScale, 
            coordinateScale
        );

        int heightNoiseNdx = 0;
        //int flatNoiseNdx = 0;
        int k = 16 / noiseResolutionX;

        for (int noiseX = 0; noiseX < noiseResolutionX; noiseX++) {
            //int relX = noiseX * k + k / 2;

            for (int noiseZ = 0; noiseZ < noiseResolutionZ; noiseZ++) {
                //int relZ = noiseZ * k + k / 2;

                /* Noise is calculated but unused for whatever reason.
                double curTemp = climateSampler.sampleTemp(startX + relX, startZ + relZ);
                double curHumid = climateSampler.sampleHumid(startX + relX, startZ + relZ) * curTemp;

                double humidVal = 1.0D - curHumid;
                humidVal *= humidVal;
                humidVal *= humidVal;
                humidVal = 1.0D - humidVal;

                double scaleVal = (scaleNoise[flatNoiseNdx] + 256D) / 512D;
                scaleVal *= humidVal;

                if (scaleVal > 1.0D) {
                    scaleVal = 1.0D;
                }

                double depthVal = depthNoise[flatNoiseNdx] / 8000D;

                if (depthVal < 0.0D) {
                    depthVal = -depthVal * 0.29999999999999999D;
                }

                depthVal = depthVal * 3D - 2D;

                if (depthVal > 1.0D) {
                    depthVal = 1.0D;
                }

                depthVal /= 8D;
                depthVal = 0.0D;

                if (scaleVal < 0.0D) {
                    scaleVal = 0.0D;
                }

                scaleVal += 0.5D;
                depthVal = (depthVal * (double) noiseResolutionY) / 16D;

                double depthVal2 = (double) noiseResolutionY / 16D;

                flatNoiseNdx++;
                */
                
                for (int noiseY = 0; noiseY < noiseResolutionY; noiseY++) {
                    double heightVal = 0.0D;
                    
                    /*
                    double scaleVal2 = (((double) noiseY - depthVal2) * heightStretch) / scaleVal;

                    if (scaleVal2 < 0.0D) {
                        scaleVal2 *= -1D;
                    }
                    */

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
                    
                    double heightValWithOffset = heightVal - 8D;
                    
                    // Sample for noise caves
                    heightValWithOffset = this.sampleNoiseCave(
                        (x + noiseX) * this.horizontalNoiseResolution,
                        noiseY * this.verticalNoiseResolution,
                        (z + noiseZ) * this.horizontalNoiseResolution,
                        heightVal,
                        heightValWithOffset
                    );
                    
                    //int slideOffset = 32;
                    int slideOffset = this.noiseSizeY;
                    if (noiseY > noiseResolutionY - slideOffset) {
                        double topSlide = (float) (noiseY - (noiseResolutionY - slideOffset)) / ((float) slideOffset - 1.0F);
                        heightValWithOffset = heightValWithOffset * (1.0D - topSlide) + -30D * topSlide;
                    }

                    //slideOffset = 8;
                    slideOffset = this.noiseSizeY / 4;
                    if (noiseY < slideOffset) {
                        double bottomSlide = (float) (slideOffset - noiseY) / ((float) slideOffset - 1.0F);
                        heightValWithOffset = heightValWithOffset * (1.0D - bottomSlide) + -30D * bottomSlide;
                    }

                    heightNoise[heightNoiseNdx] = heightValWithOffset;
                    heightNoiseNdx++;
                }
            }
        }
        
        this.heightNoisePool.returnArr(maxLimitNoise);
        this.heightNoisePool.returnArr(minLimitNoise);
        this.heightNoisePool.returnArr(mainNoise);
    }
    
    private void sampleHeightmap(int absX, int absZ) {
        int noiseResolutionY = this.noiseSizeY + 1;
        int noiseResolutionXZ = this.noiseSizeX + 1;
        
        double lerpY = 1.0D / this.verticalNoiseResolution;
        double lerpXZ = 1.0D / this.horizontalNoiseResolution;
        
        int chunkX = absX >> 4;
        int chunkZ = absZ >> 4;

        double[] heightNoise = this.heightNoisePool.borrowArr();
        this.generateHeightmap(heightNoise, chunkX * this.noiseSizeX, 0, chunkZ * this.noiseSizeZ);

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
                        
                        double curNW = lowerNW;
                        double curSW = lowerSW;
                        double avgN = (lowerNE - lowerNW) * lerpXZ; 
                        double avgS = (lowerSE - lowerSW) * lerpXZ;

                        for (int subX = 0; subX < this.horizontalNoiseResolution; subX++) {
                            int x = subX + subChunkX * this.horizontalNoiseResolution;
                            
                            double density = curNW; // var15
                            double progress = (curSW - curNW) * lerpXZ; 
                            
                            for (int subZ = 0; subZ < this.horizontalNoiseResolution; subZ++) {
                                int z = subZ + subChunkZ * this.horizontalNoiseResolution;
                                
                                if (density > 0.0) {
                                    HEIGHTMAP_CHUNK[x][z] = y;
                                }

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

        for (int pX = 0; pX < HEIGHTMAP_CHUNK.length; pX++) {
            for (int pZ = 0; pZ < HEIGHTMAP_CHUNK[pX].length; pZ++) {
                BlockPos structPos = new BlockPos((chunkX << 4) + pX, 0, (chunkZ << 4) + pZ);
                //POS.set((chunkX << 4) + pX, 0, (chunkZ << 4) + pZ);
                
                HEIGHTMAP_CACHE.put(structPos, HEIGHTMAP_CHUNK[pX][pZ] + 1); // +1 because it is one above the ground
            }
        }
        
        this.heightNoisePool.returnArr(heightNoise);
    }

    private static void fillChunkY(int y) {
        for (int z = 0; z < 16; ++z) {
            for (int x = 0; x < 16; ++x) {
                HEIGHTMAP_CHUNK[x][z] = y;
            }
        }
    }
}
