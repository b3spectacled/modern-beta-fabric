package com.bespectacled.modernbeta.world.gen.provider;

import java.util.Random;

import com.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import com.bespectacled.modernbeta.api.world.gen.NoiseChunkProvider;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.GenUtil;
import com.bespectacled.modernbeta.util.mersenne.MTRandom;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.biome.pe.climate.PEClimateSampler;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;
import com.bespectacled.modernbeta.world.spawn.PESpawnLocator;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

public class PEChunkProvider extends NoiseChunkProvider {
    private final PerlinOctaveNoise minLimitNoiseOctaves;
    private final PerlinOctaveNoise maxLimitNoiseOctaves;
    private final PerlinOctaveNoise mainNoiseOctaves;
    private final PerlinOctaveNoise beachNoiseOctaves;
    private final PerlinOctaveNoise surfaceNoiseOctaves;
    private final PerlinOctaveNoise scaleNoiseOctaves;
    private final PerlinOctaveNoise depthNoiseOctaves;
    private final PerlinOctaveNoise forestNoiseOctaves;
    
    private final ClimateSampler climateSampler;
    
    public PEChunkProvider(OldChunkGenerator chunkGenerator) {
        super(chunkGenerator);
        
        // Use Mersenne Twister random instead of Java random
        MTRandom mtRand = new MTRandom(chunkGenerator.worldSeed);
        
        // Noise Generators
        this.minLimitNoiseOctaves = new PerlinOctaveNoise(mtRand, 16, true);
        this.maxLimitNoiseOctaves = new PerlinOctaveNoise(mtRand, 16, true);
        this.mainNoiseOctaves = new PerlinOctaveNoise(mtRand, 8, true);
        this.beachNoiseOctaves = new PerlinOctaveNoise(mtRand, 4, true);
        this.surfaceNoiseOctaves = new PerlinOctaveNoise(mtRand, 4, true);
        this.scaleNoiseOctaves = new PerlinOctaveNoise(mtRand, 10, true);
        this.depthNoiseOctaves = new PerlinOctaveNoise(mtRand, 16, true);
        this.forestNoiseOctaves = new PerlinOctaveNoise(mtRand, 8, true);
        
        this.setForestOctaves(forestNoiseOctaves);
        
        // Get climate sampler from biome provider if exists,
        // else create new default PE climate sampler.
        this.climateSampler = 
            chunkGenerator.getBiomeSource() instanceof OldBiomeSource oldBiomeSource &&
            oldBiomeSource.getBiomeProvider() instanceof ClimateSampler climateSampler ?
                climateSampler :
                new PEClimateSampler(chunkGenerator.getWorldSeed());
        this.spawnLocator = new PESpawnLocator(this, this.beachNoiseOctaves);
    }
    
