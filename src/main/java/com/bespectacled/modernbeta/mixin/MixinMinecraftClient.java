package com.bespectacled.modernbeta.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.bespectacled.modernbeta.client.GoVote;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

    @Inject(method = "openScreen", at = @At("HEAD"), cancellable = true)
    private void handleVoteScreen(Screen screen, CallbackInfo ci) {

        // Handle the vote screen, go vote!
        if (GoVote.show((MinecraftClient) (Object) this, screen)) {
            ci.cancel();
        }
    }
}