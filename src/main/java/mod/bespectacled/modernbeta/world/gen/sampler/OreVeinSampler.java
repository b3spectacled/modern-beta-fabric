package mod.bespectacled.modernbeta.world.gen.sampler;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.util.noise.NoiseRules;
import mod.bespectacled.modernbeta.world.gen.blocksource.SimpleBlockSource;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.noise.NoiseParametersKeys;
import net.minecraft.world.gen.random.AbstractRandom;
import net.minecraft.world.gen.random.RandomDeriver;

public class OreVeinSampler extends NoiseSampler {
    private final DoublePerlinNoiseSampler oreVeininessNoiseSampler;
    private final DoublePerlinNoiseSampler oreVeinFirstNoiseSampler;
    private final DoublePerlinNoiseSampler oreVeinSecondNoiseSampler;
    private final DoublePerlinNoiseSampler oreGapNoiseSampler;
    
    private final RandomDeriver orePosRandomDeriver;
    
    private final SimpleBlockSource deepslateSampler;
    private final NoiseRules<OreVeinType> oreVeinRules;
    
    public OreVeinSampler(
        Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry,
        RandomDeriver randomDeriver,
        int horizontalNoiseResolution,
        int verticalNoiseResolution,
        SimpleBlockSource deepslateSampler,
        NoiseRules<OreVeinType> oreVeinRules
    ) {
        super(horizontalNoiseResolution, verticalNoiseResolution);
        
        this.oreVeininessNoiseSampler = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.ORE_VEININESS);
        this.oreVeinFirstNoiseSampler = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.ORE_VEIN_A);
        this.oreVeinSecondNoiseSampler = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.ORE_VEIN_B);
        this.oreGapNoiseSampler = NoiseParametersKeys.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParametersKeys.ORE_GAP);
        
        this.orePosRandomDeriver = randomDeriver.createRandom(ModernBeta.createId("ore")).createRandomDeriver();
        
        this.deepslateSampler = deepslateSampler;
        this.oreVeinRules = oreVeinRules;
    }
    
    public void sampleOreVeinNoise(double[] buffer, int x, int z, int minY, int noiseSizeY) {
        this.sample(buffer, x, z, minY, noiseSizeY, this.oreVeininessNoiseSampler, 1.5, 1.5);
    }

    public void sampleOreFirstNoise(double[] buffer, int x, int z, int minY, int noiseSizeY) {
        this.sample(buffer, x, z, minY, noiseSizeY, this.oreVeinFirstNoiseSampler, 4.0, 4.0);
    }

    public void sampleOreSecondNoise(double[] buffer, int x, int z, int minY, int noiseSizeY) {
        this.sample(buffer, x, z, minY, noiseSizeY, this.oreVeinSecondNoiseSampler, 4.0, 4.0);
    }

    public BlockState sample(
        int x,
        int y,
        int z,
        double oreVeinNoise,
        double oreFirstNoise,
        double oreSecondNoise
    ) {
        AbstractRandom random = this.orePosRandomDeriver.createRandom(x, y, z);
        
        OreVeinType veinType = this.getVeinType(oreVeinNoise, y);
        
        if (veinType == null) 
            return null;
        
        // Sample block source to get correct ore and filler block type
        BlockState blockState = this.deepslateSampler.apply(x, y, z);
        boolean inDeepslate = blockState != null && blockState.isOf(Blocks.DEEPSLATE);

        BlockState fillerBlock = inDeepslate ? veinType.deepslateFillerBlock() : veinType.stoneFillerBlock();
        BlockState oreBlock = inDeepslate ? veinType.deepslateOreBlock() : veinType.stoneOreBlock();
        BlockState rawBlock = veinType.rawBlock();
        
        if (random.nextFloat() > 0.7f)
            return null;
        
        if (this.shouldPlaceOreBlock(oreFirstNoise, oreSecondNoise)) {
            double oreVeinSelector = MathHelper.clampedLerpFromProgress(Math.abs(oreVeinNoise), 0.4f, 0.6f, 0.1f, 0.3f);
            
            if (random.nextFloat() < oreVeinSelector && this.oreGapNoiseSampler.sample(x, y, z) > -0.3) {
                return random.nextFloat() < 0.02f ? rawBlock : oreBlock;
            }
            
            return fillerBlock;
        }
        
        return null;
        
    }

    private OreVeinType getVeinType(double oreVeinNoise, int y) {
        OreVeinType veinType = this.oreVeinRules.sample(oreVeinNoise);
        
        if (veinType == null) {
            return null;
        }
        
        int oreMinY = veinType.minY();
        int oreMaxY = veinType.maxY();
        
        int upperY = oreMaxY - y;
        int lowerY = y - oreMinY;
        
        if (lowerY < 0 || upperY < 0) {
            return null;
        }
        
        int minY = Math.min(upperY, lowerY);
        double lerpedY = MathHelper.clampedLerpFromProgress(minY, 0.0, 20.0, -0.2, 0.0);
        
        if (Math.abs(oreVeinNoise) + lerpedY < 0.5) {
            return null;
        }
        
        return veinType;
    }
    
    private boolean shouldPlaceOreBlock(double oreFirstNoise, double oreSecondNoise) {
        double ridgedFirstNoise = Math.abs(1.0 * oreFirstNoise) - 0.08;
        double ridgedSecondNoise = Math.abs(1.0 * oreSecondNoise) - 0.08;
        
        return Math.max(ridgedFirstNoise, ridgedSecondNoise) < 0.0;
    }
}
