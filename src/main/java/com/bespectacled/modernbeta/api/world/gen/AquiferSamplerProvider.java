package com.bespectacled.modernbeta.api.world.gen;

import com.bespectacled.modernbeta.util.BlockStates;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.NoiseColumnSampler;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.AquiferSampler.FluidLevel;
import net.minecraft.world.gen.chunk.AquiferSampler.FluidLevelSampler;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.random.AbstractRandom;
import net.minecraft.world.gen.random.AtomicSimpleRandom;
import net.minecraft.world.gen.random.BlockPosRandomDeriver;

public class AquiferSamplerProvider {
    private final DoublePerlinNoiseSampler edgeDensityNoise;
    private final DoublePerlinNoiseSampler fluidLevelNoise;
    private final DoublePerlinNoiseSampler fluidTypeNoise;
    private final BlockPosRandomDeriver blockPosRandomDeriver;
    private final DoublePerlinNoiseSampler fluidTypeLevelNoise;
    
    private final FluidLevelSampler fluidLevelSampler;
    private final FluidLevelSampler lavalessFluidLevelSampler;
    
    private final NoiseColumnSampler columnSampler;
    private final ChunkNoiseSampler chunkSampler;
    
    private final int worldMinY;
    private final int worldHeight;
    private final int verticalNoiseResolution;
    
    private final boolean generateAquifers;
    
    public AquiferSamplerProvider(
        BlockState defaultFluid,
        int seaLevel,
        int lavaLevel
    ) {
        this(
            new AtomicSimpleRandom(-1),
            null,
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
        AbstractRandom random,
        NoiseColumnSampler columnSampler,
        ChunkNoiseSampler chunkSampler,
        BlockState defaultFluid,
        int seaLevel,
        int lavaLevel,
        int worldMinY,
        int worldHeight, 
        int verticalNoiseResolution,
        boolean generateAquifers
    ) {
        this.edgeDensityNoise = DoublePerlinNoiseSampler.create(random.derive(), -3, 1.0);
        this.fluidLevelNoise = DoublePerlinNoiseSampler.create(random.derive(), -7, 1.0);
        this.fluidTypeNoise = DoublePerlinNoiseSampler.create(random.derive(), -1, 1.0, 0.0);
        this.blockPosRandomDeriver = random.createBlockPosRandomDeriver();
        this.fluidTypeLevelNoise = DoublePerlinNoiseSampler.create(random.derive(), -4, 1.0);
        
        FluidLevel lavaFluidLevel = new FluidLevel(lavaLevel, BlockStates.LAVA); // Vanilla: -54
        FluidLevel seaFluidLevel = new FluidLevel(seaLevel, defaultFluid);
        
        this.fluidLevelSampler = (x, y, z) -> y < lavaLevel ? lavaFluidLevel : seaFluidLevel;
        this.lavalessFluidLevelSampler = (x, y, z) -> seaFluidLevel;
        
        this.columnSampler = columnSampler;
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
            this.edgeDensityNoise, 
            this.fluidLevelNoise,
            this.fluidTypeLevelNoise,
            this.fluidTypeNoise, 
            this.blockPosRandomDeriver, 
            this.columnSampler, 
            noiseMinY * this.verticalNoiseResolution, 
            noiseTopY * this.verticalNoiseResolution, 
            this.fluidLevelSampler
        );
    }
}
