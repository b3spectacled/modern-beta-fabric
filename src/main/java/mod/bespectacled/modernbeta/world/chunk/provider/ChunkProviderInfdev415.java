package mod.bespectacled.modernbeta.world.chunk.provider;

import java.util.Random;

import mod.bespectacled.modernbeta.api.world.chunk.ChunkProviderNoise;
import mod.bespectacled.modernbeta.api.world.chunk.SurfaceConfig;
import mod.bespectacled.modernbeta.api.world.spawn.SpawnLocator;
import mod.bespectacled.modernbeta.util.BlockStates;
import mod.bespectacled.modernbeta.util.chunk.ChunkHeightmap;
import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import mod.bespectacled.modernbeta.util.noise.SimpleNoisePos;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bespectacled.modernbeta.world.spawn.SpawnLocatorBeta;
import net.minecraft.block.BlockState;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.noise.NoiseConfig;

public class ChunkProviderInfdev415 extends ChunkProviderNoise {
    private final PerlinOctaveNoise minLimitOctaveNoise;
    private final PerlinOctaveNoise maxLimitOctaveNoise;
    private final PerlinOctaveNoise mainOctaveNoise;
    private final PerlinOctaveNoise beachOctaveNoise;
    private final PerlinOctaveNoise surfaceOctaveNoise;
    private final PerlinOctaveNoise forestOctaveNoise;
    
    public ChunkProviderInfdev415(ModernBetaChunkGenerator chunkGenerator, long seed) {
        super(chunkGenerator, seed);
        
        this.minLimitOctaveNoise = new PerlinOctaveNoise(this.random, 16, true);
        this.maxLimitOctaveNoise = new PerlinOctaveNoise(this.random, 16, true);
        this.mainOctaveNoise = new PerlinOctaveNoise(this.random, 8, true);
        this.beachOctaveNoise = new PerlinOctaveNoise(this.random, 4, true);
        this.surfaceOctaveNoise = new PerlinOctaveNoise(this.random, 4, true);
        new PerlinOctaveNoise(this.random, 5, true); // Unused in original source
        this.forestOctaveNoise = new PerlinOctaveNoise(this.random, 5, true);
    }
    
    @Override
    public SpawnLocator getSpawnLocator() {
        return new SpawnLocatorBeta(this, this.beachOctaveNoise);
    }

