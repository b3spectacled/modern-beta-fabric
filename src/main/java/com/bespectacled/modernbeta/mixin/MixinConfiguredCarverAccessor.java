package com.bespectacled.modernbeta.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.ConfiguredCarver;

@Mixin(ConfiguredCarver.class)
public interface MixinConfiguredCarverAccessor {
    @Accessor("carver")
    public Carver<?> getCarver();
}
