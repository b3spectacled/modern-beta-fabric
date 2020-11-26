package com.bespectacled.modernbeta.noise;

import java.util.Random;

import net.minecraft.util.math.MathHelper;

/*
 * Used for additional info: https://adrianb.io/2014/08/09/perlinnoise.html
 */
public class PerlinNoise extends Noise {

    private int permutations[]; 
    
    public double xOffset;
    public double yOffset;
    public double zOffset;

    public PerlinNoise() {
        this(new Random(), false); 
    }

    public PerlinNoise(Random random, boolean isIndev) {

        // Generate permutation array
        permutations = new int[512];

        if (!isIndev) {
            xOffset = random.nextDouble() * 256D;
            yOffset = random.nextDouble() * 256D;
            zOffset = random.nextDouble() * 256D; 
        } else {
            xOffset = yOffset = zOffset = 0;
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
    
    private static double fade(double t) {
        return t * t * t * (t * (t * 6.0 - 15.0) + 10.0);
    }
    
    public double samplePerlin(double x, double y) {
        return this.samplePerlin(x, y, 0.0);
    }

    public double samplePerlin(double x, double y, double z) {
        x += this.xOffset;
        y += this.yOffset;
        z += this.zOffset;
        
        int floorX = MathHelper.floor(x);
        int floorY = MathHelper.floor(y);
        int floorZ = MathHelper.floor(z);
        
        // Find unit cube that contains point.
        int X = floorX & 0xFF;
        int Y = floorY & 0xFF;
        int Z = floorZ & 0xFF;
        
        // Find relative x, y, z of point in cube.
        x -= floorX;
        y -= floorY;
        z -= floorZ;
        
        // Compute fade curves for x, y, z.
        double u = fade(x);
        double v = fade(y);
        double w = fade(z);
        
        // Hash coordinates of the 8 cube corners.
        int A = this.permutations[X] + Y;
        int AA = this.permutations[A] + Z;
        int AB = this.permutations[A + 1] + Z;
        int B = this.permutations[X + 1] + Y;
        int BA = this.permutations[B] + Z;
        int BB = this.permutations[B + 1] + Z;
        
        return lerp(
            w, 
            lerp(
                v, 
                lerp(
                    u, 
                    grad(this.permutations[AA], x, y, z), 
                    grad(this.permutations[BA], x - 1.0, y, z)), 
                lerp(
                    u, 
                    grad(this.permutations[AB], x, y - 1.0, z), 
                    grad(this.permutations[BB], x - 1.0, y - 1.0, z))), 
            lerp(
                v, 
                lerp(
                    u, 
                    grad(this.permutations[AA + 1], x, y, z - 1.0), 
                    grad(this.permutations[BA + 1], x - 1.0, y, z - 1.0)), 
                lerp(
                    u, 
                    grad(this.permutations[AB + 1], x, y - 1.0, z - 1.0), 
                    grad(this.permutations[BB + 1], x - 1.0, y - 1.0, z - 1.0)))
        );
    }
    
    public void samplePerlinArr(
        double arr[], 
        double x, double y, double z, 
        int sizeX, int sizeY, int sizeZ, 
        double scaleX, double scaleY, double scaleZ, 
        double frequency
    ) {
        int ndx = 0;
        frequency = 1.0D / frequency;
        int flagY = -1;
        
        double lerp0 = 0.0D;
        double lerp1 = 0.0D;
        double lerp2 = 0.0D;
        double lerp3 = 0.0D;
        
        // Iterate over a collection of noise points
        for (int sX = 0; sX < sizeX; sX++) {
            
            double curX = (x + (double)sX) * scaleX + xOffset;
            int floorX = MathHelper.floor(curX);
            
            int X = floorX & 0xFF;
            curX -= floorX;
            
            double u = fade(curX);
            
            for (int sZ = 0; sZ < sizeZ; sZ++) {
                
                double curZ = (z + (double)sZ) * scaleZ + zOffset;
                int floorZ = MathHelper.floor(curZ);
                
                int Z = floorZ & 0xFF;
                curZ -= floorZ;
                
                double w = fade(curZ);
                
                for (int sY = 0; sY < sizeY; sY++) {
                    
                    double curY = (y + (double) sY) * scaleY + yOffset;
                    int floorY = MathHelper.floor(curY);
                    
                    int Y = floorY & 0xFF;
                    curY -= floorY;
                    
                    double v = fade(curY);
                    
                    
                    if (sY == 0 || Y != flagY) {
                        flagY = Y;
                        
                        int A = permutations[X] + Y;
                        int AA = permutations[A] + Z;
                        int AB = permutations[A + 1] + Z;
                        int B = permutations[X + 1] + Y;
                        int BA = permutations[B] + Z;
                        int BB = permutations[B + 1] + Z;
                        
                        lerp0 = lerp(u, grad(permutations[AA], curX, curY, curZ),
                                grad(permutations[BA], curX - 1.0D, curY, curZ));
                        
                        lerp1 = lerp(u, grad(permutations[AB], curX, curY - 1.0D, curZ),
                                grad(permutations[BB], curX - 1.0D, curY - 1.0D, curZ));
                        
                        lerp2 = lerp(u, grad(permutations[AA + 1], curX, curY, curZ - 1.0D),
                                grad(permutations[BA + 1], curX - 1.0D, curY, curZ - 1.0D));
                        
                        lerp3 = lerp(u, grad(permutations[AB + 1], curX, curY - 1.0D, curZ - 1.0D),
                                grad(permutations[BB + 1], curX - 1.0D, curY - 1.0D, curZ - 1.0D));

                    }
                    
                    double res = lerp(w, lerp(v, lerp0, lerp1), lerp(v, lerp2, lerp3));
                    
                    arr[ndx++] += res * frequency;
                }
            }
        }
    }

    public void samplePerlinArrBeta(
        double arr[], 
        double x, double y, double z, 
        int sizeX, int sizeY, int sizeZ, 
        double scaleX, double scaleY, double scaleZ, 
        double frequency
    ) {
        if (sizeY != 1) {
            this.samplePerlinArr(arr, x, y, z, sizeX, sizeY, sizeZ, scaleX, scaleY, scaleZ, frequency);
            
            return;
        }
        
        int ndx = 0;
        
        frequency = 1.0D / frequency;
        
        for (int sX = 0; sX < sizeX; sX++) {
            double curX = (x + (double)sX) * scaleX + xOffset;
            int floorX = MathHelper.floor(curX);
            
            int X = floorX & 0xFF;
            curX -= floorX;
            
            double u = fade(curX);
            
            for (int sZ = 0; sZ < sizeZ; sZ++) {
                double curZ = (z + (double) sZ) * scaleZ + zOffset;
                int floorZ = MathHelper.floor(curZ);
                
                int Z = floorZ & 0xFF;
                curZ -= floorZ;
                
                double w = fade(curZ);
                
                int A = permutations[X] + 0;
                int AA = permutations[A] + Z;
                int B = permutations[X + 1] + 0;
                int BA = permutations[B] + Z;
                
                double lerp0 = lerp(
                    u, 
                    grad(permutations[AA], curX, curZ),
                    grad(permutations[BA], curX - 1.0D, 0.0D, curZ));
                double lerp1 = lerp(
                    u, 
                    grad(permutations[AA + 1], curX, 0.0D, curZ - 1.0D),
                    grad(permutations[BA + 1], curX - 1.0D, 0.0D, curZ - 1.0D));
                
                double res = lerp(w, lerp0, lerp1);
                
                arr[ndx++] += res * frequency;
            }
        }
    }
    
}
