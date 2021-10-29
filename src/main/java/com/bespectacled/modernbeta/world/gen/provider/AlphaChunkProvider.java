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
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.HeightContext;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

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
        
        int bedrockFloor = this.worldMinY + this.bedrockFloor;
        
        Random rand = this.createSurfaceRandom(chunkX, chunkZ);
        BlockPos.Mutable pos = new BlockPos.Mutable();
        
        double[] sandNoise = this.createSurfaceArray();
        double[] gravelNoise = this.createSurfaceArray();
        double[] surfaceNoise = this.createSurfaceArray();

        beachNoiseOctaves.sampleArr(sandNoise, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1, eighth, eighth, 1.0D);
        beachNoiseOctaves.sampleArr(gravelNoise, chunkZ * 16, 109.0134D, chunkX * 16, 16, 1, 16, eighth, 1.0D, eighth);
        surfaceNoiseOctaves.sampleArr(surfaceNoise, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1, eighth * 2D, eighth * 2D, eighth * 2D);

        AquiferSampler aquiferSampler = this.getAquiferSampler(chunk);
        HeightmapChunk heightmapChunk = this.getHeightmapChunk(chunkX, chunkZ);

        // Surface builder stuff
        BlockColumnHolder blockColumn = new BlockColumnHolder(chunk);
        HeightContext context = new HeightContext(this.chunkGenerator, region);
        MaterialRules.MaterialRuleContext ruleContext = new MaterialRules.MaterialRuleContext(this.surfaceBuilder, context);
        MaterialRules.BlockStateRule blockStateRule = this.surfaceRule.apply(ruleContext);
        
        // Accurate beach/terrain patterns depend on z iterating before x,
        // and array accesses changing accordingly.
        for (int localX = 0; localX < 16; localX++) {
            for (int localZ = 0; localZ < 16; localZ++) {
                int x = (chunkX << 4) + localX;
                int z = (chunkZ << 4) + localZ;
                int surfaceTopY = GenUtil.getLowestSolidHeight(chunk, this.worldHeight, this.worldMinY, localX, localZ, this.defaultFluid) + 1;
                int surfaceMinY = (this.generateNoiseCaves || this.generateNoodleCaves) ? 
                    heightmapChunk.getHeight(x, z, HeightmapChunk.Type.SURFACE_FLOOR) - 8 : 
                    this.worldMinY;
                
                boolean genSandBeach = sandNoise[localX + localZ * 16] + rand.nextDouble() * 0.20000000000000001D > 0.0D;
                boolean genGravelBeach = gravelNoise[localX + localZ * 16] + rand.nextDouble() * 0.20000000000000001D > 3D;
                int surfaceDepth = (int) (surfaceNoise[localX + localZ * 16] / 3D + 3D + rand.nextDouble() * 0.25D);

                int flag = -1;
                
                Biome biome = biomeSource.getBiomeForSurfaceGen(region, pos.set(x, surfaceTopY, z));
                
                BiomeBlocks biomeBlocks = BiomeBlocks.getBiomeBlocks(biome);
                BlockState biomeTopBlock = biomeBlocks.getTopBlock();
                BlockState biomeFillerBlock = biomeBlocks.getFillerBlock();

                BlockState topBlock = biomeTopBlock;
                BlockState fillerBlock = biomeFillerBlock;

                boolean usedCustomSurface = this.surfaceBuilder.buildSurfaceColumn(
                    region.getRegistryManager().get(Registry.BIOME_KEY),
                    region.getBiomeAccess(), 
                    blockColumn, 
                    chunk, 
                    biome, 
                    ruleContext, 
                    blockStateRule,
                    localX,
                    localZ,
                    surfaceTopY
                );
                
                // Generate from top to bottom of world
                for (int y = this.worldTopY - 1; y >= this.worldMinY; y--) {
                    pos.set(localX, y, localZ);

                    // Randomly place bedrock from y=0 to y=5
                    if (y <= bedrockFloor + rand.nextInt(6) - 1) {
                        chunk.setBlockState(pos, BlockStates.BEDROCK, false);
                        continue;
                    }
                    
                    // TODO: As of 21w08b.
                    // Check to remove later, not accurate but temporary to ensure bottom layer of world is always bedrock.
                    // Game breaks during ore decoration breaks if any block at yMin is stone/deepslate
                    // since the game checks all adjacent blocks for a particular position,
                    // even if the downward direction is below the world limit!!
                    if (y <= this.worldMinY) {
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

                        continue;
                    }
                    
                    if (flag > 0) { 
                        flag--;
                        chunk.setBlockState(pos, fillerBlock, false);
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
            depthNoise /= 2D; // Omitting this creates the Infdev 20100611 generator.

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
            density = this.sampleNoiseCave(density, 200.0, noiseX, noiseY, noiseZ);
            
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
