package com.bespectacled.modernbeta.gen.provider;

import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.gen.OldGeneratorSettings;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.DoubleArrayPool;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.AquiferSampler;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.StructureWeightSampler;

/**
 * 
 * @author Paulevs
 *
 */
public class InfdevChunkProvider extends AbstractChunkProvider {
    
    private final PerlinOctaveNoise minLimitNoiseOctaves;
    private final PerlinOctaveNoise maxLimitNoiseOctaves;
    private final PerlinOctaveNoise mainNoiseOctaves;
    private final PerlinOctaveNoise beachNoiseOctaves;
    private final PerlinOctaveNoise stoneNoiseOctaves;
    private final PerlinOctaveNoise forestNoiseOctaves;
    
    private final DoubleArrayPool heightNoisePool;
    
    public InfdevChunkProvider(long seed, OldGeneratorSettings settings) {
        //super(seed, settings);
        super(seed, -64, 192, 64, 0, -10, 1, 1, 1.0, 1.0, 80, 160, true, true, BlockStates.STONE, BlockStates.WATER, settings);
        
        // Noise Generators
        minLimitNoiseOctaves = new PerlinOctaveNoise(RAND, 16, true);
        maxLimitNoiseOctaves = new PerlinOctaveNoise(RAND, 16, true);
        mainNoiseOctaves = new PerlinOctaveNoise(RAND, 8, true);
        beachNoiseOctaves = new PerlinOctaveNoise(RAND, 4, true);
        stoneNoiseOctaves = new PerlinOctaveNoise(RAND, 4, true);
        new PerlinOctaveNoise(RAND, 5, true); // Unused in original source
        forestNoiseOctaves = new PerlinOctaveNoise(RAND, 5, true);
        
        // Noise pools
        this.heightNoisePool = new DoubleArrayPool(64, (this.noiseSizeY + 1) * this.noiseSizeX);

        setForestOctaves(forestNoiseOctaves);
    }

