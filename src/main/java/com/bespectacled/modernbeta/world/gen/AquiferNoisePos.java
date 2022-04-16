package com.bespectacled.modernbeta.world.gen;

import net.minecraft.world.gen.densityfunction.DensityFunction;

public class AquiferNoisePos implements DensityFunction.NoisePos {
    private int blockX;
    private int blockY;
    private int blockZ;
    
    public AquiferNoisePos() {
        this.blockX = 0;
        this.blockY = 0;
        this.blockZ = 0;
    }

    public AquiferNoisePos(int blockX, int blockY, int blockZ) {
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
    }
    
    public AquiferNoisePos setBlockCoords(int blockX, int blockY, int blockZ) {
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
        
        return this;
    }
    
    @Override
    public int blockX() {
        return this.blockX;
    }

    @Override
    public int blockY() {
        return this.blockY;
    }

    @Override
    public int blockZ() {
        return this.blockZ;
    }

}
