package com.bespectacled.modernbeta.api.world.gen;

import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import com.bespectacled.modernbeta.compat.CompatBiomes;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.world.decorator.OldDecorators;
import com.bespectacled.modernbeta.world.gen.StructureWeightSampler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.noise.NoiseSampler;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public abstract class ChunkProvider extends AbstractChunkProvider {
    protected static final Random RAND = new Random();
    
    protected final int worldHeight;
    protected final int seaLevel;
    
    protected final int bedrockFloor;
    protected final int bedrockCeiling;
    
    protected final BlockState defaultBlock;
    protected final BlockState defaultFluid;
    
    protected final NoiseSampler surfaceDepthNoise;

    public ChunkProvider(
        long seed, 
        ChunkGenerator chunkGenerator,    
        Supplier<ChunkGeneratorSettings> generatorSettings, 
        CompoundTag providerSettings
    ) {
        this(
            seed, 
            chunkGenerator, 
            generatorSettings, 
            providerSettings,
            generatorSettings.get().getGenerationShapeConfig().getHeight(),
            generatorSettings.get().getSeaLevel(),
            generatorSettings.get().getBedrockFloorY(),
            generatorSettings.get().getBedrockCeilingY(),
            generatorSettings.get().getDefaultBlock(),
            generatorSettings.get().getDefaultBlock()
        );
    }
    
    public ChunkProvider(
        long seed,
        ChunkGenerator chunkGenerator, 
        Supplier<ChunkGeneratorSettings> generatorSettings,
        CompoundTag providerSettings,
        int worldHeight,
        int seaLevel,
        int bedrockFloor,
        int bedrockCeiling,
        BlockState defaultBlock,
        BlockState defaultFluid
    ) {
        super(seed, chunkGenerator, generatorSettings, providerSettings);
        
        this.worldHeight = worldHeight;
        this.seaLevel = seaLevel;
        this.bedrockFloor = bedrockFloor;
        this.bedrockCeiling = bedrockCeiling;

        this.defaultBlock = defaultBlock;
        this.defaultFluid = defaultFluid;
        
        // Surface noise sampler
        this.surfaceDepthNoise = this.generatorSettings.get().getGenerationShapeConfig().hasSimplexSurfaceNoise() ? 
            new OctaveSimplexNoiseSampler(new ChunkRandom(seed), IntStream.rangeClosed(-3, 0)) : 
            new OctavePerlinNoiseSampler(new ChunkRandom(seed), IntStream.rangeClosed(-3, 0));
        
        RAND.setSeed(seed);
    }
    
    protected abstract void generateTerrain(Chunk chunk, StructureAccessor structureAccessor);
    protected abstract int sampleHeightmap(int sampleX, int sampleZ);
    
    public int getWorldHeight() {
        return this.worldHeight;
    }
    
    public int getSeaLevel() {
        return this.seaLevel;
    }
    
    /**
     * Get a new ChunkRandom object initialized with chunk coordinates for seed, for surface generation.
     * 
     * @param chunkX x-coordinate in chunk coordinates.
     * @param chunkZ z-coordinate in chunk coordinates.
     * 
     * @return New ChunkRandom object initialized with chunk coordinates for seed.
     */
    protected ChunkRandom createChunkRand(int chunkX, int chunkZ) {
        ChunkRandom chunkRand = new ChunkRandom();
        chunkRand.setTerrainSeed(chunkX, chunkZ);
        
        return chunkRand;
    }
    
    /**
     * Sets forest density using PerlinOctaveNoise object created using world seed.
     * 
     * @param forestOctaves PerlinOctaveNoise object used to set forest octaves.
     */
    protected void setForestOctaves(PerlinOctaveNoise forestOctaves) {
        OldDecorators.COUNT_BETA_NOISE_DECORATOR.setOctaves(forestOctaves);
        OldDecorators.COUNT_ALPHA_NOISE_DECORATOR.setOctaves(forestOctaves);
        OldDecorators.COUNT_INFDEV_NOISE_DECORATOR.setOctaves(forestOctaves);
    }
    
    /**
     * Use a biome-specific surface builder, at a given x/z-coordinate and topmost y-coordinate.
     * Valid biomes are checked on per-biome basis using identifier from BIOMES_WITH_CUSTOM_SURFACES set. 
     * 
     * @param biome Biome with surface builder to use.
     * @param biomeId Biome identifier, used to check if it uses valid custom surface builder.
     * @param region
     * @param chunk
     * @param random
     * @param mutable Mutable BlockPos at block coordinates position.
     * 
     * @return True if biome is included in valid biomes set and has run surface builder. False if not included and not run.
     */
    protected boolean useCustomSurfaceBuilder(Biome biome, Identifier biomeId, ChunkRegion region, Chunk chunk, ChunkRandom random, BlockPos.Mutable mutable) {
        int x = mutable.getX();
        int y = mutable.getY();
        int z = mutable.getZ();
        
        if (CompatBiomes.hasCustomSurface(biomeId)) {
            double surfaceNoise = this.surfaceDepthNoise.sample(x * 0.0625, z * 0.0625, 0.0625, (x & 0xF) * 0.0625) * 15.0;
            biome.buildSurface(
                random, 
                chunk, 
                x, z, y, 
                surfaceNoise, 
                this.defaultBlock, 
                this.defaultFluid,
                this.seaLevel,
                region.getSeed()
            );
            
            return true;
        }
        
        return false;
    }
    
    
    /**
     * Gets blockstate at block coordinates given block and default fluid block.
     * Simulates a noise density for the purpose of masking terrain around structures.
     * Used for 2D noise terrain generators (i.e. Infdev 20100227 and Indev terrain generators).
     * 
     * @param x x-coordinate in absolute block coordinates.
     * @param y y-coordinate in absolute block coordinates.
     * @param z z-coordinate in absolute block coordinates.
     * @param blockToSet Block to get blockstate for.
     * @oaran defaultFluid Default fluid block.
     * 
     * @return A blockstate.
     */
    protected BlockState getBlockState(StructureWeightSampler weightSampler, int x, int y, int z, Block blockToSet, Block defaultFluid) {
        boolean isFluid = blockToSet == Blocks.AIR || blockToSet == defaultFluid;
        double simDensity = isFluid ? -50D : 50D;
        
        double clampedDensity = weightSampler.sample(x, y, z, simDensity);
        
        BlockState blockState = blockToSet.getDefaultState();
        if (clampedDensity > 0.0 && isFluid) {
            blockState = this.defaultBlock;
        } else if (clampedDensity < 0.0 && !isFluid) {
            blockState = BlockStates.AIR;
        }

        return blockState;
    }
}
