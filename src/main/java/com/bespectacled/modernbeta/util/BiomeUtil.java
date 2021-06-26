package com.bespectacled.modernbeta.util;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.source.BiomeCoords;

/**
 * Author: WorldEdit
 */
public class BiomeUtil {
    public static final int HORIZONTAL_SECTION_COUNT =  MathHelper.log2DeBruijn(16) - 2;
    public static final int HORIZONTAL_BIT_MASK = (1 << HORIZONTAL_SECTION_COUNT) - 1;
    
    // Convert absolute coordinates to BiomeArray index
    public static int computeBiomeIndex(int x, int y, int z, int worldBottomY, int worldHeight) {
        int biomeMinY = BiomeCoords.fromBlock(worldBottomY);
        int biomeMaxY = BiomeCoords.fromBlock(worldHeight) - 1;
        
        int l = (x >> 2) & HORIZONTAL_BIT_MASK;
        int m = MathHelper.clamp((y >> 2) - biomeMinY, 0, biomeMaxY);
        int n = (z >> 2) & HORIZONTAL_BIT_MASK;

        return m << HORIZONTAL_SECTION_COUNT + HORIZONTAL_SECTION_COUNT | n << HORIZONTAL_SECTION_COUNT | l;
    }
}