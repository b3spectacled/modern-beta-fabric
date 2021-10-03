package com.bespectacled.modernbeta.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.gen.chunk.SlideConfig;

@Mixin(SlideConfig.class)
public interface MixinSlideConfig {
    @Accessor
    public double getTarget();
    
    @Accessor
    public int getSize();
    
    @Accessor
    public int getOffset();
}
