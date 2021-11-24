package com.bespectacled.modernbeta.world.gen.sampler;

@FunctionalInterface
public interface DensitySampler {
    public double sample(double density, int x, int y, int z);
    
    public static final DensitySampler DEFAULT = (density, x, y, z) -> density;
}
