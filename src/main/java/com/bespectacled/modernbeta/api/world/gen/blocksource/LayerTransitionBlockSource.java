package com.bespectacled.modernbeta.api.world.gen.blocksource;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.BlockSource;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.random.AbstractRandom;
import net.minecraft.world.gen.random.RandomDeriver;

public class LayerTransitionBlockSource implements BlockSource {

    private final RandomDeriver randomDeriver;
    private final BlockState belowState;
    private final BlockState aboveState;
    private final int minY;
    private final int maxY;
    
    public LayerTransitionBlockSource(RandomDeriver randomDeriver, BlockState belowState, BlockState aboveState, int minY, int maxY) {
        this.randomDeriver = randomDeriver;
        this.belowState = belowState;
        this.aboveState = aboveState;
        this.minY = minY;
        this.maxY = maxY;
    }
    
    @Override
    public BlockState apply(ChunkNoiseSampler noiseSampler, int x, int y, int z) {
        if (y <= this.minY) {
            return this.belowState;
        }
        
        if (y >= this.maxY) {
            return this.aboveState;
        }
        
        double yThreshold = MathHelper.lerpFromProgress(y, this.minY, this.maxY, 1.0, 0.0);
        AbstractRandom random = this.randomDeriver.createRandom(x, y, z);
        
        return (double)random.nextFloat() < yThreshold ? this.belowState : this.aboveState;
    }

}
