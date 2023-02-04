package mod.bespectacled.modernbeta.api.world.chunk;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.util.BlockStates;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.util.math.random.RandomSplitter;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.AquiferSampler.FluidLevel;
import net.minecraft.world.gen.chunk.AquiferSampler.FluidLevelSampler;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.noise.NoiseRouter;

public class AquiferSamplerProvider {
    private static final int FAR_LANDS_BOUNDARY = 12550821;
    
    private final NoiseRouter noiseRouter;
    private final RandomSplitter randomSplitter;
    
    private final FluidLevelSampler fluidLevelSampler;
    private final FluidLevelSampler lavalessFluidLevelSampler;
    
    private final ChunkNoiseSampler chunkSampler;
    
    private final int worldMinY;
    private final int worldHeight;
    private final int noiseResolutionVertical;
    
    private final boolean generateAquifers;
    
    public AquiferSamplerProvider(
        NoiseRouter noiseRouter,
        BlockState defaultFluid,
        int seaLevel,
        int lavaLevel
    ) {
        this(
            noiseRouter,
            new LocalRandom(-1).nextSplitter(),
            null,
            defaultFluid,
            seaLevel,
            lavaLevel,
            0,
            0,
            0,
            false
        );
    }
    
    public AquiferSamplerProvider(
        NoiseRouter noiseRouter,
        RandomSplitter randomSplitter,
        ChunkNoiseSampler chunkSampler,
        BlockState defaultFluid,
        int seaLevel,
        int lavaLevel,
        int worldMinY,
        int worldHeight, 
        int noiseResolutionVertical,
        boolean generateAquifers
    ) {
        this.noiseRouter = noiseRouter;
        this.randomSplitter = randomSplitter.split(ModernBeta.createId("aquifer")).nextSplitter();
        
        FluidLevel lavaFluidLevel = new FluidLevel(lavaLevel, BlockStates.LAVA); // Vanilla: -54
        FluidLevel seaFluidLevel = new FluidLevel(seaLevel, defaultFluid);
        
        this.fluidLevelSampler = (x, y, z) -> {
            // Do not generate lava past Far Lands boundary
            if (Math.abs(x) >= FAR_LANDS_BOUNDARY || Math.abs(z) >= FAR_LANDS_BOUNDARY)
                return seaFluidLevel;
            
            return y < lavaLevel ? lavaFluidLevel : seaFluidLevel;
        };
        
        this.lavalessFluidLevelSampler = (x, y, z) -> seaFluidLevel;
        
        this.chunkSampler = chunkSampler;
        
        this.worldMinY = worldMinY;
        this.worldHeight = worldHeight;
        this.noiseResolutionVertical = noiseResolutionVertical;
        
        this.generateAquifers = generateAquifers;
    }
    
    public AquiferSampler provideAquiferSampler(Chunk chunk) {
        if (!this.generateAquifers) {
            return AquiferSampler.seaLevel(this.lavalessFluidLevelSampler);
        }
        
        int minY = Math.max(this.worldMinY, chunk.getBottomY());
        int topY = Math.min(this.worldMinY + this.worldHeight, chunk.getTopY());
        
        int noiseMinY = MathHelper.floorDiv(minY, this.noiseResolutionVertical);
        int noiseTopY = MathHelper.floorDiv(topY - minY, this.noiseResolutionVertical);
        
        return AquiferSampler.aquifer(
            this.chunkSampler,
            chunk.getPos(),
            this.noiseRouter,
            this.randomSplitter,
            noiseMinY * this.noiseResolutionVertical, 
            noiseTopY * this.noiseResolutionVertical, 
            this.fluidLevelSampler
        );
    }
}
