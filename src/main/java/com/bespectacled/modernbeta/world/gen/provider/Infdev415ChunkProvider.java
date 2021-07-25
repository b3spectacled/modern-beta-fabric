package com.bespectacled.modernbeta.world.gen.provider;

import com.bespectacled.modernbeta.api.world.gen.BeachSpawnable;
import com.bespectacled.modernbeta.api.world.gen.NoiseChunkProvider;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.GenUtil;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;

public class Infdev415ChunkProvider extends NoiseChunkProvider implements BeachSpawnable {
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
    }

    @Override
    public void provideSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource) {
        double thirtysecond = 0.03125D;
        
        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        int bedrockFloor = this.minY + this.bedrockFloor;
        
        ChunkRandom rand = this.createChunkRand(chunkX, chunkZ);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                int absX = (chunkX << 4) + x;
                int absZ = (chunkZ << 4) + z;
                int topY = GenUtil.getSolidHeight(chunk, this.worldHeight, this.minY, x, z, this.defaultFluid) + 1;
                
                boolean genSandBeach = this.beachNoiseOctaves.sample(
                    absX * thirtysecond, 
                    absZ * thirtysecond, 
                    0.0) + rand.nextDouble() * 0.2 > 0.0;
                
                boolean genGravelBeach = this.beachNoiseOctaves.sample(
                    absZ * thirtysecond, 
                    109.0134,
                    absX * thirtysecond) + rand.nextDouble() * 0.2 > 3.0;
                
                double surfaceNoise = this.surfaceNoiseOctaves.sample(absX * thirtysecond * 2.0, absZ * thirtysecond * 2.0);
                int surfaceDepth = (int)(surfaceNoise / 3.0 + 3.0 + rand.nextDouble() * 0.25);
                
                int flag = -1;
                
                Biome biome = biomeSource.getBiomeForSurfaceGen(region, mutable.set(absX, topY, absZ));
                
                BlockState biomeTopBlock = biome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                BlockState biomeFillerBlock = biome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();
                
                BlockState topBlock = biomeTopBlock;
                BlockState fillerBlock = biomeFillerBlock;
                
                boolean usedCustomSurface = this.useCustomSurfaceBuilder(biome, biomeSource.getBiomeRegistry().getId(biome), region, chunk, rand, mutable);
                
                for (int y = this.worldTopY - 1; y >= this.minY; --y) {
                    
                    // Randomly place bedrock from y=0 to y=5
                    if (y <= bedrockFloor + rand.nextInt(5)) {
                        chunk.setBlockState(mutable.set(x, y, z), Blocks.BEDROCK.getDefaultState(), false);
                        continue;
                    }
                    
                    // Skip if used custom surface generation or if below minimum surface level.
                    if (usedCustomSurface || y < this.minSurfaceY) {
                        continue;
                    }
                    
                    mutable.set(x, y, z);
                    BlockState someBlock = chunk.getBlockState(mutable);
                    
                    if (someBlock.equals(BlockStates.AIR)) {
                        flag = -1;
                        
                    } else if (someBlock.equals(this.defaultBlock)) {
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
                                topBlock = this.defaultFluid;
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
    public boolean isSandAt(int x, int z, HeightLimitView world) {
        double eighth = 0.03125D;
        
        int y = this.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG, world);
        Biome biome = this.getBiomeForNoiseGen(x >> 2, 0, z >> 2);
        
        return 
            (biome.getGenerationSettings().getSurfaceConfig().getTopMaterial() == BlockStates.SAND && y >= seaLevel - 1) || 
            (beachNoiseOctaves.sample(x * eighth, z * eighth, 0.0) > 0.0 && y > seaLevel - 1 && y <= seaLevel + 1);
    }
    
    @Override
    protected void generateNoiseColumn(double[] buffer, int startNoiseX, int startNoiseZ, int localNoiseX, int localNoiseZ) {
        int noiseX = startNoiseX + localNoiseX;
        int noiseZ = startNoiseZ + localNoiseZ;
        
        for (int y = 0; y < buffer.length; ++y) {
            int noiseY = y + this.noiseMinY;
            
            // Check if y (in scaled space) is below sealevel
            // and increase density accordingly.
            //double elevGrad = y * 4.0 - 64.0;
            double densityOffset = noiseY * this.verticalNoiseResolution - (double)this.seaLevel;
            if (densityOffset < 0.0) {
                densityOffset *= 3.0;
            }
            
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
                noiseZ * coordinateScale / mainNoiseScaleZ) / 2.0;
            
            // Do not clamp noise if generating with noise caves!
            if (mainNoiseVal < -1) {
                density = this.minLimitNoiseOctaves.sample(noiseX * coordinateScale, noiseY * heightScale, noiseZ * coordinateScale) / limitScale - densityOffset;
                if (!this.generateNoiseCaves) 
                    density = MathHelper.clamp(density, -10D, 10D);
                
            } else if (mainNoiseVal > 1.0) {
                density = this.maxLimitNoiseOctaves.sample(noiseX * coordinateScale, noiseY * heightScale, noiseZ * coordinateScale) / limitScale - densityOffset;
                if (!this.generateNoiseCaves) 
                    density = MathHelper.clamp(density, -10D, 10D);
                
            } else {
                double minLimitVal = this.minLimitNoiseOctaves.sample(noiseX * coordinateScale, noiseY * heightScale, noiseZ * coordinateScale) / limitScale - densityOffset;
                double maxLimitVal = this.maxLimitNoiseOctaves.sample(noiseX * coordinateScale, noiseY * heightScale, noiseZ * coordinateScale) / limitScale - densityOffset;     
                if (!this.generateNoiseCaves) {
                    minLimitVal = MathHelper.clamp(minLimitVal, -10D, 10D);
                    maxLimitVal = MathHelper.clamp(maxLimitVal, -10D, 10D);
                }
                
                double mix = (mainNoiseVal + 1.0) / 2.0;
                density = minLimitVal + (maxLimitVal - minLimitVal) * mix;
            };
            
            // Sample for noise caves
            density = this.sampleNoiseCave(
                density,
                noiseX * this.horizontalNoiseResolution,
                noiseY * this.verticalNoiseResolution,
                noiseZ * this.horizontalNoiseResolution
            );
            
            density = this.applyBottomSlide(density, noiseY, -3);
            
            buffer[y] = density;
        }
    }
}
