package com.bespectacled.modernbeta.world.gen.provider;

import java.util.function.Supplier;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.gen.NoiseChunkProvider;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.noise.SimplexNoise;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.biome.beta.BetaClimateSampler;
import com.bespectacled.modernbeta.world.gen.OldGeneratorUtil;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class BetaIslandsChunkProvider extends NoiseChunkProvider {
    private final PerlinOctaveNoise minLimitNoiseOctaves;
    private final PerlinOctaveNoise maxLimitNoiseOctaves;
    private final PerlinOctaveNoise mainNoiseOctaves;
    private final PerlinOctaveNoise beachNoiseOctaves;
    private final PerlinOctaveNoise stoneNoiseOctaves;
    private final PerlinOctaveNoise scaleNoiseOctaves;
    private final PerlinOctaveNoise depthNoiseOctaves;
    private final PerlinOctaveNoise forestNoiseOctaves;
    
    private final SimplexNoise islandNoise;
    
    private final int centerOceanLerpDistance;
    private final int centerOceanRadius;
    private final float centerIslandFalloff;
    private final float outerIslandNoiseScale;
    private final float outerIslandNoiseOffset;
    
    public BetaIslandsChunkProvider(long seed, ChunkGenerator chunkGenerator, Supplier<ChunkGeneratorSettings> generatorSettings, CompoundTag providerSettings) {
        //super(seed, settings);
        super(seed, chunkGenerator, generatorSettings, providerSettings, 128, 64, 0, -10, BlockStates.STONE, BlockStates.WATER, 2, 1, 1.0, 1.0, 80, 160, -10, 3, 0, 15, 3, 0);
        
        // Noise Generators
        this.minLimitNoiseOctaves = new PerlinOctaveNoise(RAND, 16, true);
        this.maxLimitNoiseOctaves = new PerlinOctaveNoise(RAND, 16, true);
        this.mainNoiseOctaves = new PerlinOctaveNoise(RAND, 8, true);
        this.beachNoiseOctaves = new PerlinOctaveNoise(RAND, 4, true);
        this.stoneNoiseOctaves = new PerlinOctaveNoise(RAND, 4, true);
        this.scaleNoiseOctaves = new PerlinOctaveNoise(RAND, 10, true);
        this.depthNoiseOctaves = new PerlinOctaveNoise(RAND, 16, true);
        this.forestNoiseOctaves = new PerlinOctaveNoise(RAND, 8, true);
        
        this.islandNoise = new SimplexNoise(RAND);
        
        // Beta Islands settings
        this.centerOceanLerpDistance = this.providerSettings.contains("centerOceanLerpDistance") ? 
            this.providerSettings.getInt("centerOceanLerpDistance") : 
            ModernBeta.BETA_CONFIG.generation_config.centerOceanLerpDistance;

        this.centerOceanRadius = this.providerSettings.contains("centerOceanRadius") ? 
            this.providerSettings.getInt("centerOceanRadius") : 
            ModernBeta.BETA_CONFIG.generation_config.centerOceanRadius;

        this.centerIslandFalloff = this.providerSettings.contains("centerIslandFalloff") ? 
            this.providerSettings.getFloat("centerIslandFalloff") : 
            ModernBeta.BETA_CONFIG.generation_config.centerIslandFalloff;

        this.outerIslandNoiseScale = this.providerSettings.contains("outerIslandNoiseScale") ? 
            this.providerSettings.getFloat("outerIslandNoiseScale") : 
            ModernBeta.BETA_CONFIG.generation_config.outerIslandNoiseOffset;

        this.outerIslandNoiseOffset = this.providerSettings.contains("outerIslandNoiseOffset") ? 
            this.providerSettings.getFloat("outerIslandNoiseOffset") : 
            ModernBeta.BETA_CONFIG.generation_config.outerIslandNoiseOffset;
        
        BetaClimateSampler.INSTANCE.setSeed(seed);
        setForestOctaves(forestNoiseOctaves);
    }

    @Override
    public void provideChunk(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk) {
        this.generateTerrain(chunk, structureAccessor);
    }
    
    @Override
    public void provideSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource) {
        double eighth = 0.03125D;

        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        // TODO: Really should be pooled or something
        ChunkRandom rand = this.createChunkRand(chunkX, chunkZ);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        double[] sandNoise = this.surfaceNoisePool.borrowArr();
        double[] gravelNoise = this.surfaceNoisePool.borrowArr();
        double[] stoneNoise = this.surfaceNoisePool.borrowArr();

        sandNoise = beachNoiseOctaves.sampleArrBeta(
            sandNoise, 
            chunkX * 16, chunkZ * 16, 0.0D, 
            16, 16, 1,
            eighth, eighth, 1.0D);
        
        gravelNoise = beachNoiseOctaves.sampleArrBeta(
            gravelNoise, 
            chunkX * 16, 109.0134D, chunkZ * 16, 
            16, 1, 16, 
            eighth, 1.0D, eighth);
        
        stoneNoise = stoneNoiseOctaves.sampleArrBeta(
            stoneNoise, 
            chunkX * 16, chunkZ * 16, 0.0D, 
            16, 16, 1,
            eighth * 2D, eighth * 2D, eighth * 2D
        );

        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {
                int absX = (chunkX << 4) + x;
                int absZ = (chunkZ << 4) + z;
                int topY = OldGeneratorUtil.getSolidHeight(chunk, x, z, this.worldHeight, this.defaultFluid) + 1;
                
                boolean genSandBeach = sandNoise[z + x * 16] + rand.nextDouble() * 0.20000000000000001D > 0.0D;
                boolean genGravelBeach = gravelNoise[z + x * 16] + rand.nextDouble() * 0.20000000000000001D > 3D;
                
                int genStone = (int) (stoneNoise[z + x * 16] / 3D + 3D + rand.nextDouble() * 0.25D);
                
                int flag = -1;
                
                Biome biome = biomeSource.getBiomeForSurfaceGen(region, mutable.set(absX, topY, absZ));

                BlockState biomeTopBlock = biome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                BlockState biomeFillerBlock = biome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();

                BlockState topBlock = biomeTopBlock;
                BlockState fillerBlock = biomeFillerBlock;
                
                boolean usedCustomSurface = this.useCustomSurfaceBuilder(biome, biomeSource.getBiomeRegistry().getId(biome), region, chunk, rand, mutable);

                // Generate from top to bottom of world
                for (int y = this.worldHeight - 1; y >= 0; y--) {

                    // Randomly place bedrock from y=0 (or minHeight) to y=5
                    if (y <= bedrockFloor + rand.nextInt(5)) {
                        chunk.setBlockState(mutable.set(x, y, z), BlockStates.BEDROCK, false);
                        continue;
                    }
                    
                    // Don't surface build below 50, per 1.17 default surface builder
                    if (usedCustomSurface) {
                        continue;
                    }

                    BlockState someBlock = chunk.getBlockState(mutable.set(x, y, z));

                    if (someBlock.equals(BlockStates.AIR)) { // Skip if air block
                        flag = -1;
                        continue;
                    }

                    if (!someBlock.equals(this.defaultBlock)) { // Skip if not stone
                        continue;
                    }

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

                        if (y < this.seaLevel && topBlock.equals(BlockStates.AIR)) { // Generate water bodies
                            topBlock = this.defaultFluid;
                        }

                        flag = genStone;
                        if (y >= this.seaLevel - 1) {
                            chunk.setBlockState(mutable.set(x, y, z), topBlock, false);
                        } else {
                            chunk.setBlockState(mutable.set(x, y, z), fillerBlock, false);
                        }

                        continue;
                    }

                    if (flag <= 0) {
                        continue;
                    }

                    flag--;
                    chunk.setBlockState(mutable.set(x, y, z), fillerBlock, false);

                    // Generates layer of sandstone starting at lowest block of sand, of height 1 to 4.
                    if (flag == 0 && fillerBlock.equals(BlockStates.SAND)) {
                        flag = rand.nextInt(4);
                        fillerBlock = BlockStates.SANDSTONE;
                    }
                }
            }
        }
        
        this.surfaceNoisePool.returnArr(sandNoise);
        this.surfaceNoisePool.returnArr(gravelNoise);
        this.surfaceNoisePool.returnArr(stoneNoise);
    }

    @Override
    public int getHeight(int x, int z, Type type) {
        Integer groundHeight = heightmapCache.get(new BlockPos(x, 0, z));
        
        if (groundHeight == null) {
            groundHeight = this.sampleHeightmap(x, z);
        }

        // Not ideal
        if (type == Heightmap.Type.WORLD_SURFACE_WG && groundHeight < this.seaLevel)
            groundHeight = this.seaLevel;

        return groundHeight;
    }
    
    @Override
    public PerlinOctaveNoise getBeachNoise() {
        return this.beachNoiseOctaves;
    }
    
    @Override
    protected void generateHeightNoiseArr(int noiseX, int noiseZ, double[] heightNoise) {
        int noiseResolutionX = this.noiseSizeX + 1;
        int noiseResolutionZ = this.noiseSizeZ + 1;
        int noiseResolutionY = this.noiseSizeY + 1;
        
        int startX = noiseX / this.noiseSizeX * 16;
        int startZ = noiseZ / this.noiseSizeZ * 16;
        int transformCoord = 16 / noiseResolutionX;
        
        double[] scaleDepth = new double[2];
        
        int ndx = 0;
        for (int nX = 0; nX < noiseResolutionX; ++nX) {
            for (int nZ = 0; nZ < noiseResolutionZ; ++nZ) {
                int absX = startX + nX * transformCoord + transformCoord / 2;
                int absZ = startZ + nZ * transformCoord + transformCoord / 2;
                this.generateScaleDepth(absX, absZ, noiseX + nX, noiseZ + nZ, scaleDepth);
                
                double islandOffset = this.generateIslandOffset(nX, nZ, noiseX, noiseZ);
                
                for (int nY = 0; nY < noiseResolutionY; ++nY) {
                    heightNoise[ndx] = this.generateHeightNoise(noiseX + nX, nY, noiseZ + nZ, scaleDepth[0], scaleDepth[1], islandOffset);
                    ndx++;
                }
            }
        }
    }
    
    private void generateScaleDepth(int x, int z, int noiseX, int noiseZ, double[] scaleDepth) {
        if (scaleDepth.length != 2) 
            throw new IllegalArgumentException("[Modern Beta] Scale/Depth array has incorrect length, should be 2.");
        
        BetaClimateSampler climateSampler = BetaClimateSampler.INSTANCE;
        
        double depthNoiseScaleX = 200D;
        double depthNoiseScaleZ = 200D;
        
        //double baseSize = noiseResolutionY / 2D; // Or: 17 / 2D = 8.5
        double baseSize = 8.5D;
        
        double temp = climateSampler.sampleTemp(x, z);
        double humid = climateSampler.sampleHumid(x, z) * temp;
        
        humid = 1.0D - humid;
        humid *= humid;
        humid *= humid;
        humid = 1.0D - humid;

        double scale = this.scaleNoiseOctaves.sample(noiseX, noiseZ, 1.121D, 1.121D);
        scale = (scale + 256D) / 512D;
        scale *= humid;
        
        if (scale > 1.0D) {
            scale = 1.0D;
        }
        
        double depth0 = this.depthNoiseOctaves.sample(noiseX, noiseZ, depthNoiseScaleX, depthNoiseScaleZ);
        depth0 /= 8000D;

        if (depth0 < 0.0D) {
            depth0 = -depth0 * 0.29999999999999999D;
        }

        depth0 = depth0 * 3D - 2D;

        if (depth0 < 0.0D) {
            depth0 /= 2D;

            if (depth0 < -1D) {
                depth0 = -1D;
            }

            depth0 /= 1.3999999999999999D;
            depth0 /= 2D;

            scale = 0.0D;

        } else {
            if (depth0 > 1.0D) {
                depth0 = 1.0D;
            }
            depth0 /= 8D;
        }

        if (scale < 0.0D) {
            scale = 0.0D;
        }

        scale += 0.5D;
        //depthVal = (depthVal * (double) noiseResolutionY) / 16D;
        //double depthVal2 = (double) noiseResolutionY / 2D + depthVal * 4D;
        depth0 = depth0 * baseSize / 8D;
        double depth1 = baseSize + depth0 * 4D;
        
        scaleDepth[0] = scale;
        scaleDepth[1] = depth1;
    }
    
    // Debug: Outer edge starts at ~/tp -1170 100 770
    private double generateIslandOffset(int x, int z, int noiseStartX, int noiseStartZ) {
        float noiseX = x + noiseStartX;
        float noiseZ = z + noiseStartZ;
        
        float oceanDepth = 200.0F;
        
        int centerOceanLerpDistance = this.centerOceanLerpDistance * this.noiseSizeX;
        int centerOceanRadius = this.centerOceanRadius * this.noiseSizeX;
        float centerIslandFalloff = this.centerIslandFalloff;
        float outerIslandNoiseScale = this.outerIslandNoiseScale;
        float outerIslandNoiseOffset = this.outerIslandNoiseOffset;
        
        float dist = noiseX * noiseX + noiseZ * noiseZ;
        float radius = MathHelper.sqrt(dist);
        
        float islandOffset = 100.0F - radius * centerIslandFalloff;
        islandOffset = MathHelper.clamp(islandOffset, -oceanDepth, 0.0F);
            
        if (radius > centerOceanRadius) {
            float islandAddition = (float)this.islandNoise.sample(noiseX / outerIslandNoiseScale, noiseZ / outerIslandNoiseScale, 1.0, 1.0) + outerIslandNoiseOffset;
            
            // 0.885539 = Simplex upper range, but scale a little higher to ensure island centers have untouched terrain.
            islandAddition /= 0.8F;
            islandAddition = MathHelper.clamp(islandAddition, 0.0F, 1.0F);
            
            // Interpolate noise addition so there isn't a sharp cutoff at start of ocean ring edge.
            islandAddition = (float)MathHelper.clampedLerp(0.0F, islandAddition, (radius - centerOceanRadius) / centerOceanLerpDistance);
            
            islandOffset += islandAddition * oceanDepth;
            islandOffset = MathHelper.clamp(islandOffset, -oceanDepth, 0.0F);
        }
        
        return islandOffset;
    }
    
    private double generateHeightNoise(int noiseX, int noiseY, int noiseZ, double scale, double depth, double islandOffset) {
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
        
        // Add island offset
        densityWithOffset += islandOffset;
        
        densityWithOffset = this.applyTopSlide(densityWithOffset, noiseY, 4);
        densityWithOffset = this.applyBottomSlide(densityWithOffset, noiseY, -3);
        
        return densityWithOffset;
    }
}
