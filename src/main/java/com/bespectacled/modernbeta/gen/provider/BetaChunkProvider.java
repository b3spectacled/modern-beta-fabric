package com.bespectacled.modernbeta.gen.provider;

import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.biome.beta.BetaClimateSampler;
import com.bespectacled.modernbeta.gen.OldGeneratorSettings;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoiseNew;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.DoubleArrayPool;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
    
    private final PerlinOctaveNoiseNew minLimitNoiseOctavesNew;
    private final PerlinOctaveNoiseNew maxLimitNoiseOctavesNew;
    private final PerlinOctaveNoiseNew mainNoiseOctavesNew;
    private final PerlinOctaveNoiseNew beachNoiseOctavesNew;
    private final PerlinOctaveNoiseNew stoneNoiseOctavesNew;
    private final PerlinOctaveNoiseNew scaleNoiseOctavesNew;
    private final PerlinOctaveNoiseNew depthNoiseOctavesNew;
    
    private final DoubleArrayPool twoDNoisePool;
    private final DoubleArrayPool heightNoisePool;
    private final DoubleArrayPool beachNoisePool;
    
    public BetaChunkProvider(long seed, OldGeneratorSettings settings) {
        //super(seed, settings);
        super(seed, -64, 192, 64, 0, -10, 2, 1, 1.0, 1.0, 80, 160, false, false, true, BlockStates.STONE, BlockStates.WATER, settings);
        
        // Noise Generators
        minLimitNoiseOctaves = new PerlinOctaveNoise(RAND, 16, true);
        maxLimitNoiseOctaves = new PerlinOctaveNoise(RAND, 16, true);
        mainNoiseOctaves = new PerlinOctaveNoise(RAND, 8, true);
        beachNoiseOctaves = new PerlinOctaveNoise(RAND, 4, true);
        stoneNoiseOctaves = new PerlinOctaveNoise(RAND, 4, true);
        scaleNoiseOctaves = new PerlinOctaveNoise(RAND, 10, true);
        depthNoiseOctaves = new PerlinOctaveNoise(RAND, 16, true);
        forestNoiseOctaves = new PerlinOctaveNoise(RAND, 8, true);
        
        // Alternate Noise Generators
        ChunkRandom worldGenRand = new ChunkRandom(seed);
        minLimitNoiseOctavesNew = new PerlinOctaveNoiseNew(worldGenRand, 16);
        maxLimitNoiseOctavesNew = new PerlinOctaveNoiseNew(worldGenRand, 16);
        mainNoiseOctavesNew = new PerlinOctaveNoiseNew(worldGenRand, 8);
        beachNoiseOctavesNew = new PerlinOctaveNoiseNew(worldGenRand, 4);
        stoneNoiseOctavesNew = new PerlinOctaveNoiseNew(worldGenRand, 4);
        scaleNoiseOctavesNew = new PerlinOctaveNoiseNew(worldGenRand, 10);
        depthNoiseOctavesNew = new PerlinOctaveNoiseNew(worldGenRand, 16);
        
        // Noise array pools
        this.twoDNoisePool = new DoubleArrayPool(64, (this.noiseSizeX + 1) * (this.noiseSizeZ + 1));
        this.heightNoisePool = new DoubleArrayPool(64, (this.noiseSizeX + 1) * (this.noiseSizeZ + 1) * (this.noiseSizeY + 1));
        this.beachNoisePool = new DoubleArrayPool(64, 16 * 16);
        
        BetaClimateSampler.INSTANCE.setSeed(seed);
        setForestOctaves(forestNoiseOctaves);
    }

    @Override
    public synchronized Chunk provideChunk(StructureAccessor structureAccessor, Chunk chunk, OldBiomeSource biomeSource) {
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

                boolean genSandBeach = sandNoise[z + x * 16] + rand.nextDouble() * 0.20000000000000001D > 0.0D;
                boolean genGravelBeach = gravelNoise[z + x * 16] + rand.nextDouble() * 0.20000000000000001D > 3D;

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
                for (int y = this.worldHeight - Math.abs(this.minY) - 1; y >= this.minY; y--) {

                    // Randomly place bedrock from y=0 (or minHeight) to y=5
                    if (y <= this.minY + rand.nextInt(5)) {
                        chunk.setBlockState(mutable.set(x, y, z), BlockStates.BEDROCK, false);
                        continue;
                    }
                    
                    // Don't surface build below 50, per 1.17 default surface builder
                    if ((this.generateAquifers || this.generateNoiseCaves) && y < 50) {
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
        int groundHeight = sampleHeightmap(x, z);
        
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
        double[] scaleNoise = this.twoDNoisePool.borrowArr();
        double[] depthNoise = this.twoDNoisePool.borrowArr();
        
        this.generateScaleDepthArr(chunkX, chunkZ, scaleNoise, depthNoise);

        for (int subChunkX = 0; subChunkX < this.noiseSizeX; subChunkX++) {
            for (int subChunkZ = 0; subChunkZ < this.noiseSizeZ; subChunkZ++) {
                int noiseX = chunkX * this.noiseSizeX + subChunkX;
                int noiseZ = chunkZ * this.noiseSizeZ + subChunkZ;
                
                double scale0 = scaleNoise[subChunkZ * (this.noiseSizeX + 1) + subChunkX];
                double depth0 = depthNoise[subChunkZ * (this.noiseSizeX + 1) + subChunkX];

                double scale1 = scaleNoise[(subChunkZ + 1) * (this.noiseSizeX + 1) + subChunkX];
                double depth1 = depthNoise[(subChunkZ + 1) * (this.noiseSizeX + 1) + subChunkX];
                
                double scale2 = scaleNoise[subChunkZ * (this.noiseSizeX + 1) + (subChunkX + 1)];
                double depth2 = depthNoise[subChunkZ * (this.noiseSizeX + 1) + (subChunkX + 1)];
                
                double scale3 = scaleNoise[(subChunkZ + 1) * (this.noiseSizeX + 1) + (subChunkX + 1)];
                double depth3 = depthNoise[(subChunkZ + 1) * (this.noiseSizeX + 1) + (subChunkX + 1)];

                for (int subChunkY = 0; subChunkY < this.noiseSizeY + 1; subChunkY++) {
                    int offsetY = subChunkY + this.noiseMinY;
                    
                    heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + subChunkY] = this.generateHeightNoise(noiseX, offsetY, noiseZ, scale0, depth0);
                    heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + subChunkY] = this.generateHeightNoise(noiseX, offsetY, noiseZ + 1, scale1, depth1);
                    heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + subChunkY] = this.generateHeightNoise(noiseX + 1, offsetY, noiseZ, scale2, depth2);
                    heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + subChunkY] = this.generateHeightNoise(noiseX + 1, offsetY, noiseZ + 1, scale3, depth3);    
                }
                
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
                                
                                //double temp = BetaClimateSampler.getInstance().sampleTemp(absX, absZ);

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
        
        this.heightNoisePool.returnArr(heightNoise);
        this.twoDNoisePool.returnArr(scaleNoise);
        this.twoDNoisePool.returnArr(depthNoise);
    }
    
    private void generateScaleDepthArr(int chunkX, int chunkZ, double[] scaleNoise, double[] depthNoise) {
        int startX = chunkX * 16;
        int startZ = chunkZ * 16;
        
        int startNoiseX = chunkX * this.noiseSizeX;
        int startNoiseZ = chunkZ * this.noiseSizeZ;
        
        int noiseResolutionX = this.noiseSizeX + 1;
        int noiseResolutionZ = this.noiseSizeZ + 1;

        int transformCoord = 16 / noiseResolutionX;
        double[] scaleDepth = new double[2];
        
        int ndx = 0;
        for (int noiseZ = 0; noiseZ < noiseResolutionZ; noiseZ++) {
            for (int noiseX = 0; noiseX < noiseResolutionX; noiseX++) {
                int x = startX + noiseX * transformCoord + transformCoord / 2;
                int z = startZ + noiseZ * transformCoord + transformCoord / 2;
                
                this.generateScaleDepth(x, z, startNoiseX + noiseX, startNoiseZ + noiseZ, scaleDepth);
                
                scaleNoise[ndx] = scaleDepth[0];
                depthNoise[ndx] = scaleDepth[1];
                
                ndx++;
            }
        }
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

        double scale = this.scaleNoiseOctavesNew.sample(noiseX, noiseZ, 1.121D, 1.121D);
        scale = (scale + 256D) / 512D;
        scale *= humid;
        
        if (scale > 1.0D) {
            scale = 1.0D;
        }
        
        double depth0 = this.depthNoiseOctavesNew.sample(noiseX, noiseZ, depthNoiseScaleX, depthNoiseScaleZ);
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
            depth0 /= 2D;

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
    
    private double generateHeightNoise(int x, int y, int z, double scale, double depth) {
        int noiseResolutionY = this.noisePosY + 1;
        
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
        double densityOffset = (((double)y - depth) * heightStretch) / scale;
        
        if (densityOffset < 0.0D) {
            densityOffset *= 4D;
        }
        
        // Equivalent to current MC noise.sample() function, see NoiseColumnSampler.
        double mainNoise = (this.mainNoiseOctavesNew.sample(x, y, z, coordinateScale / mainNoiseScaleX, heightScale / mainNoiseScaleY, coordinateScale / mainNoiseScaleZ) / 10D + 1.0D) / 2D;
        if (mainNoise < 0.0D) {
            density = this.minLimitNoiseOctavesNew.sample(x, y, z, coordinateScale, heightScale, coordinateScale) / lowerLimitScale;
        } else if (mainNoise > 1.0D) {
            density = this.maxLimitNoiseOctavesNew.sample(x, y, z, coordinateScale, heightScale, coordinateScale) / upperLimitScale;
        } else {
            double minLimitNoise = this.minLimitNoiseOctavesNew.sample(x, y, z, coordinateScale, heightScale, coordinateScale) / lowerLimitScale;
            double maxLimitNoise = this.maxLimitNoiseOctavesNew.sample(x, y, z, coordinateScale, heightScale, coordinateScale) / upperLimitScale;
            density = minLimitNoise + (maxLimitNoise - minLimitNoise) * mainNoise;
        }
        
        // Equivalent to current MC addition of density offset, see NoiseColumnSampler.
        double densityWithOffset = density - densityOffset; 
        
        // Sample for noise caves
        densityWithOffset = this.sampleNoiseCave(x * this.horizontalNoiseResolution, y * this.verticalNoiseResolution, z * this.horizontalNoiseResolution, density, densityWithOffset);
        
        int slideOffset = 4;
        if (y > noiseResolutionY - slideOffset) {
            double topSlide = (float) (y - (noiseResolutionY - slideOffset)) / 3F;
            densityWithOffset = densityWithOffset * (1.0D - topSlide) + -10D * topSlide;
        }
        
        if (this.generateAquifers || this.generateNoiseCaves)
            densityWithOffset = this.applyBottomSlide(densityWithOffset, y);
        
        return densityWithOffset;
    }
    
    private int sampleHeightmap(int sampleX, int sampleZ) {
        int startX = (sampleX >> 4) << 4;
        int startZ = (sampleZ >> 4) << 4;
        int transformCoord = 16 / (this.noiseSizeX + 1);
        
        int noiseX = MathHelper.floorDiv(sampleX, this.horizontalNoiseResolution);
        int noiseZ = MathHelper.floorDiv(sampleZ, this.horizontalNoiseResolution);
        
        int modX = MathHelper.floorMod(sampleX, this.horizontalNoiseResolution);
        int modZ = MathHelper.floorMod(sampleZ, this.horizontalNoiseResolution);
        
        double lerpX = modX / (double)this.horizontalNoiseResolution;
        double lerpZ = modZ / (double)this.horizontalNoiseResolution;
        
        double[] heightNoise = this.heightNoisePool.borrowArr();
        
        double[] scaleDepth0 = new double[2];
        double[] scaleDepth1 = new double[2];
        double[] scaleDepth2 = new double[2];
        double[] scaleDepth3 = new double[2];
        
        int absX0 = startX + (noiseX + 0) * transformCoord + transformCoord / 2;
        int absZ0 = startZ + (noiseZ + 0) * transformCoord + transformCoord / 2;
        
        int absX1 = startX + (noiseX + 0) * transformCoord + transformCoord / 2;
        int absZ1 = startZ + (noiseZ + 1) * transformCoord + transformCoord / 2;
        
        int absX2 = startX + (noiseX + 1) * transformCoord + transformCoord / 2;
        int absZ2 = startZ + (noiseZ + 0) * transformCoord + transformCoord / 2;
        
        int absX3 = startX + (noiseX + 1) * transformCoord + transformCoord / 2;
        int absZ3 = startZ + (noiseZ + 1) * transformCoord + transformCoord / 2;
        
        this.generateScaleDepth(absX0, absZ0, noiseX, noiseZ, scaleDepth0);
        this.generateScaleDepth(absX1, absZ1, noiseX, noiseZ + 1, scaleDepth1);
        this.generateScaleDepth(absX2, absZ2, noiseX + 1, noiseZ, scaleDepth2);
        this.generateScaleDepth(absX3, absZ3, noiseX + 1, noiseZ + 1, scaleDepth3);

        for (int subChunkY = 0; subChunkY < this.noiseSizeY + 1; subChunkY++) {
            int offsetY = subChunkY + this.noiseMinY;
            
            heightNoise[subChunkY * this.noiseSizeX + 0] = this.generateHeightNoise(noiseX, offsetY, noiseZ, scaleDepth0[0], scaleDepth0[1]);
            heightNoise[subChunkY * this.noiseSizeX + 1] = this.generateHeightNoise(noiseX, offsetY, noiseZ + 1, scaleDepth1[0], scaleDepth1[1]);
            heightNoise[subChunkY * this.noiseSizeX + 2] = this.generateHeightNoise(noiseX + 1, offsetY, noiseZ, scaleDepth2[0], scaleDepth2[1]);
            heightNoise[subChunkY * this.noiseSizeX + 3] = this.generateHeightNoise(noiseX + 1, offsetY, noiseZ + 1, scaleDepth3[0], scaleDepth3[1]);    
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
