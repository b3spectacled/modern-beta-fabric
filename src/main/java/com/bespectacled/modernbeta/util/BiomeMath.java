package com.bespectacled.modernbeta.util;

import java.util.Random;

import com.bespectacled.modernbeta.noise.OldNoiseGeneratorOctaves2;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;

/*
 * From WorldEdit
 */
public class BiomeMath {
    public static final int HORIZONTAL_SECTION_COUNT = (int) Math.round(Math.log(16.0D) / Math.log(2.0D)) - 2;
    public static final int VERTICAL_SECTION_COUNT = (int) Math.round(Math.log(256.0D) / Math.log(2.0D)) - 2;
    public static final int HORIZONTAL_BIT_MASK = (1 << HORIZONTAL_SECTION_COUNT) - 1;
    public static final int VERTICAL_BIT_MASK = (1 << VERTICAL_SECTION_COUNT) - 1;

    private static OldNoiseGeneratorOctaves2 tempNoiseOctaves = new OldNoiseGeneratorOctaves2(new Random(0 * 9871L),
            4);;
    private static OldNoiseGeneratorOctaves2 humidNoiseOctaves = new OldNoiseGeneratorOctaves2(new Random(0 * 39811L),
            4);
    private static OldNoiseGeneratorOctaves2 noiseOctaves = new OldNoiseGeneratorOctaves2(new Random(0 * 543321L), 2);

    public static double[] temps = null;
    public static double[] humids = null;
    public static double[] noises = null;

    public static double[][] temps2D = new double[16][16];

    // Convert absolute coordinates to BiomeArray index
    public static int computeBiomeIndex(int x, int y, int z) {
        int l = (x >> 2) & HORIZONTAL_BIT_MASK;
        int m = MathHelper.clamp(y >> 2, 0, VERTICAL_BIT_MASK);
        int n = (z >> 2) & HORIZONTAL_BIT_MASK;

        return m << HORIZONTAL_SECTION_COUNT + HORIZONTAL_SECTION_COUNT | n << HORIZONTAL_SECTION_COUNT | l;
    }

    public static void setSeed(long seed) {
        initOctaves(seed);
    }

    private static void initOctaves(long seed) {
        tempNoiseOctaves = new OldNoiseGeneratorOctaves2(new Random(seed * 9871L), 4);
        humidNoiseOctaves = new OldNoiseGeneratorOctaves2(new Random(seed * 39811L), 4);
        noiseOctaves = new OldNoiseGeneratorOctaves2(new Random(seed * 543321L), 2);
    }

    public static void fetchTempHumid(int x, int z, int sizeX, int sizeZ) {
        temps = tempNoiseOctaves.func_4112_a(temps, x, z, sizeX, sizeX, 0.02500000037252903D, 0.02500000037252903D,
                0.25D);
        humids = humidNoiseOctaves.func_4112_a(humids, x, z, sizeX, sizeX, 0.05000000074505806D, 0.05000000074505806D,
                0.33333333333333331D);
        noises = noiseOctaves.func_4112_a(noises, x, z, sizeX, sizeX, 0.25D, 0.25D, 0.58823529411764708D);

        int i = 0;
        for (int j = 0; j < sizeX; j++) {
            for (int k = 0; k < sizeZ; k++) {
                double d = noises[i] * 1.1000000000000001D + 0.5D;
                double d1 = 0.01D;
                double d2 = 1.0D - d1;

                double temp = (temps[i] * 0.14999999999999999D + 0.69999999999999996D) * d2 + d * d1;

                d1 = 0.002D;
                d2 = 1.0D - d1;

                double humid = (humids[i] * 0.14999999999999999D + 0.5D) * d2 + d * d1;
                temp = 1.0D - (1.0D - temp) * (1.0D - temp);

                if (temp < 0.0D) {
                    temp = 0.0D;
                }
                if (humid < 0.0D) {
                    humid = 0.0D;
                }
                if (temp > 1.0D) {
                    temp = 1.0D;
                }
                if (humid > 1.0D) {
                    humid = 1.0D;
                }

                temps[i] = temp;
                humids[i] = humid;

                temps2D[j][k] = temp;

                i++;
            }
        }
    }

    public static double[] fetchTempHumidAtPoint(int x, int z) {
        double[] temps = null;
        double[] humids = null;
        double[] noises = null;

        temps = tempNoiseOctaves.func_4112_a(temps, x, z, 1, 1, 0.02500000037252903D, 0.02500000037252903D, 0.25D);
        humids = humidNoiseOctaves.func_4112_a(humids, x, z, 1, 1, 0.05000000074505806D, 0.05000000074505806D,
                0.33333333333333331D);
        noises = noiseOctaves.func_4112_a(noises, x, z, 1, 1, 0.25D, 0.25D, 0.58823529411764708D);

        double d = noises[0] * 1.1000000000000001D + 0.5D;
        double d1 = 0.01D;
        double d2 = 1.0D - d1;

        double temp = (temps[0] * 0.14999999999999999D + 0.69999999999999996D) * d2 + d * d1;

        d1 = 0.002D;
        d2 = 1.0D - d1;

        double humid = (humids[0] * 0.14999999999999999D + 0.5D) * d2 + d * d1;

        temp = 1.0D - (1.0D - temp) * (1.0D - temp);
        if (temp < 0.0D) {
            temp = 0.0D;
        }
        if (humid < 0.0D) {
            humid = 0.0D;
        }
        if (temp > 1.0D) {
            temp = 1.0D;
        }
        if (humid > 1.0D) {
            humid = 1.0D;
        }

        return new double[] { temp, humid };
    }

    public static double fetchSkyTemp(int x, int z) {
        double[] skyTemps = null;

        skyTemps = tempNoiseOctaves.func_4112_a(skyTemps, x, z, 1, 1, 0.02500000037252903D, 0.02500000037252903D, 0.5D);
        return skyTemps[0];
    }
}