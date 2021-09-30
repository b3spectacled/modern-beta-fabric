package com.bespectacled.modernbeta.api.world.gen.blocksource;

import java.util.ArrayList;
import java.util.List;

import com.bespectacled.modernbeta.util.BlockStates;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.BlockSource;

public class BlockSources implements BlockSource {
    private final List<BlockSource> blockSources;
    
    private BlockSources(List<BlockSource> blockSources) {
        this.blockSources = blockSources;
    }

    @Override
    public BlockState sample(int x, int y, int z) {
        for (BlockSource blockSource : this.blockSources) {
            BlockState blockState = blockSource.sample(x, y, z);
            
            if (blockState == null) continue;
            
            return blockState;
        }
        
        return BlockStates.AIR;
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
        
        public BlockSources build() {
            return new BlockSources(this.blockSources);
        }
    }
}
