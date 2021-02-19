package com.bespectacled.modernbeta.gen.provider;

import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.biome.beta.BetaClimateSampler;
import com.bespectacled.modernbeta.gen.OldGeneratorSettings;
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
    
    private final DoubleArrayPool twoDNoisePool;
    private final DoubleArrayPool threeDNoisePool;
    private final DoubleArrayPool heightNoisePool;
    private final DoubleArrayPool beachNoisePool;
    
    public BetaChunkProvider(long seed, OldGeneratorSettings settings) {
        //super(seed, settings);
        super(seed, -64, 192, 64, 0, -10, 2, 1, 1.0, 1.0, 80, 160, true, true, true, BlockStates.STONE, BlockStates.WATER, settings);
        
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
        this.twoDNoisePool = new DoubleArrayPool(64, (this.noiseSizeX + 1) * (this.noiseSizeZ + 1));
        this.threeDNoisePool = new DoubleArrayPool(64, (this.noisePosY + 1) * (this.noiseSizeX + 1) * (this.noiseSizeZ + 1));
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
                //for (int y = this.worldHeight - Math.abs(this.minY) - 1; y >= this.minY; y--) {
                for (int y = this.worldHeight - Math.abs(this.minY) - 1; y >= 0; y--) {

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
        
        StructureWeightSampler structureWeightSampler = new StructureWeightSampler(structureAccessor, chunk);
        AquiferSampler aquiferSampler = this.createAquiferSampler(chunk.getPos().x, chunk.getPos().z);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        double[] heightNoise = this.heightNoisePool.borrowArr();
        this.generateHeightmap(heightNoise, chunk.getPos().x * this.noiseSizeX, 0, chunk.getPos().z * this.noiseSizeZ);

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
    }

    private void generateHeightmap(double[] heightNoise, int x, int y, int z) {
        // For accurate terrain shape, worldHeight + minY should equal 128.
        int noiseResolutionY = this.noisePosY + 1;
        int noiseResolutionX = this.noiseSizeX + 1;
        int noiseResolutionZ = this.noiseSizeZ + 1;

        int startX = x / this.noiseSizeX * 16;
        int startZ = z / this.noiseSizeZ * 16;
        
        BetaClimateSampler climateSampler = BetaClimateSampler.INSTANCE;
        
        // Var names taken from old customized preset names
        double coordinateScale = 684.41200000000003D * this.xzScale; 
        double heightScale = 684.41200000000003D * this.yScale;

        double depthNoiseScaleX = 200D;
        double depthNoiseScaleZ = 200D;
        double depthNoiseScaleExponent = 0.5D;

        double mainNoiseScaleX = this.xzFactor; // Default: 80
        double mainNoiseScaleY = this.yFactor;  // Default: 160
        double mainNoiseScaleZ = this.xzFactor;

        double lowerLimitScale = 512D;
        double upperLimitScale = 512D;
        
        //double baseSize = noiseResolutionY / 2D; // Or: 17 / 2D = 8.5
        double baseSize = 8.5D;
        double heightStretch = 12D;
        
        double[] scaleNoise = this.twoDNoisePool.borrowArr();
        double[] depthNoise = this.twoDNoisePool.borrowArr();
        
        double[] mainNoise = this.threeDNoisePool.borrowArr();
        double[] minLimitNoise = this.threeDNoisePool.borrowArr();
        double[] maxLimitNoise = this.threeDNoisePool.borrowArr();

        // Scale and Depth noise sample in 2D, noiseResolutionX * noiseResolutionZ
        
        scaleNoiseOctaves.sampleArrBeta(
            scaleNoise, 
            x, z, 
            noiseResolutionX, noiseResolutionZ, 
            1.121D, 1.121D, 0.5D
        );
        
        depthNoiseOctaves.sampleArrBeta(
            depthNoise, 
            x, z, 
            noiseResolutionX, noiseResolutionZ, 
            depthNoiseScaleX, depthNoiseScaleZ, depthNoiseScaleExponent
        );

        // Main, Min Limit, and Max Limit noise sample in 3D, noiseResolutionX * noiseResolutionY * noiseResolutionZ
        
        mainNoiseOctaves.sampleArrBeta(
            mainNoise, 
            x, y, z, 
            noiseResolutionX, noiseResolutionY, noiseResolutionZ,
            coordinateScale / mainNoiseScaleX, 
            heightScale / mainNoiseScaleY, 
            coordinateScale / mainNoiseScaleZ
        );

        minLimitNoiseOctaves.sampleArrBeta(
            minLimitNoise, 
            x, y, z, 
            noiseResolutionX, noiseResolutionY, noiseResolutionZ,
            coordinateScale, 
            heightScale, 
            coordinateScale
        );

        maxLimitNoiseOctaves.sampleArrBeta(
            maxLimitNoise, 
            x, y, z, 
            noiseResolutionX, noiseResolutionY, noiseResolutionZ,
            coordinateScale, 
            heightScale, 
            coordinateScale
        );

        int heightNoiseNdx = 0;
        int mainNoiseNdx = 0;
        int flatNoiseNdx = 0;
        int k = 16 / noiseResolutionX;

        for (int noiseX = 0; noiseX < noiseResolutionX; noiseX++) {
            int relX = noiseX * k + k / 2;

            for (int noiseZ = 0; noiseZ < noiseResolutionZ; noiseZ++) {
                int relZ = noiseZ * k + k / 2;
                
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
                    depthVal /= 8D;
                }

                if (scaleVal < 0.0D) {
                    scaleVal = 0.0D;
                }

                scaleVal += 0.5D;
                //depthVal = (depthVal * (double) noiseResolutionY) / 16D;
                //double depthVal2 = (double) noiseResolutionY / 2D + depthVal * 4D;
                depthVal = depthVal * baseSize / 8D;
                double depthVal2 = baseSize + depthVal * 4D;
                
                flatNoiseNdx++;

                for (int noiseY = this.noiseMinY; noiseY < noiseResolutionY; noiseY++) {
                    double heightVal = 0.0D;
                    double heightOffset = (((double) noiseY - depthVal2) * heightStretch) / scaleVal;
                    
                    if (heightOffset < 0.0D) {
                        heightOffset *= 4D;
                    }
                    
                    // Equivalent to current MC noise.sample() function, see NoiseColumnSampler.
                    double minLimitVal = minLimitNoise[mainNoiseNdx] / lowerLimitScale;
                    double maxLimitVal = maxLimitNoise[mainNoiseNdx] / upperLimitScale;
                    double mainNoiseVal = (mainNoise[mainNoiseNdx] / 10D + 1.0D) / 2D;

                    if (mainNoiseVal < 0.0D) {
                        heightVal = minLimitVal;
                    } else if (mainNoiseVal > 1.0D) {
                        heightVal = maxLimitVal;
                    } else {
                        heightVal = minLimitVal + (maxLimitVal - minLimitVal) * mainNoiseVal;
                    }
                    
                    // Equivalent to current MC addition of density offset, see NoiseColumnSampler.
                    double heightValWithOffset = heightVal - heightOffset; 
                    
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
                    
                    heightNoise[heightNoiseNdx] = heightValWithOffset;
                    heightNoiseNdx++;
                    
                    // Very bad.
                    // Do not increment index used to retrieve noise values unless we're at or over 0,
                    // So that the height value array can be fully populated without having to actually sample values
                    // for entire subchunk range, which would (probably?) change terrain shape.
                    if (noiseY >= 0)
                        mainNoiseNdx++;
                }
            }
        }
        
        this.twoDNoisePool.returnArr(scaleNoise);
        this.twoDNoisePool.returnArr(depthNoise);
        
        this.threeDNoisePool.returnArr(maxLimitNoise);
        this.threeDNoisePool.returnArr(minLimitNoise);
        this.threeDNoisePool.returnArr(mainNoise);
    }

    private void sampleHeightmap(int sampleX, int sampleZ) {
        int noiseResolutionY = this.noiseSizeY + 1;
        int noiseResolutionXZ = this.noiseSizeX + 1;
        
        double lerpY = 1.0D / this.verticalNoiseResolution;
        double lerpXZ = 1.0D / this.horizontalNoiseResolution;

        int chunkX = sampleX >> 4;
        int chunkZ = sampleZ >> 4;

        double[] heightNoise = this.heightNoisePool.borrowArr();
        generateHeightmap(heightNoise, chunkX * this.noiseSizeX, 0, chunkZ * this.noiseSizeZ);

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
                            int x = subX + subChunkX * this.horizontalNoiseResolution;
                            
                            double density = curNW; // var15
                            double progress = (curSW - curNW) * lerpXZ; 

                            for (int subZ = 0; subZ < this.horizontalNoiseResolution; subZ++) {
                                int z = subChunkZ * this.horizontalNoiseResolution + subZ;
                                
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
    
}
