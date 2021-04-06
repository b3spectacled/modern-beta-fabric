package com.bespectacled.modernbeta.noise;

import java.util.Random;

/*
 * Reference: http://weber.itn.liu.se/~stegu/simplexnoise/simplexnoise.pdf
 * 
 * Tested output range, on 100000 * 100000 sample: -0.885539/0.885539
 */
public class SimplexNoise extends Noise {
    private static int[][] gradients;
    private int[] permutations;
    
    public double xOrigin;
    public double yOrigin;
    public double zOrigin;
    
    private static final double UNSKEW_FACTOR_2D;
    private static final double SKEW_FACTOR_2D;
    
    public SimplexNoise() {
        this(new Random());
    }
    
    public SimplexNoise(Random random) {
        this.permutations = new int[512];
        this.xOrigin = random.nextDouble() * 256.0;
        this.yOrigin = random.nextDouble() * 256.0;
        this.zOrigin = random.nextDouble() * 256.0;
        
        for (int i = 0; i < 256; ++i) {
            this.permutations[i] = i;
        }
        
        for (int i = 0; i < 256; ++i) {
            int permNdx = random.nextInt(256 - i) + i;
            int perm = this.permutations[i];
            
            this.permutations[i] = this.permutations[permNdx];
            this.permutations[permNdx] = perm;
            this.permutations[i + 256] = this.permutations[i];
        }
    }
    
    private static int fastFloor(double double1) {
        return (double1 > 0.0) ? ((int)double1) : ((int)double1 - 1);
    }
    
    private static double dot(int[] arr, double double2, double double4) {
        return arr[0] * double2 + arr[1] * double4;
    }
    
    public void sample(double[] arr, double x, double y, int sizeX, int sizeY, double scaleX, double scaleY, double amplitude) {
        int ndx = 0;
        
        for (int sX = 0; sX < sizeX; ++sX) {
            double curX = (x + sX) * scaleX + this.xOrigin;
            
            for (int sY = 0; sY < sizeY; ++sY) {
                double curY = (y + sY) * scaleY + this.yOrigin;
                
                double s = (curX + curY) * SimplexNoise.SKEW_FACTOR_2D;
                int i = fastFloor(curX + s);
                int j = fastFloor(curY + s);
                
                double t = (i + j) * SimplexNoise.UNSKEW_FACTOR_2D;
                double x0 = i - t;
                double y0 = j - t;
                double xDist = curX - x0;
                double yDist = curY - y0;
                
                int offsetI;
                int offsetJ;
                if (xDist > yDist) {
                    offsetI = 1;
                    offsetJ = 0;
                }
                else {
                    offsetI = 0;
                    offsetJ = 1;
                }
                
                double offsetMidX = xDist - offsetI + SimplexNoise.UNSKEW_FACTOR_2D;
                double offsetMidY = yDist - offsetJ + SimplexNoise.UNSKEW_FACTOR_2D;
                double offsetLastX = xDist - 1.0 + 2.0 * SimplexNoise.UNSKEW_FACTOR_2D;
                double offsetLastY = yDist - 1.0 + 2.0 * SimplexNoise.UNSKEW_FACTOR_2D;
                
                int hash0 = i & 0xFF;
                int hash1 = j & 0xFF;
                int gradNdx0 = this.permutations[hash0 + this.permutations[hash1]] % 12;
                int gradNdx1 = this.permutations[hash0 + offsetI + this.permutations[hash1 + offsetJ]] % 12;
                int gradNdx2 = this.permutations[hash0 + 1 + this.permutations[hash1 + 1]] % 12;
                
                double t0 = 0.5 - xDist * xDist - yDist * yDist;
                double contrib0;
                if (t0 < 0.0) {
                    contrib0 = 0.0;
                }
                else {
                    t0 *= t0;
                    contrib0 = t0 * t0 * dot(SimplexNoise.gradients[gradNdx0], xDist, yDist);
                }
                
                double t1 = 0.5 - offsetMidX * offsetMidX - offsetMidY * offsetMidY;
                double contrib1;
                if (t1 < 0.0) {
                    contrib1 = 0.0;
                }
                else {
                    t1 *= t1;
                    contrib1 = t1 * t1 * dot(SimplexNoise.gradients[gradNdx1], offsetMidX, offsetMidY);
                }
                
                double t2 = 0.5 - offsetLastX * offsetLastX - offsetLastY * offsetLastY;
                double contrib2;
                if (t2 < 0.0) {
                    contrib2 = 0.0;
                }
                else {
                    t2 *= t2;
                    contrib2 = t2 * t2 * dot(SimplexNoise.gradients[gradNdx2], offsetLastX, offsetLastY);
                }
                
                int curNdx = ndx++;
                arr[curNdx] += 70.0 * (contrib0 + contrib1 + contrib2) * amplitude;
            }
        }
    }
    
