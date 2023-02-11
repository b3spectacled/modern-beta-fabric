package mod.bespectacled.modernbeta.client.color;

public class WaterColorsBeta {
    private static int[] colorMap = new int[65536];

    public static void setColorMap(int[] map) {
        colorMap = map;
    }

    public static int getColor(double temp, double rain) {
        /*
        int rainNdx = (int)((1.0 - (rain *= temp)) * 255.0);
        int tempNdx = (int)((1.0 - temp) * 255.0);
        int ndx = rainNdx << 8 | tempNdx;
        
        if (ndx >= colorMap.length) {
            return -65281;
        }
        
        return colorMap[ndx];*/
        
        return 0xFFFFFF;
    }
}
