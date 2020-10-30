package com.bespectacled.modernbeta.colormap;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;

@Environment(EnvType.CLIENT)
public class WaterColors {
    private static int[] colorMap;
    
    public static void setColorMap(int[] arr) {
        WaterColors.colorMap = arr;
    }
    
    public static int getColor(double temp, double humid) {
        humid *= temp;
        int nt = (int)((1.0 - temp) * 255.0);
        int nh = (int)((1.0 - humid) * 255.0);
        int ndx = nh << 8 | nt;
        if (ndx > WaterColors.colorMap.length) {
            return -65281;
        }
        return WaterColors.colorMap[ndx];
    }
    
    static {
        WaterColors.colorMap = new int[65536];
    }
}

