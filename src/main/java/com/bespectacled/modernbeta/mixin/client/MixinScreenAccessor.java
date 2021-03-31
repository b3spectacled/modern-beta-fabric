package com.bespectacled.modernbeta.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

@Mixin(Screen.class)
public interface MixinScreenAccessor {
    @Accessor("client")
    public MinecraftClient getClient();
}
