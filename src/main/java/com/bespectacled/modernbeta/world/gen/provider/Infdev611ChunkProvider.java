package com.bespectacled.modernbeta.world.gen.provider;

import java.util.function.Supplier;

import com.bespectacled.modernbeta.api.world.gen.BeachSpawnable;
import com.bespectacled.modernbeta.api.world.gen.NoiseChunkProvider;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.gen.OldGeneratorUtil;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class Infdev611ChunkProvider extends NoiseChunkProvider implements BeachSpawnable {
    private final PerlinOctaveNoise minLimitNoiseOctaves;
    private final PerlinOctaveNoise maxLimitNoiseOctaves;
    private final PerlinOctaveNoise mainNoiseOctaves;
    private final PerlinOctaveNoise beachNoiseOctaves;
    private final PerlinOctaveNoise stoneNoiseOctaves;
    private final PerlinOctaveNoise scaleNoiseOctaves;
    private final PerlinOctaveNoise depthNoiseOctaves;
    private final PerlinOctaveNoise forestNoiseOctaves;
    
    public Infdev611ChunkProvider(long seed, ChunkGenerator chunkGenerator, Supplier<ChunkGeneratorSettings> generatorSettings, NbtCompound providerSettings) {
        //super(seed, settings);
        super(seed, chunkGenerator, generatorSettings, providerSettings, 0, 128, 64, 50, 0, -10, BlockStates.STONE, BlockStates.WATER, 2, 1, 1.0, 1.0, 80, 160, -10, 3, 0, 15, 3, 0, false, false, false, false, false);
        
        // Noise Generators
        this.minLimitNoiseOctaves = new PerlinOctaveNoise(rand, 16, true);
        this.maxLimitNoiseOctaves = new PerlinOctaveNoise(rand, 16, true);
        this.mainNoiseOctaves = new PerlinOctaveNoise(rand, 8, true);
        this.beachNoiseOctaves = new PerlinOctaveNoise(rand, 4, true);
        this.stoneNoiseOctaves = new PerlinOctaveNoise(rand, 4, true);
        this.scaleNoiseOctaves = new PerlinOctaveNoise(rand, 10, true);
        this.depthNoiseOctaves = new PerlinOctaveNoise(rand, 16, true);
        this.forestNoiseOctaves = new PerlinOctaveNoise(rand, 8, true);

        setForestOctaves(forestNoiseOctaves);
    }

    @Override
    public void provideSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource) {
        double eighth = 0.03125D;

        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        int bedrockFloor = this.minY + this.bedrockFloor;
        
        // TODO: Really should be pooled or something
        ChunkRandom rand = this.createChunkRand(chunkX, chunkZ);
        ChunkRandom sandstoneRand = this.createChunkRand(chunkX, chunkZ);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        // Accurate beach/terrain patterns depend on z iterating before x,
        // and array accesses changing accordingly.
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int absX = (chunkX << 4) + x;
                int absZ = (chunkZ << 4) + z;
                int topY = OldGeneratorUtil.getSolidHeight(chunk, this.worldHeight, this.minY, x, z, this.defaultFluid) + 1;
                
                boolean genSandBeach = this.beachNoiseOctaves.sample(absX * eighth, absZ * eighth, 0.0) + rand.nextDouble() * 0.2 > 0.0;
                boolean genGravelBeach = this.beachNoiseOctaves.sample(absZ * eighth, 109.0134, absX * eighth) + rand.nextDouble() * 0.2 > 3.0;
                int genStone = (int)(this.stoneNoiseOctaves.sample(absX * eighth * 2.0, absX * eighth * 2.0) / 3.0 + 3.0 + rand.nextDouble() * 0.25);

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
                    if (y <= bedrockFloor + sandstoneRand.nextInt(6) - 1) {
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
                    if (usedCustomSurface || (this.generateAquifers || this.generateNoiseCaves) && y < 50) {
                        continue;
                    }

                    BlockState someBlock = chunk.getBlockState(mutable.set(x, y, z));

                    if (someBlock.equals(BlockStates.AIR)) { // Skip if air block
                        flag = -1;
                    } else if (someBlock.equals(this.defaultBlock)) {
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

                            if (y < this.seaLevel && topBlock.equals(BlockStates.AIR)) {
                                topBlock = this.defaultFluid;
                            }

                            flag = genStone;
                            if (y >= this.seaLevel - 1) {
                                chunk.setBlockState(mutable.set(x, y, z), topBlock, false);
                            } else {
                                chunk.setBlockState(mutable.set(x, y, z), fillerBlock, false);
                            }
                            
                        } else if (flag > 0) { 
                            flag--;
                            chunk.setBlockState(mutable.set(x, y, z), fillerBlock, false);
                        }
                        
                        // Beta backport, adds layer of sandstone starting at lowest block of sand, of height 1 to 4.
                        if (flag == 0 && fillerBlock.equals(BlockStates.SAND)) {
                            flag = sandstoneRand.nextInt(4);
                            fillerBlock = BlockStates.SANDSTONE;
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
            (beachNoiseOctaves.sample(x * eighth, z * eighth, 0.0) + rand.nextDouble() * 0.2 > 0.0 && y > seaLevel - 1 && y <= seaLevel + 1);
    }

    @Override
    protected void generateScaleDepth(int startNoiseX, int startNoiseZ, int curNoiseX, int curNoiseZ, double[] scaleDepth) {
        int noiseX = startNoiseX + curNoiseX;
        int noiseZ = startNoiseZ + curNoiseZ;

        double depthNoiseScaleX = 100D;
        double depthNoiseScaleZ = 100D;
        
        //double baseSize = noiseResolutionY / 2D; // Or: 17 / 2D = 8.5
        double baseSize = 8.5D;
        
        double scale = this.scaleNoiseOctaves.sample(noiseX, 0, noiseZ, 1.0D, 0.0D, 1.0D);
        scale = (scale + 256D) / 512D;
        
        if (scale > 1.0D) {
            scale = 1.0D; 
        }

        double depth0 = this.depthNoiseOctaves.sample(noiseX, 0, noiseZ, depthNoiseScaleX, 0.0D, depthNoiseScaleZ);
        depth0 /= 8000D;
        
        if (depth0 < 0.0D) {
            depth0 = -depth0;
        }

        depth0 = depth0 * 3D - 3D;

        if (depth0 < 0.0D) {
            depth0 /= 2D;
            if (depth0 < -1D) {
                depth0 = -1D;
            }

            depth0 /= 1.3999999999999999D;
            //depth0 /= 2D; // Omitting this creates the Infdev 20100611 generator.

            scale = 0.0D;

        } else {
            if (depth0 > 1.0D) {
                depth0 = 1.0D;
            }
            depth0 /= 6D;
        }

        scale += 0.5D;
        //depth0 = (depth0 * (double) noiseResolutionY) / 16D;
        //double depth1 = (double) noiseResolutionY / 2D + depth0 * 4D;\
        depth0 = depth0 * baseSize / 8D;
        double depth1 = baseSize + depth0 * 4D;
        
        scaleDepth[0] = scale;
        scaleDepth[1] = depth1;
    }
    
    @Override
    protected double generateNoise(int noiseX, int noiseY, int noiseZ, double[] scaleDepth) {
        double scale = scaleDepth[0];
        double depth = scaleDepth[1];
        
        // Var names taken from old customized preset names
        double coordinateScale = 684.41200000000003D * this.xzScale; 
        double heightScale = 684.41200000000003D * this.yScale;
        
        double mainNoiseScaleX = this.xzFactor; // Default: 80
        double mainNoiseScaleY = this.yFactor;  // Default: 160
        double mainNoiseScaleZ = this.xzFactor;

        double lowerLimitScale = 512D;
        double upperLimitScale = 512D;
        
        double heightStretch = 12D;
        
        double density = 0.0D;
        double densityOffset = (((double)noiseY - depth) * heightStretch) / scale;
        
        if (densityOffset < 0.0D) {
            densityOffset *= 4D;
        }
        
        // Equivalent to current MC noise.sample() function, see NoiseColumnSampler.
        double mainNoise = (this.mainNoiseOctaves.sample(
            noiseX, noiseY, noiseZ, 
            coordinateScale / mainNoiseScaleX, 
            heightScale / mainNoiseScaleY, 
            coordinateScale / mainNoiseScaleZ
        ) / 10D + 1.0D) / 2D;
        
        if (mainNoise < 0.0D) {
            density = this.minLimitNoiseOctaves.sample(noiseX, noiseY, noiseZ, coordinateScale, heightScale, coordinateScale) / lowerLimitScale;
        } else if (mainNoise > 1.0D) {
            density = this.maxLimitNoiseOctaves.sample(noiseX, noiseY, noiseZ, coordinateScale, heightScale, coordinateScale) / upperLimitScale;
        } else {
            double minLimitNoise = this.minLimitNoiseOctaves.sample(noiseX, noiseY, noiseZ, coordinateScale, heightScale, coordinateScale) / lowerLimitScale;
            double maxLimitNoise = this.maxLimitNoiseOctaves.sample(noiseX, noiseY, noiseZ, coordinateScale, heightScale, coordinateScale) / upperLimitScale;
            density = minLimitNoise + (maxLimitNoise - minLimitNoise) * mainNoise;
        }
        
        // Equivalent to current MC addition of density offset, see NoiseColumnSampler.
        double densityWithOffset = density - densityOffset; 
        
        // Sample for noise caves
        densityWithOffset = this.sampleNoiseCave(
            densityWithOffset,
            noiseX * this.horizontalNoiseResolution, 
            noiseY * this.verticalNoiseResolution,
            noiseZ * this.horizontalNoiseResolution
        );
        
        densityWithOffset = this.applyTopSlide(densityWithOffset, noiseY, 4);
        densityWithOffset = this.applyBottomSlide(densityWithOffset, noiseY, -3);
        
        return densityWithOffset;
    }
}
