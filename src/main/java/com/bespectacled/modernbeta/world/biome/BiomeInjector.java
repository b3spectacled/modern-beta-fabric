package com.bespectacled.modernbeta.world.biome;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;
import com.google.common.base.Supplier;

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
    
    private final OldChunkGenerator chunkGenerator;
    private final OldBiomeSource biomeSource;
    
    private final BiomeInjectionRules rules;
    
    public BiomeInjector(OldChunkGenerator chunkGenerator, OldBiomeSource biomeSource) {
        this.chunkGenerator = chunkGenerator;
        this.biomeSource = biomeSource;
        
        // Use supplier since Indev generator may modify sea level after initialization.
        Supplier<Integer> seaLevel = () -> chunkGenerator.getSeaLevel();
        
        BiPredicate<Integer, Integer> oceanHeightPredicate = (y, height) -> this.atOceanDepth(height, OCEAN_MIN_DEPTH);
        BiPredicate<Integer, Integer> deepOceanHeightPredicate = (y, height) -> this.atOceanDepth(height, DEEP_OCEAN_MIN_DEPTH);
        BiPredicate<Integer, Integer> caveBiomeHeightPredicate = (y, height) -> y + CAVE_START_OFFSET < Math.min(height, seaLevel.get());
        
        BiomeInjectionRules.Builder builder = new BiomeInjectionRules.Builder()
            .add(caveBiomeHeightPredicate, NOOP_STATE_PREDICATE, this.biomeSource::getCaveBiome);
        
        if (chunkGenerator.generatesOceans()) {
            builder.add(deepOceanHeightPredicate, OCEAN_STATE_PREDICATE, this.biomeSource::getDeepOceanBiome);
            builder.add(oceanHeightPredicate, OCEAN_STATE_PREDICATE, this.biomeSource::getOceanBiome);
        }
        
        this.rules = builder.build();
    }
    
    public void injectBiomes(Chunk chunk) {
        ChunkPos chunkPos = chunk.getPos();
        BlockPos.Mutable pos = new BlockPos.Mutable();
        
        int startX = chunkPos.getStartX();
        int startZ = chunkPos.getStartZ();
        
        int startBiomeX = startX >> 2;
        int startBiomeZ = startZ >> 2;
        
        int[] heights = new int[16];
        BlockState[] states = new BlockState[16];
        
        // Collect heights and block states at heights
        for (int localBiomeX = 0; localBiomeX < 4; ++localBiomeX) {
            for (int localBiomeZ = 0; localBiomeZ < 4; ++localBiomeZ) {
                int x = startX + (localBiomeX << 2);
                int z = startZ + (localBiomeZ << 2);
                
                // Offset by 2 to get center of biome coordinate section,
                // to sample overall ocean depth as accurately as possible.
                int offsetX = x + 2;
                int offsetZ = z + 2;
                int height = this.chunkGenerator.getHeight(offsetX, offsetZ, Heightmap.Type.OCEAN_FLOOR_WG);
                BlockState state = chunk.getBlockState(pos.set(offsetX, height, offsetZ));
                
                int ndx = localBiomeX + localBiomeZ * 4;
                heights[ndx] = height;
                states[ndx] = state;
            }
        }
        
        // Replace biomes from biome array
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
                        BlockState state = states[ndx];
                        
                        for (int localBiomeY = 0; localBiomeY < 4; ++localBiomeY) {
                            int biomeX = localBiomeX + startBiomeX;
                            int biomeY = localBiomeY + yOffset;
                            int biomeZ = localBiomeZ + startBiomeZ;

                            int y = (localBiomeY + yOffset) << 2;
                            
                            Biome biome = this.sample(y, height, state, biomeX, biomeY, biomeZ);
                            
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
    
    public Biome sample(int y, int height, BlockState blockState, int biomeX, int biomeY, int biomeZ) {
        return this.rules.apply(y, height, blockState, biomeX, biomeY, biomeZ);
    }
    
    private boolean atOceanDepth(int height, int oceanDepth) {
        return height < this.chunkGenerator.getSeaLevel() - oceanDepth;
    }
    
    static {
        OCEAN_STATE_PREDICATE = blockState -> blockState.isOf(Blocks.WATER);
        NOOP_STATE_PREDICATE = blockState -> true;
    }
}
