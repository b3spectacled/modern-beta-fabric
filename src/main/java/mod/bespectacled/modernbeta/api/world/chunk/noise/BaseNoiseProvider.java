package mod.bespectacled.modernbeta.api.world.chunk.noise;

import net.minecraft.util.math.MathHelper;

public class BaseNoiseProvider extends NoiseProvider {
    private final BaseColumnSampler bufferSampler;
    
    protected double[] heightmapNoise;

    private double heightmapLowerNW;
    private double heightmapLowerSW;
    private double heightmapLowerNE;
    private double heightmapLowerSE;
    
    private double heightmapUpperNW; 
    private double heightmapUpperSW;
    private double heightmapUpperNE;
    private double heightmapUpperSE;
    
    private double heightmapNW;
    private double heightmapNE;
    private double heightmapSW;
    private double heightmapSE;
    
    private double heightmapN;
    private double heightmapS;
    
    private double heightmapDensity;
    
    public BaseNoiseProvider(
        int noiseSizeX, 
        int noiseSizeY, 
        int noiseSizeZ, 
        BaseColumnSampler bufferSampler
    ) {
        super(noiseSizeX, noiseSizeY, noiseSizeZ);
        
        this.bufferSampler = bufferSampler;
    }

    @Override
    protected double[] sampleNoise(int startNoiseX, int startNoiseZ) {
        double[] primaryBuffer = new double[this.noiseResY];
        double[] heightmapBuffer = new double[this.noiseResY];
        
        double[] noise = new double[this.noiseSize];
        double[] heightmapNoise = new double[this.noiseSize];
        
        int ndx = 0;
        for (int localNoiseX = 0; localNoiseX < this.noiseResX; ++localNoiseX) {
            for (int localNoiseZ = 0; localNoiseZ < this.noiseResZ; ++localNoiseZ) {
                this.bufferSampler.sampleColumn(primaryBuffer, heightmapBuffer, startNoiseX, startNoiseZ, localNoiseX, localNoiseZ);
                
                for (int nY = 0; nY < this.noiseResY; ++nY) {
                    noise[ndx] = primaryBuffer[nY];
                    heightmapNoise[ndx] = heightmapBuffer[nY];
                            
                    ndx++;
                }
            }
        }
        
        this.heightmapNoise = heightmapNoise;
        
        return noise;
    }
    
    public void sampleNoiseCornersHeightmap(int subChunkX, int subChunkY, int subChunkZ) {
        this.heightmapLowerNW = this.heightmapNoise[((subChunkX + 0) * this.noiseResX + (subChunkZ + 0)) * this.noiseResY + (subChunkY + 0)];
        this.heightmapLowerSW = this.heightmapNoise[((subChunkX + 0) * this.noiseResX + (subChunkZ + 1)) * this.noiseResY + (subChunkY + 0)];
        this.heightmapLowerNE = this.heightmapNoise[((subChunkX + 1) * this.noiseResX + (subChunkZ + 0)) * this.noiseResY + (subChunkY + 0)];
        this.heightmapLowerSE = this.heightmapNoise[((subChunkX + 1) * this.noiseResX + (subChunkZ + 1)) * this.noiseResY + (subChunkY + 0)];
        
        this.heightmapUpperNW = this.heightmapNoise[((subChunkX + 0) * this.noiseResX + (subChunkZ + 0)) * this.noiseResY + (subChunkY + 1)]; 
        this.heightmapUpperSW = this.heightmapNoise[((subChunkX + 0) * this.noiseResX + (subChunkZ + 1)) * this.noiseResY + (subChunkY + 1)];
        this.heightmapUpperNE = this.heightmapNoise[((subChunkX + 1) * this.noiseResX + (subChunkZ + 0)) * this.noiseResY + (subChunkY + 1)];
        this.heightmapUpperSE = this.heightmapNoise[((subChunkX + 1) * this.noiseResX + (subChunkZ + 1)) * this.noiseResY + (subChunkY + 1)];
    }
    
    public void sampleNoiseYHeightmap(double deltaY) {
        this.heightmapNW = MathHelper.lerp(deltaY, this.heightmapLowerNW, this.heightmapUpperNW);
        this.heightmapSW = MathHelper.lerp(deltaY, this.heightmapLowerSW, this.heightmapUpperSW);
        this.heightmapNE = MathHelper.lerp(deltaY, this.heightmapLowerNE, this.heightmapUpperNE);
        this.heightmapSE = MathHelper.lerp(deltaY, this.heightmapLowerSE, this.heightmapUpperSE);
    }
    
    public void sampleNoiseXHeightmap(double deltaX) {
        this.heightmapN = MathHelper.lerp(deltaX, this.heightmapNW, this.heightmapNE);
        this.heightmapS = MathHelper.lerp(deltaX, this.heightmapSW, this.heightmapSE);
    }
    
    public void sampleNoiseZHeightmap(double deltaZ) {
        this.heightmapDensity = MathHelper.lerp(deltaZ, this.heightmapN, this.heightmapS);
    }
    
    public double sampleHeightmap() {
        return this.heightmapDensity;
    }
    
    @FunctionalInterface
    public static interface BaseColumnSampler {
        public void sampleColumn(double[] primaryBuffer, double[] heightmapBuffer, int startNoiseX, int startNoiseZ, int localNoiseX, int localNoiseZ);
    }
}
