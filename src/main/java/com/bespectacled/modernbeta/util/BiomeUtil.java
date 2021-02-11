package com.bespectacled.modernbeta.util;

import net.minecraft.util.math.MathHelper;

/**
 * Author: WorldEdit
 */
public class BiomeUtil {
    private static final int HORIZONTAL_SECTION_COUNT =  MathHelper.log2DeBruijn(16) - 2;
    // private static final int VERTICAL_SECTION_COUNT = (int) Math.round(Math.log(256.0D) / Math.log(2.0D)) - 2;
    private static final int HORIZONTAL_BIT_MASK = (1 << HORIZONTAL_SECTION_COUNT) - 1;
    // private static final int VERTICAL_BIT_MASK = (1 << VERTICAL_SECTION_COUNT) - 1;
    
    // Convert absolute coordinates to BiomeArray index
    public static int computeBiomeIndex(int x, int y, int z) {
        // Changed: Vertical bit mask no longer used for 1.17,
        // array index for biomeY is now computed using lower and upper limits of chunk section. 
        // int m = MathHelper.clamp(y >> 2, 0, VERTICAL_BIT_MASK);

        int bottomY = -64;
        int height = 384;
        
        int biomeMinY = (bottomY >> 2);
        int biomeHeightY = (height >> 2) - 1;
        
        int l = (x >> 2) & HORIZONTAL_BIT_MASK;
        int m = MathHelper.clamp((y >> 2) - biomeMinY, 0, biomeHeightY);
        int n = (z >> 2) & HORIZONTAL_BIT_MASK;

        return m << HORIZONTAL_SECTION_COUNT + HORIZONTAL_SECTION_COUNT | n << HORIZONTAL_SECTION_COUNT | l;
    }
}