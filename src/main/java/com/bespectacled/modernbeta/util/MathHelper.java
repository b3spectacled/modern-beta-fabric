package com.bespectacled.modernbeta.util;

public class MathHelper {
    public static int floor_double(double d) {
        int i = (int) d;
        return d >= (double) i ? i : i - 1;
    }

}
