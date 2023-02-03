package mod.bespectacled.modernbeta.world.feature.placement;

import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import mod.bespectacled.modernbeta.world.feature.placement.noise.BetaNoiseBasedCount;
import mod.bespectacled.modernbeta.world.feature.placement.noise.ModernBetaNoiseBasedCount;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.placementmodifier.AbstractCountPlacementModifier;

public abstract class ModernBetaNoiseBasedCountPlacementModifier extends AbstractCountPlacementModifier {
    protected final int count;
    protected final double extraChance;
    protected final int extraCount;

    protected ModernBetaNoiseBasedCount noiseDecorator;
    
    protected ModernBetaNoiseBasedCountPlacementModifier(int count, double extraChance, int extraCount) {
        this.count = count;
        this.extraChance = extraChance;
        this.extraCount = extraCount;
        
        this.noiseDecorator = new BetaNoiseBasedCount(new LocalRandom(0L));
    }
    
    @Override
    protected int getCount(Random random, BlockPos pos) {
        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;
        
        return this.noiseDecorator.sample(chunkX, chunkZ, random) + this.count + ((random.nextFloat() < this.extraChance) ? this.extraCount : 0);
    }

    public abstract void setOctaves(PerlinOctaveNoise octaves);
}
