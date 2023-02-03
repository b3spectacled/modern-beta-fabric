package mod.bespectacled.modernbeta.world.chunk.blocksource;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.random.RandomSplitter;

public class DeepslateBlockSource implements SimpleBlockSource {
    private final int minY;
    private final int maxY;
    private final boolean useDeepslate;
    private final BlockState deepslateBlock;
    private final RandomSplitter randomSplitter;
    
    public DeepslateBlockSource(int minY, int maxY, boolean useDeepslate, BlockState deepslateBlock, RandomSplitter randomSplitter) {
        this.minY = minY;
        this.maxY = maxY;
        this.useDeepslate = useDeepslate;
        this.deepslateBlock = deepslateBlock;
        this.randomSplitter = randomSplitter;
    }
    
    
    @Override
    public BlockState apply(int x, int y, int z) {
        if (!this.useDeepslate || y >= maxY)
            return null;
        
        if (y <= minY)
            return this.deepslateBlock;
        
        double yThreshold = MathHelper.lerp(MathHelper.getLerpProgress(y, minY, maxY), 1.0, 0.0);
        Random random = this.randomSplitter.split(x, y, z);
        
        return (double)random.nextFloat() < yThreshold ? this.deepslateBlock : null;
    }

}
