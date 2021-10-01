package com.bespectacled.modernbeta.api.world.gen.blocksource;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.BlockSource;
import net.minecraft.world.gen.ChunkRandom;

public class DeepslateSourceNullable implements BlockSource {
    private final ChunkRandom random;
    private final long seed;
    private final BlockState deepslateState;
    
    public DeepslateSourceNullable(long seed, BlockState deepslateState) {
        this.random = new ChunkRandom(seed);
        this.seed = seed;
        this.deepslateState = deepslateState;
    }

    @Override
    public BlockState sample(int x, int y, int z) {
        if (y < -8) {
            return this.deepslateState;
        }
        
        if (y > 0) {
            return null;
        }
        
        double threshold = MathHelper.lerpFromProgress(y, -8.0, 0.0, 1.0, 0.0);
        this.random.setDeepslateSeed(this.seed, x, y, z);
        
        return (double)this.random.nextFloat() < threshold ? this.deepslateState : null;
    }

}
