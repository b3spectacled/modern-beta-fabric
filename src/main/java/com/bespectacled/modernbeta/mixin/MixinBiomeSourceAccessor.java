package com.bespectacled.modernbeta.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

@Mixin(BiomeSource.class)
public interface MixinBiomeSourceAccessor {
    @Mutable
    @Accessor("biomes")
    public void setBiomes(List<Biome> biomes);
}
