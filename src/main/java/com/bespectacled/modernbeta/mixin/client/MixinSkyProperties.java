package com.bespectacled.modernbeta.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.render.SkyProperties;

@Mixin(SkyProperties.class)
public class MixinSkyProperties {
    @Shadow private SkyProperties.SkyType skyType;
    @Shadow private float cloudsHeight;
    
    @Inject(
        method = "getCloudsHeight", at = @At("HEAD"), cancellable = true
    )
    public void injectCloudsHeight(CallbackInfoReturnable<Float> info) {
        if (this.skyType == SkyProperties.SkyType.NORMAL) {
            info.setReturnValue(108F);
        }
    }
}
