package com.bespectacled.modernbeta.util.noise;

import java.util.Random;

import net.minecraft.util.math.MathHelper;

/*
 * Used for additional info: https://adrianb.io/2014/08/09/perlinnoise.html
 */
public class PerlinNoise {
    private int permutations[]; 
    
    public double offsetX;
    public double offsetY;
    public double offsetZ;

    public PerlinNoise() {
        this(new Random(), false); 
    }

    public PerlinNoise(Random random, boolean useOffset) {
        // Generate permutation array
        this.permutations = new int[512];

        this.offsetX = this.offsetY = this.offsetZ = 0;
        
        if (useOffset) {
            this.offsetX = random.nextDouble() * 256D;
            this.offsetY = random.nextDouble() * 256D;
            this.offsetZ = random.nextDouble() * 256D; 
        } 
        
        for (int i = 0; i < 256; i++) {
            this.permutations[i] = i;
        }

        for (int i = 0; i < 256; i++) {
            int j = random.nextInt(256 - i) + i;
            int k = this.permutations[i];

            this.permutations[i] = this.permutations[j];
            this.permutations[j] = k;

            // Repeat first 256 values to avoid buffer overflow
            this.permutations[i + 256] = this.permutations[i];
        }
    }

    public double sample(double x, double y) {
        return this.sample(x, y, 0.0);
    }

    public double sample(double x, double y, double z) {
        x += this.offsetX;
        y += this.offsetY;
        z += this.offsetZ;
        
        int floorX = MathHelper.floor(x);
        int floorY = MathHelper.floor(y);
        int floorZ = MathHelper.floor(z);
        
        // Find unit cube that contains point.
        int X = floorX & 0xFF;
        int Y = floorY & 0xFF;
        int Z = floorZ & 0xFF;
        
        // Find local x, y, z of point in cube.
        x -= floorX;
        y -= floorY;
        z -= floorZ;
        
        // Compute fade curves for x, y, z.
        double u = fade(x);
        double v = fade(y);
        double w = fade(z);
        
        // Hash coordinates of the 8 cube corners.
        int A =  this.permutations[X] + Y;
        int AA = this.permutations[A] + Z;
        int AB = this.permutations[A + 1] + Z;
        int B =  this.permutations[X + 1] + Y;
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
   
    public void sampleAlpha(
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
            for (int sZ = 0; sZ < sizeZ; sZ++) {
                for (int sY = 0; sY < sizeY; sY++) {
                    double curX = (x + (double)sX) * scaleX + this.offsetX;
                    double curY = (y + (double)sY) * scaleY + this.offsetY;
                    double curZ = (z + (double)sZ) * scaleZ + this.offsetZ;

                    int floorX = MathHelper.floor(curX);
                    int floorY = MathHelper.floor(curY);
                    int floorZ = MathHelper.floor(curZ);
                    
                    // Find unit cube that contains point.
                    int X = floorX & 0xFF;
                    int Y = floorY & 0xFF;
                    int Z = floorZ & 0xFF;
                    
                    // Find local x, y, z of point in cube.
                    curX -= floorX;
                    curY -= floorY;
                    curZ -= floorZ;
                    
                    // Compute fade curves for x, y, z.
                    double u = fade(curX);
                    double v = fade(curY);
                    double w = fade(curZ);
                    
                    if (sY == 0 || Y != flagY) {
                        flagY = Y;
                        
                        int A =  this.permutations[X] + Y;
                        int AA = this.permutations[A] + Z;
                        int AB = this.permutations[A + 1] + Z;
                        int B =  this.permutations[X + 1] + Y;
                        int BA = this.permutations[B] + Z;
                        int BB = this.permutations[B + 1] + Z;
                        
                        lerp0 = lerp(
                            u,
                            grad(this.permutations[AA], curX, curY, curZ),
                            grad(this.permutations[BA], curX - 1.0D, curY, curZ)
                        );
                        
                        lerp1 = lerp(
                            u,
                            grad(this.permutations[AB], curX, curY - 1.0D, curZ),
                            grad(this.permutations[BB], curX - 1.0D, curY - 1.0D, curZ)
                        );
                        
                        lerp2 = lerp(
                            u,
                            grad(this.permutations[AA + 1], curX, curY, curZ - 1.0D),
                            grad(this.permutations[BA + 1], curX - 1.0D, curY, curZ - 1.0D)
                        );
                        
                        lerp3 = lerp(
                            u,
                            grad(this.permutations[AB + 1], curX, curY - 1.0D, curZ - 1.0D),
                            grad(this.permutations[BB + 1], curX - 1.0D, curY - 1.0D, curZ - 1.0D)
                        );
                    }
                    
                    double res = lerp(w, lerp(v, lerp0, lerp1), lerp(v, lerp2, lerp3));
                    
                    arr[ndx++] += res * frequency;
                }
            }
        }
    }

