package com.bespectacled.modernbeta.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.gen.NoiseColumnSampler;

@Mixin(NoiseColumnSampler.class)
public interface MixinNoiseColumnSamplerInvoker {
    @Accessor
    public DoublePerlinNoiseSampler getCaveCheeseNoise();
    
    @Invoker("sampleCaveEntranceNoise")
    public double invokeSampleCaveEntranceNoise(int x, int y, int z);
    
    @Invoker("samplePillarNoise")
    public double invokeSamplePillarNoise(int x, int y, int z);
    
    @Invoker("sampleCaveLayerNoise")
    public double invokeSampleCaveLayerNoise(int x, int y, int z);
    
    @Invoker("sampleSpaghetti3dNoise")
    public double invokeSampleSpaghetti3dNoise(int x, int y, int z);
    
    @Invoker("sampleSpaghetti2dNoise")
    public double invokeSampleSpaghetti2dNoise(int x, int y, int z);
    
    @Invoker("sampleSpaghettiRoughnessNoise")
    public double invokeSampleSpaghettiRoughnessNoise(int x, int y, int z);
}   
