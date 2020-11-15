package com.bespectacled.modernbeta.noise;

import java.util.Random;

import com.bespectacled.modernbeta.util.MathHelper;

/*
 * Used for additional info: https://adrianb.io/2014/08/09/perlinnoise.html
 */
public class PerlinNoise extends Noise {

    private int permutations[]; 
    
    public double xCoord;
    public double yCoord;
    public double zCoord;

    public PerlinNoise() {
        this(new Random(), false);
    }

    public PerlinNoise(Random random, boolean isIndev) {

        // Generate permutation array
        permutations = new int[512];

        if (!isIndev) {
            xCoord = random.nextDouble() * 256D;
            yCoord = random.nextDouble() * 256D;
            zCoord = random.nextDouble() * 256D; 
        }
        
        for (int i = 0; i < 256; i++) {
            permutations[i] = i;
        }

        for (int i = 0; i < 256; i++) {
            int j = random.nextInt(256 - i) + i;
            int k = permutations[i];

            permutations[i] = permutations[j];
            permutations[j] = k;

            // Repeat first 256 values to avoid buffer overflow
            permutations[i + 256] = permutations[i];
        }
    }

    

    public final double lerp(double d, double d1, double d2) {
        return d1 + d * (d2 - d1);
    }

    /*
    public final double grad(int i, double d, double d1) {
        int j = i & 0xf;
        double d2 = (double) (1 - ((j & 8) >> 3)) * d;
        double d3 = j >= 4 ? j != 12 && j != 14 ? d1 : d : 0.0D;
        return ((j & 1) != 0 ? -d2 : d2) + ((j & 2) != 0 ? -d3 : d3);
    }*/
    
    public final double grad(int hash, double x, double y) {
        int integer7 = hash & 0xF;
        double double8 = (1 - ((integer7 & 0x8) >> 3)) * x;
        double double10 = (integer7 < 4) ? 0.0 : ((integer7 == 12 || integer7 == 14) ? x : y);
        return (((integer7 & 0x1) == 0x0) ? double8 : (-double8)) + (((integer7 & 0x2) == 0x0) ? double10 : (-double10));
    }

    // Using alternate function from
    // https://adrianb.io/2014/08/09/perlinnoise.html
    public final double grad(int hash, double x, double y, double z) {
        switch (hash & 0xF) {
            case 0x0:
                return x + y;
            case 0x1:
                return -x + y;
            case 0x2:
                return x - y;
            case 0x3:
                return -x - y;
            case 0x4:
                return x + z;
            case 0x5:
                return -x + z;
            case 0x6:
                return x - z;
            case 0x7:
                return -x - z;
            case 0x8:
                return y + z;
            case 0x9:
                return -y + z;
            case 0xA:
                return y - z;
            case 0xB:
                return -y - z;
            case 0xC:
                return y + x;
            case 0xD:
                return -y + z;
            case 0xE:
                return y - x;
            case 0xF:
                return -y - z;
            default:
                return 0; // never happens
        }
    }

    public double sampleBeta(double d, double d1) {
        return this.sampleBetaNoise(d, d1, 0.0D);
    }
    
    public final double sampleInfdev(double double2, double double4) {
        return this.sampleInfdevNoise(double2, double4, 0.0);
    }
    
    public final double sampleInfdev(double double2, double double4, double double6) {
        return this.sampleInfdevNoise(double2, double4, double6);
    }

    
    private static double generateIndevNoise(double double1) {
        return double1 * double1 * double1 * (double1 * (double1 * 6.0 - 15.0) + 10.0);
    }
    
