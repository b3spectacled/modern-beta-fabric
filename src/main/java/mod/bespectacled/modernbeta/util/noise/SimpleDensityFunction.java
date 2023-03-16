package mod.bespectacled.modernbeta.util.noise;

import java.util.Arrays;

import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public enum SimpleDensityFunction implements DensityFunctionTypes.Beardifying {
    INSTANCE;

    @Override
    public double sample(DensityFunction.NoisePos pos) {
        return 0.0;
    }

    @Override
    public void fill(double[] densities, DensityFunction.EachApplier applier) {
        Arrays.fill(densities, 0.0);
    }
    
    @Override
    public double maxValue() {
        return 0;
    }

    @Override
    public double minValue() {
        return 0;
    }
}