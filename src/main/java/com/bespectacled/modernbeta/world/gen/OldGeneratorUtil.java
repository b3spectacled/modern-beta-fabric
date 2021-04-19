package com.bespectacled.modernbeta.world.gen;

import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class OldGeneratorUtil {
    private static final BlockPos.Mutable POS = new BlockPos.Mutable();
    
    public static int getSolidHeight(Chunk chunk, int x, int z, int worldHeight, BlockState defaultFluid) {
        for (int y = worldHeight - 1; y >= 0; y--) {
            BlockState someBlock = chunk.getBlockState(POS.set(x, y, z));
            if (!(someBlock.equals(BlockStates.AIR) || someBlock.equals(defaultFluid) || someBlock.equals(BlockStates.ICE)))
                return y;
        }
        
        return 0;
    }
    
    public static Biome getOceanBiome(Chunk chunk, ChunkGenerator gen, BiomeSource biomeSource, boolean generateOceans, int seaLevel) {
        ChunkPos chunkPos = chunk.getPos();
        
        int x = chunkPos.getStartX();
        int z = chunkPos.getStartZ();
        
        int biomeX = x >> 2;
        int biomeZ = z >> 2;
        
        Biome biome;

        if (generateOceans && gen.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG) < seaLevel - 4) {
            biome = ((OldBiomeSource)biomeSource).getOceanBiomeForNoiseGen(biomeX, 0, biomeZ);
        } else {
            biome = biomeSource.getBiomeForNoiseGen(biomeX, 0, biomeZ);
        }

        return biome;
    }
    
}
