package com.bespectacled.modernbeta.world.gen.provider.indev;

public class IndevUtil {
    public static boolean inIndevRegion(int x, int z, int width, int length) {
        int halfWidth = width / 2;
        int halfLength = length / 2;
        
        if (x >= -halfWidth && x < halfWidth && z >= -halfLength && z < halfLength)
            return true;
        
        return false;
    }
}