    @Override
    public void provideSurface(ChunkRegion region, StructureAccessor structureAccessor, Chunk chunk, ModernBetaBiomeSource biomeSource, NoiseConfig noiseConfig) {
        double scale = 0.03125D;
        
        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;
        
        int startX = chunk.getPos().getStartX();
        int startZ = chunk.getPos().getStartZ();
        
        Random rand = this.createSurfaceRandom(chunkX, chunkZ);
        Random bedrockRand = this.createSurfaceRandom(chunkX, chunkZ);
        BlockPos.Mutable pos = new BlockPos.Mutable();

        AquiferSampler aquiferSampler = this.getAquiferSampler(chunk, noiseConfig);
        ChunkHeightmap heightmapChunk = this.getChunkHeightmap(chunkX, chunkZ);
        SimpleNoisePos noisePos = new SimpleNoisePos();

        for (int localX = 0; localX < 16; ++localX) {
            for (int localZ = 0; localZ < 16; ++localZ) {
                int x = startX + localX;
                int z = startZ + localZ;
                int surfaceTopY = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG).get(localX, localZ);
                int surfaceMinY = (this.hasNoisePostProcessor()) ? 
                    heightmapChunk.getHeight(x, z, ChunkHeightmap.Type.SURFACE_FLOOR) - 8 : 
                    this.worldMinY;
                
                boolean genSandBeach = this.beachOctaveNoise.sample(
                    x * scale,
                    z * scale,
                    0.0
                ) + rand.nextDouble() * 0.2 > 0.0;
                
                boolean genGravelBeach = this.beachOctaveNoise.sample(
                    z * scale, 
                    109.0134,
                    x * scale
                ) + rand.nextDouble() * 0.2 > 3.0;
                
                double surfaceNoise = this.surfaceOctaveNoise.sampleXY(
                    x * scale * 2.0,
                    z * scale * 2.0
                );
                
                int surfaceDepth = (int)(surfaceNoise / 3.0 + 3.0 + rand.nextDouble() * 0.25);
                
                int runDepth = -1;
                
                RegistryEntry<Biome> biome = biomeSource.getBiomeForSurfaceGen(region, pos.set(x, surfaceTopY, z));
                
                SurfaceConfig surfaceConfig = SurfaceConfig.getSurfaceConfig(biome);
                BlockState topBlock = surfaceConfig.normal().topBlock();
                BlockState fillerBlock = surfaceConfig.normal().fillerBlock();
                
                for (int y = this.worldTopY - 1; y >= this.worldMinY; --y) {
                    BlockState blockState;
                    
                    pos.set(localX, y, localZ);
                    blockState = chunk.getBlockState(pos);
                    
                    // Place bedrock
                    if (y <= this.bedrockFloor + bedrockRand.nextInt(5)) {
                        chunk.setBlockState(pos, BlockStates.BEDROCK, false);
                        continue;
                    }
                    
                    // Skip if at surface min y
                    if (y < surfaceMinY) {
                        continue;
                    }

                    if (blockState.isAir()) {
                        runDepth = -1;
                        
                    } else if (blockState.isOf(this.defaultBlock.getBlock())) {
                        if (runDepth == -1) {
                            if (surfaceDepth <= 0) {
                                topBlock = BlockStates.AIR;
                                fillerBlock = this.defaultBlock;
                                
                            } else if (y >= this.seaLevel - 4 && y <= this.seaLevel + 1) {
                                topBlock = surfaceConfig.normal().topBlock();
                                fillerBlock = surfaceConfig.normal().fillerBlock();
                                
                                if (genGravelBeach) {
                                    topBlock = surfaceConfig.beachGravel().topBlock();
                                    fillerBlock = surfaceConfig.beachGravel().fillerBlock();
                                }
                                
                                if (genSandBeach) {
                                    topBlock = surfaceConfig.beachSand().topBlock();
                                    fillerBlock = surfaceConfig.beachSand().fillerBlock();
                                }
                            }
                            
                            runDepth = surfaceDepth;
                            
                            if (y < this.seaLevel && topBlock.isAir()) { // Generate water bodies
                                BlockState fluidBlock = aquiferSampler.apply(noisePos.set(x, y, z), 0.0);
                                
                                boolean isAir = fluidBlock == null;
                                topBlock = isAir ? BlockStates.AIR : fluidBlock;
                                
                                this.scheduleFluidTick(chunk, aquiferSampler, pos, topBlock);
                            }
                            
                            blockState = (y >= this.seaLevel - 1 || (y < this.seaLevel - 1 && chunk.getBlockState(pos.up()).isAir())) ? 
                                topBlock : 
                                fillerBlock;
                            
                            chunk.setBlockState(pos, blockState, false);
                            
                        } else if (runDepth > 0) {
                            --runDepth;
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
        
        double islandOffset = this.getIslandOffset(noiseX, noiseZ);
        
        double coordinateScale = this.chunkSettings.noiseCoordinateScale;
        double heightScale = this.chunkSettings.noiseHeightScale;
        
        double mainNoiseScaleX = this.chunkSettings.noiseMainNoiseScaleX;
        double mainNoiseScaleY = this.chunkSettings.noiseMainNoiseScaleY;
        double mainNoiseScaleZ = this.chunkSettings.noiseMainNoiseScaleZ;

        double lowerLimitScale = this.chunkSettings.noiseLowerLimitScale;
        double upperLimitScale = this.chunkSettings.noiseUpperLimitScale;
        
        for (int y = 0; y < primaryBuffer.length; ++y) {
            int noiseY = y + this.noiseMinY;
            
            double density;
            double heightmapDensity;
            
            double densityOffset = this.getOffset(noiseY);
            
            // Default values: 8.55515, 1.71103, 8.55515
            double mainNoiseVal = this.mainOctaveNoise.sample(
                noiseX * coordinateScale / mainNoiseScaleX,
                noiseY * coordinateScale / mainNoiseScaleY, 
                noiseZ * coordinateScale / mainNoiseScaleZ
            ) / 2.0;
            
            // Do not clamp noise if generating with noise caves!
            if (mainNoiseVal < -1.0) {
                density = this.minLimitOctaveNoise.sample(
                    noiseX * coordinateScale, 
                    noiseY * heightScale, 
                    noiseZ * coordinateScale
                ) / lowerLimitScale;
                
                density -= densityOffset;
                density += islandOffset;
                
                density = this.clampNoise(density);
                
            } else if (mainNoiseVal > 1.0) {
                density = this.maxLimitOctaveNoise.sample(
                    noiseX * coordinateScale, 
                    noiseY * heightScale, 
                    noiseZ * coordinateScale
                ) / upperLimitScale;

                density -= densityOffset;
                density += islandOffset;
                
                density = this.clampNoise(density);
                
            } else {
                double minLimitVal = this.minLimitOctaveNoise.sample(
                    noiseX * coordinateScale, 
                    noiseY * heightScale, 
                    noiseZ * coordinateScale
                ) / lowerLimitScale;
                
                double maxLimitVal = this.maxLimitOctaveNoise.sample(
                    noiseX * coordinateScale, 
                    noiseY * heightScale, 
                    noiseZ * coordinateScale
                ) / upperLimitScale;
                
                minLimitVal -= densityOffset;
                maxLimitVal -= densityOffset;

                minLimitVal += islandOffset;
                maxLimitVal += islandOffset;

                minLimitVal = this.clampNoise(minLimitVal);
                maxLimitVal = this.clampNoise(maxLimitVal);
                                
                double delta = (mainNoiseVal + 1.0) / 2.0;
                density = minLimitVal + (maxLimitVal - minLimitVal) * delta;
            };
            
            // Sample without noise caves
            heightmapDensity = density;
            
            // Sample for noise caves
            density = this.sampleNoisePostProcessor(density, noiseX, noiseY, noiseZ);
            
            // Apply slides
            density = this.applySlides(density, y);
            heightmapDensity = this.applySlides(heightmapDensity, y);
            
            primaryBuffer[y] = density;
            heightmapBuffer[y] = heightmapDensity;
        }
    }
    
    @Override
    protected PerlinOctaveNoise getForestOctaveNoise() {
        return this.forestOctaveNoise;
    }
    
    private double clampNoise(double density) {
        if (this.hasNoisePostProcessor())
            return density;
        
        return MathHelper.clamp(density, -10D, 10D);
    }
    
    private double getOffset(int noiseY) {
        // Check if y (in scaled space) is below sealevel
        // and increase density accordingly.
        //double offset = y * 4.0 - 64.0;
        double offset = noiseY * this.noiseResolutionVertical - (double)this.seaLevel;
        
        if (offset < 0.0)
            offset *= 3.0;
        
        return offset;
    }
}
