package com.bespectacled.modernbeta.util.pool;

import java.util.function.Predicate;

public class IntArrayPool extends ObjectPool<int[]> {
    private final int arraySize;

    public IntArrayPool(int initialCapacity, Predicate<int[]> validator, int arraySize) {
        super(initialCapacity, validator);

        this.arraySize = arraySize;
    }

    @Override
    protected int[] createObj() {
        return new int[this.arraySize];
    }
}
