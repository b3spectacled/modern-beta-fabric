package com.bespectacled.modernbeta.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Constant;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;


@Mixin(value = WorldRenderer.class, priority = 1)
public class MixinWorldRenderer {
	private static Random rand = new Random(10842L);
	
	@ModifyVariable(
		method = "renderStars(Lnet/minecraft/client/render/BufferBuilder;)V",
		at = @At(value = "INVOKE_ASSIGN"),
		index = 10
	)
	private double modifyStarSize(double starSize) {
	    rand.nextFloat();
	    rand.nextFloat();
	    rand.nextFloat();
	    
	    double newSize = 0.25 + rand.nextDouble() * 0.25;
	    
	    rand.nextDouble();
	    
	    return newSize;
	}
	
	@ModifyConstant(
        method = "renderStars(Lnet/minecraft/client/render/BufferBuilder;)V",
        constant = @Constant(intValue = 1500)
    )
	private int modifyStarCount(int starCount) {
	    //System.out.println(origValue);
	    
	    return starCount;
	}
	
	
	/*
	@Inject(
        method = "renderStars(Lnet/minecraft/client/render/BufferBuilder;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilder;vertex(DDD)Lnet/minecraft/client/render/VertexConsumer;")
    )
    private void modifyStarColor(BufferBuilder bufferBuilder, CallbackInfo info) {

    }
    */
}
