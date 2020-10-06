package com.bespectacled.modernbeta.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.render.WorldRenderer;


@Mixin(value = WorldRenderer.class, priority = 1)
public class MixinWorldRenderer {
	
	/*
	@ModifyVariable(
		method = "renderStars",
		at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/Random;nextFloat()F"),
		index = 10
	)
	private double modifyStars(double double11) {
	    
	    return 0.25;
	}
	*/
	
    
    /*
	@ModifyVariable(
        method = "renderStars(LBufferBuilder;)V",
        at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/Random;nextFloat()F"),
        ordinal = 1
    )
    private double modifyStars(double double11) {
        // Do stuff here
    }
	*/

}
