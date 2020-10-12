package com.bespectacled.modernbeta.util;

import net.minecraft.util.Util;

public class MathHelper {
    private static final float[] SINE_TABLE;
    private static final double[] ARCSINE_TABLE = new double[257];
    private static final double[] COSINE_TABLE = new double[257];

    public static float cos(float float1) {
        return MathHelper.SINE_TABLE[(int) (float1 * 10430.378f + 16384.0f) & 0xFFFF];
    }

    public static int floor_double(double d) {
        int i = (int) d;
        return d >= (double) i ? i : i - 1;
    }

    public static float clamp(float float1, float float2, float float3) {
        if (float1 < float2) {
            return float2;
        }
        if (float1 > float3) {
            return float3;
        }
        return float1;
    }

    static {
        SINE_TABLE = Util.<float[]>make(new float[65536], arr -> {
            for (int integer2 = 0; integer2 < arr.length; ++integer2) {
                arr[integer2] = (float) Math.sin(integer2 * 3.141592653589793 * 2.0 / 65536.0);
            }
            return;
        });

        for (int j = 0; j < 257; ++j) {
            double double2 = j / 256.0;
            double double4 = Math.asin(double2);
            MathHelper.COSINE_TABLE[j] = Math.cos(double4);
            MathHelper.ARCSINE_TABLE[j] = double4;
        }
    }
}
