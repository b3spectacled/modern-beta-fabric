package com.bespectacled.modernbeta.api.world.gen.noise;

import java.util.function.BiFunction;

public class BaseNoiseProvider extends NoiseProvider {
    public BaseNoiseProvider(
        int noiseSizeX, 
        int noiseSizeY, 
        int noiseSizeZ, 
        int noiseX, 
        int noiseZ, 
        BiFunction<Integer, Integer, double[]> noiseFunc
    ) {
        super(noiseSizeX, noiseSizeY, noiseSizeZ, noiseX, noiseZ, noiseFunc);
    }
}
