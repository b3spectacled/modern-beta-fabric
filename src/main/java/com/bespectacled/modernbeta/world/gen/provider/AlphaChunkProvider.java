package com.bespectacled.modernbeta.world.gen.provider;

import java.util.Random;

import com.bespectacled.modernbeta.api.world.gen.NoiseChunkProvider;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;
import com.bespectacled.modernbeta.world.spawn.BeachSpawnLocator;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

public class AlphaChunkProvider extends NoiseChunkProvider {
    private final PerlinOctaveNoise minLimitNoiseOctaves;
    private final PerlinOctaveNoise maxLimitNoiseOctaves;
    private final PerlinOctaveNoise mainNoiseOctaves;
    private final PerlinOctaveNoise beachNoiseOctaves;
    private final PerlinOctaveNoise surfaceNoiseOctaves;
    private final PerlinOctaveNoise scaleNoiseOctaves;
    private final PerlinOctaveNoise depthNoiseOctaves;
    private final PerlinOctaveNoise forestNoiseOctaves;
    
    public AlphaChunkProvider(OldChunkGenerator chunkGenerator) {
        super(chunkGenerator);

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
        double scale = 0.03125D;

        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        int bedrockFloor = this.worldMinY + this.bedrockFloor;
        
        Random rand = this.createSurfaceRandom(chunkX, chunkZ);
        BlockPos.Mutable pos = new BlockPos.Mutable();

        double[] sandNoise = beachNoiseOctaves.sampleAlpha(
            chunkX * 16, chunkZ * 16, 0.0D,
            16, 16, 1,
            scale, scale, 1.0D
        );
        
        double[] gravelNoise = beachNoiseOctaves.sampleAlpha(
            chunkZ * 16, 109.0134D, chunkX * 16,
            16, 1, 16,
            scale, 1.0D, scale
        );
        
        double[] surfaceNoise = surfaceNoiseOctaves.sampleAlpha(
            chunkX * 16, chunkZ * 16, 0.0D,
            16, 16, 1,
            scale * 2D, scale * 2D, scale * 2D
        );

        // Accurate beach/terrain patterns depend on z iterating before x,
        // and array accesses changing accordingly.
        for (int localX = 0; localX < 16; localX++) {
            for (int localZ = 0; localZ < 16; localZ++) {
                int x = (chunkX << 4) + localX;
                int z = (chunkZ << 4) + localZ;
                int surfaceTopY = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG).get(localX, localZ);
                
                boolean genSandBeach = sandNoise[localX + localZ * 16] + rand.nextDouble() * 0.2D > 0.0D;
                boolean genGravelBeach = gravelNoise[localX + localZ * 16] + rand.nextDouble() * 0.2D > 3D;
                int surfaceDepth = (int) (surfaceNoise[localX + localZ * 16] / 3D + 3D + rand.nextDouble() * 0.25D);

                int runDepth = -1;
                
                Biome biome = biomeSource.getBiomeForSurfaceGen(region, pos.set(x, surfaceTopY, z));

                BlockState biomeTopBlock = biome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                BlockState biomeFillerBlock = biome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();

                BlockState topBlock = biomeTopBlock;
                BlockState fillerBlock = biomeFillerBlock;

                boolean usedCustomSurface = this.useCustomSurfaceBuilder(biome, biomeSource.getBiomeRegistry().getId(biome), region, chunk, rand, pos);
                
                // Generate from top to bottom of world
                for (int y = this.worldTopY - 1; y >= this.worldMinY; y--) {

                    // Randomly place bedrock from y=0 to y=5
                    if (y <= bedrockFloor + rand.nextInt(6) - 1) {
                        chunk.setBlockState(pos.set(localX, y, localZ), BlockStates.BEDROCK, false);
                        continue;
                    }
                    
                    // TODO: As of 21w08b.
                    // Check to remove later, not accurate but temporary to ensure bottom layer of world is always bedrock.
                    // Game breaks during ore decoration breaks if any block at yMin is stone/deepslate
                    // since the game checks all adjacent blocks for a particular position,
                    // even if the downward direction is below the world limit!!
                    if (y <= this.worldMinY) {
                        chunk.setBlockState(pos.set(localX, y, localZ), BlockStates.BEDROCK, false);
                        continue;
                    }
                    
                    // Skip if used custom surface generation or if below minimum surface level.
                    if (usedCustomSurface || y < this.surfaceMinY) {
                        continue;
                    }

                    BlockState blockState = chunk.getBlockState(pos.set(localX, y, localZ));

                    if (blockState.equals(BlockStates.AIR)) { // Skip if air block
                        runDepth = -1;
                        continue;
                    }

                    if (!blockState.equals(this.defaultBlock)) { // Skip if not stone
                        continue;
                    }

                    if (runDepth == -1) {
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
                            topBlock = this.defaultFluid;
                        }

                        // Main surface builder section
                        runDepth = surfaceDepth;
                        if (y >= this.seaLevel - 1) {
                            chunk.setBlockState(pos.set(localX, y, localZ), topBlock, false);
                        } else {
                            chunk.setBlockState(pos.set(localX, y, localZ), fillerBlock, false);
                        }

                        continue;
                    }
                    
                    if (runDepth > 0) { 
                        runDepth--;
                        chunk.setBlockState(pos.set(localX, y, localZ), fillerBlock, false);
                    }
                }
            }
        }
    }
    
    @Override
    protected void sampleNoiseColumn(double[] buffer, int startNoiseX, int startNoiseZ, int localNoiseX, int localNoiseZ) {
        int noiseX = startNoiseX + localNoiseX;
        int noiseZ = startNoiseZ + localNoiseZ;

        double depthNoiseScaleX = 100D;
        double depthNoiseScaleZ = 100D;
        
        double coordinateScale = 684.412D * this.xzScale; 
        double heightScale = 684.412D * this.yScale;
        
        double mainNoiseScaleX = this.xzFactor; // Default: 80
        double mainNoiseScaleY = this.yFactor;  // Default: 160
        double mainNoiseScaleZ = this.xzFactor;

        double lowerLimitScale = 512D;
        double upperLimitScale = 512D;
        
        double baseSize = 8.5D;
        double heightStretch = 12D;
        
        double scale = this.scaleNoiseOctaves.sample(noiseX, 0, noiseZ, 1.0D, 0.0D, 1.0D);
        scale = (scale + 256D) / 512D;
        
        if (scale > 1.0D) {
            scale = 1.0D; 
        }

        double depth = this.depthNoiseOctaves.sample(noiseX, 0, noiseZ, depthNoiseScaleX, 0.0D, depthNoiseScaleZ);
        depth /= 8000D;
        
        if (depth < 0.0D) {
            depth = -depth;
        }

        depth = depth * 3D - 3D;

        if (depth < 0.0D) {
            depth /= 2D;
            if (depth < -1D) {
                depth = -1D;
            }

            depth /= 1.4D;
            depth /= 2D; // Omitting this creates the Infdev 20100611 generator.

            scale = 0.0D;

        } else {
            if (depth > 1.0D) {
                depth = 1.0D;
            }
            depth /= 6D;
        }

        scale += 0.5D;
        depth = depth * baseSize / 8D;
        depth = baseSize + depth * 4D;
        
        for (int y = 0; y < buffer.length; ++y) {
            int noiseY = y + this.noiseMinY;
            
            double density;
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
            
            // Sample for noise caves
            density = this.sampleNoiseCave(density, noiseX, noiseY, noiseZ);
            
            // Apply slides
            density = this.applySlides(density, y);
            
            buffer[y] = density;
        }
    }
    
    private double getOffset(int noiseY, double heightStretch, double depth, double scale) {
        double offset = (((double)noiseY - depth) * heightStretch) / scale;
        
        if (offset < 0D)
            offset *= 4D;
        
        return offset;
    }
}
