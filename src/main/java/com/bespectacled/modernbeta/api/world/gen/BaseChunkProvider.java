package com.bespectacled.modernbeta.api.world.gen;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.compat.CompatBiomes;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.world.decorator.OldDecorators;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;

import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.noise.NoiseSampler;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;

public abstract class BaseChunkProvider extends ChunkProvider {
    // Set for specifying which biomes should use their vanilla surface builders.
    // Done on per-biome basis for best mod compatibility.
    private static final Set<Identifier> BIOMES_WITH_CUSTOM_SURFACES = new HashSet<Identifier>(
        Stream.concat(
            CompatBiomes.BIOMES_WITH_CUSTOM_SURFACES.stream().map(b -> new Identifier(b)), 
            ModernBeta.COMPAT_CONFIG.biomesWithCustomSurfaces.stream().map(b -> new Identifier(b))
        ).toList()
    );
    
    protected final Random rand;
    
    protected final int minY;
    protected final int worldHeight;
    protected final int worldTopY;
    protected final int seaLevel;
    protected final int minSurfaceY;
    
    protected final int bedrockFloor;
    protected final int bedrockCeiling;
    
    protected final BlockState defaultBlock;
    protected final BlockState defaultFluid;
    
    protected final NoiseSampler surfaceDepthNoise;

    public BaseChunkProvider(OldChunkGenerator chunkGenerator) {
        this(
            chunkGenerator,
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().getMinimumY(),
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().getHeight(),
            chunkGenerator.getGeneratorSettings().get().getSeaLevel(),
            chunkGenerator.getGeneratorSettings().get().getMinSurfaceLevel(),
            chunkGenerator.getGeneratorSettings().get().getBedrockFloorY(),
            chunkGenerator.getGeneratorSettings().get().getBedrockCeilingY(),
            chunkGenerator.getGeneratorSettings().get().getDefaultBlock(),
            chunkGenerator.getGeneratorSettings().get().getDefaultFluid()
        );
    }
    
    public BaseChunkProvider(
        OldChunkGenerator chunkGenerator,
        int minY,
        int worldHeight,
        int seaLevel,
        int minSurfaceY,
        int bedrockFloor,
        int bedrockCeiling,
        BlockState defaultBlock,
        BlockState defaultFluid
    ) {
        super(chunkGenerator);
        
        this.minY = minY;
        this.worldHeight = worldHeight;
        this.worldTopY = worldHeight + minY;
        this.seaLevel = seaLevel;
        this.minSurfaceY = minSurfaceY;
        this.bedrockFloor = bedrockFloor;
        this.bedrockCeiling = bedrockCeiling;

        this.defaultBlock = defaultBlock;
        this.defaultFluid = defaultFluid;
        
        // Surface noise sampler
        this.surfaceDepthNoise = this.generatorSettings.get().getGenerationShapeConfig().hasSimplexSurfaceNoise() ? 
            new OctaveSimplexNoiseSampler(new ChunkRandom(seed), IntStream.rangeClosed(-3, 0)) : 
            new OctavePerlinNoiseSampler(new ChunkRandom(seed), IntStream.rangeClosed(-3, 0));
        
        this.rand = new Random(seed);
        
        // Handle bad height values
        if (this.minY > this.worldHeight)
            throw new IllegalStateException("[Modern Beta] Minimum height cannot be greater than world height!");
    }
    
    /**
     * @return Total world height including minimum y coordinate in block coordinates. 
     */
    public int getWorldHeight() {
        return this.worldHeight;
    }
    
    /**
     * @return Minimum Y coordinate in block coordinates.
     */
    public int getMinimumY() {
        return this.minY;
    }
    
    /**
     * @return World sea level in block coordinates.
     */
    public int getSeaLevel() {
        return this.seaLevel;
    }
    
    /**
     * Get a new Random object initialized with chunk coordinates for seed, for surface generation.
     * 
     * @param chunkX x-coordinate in chunk coordinates.
     * @param chunkZ z-coordinate in chunk coordinates.
     * 
     * @return New Random object initialized with chunk coordinates for seed.
     */
    protected Random createSurfaceRandom(int chunkX, int chunkZ) {
        long seed = (long)chunkX * 0x4f9939f508L + (long)chunkZ * 0x1ef1565bd5L;
        
        return new Random(seed);
    }
    
    /**
     * Sets forest density using PerlinOctaveNoise object created using world seed.
     * 
     * @param forestOctaves PerlinOctaveNoise object used to set forest octaves.
     */
    protected void setForestOctaves(PerlinOctaveNoise forestOctaves) {
        OldDecorators.COUNT_BETA_NOISE.setOctaves(forestOctaves);
        OldDecorators.COUNT_ALPHA_NOISE.setOctaves(forestOctaves);
        OldDecorators.COUNT_INFDEV_415_NOISE.setOctaves(forestOctaves);
        OldDecorators.COUNT_INFDEV_420_NOISE.setOctaves(forestOctaves);
        OldDecorators.COUNT_INFDEV_611_NOISE.setOctaves(forestOctaves);
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
    protected boolean useCustomSurfaceBuilder(Biome biome, Identifier biomeId, ChunkRegion region, Chunk chunk, Random random, BlockPos.Mutable mutable) {
        int x = mutable.getX();
        int y = mutable.getY();
        int z = mutable.getZ();
        
        if (BIOMES_WITH_CUSTOM_SURFACES.contains(biomeId)) {
            double surfaceNoise = this.surfaceDepthNoise.sample(x * 0.0625, z * 0.0625, 0.0625, (x & 0xF) * 0.0625) * 15.0;
            biome.buildSurface(
                random, 
                chunk, 
                x, z, y, 
                surfaceNoise, 
                this.defaultBlock, 
                this.defaultFluid,
                this.seaLevel, 
                this.minSurfaceY,
                region.getSeed()
            );
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Creates an array to hold 16 * 16 array of double values for surface generation.
     * 
     * @return Double array of size 256.
     */
    protected double[] createSurfaceArray() {
        return new double[256];
    }
}
