package com.bespectacled.modernbeta.world.gen.provider;

import java.util.Random;

import com.bespectacled.modernbeta.api.world.gen.NoiseChunkProvider;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BlockColumnHolder;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.GenUtil;
import com.bespectacled.modernbeta.util.chunk.HeightmapChunk;
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
        
        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;
        
        int bedrockFloor = this.minY + this.bedrockFloor;
        
        Random rand = this.createSurfaceRandom(chunkX, chunkZ);
        Random bedrockRand = this.createSurfaceRandom(chunkX, chunkZ);
        BlockPos.Mutable pos = new BlockPos.Mutable();

        AquiferSampler aquiferSampler = this.getAquiferSampler(chunk);
        HeightmapChunk heightmapChunk = this.getHeightmapChunk(chunkX, chunkZ);
        BlockColumnHolder blockColumn = new BlockColumnHolder(chunk);
        
        for (int localX = 0; localX < 16; ++localX) {
            for (int localZ = 0; localZ < 16; ++localZ) {
                int x = (chunkX << 4) + localX;
                int z = (chunkZ << 4) + localZ;
                int topY = GenUtil.getLowestSolidHeight(chunk, this.worldHeight, this.minY, localX, localZ, this.defaultFluid) + 1;
                int surfaceMinY = (this.generateNoiseCaves || this.generateNoodleCaves) ? 
                    heightmapChunk.getHeight(x, z, HeightmapChunk.Type.SURFACE_FLOOR) - 8 : 
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
                
                Biome biome = biomeSource.getBiomeForSurfaceGen(region, pos.set(x, topY, z));
                
                BlockState biomeTopBlock = biome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                BlockState biomeFillerBlock = biome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();
                
                BlockState topBlock = biomeTopBlock;
                BlockState fillerBlock = biomeFillerBlock;
                
                boolean usedCustomSurface = this.useCustomSurfaceBuilder(biome, biomeSource.getBiomeRegistry().getId(biome), region, chunk, rand, pos, blockColumn);
                
                for (int y = this.worldTopY - 1; y >= this.minY; --y) {
                    pos.set(localX, y, localZ);
                    
                    // Randomly place bedrock from y=0 to y=5
                    if (y <= bedrockFloor + bedrockRand.nextInt(5)) {
                        chunk.setBlockState(pos, BlockStates.BEDROCK, false);
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
                    
                    BlockState blockState = chunk.getBlockState(pos);
                    
                    if (blockState.isAir()) {
                        flag = -1;
                        
                    } else if (blockState.isOf(this.defaultBlock.getBlock())) {
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
                            
                            flag = surfaceDepth;
                            
                            if (y < this.seaLevel && topBlock.isAir()) { // Generate water bodies
                                BlockState fluidBlock = aquiferSampler.apply(x, y, z, 0.0, 0.0);
                                
                                boolean isAir = fluidBlock == null;
                                topBlock = isAir ? BlockStates.AIR : fluidBlock;
                                
                                this.scheduleFluidTick(chunk, aquiferSampler, pos, topBlock);
                            }
                            
                            blockState = (y >= this.seaLevel - 1 || (y < this.seaLevel - 1 && chunk.getBlockState(pos.up()).isAir())) ? 
                                topBlock : 
                                fillerBlock;
                            
                            chunk.setBlockState(pos, blockState, false);
                            
                        } else if (flag > 0) {
                            --flag;
                            chunk.setBlockState(pos, fillerBlock, false);
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
            
            // Sample without noise caves
            double heightmapDensity = density;
            
            // Sample for noise caves
            density = this.sampleNoiseCave(density, 200.0, noiseX, noiseY, noiseZ);
            
            // Apply slides
            density = this.applyBottomSlide(density, noiseY, -3);
            heightmapDensity = this.applyBottomSlide(heightmapDensity, noiseY, -3);
            
            primaryBuffer[y] = density;
            heightmapBuffer[y] = heightmapDensity;
        }
    }
    
    private double getOffset(int noiseY, double baseSize, double heightStretch) {
        double offset = ((double) noiseY - baseSize) * heightStretch;
        
        if (offset < 0.0)
            offset *= 2.0;
        
        return offset;
    }

}
