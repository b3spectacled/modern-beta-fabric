package com.bespectacled.modernbeta.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.config.ModernBetaConfig;
import com.mojang.blaze3d.systems.RenderSystem;

import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.world.ClientWorld.Properties;
import net.minecraft.util.math.Vec3d;


@Mixin(value = WorldRenderer.class, priority = 1)
public class MixinWorldRenderer {
    
    @Unique
    private ModernBetaConfig BETA_CONFIG = ModernBeta.BETA_CONFIG;
	
	@ModifyVariable(
        method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;F)V",
        at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/render/SkyProperties;getSkyColor(FF)[F")
    )
    private float[] modifySkySunsetCols(float[] skyCols) {
	    return BETA_CONFIG.renderAlphaSunset ? null : skyCols;
    }
	
}
