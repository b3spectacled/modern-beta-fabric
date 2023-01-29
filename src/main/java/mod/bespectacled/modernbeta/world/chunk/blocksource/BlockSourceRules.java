package mod.bespectacled.modernbeta.world.chunk.blocksource;

import java.util.ArrayList;
import java.util.List;

import mod.bespectacled.modernbeta.util.BlockStates;
import net.minecraft.block.BlockState;

public class BlockSourceRules implements SimpleBlockSource {
    private static final boolean DEBUG = false;
    
    private final List<SimpleBlockSource> rules;
    private final BlockState defaultBlock;
    
    private BlockSourceRules(List<SimpleBlockSource> blockSources, BlockState defaultBlock) {
        this.rules = blockSources;
        this.defaultBlock = defaultBlock;
    }

    @Override
    public BlockState apply(int x, int y, int z) {
        for (SimpleBlockSource blockSource : this.rules) {
            BlockState blockState = blockSource.apply(x, y, z);
            
            if (blockState != null)
                return blockState;
        }
        
        return DEBUG ? BlockStates.AIR : this.defaultBlock;
    }
    
    public static class Builder {
        private final List<SimpleBlockSource> rules;
        
        public Builder() {
            this.rules = new ArrayList<>();
        }
        
        public Builder add(SimpleBlockSource blockSource) {
            this.rules.add(blockSource);
            
            return this;
        }
        
        public BlockSourceRules build(BlockState defaultBlock) {
            return new BlockSourceRules(this.rules, defaultBlock);
        }
    }
}
