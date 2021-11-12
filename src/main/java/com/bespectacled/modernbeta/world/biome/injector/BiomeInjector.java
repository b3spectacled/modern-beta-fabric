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
    
    private static final Predicate<BlockState> OCEAN_STATE_PREDICATE;
    private static final Predicate<BlockState> NOOP_STATE_PREDICATE;
    
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
        
        oceanHeightPredicate = (y, height, minHeight) -> this.atOceanDepth(height, OCEAN_MIN_DEPTH);
        deepOceanHeightPredicate = (y, height, minHeight) -> this.atOceanDepth(height, DEEP_OCEAN_MIN_DEPTH);
        caveBiomeHeightPredicate = (y, height, minHeight) -> y + CAVE_START_OFFSET < minHeight;
        
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
        
        // Collect initial heights and block states at heights
        for (int localBiomeX = 0; localBiomeX < 4; ++localBiomeX) {
            for (int localBiomeZ = 0; localBiomeZ < 4; ++localBiomeZ) {
                // Offset by 2 to get center of biome coordinate section,
                // to sample overall ocean depth as accurately as possible.
                int x = ((localBiomeX + startBiomeX) << 2) + 2;
                int z = ((localBiomeZ + startBiomeZ) << 2) + 2;
                
                int height = this.oldChunkGenerator.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG);
                BlockState state = chunk.getBlockState(pos.set(x, height, z));
                
                int ndx = localBiomeX + localBiomeZ * 4;
                heights[ndx] = height;
                states[ndx] = state;
            }
        }
        
        // Collect min heights in an area centered on local biome coordinate
        for (int localBiomeX = 0; localBiomeX < 4; ++localBiomeX) {
            for (int localBiomeZ = 0; localBiomeZ < 4; ++localBiomeZ) {
                int ndx = localBiomeX + localBiomeZ * 4;
                int height = heights[ndx];
                
                for (int areaBiomeX = -1; areaBiomeX <= 1; ++areaBiomeX) {
                    for (int areaBiomeZ = -1; areaBiomeZ <= 1; ++areaBiomeZ) {
                        int biomeX = localBiomeX + areaBiomeX;
                        int biomeZ = localBiomeZ + areaBiomeZ;
                        
                        int curHeight;
                        if (biomeX >= 0 && biomeX < 4 && biomeZ >= 0 && biomeZ < 4) {
                            curHeight = heights[biomeX + biomeZ * 4];
                        } else {
                            curHeight = this.getCenteredHeight(biomeX + startBiomeX, biomeZ + startBiomeZ);
                        }
                        
                        height = Math.min(height, curHeight);
                    }
                }
                
                minHeights[ndx] = height;
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
    
    private int getCenteredHeight(int biomeX, int biomeZ) {
        // Offset by 2 to get center of biome coordinate section in block coordinates
        int x = (biomeX << 2) + 2;
        int z = (biomeZ << 2) + 2;
        
        return this.chunkProvider instanceof NoiseChunkProvider noiseChunkProvider ?
            noiseChunkProvider.getHeight(x, z, HeightmapChunk.Type.SURFACE_FLOOR) :
            this.chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG);
    }
    
    static {
        OCEAN_STATE_PREDICATE = blockState -> blockState.isOf(Blocks.WATER);
        NOOP_STATE_PREDICATE = blockState -> true;
    }
}
