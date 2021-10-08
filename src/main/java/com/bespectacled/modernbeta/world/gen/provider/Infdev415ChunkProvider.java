package com.bespectacled.modernbeta.world.gen.provider;

import java.util.Random;

import com.bespectacled.modernbeta.api.world.gen.NoiseChunkProvider;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.GenUtil;
import com.bespectacled.modernbeta.util.chunk.HeightmapChunk;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;
import com.bespectacled.modernbeta.world.spawn.BeachSpawnLocator;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.AquiferSampler;

public class Infdev415ChunkProvider extends NoiseChunkProvider {
    private final PerlinOctaveNoise minLimitNoiseOctaves;
    private final PerlinOctaveNoise maxLimitNoiseOctaves;
    private final PerlinOctaveNoise mainNoiseOctaves;
    private final PerlinOctaveNoise beachNoiseOctaves;
    private final PerlinOctaveNoise surfaceNoiseOctaves;
    private final PerlinOctaveNoise forestNoiseOctaves;
    
    public Infdev415ChunkProvider(OldChunkGenerator chunkGenerator) {
        super(chunkGenerator);
        //super(chunkGenerator, 0, 128, 64, 50, 0, -10, BlockStates.STONE, BlockStates.WATER, 1, 1, 1.0, 1.0, 80, 400, -10, 3, 0, 15, 3, 0, false, false, false, false, false);
        
        // Noise Generators
        this.minLimitNoiseOctaves = new PerlinOctaveNoise(rand, 16, true);
        this.maxLimitNoiseOctaves = new PerlinOctaveNoise(rand, 16, true);
        this.mainNoiseOctaves = new PerlinOctaveNoise(rand, 8, true);
        this.beachNoiseOctaves = new PerlinOctaveNoise(rand, 4, true);
        this.surfaceNoiseOctaves = new PerlinOctaveNoise(rand, 4, true);
        new PerlinOctaveNoise(rand, 5, true); // Unused in original source
        this.forestNoiseOctaves = new PerlinOctaveNoise(rand, 5, true);

        setForestOctaves(forestNoiseOctaves);
        
        this.spawnLocator = new BeachSpawnLocator(this, this.beachNoiseOctaves);
    }

