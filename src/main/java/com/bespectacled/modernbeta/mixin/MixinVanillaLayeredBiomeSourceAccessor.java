package com.bespectacled.modernbeta.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;

@Mixin(VanillaLayeredBiomeSource.class)
public interface MixinVanillaLayeredBiomeSourceAccessor {
    @Accessor
    static List<RegistryKey<Biome>> getBIOMES() {
        throw new AssertionError();
    }
}
