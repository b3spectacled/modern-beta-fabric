package com.bespectacled.modernbeta.mixin;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeArray;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.bespectacled.modernbeta.util.BiomeUtil;
import com.bespectacled.modernbeta.util.MutableBiomeArray;

/**
 * @Author WorldEdit
 */
@Mixin(BiomeArray.class)
public abstract class MixinBiomeArray implements MutableBiomeArray {
    @Shadow private Biome[] data;

    @Override
    public void setBiome(int x, int y, int z, Biome biome) {
        this.data[BiomeUtil.computeBiomeIndex(x, y, z)] = biome;
    }

    @Override
    public Biome getBiome(int x, int y, int z) {
        return this.data[BiomeUtil.computeBiomeIndex(x, y, z)];
    }
    
    @Override
    public void setBiome(int i, Biome biome) {
        this.data[i] = biome;
    }
    
    @Override
    public Biome getBiome(int i) {
        return this.data[i];
    }
    
    @Override
    public int getBiomeArrLen() {
        return this.data.length;
    }
}