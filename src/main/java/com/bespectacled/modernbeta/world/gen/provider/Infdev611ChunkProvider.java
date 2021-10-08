package com.bespectacled.modernbeta.world.gen.provider;

import java.util.Random;

import com.bespectacled.modernbeta.api.world.gen.NoiseChunkProvider;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.GenUtil;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;
import com.bespectacled.modernbeta.world.spawn.BeachSpawnLocator;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.AquiferSampler;

public class Infdev611ChunkProvider extends NoiseChunkProvider {
    private final PerlinOctaveNoise minLimitNoiseOctaves;
    private final PerlinOctaveNoise maxLimitNoiseOctaves;
    private final PerlinOctaveNoise mainNoiseOctaves;
    private final PerlinOctaveNoise beachNoiseOctaves;
    private final PerlinOctaveNoise surfaceNoiseOctaves;
    private final PerlinOctaveNoise scaleNoiseOctaves;
    private final PerlinOctaveNoise depthNoiseOctaves;
    private final PerlinOctaveNoise forestNoiseOctaves;
    
    public Infdev611ChunkProvider(OldChunkGenerator chunkGenerator) {
        super(chunkGenerator);
        //super(chunkGenerator, 0, 128, 64, 50, 0, -10, BlockStates.STONE, BlockStates.WATER, 2, 1, 1.0, 1.0, 80, 160, -10, 3, 0, 15, 3, 0, false, false, false, false, false);
        
        // Noise Generators
        this.minLimitNoiseOctaves = new PerlinOctaveNoise(rand, 16, true);
        this.maxLimitNoiseOctaves = new PerlinOctaveNoise(rand, 16, true);
        this.mainNoiseOctaves = new PerlinOctaveNoise(rand, 8, true);
        this.beachNoiseOctaves = new PerlinOctaveNoise(rand, 4, true);
        this.surfaceNoiseOctaves = new PerlinOctaveNoise(rand, 4, true);
        this.scaleNoiseOctaves = new PerlinOctaveNoise(rand, 10, true);
        this.depthNoiseOctaves = new PerlinOctaveNoise(rand, 16, true);
        this.forestNoiseOctaves = new PerlinOctaveNoise(rand, 8, true);

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
        
        // Accurate beach/terrain patterns depend on z iterating before x,
        // and array accesses changing accordingly.
        for (int localX = 0; localX < 16; localX++) {
            for (int localZ = 0; localZ < 16; localZ++) {
                int x = (chunkX << 4) + localX;
                int z = (chunkZ << 4) + localZ;
                int topY = GenUtil.getLowestSolidHeight(chunk, this.worldHeight, this.minY, localX, localZ, this.defaultFluid) + 1;
                int surfaceMinY = (this.generateNoiseCaves || this.generateNoodleCaves) ? 50 : this.minY;
                
                boolean genSandBeach = this.beachNoiseOctaves.sample(x * eighth, z * eighth, 0.0) + rand.nextDouble() * 0.2 > 0.0;
                boolean genGravelBeach = this.beachNoiseOctaves.sample(z * eighth, 109.0134, x * eighth) + rand.nextDouble() * 0.2 > 3.0;
                int surfaceDepth = (int)(this.surfaceNoiseOctaves.sample(x * eighth * 2.0, x * eighth * 2.0) / 3.0 + 3.0 + rand.nextDouble() * 0.25);

                int flag = -1;
                
                Biome biome = biomeSource.getBiomeForSurfaceGen(region, mutable.set(x, topY, z));

                BlockState biomeTopBlock = biome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                BlockState biomeFillerBlock = biome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();

                BlockState topBlock = biomeTopBlock;
                BlockState fillerBlock = biomeFillerBlock;

                boolean usedCustomSurface = this.useCustomSurfaceBuilder(biome, biomeSource.getBiomeRegistry().getId(biome), region, chunk, rand, mutable);
                
                // Generate from top to bottom of world
                for (int y = this.worldTopY - 1; y >= this.minY; y--) {

                    // Randomly place bedrock from y=0 to y=5
                    if (y <= bedrockFloor + rand.nextInt(6) - 1) {
                        chunk.setBlockState(mutable.set(localX, y, localZ), BlockStates.BEDROCK, false);
                        continue;
                    }
                    
                    // TODO: As of 21w08b.
                    // Check to remove later, not accurate but temporary to ensure bottom layer of world is always bedrock.
                    // Game breaks during ore decoration breaks if any block at yMin is stone/deepslate
                    // since the game checks all adjacent blocks for a particular position,
                    // even if the downward direction is below the world limit!!
                    if (y <= this.minY) {
                        chunk.setBlockState(mutable.set(localX, y, localZ), BlockStates.BEDROCK, false);
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

                    BlockState blockState = chunk.getBlockState(mutable.set(localX, y, localZ));

                    if (blockState.equals(BlockStates.AIR)) { // Skip if air block
                        flag = -1;
                    } else if (blockState.equals(this.defaultBlock)) {
                        if (flag == -1) {
                            if (surfaceDepth <= 0) { // Generate stone basin if noise permits
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
                                BlockState fluidBlock = aquiferSampler.apply(x, y, z, 0.0, 0.0);
                                
                                topBlock = fluidBlock == null ? BlockStates.AIR : fluidBlock;
                            }

                            flag = surfaceDepth;
                            if (y >= this.seaLevel - 1) {
                                chunk.setBlockState(mutable.set(localX, y, localZ), topBlock, false);
                            } else {
                                chunk.setBlockState(mutable.set(localX, y, localZ), fillerBlock, false);
                            }
                            
                        } else if (flag > 0) { 
                            flag--;
                            chunk.setBlockState(mutable.set(localX, y, localZ), fillerBlock, false);
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

        double depthNoiseScaleX = 100D;
        double depthNoiseScaleZ = 100D;
        
        //double baseSize = noiseResolutionY / 2D; // Or: 17 / 2D = 8.5
        double baseSize = 8.5D;
        
        double scaleNoise = this.scaleNoiseOctaves.sample(noiseX, 0, noiseZ, 1.0D, 0.0D, 1.0D);
        scaleNoise = (scaleNoise + 256D) / 512D;
        
        if (scaleNoise > 1.0D) {
            scaleNoise = 1.0D; 
        }

        double depthNoise = this.depthNoiseOctaves.sample(noiseX, 0, noiseZ, depthNoiseScaleX, 0.0D, depthNoiseScaleZ);
        depthNoise /= 8000D;
        
        if (depthNoise < 0.0D) {
            depthNoise = -depthNoise;
        }

        depthNoise = depthNoise * 3D - 3D;

        if (depthNoise < 0.0D) {
            depthNoise /= 2D;
            if (depthNoise < -1D) {
                depthNoise = -1D;
            }

            depthNoise /= 1.4D;
            //depthNoise /= 2D; // Omitting this creates the Infdev 20100611 generator.

            scaleNoise = 0.0D;

        } else {
            if (depthNoise > 1.0D) {
                depthNoise = 1.0D;
            }
            depthNoise /= 6D;
        }

        scaleNoise += 0.5D;
        //depth0 = (depth0 * (double) noiseResolutionY) / 16D;
        //double depth1 = (double) noiseResolutionY / 2D + depth0 * 4D;\
        depthNoise = depthNoise * baseSize / 8D;
        depthNoise = baseSize + depthNoise * 4D;
        
        double scale = scaleNoise;
        double depth = depthNoise;
        
        for (int y = 0; y < primaryBuffer.length; ++y) {
            int noiseY = y + this.noiseMinY;
            
            // Var names taken from old customized preset names
            double coordinateScale = 684.412D * this.xzScale; 
            double heightScale = 684.412D * this.yScale;
            
            double mainNoiseScaleX = this.xzFactor; // Default: 80
            double mainNoiseScaleY = this.yFactor;  // Default: 160
            double mainNoiseScaleZ = this.xzFactor;

            double lowerLimitScale = 512D;
            double upperLimitScale = 512D;
            
            double heightStretch = 12D;
            
            double density = 0.0D;
            double densityOffset = this.getOffset(noiseY, heightStretch, depth, scale);
            
            // Equivalent to current MC noise.sample() function, see NoiseColumnSampler.
            double mainNoise = (this.mainNoiseOctaves.sample(
                noiseX, noiseY, noiseZ, 
                coordinateScale / mainNoiseScaleX, 
                heightScale / mainNoiseScaleY, 
                coordinateScale / mainNoiseScaleZ
            ) / 10D + 1.0D) / 2D;
            
            if (mainNoise < 0.0D) {
                density = this.minLimitNoiseOctaves.sample(
                    noiseX, noiseY, noiseZ, 
                    coordinateScale, 
                    heightScale, 
                    coordinateScale
                ) / lowerLimitScale;
                
            } else if (mainNoise > 1.0D) {
                density = this.maxLimitNoiseOctaves.sample(
                    noiseX, noiseY, noiseZ, 
                    coordinateScale, 
                    heightScale, 
                    coordinateScale
                ) / upperLimitScale;
                
            } else {
                double minLimitNoise = this.minLimitNoiseOctaves.sample(
                    noiseX, noiseY, noiseZ, 
                    coordinateScale, 
                    heightScale, 
                    coordinateScale
                ) / lowerLimitScale;
                
                double maxLimitNoise = this.maxLimitNoiseOctaves.sample(
                    noiseX, noiseY, noiseZ, 
                    coordinateScale, 
                    heightScale, 
                    coordinateScale
                ) / upperLimitScale;
                
                density = minLimitNoise + (maxLimitNoise - minLimitNoise) * mainNoise;
            }
            
            // Equivalent to current MC addition of density offset, see NoiseColumnSampler.
            density -= densityOffset; 
            
            // Sample without noise caves
            double heightmapDensity = density;
            
            // Sample for noise caves
            density = this.sampleNoiseCave(density, 170.0, noiseX, noiseY, noiseZ);
            
            // Apply slides
            density = this.applyTopSlide(density, noiseY, 4);
            density = this.applyBottomSlide(density, noiseY, -3);
            
            heightmapDensity = this.applyTopSlide(heightmapDensity, noiseY, 4);
            heightmapDensity = this.applyBottomSlide(heightmapDensity, noiseY, -3);
            
            primaryBuffer[y] = density;
            heightmapBuffer[y] = heightmapDensity;
        }
    }
    
    private double getOffset(int noiseY, double heightStretch, double depth, double scale) {
        double offset = (((double)noiseY - depth) * heightStretch) / scale;
        
        if (offset < 0D)
            offset *= 4D;
        
        return offset;
    }
}
