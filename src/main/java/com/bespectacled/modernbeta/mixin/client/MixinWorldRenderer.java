package com.bespectacled.modernbeta.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import com.bespectacled.modernbeta.ModernBeta;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.At;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.WorldRenderer;

@Environment(EnvType.CLIENT)
@Mixin(value = WorldRenderer.class, priority = 1)
public class MixinWorldRenderer {
	@ModifyVariable(
        method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLjava/lang/Runnable;)V",
        at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/render/SkyProperties;getFogColorOverride(FF)[F")
    )
    private float[] modifySkySunsetCols(float[] skyCols) {
	    return ModernBeta.RENDER_CONFIG.renderAlphaSunset ? null : skyCols;
    }
}
