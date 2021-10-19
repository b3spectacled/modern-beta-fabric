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
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

public class Infdev420ChunkProvider extends NoiseChunkProvider {
    private final PerlinOctaveNoise minLimitNoiseOctaves;
    private final PerlinOctaveNoise maxLimitNoiseOctaves;
    private final PerlinOctaveNoise mainNoiseOctaves;
    private final PerlinOctaveNoise beachNoiseOctaves;
    private final PerlinOctaveNoise surfaceNoiseOctaves;
    private final PerlinOctaveNoise forestNoiseOctaves;
    
    public Infdev420ChunkProvider(OldChunkGenerator chunkGenerator) {
        super(chunkGenerator);
        
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

        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        int bedrockFloor = this.minY + this.bedrockFloor;
        
        Random rand = this.createSurfaceRandom(chunkX, chunkZ);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        // Accurate beach/terrain patterns depend on z iterating before x,
        // and array accesses changing accordingly.
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int absX = (chunkX << 4) + x;
                int absZ = (chunkZ << 4) + z;
                int topY = GenUtil.getSolidHeight(chunk, this.worldHeight, this.minY, x, z, this.defaultFluid) + 1;
                
                boolean genSandBeach = this.beachNoiseOctaves.sample(absX * eighth, absZ * eighth, 0.0) + rand.nextDouble() * 0.2 > 0.0;
                boolean genGravelBeach = this.beachNoiseOctaves.sample(absZ * eighth, 109.0134, absX * eighth) + rand.nextDouble() * 0.2 > 3.0;
                int surfaceDepth = (int)(this.surfaceNoiseOctaves.sample(absX * eighth * 2.0, absX * eighth * 2.0) / 3.0 + 3.0 + rand.nextDouble() * 0.25);

                int flag = -1;
                
                Biome biome = biomeSource.getBiomeForSurfaceGen(region, mutable.set(absX, topY, absZ));

                BlockState biomeTopBlock = biome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                BlockState biomeFillerBlock = biome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();

                BlockState topBlock = biomeTopBlock;
                BlockState fillerBlock = biomeFillerBlock;

                boolean usedCustomSurface = this.useCustomSurfaceBuilder(biome, biomeSource.getBiomeRegistry().getId(biome), region, chunk, rand, mutable);
                
                // Generate from top to bottom of world
                for (int y = this.worldTopY - 1; y >= this.minY; y--) {

                    // Randomly place bedrock from y=0 to y=5
                    if (y <= bedrockFloor + rand.nextInt(6) - 1) {
                        chunk.setBlockState(mutable.set(x, y, z), BlockStates.BEDROCK, false);
                        continue;
                    }
                    
                    // TODO: As of 21w08b.
                    // Check to remove later, not accurate but temporary to ensure bottom layer of world is always bedrock.
                    // Game breaks during ore decoration breaks if any block at yMin is stone/deepslate
                    // since the game checks all adjacent blocks for a particular position,
                    // even if the downward direction is below the world limit!!
                    if (y <= this.minY) {
                        chunk.setBlockState(mutable.set(x, y, z), BlockStates.BEDROCK, false);
                        continue;
                    }
                    
                    // Don't surface build below 50, per 1.17 default surface builder
                    // Skip if used custom surface generation or if below minimum surface level.
                    if (usedCustomSurface || y < this.minSurfaceY) {
                        continue;
                    }

                    BlockState blockState = chunk.getBlockState(mutable.set(x, y, z));

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
                                topBlock = this.defaultFluid;
                            }

                            flag = surfaceDepth;
                            if (y >= this.seaLevel - 1) {
                                chunk.setBlockState(mutable.set(x, y, z), topBlock, false);
                            } else {
                                chunk.setBlockState(mutable.set(x, y, z), fillerBlock, false);
                            }
                            
                        } else if (flag > 0) { 
                            flag--;
                            chunk.setBlockState(mutable.set(x, y, z), fillerBlock, false);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void sampleNoiseColumn(double[] buffer, int startNoiseX, int startNoiseZ, int localNoiseX, int localNoiseZ) {
        int noiseX = startNoiseX + localNoiseX;
        int noiseZ = startNoiseZ + localNoiseZ;
        
        for (int y = 0; y < buffer.length; ++y) {
            int noiseY = y + this.noiseMinY;
            
            double coordinateScale = 684.412D * this.xzScale; 
            double heightScale = 684.412D * this.yScale;
            
            double mainNoiseScaleX = this.xzFactor; // Default: 80
            double mainNoiseScaleY = this.yFactor;  // Default: 160
            double mainNoiseScaleZ = this.xzFactor;
            
            double lowerLimitScale = 512.0D;
            double upperLimitScale = 512.0D;
            
            double baseSize = 8.5D;
            double heightStretch = 12D;
            
            double density;
            double densityOffset = this.getOffset(noiseY, baseSize, heightStretch);
            
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
            density = this.applyBottomSlide(density, noiseY, -3);
            
            buffer[y] = density;
        }
    }
    
    private double getOffset(int noiseY, double baseSize, double heightStretch) {
        double offset = ((double) noiseY - baseSize) * heightStretch;
        
        if (offset < 0.0)
            offset *= 2.0;
        
        return offset;
    }

}
