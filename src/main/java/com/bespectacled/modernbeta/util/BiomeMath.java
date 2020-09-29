package com.bespectacled.modernbeta.util;

import net.minecraft.util.math.MathHelper;

/*
 * From WorldEdit
 */
public class BiomeMath {
	public static final int HORIZONTAL_SECTION_COUNT = (int) Math.round(Math.log(16.0D) / Math.log(2.0D)) - 2;
	public static final int VERTICAL_SECTION_COUNT = (int) Math.round(Math.log(256.0D) / Math.log(2.0D)) - 2;
	public static final int HORIZONTAL_BIT_MASK = (1 << HORIZONTAL_SECTION_COUNT) - 1;
	public static final int VERTICAL_BIT_MASK = (1 << VERTICAL_SECTION_COUNT) - 1;

	// Convert absolute coordinates to BiomeArray index
	public static int computeBiomeIndex (int x, int y, int z)
	{
		int l = (x >> 2) & HORIZONTAL_BIT_MASK;
		int m = MathHelper.clamp(y >> 2, 0, VERTICAL_BIT_MASK);
		int n = (z >> 2) & HORIZONTAL_BIT_MASK;
		
		return m << HORIZONTAL_SECTION_COUNT + HORIZONTAL_SECTION_COUNT | n << HORIZONTAL_SECTION_COUNT | l;
	}
}