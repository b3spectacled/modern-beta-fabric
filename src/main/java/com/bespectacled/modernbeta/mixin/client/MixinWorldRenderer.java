package com.bespectacled.modernbeta.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.bespectacled.modernbeta.ModernBeta;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.WorldRenderer;

@Environment(EnvType.CLIENT)
@Mixin(value = WorldRenderer.class, priority = 1)
public class MixinWorldRenderer {
    @ModifyVariable(
            method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;F)V",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/render/SkyProperties;getFogColorOverride(FF)[F")
        )
    private float[] modifySkySunsetCols(float[] skyCols) {
	    return ModernBeta.RENDER_CONFIG.otherConfig.renderAlphaSunset ? null : skyCols;
    }
}
