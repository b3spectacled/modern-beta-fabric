package com.bespectacled.modernbeta.util.noise;

import java.util.Arrays;

import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes.class_7050;

public enum SimpleDensityFunction implements class_7050 {
    INSTANCE;

    @Override
    public double sample(DensityFunction.NoisePos pos) {
        return 0.0;
    }

    @Override
    public void method_40470(double[] ds, class_6911 arg) {
        Arrays.fill(ds, 0.0);
    }

    @Override
    public double minValue() {
        return 0.0;
    }

    @Override
    public double maxValue() {
        return 0.0;
    }
}