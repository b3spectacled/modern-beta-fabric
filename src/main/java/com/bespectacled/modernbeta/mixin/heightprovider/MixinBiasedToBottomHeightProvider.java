package com.bespectacled.modernbeta.mixin.heightprovider;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.gen.heightprovider.BiasedToBottomHeightProvider;

@Mixin(BiasedToBottomHeightProvider.class)
public class MixinBiasedToBottomHeightProvider {
    /*
    @Redirect(method = "get", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V"))
    private void redirectWarn(Logger self, String string, Object object) {
        // Suppress console spam, see: See: MC-236933 and MC-236723
    }
    */
}
