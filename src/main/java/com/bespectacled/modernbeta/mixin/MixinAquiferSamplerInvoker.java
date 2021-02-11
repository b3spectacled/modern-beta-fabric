package com.bespectacled.modernbeta.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.gen.AquiferSampler;

@Mixin(AquiferSampler.class)
public interface MixinAquiferSamplerInvoker {
    @Invoker("apply")
    public void invokeApply(int x, int y, int z);
}
