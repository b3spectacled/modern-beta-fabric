package com.bespectacled.modernbeta.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.gui.screen.world.MoreOptionsDialog;
import net.minecraft.world.gen.GeneratorOptions;

@Mixin(MoreOptionsDialog.class)
public interface MixinMoreOptionsDialogInvoker {
    @Invoker("setGeneratorOptions")
    public void invokeSetGeneratorOptions(GeneratorOptions generatorOptions);
}