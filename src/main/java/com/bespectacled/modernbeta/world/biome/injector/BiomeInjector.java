package com.bespectacled.modernbeta.world.biome.injector;

import java.util.function.Predicate;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.gen.ChunkProvider;
import com.bespectacled.modernbeta.api.world.gen.NoiseChunkProvider;
import com.bespectacled.modernbeta.util.chunk.HeightmapChunk;
import com.bespectacled.modernbeta.util.function.TriPredicate;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.PalettedContainer;

public class BiomeInjector {
    public static final int OCEAN_MIN_DEPTH = 4;
    public static final int DEEP_OCEAN_MIN_DEPTH = 16;
    
    public static final int CAVE_START_OFFSET = 8;
    public static final int CAVE_END_OFFSET = 16;
    
    private static final Predicate<BlockState> OCEAN_STATE_PREDICATE = blockState -> blockState.isOf(Blocks.WATER);
    private static final Predicate<BlockState> NOOP_STATE_PREDICATE = blockState -> true;
    
    private final OldChunkGenerator oldChunkGenerator;
    private final OldBiomeSource oldBiomeSource;
    private final ChunkProvider chunkProvider;
    
    private final BiomeInjectionRules rules;
    
    public BiomeInjector(OldChunkGenerator oldChunkGenerator, OldBiomeSource oldBiomeSource) {
        this.oldChunkGenerator = oldChunkGenerator;
        this.oldBiomeSource = oldBiomeSource;
        this.chunkProvider = oldChunkGenerator.getChunkProvider();
        
        TriPredicate<Integer, Integer, Integer> oceanHeightPredicate;
        TriPredicate<Integer, Integer, Integer> deepOceanHeightPredicate;
        TriPredicate<Integer, Integer, Integer> caveBiomeHeightPredicate;
        
        oceanHeightPredicate = (y, topHeight, minHeight) -> this.atOceanDepth(topHeight, OCEAN_MIN_DEPTH);
        deepOceanHeightPredicate = (y, topHeight, minHeight) -> this.atOceanDepth(topHeight, DEEP_OCEAN_MIN_DEPTH);
        caveBiomeHeightPredicate = (y, topHeight, minHeight) -> y + CAVE_START_OFFSET < minHeight;
        
        BiomeInjectionRules.Builder builder = new BiomeInjectionRules.Builder()
            .add(caveBiomeHeightPredicate, NOOP_STATE_PREDICATE, this.oldBiomeSource::getCaveBiome);
        
        if (oldChunkGenerator.generatesOceans()) {
            builder.add(deepOceanHeightPredicate, OCEAN_STATE_PREDICATE, this.oldBiomeSource::getDeepOceanBiome);
            builder.add(oceanHeightPredicate, OCEAN_STATE_PREDICATE, this.oldBiomeSource::getOceanBiome);
        }
        
        this.rules = builder.build();
    }
    
    public void injectBiomes(Chunk chunk) {
        ChunkPos chunkPos = chunk.getPos();
        BlockPos.Mutable pos = new BlockPos.Mutable();
        
        int startBiomeX = chunkPos.getStartX() >> 2;
        int startBiomeZ = chunkPos.getStartZ() >> 2;
        
        int[] heights = new int[16];
        int[] minHeights = new int[16];
        BlockState[] states = new BlockState[16];
        
        /*
         * Collect the following:
         * -> Height at local biome coordinate.
         * -> Minimum height of area around local biome coordinate.
         * -> Blockstate at height of local biome coordinate.
         */
        for (int localBiomeX = 0; localBiomeX < 4; ++localBiomeX) {
            for (int localBiomeZ = 0; localBiomeZ < 4; ++localBiomeZ) {
                int ndx = localBiomeX + localBiomeZ * 4;
                
                int biomeX = localBiomeX + startBiomeX;
                int biomeZ = localBiomeZ + startBiomeZ;
                
                int x = (biomeX << 2) + 2;
                int z = (biomeZ << 2) + 2;
                
                int height = this.chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG);
                int minHeight = this.sampleMinHeightAround(biomeX, biomeZ, height);
                BlockState state = chunk.getBlockState(pos.set(x, height, z));
                
                heights[ndx] = height;
                minHeights[ndx] = minHeight;
                states[ndx] = state;
            }
        }
        
        // Replace biomes from biome container
        for (int sectionY = 0; sectionY < chunk.countVerticalSections(); ++sectionY) {
            ChunkSection section = chunk.getSection(sectionY);
            PalettedContainer<Biome> container = section.getBiomeContainer();
            
            container.lock();
            try {
                int yOffset = section.getYOffset() >> 2;
                
                for (int localBiomeX = 0; localBiomeX < 4; ++localBiomeX) {
                    for (int localBiomeZ = 0; localBiomeZ < 4; ++localBiomeZ) {
                        int ndx = localBiomeX + localBiomeZ * 4;
                        
                        int height = heights[ndx];
                        int minHeight = minHeights[ndx];
                        BlockState state = states[ndx];
                        
                        for (int localBiomeY = 0; localBiomeY < 4; ++localBiomeY) {
                            int biomeX = localBiomeX + startBiomeX;
                            int biomeY = localBiomeY + yOffset;
                            int biomeZ = localBiomeZ + startBiomeZ;

                            int y = (localBiomeY + yOffset) << 2;
                            
                            Biome biome = this.test(y, height, minHeight, state).apply(biomeX, biomeY, biomeZ);
                            
                            if (biome != null) {
                                container.swapUnsafe(localBiomeX, localBiomeY, localBiomeZ, biome);
                            }
                        }   
                    }
                }
                
            } catch (Exception e) {
                ModernBeta.log(Level.ERROR, "Unable to replace biomes!");
                e.printStackTrace();
                
            } finally {
                container.unlock();
            }
        }
    }
    
    public BiomeInjectionResolver test(int y, int height, int minHeight, BlockState blockState) {
        return this.rules.test(y, height, minHeight, blockState);
    }
    
    private boolean atOceanDepth(int height, int oceanDepth) {
        return height < this.oldChunkGenerator.getSeaLevel() - oceanDepth;
    }
    
    public int getCenteredHeight(int biomeX, int biomeZ) {
        // Offset by 2 to get center of biome coordinate section in block coordinates
        int x = (biomeX << 2) + 2;
        int z = (biomeZ << 2) + 2;
        
        return this.chunkProvider instanceof NoiseChunkProvider noiseChunkProvider ?
            noiseChunkProvider.getHeight(x, z, HeightmapChunk.Type.SURFACE_FLOOR) :
            this.chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG);
    }
    
    public int sampleMinHeightAround(int centerBiomeX, int centerBiomeZ, int initialHeight) {
        int minHeight = initialHeight;
        
        for (int areaBiomeX = -1; areaBiomeX <= 1; ++areaBiomeX) {
            for (int areaBiomeZ = -1; areaBiomeZ <= 1; ++areaBiomeZ) {
                int biomeX = centerBiomeX + areaBiomeX;
                int biomeZ = centerBiomeZ + areaBiomeZ;
                
                minHeight = Math.min(minHeight, this.getCenteredHeight(biomeX, biomeZ));
            }
        }
        
        return minHeight;
    }
}
