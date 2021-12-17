package com.bespectacled.modernbeta.world.biome.injector;

import java.util.function.Predicate;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.gen.ChunkProvider;
import com.bespectacled.modernbeta.api.world.gen.NoiseChunkProvider;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.util.chunk.HeightmapChunk;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.biome.injector.BiomeInjectionRules.BiomeInjectionContext;
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
    
    public static final Predicate<BiomeInjectionContext> CAVE_PREDICATE = context ->
        context.getY() >= context.worldMinY && context.getY() + CAVE_START_OFFSET < context.minHeight;
    
    private final OldChunkGenerator oldChunkGenerator;
    private final OldBiomeSource oldBiomeSource;
    private final ChunkProvider chunkProvider;
    
    private final BiomeInjectionRules rules;
    
    public BiomeInjector(OldChunkGenerator oldChunkGenerator, OldBiomeSource oldBiomeSource) {
        this.oldChunkGenerator = oldChunkGenerator;
        this.oldBiomeSource = oldBiomeSource;
        this.chunkProvider = oldChunkGenerator.getChunkProvider();
        
        boolean generatesOceans = NbtUtil.readBoolean(NbtTags.GEN_OCEANS, this.oldBiomeSource.getBiomeSettings(), false);
        
        Predicate<BiomeInjectionContext> oceanPredicate = context -> 
            this.atOceanDepth(context.topHeight, OCEAN_MIN_DEPTH) && 
            context.topState.isOf(Blocks.WATER);
        
        Predicate<BiomeInjectionContext> deepOceanPredicate = context ->
            this.atOceanDepth(context.topHeight, DEEP_OCEAN_MIN_DEPTH) && 
            context.topState.isOf(Blocks.WATER);
            
        BiomeInjectionRules.Builder builder = new BiomeInjectionRules.Builder()
            .add(CAVE_PREDICATE, this.oldBiomeSource::getCaveBiome);
        
        if (generatesOceans) {
            builder.add(deepOceanPredicate, this.oldBiomeSource::getDeepOceanBiome);
            builder.add(oceanPredicate, this.oldBiomeSource::getOceanBiome);
        }
        
        this.rules = builder.build();
    }
    
    public void injectBiomes(Chunk chunk) {
        ChunkPos chunkPos = chunk.getPos();
        BlockPos.Mutable pos = new BlockPos.Mutable();
        
        int startBiomeX = chunkPos.getStartX() >> 2;
        int startBiomeZ = chunkPos.getStartZ() >> 2;
        
        /*
         * Collect the following for an x/z coordinate:
         * -> Height at local biome coordinate.
         * -> Minimum height of area around local biome coordinate.
         * -> Blockstate at height of local biome coordinate.
         */
        
        // Replace biomes from biome container
        for (int sectionY = 0; sectionY < chunk.countVerticalSections(); ++sectionY) {
            ChunkSection section = chunk.getSection(sectionY);
            PalettedContainer<Biome> container = section.getBiomeContainer();
            
            container.lock();
            try {
                int yOffset = section.getYOffset() >> 2;
                
                for (int localBiomeX = 0; localBiomeX < 4; ++localBiomeX) {
                    for (int localBiomeZ = 0; localBiomeZ < 4; ++localBiomeZ) {
                        int biomeX = localBiomeX + startBiomeX;
                        int biomeZ = localBiomeZ + startBiomeZ;
                        
                        int x = (biomeX << 2) + 2;
                        int z = (biomeZ << 2) + 2;
                        
                        int worldMinY = this.chunkProvider.getWorldMinY();
                        int topHeight = this.chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG);
                        int minHeight = this.sampleMinHeightAround(biomeX, biomeZ);
                        
                        BlockState topState = chunk.getBlockState(pos.set(x, topHeight, z));
                        BlockState minState = chunk.getBlockState(pos.set(x, minHeight, z));
                        
                        BiomeInjectionContext context = new BiomeInjectionContext(
                            worldMinY,
                            topHeight,
                            minHeight,
                            topState,
                            minState
                        );
                        
                        for (int localBiomeY = 0; localBiomeY < 4; ++localBiomeY) {
                            int biomeY = localBiomeY + yOffset;
                            int y = (localBiomeY + yOffset) << 2;
                            
                            context.setY(y);
                            Biome biome = this.sample(context, biomeX, biomeY, biomeZ);
                            
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
    
    public Biome sample(BiomeInjectionContext context, int biomeX, int biomeY, int biomeZ) {
        return this.rules.test(context, biomeX, biomeY, biomeZ);
    }
    
    public int getCenteredHeight(int biomeX, int biomeZ) {
        // Offset by 2 to get center of biome coordinate section in block coordinates
        int x = (biomeX << 2) + 2;
        int z = (biomeZ << 2) + 2;
        
        return this.chunkProvider instanceof NoiseChunkProvider noiseChunkProvider ?
            noiseChunkProvider.getHeight(x, z, HeightmapChunk.Type.SURFACE_FLOOR) :
            this.chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG);
    }
    
    public int sampleMinHeightAround(int centerBiomeX, int centerBiomeZ) {
        int minHeight = Integer.MAX_VALUE;
        
        for (int areaBiomeX = -1; areaBiomeX <= 1; ++areaBiomeX) {
            for (int areaBiomeZ = -1; areaBiomeZ <= 1; ++areaBiomeZ) {
                int biomeX = centerBiomeX + areaBiomeX;
                int biomeZ = centerBiomeZ + areaBiomeZ;
                
                minHeight = Math.min(minHeight, this.getCenteredHeight(biomeX, biomeZ));
            }
        }
        
        return minHeight;
    }

    private boolean atOceanDepth(int topHeight, int oceanDepth) {
        return topHeight < this.oldChunkGenerator.getSeaLevel() - oceanDepth;
    }
}