    public final double sampleIndevNoise(double double2, double double4) {
        double double11 = 0.0;
        double double9 = double4;
        double double7 = double2;
        int integer3 = MathHelper.floor_double(double7) & 0xFF;
        int integer4 = MathHelper.floor_double(double9) & 0xFF;
        int integer5 = MathHelper.floor_double(0.0) & 0xFF;
        double7 -= MathHelper.floor_double(double7);
        double9 -= MathHelper.floor_double(double9);
        double11 = 0.0 - MathHelper.floor_double(0.0);
        double double16 = generateIndevNoise(double7);
        double double18 = generateIndevNoise(double9);
        double double20 = generateIndevNoise(double11);
        int integer6 = this.permutations[integer3] + integer4;
        int integer13 = this.permutations[integer6] + integer5;
        integer6 = this.permutations[integer6 + 1] + integer5;
        integer3 = this.permutations[integer3 + 1] + integer4;
        integer4 = this.permutations[integer3] + integer5;
        integer3 = this.permutations[integer3 + 1] + integer5;
        return lerp(
            double20, 
            lerp(
                double18, 
                lerp(
                    double16, 
                    grad(this.permutations[integer13], double7, double9, double11), 
                    grad(this.permutations[integer4], double7 - 1.0, double9, double11)), 
                lerp(
                    double16, 
                    grad(this.permutations[integer6], double7, double9 - 1.0, double11), 
                    grad(this.permutations[integer3], double7 - 1.0, double9 - 1.0, double11))), 
            lerp(
                double18, 
                lerp(
                    double16, 
                    grad(this.permutations[integer13 + 1], double7, double9, double11 - 1.0), 
                    grad(this.permutations[integer4 + 1], double7 - 1.0, double9, double11 - 1.0)), 
                lerp(
                    double16, 
                    grad(this.permutations[integer6 + 1], double7, double9 - 1.0, double11 - 1.0), 
                    grad(this.permutations[integer3 + 1], double7 - 1.0, double9 - 1.0, double11 - 1.0))));
    }

    private double sampleInfdevNoise(double double2, double double4, double double6) {
        double double8 = double2 + this.xCoord;
        double double10 = double4 + this.yCoord;
        double double12 = double6 + this.zCoord;
        int integer2 = MathHelper.floor_double(double8) & 0xFF;
        int integer3 = MathHelper.floor_double(double10) & 0xFF;
        int integer4 = MathHelper.floor_double(double12) & 0xFF;
        double8 -= MathHelper.floor_double(double8);
        double10 -= MathHelper.floor_double(double10);
        double12 -= MathHelper.floor_double(double12);
        double double17 = generateIndevNoise(double8);
        double double19 = generateIndevNoise(double10);
        double double21 = generateIndevNoise(double12);
        int integer5 = this.permutations[integer2] + integer3;
        int integer6 = this.permutations[integer5] + integer4;
        integer5 = this.permutations[integer5 + 1] + integer4;
        integer2 = this.permutations[integer2 + 1] + integer3;
        integer3 = this.permutations[integer2] + integer4;
        integer2 = this.permutations[integer2 + 1] + integer4;
        return lerp(
            double21, 
            lerp(
                double19, 
                lerp(
                    double17, 
                    grad(this.permutations[integer6], double8, double10, double12), 
                    grad(this.permutations[integer3], double8 - 1.0, double10, double12)), 
                lerp(
                    double17, 
                    grad(this.permutations[integer5], double8, double10 - 1.0, double12), 
                    grad(this.permutations[integer2], double8 - 1.0, double10 - 1.0, double12))), 
            lerp(
                double19, 
                lerp(
                    double17, 
                    grad(this.permutations[integer6 + 1], double8, double10, double12 - 1.0), 
                    grad(this.permutations[integer3 + 1], double8 - 1.0, double10, double12 - 1.0)), 
                lerp(
                    double17, 
                    grad(this.permutations[integer5 + 1], double8, double10 - 1.0, double12 - 1.0), 
                    grad(this.permutations[integer2 + 1], double8 - 1.0, double10 - 1.0, double12 - 1.0))));
    }
    
