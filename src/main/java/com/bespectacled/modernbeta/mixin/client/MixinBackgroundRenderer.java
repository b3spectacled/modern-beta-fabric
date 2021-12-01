package com.bespectacled.modernbeta.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.util.ModernBetaClientWorld;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
@Mixin(BackgroundRenderer.class)
public class MixinBackgroundRenderer {
    @Unique private static final Vec3d OLD_FOG_COLOR = new Vec3d(0.753F, 0.847F, 1.0F);
    
    @Unique private static int capturedRenderDistance = 16;
    @Unique private static float oldFogWeight = calculateFogWeight(16);
    @Unique private static boolean isOldWorld = false;
    
    @ModifyVariable(
        method = "render",
        at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/render/SkyProperties;getFogColorOverride(FF)[F")
    )
    private static float[] modifyFogSunsetCols(float[] skyCols) {
        return ModernBeta.RENDER_CONFIG.configOther.renderAlphaSunset ? null : skyCols;
    }
    
    @Inject(method = "render", at = @At("HEAD"))
    private static void captureVars(Camera camera, float tickDelta, ClientWorld world, int renderDistance, float skyDarkness, CallbackInfo info) {
        if (capturedRenderDistance != renderDistance) {
            capturedRenderDistance = renderDistance;
            oldFogWeight = calculateFogWeight(renderDistance);
        }
        
        // Track whether current client world is Modern Beta world,
        // old fog weighting won't be used if not.
        isOldWorld = world instanceof ModernBetaClientWorld clientWorld ? clientWorld.isModernBetaWorld() : false;
    }
    
    @ModifyVariable(
        method = "render",
        at = @At(
            value = "INVOKE", 
            target = "Lnet/minecraft/client/world/ClientWorld;method_23777(Lnet/minecraft/util/math/Vec3d;F)Lnet/minecraft/util/math/Vec3d;"
        ),
        index = 7
    )
    private static float modifyFogWeighting(float weight) {
        return isOldWorld && ModernBeta.RENDER_CONFIG.configBiomeColor.renderOldFogColor ? oldFogWeight : weight;
    }
    
    @Unique
    private static float calculateFogWeight(int renderDistance) {
        // Old fog formula with old render distance: weight = 1.0F / (float)(4 - renderDistance) 
        // where renderDistance is 0-3, 0 being 'Far' and 3 being 'Very Short'
        
        int clampedDistance = MathHelper.clamp(renderDistance, 0, 16);
        clampedDistance = (int)((16 - clampedDistance) / (float)16 * 3);
        
        float weight = 1.0F / (float)(4 - clampedDistance);
        weight = 1.0F - (float)Math.pow(weight, 0.25);
        
        return weight;
    }
}
