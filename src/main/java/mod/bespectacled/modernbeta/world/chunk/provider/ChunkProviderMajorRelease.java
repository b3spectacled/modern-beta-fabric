package mod.bespectacled.modernbeta.world.chunk.provider;

import mod.bespectacled.modernbeta.api.world.chunk.ChunkProviderForcedHeight;
import mod.bespectacled.modernbeta.api.world.spawn.SpawnLocator;
import mod.bespectacled.modernbeta.util.BlockStates;
import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import mod.bespectacled.modernbeta.util.noise.SimplexOctaveNoise;
import mod.bespectacled.modernbeta.world.biome.HeightConfig;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bespectacled.modernbeta.world.spawn.SpawnLocatorRelease;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.noise.NoiseConfig;

import java.util.Random;

public class ChunkProviderMajorRelease extends ChunkProviderForcedHeight {
    private final PerlinOctaveNoise minLimitOctaveNoise;
    private final PerlinOctaveNoise maxLimitOctaveNoise;
    private final PerlinOctaveNoise mainOctaveNoise;
    private final SimplexOctaveNoise surfaceOctaveNoise;
    private final PerlinOctaveNoise depthOctaveNoise;
    private final PerlinOctaveNoise forestOctaveNoise;

    public ChunkProviderMajorRelease(ModernBetaChunkGenerator chunkGenerator, long seed) {
        super(chunkGenerator, seed);

        this.minLimitOctaveNoise = new PerlinOctaveNoise(this.random, 16, true);
        this.maxLimitOctaveNoise = new PerlinOctaveNoise(this.random, 16, true);
        this.mainOctaveNoise = new PerlinOctaveNoise(this.random, 8, true);
        this.surfaceOctaveNoise = new SimplexOctaveNoise(this.random, 4);
        new PerlinOctaveNoise(this.random, 10, true);
        this.depthOctaveNoise = new PerlinOctaveNoise(this.random, 16, true);
        this.forestOctaveNoise = new PerlinOctaveNoise(this.random, 8, true);
    }
    
    @Override
    public SpawnLocator getSpawnLocator() {
        return new SpawnLocatorRelease(this, new Random(this.seed));
    }

    @Override
    public void provideSurface(ChunkRegion region, StructureAccessor structureAccessor, Chunk chunk, ModernBetaBiomeSource biomeSource, NoiseConfig noiseConfig) {
        this.chunkGenerator.buildDefaultSurface(region, structureAccessor, noiseConfig, chunk);
        this.provideSurfaceExtra(region, structureAccessor, chunk, biomeSource, noiseConfig);
    }

