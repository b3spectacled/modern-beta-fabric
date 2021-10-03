package com.bespectacled.modernbeta.world.gen.sampler;

@FunctionalInterface
public interface WeightSampler {
    public double sample(double weight, int x, int y, int z);
    
    public static final WeightSampler DEFAULT = (weight, x, y, z) -> weight;
}
