package com.bespectacled.modernbeta.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.gen.decorator.PlacementModifier;
import net.minecraft.world.gen.feature.PlacedFeature;

@Mixin(PlacedFeature.class)
public interface MixinPlacedFeatureAccessor {
    @Accessor
    List<PlacementModifier> getPlacementModifiers();
}
