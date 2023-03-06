package mod.bespectacled.modernbeta.world.chunk.provider;

import java.util.Random;

import mod.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import mod.bespectacled.modernbeta.api.world.biome.climate.Clime;
import mod.bespectacled.modernbeta.api.world.chunk.ChunkProviderNoise;
import mod.bespectacled.modernbeta.api.world.chunk.SurfaceConfig;
import mod.bespectacled.modernbeta.api.world.spawn.SpawnLocator;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsBiome;
import mod.bespectacled.modernbeta.util.BlockStates;
import mod.bespectacled.modernbeta.util.chunk.ChunkHeightmap;
import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import mod.bespectacled.modernbeta.util.noise.SimpleNoisePos;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.biome.provider.BiomeProviderBeta;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bespectacled.modernbeta.world.spawn.SpawnLocatorBeta;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.noise.NoiseConfig;

public class ChunkProviderBeta extends ChunkProviderNoise {
    private final PerlinOctaveNoise minLimitOctaveNoise;
    private final PerlinOctaveNoise maxLimitOctaveNoise;
    private final PerlinOctaveNoise mainOctaveNoise;
    private final PerlinOctaveNoise beachOctaveNoise;
    private final PerlinOctaveNoise surfaceOctaveNoise;
    private final PerlinOctaveNoise scaleOctaveNoise;
    private final PerlinOctaveNoise depthOctaveNoise;
    private final PerlinOctaveNoise forestOctaveNoise;
    
    private final ClimateSampler climateSampler;
    
    public ChunkProviderBeta(ModernBetaChunkGenerator chunkGenerator, long seed) {
        super(chunkGenerator, seed);
        
        this.minLimitOctaveNoise = new PerlinOctaveNoise(this.random, 16, true);
        this.maxLimitOctaveNoise = new PerlinOctaveNoise(this.random, 16, true);
        this.mainOctaveNoise = new PerlinOctaveNoise(this.random, 8, true);
        this.beachOctaveNoise = new PerlinOctaveNoise(this.random, 4, true);
        this.surfaceOctaveNoise = new PerlinOctaveNoise(this.random, 4, true);
        this.scaleOctaveNoise = new PerlinOctaveNoise(this.random, 10, true);
        this.depthOctaveNoise = new PerlinOctaveNoise(this.random, 16, true);
        this.forestOctaveNoise = new PerlinOctaveNoise(this.random, 8, true);
        
        this.climateSampler = (
            this.chunkGenerator.getBiomeSource() instanceof ModernBetaBiomeSource biomeSource &&
            biomeSource.getBiomeProvider() instanceof BiomeProviderBeta biomeProviderBeta
        ) ? biomeProviderBeta : new BiomeProviderBeta(new ModernBetaSettingsBiome().toCompound(), null, seed);
    }
    
    @Override
    public SpawnLocator getSpawnLocator() {
        return new SpawnLocatorBeta(this, this.beachOctaveNoise);
    }
    
    @Override
    public void provideSurface(ChunkRegion region, Chunk chunk, ModernBetaBiomeSource biomeSource, NoiseConfig noiseConfig) {
        double scale = 0.03125;

        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;
        
        int startX = chunk.getPos().getStartX();
        int startZ = chunk.getPos().getStartZ();
        
        int bedrockFloor = this.worldMinY + this.bedrockFloor;
        
        Random rand = this.createSurfaceRandom(chunkX, chunkZ);
        BlockPos.Mutable pos = new BlockPos.Mutable();
        
        AquiferSampler aquiferSampler = this.getAquiferSampler(chunk, noiseConfig);
        ChunkHeightmap heightmapChunk = this.getChunkHeightmap(chunkX, chunkZ);
        SimpleNoisePos noisePos = new SimpleNoisePos();
        
        double[] sandNoise = beachOctaveNoise.sampleBeta(
            chunkX * 16, chunkZ * 16, 0.0D, 
            16, 16, 1,
            scale, scale, 1.0D);
        
        double[] gravelNoise = beachOctaveNoise.sampleBeta(
            chunkX * 16, 109.0134D, chunkZ * 16, 
            16, 1, 16, 
            scale, 1.0D, scale);
        
        double[] surfaceNoise = surfaceOctaveNoise.sampleBeta(
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
                
                boolean genSandBeach = sandNoise[localZ + localX * 16] + rand.nextDouble() * 0.2D > 0.0D;
                boolean genGravelBeach = gravelNoise[localZ + localX * 16] + rand.nextDouble() * 0.2D > 3D;
                int surfaceDepth = (int) (surfaceNoise[localZ + localX * 16] / 3D + 3D + rand.nextDouble() * 0.25D);
                
                int runDepth = -1;
                
                RegistryEntry<Biome> biome = biomeSource.getBiomeForSurfaceGen(region, pos.set(x, surfaceTopY, z));

                SurfaceConfig surfaceConfig = SurfaceConfig.getSurfaceConfig(biome);
                BlockState topBlock = surfaceConfig.normal().topBlock();
                BlockState fillerBlock = surfaceConfig.normal().fillerBlock();
                
                // Generate from top to bottom of world
                for (int y = this.worldTopY - 1; y >= this.worldMinY; y--) {
                    BlockState blockState;
                    
                    pos.set(localX, y, localZ);
                    blockState = chunk.getBlockState(pos);
                    
                    // Place bedrock
                    if (y <= bedrockFloor + rand.nextInt(5)) {
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
    protected void sampleNoiseColumn(double[] primaryBuffer, double[] heightmapBuffer, int startNoiseX, int startNoiseZ, int localNoiseX, int localNoiseZ) {
        int horizNoiseResolution = 16 / (this.noiseSizeX + 1);
        int x = (startNoiseX / this.noiseSizeX * 16) + localNoiseX * horizNoiseResolution + horizNoiseResolution / 2;
        int z = (startNoiseZ / this.noiseSizeZ * 16) + localNoiseZ * horizNoiseResolution + horizNoiseResolution / 2;
        
        int noiseX = startNoiseX + localNoiseX;
        int noiseZ = startNoiseZ + localNoiseZ;
        
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
        
        double islandOffset = this.getIslandOffset(startNoiseX, startNoiseZ, localNoiseX, localNoiseZ);

        Clime clime = this.climateSampler.sample(x, z);
        double temp = clime.temp();
        double rain = clime.rain() * temp;
        
        rain = 1.0D - rain;
        rain *= rain;
        rain *= rain;
        rain = 1.0D - rain;

        double scale = this.scaleOctaveNoise.sampleXZ(noiseX, noiseZ, 1.121D, 1.121D);
        scale = (scale + 256D) / 512D;
        scale *= rain;
        
        if (scale > 1.0D) {
            scale = 1.0D;
        }
        
        double depth = this.depthOctaveNoise.sampleXZ(noiseX, noiseZ, depthNoiseScaleX, depthNoiseScaleZ);
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

            scale = 0.0D;

        } else {
            if (depth > 1.0D) {
                depth = 1.0D;
            }
            depth /= 8D;
        }

        if (scale < 0.0D) {
            scale = 0.0D;
        }

        scale += 0.5D;
        depth = depth * baseSize / 8D;
        depth = baseSize + depth * 4D;
        
        for (int y = 0; y < primaryBuffer.length; ++y) {
            int noiseY = y + this.noiseMinY;
            
            double density;
            double heightmapDensity;
            
            double densityOffset = this.getOffset(noiseY, heightStretch, depth, scale);
                       
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
