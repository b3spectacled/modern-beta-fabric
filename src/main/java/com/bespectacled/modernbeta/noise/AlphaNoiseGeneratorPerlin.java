package com.bespectacled.modernbeta.noise;

import java.util.Random;

public class AlphaNoiseGeneratorPerlin extends NoiseGenerator {
    private int permutations[];
    public double xCoord;
    public double yCoord;
    public double zCoord;

    public AlphaNoiseGeneratorPerlin() {
        this(new Random());
    }

    public AlphaNoiseGeneratorPerlin(Random random) {
        // Generate permutation array
        permutations = new int[512];

        xCoord = random.nextDouble() * 256D;
        yCoord = random.nextDouble() * 256D;
        zCoord = random.nextDouble() * 256D;

        for (int i = 0; i < 256; i++) {
            permutations[i] = i;
        }

        for (int j = 0; j < 256; j++) {
            int k = random.nextInt(256 - j) + j;
            int l = permutations[j];
            permutations[j] = permutations[k];
            permutations[k] = l;

            // Repeat first 256 values to avoid buffer overflow
            permutations[j + 256] = permutations[j];
        }

    }

    public double generateNoise(double d, double d1, double d2) {
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
        return lerp(d8,
                lerp(d7, lerp(d6, grad(permutations[l1], d3, d4, d5), grad(permutations[k2], d3 - 1.0D, d4, d5)),
                        lerp(d6, grad(permutations[i2], d3, d4 - 1.0D, d5),
                                grad(permutations[l2], d3 - 1.0D, d4 - 1.0D, d5))),
                lerp(d7, lerp(d6, grad(permutations[l1 + 1], d3, d4, d5 - 1.0D),
                        grad(permutations[k2 + 1], d3 - 1.0D, d4, d5 - 1.0D)),
                        lerp(d6, grad(permutations[i2 + 1], d3, d4 - 1.0D, d5 - 1.0D),
                                grad(permutations[l2 + 1], d3 - 1.0D, d4 - 1.0D, d5 - 1.0D))));
    }

    public double lerp(double d, double d1, double d2) {
        return d1 + d * (d2 - d1);
    }

    // Using alternate function from
    // https://adrianb.io/2014/08/09/perlinnoise.html
    public double grad(int i, double d, double d1, double d2) {
        int j = i & 0xf;
        double d3 = j >= 8 ? d1 : d;
        double d4 = j >= 4 ? j != 12 && j != 14 ? d2 : d : d1;
        return ((j & 1) != 0 ? -d3 : d3) + ((j & 2) != 0 ? -d4 : d4);

        /*
         * switch(hash & 0xF) { case 0x0: return x + y; case 0x1: return -x + y; case
         * 0x2: return x - y; case 0x3: return -x - y; case 0x4: return x + z; case 0x5:
         * return -x + z; case 0x6: return x - z; case 0x7: return -x - z; case 0x8:
         * return y + z; case 0x9: return -y + z; case 0xA: return y - z; case 0xB:
         * return -y - z; case 0xC: return y + x; case 0xD: return -y + z; case 0xE:
         * return y - x; case 0xF: return -y - z; default: return 0; // never happens }
         */
    }

    public double func_801_a(double d, double d1) {
        return generateNoise(d, d1, 0.0D);
    }

    public void func_805_a(double ad[], double d, double d1, double d2, int i, int j, int k, double d3, double d4,
            double d5, double d6) {
        int l = 0;
        double d7 = 1.0D / d6;
        int i1 = -1;
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        boolean flag4 = false;
        boolean flag5 = false;
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

}
