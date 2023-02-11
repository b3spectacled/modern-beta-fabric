package mod.bespectacled.modernbeta.client.color;

public class BlockColorMap {
    private final int[] colorMap;
    
    public BlockColorMap() {
        this.colorMap = new int[65536];
    }
    
    public void setColorMap(int[] map) {
        if (map.length != 65536)
            throw new IllegalArgumentException("[Modern Beta] Color map is an invalid size!");
        
        for (int i = 0; i < colorMap.length; ++i) {
            this.colorMap[i] = map[i];
        }
    }
    
    public int getColor(double temp, double rain) {
        int rainNdx = (int)((1.0 - (rain *= temp)) * 255.0);
        int tempNdx = (int)((1.0 - temp) * 255.0);
        int ndx = rainNdx << 8 | tempNdx;
        
        if (ndx >= this.colorMap.length) {
            return -65281;
        }
        
        return this.colorMap[ndx];
    }
}
