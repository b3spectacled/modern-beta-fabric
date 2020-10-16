package com.bespectacled.modernbeta.noise;

import java.util.Random;

import com.bespectacled.modernbeta.util.MathHelper;

public final class IndevNoiseGeneratorPerlin extends IndevNoiseGenerator {
    private int[] permutations;
    
    public IndevNoiseGeneratorPerlin() {
        this(new Random());
    }
    
    public IndevNoiseGeneratorPerlin(Random random) {
        this.permutations = new int[512];
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
    
    private static double generateNoise(double double1) {
        return double1 * double1 * double1 * (double1 * (double1 * 6.0 - 15.0) + 10.0);
    }
    
    private static double lerp(double double1, double double3, double double5) {
        return double3 + double1 * (double5 - double3);
    }
    
    private static double grad(int integer, double double2, double double4, double double6) {
        double double9 = ((integer &= 0xF) < 8) ? double2 : double4;
        double double11 = (integer < 4) ? double4 : ((integer == 12 || integer == 14) ? double2 : double6);
        return (((integer & 0x1) == 0x0) ? double9 : (-double9)) + (((integer & 0x2) == 0x0) ? double11 : (-double11));
    }
    
    @Override
    public final double IndevNoiseGenerator(double double2, double double4) {
        double double11 = 0.0;
        double double9 = double4;
        double double7 = double2;
        int integer3 = MathHelper.floor_double(double7) & 0xFF;
        int integer4 = MathHelper.floor_double(double9) & 0xFF;
        int integer5 = MathHelper.floor_double(0.0) & 0xFF;
        double7 -= MathHelper.floor_double(double7);
        double9 -= MathHelper.floor_double(double9);
        double11 = 0.0 - MathHelper.floor_double(0.0);
        double double16 = generateNoise(double7);
        double double18 = generateNoise(double9);
        double double20 = generateNoise(double11);
        int integer6 = this.permutations[integer3] + integer4;
        int integer13 = this.permutations[integer6] + integer5;
        integer6 = this.permutations[integer6 + 1] + integer5;
        integer3 = this.permutations[integer3 + 1] + integer4;
        integer4 = this.permutations[integer3] + integer5;
        integer3 = this.permutations[integer3 + 1] + integer5;
        return lerp(double20, lerp(double18, lerp(double16, grad(this.permutations[integer13], double7, double9, double11), grad(this.permutations[integer4], double7 - 1.0, double9, double11)), lerp(double16, grad(this.permutations[integer6], double7, double9 - 1.0, double11), grad(this.permutations[integer3], double7 - 1.0, double9 - 1.0, double11))), lerp(double18, lerp(double16, grad(this.permutations[integer13 + 1], double7, double9, double11 - 1.0), grad(this.permutations[integer4 + 1], double7 - 1.0, double9, double11 - 1.0)), lerp(double16, grad(this.permutations[integer6 + 1], double7, double9 - 1.0, double11 - 1.0), grad(this.permutations[integer3 + 1], double7 - 1.0, double9 - 1.0, double11 - 1.0))));
    }
}
