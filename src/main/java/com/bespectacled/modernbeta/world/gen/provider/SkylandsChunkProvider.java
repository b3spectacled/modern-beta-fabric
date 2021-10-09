package com.bespectacled.modernbeta.world.gen.provider;

import java.util.Random;

import com.bespectacled.modernbeta.api.world.gen.NoiseChunkProvider;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BlockColumnHolder;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.GenUtil;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

public class SkylandsChunkProvider extends NoiseChunkProvider {
    private final PerlinOctaveNoise minLimitNoiseOctaves;
    private final PerlinOctaveNoise maxLimitNoiseOctaves;
    private final PerlinOctaveNoise mainNoiseOctaves;
    private final PerlinOctaveNoise surfaceNoiseOctaves;
    private final PerlinOctaveNoise forestNoiseOctaves;
    
    public SkylandsChunkProvider(OldChunkGenerator chunkGenerator) {
        super(chunkGenerator);
        //super(chunkGenerator, 0, 128, 0, 0, 0, -10, BlockStates.STONE, BlockStates.AIR, 1, 2, 2.0, 1.0, 80, 160, -30, 31, 0, -30, 7, 0, false, false, false, false, false);
        
        // Noise Generators
        this.minLimitNoiseOctaves = new PerlinOctaveNoise(rand, 16, true);
        this.maxLimitNoiseOctaves = new PerlinOctaveNoise(rand, 16, true);
        this.mainNoiseOctaves = new PerlinOctaveNoise(rand, 8, true);
        new PerlinOctaveNoise(rand, 4, true);
        this.surfaceNoiseOctaves = new PerlinOctaveNoise(rand, 4, true);
        new PerlinOctaveNoise(rand, 10, true);
        new PerlinOctaveNoise(rand, 16, true);
        this.forestNoiseOctaves = new PerlinOctaveNoise(rand, 8, true);

        setForestOctaves(forestNoiseOctaves);
    }

    @Override
    public void provideSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource) {
        double eighth = 0.03125D;

        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;

        Random rand = this.createSurfaceRandom(chunkX, chunkZ);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        double[] surfaceNoise = this.createSurfaceArray();
        surfaceNoise = surfaceNoiseOctaves.sampleArrShelf(surfaceNoise, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1, eighth * 2D, eighth * 2D, eighth * 2D);

        BlockColumnHolder blockColumn = new BlockColumnHolder(chunk);
        
        for (int localZ = 0; localZ < 16; localZ++) {
            for (int localX = 0; localX < 16; localX++) {
                int x = (chunkX << 4) + localX; 
                int z = (chunkZ << 4) + localZ;
                int topY = GenUtil.getLowestSolidHeight(chunk, this.worldHeight, this.minY, localX, localZ, this.defaultFluid) + 1;

                int surfaceDepth = (int) (surfaceNoise[localZ + localX * 16] / 3D + 3D + rand.nextDouble() * 0.25D);
                int flag = -1;

                Biome biome = biomeSource.getBiomeForSurfaceGen(region, mutable.set(x, topY, z));
                
                BlockState biomeTopBlock = biome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                BlockState biomeFillerBlock = biome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();

                BlockState topBlock = biomeTopBlock;
                BlockState fillerBlock = biomeFillerBlock;
                
                boolean usedCustomSurface = this.useCustomSurfaceBuilder(biome, biomeSource.getBiomeRegistry().getId(biome), region, chunk, rand, mutable, blockColumn);

                // Generate from top to bottom of world
                for (int y = this.worldTopY - 1; y >= this.minY; y--) {
                    mutable.set(localX, y, localZ);
                    
                    // Skip if used custom surface generation or if below minimum surface level.
                    if (usedCustomSurface) {
                        continue;
                    }
                    
                    BlockState blockState = chunk.getBlockState(mutable);
                    
                    if (blockState.isAir()) { // Skip if air block
                        flag = -1;
                        continue;
                    }

                    if (!blockState.isOf(this.defaultBlock.getBlock())) { // Skip if not stone
                        continue;
                    }

                    if (flag == -1) {
                        if (surfaceDepth <= 0) { // Generate stone basin if noise permits
                            topBlock = BlockStates.AIR;
                            fillerBlock = this.defaultBlock;
                        }

                        flag = surfaceDepth;
                        
                        blockState = (y >= 0) ? 
                            topBlock : 
                            fillerBlock;
                        
                        chunk.setBlockState(mutable, blockState, false);

                        continue;
                    }

                    if (flag <= 0) {
                        continue;
                    }

                    flag--;
                    chunk.setBlockState(mutable, fillerBlock, false);

                    // Generates layer of sandstone starting at lowest block of sand, of height 1 to 4.
                    if (flag == 0 && fillerBlock.isOf(BlockStates.SAND.getBlock())) {
                        flag = rand.nextInt(4);
                        fillerBlock = BlockStates.SANDSTONE;
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
            
            // Var names taken from old customized preset names
            double coordinateScale = 684.412D * this.xzScale; 
            double heightScale = 684.412D * this.yScale;
            
            double mainNoiseScaleX = this.xzFactor; // Default: 80
            double mainNoiseScaleY = this.yFactor;  // Default: 160
            double mainNoiseScaleZ = this.xzFactor;

            double lowerLimitScale = 512D;
            double upperLimitScale = 512D;
            
            double density = 0.0D;
            double densityOffset = this.getOffset();
            
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
            density = this.applyTopSlide(density, noiseY, this.noiseSizeY);
            density = this.applyBottomSlide(density, noiseY, -8);
            
            heightmapDensity = this.applyTopSlide(heightmapDensity, noiseY, this.noiseSizeY);
            heightmapDensity = this.applyBottomSlide(heightmapDensity, noiseY, -8);
            
            primaryBuffer[y] = density;
            heightmapBuffer[y] = heightmapDensity;
        }
    }
    
    private double getOffset() {
        return 8D;
    }
}
