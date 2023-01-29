package mod.bespectacled.modernbeta.world.chunk.provider;

import java.util.Random;

import mod.bespectacled.modernbeta.api.world.chunk.NoiseChunkProvider;
import mod.bespectacled.modernbeta.api.world.chunk.SurfaceConfig;
import mod.bespectacled.modernbeta.util.BlockColumnHolder;
import mod.bespectacled.modernbeta.util.BlockStates;
import mod.bespectacled.modernbeta.util.chunk.HeightmapChunk;
import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import mod.bespectacled.modernbeta.util.noise.SimpleNoisePos;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaSurfaceRules;
import mod.bespectacled.modernbeta.world.spawn.BeachSpawnLocator;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.AquiferSampler;

public class AlphaChunkProvider extends NoiseChunkProvider {
    private final PerlinOctaveNoise minLimitOctaveNoise;
    private final PerlinOctaveNoise maxLimitOctaveNoise;
    private final PerlinOctaveNoise mainOctaveNoise;
    private final PerlinOctaveNoise beachOctaveNoise;
    private final PerlinOctaveNoise surfaceOctaveNoise;
    private final PerlinOctaveNoise scaleOctaveNoise;
    private final PerlinOctaveNoise depthOctaveNoise;
    private final PerlinOctaveNoise forestOctaveNoise;
    
    public AlphaChunkProvider(ModernBetaChunkGenerator chunkGenerator) {
        super(chunkGenerator);

        // Noise Generators
        this.minLimitOctaveNoise = new PerlinOctaveNoise(random, 16, true);
        this.maxLimitOctaveNoise = new PerlinOctaveNoise(random, 16, true);
        this.mainOctaveNoise = new PerlinOctaveNoise(random, 8, true);
        this.beachOctaveNoise = new PerlinOctaveNoise(random, 4, true);
        this.surfaceOctaveNoise = new PerlinOctaveNoise(random, 4, true);
        this.scaleOctaveNoise = new PerlinOctaveNoise(random, 10, true);
        this.depthOctaveNoise = new PerlinOctaveNoise(random, 16, true);
        this.forestOctaveNoise = new PerlinOctaveNoise(random, 8, true);

        setForestOctaves(forestOctaveNoise);
        
        this.spawnLocator = new BeachSpawnLocator(this, this.beachOctaveNoise);
    }

