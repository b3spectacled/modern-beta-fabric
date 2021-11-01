package com.bespectacled.modernbeta.world.gen.sampler;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.mixin.MixinVeinTypeAccessor;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.NoiseColumnSampler;
import net.minecraft.world.gen.NoiseHelper;
import net.minecraft.world.gen.noise.NoiseParametersKeys;
import net.minecraft.world.gen.random.AbstractRandom;
import net.minecraft.world.gen.random.RandomDeriver;

public class OreVeinSampler {
    private final DoublePerlinNoiseSampler oreVeininessNoiseSampler;
    private final DoublePerlinNoiseSampler oreVeinFirstNoiseSampler;
    private final DoublePerlinNoiseSampler oreVeinSecondNoiseSampler;
    private final DoublePerlinNoiseSampler oreGapNoiseSampler;
    
    private final RandomDeriver orePosRandomDeriver;
    
    private final int horizontalNoiseResolution;
    private final int verticalNoiseResolution;
    
    public OreVeinSampler(Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry, RandomDeriver randomDeriver, int horizontalNoiseResolution, int verticalNoiseResolution) {
        this.oreVeininessNoiseSampler = NoiseParametersKeys.method_39173(noiseRegistry, randomDeriver, NoiseParametersKeys.ORE_VEININESS);
        this.oreVeinFirstNoiseSampler = NoiseParametersKeys.method_39173(noiseRegistry, randomDeriver, NoiseParametersKeys.ORE_VEIN_A);
        this.oreVeinSecondNoiseSampler = NoiseParametersKeys.method_39173(noiseRegistry, randomDeriver, NoiseParametersKeys.ORE_VEIN_B);
        this.oreGapNoiseSampler = NoiseParametersKeys.method_39173(noiseRegistry, randomDeriver, NoiseParametersKeys.ORE_GAP);
        
        this.orePosRandomDeriver = randomDeriver.createRandom(ModernBeta.createId("ore")).createBlockPosRandomDeriver();
        
        this.horizontalNoiseResolution = horizontalNoiseResolution;
        this.verticalNoiseResolution = verticalNoiseResolution;
    }
    
    public void sampleOreFrequencyNoise(double[] buffer, int x, int z, int minY, int noiseSizeY) {
        this.sample(buffer, x, z, this.oreVeininessNoiseSampler, 1.5, minY, noiseSizeY);
    }

    public void sampleFirstOrePlacementNoise(double[] buffer, int x, int z, int minY, int noiseSizeY) {
        this.sample(buffer, x, z, this.oreVeinFirstNoiseSampler, 4.0, minY, noiseSizeY);
    }

    public void sampleSecondOrePlacementNoise(double[] buffer, int x, int z, int minY, int noiseSizeY) {
        this.sample(buffer, x, z, this.oreVeinSecondNoiseSampler, 4.0, minY, noiseSizeY);
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
            double oreVeinSelector = MathHelper.clampedLerpFromProgress(Math.abs(oreFrequencyNoise), 0.4f, 0.6f, 0.1f, 0.3f);
            
            if (random.nextFloat() < oreVeinSelector && this.oreGapNoiseSampler.sample(x, y, z) > -0.3) {
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
