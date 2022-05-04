package com.bespectacled.modernbeta.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.StructureFeature;

@Mixin(StructureFeature.class)
public interface MixinStructureFeatureAccessor {
    @Invoker("register")
    public static <F extends StructureFeature<?>> F invokeRegister(String name, F structureFeature, GenerationStep.Feature step) {
        throw new AssertionError();
    }
}
