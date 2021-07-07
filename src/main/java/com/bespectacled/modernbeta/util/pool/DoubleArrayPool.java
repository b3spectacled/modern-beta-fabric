package com.bespectacled.modernbeta.util.pool;

import java.util.function.Predicate;

public class DoubleArrayPool extends ObjectPool<double[]> {
    private final int arraySize;

    public DoubleArrayPool(int initialCapacity, Predicate<double[]> validator, int arraySize) {
        super(initialCapacity, validator);

        this.arraySize = arraySize;
    }

    @Override
    protected double[] createObj() {
        return new double[this.arraySize];
    }
}
