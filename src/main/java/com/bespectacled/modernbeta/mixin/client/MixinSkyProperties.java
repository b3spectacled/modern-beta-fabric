package com.bespectacled.modernbeta.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.bespectacled.modernbeta.ModernBeta;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.SkyProperties;

@Environment(EnvType.CLIENT)
@Mixin(SkyProperties.class)
public class MixinSkyProperties {
    @Inject(method = "getCloudsHeight", at = @At("HEAD"), cancellable = true)
    public void injectCloudsHeight(CallbackInfoReturnable<Float> info) {
        SkyProperties skyProperties = (SkyProperties)(Object)this;
        
        if (skyProperties instanceof SkyProperties.Overworld && ModernBeta.RENDER_CONFIG.otherConfig.renderLowClouds) {
            info.setReturnValue(108F);
        }
    }
}