    @Override
    public void provideSurfaceExtra(ChunkRegion region, StructureAccessor structureAccessor, Chunk chunk, ModernBetaBiomeSource biomeSource, NoiseConfig noiseConfig) {
        double scale = 0.03125;

        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;

        Random rand = this.createSurfaceRandom(chunkX, chunkZ);
        BlockPos.Mutable pos = new BlockPos.Mutable();

        for (int localZ = 0; localZ < 16; localZ++) {
            for (int localX = 0; localX < 16; localX++) {
                pos.set(localX, 0, localZ);
                int surfaceTopY = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG).get(localX, localZ) - 1;
                int surfaceDepth = (int)
                    (this.surfaceOctaveNoise.sample((chunkX * 16 + localX) * scale * 2D, (chunkZ * 16 + localZ) * scale * 2D, 1.5D, 1.0D)
                    / 3D + 3D + rand.nextDouble() * 0.25D);

                if (surfaceDepth <= 0) {
                    int y = surfaceTopY;
                    pos.setY(y);
                    chunk.setBlockState(pos, y < this.seaLevel ? BlockStates.WATER : BlockStates.AIR, false);
                    pos.setY(--y);

                    BlockState blockState;
                    while (!(blockState = chunk.getBlockState(pos)).isAir() && !blockState.isOf(this.defaultBlock.getBlock())) {
                        chunk.setBlockState(pos, this.defaultBlock, false);
                        pos.setY(--y);
                    }
                } else if (surfaceTopY < this.seaLevel - 7 - surfaceDepth) {
                    int y = surfaceTopY;
                    pos.setY(y);
                    chunk.setBlockState(pos, BlockStates.GRAVEL, false);
                    pos.setY(--y);

                    BlockState blockState;
                    while (!(blockState = chunk.getBlockState(pos)).isAir() && !blockState.isOf(this.defaultBlock.getBlock())) {
                        chunk.setBlockState(pos, this.defaultBlock, false);
                        pos.setY(--y);
                    }
                }

                for (int y = this.bedrockFloor; y < this.bedrockFloor + 5; y++) {
                    if (y <= this.bedrockFloor + this.random.nextInt(5)) {
                        pos.setY(y);
                        chunk.setBlockState(pos, BlockStates.BEDROCK, false);
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
        
        double depthNoiseScaleX = this.chunkSettings.noiseDepthNoiseScaleX;
        double depthNoiseScaleZ = this.chunkSettings.noiseDepthNoiseScaleZ;
        
        double coordinateScale = this.chunkSettings.noiseCoordinateScale;
        double heightScale = this.chunkSettings.noiseHeightScale;
        
        double mainNoiseScaleX = this.chunkSettings.noiseMainNoiseScaleX;
        double mainNoiseScaleY = this.chunkSettings.noiseMainNoiseScaleY;
        double mainNoiseScaleZ = this.chunkSettings.noiseMainNoiseScaleZ;

        double lowerLimitScale = this.chunkSettings.noiseLowerLimitScale;
        double upperLimitScale = this.chunkSettings.noiseUpperLimitScale;
        
        double baseSize = this.chunkSettings.noiseBaseSize;
        double heightStretch = this.chunkSettings.noiseStretchY;
        
        double depth = this.depthOctaveNoise.sampleXZWrapped(noiseX, noiseZ, depthNoiseScaleX, depthNoiseScaleZ);

        HeightConfig heightConfig = this.getHeightConfigAt(noiseX, noiseZ);

        depth /= 8000D;

        if (depth < 0.0D) {
            depth = -depth * 0.3D;
        }

        depth = depth * 3D - 2D;

        if (depth < 0.0D) {
            depth /= 2D;

            if (depth < -1D) {
                depth = -1D;
            }

            depth /= 1.4D;
            depth /= 2D;
        } else {
            if (depth > 1.0D) {
                depth = 1.0D;
            }
            depth /= 8D;
        }

        depth = heightConfig.depth() + depth * 0.2D;
        depth *= baseSize / 8.0D;
        depth = baseSize + depth * 4.0D;
        
        for (int y = 0; y < primaryBuffer.length; ++y) {
            int noiseY = y + this.noiseMinY;
            
            double density;
            double heightmapDensity;
            
            double densityOffset = this.getOffset(noiseY, heightStretch, depth, heightConfig.scale());
                       
            double mainNoise = (this.mainOctaveNoise.sampleWrapped(
                noiseX, noiseY, noiseZ,
                coordinateScale / mainNoiseScaleX, 
                heightScale / mainNoiseScaleY, 
                coordinateScale / mainNoiseScaleZ
            ) / 10D + 1.0D) / 2D;
            
            if (mainNoise < 0.0D) {
                density = this.minLimitOctaveNoise.sampleWrapped(
                    noiseX, noiseY, noiseZ,
                    coordinateScale, 
                    heightScale, 
                    coordinateScale
                ) / lowerLimitScale;
                
            } else if (mainNoise > 1.0D) {
                density = this.maxLimitOctaveNoise.sampleWrapped(
                    noiseX, noiseY, noiseZ,
                    coordinateScale, 
                    heightScale, 
                    coordinateScale
                ) / upperLimitScale;
                
            } else {
                double minLimitNoise = this.minLimitOctaveNoise.sampleWrapped(
                    noiseX, noiseY, noiseZ,
                    coordinateScale, 
                    heightScale, 
                    coordinateScale
                ) / lowerLimitScale;
                
                double maxLimitNoise = this.maxLimitOctaveNoise.sampleWrapped(
                    noiseX, noiseY, noiseZ,
                    coordinateScale, 
                    heightScale, 
                    coordinateScale
                ) / upperLimitScale;
                
                density = minLimitNoise + (maxLimitNoise - minLimitNoise) * mainNoise;
            }
            
            density -= densityOffset;
            density += islandOffset;
            
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
    
    private double getOffset(int noiseY, double heightStretch, double depth, double scale) {
        double offset = (((double)noiseY - depth) * heightStretch) / scale;
        
        if (offset < 0D)
            offset *= 4D;
        
        return offset;
    }
}
