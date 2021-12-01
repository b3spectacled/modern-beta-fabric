package com.bespectacled.modernbeta.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.bespectacled.modernbeta.ModernBeta;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.DimensionEffects;

@Environment(EnvType.CLIENT)
@Mixin(DimensionEffects.class)
public class MixinDimensionEffects {
    @Inject(method = "getCloudsHeight", at = @At("HEAD"), cancellable = true)
    public void injectCloudsHeight(CallbackInfoReturnable<Float> info) {
        DimensionEffects dimensionEffects = (DimensionEffects)(Object)this;
        
        if (dimensionEffects instanceof DimensionEffects.Overworld) {
            info.setReturnValue((float)ModernBeta.RENDER_CONFIG.configOther.cloudHeight);
        }
    }
}
