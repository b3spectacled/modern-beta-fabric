package com.bespectacled.modernbeta.api.world.gen;

import java.util.List;
import java.util.Random;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.mixin.MixinPlacedFeatureAccessor;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.world.feature.placement.OldNoiseBasedCountPlacementModifier;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.chunk.AquiferSampler.FluidLevel;
import net.minecraft.world.gen.chunk.AquiferSampler.FluidLevelSampler;
import net.minecraft.world.gen.decorator.PlacementModifier;
import net.minecraft.world.gen.feature.PlacedFeature;

public abstract class BaseChunkProvider extends ChunkProvider {
    
    
    private static final int LAVA_LEVEL = -53; // Vanilla: -54;
    
    protected final Random rand;
    
    protected final Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry;
    
    protected final int worldMinY;
    protected final int worldHeight;
    protected final int worldTopY;
    protected final int seaLevel;
    
    protected final int bedrockFloor;
    protected final int bedrockCeiling;
    
    protected final boolean generateDeepslate;
    
    protected final BlockState defaultBlock;
    protected final BlockState defaultFluid;
    
    protected final FluidLevelSampler fluidLevelSampler;
    protected final FluidLevelSampler lavalessFluidLevelSampler;

    public BaseChunkProvider(OldChunkGenerator chunkGenerator) {
        this(
            chunkGenerator,
            chunkGenerator.getNoiseRegistry(),
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().minimumY(),
            chunkGenerator.getGeneratorSettings().get().getGenerationShapeConfig().height(),
            chunkGenerator.getGeneratorSettings().get().getSeaLevel(),
            0,
            Integer.MIN_VALUE,
            NbtUtil.readBoolean(NbtTags.GEN_DEEPSLATE, chunkGenerator.getProviderSettings(), ModernBeta.GEN_CONFIG.infGenConfig.generateDeepslate),
            chunkGenerator.getGeneratorSettings().get().getDefaultBlock(),
            chunkGenerator.getGeneratorSettings().get().getDefaultFluid()
        );
    }
    
    public BaseChunkProvider(
        OldChunkGenerator chunkGenerator,
        Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry,
        int worldMinY,
        int worldHeight,
        int seaLevel,
        int bedrockFloor,
        int bedrockCeiling,
        boolean generateDeepslate,
        BlockState defaultBlock,
        BlockState defaultFluid
    ) {
        super(chunkGenerator);
        
        this.noiseRegistry = noiseRegistry;
        
        this.worldMinY = worldMinY;
        this.worldHeight = worldHeight;
        this.worldTopY = worldHeight + worldMinY;
        this.seaLevel = seaLevel;
        this.bedrockFloor = bedrockFloor;
        this.bedrockCeiling = bedrockCeiling;
        this.generateDeepslate = generateDeepslate;

        this.defaultBlock = defaultBlock;
        this.defaultFluid = defaultFluid;
        
        this.rand = new Random(seed);
        
        FluidLevel lavaFluidLevel = new FluidLevel(LAVA_LEVEL, BlockStates.LAVA); // Vanilla: -54
        FluidLevel seaFluidLevel = new FluidLevel(seaLevel, this.defaultFluid);
        
        this.fluidLevelSampler = (x, y, z) -> y < LAVA_LEVEL ? lavaFluidLevel : seaFluidLevel;
        this.lavalessFluidLevelSampler = (x, y, z) -> seaFluidLevel;
        
        // Handle bad height values
        if (this.worldMinY > this.worldHeight)
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
    public int getWorldMinY() {
        return this.worldMinY;
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
     * Sets forest density using PerlinOctaveNoise sampler created with world seed.
     * Checks every placed feature in the biome source feature list,
     * and if it uses OldNoiseBasedCountPlacementModifier, replaces the noise sampler.
     * 
     * @param forestOctaves PerlinOctaveNoise object used to set forest octaves.
     */
    protected void setForestOctaves(PerlinOctaveNoise forestOctaves) {
        List<List<PlacedFeature>> generationSteps = this.chunkGenerator.getBiomeSource().method_38115();
        
        for (List<PlacedFeature> step : generationSteps) {
            for (PlacedFeature placedFeature : step) {
                MixinPlacedFeatureAccessor accessor = (MixinPlacedFeatureAccessor)placedFeature;
                List<PlacementModifier> modifiers = accessor.getPlacementModifiers();
                
                for (PlacementModifier modifier : modifiers) {
                    if (modifier instanceof OldNoiseBasedCountPlacementModifier noiseModifier) {
                        noiseModifier.setOctaves(forestOctaves);
                    }
                }
            }
        }
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
