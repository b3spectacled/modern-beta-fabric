package com.bespectacled.modernbeta.mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.config.ModernBetaConfig;
import com.bespectacled.modernbeta.util.MutableClientWorld;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Mixin(BackgroundRenderer.class)
public class MixinBackgroundRenderer {
    
    @Unique private static ModernBetaConfig BETA_CONFIG = ModernBeta.BETA_CONFIG;
    @Unique private static int capturedRenderDistance = 16;
    @Unique private static ClientWorld clientWorld = null;
    
    @ModifyVariable(
        method = "render",
        at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/render/SkyProperties;getFogColorOverride(FF)[F")
    )
    private static float[] modifyFogSunsetCols(float[] skyCols) {
        return BETA_CONFIG.renderAlphaSunset ? null : skyCols;
    }
    
    @Inject(method = "render", at = @At("HEAD"))
    private static void captureVars(Camera camera, float tickDelta, ClientWorld world, int renderDistance, float skyDarkness, CallbackInfo info) {
        if (capturedRenderDistance != renderDistance)
            capturedRenderDistance = renderDistance;
        
        if (!world.equals(clientWorld)) {
            clientWorld = world;
        }
    }
    
    @ModifyVariable(
        method = "render",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;method_23777(Lnet/minecraft/util/math/BlockPos;F)Lnet/minecraft/util/math/Vec3d;"),
        index = 6
    )
    private static float modifyFogModifier(float weighting) {
        // Old fog formula with old render distance: weighting = 1.0F / (float)(4 - renderDistance) 
        // where renderDistance is 0-3, 0 being 'Far' and 3 being 'Short'
        if (BETA_CONFIG.renderBetaSkyColor && MutableClientWorld.inject(clientWorld).usesBetaColors()) {
            int clampedDistance = MathHelper.clamp(capturedRenderDistance, 0, 16);
            clampedDistance = (int)((16 - clampedDistance) / (float)16 * 3); 
            
            weighting = 1.0F / (float)(4 - clampedDistance);
            weighting = 1.0F - (float)Math.pow(weighting, 0.25); 
        }
        
        return weighting;
    }
        
    
}
