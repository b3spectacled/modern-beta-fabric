package mod.bespectacled.modernbeta.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import mod.bespectacled.modernbeta.ModernBeta;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class MixinInGameHud {
    @Shadow private MinecraftClient client;
    
    @Unique private static final String MODERN_BETA_VERSION;
    
    @Inject(method = "render", at = @At("TAIL"))
    private void injectDebugVersion(MatrixStack matrices, float tickDelta, CallbackInfo info) {
        if (ModernBeta.CONFIG.useGameVersion && !this.client.options.debugEnabled) {
            this.renderDebugVersion(matrices);
        }
    }
    
    @Unique
    private void renderDebugVersion(MatrixStack matrices) {
        this.client.textRenderer.drawWithShadow(matrices, MODERN_BETA_VERSION, 2f, 2f, 0xFFFFFF);
    }
    
    static {
        MODERN_BETA_VERSION = (Math.random() > 0.0001 ? "Minecraft " : "Minceraft ") + SharedConstants.getGameVersion().getName();
    }
}
