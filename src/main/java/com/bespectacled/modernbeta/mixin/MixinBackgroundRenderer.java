package com.bespectacled.modernbeta.mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.config.ModernBetaConfig;
import net.minecraft.client.render.BackgroundRenderer;

@Mixin(BackgroundRenderer.class)
public class MixinBackgroundRenderer {
    
    @Unique
    private static ModernBetaConfig BETA_CONFIG = ModernBeta.BETA_CONFIG;
    
    @ModifyVariable(
        method = "render",
        at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/render/SkyProperties;getFogColorOverride(FF)[F")
    )
    private static float[] modifyFogSunsetCols(float[] skyCols) {
        return BETA_CONFIG.renderAlphaSunset ? null : skyCols;
    }
    
}
