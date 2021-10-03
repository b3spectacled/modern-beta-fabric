package com.bespectacled.modernbeta.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.AquiferSampler.FluidLevelSampler;

@Mixin(AquiferSampler.Impl.class)
public class MixinAquiferSampler {
    @Shadow
    private FluidLevelSampler field_34580;
    
    @Inject(method = "apply", at = @At("HEAD"), cancellable = true)
    private void injectApply(int x, int y, int z, double density, double clampedDensity, CallbackInfoReturnable<BlockState> info) {
        //if (density <= -200.0)
        //    info.setReturnValue(this.field_34580.computeFluid(x, y, z).getBlockState(y));
    }
}
