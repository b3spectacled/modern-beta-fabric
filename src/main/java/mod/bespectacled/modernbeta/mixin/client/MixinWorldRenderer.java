package mod.bespectacled.modernbeta.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import mod.bespectacled.modernbeta.ModernBeta;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.WorldRenderer;

@Environment(EnvType.CLIENT)
@Mixin(value = WorldRenderer.class, priority = 1)
public class MixinWorldRenderer {
	@ModifyVariable(
        method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V",
        at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/render/DimensionEffects;getFogColorOverride(FF)[F")
    )
    private float[] modifySkySunsetCols(float[] skyCols) {
	    return ModernBeta.RENDER_CONFIG.configOther.renderAlphaSunset ? null : skyCols;
    }
}
