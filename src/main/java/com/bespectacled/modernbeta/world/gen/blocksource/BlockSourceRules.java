package com.bespectacled.modernbeta.world.gen.blocksource;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockState;

public class BlockSourceRules implements BlockSource {
    private final List<BlockSource> rules;
    private final BlockState defaultBlock;
    
    private BlockSourceRules(List<BlockSource> blockSources, BlockState defaultBlock) {
        this.rules = blockSources;
        this.defaultBlock = defaultBlock;
    }

    @Override
    public BlockState sample(int x, int y, int z) {
        for (BlockSource blockSource : this.rules) {
            BlockState blockState = blockSource.sample(x, y, z);
            
            if (blockState == null) continue;
            
            return blockState;
        }
        
        return this.defaultBlock;
    }
    
    public static class Builder {
        private final List<BlockSource> rules;
        
        public Builder() {
            this.rules = new ArrayList<>();
        }
        
        public Builder add(BlockSource blockSource) {
            this.rules.add(blockSource);
            
            return this;
        }
        
        public BlockSourceRules build(BlockState defaultBlock) {
            return new BlockSourceRules(this.rules, defaultBlock);
        }
    }
}
