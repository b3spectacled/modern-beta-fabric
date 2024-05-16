package mod.bespectacled.modernbeta.util.noise;

import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class PerlinOctaveNoise {
    private final PerlinNoise noises[];
    private final int octaves;
    
    public PerlinOctaveNoise(Random random, int octaves, boolean useOffset) {
        this.noises = new PerlinNoise[octaves];
        this.octaves = octaves;
        
        for (int i = 0; i < octaves; i++) {
            this.noises[i] = new PerlinNoise(random, useOffset);
        }
    }

    /*
     * Release 3D array noise sampler.
     */
    public double[] sampleRelease(
        double x,
        double y,
        double z,
        int sizeX,
        int sizeY,
        int sizeZ,
        double scaleX,
        double scaleY,
        double scaleZ
    ) {
        double[] noise = new double[sizeX * sizeY * sizeZ];
        double frequency = 1.0;

        for (int i = 0; i < octaves; i++) {
            double offX = x * frequency * scaleX;
            double offZ = z * frequency * scaleZ;
            long offXCoord = MathHelper.lfloor(offX);
            long offZCoord = MathHelper.lfloor(offZ);
            offX -= offXCoord;
            offZ -= offZCoord;
            offXCoord %= 16777216L;
            offZCoord %= 16777216L;
            offX += offXCoord;
            offZ += offZCoord;
            offX /= frequency * scaleX;
            offZ /= frequency * scaleX;

            this.noises[i].sampleBeta(
                noise,
                offX,
                y,
                offZ,
                sizeX,
                sizeY,
                sizeZ,
                scaleX * frequency,
                scaleY * frequency,
                scaleZ * frequency,
                frequency
            );

            frequency /= 2.0;
        }

        return noise;
    }

    /*
     * Beta 3D array noise sampler.
     */
    public double[] sampleBeta(
        double x,
        double y,
        double z, 
        int sizeX,
        int sizeY,
        int sizeZ, 
        double scaleX,
        double scaleY,
        double scaleZ
    ) {
        double[] noise = new double[sizeX * sizeY * sizeZ];
        double frequency = 1.0;
        
        for (int i = 0; i < octaves; i++) {
            this.noises[i].sampleBeta(
                noise, 
                x,
                y,
                z, 
                sizeX,
                sizeY,
                sizeZ,
                scaleX * frequency,
                scaleY * frequency,
                scaleZ * frequency,
                frequency
            );
            
            frequency /= 2.0;
        }
        
        return noise;
    }
    
    /*
     * Alpha 3D array noise sampler.
     */
    public double[] sampleAlpha(
        double x,
        double y,
        double z, 
        int sizeX,
        int sizeY,
        int sizeZ, 
        double scaleX,
        double scaleY,
        double scaleZ
    ) {
        double[] noise = new double[sizeX * sizeY * sizeZ];
        double frequency = 1.0;
        
        for (int i = 0; i < octaves; i++) {
            this.noises[i].sampleAlpha(
                noise, 
                x,
                y,
                z, 
                sizeX,
                sizeY,
                sizeZ,
                scaleX * frequency,
                scaleY * frequency,
                scaleZ * frequency,
                frequency
            );
            
            frequency /= 2.0;
        }

        return noise;
    }
    
    /*
     * Standard 2D Perlin noise sampler.
     */
    public final double sampleXY(double x, double y) {
        double total = 0.0;
        double frequency = 1.0;
        
        for (int i = 0; i < this.octaves; ++i) {
            total += this.noises[i].sample(x / frequency, y / frequency) * frequency;
            frequency *= 2.0;
        }
        
        return total;
    }
    
    /*
     * Standard 3D Perlin noise sampler.
     */
    public final double sample(double x, double y, double z) {
        double total = 0.0;
        double frequency = 1.0;
        
        for (int i = 0; i < this.octaves; ++i) {
            total += this.noises[i].sample(x / frequency, y / frequency, z / frequency) * frequency;
            frequency *= 2.0;
        }
        
        return total;
    }

    /*
     * Release 2D noise sampler. This noise sampler does not overflow.
     */
    public final double sampleXZWrapped(double x, double z, double scaleX, double scaleZ) {
        double total = 0.0;
        double frequency = 1.0;

        for (int i = 0; i < this.octaves; ++i) {
            double offX = x * frequency * scaleX;
            double offZ = z * frequency * scaleZ;
            long offXCoord = MathHelper.lfloor(offX);
            long offZCoord = MathHelper.lfloor(offZ);
            offX -= offXCoord;
            offZ -= offZCoord;
            offXCoord %= 16777216L;
            offZCoord %= 16777216L;
            offX += offXCoord;
            offZ += offZCoord;

            total += this.noises[i].sampleXZ(
                offX,
                offZ,
                frequency
            );
            frequency /= 2.0;
        }

        return total;
    }
    
    /*
     * Beta 2D noise sampler.
     * This functions like sample(x, 0.0, z), except yOrigin is ignored.
     */
    public final double sampleXZ(double x, double z, double scaleX, double scaleZ) {
        double total = 0.0;
        double frequency = 1.0;
        
        for (int i = 0; i < this.octaves; ++i) {
            total += this.noises[i].sampleXZ(
                x * scaleX * frequency, 
                z * scaleZ * frequency, 
                frequency
            );
            frequency /= 2.0;
        }
        
        return total;
    }

    /*
     * Release 3D noise sampler. This noise sampler does not overflow horizontally.
     */
    public final double sampleWrapped(double x, double y, double z, double scaleX, double scaleY, double scaleZ) {
        double total = 0.0;
        double frequency = 1.0;

        for (int i = 0; i < this.octaves; ++i) {
            double offX = x * frequency * scaleX;
            double offZ = z * frequency * scaleZ;
            long offXCoord = MathHelper.lfloor(offX);
            long offZCoord = MathHelper.lfloor(offZ);
            offX -= offXCoord;
            offZ -= offZCoord;
            offXCoord %= 16777216L;
            offZCoord %= 16777216L;
            offX += offXCoord;
            offZ += offZCoord;

            total += this.noises[i].sampleXYZ(
                offX,
                y * scaleY * frequency,
                offZ,
                scaleY * frequency,
                y * scaleY * frequency
            ) / frequency;

            frequency /= 2.0;
        }

        return total;
    }

    /*
     * Alpha/Beta 3D noise sampler.
     */
    public final double sample(double x, double y, double z, double scaleX, double scaleY, double scaleZ) {
        double total = 0.0;
        double frequency = 1.0;
        
        for (int i = 0; i < this.octaves; ++i) {
            total += this.noises[i].sampleXYZ(
                x * scaleX * frequency, 
                y * scaleY * frequency, 
                z * scaleZ * frequency, 
                scaleY * frequency, 
                y * scaleY * frequency
            ) / frequency;
            
            frequency /= 2.0;
        }

        return total;
    }
}