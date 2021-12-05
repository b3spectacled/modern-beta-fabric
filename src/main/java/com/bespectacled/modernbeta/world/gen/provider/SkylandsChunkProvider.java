package com.bespectacled.modernbeta.world.gen.provider;

import java.util.Random;

import com.bespectacled.modernbeta.api.world.gen.NoiseChunkProvider;
import com.bespectacled.modernbeta.api.world.gen.SurfaceConfig;
import com.bespectacled.modernbeta.util.BlockColumnHolder;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.HeightContext;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

public class SkylandsChunkProvider extends NoiseChunkProvider {
    private final PerlinOctaveNoise minLimitNoiseOctaves;
    private final PerlinOctaveNoise maxLimitNoiseOctaves;
    private final PerlinOctaveNoise mainNoiseOctaves;
    private final PerlinOctaveNoise surfaceNoiseOctaves;
    private final PerlinOctaveNoise forestNoiseOctaves;
    
    public SkylandsChunkProvider(OldChunkGenerator chunkGenerator) {
        super(chunkGenerator);

        // Noise Generators
        this.minLimitNoiseOctaves = new PerlinOctaveNoise(rand, 16, true);
        this.maxLimitNoiseOctaves = new PerlinOctaveNoise(rand, 16, true);
        this.mainNoiseOctaves = new PerlinOctaveNoise(rand, 8, true);
        new PerlinOctaveNoise(rand, 4, true);
        this.surfaceNoiseOctaves = new PerlinOctaveNoise(rand, 4, true);
        new PerlinOctaveNoise(rand, 10, true);
        new PerlinOctaveNoise(rand, 16, true);
        this.forestNoiseOctaves = new PerlinOctaveNoise(rand, 8, true);

        setForestOctaves(forestNoiseOctaves);
    }

    @Override
    public void provideSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource) {
        double scale = 0.03125D;

        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;

        Random rand = this.createSurfaceRandom(chunkX, chunkZ);
        BlockPos.Mutable pos = new BlockPos.Mutable();

        double[] surfaceNoise = surfaceNoiseOctaves.sampleBeta(
            chunkX * 16, chunkZ * 16, 0.0D, 
            16, 16, 1,
            scale * 2D, scale * 2D, scale * 2D
        );

        // Surface builder stuff
        BlockColumnHolder blockColumn = new BlockColumnHolder(chunk);
        HeightContext context = new HeightContext(this.chunkGenerator, region);
        
        BiomeAccess biomeAccess = region.getBiomeAccess();
        Registry<Biome> biomeRegistry = region.getRegistryManager().get(Registry.BIOME_KEY);
        
        MaterialRules.MaterialRuleContext ruleContext = new MaterialRules.MaterialRuleContext(
            this.surfaceBuilder,
            chunk,
            this.dummyNoiseChunkSampler,
            biomeAccess::getBiome,
            biomeRegistry,
            context
        );
        MaterialRules.BlockStateRule blockStateRule = this.surfaceRule.apply(ruleContext);
        
        for (int localZ = 0; localZ < 16; localZ++) {
            for (int localX = 0; localX < 16; localX++) {
                int x = (chunkX << 4) + localX; 
                int z = (chunkZ << 4) + localZ;
                int surfaceTopY = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG).get(localX, localZ);

                int surfaceDepth = (int) (surfaceNoise[localZ + localX * 16] / 3D + 3D + rand.nextDouble() * 0.25D);
                int runDepth = -1;

                Biome biome = biomeSource.getBiomeForSurfaceGen(region, pos.set(x, surfaceTopY, z));
                
                SurfaceConfig surfaceConfig = SurfaceConfig.getSurfaceConfig(biome);
                BlockState topBlock = surfaceConfig.topBlock();
                BlockState fillerBlock = surfaceConfig.fillerBlock();

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
                    
                    // Skip if used custom surface generation or if below minimum surface level.
                    if (usedCustomSurface) {
                        continue;
                    }
                    
                    BlockState blockState = chunk.getBlockState(pos);
                    
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
                        }

                        runDepth = surfaceDepth;
                        
                        blockState = (y >= 0) ? 
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
                    if (runDepth == 0 && fillerBlock.isOf(BlockStates.SAND.getBlock())) {
                        runDepth = rand.nextInt(4);
                        fillerBlock = BlockStates.SANDSTONE;
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
        double heightScale = 684.412D * this.yScale;
        
        double mainNoiseScaleX = this.xzFactor; // Default: 80
        double mainNoiseScaleY = this.yFactor;  // Default: 160
        double mainNoiseScaleZ = this.xzFactor;

        double lowerLimitScale = 512D;
        double upperLimitScale = 512D;
        
        // Density norm (sum of 16 octaves of noise / limitScale => [-128, 128])
        double densityScale = 128.0;
        double tunnelThreshold = 200.0 / densityScale;
        
        for (int y = 0; y < primaryBuffer.length; ++y) {
            int noiseY = y + this.noiseMinY;

            double density;
            double heightmapDensity;
            
            double densityOffset = this.getOffset();
            
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
            
            // Normalize density
            density /= densityScale;
            
            // Sample without noise caves
            heightmapDensity = density;
            
            // Sample for noise caves
            density = this.sampleNoiseCave(density, tunnelThreshold, noiseX, noiseY, noiseZ);
            
            // Apply slides
            density = this.applySlides(density, y);
            heightmapDensity = this.applySlides(heightmapDensity, y);
            
            primaryBuffer[y] = MathHelper.clamp(density, -64.0, 64.0);
            heightmapBuffer[y] = MathHelper.clamp(heightmapDensity, -64.0, 64.0);
        }
    }
    
    private double getOffset() {
        return 8D;
    }
}
