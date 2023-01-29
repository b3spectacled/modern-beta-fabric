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

public class Infdev415ChunkProvider extends NoiseChunkProvider {
    private final PerlinOctaveNoise minLimitOctaveNoise;
    private final PerlinOctaveNoise maxLimitOctaveNoise;
    private final PerlinOctaveNoise mainOctaveNoise;
    private final PerlinOctaveNoise beachOctaveNoise;
    private final PerlinOctaveNoise surfaceOctaveNoise;
    private final PerlinOctaveNoise forestOctaveNoise;
    
    public Infdev415ChunkProvider(ModernBetaChunkGenerator chunkGenerator) {
        super(chunkGenerator);
        
        // Noise Generators
        this.minLimitOctaveNoise = new PerlinOctaveNoise(random, 16, true);
        this.maxLimitOctaveNoise = new PerlinOctaveNoise(random, 16, true);
        this.mainOctaveNoise = new PerlinOctaveNoise(random, 8, true);
        this.beachOctaveNoise = new PerlinOctaveNoise(random, 4, true);
        this.surfaceOctaveNoise = new PerlinOctaveNoise(random, 4, true);
        new PerlinOctaveNoise(random, 5, true); // Unused in original source
        this.forestOctaveNoise = new PerlinOctaveNoise(random, 5, true);

        setForestOctaves(forestOctaveNoise);
        
        this.spawnLocator = new BeachSpawnLocator(this, this.beachOctaveNoise);
    }

    @Override
    public void provideSurface(ChunkRegion region, Chunk chunk, ModernBetaBiomeSource biomeSource) {
        double scale = 0.03125D;
        
        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;
        
        int startX = chunk.getPos().getStartX();
        int startZ = chunk.getPos().getStartZ();
        
        int bedrockFloor = this.worldMinY + this.bedrockFloor;
        
        Random rand = this.createSurfaceRandom(chunkX, chunkZ);
        Random bedrockRand = this.createSurfaceRandom(chunkX, chunkZ);
        BlockPos.Mutable pos = new BlockPos.Mutable();

        AquiferSampler aquiferSampler = this.getAquiferSampler(chunk);
        HeightmapChunk heightmapChunk = this.getHeightmapChunk(chunkX, chunkZ);
        SimpleNoisePos noisePos = new SimpleNoisePos();

        // Surface builder stuff
        BlockColumnHolder blockColumn = new BlockColumnHolder(chunk);
        ModernBetaSurfaceRules surfaceRules = new ModernBetaSurfaceRules(region, chunk, this.chunkGenerator);

        for (int localX = 0; localX < 16; ++localX) {
            for (int localZ = 0; localZ < 16; ++localZ) {
                int x = startX + localX;
                int z = startZ + localZ;
                int surfaceTopY = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG).get(localX, localZ);
                int surfaceMinY = (this.hasNoisePostProcessor()) ? 
                    heightmapChunk.getHeight(x, z, HeightmapChunk.Type.SURFACE_FLOOR) - 8 : 
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
                
                for (int y = this.worldTopY - 1; y >= this.worldMinY; --y) {
                    BlockState blockState;
                    
                    pos.set(localX, y, localZ);
                    blockState = chunk.getBlockState(pos);
                    
                    // Place deepslate
                    BlockState deepslateState = this.sampleDeepslateState(x, y, z);
                    if (deepslateState != null && blockState.isOf(this.defaultBlock.getBlock())) {
                        chunk.setBlockState(pos, deepslateState, false);
                    }
                    
                    // Place bedrock
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
                    
                    if (blockState.isAir()) {
                        runDepth = -1;
                        
                    } else if (blockState.isOf(this.defaultBlock.getBlock())) {
                        if (runDepth == -1) {
                            if (surfaceDepth <= 0) {
                                topBlock = BlockStates.AIR;
                                fillerBlock = this.defaultBlock;
                                
                            } else if (y >= this.seaLevel - 4 && y <= this.seaLevel + 1) {
                                topBlock = surfaceConfig.topBlock();
                                fillerBlock = surfaceConfig.fillerBlock();
                                
                                if (genGravelBeach) {
                                    topBlock = BlockStates.AIR;
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
        
        double coordinateScale = 684.412D * this.xzScale; 
        double heightScale = 984.412D * this.yScale;
        
        double mainNoiseScaleX = this.xzFactor; // Default: 80
        double mainNoiseScaleY = this.yFactor;  // Default: 400
        double mainNoiseScaleZ = this.xzFactor;
        
        double limitScale = 512.0D;
        
        // Density norm (sum of 16 octaves of noise / limitScale => [-128, 128])
        // This affects terrain so only scale terrain when generating with noise caves.
        double densityScale = this.hasNoisePostProcessor() ? 128.0 : 1.0;
        
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
                ) / limitScale;
                
                density -= densityOffset;
                density /= densityScale;
                
                density = this.clampNoise(density);
                
            } else if (mainNoiseVal > 1.0) {
                density = this.maxLimitOctaveNoise.sample(
                    noiseX * coordinateScale, 
                    noiseY * heightScale, 
                    noiseZ * coordinateScale
                ) / limitScale;

                density -= densityOffset;
                density /= densityScale;
                
                density = this.clampNoise(density);
                
            } else {
                double minLimitVal = this.minLimitOctaveNoise.sample(
                    noiseX * coordinateScale, 
                    noiseY * heightScale, 
                    noiseZ * coordinateScale
                ) / limitScale;
                
                double maxLimitVal = this.maxLimitOctaveNoise.sample(
                    noiseX * coordinateScale, 
                    noiseY * heightScale, 
                    noiseZ * coordinateScale
                ) / limitScale;
                
                minLimitVal -= densityOffset;
                maxLimitVal -= densityOffset;
                
                minLimitVal /= densityScale;
                maxLimitVal /= densityScale;
                
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
            
            primaryBuffer[y] = MathHelper.clamp(density, -64.0, 64.0);
            heightmapBuffer[y] = MathHelper.clamp(heightmapDensity, -64.0, 64.0);
        }
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
        double offset = noiseY * this.verticalNoiseResolution - (double)this.seaLevel;
        
        if (offset < 0.0)
            offset *= 3.0;
        
        return offset;
    }
}
