package com.bespectacled.modernbeta.api.world.gen.blockstate;

import net.minecraft.block.BlockState;

@FunctionalInterface
public interface BlockStateProvider {
    public BlockState getBlockState(int x, int y, int z);
}