    @Override
    public void provideSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource) {
        double eighth = 0.03125D;
        
        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;
        
        int bedrockFloor = this.minY + this.bedrockFloor;
        
        Random rand = this.createSurfaceRandom(chunkX, chunkZ);
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        AquiferSampler aquiferSampler = this.createAquiferSampler(this.noiseMinY, this.noiseTopY, chunkPos);
        
        for (int localX = 0; localX < 16; ++localX) {
            for (int localZ = 0; localZ < 16; ++localZ) {
                int x = (chunkX << 4) + localX;
                int z = (chunkZ << 4) + localZ;
                int topY = GenUtil.getLowestSolidHeight(chunk, this.worldHeight, this.minY, localX, localZ, this.defaultFluid) + 1;
                int surfaceMinY = (this.generateNoiseCaves || this.generateNoodleCaves) ? 
                    this.getHeight(x, z, HeightmapChunk.Type.SURFACE_FLOOR) - 8 : 
                    this.minY;
                
                boolean genSandBeach = this.beachNoiseOctaves.sample(
                    x * eighth, 
                    z * eighth, 
                    0.0) + rand.nextDouble() * 0.2 > 0.0;
                
                boolean genGravelBeach = this.beachNoiseOctaves.sample(
                    z * eighth, 
                    109.0134,
                    x * eighth) + rand.nextDouble() * 0.2 > 3.0;
                
                double surfaceNoise = this.surfaceNoiseOctaves.sample(x * eighth * 2.0, z * eighth * 2.0);
                int surfaceDepth = (int)(surfaceNoise / 3.0 + 3.0 + rand.nextDouble() * 0.25);
                
                int flag = -1;
                
                Biome biome = biomeSource.getBiomeForSurfaceGen(region, mutable.set(x, topY, z));
                
                BlockState biomeTopBlock = biome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                BlockState biomeFillerBlock = biome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();
                
                BlockState topBlock = biomeTopBlock;
                BlockState fillerBlock = biomeFillerBlock;
                
                boolean usedCustomSurface = this.useCustomSurfaceBuilder(biome, biomeSource.getBiomeRegistry().getId(biome), region, chunk, rand, mutable);
                
                for (int y = this.worldTopY - 1; y >= this.minY; --y) {
                    
                    // Randomly place bedrock from y=0 to y=5
                    if (y <= bedrockFloor + rand.nextInt(5)) {
                        chunk.setBlockState(mutable.set(localX, y, localZ), Blocks.BEDROCK.getDefaultState(), false);
                        continue;
                    }
                    
                    // Skip if at surface min y
                    if (y < surfaceMinY) {
                        continue;
                    }
                    
                    // Skip if used custom surface generation.
                    if (usedCustomSurface) {
                        continue;
                    }
                    
                    mutable.set(localX, y, localZ);
                    BlockState blockState = chunk.getBlockState(mutable);
                    
                    if (blockState.equals(BlockStates.AIR)) {
                        flag = -1;
                        
                    } else if (blockState.equals(this.defaultBlock)) {
                        if (flag == -1) {
                            if (surfaceDepth <= 0) {
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
                                BlockState fluidBlock = aquiferSampler.apply(x, y, z, 0.0, 0.0);
                                
                                topBlock = fluidBlock == null ? BlockStates.AIR : fluidBlock;
                            }
                            
                            flag = surfaceDepth;
                            
                            if (y >= this.seaLevel - 1) {
                                chunk.setBlockState(mutable, topBlock, false);
                            } else {
                                chunk.setBlockState(mutable, fillerBlock, false);
                            }
                            
                        } else if (flag > 0) {
                            --flag;
                            chunk.setBlockState(mutable, fillerBlock, false);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    protected void sampleNoiseColumn(double[] primaryBuffer, double[] heightmapBuffer, int startNoiseX, int startNoiseZ, int localNoiseX, int localNoiseZ) {
        int noiseX = startNoiseX + localNoiseX;
        int noiseZ = startNoiseZ + localNoiseZ;
        
        for (int y = 0; y < primaryBuffer.length; ++y) {
            int noiseY = y + this.noiseMinY;
            
            double densityOffset = this.getOffset(noiseY);
            
            double coordinateScale = 684.412D * this.xzScale; 
            double heightScale = 984.412D * this.yScale;
            
            double mainNoiseScaleX = this.xzFactor; // Default: 80
            double mainNoiseScaleY = this.yFactor;  // Default: 400
            double mainNoiseScaleZ = this.xzFactor;
            
            double limitScale = 512.0D;
            
            double density;
            
            // Default values: 8.55515, 1.71103, 8.55515
            double mainNoiseVal = this.mainNoiseOctaves.sample(
                noiseX * coordinateScale / mainNoiseScaleX, 
                noiseY * coordinateScale / mainNoiseScaleY, 
                noiseZ * coordinateScale / mainNoiseScaleZ
            ) / 2.0;
            
            // Do not clamp noise if generating with noise caves!
            if (mainNoiseVal < -1) {
                density = this.minLimitNoiseOctaves.sample(
                    noiseX * coordinateScale, 
                    noiseY * heightScale, 
                    noiseZ * coordinateScale
                ) / limitScale - densityOffset;
                
            } else if (mainNoiseVal > 1.0) {
                density = this.maxLimitNoiseOctaves.sample(
                    noiseX * coordinateScale, 
                    noiseY * heightScale, 
                    noiseZ * coordinateScale
                ) / limitScale - densityOffset;
                
            } else {
                double minLimitVal = this.minLimitNoiseOctaves.sample(
                    noiseX * coordinateScale, 
                    noiseY * heightScale, 
                    noiseZ * coordinateScale
                ) / limitScale - densityOffset;
                
                double maxLimitVal = this.maxLimitNoiseOctaves.sample(
                    noiseX * coordinateScale, 
                    noiseY * heightScale, 
                    noiseZ * coordinateScale
                ) / limitScale - densityOffset;
                                
                double delta = (mainNoiseVal + 1.0) / 2.0;
                density = minLimitVal + (maxLimitVal - minLimitVal) * delta;
            };
            
            // Sample without noise caves
            double heightmapDensity = density;
            
            // Sample for noise caves
            density = this.sampleNoiseCave(density, 8.5, noiseX, noiseY, noiseZ);
            
            // Apply slides
            density = this.applyBottomSlide(density, noiseY, -3);
            heightmapDensity = this.applyBottomSlide(heightmapDensity, noiseY, -3);
            
            primaryBuffer[y] = density;
            heightmapBuffer[y] = heightmapDensity;
        }
    }
    
    private double getOffset(int noiseY) {
        // Check if y (in scaled space) is below sealevel
        // and increase density accordingly.
        //double offset = y * 4.0 - 64.0;
        double offset = noiseY * this.verticalNoiseResolution - (double)this.seaLevel;
        
        if (offset < 0.0)
            offset *= 3.0;
        
        return offset;
    }
}
