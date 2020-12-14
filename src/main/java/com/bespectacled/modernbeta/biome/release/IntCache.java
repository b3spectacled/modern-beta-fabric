package com.bespectacled.modernbeta.biome.release;

import java.util.ArrayList;
import java.util.List;

public class IntCache {
    private static int intCacheSize = 256;
    private static List<int[]> smallArrayPool = new ArrayList<int[]>();
    private static List<int[]> smallArrayUsed = new ArrayList<int[]>();
    private static List<int[]> largeArrayPool = new ArrayList<int[]>();
    private static List<int[]> largeArrayUsed = new ArrayList<int[]>();

    public IntCache() {}

    public static synchronized int[] getIntCache(int size) {
        if(size <= 256) {
            if(smallArrayPool.size() == 0) {
                int arr[] = new int[256];
                smallArrayUsed.add(arr);
                
                return arr;
            } else {
                int arr[] = (int[])smallArrayPool.remove(smallArrayPool.size() - 1);
                smallArrayUsed.add(arr);
                
                return arr;
            }
        }
        
        if(size > intCacheSize) {
            intCacheSize = size;
            largeArrayPool.clear();
            largeArrayUsed.clear();
            
            int arr[] = new int[intCacheSize];
            largeArrayUsed.add(arr);
            
            return arr;
        }
        
        if(largeArrayPool.size() == 0) {
            int arr[] = new int[intCacheSize];
            largeArrayUsed.add(arr);
            
            return arr;
        } else {
            int arr[] = (int[])largeArrayPool.remove(largeArrayPool.size() - 1);
            largeArrayUsed.add(arr);
            
            return arr;
        }
    }

    public static synchronized void resetIntCache() {
        if(largeArrayPool.size() > 0) {
            largeArrayPool.remove(largeArrayPool.size() - 1);
        }
        if(smallArrayPool.size() > 0) {
            smallArrayPool.remove(smallArrayPool.size() - 1);
        }
        
        largeArrayPool.addAll(largeArrayUsed);
        smallArrayPool.addAll(smallArrayUsed);
        largeArrayUsed.clear();
        smallArrayUsed.clear();
    }
}
