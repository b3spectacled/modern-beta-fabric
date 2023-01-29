package mod.bespectacled.modernbeta.world.gen.provider;

import java.util.Random;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import mod.bespectacled.modernbeta.api.world.biome.climate.Clime;
import mod.bespectacled.modernbeta.api.world.gen.NoiseChunkProvider;
import mod.bespectacled.modernbeta.api.world.gen.SurfaceConfig;
import mod.bespectacled.modernbeta.util.BlockColumnHolder;
import mod.bespectacled.modernbeta.util.BlockStates;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import mod.bespectacled.modernbeta.util.chunk.HeightmapChunk;
import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import mod.bespectacled.modernbeta.util.noise.SimpleNoisePos;
import mod.bespectacled.modernbeta.util.noise.SimplexNoise;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.biome.provider.BetaBiomeProvider;
import mod.bespectacled.modernbeta.world.biome.provider.settings.BiomeProviderSettings;
import mod.bespectacled.modernbeta.world.gen.ModernBetaChunkGenerator;
import mod.bespectacled.modernbeta.world.gen.ModernBetaSurfaceRules;
import mod.bespectacled.modernbeta.world.spawn.BeachSpawnLocator;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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

public class BetaIslandsChunkProvider extends NoiseChunkProvider {
    private final PerlinOctaveNoise minLimitNoiseOctaves;
    private final PerlinOctaveNoise maxLimitNoiseOctaves;
    private final PerlinOctaveNoise mainNoiseOctaves;
    private final PerlinOctaveNoise beachNoiseOctaves;
    private final PerlinOctaveNoise surfaceNoiseOctaves;
    private final PerlinOctaveNoise scaleNoiseOctaves;
    private final PerlinOctaveNoise depthNoiseOctaves;
    private final PerlinOctaveNoise forestNoiseOctaves;
    
    private final SimplexNoise islandNoise;
    
    private final boolean generateOuterIslands;
    private final int centerIslandRadius;
    private final float centerIslandFalloff;
    private final int centerOceanLerpDistance;
    private final int centerOceanRadius;
    private final float outerIslandNoiseScale;
    private final float outerIslandNoiseOffset;

    private final ClimateSampler climateSampler;
    
