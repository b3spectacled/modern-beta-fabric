package com.bespectacled.modernbeta.gen.provider;

import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.biome.beta.BetaClimateSampler;
import com.bespectacled.modernbeta.gen.OldGeneratorSettings;
import com.bespectacled.modernbeta.gen.OldGeneratorUtil;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.DoubleArrayPool;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.AquiferSampler;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.StructureWeightSampler;

public class BetaChunkProvider extends AbstractChunkProvider {
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
    
    public BetaChunkProvider(long seed, OldGeneratorSettings settings) {
        //super(seed, settings);
        super(seed, -64, 192, 64, 0, -10, 2, 1, 1.0, 1.0, 80, 160, -10, 3, 0, 15, 3, 0, true, true, true, BlockStates.STONE, BlockStates.WATER, settings);
        
        // Noise Generators
        this.minLimitNoiseOctaves = new PerlinOctaveNoise(RAND, 16, true);
        this.maxLimitNoiseOctaves = new PerlinOctaveNoise(RAND, 16, true);
        this.mainNoiseOctaves = new PerlinOctaveNoise(RAND, 8, true);
        this.beachNoiseOctaves = new PerlinOctaveNoise(RAND, 4, true);
        this.stoneNoiseOctaves = new PerlinOctaveNoise(RAND, 4, true);
        this.scaleNoiseOctaves = new PerlinOctaveNoise(RAND, 10, true);
        this.depthNoiseOctaves = new PerlinOctaveNoise(RAND, 16, true);
        this.forestNoiseOctaves = new PerlinOctaveNoise(RAND, 8, true);
        
        // Noise array pools
        this.heightNoisePool = new DoubleArrayPool(64, (this.noiseSizeX + 1) * (this.noiseSizeZ + 1) * (this.noiseSizeY + 1));
        this.beachNoisePool = new DoubleArrayPool(64, 16 * 16);
        
        BetaClimateSampler.INSTANCE.setSeed(seed);
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
        
        int bedrockFloor = this.minY + this.bedrockFloor;
        
        // TODO: Really should be pooled or something
        ChunkRandom rand = this.createChunkRand(chunkX, chunkZ);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        double[] sandNoise = this.beachNoisePool.borrowArr();
        double[] gravelNoise = this.beachNoisePool.borrowArr();
        double[] stoneNoise = this.beachNoisePool.borrowArr();

        sandNoise = beachNoiseOctaves.sampleArrBeta(
            sandNoise, 
            chunkX * 16, chunkZ * 16, 0.0D, 
            16, 16, 1,
            eighth, eighth, 1.0D);
        
        gravelNoise = beachNoiseOctaves.sampleArrBeta(
            gravelNoise, 
            chunkX * 16, 109.0134D, chunkZ * 16, 
            16, 1, 16, 
            eighth, 1.0D, eighth);
        
        stoneNoise = stoneNoiseOctaves.sampleArrBeta(
            stoneNoise, 
            chunkX * 16, chunkZ * 16, 0.0D, 
            16, 16, 1,
            eighth * 2D, eighth * 2D, eighth * 2D
        );

        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {
                int absX = (chunkX << 4) + x;
                int absZ = (chunkZ << 4) + z;
                int topY = OldGeneratorUtil.getSolidHeight(chunk, this.worldHeight, this.minY, x, z) + 1;
                
                boolean genSandBeach = sandNoise[z + x * 16] + rand.nextDouble() * 0.20000000000000001D > 0.0D;
                boolean genGravelBeach = gravelNoise[z + x * 16] + rand.nextDouble() * 0.20000000000000001D > 3D;
                
                int genStone = (int) (stoneNoise[z + x * 16] / 3D + 3D + rand.nextDouble() * 0.25D);
                
                int flag = -1;
                
                Biome biome = getBiomeForSurfaceGen(mutable.set(absX, topY, absZ), region, biomeSource);

                BlockState biomeTopBlock = biome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                BlockState biomeFillerBlock = biome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();

                BlockState topBlock = biomeTopBlock;
                BlockState fillerBlock = biomeFillerBlock;
                
                boolean usedCustomSurface = this.useCustomSurfaceBuilder(biome, biomeSource.getBiomeRegistry().getId(biome), region, chunk, rand, mutable);

                // Generate from top to bottom of world
                for (int y = this.worldTopY - 1; y >= this.minY; y--) {

                    // Randomly place bedrock from y=0 (or minHeight) to y=5
                    if (y <= bedrockFloor + rand.nextInt(5)) {
                        chunk.setBlockState(mutable.set(x, y, z), BlockStates.BEDROCK, false);
                        continue;
                    }
                    
                    // Don't surface build below 50, per 1.17 default surface builder
                    if (usedCustomSurface || (this.generateAquifers || this.generateNoiseCaves) && y < 50) {
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

                        if (y < this.seaLevel && topBlock.equals(BlockStates.AIR)) { // Generate water bodies
                            topBlock = this.defaultFluid;
                        }

                        flag = genStone;
                        if (y >= this.seaLevel - 1) {
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
        
        this.beachNoisePool.returnArr(sandNoise);
        this.beachNoisePool.returnArr(gravelNoise);
        this.beachNoisePool.returnArr(stoneNoise);
    }

    @Override
    public int getHeight(int x, int z, Type type) {
        Integer groundHeight = HEIGHTMAP_CACHE.get(new BlockPos(x, 0, z));
        
        if (groundHeight == null) {
            groundHeight = this.sampleHeightmap(x, z);
        }

        // Not ideal
        if (type == Heightmap.Type.WORLD_SURFACE_WG && groundHeight < this.seaLevel)
            groundHeight = this.seaLevel;

        return groundHeight;
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
        
        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;

        Heightmap heightmapOCEAN = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSURFACE = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
        
        StructureWeightSampler structureWeightSampler = new StructureWeightSampler(structureAccessor, chunk);
        AquiferSampler aquiferSampler = this.createAquiferSampler(chunk.getPos().x, chunk.getPos().z);
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
                                
                                if (absX == 1139 && absZ == -420) {
                                    System.out.println("HEIGHT: " + y);
                                    System.out.println("NOISE X / NOISE Z: " + subChunkX + "/" + subChunkZ);
                                }
                                
                                BlockState blockToSet = this.getBlockState(structureWeightSampler, aquiferSampler, absX, y, absZ, density);
                                chunk.setBlockState(mutable.set(x, y, z), blockToSet, false);

                                heightmapOCEAN.trackUpdate(x, y, z, blockToSet);
                                heightmapSURFACE.trackUpdate(x, y, z, blockToSet);
                                this.scheduleFluidTick(chunk, aquiferSampler, mutable.set(absX, y, absZ), blockToSet);

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
    
    private void generateScaleDepth(int x, int z, int noiseX, int noiseZ, double[] scaleDepth) {
        if (scaleDepth.length != 2) 
            throw new IllegalArgumentException("[Modern Beta] Scale/Depth array has incorrect length, should be 2.");
        
        BetaClimateSampler climateSampler = BetaClimateSampler.INSTANCE;
        
        double depthNoiseScaleX = 200D;
        double depthNoiseScaleZ = 200D;
        
        //double baseSize = noiseResolutionY / 2D; // Or: 17 / 2D = 8.5
        double baseSize = 8.5D;
        
        double temp = climateSampler.sampleTemp(x, z);
        double humid = climateSampler.sampleHumid(x, z) * temp;
        
        humid = 1.0D - humid;
        humid *= humid;
        humid *= humid;
        humid = 1.0D - humid;

        double scale = this.scaleNoiseOctaves.sample(noiseX, noiseZ, 1.121D, 1.121D);
        scale = (scale + 256D) / 512D;
        scale *= humid;
        
        if (scale > 1.0D) {
            scale = 1.0D;
        }
        
        double depth0 = this.depthNoiseOctaves.sample(noiseX, noiseZ, depthNoiseScaleX, depthNoiseScaleZ);
        depth0 /= 8000D;

        if (depth0 < 0.0D) {
            depth0 = -depth0 * 0.29999999999999999D;
        }

        depth0 = depth0 * 3D - 2D;

        if (depth0 < 0.0D) {
            depth0 /= 2D;

            if (depth0 < -1D) {
                depth0 = -1D;
            }

            depth0 /= 1.3999999999999999D;
            if (!this.generateDeepOceans) depth0 /= 2D;

            scale = 0.0D;

        } else {
            if (depth0 > 1.0D) {
                depth0 = 1.0D;
            }
            depth0 /= 8D;
        }

        if (scale < 0.0D) {
            scale = 0.0D;
        }

        scale += 0.5D;
        //depthVal = (depthVal * (double) noiseResolutionY) / 16D;
        //double depthVal2 = (double) noiseResolutionY / 2D + depthVal * 4D;
        depth0 = depth0 * baseSize / 8D;
        double depth1 = baseSize + depth0 * 4D;
        
        scaleDepth[0] = scale;
        scaleDepth[1] = depth1;
    }
    
    private void generateHeightNoiseArr(int noiseX, int noiseY, int noiseZ, double[] heightNoise) {
        int noiseResolutionX = this.noiseSizeX + 1;
        int noiseResolutionZ = this.noiseSizeZ + 1;
        int noiseResolutionY = this.noiseSizeY + 1;
        
        int startX = noiseX / this.noiseSizeX * 16;
        int startZ = noiseZ / this.noiseSizeZ * 16;
        int transformCoord = 16 / noiseResolutionX;
        
        double[] scaleDepth = new double[2];
        
        int ndx = 0;
        for (int nX = 0; nX < noiseResolutionX; ++nX) {
            for (int nZ = 0; nZ < noiseResolutionZ; ++nZ) {
                int absX = startX + nX * transformCoord + transformCoord / 2;
                int absZ = startZ + nZ * transformCoord + transformCoord / 2;
                this.generateScaleDepth(absX, absZ, noiseX + nX, noiseZ + nZ, scaleDepth);
                
                for (int nY = this.noiseMinY; nY < noiseResolutionY + this.noiseMinY; ++nY) {
                    heightNoise[ndx] = this.generateHeightNoise(noiseX + nX, nY, noiseZ + nZ, scaleDepth[0], scaleDepth[1]);
                    ndx++;
                }
            }
        }
    }
    
    private double generateHeightNoise(int noiseX, int noiseY, int noiseZ, double scale, double depth) {
        // Var names taken from old customized preset names
        double coordinateScale = 684.41200000000003D * this.xzScale; 
        double heightScale = 684.41200000000003D * this.yScale;
        
        double mainNoiseScaleX = this.xzFactor; // Default: 80
        double mainNoiseScaleY = this.yFactor;  // Default: 160
        double mainNoiseScaleZ = this.xzFactor;

        double lowerLimitScale = 512D;
        double upperLimitScale = 512D;
        
        double heightStretch = 12D;
        
        double density = 0.0D;
        double densityOffset = (((double)noiseY - depth) * heightStretch) / scale;
        
        if (densityOffset < 0.0D) {
            densityOffset *= 4D;
        }
        
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
        
        densityWithOffset = this.applyTopSlide(densityWithOffset, noiseY, 4);
        densityWithOffset = this.applyBottomSlide(densityWithOffset, noiseY, -3);
        
        return densityWithOffset;
    }

    
    private int sampleHeightmap(int sampleX, int sampleZ) {
        int noiseResolutionY = this.noiseSizeY + 1;
        int noiseResolutionXZ = this.noiseSizeX + 1;

        int chunkX = sampleX >> 4;
        int chunkZ = sampleZ >> 4;
        
        int startX = chunkX << 4;
        int startZ = chunkZ << 4;
        
        double lerpY = 1.0D / this.verticalNoiseResolution;
        double lerpXZ = 1.0D / this.horizontalNoiseResolution;

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
                        double avgN = (lowerNE - lowerNW) * lerpXZ;
                        double avgS = (lowerSE - lowerSW) * lerpXZ;
                        
                        for (int subX = 0; subX < this.horizontalNoiseResolution; subX++) {
                            int x = (subX + subChunkX * this.horizontalNoiseResolution);
                            
                            double density = curNW;
                            double progress = (curSW - curNW) * lerpXZ; 

                            for (int subZ = 0; subZ < this.horizontalNoiseResolution; subZ++) {
                                int z = (subChunkZ * this.horizontalNoiseResolution + subZ);
                                
                                if (density > 0.0) {
                                    HEIGHTMAP_CHUNK[z + x * 16] = y;
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

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                HEIGHTMAP_CACHE.put(new BlockPos(startX + x, 0, startZ + z), HEIGHTMAP_CHUNK[z + x * 16] + 1);
            }
        }
        
        this.heightNoisePool.returnArr(heightNoise);
        return HEIGHTMAP_CACHE.get(new BlockPos(sampleX, 0, sampleZ));
    }
}
