package mod.bespectacled.modernbeta.world.chunk.provider;

import mod.bespectacled.modernbeta.api.world.chunk.ChunkProviderNoise;
import mod.bespectacled.modernbeta.api.world.chunk.surface.SurfaceBlocks;
import mod.bespectacled.modernbeta.api.world.chunk.surface.SurfaceConfig;
import mod.bespectacled.modernbeta.api.world.spawn.SpawnLocator;
import mod.bespectacled.modernbeta.util.BlockStates;
import mod.bespectacled.modernbeta.util.chunk.ChunkHeightmap;
import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import mod.bespectacled.modernbeta.util.noise.SimpleNoisePos;
import mod.bespectacled.modernbeta.world.biome.HeightConfig;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.biome.provider.fractal.BiomeInfo;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bespectacled.modernbeta.world.spawn.SpawnLocatorEarlyRelease;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ChunkProviderEarlyRelease extends ChunkProviderNoise {
    private static final float[] BIOME_HEIGHT_WEIGHTS = new float[25];

    private final PerlinOctaveNoise minLimitOctaveNoise;
    private final PerlinOctaveNoise maxLimitOctaveNoise;
    private final PerlinOctaveNoise mainOctaveNoise;
    private final PerlinOctaveNoise surfaceOctaveNoise;
    private final PerlinOctaveNoise depthOctaveNoise;
    private final PerlinOctaveNoise forestOctaveNoise;
    private final Map<BiomeInfo, HeightConfig> heightOverrideCache;

    static {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                float value = 10.0F / MathHelper.sqrt((float)(x * x + z * z) + 0.2F);
                BIOME_HEIGHT_WEIGHTS[x + 2 + (z + 2) * 5] = value;
            }
        }
    }

    public ChunkProviderEarlyRelease(ModernBetaChunkGenerator chunkGenerator, long seed) {
        super(chunkGenerator, seed);

        this.minLimitOctaveNoise = new PerlinOctaveNoise(this.random, 16, true);
        this.maxLimitOctaveNoise = new PerlinOctaveNoise(this.random, 16, true);
        this.mainOctaveNoise = new PerlinOctaveNoise(this.random, 8, true);
        this.surfaceOctaveNoise = new PerlinOctaveNoise(this.random, 4, true);
        new PerlinOctaveNoise(this.random, 10, true);
        this.depthOctaveNoise = new PerlinOctaveNoise(this.random, 16, true);
        this.forestOctaveNoise = new PerlinOctaveNoise(this.random, 8, true);

        this.heightOverrideCache = new HashMap<>();
    }
    
    @Override
    public SpawnLocator getSpawnLocator() {
        return new SpawnLocatorEarlyRelease(this, new Random(this.seed));
    }

    @Override
    public void provideSurface(ChunkRegion region, StructureAccessor structureAccessor, Chunk chunk, ModernBetaBiomeSource biomeSource, NoiseConfig noiseConfig) {
        double scale = 0.03125;

        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;
        
        int startX = chunk.getPos().getStartX();
        int startZ = chunk.getPos().getStartZ();
        
        Random rand = this.createSurfaceRandom(chunkX, chunkZ);
        BlockPos.Mutable pos = new BlockPos.Mutable();
        
        AquiferSampler aquiferSampler = this.getAquiferSampler(chunk, noiseConfig);
        ChunkHeightmap heightmapChunk = this.getChunkHeightmap(chunkX, chunkZ);
        SimpleNoisePos noisePos = new SimpleNoisePos();

        double[] surfaceNoise = surfaceOctaveNoise.sampleRelease(
            chunkX * 16, chunkZ * 16, 0.0D,
            16, 16, 1,
            scale * 2D, scale * 2D, scale * 2D
        );

        for (int localZ = 0; localZ < 16; localZ++) {
            for (int localX = 0; localX < 16; localX++) {
                int x = startX + localX;
                int z = startZ + localZ;
                int surfaceTopY = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG).get(localX, localZ);
                int surfaceMinY = (this.hasNoisePostProcessor()) ? 
                    heightmapChunk.getHeight(x, z, ChunkHeightmap.Type.SURFACE_FLOOR) - 8 : 
                    this.worldMinY;

                int surfaceDepth = (int) (surfaceNoise[localZ + localX * 16] / 3D + 3D + rand.nextDouble() * 0.25D);

                int runDepth = -1;
                
                RegistryEntry<Biome> biome = biomeSource.getBiomeForSurfaceGen(region, pos.set(x, surfaceTopY, z));

                SurfaceConfig surfaceConfig = this.surfaceBuilder.getSurfaceConfig(biome);
                BlockState topBlock = surfaceConfig.normal().topBlock();
                BlockState fillerBlock = surfaceConfig.normal().fillerBlock();

                // Generate from top to bottom of world
                for (int y = this.worldTopY - 1; y >= this.worldMinY; y--) {
                    BlockState blockState;
                    
                    pos.set(localX, y, localZ);
                    blockState = chunk.getBlockState(pos);
                    
                    // Place bedrock
                    if (y <= this.bedrockFloor + rand.nextInt(5)) {
                        chunk.setBlockState(pos, BlockStates.BEDROCK, false);
                        continue;
                    }
                    
                    // Skip if at surface min y
                    if (y < surfaceMinY) {
                        continue;
                    }
                    
                    if (blockState.isAir()) { // Skip if air block
                        runDepth = -1;
                        continue;
                    }

                    if (!blockState.isOf(this.defaultBlock.getBlock())) { // Skip if not stone
                        continue;
                    }
                    
                    // At the first default block
                    if (runDepth == -1) {
                        if (surfaceDepth <= 0) { // Generate stone basin if noise permits
                            topBlock = BlockStates.AIR;
                            fillerBlock = this.defaultBlock;
                            
                        } else if (y >= this.seaLevel - 4 && y <= this.seaLevel + 1) { // Generate beaches at this y range
                            topBlock = surfaceConfig.normal().topBlock();
                            fillerBlock = surfaceConfig.normal().fillerBlock();
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

                        continue;
                    }

                    if (runDepth <= 0) {
                        continue;
                    }

                    runDepth--;
                    chunk.setBlockState(pos, fillerBlock, false);

                    // Generates layer of sandstone starting at lowest block of sand, of height 1 to 4.
                    if (runDepth == 0 && fillerBlock.isOf(Blocks.SAND)) {
                        runDepth = rand.nextInt(4);
                        fillerBlock = BlockStates.SANDSTONE;
                    }
                    
                    if (runDepth == 0 && fillerBlock.isOf(Blocks.RED_SAND)) {
                        runDepth = rand.nextInt(4);
                        fillerBlock = BlockStates.RED_SANDSTONE;
                    }
                }
            }
        }
    }

    @Override
    public void provideSurfaceExtra(ChunkRegion region, StructureAccessor structureAccessor, Chunk chunk, ModernBetaBiomeSource biomeSource, NoiseConfig noiseConfig) {
        double scale = 0.03125;

        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;

        Random rand = this.createSurfaceRandom(chunkX, chunkZ);
        BlockPos.Mutable pos = new BlockPos.Mutable();

        double[] surfaceNoise = surfaceOctaveNoise.sampleBeta(
            chunkX * 16, chunkZ * 16, 0.0D,
            16, 16, 1,
            scale * 2D, scale * 2D, scale * 2D
        );

        for (int localZ = 0; localZ < 16; localZ++) {
            for (int localX = 0; localX < 16; localX++) {
                int surfaceTopY = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG).get(localX, localZ);
                int surfaceDepth = (int) (surfaceNoise[localZ + localX * 16] / 3D + 3D + rand.nextDouble() * 0.25D);

                if (surfaceDepth <= 0) {
                    int y = surfaceTopY;
                    pos.set(localX, y, localZ);
                    chunk.setBlockState(pos, BlockStates.AIR, false);
                    pos.setY(--y);

                    BlockState blockState;
                    while (!(blockState = chunk.getBlockState(pos)).isAir() && !blockState.isOf(this.defaultBlock.getBlock())) {
                        chunk.setBlockState(pos, this.defaultBlock, false);
                        pos.setY(--y);
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

        float biomeHeightStretch = 0.0F;
        float biomeSurfaceHeight = 0.0F;
        float totalBiomeHeightWeight = 0.0F;

        BiomeInfo biome = this.getBiomeInfo(noiseX, noiseZ);
        double minSurfaceHeight = this.getHeightConfig(biome).scale();

        for (int biomeX = -2; biomeX <= 2; biomeX++) {
            for (int biomeZ = -2; biomeZ <= 2; biomeZ++) {
                biome = this.getBiomeInfo(noiseX + biomeX, noiseZ + biomeZ);
                HeightConfig heightConfig = this.getHeightConfig(biome);

                float weight = BIOME_HEIGHT_WEIGHTS[biomeX + 2 + (biomeZ + 2) * 5] / (heightConfig.scale() + 2.0F);
                if (heightConfig.scale() > minSurfaceHeight) {
                    weight /= 2.0F;
                }

                biomeHeightStretch += heightConfig.depth() * weight;
                biomeSurfaceHeight += heightConfig.scale() * weight;
                totalBiomeHeightWeight += weight;
            }
        }

        biomeHeightStretch /= totalBiomeHeightWeight;
        biomeSurfaceHeight /= totalBiomeHeightWeight;
        biomeHeightStretch = biomeHeightStretch * 0.9F + 0.1F;
        biomeSurfaceHeight = (biomeSurfaceHeight * 4.0F - 1.0F) / 8.0F;

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

        depth = biomeSurfaceHeight + depth * 0.2D;
        depth *= baseSize / 8.0D;
        depth = baseSize + depth * 4.0D;
        
        for (int y = 0; y < primaryBuffer.length; ++y) {
            int noiseY = y + this.noiseMinY;
            
            double density;
            double heightmapDensity;
            
            double densityOffset = this.getOffset(noiseY, heightStretch, depth, biomeHeightStretch);
                       
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

    private BiomeInfo getBiomeInfo(int biomeX, int biomeZ) {
        if (chunkGenerator.getBiomeSource() instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
            return modernBetaBiomeSource.getBiomeForHeightGen(biomeX, 16, biomeZ);
        } else {
            return BiomeInfo.of(this.getBiome(biomeX, 16, biomeZ, null));
        }
    }

    private HeightConfig getHeightConfig(BiomeInfo biomeInfo) {
        HeightConfig config = HeightConfig.getHeightConfig(biomeInfo);

        if (this.chunkSettings.releaseExtraHillHeight) {
            if (config.equals(HeightConfig.HILLS)) config = HeightConfig.HILLS_1_3;
            else if (config.equals(HeightConfig.SHORT_HILLS)) config = HeightConfig.SHORT_HILLS_1_3;
        }

        String id = biomeInfo.getId();
        if (this.chunkSettings.releaseHeightOverrides.containsKey(id)) {
            HeightConfig fallbackConfig = config;
            config = this.heightOverrideCache.computeIfAbsent(biomeInfo, k -> {
                String heightConfigString = this.chunkSettings.releaseHeightOverrides.get(id);
                String[] heightConfigPair = heightConfigString.split(";");
                try {
                    float scale = Float.parseFloat(heightConfigPair[0]);
                    float depth = Float.parseFloat(heightConfigPair[1]);
                    return new HeightConfig(scale, depth);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {
                    return fallbackConfig;
                }
            });
        }

        return config;
    }
}
