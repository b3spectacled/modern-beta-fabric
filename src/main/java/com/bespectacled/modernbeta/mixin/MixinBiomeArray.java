package com.bespectacled.modernbeta.mixin;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeArray;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.bespectacled.modernbeta.util.BiomeUtil;
import com.bespectacled.modernbeta.util.mutable.MutableBiomeArray;

/**
 * @Author WorldEdit
 */
@Mixin(BiomeArray.class)
public abstract class MixinBiomeArray implements MutableBiomeArray {
    @Shadow private Biome[] data;

    @Override
    public void setBiome(int x, int y, int z, Biome biome, int worldBottomY, int worldHeight) {
        this.data[BiomeUtil.computeBiomeIndex(x, y, z, worldBottomY, worldHeight)] = biome;
    }

    @Override
    public Biome getBiome(int x, int y, int z, int worldBottomY, int worldHeight) {
        return this.data[BiomeUtil.computeBiomeIndex(x, y, z, worldBottomY, worldHeight)];
    }
}