package com.bespectacled.modernbeta.world.gen.sampler;

import com.bespectacled.modernbeta.mixin.MixinVeinTypeAccessor;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.gen.NoiseColumnSampler;
import net.minecraft.world.gen.NoiseHelper;
import net.minecraft.world.gen.random.AbstractRandom;
import net.minecraft.world.gen.random.BlockPosRandomDeriver;

public class OreVeinSampler {
    private final DoublePerlinNoiseSampler oreFrequencyNoiseSampler;
    private final DoublePerlinNoiseSampler firstOrePlacementNoiseSampler;
    private final DoublePerlinNoiseSampler secondOrePlacementNoiseSampler;
    private final DoublePerlinNoiseSampler oreChanceNoiseSampler;
    
    private final BlockPosRandomDeriver orePosRandomDeriver;
    
    private final int horizontalNoiseResolution;
    private final int verticalNoiseResolution;
    
    public OreVeinSampler(AbstractRandom random, int horizontalNoiseResolution, int verticalNoiseResolution) {
        this.oreFrequencyNoiseSampler = DoublePerlinNoiseSampler.create(random.derive(), -8, 1.0);
        this.firstOrePlacementNoiseSampler = DoublePerlinNoiseSampler.create(random.derive(), -7, 1.0);
        this.secondOrePlacementNoiseSampler = DoublePerlinNoiseSampler.create(random.derive(), -7, 1.0);
        this.oreChanceNoiseSampler = DoublePerlinNoiseSampler.create(random.derive(), -5, 1.0);
        this.orePosRandomDeriver = random.createBlockPosRandomDeriver();
        
        this.horizontalNoiseResolution = horizontalNoiseResolution;
        this.verticalNoiseResolution = verticalNoiseResolution;
    }
    
    public void sampleOreFrequencyNoise(double[] buffer, int x, int z, int minY, int noiseSizeY) {
        this.sample(buffer, x, z, this.oreFrequencyNoiseSampler, 1.5, minY, noiseSizeY);
    }

    public void sampleFirstOrePlacementNoise(double[] buffer, int x, int z, int minY, int noiseSizeY) {
        this.sample(buffer, x, z, this.firstOrePlacementNoiseSampler, 4.0, minY, noiseSizeY);
    }

    public void sampleSecondOrePlacementNoise(double[] buffer, int x, int z, int minY, int noiseSizeY) {
        this.sample(buffer, x, z, this.secondOrePlacementNoiseSampler, 4.0, minY, noiseSizeY);
    }

    public void sample(double[] buffer, int x, int z, DoublePerlinNoiseSampler sampler, double scale, int minY, int noiseSizeY) {
        for (int y = 0; y < noiseSizeY; ++y) {
            double noise;
            int actualY = y + minY;
            
            int noiseX = x * this.horizontalNoiseResolution;
            int noiseY = actualY * this.verticalNoiseResolution;
            int noiseZ = z * this.horizontalNoiseResolution;
            
            noise = NoiseHelper.lerpFromProgress(
                sampler, 
                (double)noiseX * scale, 
                (double)noiseY * scale, 
                (double)noiseZ * scale, 
                -1.0, 1.0
            );
            
            buffer[y] = noise;
        }
    }
    
    @SuppressWarnings("unused")
    public BlockState sample(int x, int y, int z, double oreFrequencyNoise, double firstOrePlacementNoise, double secondOrePlacementNoise) {
        AbstractRandom random = this.orePosRandomDeriver.createRandom(x, y, z);
        
        NoiseColumnSampler.VeinType veinType = this.getVeinType(oreFrequencyNoise, y);
        
        if (veinType == null) 
            return null;
        
        BlockState oreBlock = ((MixinVeinTypeAccessor)(Object)veinType).getOre();
        BlockState rawBlock = ((MixinVeinTypeAccessor)(Object)veinType).getRawBlock();
        BlockState stoneBlock = ((MixinVeinTypeAccessor)(Object)veinType).getStone();
        
        if (random.nextFloat() > 0.7f)
            return null;
        
        if (this.shouldPlaceOreBlock(firstOrePlacementNoise, secondOrePlacementNoise)) {
            double oreVeinSelector = MathHelper.clampedLerpFromProgress(Math.abs(oreFrequencyNoise), 0.5, 0.6f, 0.1f, 0.3f);
            
            if (random.nextFloat() < oreVeinSelector && this.oreChanceNoiseSampler.sample(x, y, z) > -0.3) {
                return random.nextFloat() < 0.02f ? rawBlock : oreBlock;
            }
            
            return stoneBlock;
        }
        
        return null;
        
    }
    
    private NoiseColumnSampler.VeinType getVeinType(double oreFrequencyNoise, int y) {
        NoiseColumnSampler.VeinType veinType = oreFrequencyNoise > 0.0 ? NoiseColumnSampler.VeinType.COPPER : NoiseColumnSampler.VeinType.IRON;
        
        int oreMinY = ((MixinVeinTypeAccessor)(Object)veinType).getMinY();
        int oreMaxY = ((MixinVeinTypeAccessor)(Object)veinType).getMaxY();
        
        int upperY = oreMaxY - y;
        int lowerY = y - oreMinY;
        
        if (lowerY < 0 || upperY < 0) {
            return null;
        }
        
        int minY = Math.min(upperY, lowerY);
        double lerpedY = MathHelper.clampedLerpFromProgress(minY, 0.0, 20.0, -0.2, 0.0);
        
        if (Math.abs(oreFrequencyNoise) + lerpedY < 0.5) {
            return null;
        }
        
        return veinType;
    }
    
    private boolean shouldPlaceOreBlock(double firstOrePlacementNoise, double secondOrePlacementNoise) {
        double ridgedSecondNoise = Math.abs(1.0 * secondOrePlacementNoise) - 0.08;
        double ridgedFirstNoise = Math.abs(1.0 * firstOrePlacementNoise) - 0.08;
        
        return Math.max(ridgedFirstNoise, ridgedSecondNoise) < 0.0;
    }
}
