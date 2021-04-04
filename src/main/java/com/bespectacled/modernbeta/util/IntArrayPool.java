package com.bespectacled.modernbeta.util;

import java.util.concurrent.ConcurrentLinkedQueue;

public class IntArrayPool {
    private final ConcurrentLinkedQueue<int[]> arrPool;
    
    private final int initialCapacity;
    private final int arraySize;
    
    public IntArrayPool(int initialCapacity, int arraySize) {
        this.arrPool = new ConcurrentLinkedQueue<int[]>();
        
        this.initialCapacity = initialCapacity;
        this.arraySize = arraySize;
        
        for (int i = 0; i < this.initialCapacity; ++i) {
            this.arrPool.add(new int[this.arraySize]);
        }
    }
    
    public int[] borrowArr() {
        int[] arrToBorrow = this.arrPool.poll();
        
        if (arrToBorrow == null)
            arrToBorrow = new int[this.arraySize];
        
        //System.out.println("Borrowing, size after poll: " + this.arrPool.size());
        
        return arrToBorrow;
    }
    
    public void returnArr(int[] arr) {
        if (arr == null || arr.length != this.arraySize)
            throw new IllegalArgumentException("[Modern Beta] Returned double array of invalid type!");

        //System.out.println("Returning, size before return: " + this.arrPool.size());
        
        this.arrPool.add(arr);
    }
}
