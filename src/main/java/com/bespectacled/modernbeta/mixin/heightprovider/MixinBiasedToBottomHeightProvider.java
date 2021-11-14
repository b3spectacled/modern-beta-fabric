package com.bespectacled.modernbeta.mixin.heightprovider;

import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.world.gen.heightprovider.BiasedToBottomHeightProvider;

@Mixin(BiasedToBottomHeightProvider.class)
public class MixinBiasedToBottomHeightProvider {
    @Redirect(method = "get", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V"))
    private void redirectWarn(Logger self, String string, Object object) {}
}
