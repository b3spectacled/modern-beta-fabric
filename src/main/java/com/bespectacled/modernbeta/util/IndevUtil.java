package com.bespectacled.modernbeta.util;

public class IndevUtil {
    public enum Theme {
        NORMAL,
        HELL,
        PARADISE,
        WOODS,
        SNOWY
    }
    
    public enum Type {
        ISLAND,
        FLOATING,
        INLAND
    }
    
    public static boolean inIndevRegion(int x, int z, int width, int length) {
        int halfWidth = width / 2;
        int halfLength = length / 2;
        
        if (x >= -halfWidth && x < halfWidth && z >= -halfLength && z < halfLength)
            return true;
        
        return false;
    }
}
