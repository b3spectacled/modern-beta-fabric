package com.bespectacled.modernbeta.world.gen.blocksource;

import net.minecraft.block.BlockState;

@FunctionalInterface
public interface BlockSource {
    public BlockState sample(int x, int y, int z);
}