    public double sample(double x, double y, double scaleX, double scaleY) {
        x = x * scaleX + this.xOrigin;
        y = y * scaleY + this.yOrigin;
        
        double s = (x + y) * SimplexNoise.SKEW_FACTOR_2D;
        int i = fastFloor(x + s);
        int j = fastFloor(y + s);
        
        double t = (i + j) * SimplexNoise.UNSKEW_FACTOR_2D;
        double x0 = i - t;
        double y0 = j - t;
        double xDist = x - x0;
        double yDist = y - y0;
        
        int offsetI;
        int offsetJ;
        if (xDist > yDist) {
            offsetI = 1;
            offsetJ = 0;
        }
        else {
            offsetI = 0;
            offsetJ = 1;
        }
        
        double offsetMidX = xDist - offsetI + SimplexNoise.UNSKEW_FACTOR_2D;
        double offsetMidY = yDist - offsetJ + SimplexNoise.UNSKEW_FACTOR_2D;
        double offsetLastX = xDist - 1.0 + 2.0 * SimplexNoise.UNSKEW_FACTOR_2D;
        double offsetLastY = yDist - 1.0 + 2.0 * SimplexNoise.UNSKEW_FACTOR_2D;
        
        int hash0 = i & 0xFF;
        int hash1 = j & 0xFF;
        int gradNdx0 = this.permutations[hash0 + this.permutations[hash1]] % 12;
        int gradNdx1 = this.permutations[hash0 + offsetI + this.permutations[hash1 + offsetJ]] % 12;
        int gradNdx2 = this.permutations[hash0 + 1 + this.permutations[hash1 + 1]] % 12;
        
        double t0 = 0.5 - xDist * xDist - yDist * yDist;
        double contrib0;
        if (t0 < 0.0) {
            contrib0 = 0.0;
        }
        else {
            t0 *= t0;
            contrib0 = t0 * t0 * dot(SimplexNoise.gradients[gradNdx0], xDist, yDist);
        }
        
        double t1 = 0.5 - offsetMidX * offsetMidX - offsetMidY * offsetMidY;
        double contrib1;
        if (t1 < 0.0) {
            contrib1 = 0.0;
        }
        else {
            t1 *= t1;
            contrib1 = t1 * t1 * dot(SimplexNoise.gradients[gradNdx1], offsetMidX, offsetMidY);
        }
        
        double t2 = 0.5 - offsetLastX * offsetLastX - offsetLastY * offsetLastY;
        double contrib2;
        if (t2 < 0.0) {
            contrib2 = 0.0;
        }
        else {
            t2 *= t2;
            contrib2 = t2 * t2 * dot(SimplexNoise.gradients[gradNdx2], offsetLastX, offsetLastY);
        }
        
        return 70.0 * (contrib0 + contrib1 + contrib2);
    }
    
    
    static {
        SimplexNoise.gradients = new int[][] { { 1, 1, 0 }, { -1, 1, 0 }, { 1, -1, 0 }, { -1, -1, 0 }, { 1, 0, 1 }, { -1, 0, 1 }, { 1, 0, -1 }, { -1, 0, -1 }, { 0, 1, 1 }, { 0, -1, 1 }, { 0, 1, -1 }, { 0, -1, -1 } };
        SKEW_FACTOR_2D = 0.5 * (Math.sqrt(3.0) - 1.0);
        UNSKEW_FACTOR_2D = (3.0 - Math.sqrt(3.0)) / 6.0;
    }
}
