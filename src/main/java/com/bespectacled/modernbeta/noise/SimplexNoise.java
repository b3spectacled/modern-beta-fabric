package com.bespectacled.modernbeta.noise;

import java.util.Random;

public class SimplexNoise extends Noise {
    private static int[][] gradients;
    private int[] permutations;
    public double xOffset;
    public double yOffset;
    public double zOffset;
    private static final double UNSKEW_FACTOR_2D;
    private static final double SKEW_FACTOR_2D;
    
    public SimplexNoise() {
        this(new Random());
    }
    
    public SimplexNoise(Random random) {
        this.permutations = new int[512];
        this.xOffset = random.nextDouble() * 256.0;
        this.yOffset = random.nextDouble() * 256.0;
        this.zOffset = random.nextDouble() * 256.0;
        for (int i = 0; i < 256; ++i) {
            this.permutations[i] = i;
        }
        for (int i = 0; i < 256; ++i) {
            int integer4 = random.nextInt(256 - i) + i;
            int integer5 = this.permutations[i];
            this.permutations[i] = this.permutations[integer4];
            this.permutations[integer4] = integer5;
            this.permutations[i + 256] = this.permutations[i];
        }
    }
    
    private static int floor(double double1) {
        return (double1 > 0.0) ? ((int)double1) : ((int)double1 - 1);
    }
    
    private static double dot(int[] arr, double double2, double double4) {
        return arr[0] * double2 + arr[1] * double4;
    }
    
    public void sample(double[] arr, double double3, double double5, int integer7, int integer8, double double9, double double11, double double13) {
        int integer15 = 0;
        for (int i = 0; i < integer7; ++i) {
            double double17 = (double3 + i) * double9 + this.xOffset;
            for (int j = 0; j < integer8; ++j) {
                double double20 = (double5 + j) * double11 + this.yOffset;
                double double28 = (double17 + double20) * SimplexNoise.UNSKEW_FACTOR_2D;
                int integer30 = floor(double17 + double28);
                int integer31 = floor(double20 + double28);
                double double32 = (integer30 + integer31) * SimplexNoise.SKEW_FACTOR_2D;
                double double34 = integer30 - double32;
                double double36 = integer31 - double32;
                double double38 = double17 - double34;
                double double40 = double20 - double36;
                int integer42;
                int integer43;
                if (double38 > double40) {
                    integer42 = 1;
                    integer43 = 0;
                }
                else {
                    integer42 = 0;
                    integer43 = 1;
                }
                double double44 = double38 - integer42 + SimplexNoise.SKEW_FACTOR_2D;
                double double46 = double40 - integer43 + SimplexNoise.SKEW_FACTOR_2D;
                double double48 = double38 - 1.0 + 2.0 * SimplexNoise.SKEW_FACTOR_2D;
                double double50 = double40 - 1.0 + 2.0 * SimplexNoise.SKEW_FACTOR_2D;
                int integer52 = integer30 & 0xFF;
                int integer53 = integer31 & 0xFF;
                int integer54 = this.permutations[integer52 + this.permutations[integer53]] % 12;
                int integer55 = this.permutations[integer52 + integer42 + this.permutations[integer53 + integer43]] % 12;
                int integer56 = this.permutations[integer52 + 1 + this.permutations[integer53 + 1]] % 12;
                double double57 = 0.5 - double38 * double38 - double40 * double40;
                double double22;
                if (double57 < 0.0) {
                    double22 = 0.0;
                }
                else {
                    double57 *= double57;
                    double22 = double57 * double57 * dot(SimplexNoise.gradients[integer54], double38, double40);
                }
                double double59 = 0.5 - double44 * double44 - double46 * double46;
                double double24;
                if (double59 < 0.0) {
                    double24 = 0.0;
                }
                else {
                    double59 *= double59;
                    double24 = double59 * double59 * dot(SimplexNoise.gradients[integer55], double44, double46);
                }
                double double61 = 0.5 - double48 * double48 - double50 * double50;
                double double26;
                if (double61 < 0.0) {
                    double26 = 0.0;
                }
                else {
                    double61 *= double61;
                    double26 = double61 * double61 * dot(SimplexNoise.gradients[integer56], double48, double50);
                }
                int n21 = integer15++;
                arr[n21] += 70.0 * (double22 + double24 + double26) * double13;
            }
        }
    }
    
    static {
        SimplexNoise.gradients = new int[][] { { 1, 1, 0 }, { -1, 1, 0 }, { 1, -1, 0 }, { -1, -1, 0 }, { 1, 0, 1 }, { -1, 0, 1 }, { 1, 0, -1 }, { -1, 0, -1 }, { 0, 1, 1 }, { 0, -1, 1 }, { 0, 1, -1 }, { 0, -1, -1 } };
        UNSKEW_FACTOR_2D = 0.5 * (Math.sqrt(3.0) - 1.0);
        SKEW_FACTOR_2D = (3.0 - Math.sqrt(3.0)) / 6.0;
    }
}
