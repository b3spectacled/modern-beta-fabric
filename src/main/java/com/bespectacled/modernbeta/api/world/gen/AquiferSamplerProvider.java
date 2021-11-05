package com.bespectacled.modernbeta.api.world.gen;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.util.BlockStates;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.NoiseColumnSampler;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.AquiferSampler.FluidLevel;
import net.minecraft.world.gen.chunk.AquiferSampler.FluidLevelSampler;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.noise.NoiseParametersKeys;
import net.minecraft.world.gen.random.AtomicSimpleRandom;
import net.minecraft.world.gen.random.RandomDeriver;

public class AquiferSamplerProvider {
    private final DoublePerlinNoiseSampler aquiferBarrierNoise;
    private final DoublePerlinNoiseSampler aquiferFluidLevelFloodedNoise;
    private final DoublePerlinNoiseSampler aquiferLavaNoise;
    private final DoublePerlinNoiseSampler aquiferLevelSpreadNoise;

    private final RandomDeriver aquiferRandomDeriver;
    
    private final FluidLevelSampler fluidLevelSampler;
    private final FluidLevelSampler lavalessFluidLevelSampler;
    
    private final NoiseColumnSampler columnSampler;
    private final ChunkNoiseSampler chunkSampler;
    
    private final int worldMinY;
    private final int worldHeight;
    private final int verticalNoiseResolution;
    
    private final boolean generateAquifers;
    
    public AquiferSamplerProvider(
        Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry,  
        BlockState defaultFluid,
        int seaLevel,
        int lavaLevel
    ) {
        this(
            noiseRegistry,
            new AtomicSimpleRandom(-1).createRandomDeriver(),
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
        Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry,
        RandomDeriver randomDeriver,
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
        this.aquiferBarrierNoise = NoiseParametersKeys.method_39173(noiseRegistry, randomDeriver, NoiseParametersKeys.AQUIFER_BARRIER);
        this.aquiferFluidLevelFloodedNoise = NoiseParametersKeys.method_39173(noiseRegistry, randomDeriver, NoiseParametersKeys.AQUIFER_FLUID_LEVEL_FLOODEDNESS);
        this.aquiferLavaNoise = NoiseParametersKeys.method_39173(noiseRegistry, randomDeriver, NoiseParametersKeys.AQUIFER_LAVA);
        this.aquiferLevelSpreadNoise = NoiseParametersKeys.method_39173(noiseRegistry, randomDeriver, NoiseParametersKeys.AQUIFER_FLUID_LEVEL_SPREAD);

        this.aquiferRandomDeriver = randomDeriver.createRandom(ModernBeta.createId("aquifer")).createRandomDeriver();
        
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
            this.aquiferBarrierNoise, 
            this.aquiferFluidLevelFloodedNoise,
            this.aquiferLevelSpreadNoise,
            this.aquiferLavaNoise, 
            this.aquiferRandomDeriver, 
            this.columnSampler, 
            noiseMinY * this.verticalNoiseResolution, 
            noiseTopY * this.verticalNoiseResolution, 
            this.fluidLevelSampler
        );
    }
}
