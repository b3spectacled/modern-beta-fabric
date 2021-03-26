package com.bespectacled.modernbeta.gen;

import java.util.HashMap;
import java.util.Map;
import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.util.BiomeUtil;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.MutableBiomeArray;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeCoords;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class OldGeneratorUtil {
    private static final BlockPos.Mutable POS = new BlockPos.Mutable();
    
    public static int getSolidHeight(Chunk chunk, int worldHeight, int minY, int x, int z) {
        for (int y = worldHeight + minY - 1; y >= minY; y--) {
            BlockState someBlock = chunk.getBlockState(POS.set(x, y, z));
            if (!(someBlock.equals(BlockStates.AIR) || someBlock.equals(BlockStates.WATER) || someBlock.equals(BlockStates.ICE)))
                return y;
        }
        return minY;
    }
    
    public static Biome getOceanBiome(Chunk chunk, ChunkGenerator gen, BiomeSource biomeSource, boolean generateOceans, int seaLevel) {
        ChunkPos chunkPos = chunk.getPos();
        
        int x = chunkPos.getStartX();
        int z = chunkPos.getStartZ();
        
        int biomeX = x >> 2;
        int biomeZ = z >> 2;
        
        Biome biome;

        if (generateOceans && gen.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG, null) < seaLevel - 4) {
            biome = ((OldBiomeSource)biomeSource).getOceanBiomeForNoiseGen(biomeX, 0, biomeZ);
        } else {
            biome = biomeSource.getBiomeForNoiseGen(chunkPos);
        }

        return biome;
    }
    
    public static void replaceOceansInChunk(Chunk chunk, BiomeSource biomeSource, int worldHeight, int minY, int seaLevel, int cutoff) {
        MutableBiomeArray mutableBiomes = MutableBiomeArray.inject(chunk.getBiomeArray());
        Map<BlockPos, Biome> oceanPositions = new HashMap<BlockPos, Biome>();
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();
        ChunkPos chunkPos = chunk.getPos();
        
        int biomeCutoff = BiomeCoords.fromBlock(cutoff);
        int biomeMinY = BiomeCoords.fromBlock(minY);
        
        // Collect potential ocean positions in a chunk.
        for (int x = 0; x < 4; ++x) {
            for (int z = 0; z < 4; ++z) {
                int absX = chunkPos.getStartX() + (x << 2);
                int absZ = chunkPos.getStartZ() + (z << 2);
                
                if (OldGeneratorUtil.getSolidHeight(chunk, worldHeight, minY, absX, absZ) < seaLevel - 4) {
                    BlockPos blockPos = new BlockPos(x, 0, z);
                    oceanPositions.put(blockPos, ((OldBiomeSource)biomeSource).getOceanBiomeForNoiseGen(absX >> 2, 0, absZ >> 2));
                }
            }
        }
        
        // See BiomeArray for reference.
        for (int i = 0; i < mutableBiomes.getBiomeArrLen(); ++i) {
            int offsetBiomeX = i & BiomeUtil.HORIZONTAL_BIT_MASK;
            int offsetBiomeY = i >> BiomeUtil.HORIZONTAL_SECTION_COUNT + BiomeUtil.HORIZONTAL_SECTION_COUNT;
            int offsetBiomeZ = i >> BiomeUtil.HORIZONTAL_SECTION_COUNT & BiomeUtil.HORIZONTAL_BIT_MASK;
            
            // Skip if below y-cutoff.
            int biomeY = biomeMinY + offsetBiomeY;
            boolean isAboveCutoff = biomeY >= biomeCutoff;
            
            isAboveCutoff = true; // TODO: Remove when cave biomes are in.
            
            mutablePos.set(offsetBiomeX, 0, offsetBiomeZ);
            if (isAboveCutoff && oceanPositions.get(mutablePos) != null) {
                mutableBiomes.setBiome(i, oceanPositions.get(mutablePos));
            }
        }
    }
}