    public void sampleBeta(
        double arr[], 
        double x, double y, double z, 
        int sizeX, int sizeY, int sizeZ, 
        double scaleX, double scaleY, double scaleZ, 
        double frequency
    ) {
        if (sizeY != 1) {
            this.sampleAlpha(arr, x, y, z, sizeX, sizeY, sizeZ, scaleX, scaleY, scaleZ, frequency);
        } else {
            int ndx = 0;
            for (int sX = 0; sX < sizeX; sX++) {
                for (int sZ = 0; sZ < sizeZ; sZ++) {
                    double curX = (x + (double)sX) * scaleX;
                    double curZ = (z + (double)sZ) * scaleZ;
                    
                    arr[ndx++] += this.sampleXZ(curX, curZ, frequency);
                }
            }
        }
    }
    
    public double sampleXZ(double x, double z, double frequency) {
        frequency = 1.0D / frequency;
        
        x = x + this.offsetX;
        z = z + this.offsetZ;
        
        int floorX = MathHelper.floor(x);
        int floorZ = MathHelper.floor(z);
        
        // Find unit cube that contains point.
        int X = floorX & 0xFF;
        int Z = floorZ & 0xFF;
        
        // Find local x, y, z of point in cube.
        x -= floorX;
        z -= floorZ;
        
        // Compute fade curves for x, y, z.
        double u = fade(x);
        double w = fade(z);
        
        int A = this.permutations[X] + 0;
        int AA = this.permutations[A] + Z;
        int B = this.permutations[X + 1] + 0;
        int BA = this.permutations[B] + Z;
        
        double lerp0 = lerp(
            u,
            grad(this.permutations[AA], x, 0.0D, z),
            grad(this.permutations[BA], x - 1.0D, 0.0D, z));
        double lerp1 = lerp(
            u, 
            grad(this.permutations[AA + 1], x, 0.0D, z - 1.0D),
            grad(this.permutations[BA + 1], x - 1.0D, 0.0D, z - 1.0D));
        
        double res = lerp(w, lerp0, lerp1);
        
        return res * frequency;
    }
    
    /*
     * From vanilla PerlinNoiseSampler.
     */
    public double sampleXYZ(double x, double y, double z, double yScale, double yMax) {
        x += this.offsetX;
        y += this.offsetY;
        z += this.offsetZ;
        
        int floorX = MathHelper.floor(x);
        int floorY = MathHelper.floor(y);
        int floorZ = MathHelper.floor(z);
        
        x -= floorX;
        y -= floorY;
        z -= floorZ;
        
        double yOffset = 0.0;
        if (yScale != 0.0) {
            if (yMax >= 0.0 && yMax < y) {
                yOffset = yMax;
            } else {
                yOffset = y;
            }
            
            yOffset = MathHelper.floor(yOffset / yScale + 1.0000000116860974E-7) * yScale;
        } else {
            yOffset = 0.0;
        }
        
        return this.sampleXYZ(floorX, floorY, floorZ, x, y - yOffset, z, y);
    }
    
    /*
     * From vanilla PerlinNoiseSampler.
     */
    private double sampleXYZ(int floorX, int floorY, int floorZ, double localX, double localOffsetY, double localZ, double localY) {
        // Find unit cube that contains point.
        int X = floorX & 0xFF;
        int Y = floorY & 0xFF;
        int Z = floorZ & 0xFF;
        
        int A =  this.permutations[X] + Y;
        int AA = this.permutations[A] + Z;
        int AB = this.permutations[A + 1] + Z;
        int B =  this.permutations[X + 1] + Y;
        int BA = this.permutations[B] + Z;
        int BB = this.permutations[B + 1] + Z;
        
        // Calculate dot of hashed gradient vector against 8 location vectors.
        double grad0 = grad(this.permutations[AA], localX, localOffsetY, localZ);
        double grad1 = grad(this.permutations[BA], localX - 1.0, localOffsetY, localZ);
        
        double grad2 = grad(this.permutations[AB], localX, localOffsetY - 1.0, localZ);
        double grad3 = grad(this.permutations[BB], localX - 1.0, localOffsetY - 1.0, localZ);
        
        double grad4 = grad(this.permutations[AA + 1], localX, localOffsetY, localZ - 1.0);
        double grad5 = grad(this.permutations[BA + 1], localX - 1.0, localOffsetY, localZ - 1.0);
        
        double grad6 = grad(this.permutations[AB + 1], localX, localOffsetY - 1.0, localZ - 1.0);
        double grad7 = grad(this.permutations[BB + 1], localX - 1.0, localOffsetY - 1.0, localZ - 1.0);
        
        double u = fade(localX);
        double v = fade(localY);
        double w = fade(localZ);
        
        return MathHelper.lerp3(u, v, w, grad0, grad1, grad2, grad3, grad4, grad5, grad6, grad7);
    }

    private static double lerp(double delta, double start, double end) {
        return start + delta * (end - start);
    }

    // Using alternate function from
    // https://adrianb.io/2014/08/09/perlinnoise.html
    private static double grad(int hash, double x, double y, double z) {
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
}