    @Override
    public void provideSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource) {
        double eighth = 0.03125D;

        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        int bedrockFloor = this.minY + this.bedrockFloor;
        
        Random rand = this.createSurfaceRandom(chunkX, chunkZ);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        double[] sandNoise = this.createSurfaceArray();
        double[] gravelNoise = this.createSurfaceArray();
        double[] surfaceNoise = this.createSurfaceArray();

        sandNoise = beachNoiseOctaves.sampleArrShelf(
            sandNoise, 
            chunkX * 16, chunkZ * 16, 0.0D, 
            16, 16, 1,
            eighth, eighth, 1.0D);
        
        gravelNoise = beachNoiseOctaves.sampleArrShelf(
            gravelNoise, 
            chunkX * 16, 109.0134D, chunkZ * 16, 
            16, 1, 16, 
            eighth, 1.0D, eighth);
        
        surfaceNoise = surfaceNoiseOctaves.sampleArrShelf(
            surfaceNoise, 
            chunkX * 16, chunkZ * 16, 0.0D, 
            16, 16, 1,
            eighth * 2D, eighth * 2D, eighth * 2D
        );

        for (int localZ = 0; localZ < 16; localZ++) {
            for (int localX = 0; localX < 16; localX++) {
                int x = (chunkX << 4) + localX;
                int z = (chunkZ << 4) + localZ;
                int topY = GenUtil.getLowestSolidHeight(chunk, this.worldHeight, this.minY, localX, localZ, this.defaultFluid) + 1;
                
                // MCPE uses nextFloat() instead of nextDouble()
                boolean genSandBeach = sandNoise[localZ + localX * 16] + rand.nextFloat() * 0.2D > 0.0D;
                boolean genGravelBeach = gravelNoise[localZ + localX * 16] + rand.nextFloat() * 0.2D > 3D;
                int surfaceDepth = (int) (surfaceNoise[localZ + localX * 16] / 3D + 3D + rand.nextFloat() * 0.25D);
                
                int flag = -1;
                
                Biome biome = biomeSource.getBiomeForSurfaceGen(region, mutable.set(x, topY, z));

                BlockState biomeTopBlock = biome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                BlockState biomeFillerBlock = biome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();

                BlockState topBlock = biomeTopBlock;
                BlockState fillerBlock = biomeFillerBlock;
                
                boolean usedCustomSurface = this.useCustomSurfaceBuilder(biome, biomeSource.getBiomeRegistry().getId(biome), region, chunk, rand, mutable);

                // Generate from top to bottom of world
                for (int y = this.worldTopY - 1; y >= this.minY; y--) {

                    // Randomly place bedrock from y=0 (or minHeight) to y=5
                    if (y <= bedrockFloor + rand.nextInt(5)) {
                        chunk.setBlockState(mutable.set(localX, y, localZ), BlockStates.BEDROCK, false);
                        continue;
                    }
                    
                    // Skip if used custom surface generation or if below minimum surface level.
                    if (usedCustomSurface) {
                        continue;
                    }

                    BlockState blockState = chunk.getBlockState(mutable.set(localX, y, localZ));

                    if (blockState.equals(BlockStates.AIR)) { // Skip if air block
                        flag = -1;
                        continue;
                    }

                    if (!blockState.equals(this.defaultBlock)) { // Skip if not stone
                        continue;
                    }

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

                        if (y < this.seaLevel && topBlock.equals(BlockStates.AIR)) { // Generate water bodies
                            topBlock = this.defaultFluid;
                        }

                        flag = surfaceDepth;
                        if (y >= this.seaLevel - 1) {
                            chunk.setBlockState(mutable.set(localX, y, localZ), topBlock, false);
                        } else {
                            chunk.setBlockState(mutable.set(localX, y, localZ), fillerBlock, false);
                        }

                        continue;
                    }

                    if (flag <= 0) {
                        continue;
                    }

                    flag--;
                    chunk.setBlockState(mutable.set(localX, y, localZ), fillerBlock, false);

                    // Generates layer of sandstone starting at lowest block of sand, of height 1 to 4.
                    if (flag == 0 && fillerBlock.equals(BlockStates.SAND)) {
                        flag = rand.nextInt(4);
                        fillerBlock = BlockStates.SANDSTONE;
                    }
                }
            }
        }
    }
    
    @Override
    protected void sampleNoiseColumn(double[] primaryBuffer, double[] heightmapBuffer, int startNoiseX, int startNoiseZ, int localNoiseX, int localNoiseZ) {
        int horizNoiseResolution = 16 / (this.noiseSizeX + 1);
        int x = (startNoiseX / this.noiseSizeX * 16) + localNoiseX * horizNoiseResolution + horizNoiseResolution / 2;
        int z = (startNoiseZ / this.noiseSizeZ * 16) + localNoiseZ * horizNoiseResolution + horizNoiseResolution / 2;
        
        int noiseX = startNoiseX + localNoiseX;
        int noiseZ = startNoiseZ + localNoiseZ;
        
        double depthNoiseScaleX = 200D;
        double depthNoiseScaleZ = 200D;
        
        //double baseSize = noiseResolutionY / 2D; // Or: 17 / 2D = 8.5
        double baseSize = 8.5D;
        
        double temp = this.climateSampler.sampleTemp(x, z);
        double rain = this.climateSampler.sampleRain(x, z) * temp;
        
        rain = 1.0D - rain;
        rain *= rain;
        rain *= rain;
        rain = 1.0D - rain;

        double scaleNoise = this.scaleNoiseOctaves.sample(noiseX, noiseZ, 1.121D, 1.121D);
        scaleNoise = (scaleNoise + 256D) / 512D;
        scaleNoise *= rain;
        
        if (scaleNoise > 1.0D) {
            scaleNoise = 1.0D;
        }
        
        double depthNoise = this.depthNoiseOctaves.sample(noiseX, noiseZ, depthNoiseScaleX, depthNoiseScaleZ);
        depthNoise /= 8000D;

        if (depthNoise < 0.0D) {
            depthNoise = -depthNoise * 0.3D;
        }

        depthNoise = depthNoise * 3D - 2D;

        if (depthNoise < 0.0D) {
            depthNoise /= 2D;

            if (depthNoise < -1D) {
                depthNoise = -1D;
            }

            depthNoise /= 1.4D;
            depthNoise /= 2D;

            scaleNoise = 0.0D;

        } else {
            if (depthNoise > 1.0D) {
                depthNoise = 1.0D;
            }
            depthNoise /= 8D;
        }

        if (scaleNoise < 0.0D) {
            scaleNoise = 0.0D;
        }

        scaleNoise += 0.5D;
        //depthVal = (depthVal * (double) noiseResolutionY) / 16D;
        //double depthVal2 = (double) noiseResolutionY / 2D + depthVal * 4D;
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
    
    /*
     * MCPE uses different values to seed random surface generation.
     */
    @Override
    protected Random createSurfaceRandom(int chunkX, int chunkZ) {
        long seed = (long)chunkX * 0x14609048 + (long)chunkZ * 0x7ebe2d5;
        
        return new MTRandom(seed);
    }
    
    private double getOffset(int noiseY, double heightStretch, double depth, double scale) {
        double offset = (((double)noiseY - depth) * heightStretch) / scale;
        
        if (offset < 0D)
            offset *= 4D;
        
        return offset;
    }
} 
