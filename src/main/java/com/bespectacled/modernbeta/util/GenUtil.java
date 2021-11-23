package com.bespectacled.modernbeta.util;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class GenUtil {
    public static int getSolidHeight(Chunk chunk, int worldHeight, int minY, int x, int z, BlockState defaultFluid) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        for (int y = worldHeight + minY - 1; y >= minY; y--) {
            BlockState blockState = chunk.getBlockState(mutable.set(x, y, z));
            if (!(blockState.equals(BlockStates.AIR) || blockState.equals(defaultFluid) || blockState.equals(BlockStates.ICE)))
                return y;
        }
        
        return minY;
    }
}
