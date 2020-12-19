package com.bespectacled.modernbeta.util;

import java.util.Random;
import com.bespectacled.modernbeta.noise.SimplexOctaveNoise;

import net.minecraft.util.math.MathHelper;

/**
 * Author: WorldEdit
 */
public class BiomeUtil {
    private static final int HORIZONTAL_SECTION_COUNT = (int) Math.round(Math.log(16.0D) / Math.log(2.0D)) - 2;
    private static final int VERTICAL_SECTION_COUNT = (int) Math.round(Math.log(256.0D) / Math.log(2.0D)) - 2;
    private static final int HORIZONTAL_BIT_MASK = (1 << HORIZONTAL_SECTION_COUNT) - 1;
    private static final int VERTICAL_BIT_MASK = (1 << VERTICAL_SECTION_COUNT) - 1;

    private static SimplexOctaveNoise tempNoiseOctaves = new SimplexOctaveNoise(new Random(1 * 9871L), 4);
    private static SimplexOctaveNoise humidNoiseOctaves = new SimplexOctaveNoise(new Random(1 * 39811L), 4);
    private static SimplexOctaveNoise noiseOctaves = new SimplexOctaveNoise(new Random(1 * 543321L), 2);
    
    // Convert absolute coordinates to BiomeArray index
    public static int computeBiomeIndex(int x, int y, int z) {
        int l = (x >> 2) & HORIZONTAL_BIT_MASK;
        int m = MathHelper.clamp(y >> 2, 0, VERTICAL_BIT_MASK);
        int n = (z >> 2) & HORIZONTAL_BIT_MASK;

        return m << HORIZONTAL_SECTION_COUNT + HORIZONTAL_SECTION_COUNT | n << HORIZONTAL_SECTION_COUNT | l;
    }
}