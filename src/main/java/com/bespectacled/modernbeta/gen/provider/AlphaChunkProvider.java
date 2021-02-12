package com.bespectacled.modernbeta.gen.provider;

import java.util.Random;

import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.gen.OldGeneratorSettings;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BlockStates;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.AquiferSampler;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.StructureWeightSampler;

public class AlphaChunkProvider extends AbstractChunkProvider {
    private final PerlinOctaveNoise minLimitNoiseOctaves;
    private final PerlinOctaveNoise maxLimitNoiseOctaves;
    private final PerlinOctaveNoise mainNoiseOctaves;
    private final PerlinOctaveNoise beachNoiseOctaves;
    private final PerlinOctaveNoise stoneNoiseOctaves;
    private final PerlinOctaveNoise scaleNoiseOctaves;
    private final PerlinOctaveNoise depthNoiseOctaves;
    private final PerlinOctaveNoise forestNoiseOctaves;
    
    public AlphaChunkProvider(long seed, OldGeneratorSettings settings) {
        //super(seed, settings);
        super(seed, -64, 192, 64, 0, -10, 2, 1, 1.0, 1.0, 80, 160, true, true, BlockStates.STONE, BlockStates.WATER, settings);
        
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
        double eighth = 0.03125D; // eighth 

        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        // TODO: Really should be pooled or something
        ChunkRandom rand = this.createChunkRand(chunkX, chunkZ);
        ChunkRandom sandstoneRand = this.createChunkRand(chunkX, chunkZ);
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        double[] sandNoise = beachNoiseOctaves.sampleArr(null, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1, eighth, eighth, 1.0D);
        double[] gravelNoise = beachNoiseOctaves.sampleArr(null, chunkZ * 16, 109.0134D, chunkX * 16, 16, 1, 16, eighth, 1.0D, eighth);
        double[] stoneNoise = stoneNoiseOctaves.sampleArr(null, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1, eighth * 2D, eighth * 2D, eighth * 2D);

        // Accurate beach/terrain patterns depend on z iterating before x,
        // and array accesses changing accordingly.
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {

                boolean genSandBeach = sandNoise[x + z * 16] + rand.nextDouble() * 0.20000000000000001D > 0.0D;
                boolean genGravelBeach = gravelNoise[x + z * 16] + rand.nextDouble() * 0.20000000000000001D > 3D;

                int genStone = (int) (stoneNoise[x + z * 16] / 3D + 3D + rand.nextDouble() * 0.25D);
                int flag = -1;
                
                int absX = (chunkX << 4) + x;
                int absZ = (chunkZ << 4) + z;
                
                Biome curBiome = getBiomeForSurfaceGen(mutable.set(absX, 0, absZ), region, biomeSource);

                BlockState biomeTopBlock = curBiome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                BlockState biomeFillerBlock = curBiome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();

                BlockState topBlock = biomeTopBlock;
                BlockState fillerBlock = biomeFillerBlock;

                // Generate from top to bottom of world
                for (int y = this.worldHeight + this.minY - 1; y >= this.minY; y--) {

                    // Randomly place bedrock from y=0 to y=5
                    if (y <= (this.minY + rand.nextInt(6)) - 1) {
                        chunk.setBlockState(mutable.set(x, y, z), BlockStates.BEDROCK, false);
                        continue;
                    }
                    
                    if (y < 50) {
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
                                topBlock = BlockStates.AIR; // This reduces gravel beach height by 1
                                fillerBlock = BlockStates.GRAVEL;
                            }

                            if (genSandBeach) {
                                topBlock = BlockStates.SAND;
                                fillerBlock = BlockStates.SAND;
                            }
                        }

                        if (y < this.seaLevel && topBlock.equals(BlockStates.AIR)) {
                            topBlock = this.defaultFluid;
                        }

                        // Main surface builder section
                        flag = genStone;
                        if (y >= this.seaLevel - 1) {
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

                    // Gens layer of sandstone starting at lowest block of sand, of height 1 to 4.
                    // Beta backport.
                    if (flag == 0 && fillerBlock.equals(BlockStates.SAND)) {
                        flag = sandstoneRand.nextInt(4);
                        fillerBlock = BlockStates.SANDSTONE;
                    }
                }
            }
        }
        
    }
    
    @Override
    public int getHeight(int x, int z, Type type) {
        BlockPos structPos = new BlockPos(x, 0, z);
        
        if (HEIGHTMAP_CACHE.get(structPos) == null) {
            sampleHeightmap(x, z);
        }

        int groundHeight = HEIGHTMAP_CACHE.get(structPos);

        // Not ideal
        if (type == Heightmap.Type.WORLD_SURFACE_WG && groundHeight < this.seaLevel)
            groundHeight = this.seaLevel;

        return groundHeight;
    }
    
    private void generateTerrain(Chunk chunk, StructureAccessor structureAccessor) {
        int noiseResolutionY = this.noiseSizeY + 1;
        int noiseResolutionXZ = this.noiseSizeX + 1;
        
        double lerpY = 1.0D / this.verticalNoiseResolution;
        double lerpXZ = 1.0D / this.horizontalNoiseResolution;

        Heightmap heightmapOCEAN = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSURFACE = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
        
        StructureWeightSampler structureWeightSampler = new StructureWeightSampler(structureAccessor, chunk);
        AquiferSampler aquiferSampler = this.createAquiferSampler(chunk.getPos().x, chunk.getPos().z);
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        double[] heightNoise = generateHeightmap(chunk.getPos().x * this.noiseSizeX, 0, chunk.getPos().z * this.noiseSizeZ);

        for (int subChunkX = 0; subChunkX < this.noiseSizeX; subChunkX++) {
            for (int subChunkZ = 0; subChunkZ < this.noiseSizeZ; subChunkZ++) {
                for (int subChunkY = 0; subChunkY < this.noiseSizeY; subChunkY++) {

                    double lowerNW, lowerSW, lowerNE, lowerSE;
                    double upperNW, upperSW, upperNE, upperSE;

                    lowerNW = heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 0)];
                    lowerSW = heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 0)];
                    lowerNE = heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 0)];
                    lowerSE = heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 0)];

                    upperNW = (heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 1)] - lowerNW) * lerpY; 
                    upperSW = (heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 1)] - lowerSW) * lerpY;
                    upperNE = (heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 1)] - lowerNE) * lerpY;
                    upperSE = (heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 1)] - lowerSE) * lerpY;
                    
                    for (int subY = 0; subY < this.verticalNoiseResolution; subY++) {
                        int y = subChunkY * this.verticalNoiseResolution + subY;
                        y += this.minY;
                        
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

                                //BlockState blockToSet = getBlockState(structureWeightSampler, absX, y, absZ, density);
                                BlockState blockToSet = this.getBlockState(structureWeightSampler, aquiferSampler, absX, y, absZ, density);
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
        
        // For accurate terrain shape, worldHeight + minY should equal 128.
        int noiseResolutionY = (this.worldHeight + this.minY) / this.verticalNoiseResolution + 1;
        int noiseResolutionX = this.noiseSizeX + 1;
        int noiseResolutionZ = this.noiseSizeZ + 1;

        double coordinateScale = 684.41200000000003D * this.xzScale; 
        double heightScale = 684.41200000000003D * this.yScale;
 
        double depthNoiseScaleX = 100D;
        double depthNoiseScaleZ = 100D;

        double mainNoiseScaleX = this.xzFactor; // Default: 80
        double mainNoiseScaleY = this.yFactor;  // Default: 160
        double mainNoiseScaleZ = this.xzFactor;

        double lowerLimitScale = 512D;
        double upperLimitScale = 512D;
        
        double heightStretch = 12D;

        double[] scaleNoise = scaleNoiseOctaves.sampleArr(null, x, y, z, noiseResolutionX, 1, noiseResolutionZ, 1.0D, 0.0D, 1.0D);
        double[] depthNoise = depthNoiseOctaves.sampleArr(null, x, y, z, noiseResolutionX, 1, noiseResolutionZ, depthNoiseScaleX, 0.0D, depthNoiseScaleZ);

        double[] mainNoise = mainNoiseOctaves.sampleArr(
            null, 
            x, y, z, 
            noiseResolutionX, noiseResolutionY, noiseResolutionZ,
            coordinateScale / mainNoiseScaleX, 
            heightScale / mainNoiseScaleY, 
            coordinateScale / mainNoiseScaleZ
        );

        double[] minLimitNoise = minLimitNoiseOctaves.sampleArr(
            null, 
            x, y, z, 
            noiseResolutionX, noiseResolutionY, noiseResolutionZ,
            coordinateScale, 
            heightScale, 
            coordinateScale
        );

        double[] maxLimitNoise = maxLimitNoiseOctaves.sampleArr(
            null, 
            x, y, z, 
            noiseResolutionX, noiseResolutionY, noiseResolutionZ,
            coordinateScale,
            heightScale,
            coordinateScale
        );

        int mainNoiseNdx = 0;
        int heightNoiseNdx = 0;
        int flatNoiseNdx = 0;
        
        for (int noiseX = 0; noiseX < noiseResolutionX; noiseX++) {
            for (int noiseZ = 0; noiseZ < noiseResolutionZ; noiseZ++) {

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

                double depthVal2 = (double) noiseResolutionY / 2D + depthVal * 4D;

                flatNoiseNdx++;

                for (int noiseY = this.noiseMinY; noiseY < noiseResolutionY; noiseY++) {
                    double heightVal = 0.0D;
                    double scaleVal2 = (((double) noiseY - depthVal2) * heightStretch) / scaleVal;

                    if (scaleVal2 < 0.0D) {
                        scaleVal2 *= 4D;
                    }

                    double minLimitVal = minLimitNoise[mainNoiseNdx] / lowerLimitScale;
                    double maxLimitVal = maxLimitNoise[mainNoiseNdx] / upperLimitScale;
                    double mainLimitVal = (mainNoise[mainNoiseNdx] / 10D + 1.0D) / 2D;

                    if (mainLimitVal < 0.0D) {
                        heightVal = minLimitVal;
                    } else if (mainLimitVal > 1.0D) {
                        heightVal = maxLimitVal;
                    } else {
                        heightVal = minLimitVal + (maxLimitVal - minLimitVal) * mainLimitVal;
                    }
                    
                    double heightValWithOffset = heightVal - scaleVal2; 
                    
                    // Sample for noise caves
                    heightValWithOffset = this.sampleNoiseCave(
                        (x + noiseX) * this.horizontalNoiseResolution,
                        noiseY * this.verticalNoiseResolution,
                        (z + noiseZ) * this.horizontalNoiseResolution,
                        heightVal,
                        heightValWithOffset
                    );

                    int slideOffset = 4;
                    if (noiseY > noiseResolutionY - slideOffset) {
                        double topSlide = (float) (noiseY - (noiseResolutionY - slideOffset)) / 3F;
                        heightValWithOffset = heightValWithOffset * (1.0D - topSlide) + -10D * topSlide;
                    }
                    
                    if (this.generateAquifers || this.generateNoiseCaves)
                        heightValWithOffset = this.applyBottomSlide(heightValWithOffset, noiseY);

                    // Unused in original source
                    /*
                    if ((double) noiseY < 0.0D) {
                        double bottomSlide = (0.0D - (double) noiseY) / 4D;
                        if (bottomSlide < 0.0D) {
                            bottomSlide = 0.0D;
                        }
                        if (bottomSlide > 1.0D) {
                            bottomSlide = 1.0D;
                        }
                        heightValWithOffset = heightValWithOffset * (1.0D - bottomSlide) + -10D * bottomSlide;
                    }
                    */

                    heightNoise[heightNoiseNdx] = heightValWithOffset;
                    heightNoiseNdx++;
                    
                    if (noiseY >= 0)
                        mainNoiseNdx++;
                }
            }
        }
        
        return heightNoise;
    }
    
    private void sampleHeightmap(int sampleX, int sampleZ) {
        int noiseResolutionY = this.noiseSizeY + 1;
        int noiseResolutionXZ = this.noiseSizeX + 1;
        
        double lerpY = 1.0D / this.verticalNoiseResolution;
        double lerpXZ = 1.0D / this.horizontalNoiseResolution;
        
        int chunkX = sampleX >> 4;
        int chunkZ = sampleZ >> 4;

        double[] heightNoise = generateHeightmap(chunkX * this.noiseSizeX, 0, chunkZ * this.noiseSizeZ);

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
        
    }

    @Override
    public PerlinOctaveNoise getBeachNoiseOctaves() {
        return this.beachNoiseOctaves;
    }

}