    @Override
    public Chunk provideChunk(StructureAccessor structureAccessor, Chunk chunk, OldBiomeSource biomeSource) {
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
        ChunkRandom sandstoneRand = this.createChunkRand(chunkX, chunkZ);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                int absX = (chunkX << 4) + x;
                int absZ = (chunkZ << 4) + z;
                
                boolean genSandBeach = this.beachNoiseOctaves.sample(
                    absX * thirtysecond, 
                    absZ * thirtysecond, 
                    0.0) + rand.nextDouble() * 0.2 > 0.0;
                
                boolean genGravelBeach = this.beachNoiseOctaves.sample(
                    absZ * thirtysecond, 
                    109.0134,
                    absX * thirtysecond) + rand.nextDouble() * 0.2 > 3.0;
                
                int genStone = (int)(this.stoneNoiseOctaves.sample(
                    absX * thirtysecond * 2.0, 
                    absZ * thirtysecond * 2.0) / 3.0 + 3.0 + rand.nextDouble() * 0.25);
                
                int flag = -1;
                
                Biome curBiome = getBiomeForSurfaceGen(mutable.set(absX, 0, absZ), region, biomeSource);
                
                BlockState biomeTopBlock = curBiome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                BlockState biomeFillerBlock = curBiome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();
                
                BlockState topBlock = biomeTopBlock;
                BlockState fillerBlock = biomeFillerBlock;
                
                for (int y = this.worldHeight - Math.abs(this.minY) - 1; y >= 0; --y) {
                    
                    // Randomly place bedrock from y=0 to y=5
                    if (y <= this.minY + rand.nextInt(5)) {
                        chunk.setBlockState(mutable.set(x, y, z), Blocks.BEDROCK.getDefaultState(), false);
                        continue;
                    }
                    
                    // Don't surface build below 50, per 1.17 default surface builder
                    if ((this.generateAquifers || this.generateNoiseCaves) && y < 50) {
                        continue;
                    }
                    
                    mutable.set(x, y, z);
                    BlockState someBlock = chunk.getBlockState(mutable);
                    
                    if (someBlock.equals(BlockStates.AIR)) {
                        flag = -1;
                        
                    } else if (someBlock.equals(this.defaultBlock)) {
                        if (flag == -1) {
                            if (genStone <= 0) {
                                topBlock = BlockStates.AIR;
                                fillerBlock = this.defaultBlock;
                                
                            } else if (y >= this.seaLevel - 4 && y <= this.seaLevel + 1) {
                                topBlock = biomeTopBlock;
                                fillerBlock = biomeFillerBlock;
                                
                                if (genGravelBeach) {
                                    topBlock = BlockStates.AIR;
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
                            
                            flag = genStone;
                            
                            if (y >= this.seaLevel - 1) {
                                chunk.setBlockState(mutable, topBlock, false);
                            } else {
                                chunk.setBlockState(mutable, fillerBlock, false);
                            }
                            
                        } else if (flag > 0) {
                            --flag;
                            chunk.setBlockState(mutable, fillerBlock, false);
                            
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
        }
        
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
        //double[] heightNoise = new double[(this.noiseSizeY + 1) * this.noiseSizeX];
        double[] heightNoise = this.heightNoisePool.borrowArr();
        
        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        Heightmap heightmapOCEAN = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSURFACE = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
        
        StructureWeightSampler structureWeightSampler = new StructureWeightSampler(structureAccessor, chunk);
        AquiferSampler aquiferSampler = this.createAquiferSampler(chunk.getPos().x, chunk.getPos().z);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        for (int subChunkX = 0; subChunkX < this.noiseSizeX; ++subChunkX) {
            for (int subChunkZ = 0; subChunkZ < this.noiseSizeZ; ++ subChunkZ) {
                int bX = chunkX * this.noiseSizeX + subChunkX;
                int bZ = chunkZ * this.noiseSizeZ + subChunkZ;
                
                for (int bY = 0; bY < this.noiseSizeY + 1; ++bY) {
                    int offsetY = bY + this.noiseMinY;
                    
                    heightNoise[bY * this.noiseSizeX + 0] = this.generateHeightmap(bX, offsetY, bZ);
                    heightNoise[bY * this.noiseSizeX + 1] = this.generateHeightmap(bX, offsetY, bZ + 1);
                    heightNoise[bY * this.noiseSizeX + 2] = this.generateHeightmap(bX + 1, offsetY, bZ);
                    heightNoise[bY * this.noiseSizeX + 3] = this.generateHeightmap(bX + 1, offsetY, bZ + 1);
                }
                
                for (int subChunkY = 0; subChunkY < this.noiseSizeY; ++subChunkY) {
                    
                    double lower0, lower1, lower2, lower3;
                    double upper0, upper1, upper2, upper3;
                    
                    lower0 = heightNoise[(subChunkY) * this.noiseSizeX + 0];
                    lower1 = heightNoise[(subChunkY) * this.noiseSizeX + 1];
                    lower2 = heightNoise[(subChunkY) * this.noiseSizeX + 2];
                    lower3 = heightNoise[(subChunkY) * this.noiseSizeX + 3];
                    
                    upper0 = heightNoise[(subChunkY + 1) * this.noiseSizeX + 0];
                    upper1 = heightNoise[(subChunkY + 1) * this.noiseSizeX + 1];
                    upper2 = heightNoise[(subChunkY + 1) * this.noiseSizeX + 2];
                    upper3 = heightNoise[(subChunkY + 1) * this.noiseSizeX + 3];
                    
                    for (int subY = 0; subY < this.verticalNoiseResolution; ++subY) {
                        int y = subChunkY * this.verticalNoiseResolution + subY;
                        y += this.minY;
                        
                        double lerpY = subY / (double)this.verticalNoiseResolution;
                        
                        double nx1 = lower0 + (upper0 - lower0) * lerpY;
                        double nx2 = lower1 + (upper1 - lower1) * lerpY;
                        double nx3 = lower2 + (upper2 - lower2) * lerpY;
                        double nx4 = lower3 + (upper3 - lower3) * lerpY;
                        
                        for (int subX = 0; subX < this.horizontalNoiseResolution; ++subX) {
                            int x = subX + subChunkX * this.horizontalNoiseResolution;
                            int absX = (chunk.getPos().x << 4) + x;
                            
                            double lerpX = subX / (double)this.horizontalNoiseResolution;
                            
                            double nz1 = nx1 + (nx3 - nx1) * lerpX;
                            double nz2 = nx2 + (nx4 - nx2) * lerpX;
                            
                            for (int subZ = 0; subZ < this.horizontalNoiseResolution; ++subZ) {
                                int z = subZ + subChunkZ * this.horizontalNoiseResolution;
                                int absZ = (chunk.getPos().z << 4) + z;
                                
                                double lerpZ = subZ / (double)this.horizontalNoiseResolution;
                                
                                double density = nz1 + (nz2 - nz1) * lerpZ;
                                
                                //BlockState blockToSet = getBlockState(structureWeightSampler, absX, y, absZ, density);
                                BlockState blockToSet = this.getBlockState(structureWeightSampler, aquiferSampler, absX, y, absZ, density);
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
    
    private double generateHeightmap(int x, int y, int z) {
        // Check if y (in scaled space) is below sealevel
        // and increase density accordingly.
        //double elevGrad = y * 4.0 - 64.0;
        double heightOffset = y * this.verticalNoiseResolution - (double)this.seaLevel;
        if (heightOffset < 0.0) {
            heightOffset *= 3.0;
        }
        
        double coordinateScale = 684.412D * this.xzScale; 
        double heightScale = 984.412D * this.yScale;
        
        double limitScale = 512.0D;
        
        double heightVal;
        double mainNoiseVal = this.mainNoiseOctaves.sample(x * 8.55515, y * 1.71103, z * 8.55515) / 2.0;
        
        if (mainNoiseVal < -1) { // Lower limit(?)
            heightVal = MathHelper.clamp(
                this.minLimitNoiseOctaves.sample(x * coordinateScale, y * heightScale, z * coordinateScale) / limitScale - heightOffset, 
                -10.0, 
                10.0
            );
            
        } else if (mainNoiseVal > 1.0) { // Upper limit(?)
            heightVal = MathHelper.clamp(
                this.maxLimitNoiseOctaves.sample(x * coordinateScale, y * heightScale, z * coordinateScale) / limitScale - heightOffset, 
                -10.0, 
                10.0
            );
            
        } else {
            double minLimitVal = MathHelper.clamp(
                this.minLimitNoiseOctaves.sample(x * coordinateScale, y * heightScale, z * coordinateScale) / limitScale - heightOffset, 
                -10.0, 
                10.0
            );
            
            double maxLimitVal = MathHelper.clamp(
                this.maxLimitNoiseOctaves.sample(x * coordinateScale, y * heightScale, z * coordinateScale) / limitScale - heightOffset, 
                -10.0, 
                10.0
            );
            
            double mix = (mainNoiseVal + 1.0) / 2.0;
            
            heightVal = minLimitVal + (maxLimitVal - minLimitVal) * mix;
        }
        
        heightVal = this.sampleNoiseCave(
            (int)x * this.horizontalNoiseResolution,
            (int)y * this.verticalNoiseResolution,
            (int)z * this.horizontalNoiseResolution,
            this.maxLimitNoiseOctaves.sample(x * coordinateScale, y * heightScale, z * coordinateScale) / limitScale,
            heightVal
        );
        
        if (this.generateAquifers || this.generateNoiseCaves)
            heightVal = this.applyBottomSlide(heightVal, y);
    
        return heightVal;
    }
    
    private int sampleHeightmap(int sampleX, int sampleZ) {
        //double[] heightNoise = new double[(this.noiseSizeY + 1) * this.noiseSizeX];
        double[] heightNoise = this.heightNoisePool.borrowArr();
        
        int noiseX = MathHelper.floorDiv(sampleX, this.horizontalNoiseResolution);
        int noiseZ = MathHelper.floorDiv(sampleZ, this.horizontalNoiseResolution);
        
        int modX = MathHelper.floorMod(sampleX, this.horizontalNoiseResolution);
        int modZ = MathHelper.floorMod(sampleZ, this.horizontalNoiseResolution);
        
        double lerpX = modX / (double)this.horizontalNoiseResolution;
        double lerpZ = modZ / (double)this.horizontalNoiseResolution;
        
        for (int noiseY = 0; noiseY < this.noiseSizeY + 1; ++noiseY) {
            int offsetY = noiseY + this.noiseMinY;
            
            heightNoise[noiseY * this.noiseSizeX + 0] = this.generateHeightmap(noiseX, offsetY, noiseZ);
            heightNoise[noiseY * this.noiseSizeX + 1] = this.generateHeightmap(noiseX, offsetY, noiseZ + 1);
            heightNoise[noiseY * this.noiseSizeX + 2] = this.generateHeightmap(noiseX + 1, offsetY, noiseZ);
            heightNoise[noiseY * this.noiseSizeX + 3] = this.generateHeightmap(noiseX + 1, offsetY, noiseZ + 1);
        }
        
        for (int subChunkY = this.noiseSizeY - 1; subChunkY >= 0; --subChunkY) {
            double lower0 = heightNoise[(subChunkY) * this.noiseSizeX + 0];
            double lower1 = heightNoise[(subChunkY) * this.noiseSizeX + 1];
            double lower2 = heightNoise[(subChunkY) * this.noiseSizeX + 2];
            double lower3 = heightNoise[(subChunkY) * this.noiseSizeX + 3];
            
            double upper0 = heightNoise[(subChunkY + 1) * this.noiseSizeX + 0];
            double upper1 = heightNoise[(subChunkY + 1) * this.noiseSizeX + 1];
            double upper2 = heightNoise[(subChunkY + 1) * this.noiseSizeX + 2];
            double upper3 = heightNoise[(subChunkY + 1) * this.noiseSizeX + 3];
            
            for (int subY = this.verticalNoiseResolution - 1; subY >= 0; --subY) {
                int y = subChunkY * this.verticalNoiseResolution + subY;
                y += this.minY;
                        
                double lerpY = subY / (double)this.verticalNoiseResolution;
                
                double density = MathHelper.lerp3(lerpY, lerpX, lerpZ, lower0, upper0, lower2, upper2, lower1, upper1, lower3, upper3);
                
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