    public BetaIslandsChunkProvider(ModernBetaChunkGenerator chunkGenerator) {
        super(chunkGenerator);
        
        // Noise Generators
        this.minLimitNoiseOctaves = new PerlinOctaveNoise(random, 16, true);
        this.maxLimitNoiseOctaves = new PerlinOctaveNoise(random, 16, true);
        this.mainNoiseOctaves = new PerlinOctaveNoise(random, 8, true);
        this.beachNoiseOctaves = new PerlinOctaveNoise(random, 4, true);
        this.surfaceNoiseOctaves = new PerlinOctaveNoise(random, 4, true);
        this.scaleNoiseOctaves = new PerlinOctaveNoise(random, 10, true);
        this.depthNoiseOctaves = new PerlinOctaveNoise(random, 16, true);
        this.forestNoiseOctaves = new PerlinOctaveNoise(random, 8, true);
        this.islandNoise = new SimplexNoise(random);

        setForestOctaves(forestNoiseOctaves);
        
        // Get climate sampler from biome provider if exists and enabled,
        // else create new default Beta climate sampler.
        boolean sampleClimate = NbtUtil.toBoolean(
            this.providerSettings.get(NbtTags.SAMPLE_CLIMATE), 
            ModernBeta.CHUNK_CONFIG.sampleClimate
        );

        this.climateSampler = (
            chunkGenerator.getBiomeSource() instanceof ModernBetaBiomeSource oldBiomeSource &&
            oldBiomeSource.getBiomeProvider() instanceof ClimateSampler climateSampler &&
            sampleClimate
        ) ? climateSampler : new BetaBiomeProvider(chunkGenerator.getWorldSeed(), BiomeProviderSettings.createSettingsBeta(), null);
        this.spawnLocator = new BeachSpawnLocator(this, this.beachNoiseOctaves);
        
        // Beta Islands settings
        this.generateOuterIslands = NbtUtil.toBoolean(
            this.providerSettings.get(NbtTags.ISLANDS_USE_OUTER_ISLANDS),
            ModernBeta.CHUNK_CONFIG.islandsUseOuterIslands
        );
        
        this.centerIslandRadius = NbtUtil.toInt(
            this.providerSettings.get(NbtTags.ISLANDS_CENTER_ISLAND_RADIUS),
            ModernBeta.CHUNK_CONFIG.islandsCenterIslandRadius
        );
        
        this.centerIslandFalloff = NbtUtil.toFloat(
            this.providerSettings.get(NbtTags.ISLANDS_CENTER_ISLAND_FALLOFF),
            ModernBeta.CHUNK_CONFIG.islandsCenterIslandFalloff
        );
        
        this.centerOceanLerpDistance = NbtUtil.toInt(
            this.providerSettings.get(NbtTags.ISLANDS_CENTER_OCEAN_FALLOFF_DIST),
            ModernBeta.CHUNK_CONFIG.islandsCenterOceanFalloffDistance
        );
        
        this.centerOceanRadius = NbtUtil.toInt(
            this.providerSettings.get(NbtTags.ISLANDS_CENTER_ISLAND_RADIUS),
            ModernBeta.CHUNK_CONFIG.islandsCenterOceanRadius
        );
        
        this.outerIslandNoiseScale = NbtUtil.toFloat(
            this.providerSettings.get(NbtTags.ISLANDS_OUTER_ISLAND_NOISE_SCALE),
            ModernBeta.CHUNK_CONFIG.islandsOuterIslandNoiseScale
        );
        
        this.outerIslandNoiseOffset = NbtUtil.toFloat(
            this.providerSettings.get(NbtTags.ISLANDS_OUTER_ISLAND_NOISE_OFFSET),
            ModernBeta.CHUNK_CONFIG.islandsOuterIslandNoiseOffset
        );     
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
        
        double[] sandNoise = beachNoiseOctaves.sampleBeta(
            chunkX * 16, chunkZ * 16, 0.0D, 
            16, 16, 1,
            scale, scale, 1.0D);
        
        double[] gravelNoise = beachNoiseOctaves.sampleBeta(
            chunkX * 16, 109.0134D, chunkZ * 16, 
            16, 1, 16, 
            scale, 1.0D, scale);
        
        double[] surfaceNoise = surfaceNoiseOctaves.sampleBeta(
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
                    heightmapChunk.getHeight(x, z, HeightmapChunk.Type.SURFACE_FLOOR) - 8 : 
                    this.worldMinY;
                
                boolean genSandBeach = sandNoise[localZ + localX * 16] + rand.nextDouble() * 0.2D > 0.0D;
                boolean genGravelBeach = gravelNoise[localZ + localX * 16] + rand.nextDouble() * 0.2D > 3D;
                int surfaceDepth = (int) (surfaceNoise[localZ + localX * 16] / 3D + 3D + rand.nextDouble() * 0.25D);
                
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
                    if (y <= bedrockFloor + rand.nextInt(5)) {
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
                    
                    // At the first default block
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
        
        double depthNoiseScaleX = 200D;
        double depthNoiseScaleZ = 200D;
        
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

        Clime clime = this.climateSampler.sample(x, z);
        double temp = clime.temp();
        double rain = clime.rain() * temp;
        
        rain = 1.0D - rain;
        rain *= rain;
        rain *= rain;
        rain = 1.0D - rain;
    
        double scale = this.scaleNoiseOctaves.sampleXZ(noiseX, noiseZ, 1.121D, 1.121D);
        scale = (scale + 256D) / 512D;
        scale *= rain;
        
        if (scale > 1.0D) {
            scale = 1.0D;
        }
        
        double depth = this.depthNoiseOctaves.sampleXZ(noiseX, noiseZ, depthNoiseScaleX, depthNoiseScaleZ);
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
        
        double islandOffset = this.generateIslandOffset(
            startNoiseX, 
            startNoiseZ, 
            localNoiseX, 
            localNoiseZ
        );
        
        for (int y = 0; y < primaryBuffer.length; ++y) {
            int noiseY = y + this.noiseMinY;
            
            double density;
            double heightmapDensity;
            
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
            
            // Add island offset
            density += islandOffset;
            
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

    private double generateIslandOffset(int startNoiseX, int startNoiseZ, int curNoiseX, int curNoiseZ) {
        float noiseX = curNoiseX + startNoiseX;
        float noiseZ = curNoiseZ + startNoiseZ;
        
        float oceanDepth = 200.0F;
        
        int centerOceanLerpDistance = this.centerOceanLerpDistance * this.noiseSizeX;
        int centerOceanRadius = this.centerOceanRadius * this.noiseSizeX;
        
        float centerIslandRadius = this.centerIslandRadius * this.noiseSizeX;
        
        float outerIslandNoiseScale = this.outerIslandNoiseScale;
        float outerIslandNoiseOffset = this.outerIslandNoiseOffset;
        
        float dist = noiseX * noiseX + noiseZ * noiseZ;
        float radius = MathHelper.sqrt(dist);
        
        float islandOffset = centerIslandRadius - radius; 
        if (islandOffset < 0.0) 
            islandOffset *= this.centerIslandFalloff; // Increase the rate of falloff past the island radius
        
        islandOffset = MathHelper.clamp(islandOffset, -oceanDepth, 0.0F);
            
        if (this.generateOuterIslands && radius > centerOceanRadius) {
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
    
    private double getOffset(int noiseY, double heightStretch, double depth, double scale) {
        double offset = (((double)noiseY - depth) * heightStretch) / scale;
        
        if (offset < 0D)
            offset *= 4D;
        
        return offset;
    }
}
