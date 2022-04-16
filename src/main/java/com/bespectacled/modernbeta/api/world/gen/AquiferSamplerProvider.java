package com.bespectacled.modernbeta.api.world.gen;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.util.BlockStates;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.AquiferSampler.FluidLevel;
import net.minecraft.world.gen.chunk.AquiferSampler.FluidLevelSampler;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.noise.NoiseRouter;
import net.minecraft.world.gen.random.AtomicSimpleRandom;
import net.minecraft.world.gen.random.RandomDeriver;

public class AquiferSamplerProvider {
    private final NoiseRouter noiseRouter;
    private final RandomDeriver aquiferRandomDeriver;
    
    private final FluidLevelSampler fluidLevelSampler;
    private final FluidLevelSampler lavalessFluidLevelSampler;
    
    private final ChunkNoiseSampler chunkSampler;
    
    private final int worldMinY;
    private final int worldHeight;
    private final int verticalNoiseResolution;
    
    private final boolean generateAquifers;
    
    public AquiferSamplerProvider(
        NoiseRouter noiseRouter,
        BlockState defaultFluid,
        int seaLevel,
        int lavaLevel
    ) {
        this(
            noiseRouter,
            new AtomicSimpleRandom(-1).createRandomDeriver(),
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
        RandomDeriver randomDeriver,
        ChunkNoiseSampler chunkSampler,
        BlockState defaultFluid,
        int seaLevel,
        int lavaLevel,
        int worldMinY,
        int worldHeight, 
        int verticalNoiseResolution,
        boolean generateAquifers
    ) {
        this.noiseRouter = noiseRouter;
        this.aquiferRandomDeriver = randomDeriver.createRandom(ModernBeta.createId("aquifer")).createRandomDeriver();
        
        FluidLevel lavaFluidLevel = new FluidLevel(lavaLevel, BlockStates.LAVA); // Vanilla: -54
        FluidLevel seaFluidLevel = new FluidLevel(seaLevel, defaultFluid);
        
        this.fluidLevelSampler = (x, y, z) -> y < lavaLevel ? lavaFluidLevel : seaFluidLevel;
        this.lavalessFluidLevelSampler = (x, y, z) -> seaFluidLevel;
        
        this.chunkSampler = chunkSampler;
        
        this.worldMinY = worldMinY;
        this.worldHeight = worldHeight;
        this.verticalNoiseResolution = verticalNoiseResolution;
        
        this.generateAquifers = generateAquifers;
    }
    
    public AquiferSampler provideAquiferSampler(Chunk chunk) {
        if (!this.generateAquifers) {
            return AquiferSampler.seaLevel(this.lavalessFluidLevelSampler);
        }
        
        int minY = Math.max(this.worldMinY, chunk.getBottomY());
        int topY = Math.min(this.worldMinY + this.worldHeight, chunk.getTopY());
        
        int noiseMinY = MathHelper.floorDiv(minY, this.verticalNoiseResolution);
        int noiseTopY = MathHelper.floorDiv(topY - minY, this.verticalNoiseResolution);
        
        return AquiferSampler.aquifer(
            this.chunkSampler,
            chunk.getPos(),
            this.noiseRouter.barrierNoise(),
            this.noiseRouter.fluidLevelFloodednessNoise(),
            this.noiseRouter.fluidLevelSpreadNoise(),
            this.noiseRouter.lavaNoise(),
            this.aquiferRandomDeriver,
            noiseMinY * this.verticalNoiseResolution, 
            noiseTopY * this.verticalNoiseResolution, 
            this.fluidLevelSampler
        );
    }
}
