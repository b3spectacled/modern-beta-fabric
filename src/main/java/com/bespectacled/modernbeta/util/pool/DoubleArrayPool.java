package com.bespectacled.modernbeta.util.pool;

import java.util.concurrent.ConcurrentLinkedQueue;

public class DoubleArrayPool {
    private final ConcurrentLinkedQueue<double[]> arrPool;
    
    private final int initialCapacity;
    private final int arraySize;
    
    public DoubleArrayPool(int initialCapacity, int arraySize) {
        this.arrPool = new ConcurrentLinkedQueue<double[]>();
        
        this.initialCapacity = initialCapacity;
        this.arraySize = arraySize;
        
        for (int i = 0; i < this.initialCapacity; ++i) {
            this.arrPool.add(new double[this.arraySize]);
        }
    }
    
    public double[] borrowArr() {
        double[] arrToBorrow = this.arrPool.poll();
        
        if (arrToBorrow == null)
            arrToBorrow = new double[this.arraySize];
        
        return arrToBorrow;
    }
    
    public void returnArr(double[] arr) {
        if (arr == null || arr.length != this.arraySize)
            throw new IllegalArgumentException("[Modern Beta] Returned double array of invalid type!");

        //System.out.println("Returning, size before return: " + this.arrPool.size());
        
        this.arrPool.add(arr);
    }
}
