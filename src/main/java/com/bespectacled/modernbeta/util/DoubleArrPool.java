package com.bespectacled.modernbeta.util;

import java.util.LinkedList;

public class DoubleArrPool {
    private LinkedList<double[]> arrPool;
    
    private final int initialCapacity;
    private final int arraySize;
    
    public DoubleArrPool(int initialCapacity, int arraySize) {
        this.arrPool = new LinkedList<double[]>();
        
        this.initialCapacity = initialCapacity;
        this.arraySize = arraySize;
        
        for (int i = 0; i < this.initialCapacity; ++i) {
            this.arrPool.add(new double[this.arraySize]);
        }
    }
    
    public synchronized double[] borrowArr() {
        double[] arrToBorrow;
        
        if (this.arrPool.size() <= 0) {
            arrToBorrow = new double[this.arraySize];
        } else {
            arrToBorrow = this.arrPool.removeFirst();
        }
        
        //System.out.println("Borrowing arr, new size: " + this.availableArrs.size());
        
        return arrToBorrow;
    }
    
    public synchronized void returnArr(double[] arr) {
        if (arr == null || arr.length != this.arraySize)
            throw new IllegalStateException("[Modern Beta] Returned double array pool of invalid type!");
        
        //System.out.println("Returning arr, new size: " + this.availableArrs.size());
        
        this.arrPool.addLast(arr);
    }
}
