package com.bespectacled.modernbeta.util;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class GenUtil {
    public static int getSolidHeight(Chunk chunk, int x, int z, int worldHeight, BlockState defaultFluid) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        for (int y = worldHeight - 1; y >= 0; y--) {
            BlockState someBlock = chunk.getBlockState(mutable.set(x, y, z));
            if (!(someBlock.equals(BlockStates.AIR) || someBlock.equals(defaultFluid) || someBlock.equals(BlockStates.ICE)))
                return y;
        }
        
        return 0;
    }
}
