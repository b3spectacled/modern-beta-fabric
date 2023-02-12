package mod.bespectacled.modernbeta.world.blocksource;

import java.util.ArrayList;
import java.util.List;

import mod.bespectacled.modernbeta.api.world.blocksource.BlockSource;
import mod.bespectacled.modernbeta.util.BlockStates;
import net.minecraft.block.BlockState;

public class BlockSourceRules implements BlockSource {
    private static final boolean DEBUG = false;
    
    private final List<BlockSource> rules;
    private final BlockState defaultBlock;
    
    private BlockSourceRules(List<BlockSource> blockSources, BlockState defaultBlock) {
        this.rules = blockSources;
        this.defaultBlock = defaultBlock;
    }

    @Override
    public BlockState apply(int x, int y, int z) {
        for (BlockSource blockSource : this.rules) {
            BlockState blockState = blockSource.apply(x, y, z);
            
            if (blockState != null)
                return blockState;
        }
        
        return DEBUG ? BlockStates.AIR : this.defaultBlock;
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