    @Override
    public void provideSurface(ChunkRegion region, Chunk chunk, ModernBetaBiomeSource biomeSource) {
        double scale = 0.03125;

        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;
        
        int startX = chunk.getPos().getStartX();
        int startZ = chunk.getPos().getStartZ();
        
        int bedrockFloor = this.worldMinY + this.bedrockFloor;
        
        Random rand = this.createSurfaceRandom(chunkX, chunkZ);
        BlockPos.Mutable pos = new BlockPos.Mutable();

        AquiferSampler aquiferSampler = this.getAquiferSampler(chunk);
        HeightmapChunk heightmapChunk = this.getHeightmapChunk(chunkX, chunkZ);
        SimpleNoisePos noisePos = new SimpleNoisePos();

        // Surface builder stuff
        BlockColumnHolder blockColumn = new BlockColumnHolder(chunk);
        ModernBetaSurfaceRules surfaceRules = new ModernBetaSurfaceRules(region, chunk, this.chunkGenerator);
        
        double[] sandNoise = beachOctaveNoise.sampleAlpha(
            chunkX * 16, chunkZ * 16, 0.0D,
            16, 16, 1,
            scale, scale, 1.0D
        );
        
        double[] gravelNoise = beachOctaveNoise.sampleAlpha(
            chunkZ * 16, 109.0134D, chunkX * 16,
            16, 1, 16,
            scale, 1.0D, scale
        );
        
        double[] surfaceNoise = surfaceOctaveNoise.sampleAlpha(
            chunkX * 16, chunkZ * 16, 0.0D,
            16, 16, 1,
            scale * 2D, scale * 2D, scale * 2D
        );
        
        for (int localX = 0; localX < 16; localX++) {
            for (int localZ = 0; localZ < 16; localZ++) {
                int x = startX + localX;
                int z = startZ + localZ;
                int surfaceTopY = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG).get(localX, localZ);
                int surfaceMinY = (this.hasNoisePostProcessor()) ? 
                    heightmapChunk.getHeight(x, z, HeightmapChunk.Type.SURFACE_FLOOR) - 8 : 
                    this.worldMinY;
                
                boolean genSandBeach = sandNoise[localX + localZ * 16] + rand.nextDouble() * 0.2 > 0.0;
                boolean genGravelBeach = gravelNoise[localX + localZ * 16] + rand.nextDouble() * 0.2 > 3.0;
                int surfaceDepth = (int) (surfaceNoise[localX + localZ * 16] / 3.0 + 3.0 + rand.nextDouble() * 0.25);

                int runDepth = -1;
                
                RegistryEntry<Biome> biome = biomeSource.getBiomeForSurfaceGen(region, pos.set(x, surfaceTopY, z));
                
                SurfaceConfig surfaceConfig = SurfaceConfig.getSurfaceConfig(biome);
                BlockState topBlock = surfaceConfig.topBlock();
                BlockState fillerBlock = surfaceConfig.fillerBlock();

                boolean usedCustomSurface = this.surfaceBuilder.buildSurfaceColumn(
                    region.getRegistryManager().get(Registry.BIOME_KEY),
                    region.getBiomeAccess(), 
                    blockColumn, 
                    chunk, 
                    biome, 
                    surfaceRules.getRuleContext(), 
                    surfaceRules.getBlockStateRule(),
                    localX,
                    localZ,
                    surfaceTopY
                );
                
                // Generate from top to bottom of world
                for (int y = this.worldTopY - 1; y >= this.worldMinY; y--) {
                    BlockState blockState;
                    
                    pos.set(localX, y, localZ);
                    blockState = chunk.getBlockState(pos);
                    
                    // Place deepslate
                    BlockState deepslateState = this.sampleDeepslateState(x, y, z);
                    if (deepslateState != null && blockState.isOf(this.defaultBlock.getBlock())) {
                        chunk.setBlockState(pos, deepslateState, false);
                    }
                    
                    // Place bedrock
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

                    if (blockState.isAir()) { // Skip if air block
                        runDepth = -1;
                        continue;
                    }

                    if (!blockState.isOf(this.defaultBlock.getBlock())) { // Skip if not stone
                        continue;
                    }

                    if (runDepth == -1) {
                        if (surfaceDepth <= 0) { // Generate stone basin if noise permits
                            topBlock = BlockStates.AIR;
                            fillerBlock = this.defaultBlock;
                            
                        } else if (y >= this.seaLevel - 4 && y <= this.seaLevel + 1) { // Generate beaches at this y range
                            topBlock = surfaceConfig.topBlock();
                            fillerBlock = surfaceConfig.fillerBlock();

                            if (genGravelBeach) {
                                topBlock = BlockStates.AIR; // This reduces gravel beach height by 1
                                fillerBlock = BlockStates.GRAVEL;
                            }

                            if (genSandBeach) {
                                topBlock = BlockStates.SAND;
                                fillerBlock = BlockStates.SAND;
                            }
                        }

                        runDepth = surfaceDepth;
                        
                        if (y < this.seaLevel && topBlock.isAir()) { // Generate water bodies
                            BlockState fluidBlock = aquiferSampler.apply(noisePos.setBlockCoords(x, y, z), 0.0);
                            
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
                    
                    if (runDepth > 0) { 
                        runDepth--;
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
        
        double coordinateScale = 684.412D * this.xzScale; 
        double heightScale = 684.412D * this.yScale;
        
        double mainNoiseScaleX = this.xzFactor; // Default: 80
        double mainNoiseScaleY = this.yFactor;  // Default: 160
        double mainNoiseScaleZ = this.xzFactor;

        double lowerLimitScale = 512D;
        double upperLimitScale = 512D;
        
        double baseSize = 8.5D;
        double heightStretch = 12D;
        
        // Density norm (sum of 16 octaves of noise / limitScale => [-128, 128])
        double densityScale = 128.0;
        
        double scale = this.scaleOctaveNoise.sample(noiseX, 0, noiseZ, 1.0, 0.0, 1.0);
        scale = (scale + 256D) / 512D;
        
        if (scale > 1.0D) {
            scale = 1.0D; 
        }

        double depth = this.depthOctaveNoise.sample(noiseX, 0, noiseZ, depthNoiseScaleX, 0.0, depthNoiseScaleZ);
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
        
        for (int y = 0; y < primaryBuffer.length; ++y) {
            int noiseY = y + this.noiseMinY;

            double density;
            double heightmapDensity;
            
            double densityOffset = this.getOffset(noiseY, heightStretch, depth, scale);
            
            // Equivalent to current MC noise.sample() function, see NoiseColumnSampler.
            double mainNoise = (this.mainOctaveNoise.sample(
                noiseX, noiseY, noiseZ,
                coordinateScale / mainNoiseScaleX, 
                heightScale / mainNoiseScaleY, 
                coordinateScale / mainNoiseScaleZ
            ) / 10D + 1.0D) / 2D;
            
            if (mainNoise < 0.0D) {
                density = this.minLimitOctaveNoise.sample(
                    noiseX, noiseY, noiseZ,
                    coordinateScale, 
                    heightScale, 
                    coordinateScale
                ) / lowerLimitScale;
                
            } else if (mainNoise > 1.0D) {
                density = this.maxLimitOctaveNoise.sample(
                    noiseX, noiseY, noiseZ,
                    coordinateScale, 
                    heightScale, 
                    coordinateScale
                ) / upperLimitScale;
                
            } else {
                double minLimitNoise = this.minLimitOctaveNoise.sample(
                    noiseX, noiseY, noiseZ,
                    coordinateScale, 
                    heightScale, 
                    coordinateScale
                ) / lowerLimitScale;
                
                double maxLimitNoise = this.maxLimitOctaveNoise.sample(
                    noiseX, noiseY, noiseZ,
                    coordinateScale, 
                    heightScale, 
                    coordinateScale
                ) / upperLimitScale;
                
                density = minLimitNoise + (maxLimitNoise - minLimitNoise) * mainNoise;
            }

            // Equivalent to current MC addition of density offset, see NoiseColumnSampler.
            density -= densityOffset;
            
            // Normalize density
            density /= densityScale;
            
            // Sample without noise caves
            heightmapDensity = density;
            
            // Sample for noise caves
            density = this.sampleNoisePostProcessor(density, noiseX, noiseY, noiseZ);
            
            // Apply slides
            density = this.applySlides(density, y);
            heightmapDensity = this.applySlides(heightmapDensity, y);
            
            primaryBuffer[y] = MathHelper.clamp(density, -64.0, 64.0);
            heightmapBuffer[y] = MathHelper.clamp(heightmapDensity, -64.0, 64.0);
        }
    }
    
    private double getOffset(int noiseY, double heightStretch, double depth, double scale) {
        double offset = (((double)noiseY - depth) * heightStretch) / scale;
        
        if (offset < 0D)
            offset *= 4D;
        
        return offset;
    }
}
