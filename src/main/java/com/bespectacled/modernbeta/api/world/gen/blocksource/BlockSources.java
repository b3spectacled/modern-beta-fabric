package com.bespectacled.modernbeta.api.world.gen.blocksource;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.BlockSource;

public class BlockSources implements BlockSource {
    private final List<BlockSource> blockSources;
    private final BlockState defaultBlock;
    
    private BlockSources(List<BlockSource> blockSources, BlockState defaultBlock) {
        this.blockSources = blockSources;
        this.defaultBlock = defaultBlock;
    }

    @Override
    public BlockState sample(int x, int y, int z) {
        for (BlockSource blockSource : this.blockSources) {
            BlockState blockState = blockSource.sample(x, y, z);
            
            if (blockState == null) continue;
            
            return blockState;
        }
        
        return this.defaultBlock;
    }
    
    public static class Builder {
        private final List<BlockSource> blockSources;
        
        public Builder() {
            this.blockSources = new ArrayList<>();
        }
        
        public Builder add(BlockSource blockSource) {
            this.blockSources.add(blockSource);
            
            return this;
        }
        
        public BlockSources build(BlockState defaultBlock) {
            return new BlockSources(this.blockSources, defaultBlock);
        }
    }
}
