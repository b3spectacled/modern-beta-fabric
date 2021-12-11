package com.bespectacled.modernbeta.util.noise;

import java.util.Random;

import net.minecraft.util.math.MathHelper;

/*
 * Used for additional info: https://adrianb.io/2014/08/09/perlinnoise.html
 */
public class PerlinNoise {
    private int permutations[]; 
    
    public double xOffset;
    public double yOffset;
    public double zOffset;

    public PerlinNoise() {
        this(new Random(), false); 
    }

    public PerlinNoise(Random random, boolean useOffset) {
        // Generate permutation array
        this.permutations = new int[512];

        this.xOffset = this.yOffset = this.zOffset = 0;
        
        if (useOffset) {
            this.xOffset = random.nextDouble() * 256D;
            this.yOffset = random.nextDouble() * 256D;
            this.zOffset = random.nextDouble() * 256D; 
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

    public double sampleXY(double x, double y) {
        return this.sample(x, y, 0.0);
    }

    public double sample(double x, double y, double z) {
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
    
    public double sampleXZ(double x, double z, double frequency) {
        frequency = 1.0D / frequency;
        
        x = x + this.xOffset;
        z = z + this.zOffset;
        
        int floorX = MathHelper.floor(x);
        int floorZ = MathHelper.floor(z);
        
        // Find unit cube that contains point.
        int X = floorX & 0xFF;
        int Z = floorZ & 0xFF;
        
        // Find relative x, y, z of point in cube.
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
            //grad(permutations[AA], curX, curZ), // Below should give same result but faster
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
     * Point-based implementation of Beta Perlin sampler,
     * accounting for Y unit cube idiosyncrasy.
     * 
     * When using in cases where y is fixed (i.e. gravel beach sampling), use sampleXZ instead.
     * 
     */
    public double sample(double x, double z, double startY, double localY, double scaleX, double scaleY, double scaleZ, double frequency) {
        double lerp0;
        double lerp1;
        double lerp2;
        double lerp3;
        
        double y = startY + localY;
        
        double scaledX = x * scaleX / frequency;
        double scaledY = y * scaleY / frequency;
        double scaledZ = z * scaleZ / frequency;
        
        double curX = scaledX + this.xOffset;
        double curY = scaledY + this.yOffset;
        double curZ = scaledZ + this.zOffset;
        
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
        
        // Account for Beta/Alpha implementation;
        // calculate y at the *start* of current unit cube
        double actualY = this.calculateActualY(startY, localY, scaleY, Y, frequency);
        
        // Hash coordinates of the 8 cube corners.
        int A =  this.permutations[X] + Y;
        int AA = this.permutations[A] + Z;
        int AB = this.permutations[A + 1] + Z;
        int B =  this.permutations[X + 1] + Y;
        int BA = this.permutations[B] + Z;
        int BB = this.permutations[B + 1] + Z;
        
        lerp0 = lerp(
            u,
            grad(this.permutations[AA], curX, actualY, curZ),
            grad(this.permutations[BA], curX - 1.0D, actualY, curZ)
        );
        
        lerp1 = lerp(
            u,
            grad(this.permutations[AB], curX, actualY - 1.0D, curZ),
            grad(this.permutations[BB], curX - 1.0D, actualY - 1.0D, curZ)
        );
        
        lerp2 = lerp(
            u,
            grad(this.permutations[AA + 1], curX, actualY, curZ - 1.0D),
            grad(this.permutations[BA + 1], curX - 1.0D, actualY, curZ - 1.0D)
        );
        
        lerp3 = lerp(
            u,
            grad(this.permutations[AB + 1], curX, actualY - 1.0D, curZ - 1.0D),
            grad(this.permutations[BB + 1], curX - 1.0D, actualY - 1.0D, curZ - 1.0D)
        );
        
        return lerp(w, lerp(v, lerp0, lerp1), lerp(v, lerp2, lerp3));
    }
    
    private double calculateActualY(double startY, double localY, double scaleY, int unitY, double frequency) {
        double scaledY;
        int floorY;
        
        while (localY > 0) {
            scaledY = (startY + localY) * scaleY / frequency;
            scaledY += this.yOffset;
            
            floorY = MathHelper.floor(scaledY);
            int Y = floorY & 0xFF;
            
            if (Y != unitY) {
                localY++;
                break;
            }
            
            localY--;
        }
        
        scaledY = (startY + localY) * scaleY / frequency;
        scaledY += this.yOffset;
        
        floorY = MathHelper.floor(scaledY);
        
        return scaledY - floorY;
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