    public void sampleAlphaNoise(double ad[], double d, double d1, double d2, int i, int j, int k, double d3, double d4,
            double d5, double d6) {
        int l = 0;
        double d7 = 1.0D / d6;
        int i1 = -1;
        double d8 = 0.0D;
        double d9 = 0.0D;
        double d10 = 0.0D;
        double d11 = 0.0D;
        for (int l2 = 0; l2 < i; l2++) {
            double d12 = (d + (double) l2) * d3 + xCoord;
            int i3 = (int) d12;
            if (d12 < (double) i3) {
                i3--;
            }
            int j3 = i3 & 0xff;
            d12 -= i3;
            double d13 = d12 * d12 * d12 * (d12 * (d12 * 6D - 15D) + 10D);
            for (int k3 = 0; k3 < k; k3++) {
                double d14 = (d2 + (double) k3) * d5 + zCoord;
                int l3 = (int) d14;
                if (d14 < (double) l3) {
                    l3--;
                }
                int i4 = l3 & 0xff;
                d14 -= l3;
                double d15 = d14 * d14 * d14 * (d14 * (d14 * 6D - 15D) + 10D);
                for (int j4 = 0; j4 < j; j4++) {
                    double d16 = (d1 + (double) j4) * d4 + yCoord;
                    int k4 = (int) d16;
                    if (d16 < (double) k4) {
                        k4--;
                    }
                    int l4 = k4 & 0xff;
                    d16 -= k4;
                    double d17 = d16 * d16 * d16 * (d16 * (d16 * 6D - 15D) + 10D);
                    if (j4 == 0 || l4 != i1) {
                        i1 = l4;
                        int j1 = permutations[j3] + l4;
                        int k1 = permutations[j1] + i4;
                        int l1 = permutations[j1 + 1] + i4;
                        int i2 = permutations[j3 + 1] + l4;
                        int j2 = permutations[i2] + i4;
                        int k2 = permutations[i2 + 1] + i4;
                        d8 = lerp(d13, grad(permutations[k1], d12, d16, d14),
                                grad(permutations[j2], d12 - 1.0D, d16, d14));
                        d9 = lerp(d13, grad(permutations[l1], d12, d16 - 1.0D, d14),
                                grad(permutations[k2], d12 - 1.0D, d16 - 1.0D, d14));
                        d10 = lerp(d13, grad(permutations[k1 + 1], d12, d16, d14 - 1.0D),
                                grad(permutations[j2 + 1], d12 - 1.0D, d16, d14 - 1.0D));
                        d11 = lerp(d13, grad(permutations[l1 + 1], d12, d16 - 1.0D, d14 - 1.0D),
                                grad(permutations[k2 + 1], d12 - 1.0D, d16 - 1.0D, d14 - 1.0D));
                    }
                    double d18 = lerp(d17, d8, d9);
                    double d19 = lerp(d17, d10, d11);
                    double d20 = lerp(d15, d18, d19);
                    ad[l++] += d20 * d7;
                }

            }

        }

    }
    
    public double sampleBetaNoise(double d, double d1, double d2) {
        double d3 = d + xCoord;
        double d4 = d1 + yCoord;
        double d5 = d2 + zCoord;
        int i = (int) d3;
        int j = (int) d4;
        int k = (int) d5;
        if (d3 < (double) i) {
            i--;
        }
        if (d4 < (double) j) {
            j--;
        }
        if (d5 < (double) k) {
            k--;
        }
        int l = i & 0xff;
        int i1 = j & 0xff;
        int j1 = k & 0xff;
        d3 -= i;
        d4 -= j;
        d5 -= k;
        double d6 = d3 * d3 * d3 * (d3 * (d3 * 6D - 15D) + 10D);
        double d7 = d4 * d4 * d4 * (d4 * (d4 * 6D - 15D) + 10D);
        double d8 = d5 * d5 * d5 * (d5 * (d5 * 6D - 15D) + 10D);
        int k1 = permutations[l] + i1;
        int l1 = permutations[k1] + j1;
        int i2 = permutations[k1 + 1] + j1;
        int j2 = permutations[l + 1] + i1;
        int k2 = permutations[j2] + j1;
        int l2 = permutations[j2 + 1] + j1;
        return lerp(
            d8, 
            lerp(
                d7, 
                lerp(
                    d6, 
                    grad(permutations[l1], d3, d4, d5), 
                    grad(permutations[k2], d3 - 1.0D, d4, d5)), 
                lerp(d6, 
                    grad(permutations[i2], d3, d4 - 1.0D, d5), 
                    grad(permutations[l2], d3 - 1.0D, d4 - 1.0D, d5))),
            lerp(
                d7, 
                lerp(
                    d6,
                    grad(permutations[l1 + 1], d3, d4, d5 - 1.0D), 
                    grad(permutations[k2 + 1], d3 - 1.0D, d4, d5 - 1.0D)),
                lerp(
                    d6, 
                    grad(permutations[i2 + 1], d3, d4 - 1.0D, d5 - 1.0D), 
                    grad(permutations[l2 + 1], d3 - 1.0D, d4 - 1.0D, d5 - 1.0D)))
        );
    }
    
