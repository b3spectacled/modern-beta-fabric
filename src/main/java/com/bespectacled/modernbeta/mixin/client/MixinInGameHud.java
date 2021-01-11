package com.bespectacled.modernbeta.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bespectacled.modernbeta.ModernBeta;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(InGameHud.class)
public class MixinInGameHud {
    @Shadow
    private MinecraftClient client;
    
    @Inject(method = "render", at = @At("TAIL"))
    private void injectDebugVersion(MatrixStack matrices, float tickDelta, CallbackInfo info) {
        if (ModernBeta.BETA_CONFIG.renderGameVersion && !this.client.options.debugEnabled) {
            this.renderDebugVersion(matrices);
        }
    }
    
    private void renderDebugVersion(MatrixStack matrices) {
        TextRenderer fontRenderer = this.client.textRenderer;
        String version = "Minecraft " + SharedConstants.getGameVersion().getName();
        
        fontRenderer.drawWithShadow(matrices, version, 2f, 2f, 0xFFFFFF);
    }
}
