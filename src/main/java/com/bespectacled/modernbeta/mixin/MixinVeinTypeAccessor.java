package com.bespectacled.modernbeta.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.NoiseColumnSampler;

@Mixin(NoiseColumnSampler.VeinType.class)
public interface MixinVeinTypeAccessor {
    @Accessor
    public BlockState getOre();
    
    @Accessor
    public BlockState getRawBlock();
    
    @Accessor
    public BlockState getStone();
    
    @Accessor
    public int getMinY();
    
    @Accessor
    public int getMaxY();
}