    public void sampleBetaNoise(double ad[], double d, double d1, double d2, int i, int j, int k, double d3, double d4,
            double d5, double d6) {
        if (j == 1) {
            int j3 = 0;
            double d12 = 1.0D / d6;
            for (int i4 = 0; i4 < i; i4++) {
                double d14 = (d + (double) i4) * d3 + xCoord;
                int j4 = (int) d14;
                if (d14 < (double) j4) {
                    j4--;
                }
                int k4 = j4 & 0xff;
                d14 -= j4;
                double d17 = d14 * d14 * d14 * (d14 * (d14 * 6D - 15D) + 10D);
                for (int l4 = 0; l4 < k; l4++) {
                    double d19 = (d2 + (double) l4) * d5 + zCoord;
                    int j5 = (int) d19;
                    if (d19 < (double) j5) {
                        j5--;
                    }
                    int l5 = j5 & 0xff;
                    d19 -= j5;
                    double d21 = d19 * d19 * d19 * (d19 * (d19 * 6D - 15D) + 10D);
                    int l = permutations[k4] + 0;
                    int j1 = permutations[l] + l5;
                    int k1 = permutations[k4 + 1] + 0;
                    int l1 = permutations[k1] + l5;
                    
                    double d9 = lerp(
                        d17, 
                        grad(permutations[j1], d14, d19),
                        grad(permutations[l1], d14 - 1.0D, 0.0D, d19));
                    double d11 = lerp(
                        d17, 
                        grad(permutations[j1 + 1], d14, 0.0D, d19 - 1.0D),
                        grad(permutations[l1 + 1], d14 - 1.0D, 0.0D, d19 - 1.0D));
                    double d23 = lerp(d21, d9, d11);
                    
                    ad[j3++] += d23 * d12;
                }

            }

            return;
        }
        int i1 = 0;
        double d7 = 1.0D / d6;
        int i2 = -1;

        double d13 = 0.0D;
        double d15 = 0.0D;
        double d16 = 0.0D;
        double d18 = 0.0D;
        for (int i5 = 0; i5 < i; i5++) {
            double d20 = (d + (double) i5) * d3 + xCoord;
            int k5 = (int) d20;
            if (d20 < (double) k5) {
                k5--;
            }
            int i6 = k5 & 0xff;
            d20 -= k5;
            double d22 = d20 * d20 * d20 * (d20 * (d20 * 6D - 15D) + 10D);
            for (int j6 = 0; j6 < k; j6++) {
                double d24 = (d2 + (double) j6) * d5 + zCoord;
                int k6 = (int) d24;
                if (d24 < (double) k6) {
                    k6--;
                }
                int l6 = k6 & 0xff;
                d24 -= k6;
                double d25 = d24 * d24 * d24 * (d24 * (d24 * 6D - 15D) + 10D);
                for (int i7 = 0; i7 < j; i7++) {
                    double d26 = (d1 + (double) i7) * d4 + yCoord;
                    int j7 = (int) d26;
                    if (d26 < (double) j7) {
                        j7--;
                    }
                    int k7 = j7 & 0xff;
                    d26 -= j7;
                    double d27 = d26 * d26 * d26 * (d26 * (d26 * 6D - 15D) + 10D);
                    if (i7 == 0 || k7 != i2) {
                        i2 = k7;
                        int j2 = permutations[i6] + k7;
                        int k2 = permutations[j2] + l6;
                        int l2 = permutations[j2 + 1] + l6;
                        int i3 = permutations[i6 + 1] + k7;
                        int k3 = permutations[i3] + l6;
                        int l3 = permutations[i3 + 1] + l6;
                        d13 = lerp(d22, grad(permutations[k2], d20, d26, d24),
                                grad(permutations[k3], d20 - 1.0D, d26, d24));
                        d15 = lerp(d22, grad(permutations[l2], d20, d26 - 1.0D, d24),
                                grad(permutations[l3], d20 - 1.0D, d26 - 1.0D, d24));
                        d16 = lerp(d22, grad(permutations[k2 + 1], d20, d26, d24 - 1.0D),
                                grad(permutations[k3 + 1], d20 - 1.0D, d26, d24 - 1.0D));
                        d18 = lerp(d22, grad(permutations[l2 + 1], d20, d26 - 1.0D, d24 - 1.0D),
                                grad(permutations[l3 + 1], d20 - 1.0D, d26 - 1.0D, d24 - 1.0D));
                    }
                    double d28 = lerp(d27, d13, d15);
                    double d29 = lerp(d27, d16, d18);
                    double d30 = lerp(d25, d28, d29);
                    ad[i1++] += d30 * d7;
                }

            }
        }
    }
}
