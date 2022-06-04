package com.bespectacled.modernbeta.mixin.heightprovider;

import java.util.Random;

import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.world.gen.HeightContext;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.heightprovider.TrapezoidHeightProvider;

@Mixin(TrapezoidHeightProvider.class)
public class MixinTrapezoidHeightProvider {
    @Unique private boolean warned = false;
    
    @Shadow private YOffset minOffset;
    @Shadow private YOffset maxOffset;
    @Shadow private int plateau;
    
    @Inject(method = "get", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V")) 
    private void injectWarnReturn(Random random, HeightContext context, CallbackInfoReturnable<Integer> info) {
        int minOffsetY = this.minOffset.getY(context);
        int maxOffsetY = this.maxOffset.getY(context);
        
        if (minOffsetY > maxOffsetY) {
            if (this.warned) {
                info.setReturnValue(minOffsetY);
            } else {
                ModernBeta.log(Level.INFO, "Suppressing height provider warning..");
                
                this.warned = true;
            }
        }
    }
}
