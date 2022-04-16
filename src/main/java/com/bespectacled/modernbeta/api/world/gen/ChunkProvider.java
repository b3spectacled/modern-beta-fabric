package com.bespectacled.modernbeta.api.world.gen;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.bespectacled.modernbeta.api.world.spawn.SpawnLocator;
import com.bespectacled.modernbeta.mixin.MixinPlacedFeatureAccessor;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.noise.SimpleDensityFunction;
import com.bespectacled.modernbeta.util.settings.Settings;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.feature.placement.OldNoiseBasedCountPlacementModifier;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;
import com.bespectacled.modernbeta.world.gen.OldChunkNoiseSampler;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource.IndexedFeatures;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.AquiferSampler.FluidLevel;
import net.minecraft.world.gen.chunk.AquiferSampler.FluidLevelSampler;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.noise.NoiseRouter;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.random.AbstractRandom;
import net.minecraft.world.gen.random.ChunkRandom;
import net.minecraft.world.gen.random.RandomDeriver;

public abstract class ChunkProvider {
    protected final OldChunkGenerator chunkGenerator;
    
    protected final long seed;
    protected final RegistryEntry<ChunkGeneratorSettings> generatorSettings;
    protected final Settings providerSettings;
    protected final Random random;

    protected final Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry;
    protected final NoiseRouter noiseRouter;
    
    protected final OldChunkNoiseSampler dummyNoiseChunkSampler;

    protected final ChunkRandom.RandomProvider randomProvider;
    protected final RandomDeriver randomDeriver;
    
    protected final boolean generateDeepslate;
    
    private final FluidLevelSampler emptyFluidLevelSampler;
    
    protected SpawnLocator spawnLocator;
    
    /**
     * Construct a Modern Beta chunk provider with seed and settings.
     * 
     * @param chunkGenerator Parent OldChunkGenerator object used to initialize fields.
     */
    public ChunkProvider(OldChunkGenerator chunkGenerator) {
        this.chunkGenerator = chunkGenerator;
        
        this.seed = chunkGenerator.getWorldSeed();
        this.generatorSettings = chunkGenerator.getGeneratorSettings();
        this.providerSettings = chunkGenerator.getChunkSettings();
        this.random = new Random(seed);
        
        this.noiseRegistry = this.chunkGenerator.getNoiseRegistry();
        this.noiseRouter = this.generatorSettings.value().method_41099(this.noiseRegistry, this.seed);

        this.emptyFluidLevelSampler = (x, y, z) -> new FluidLevel(this.getSeaLevel(), BlockStates.AIR);
        this.randomProvider = chunkGenerator.getGeneratorSettings().value().getRandomProvider();
        this.randomDeriver = this.randomProvider.create(this.seed).createRandomDeriver();

        // Modified ChunkNoiseSampler
        ChunkGeneratorSettings generatorSettings = chunkGenerator.getGeneratorSettings().value();
        GenerationShapeConfig shapeConfig = generatorSettings.generationShapeConfig();
        int verticalNoiseResolution = shapeConfig.verticalSize() * 4;
        int horizontalNoiseResolution = shapeConfig.horizontalSize() * 4;
        
        this.dummyNoiseChunkSampler = new OldChunkNoiseSampler(
            horizontalNoiseResolution,
            verticalNoiseResolution,
            16 / horizontalNoiseResolution,
            this.noiseRouter,
            0, 
            0,
            () -> SimpleDensityFunction.INSTANCE,
            () -> this.generatorSettings.value(),
            this.emptyFluidLevelSampler,
            Blender.getNoBlending(),
            this
        );
        
        this.generateDeepslate = NbtUtil.toBoolean(this.providerSettings.get(NbtTags.GEN_DEEPSLATE), false);
        
        this.spawnLocator = SpawnLocator.DEFAULT;
    }
    
    /**
     * Generates base terrain for given chunk and returns it.
     * 
     * @param executor
     * @param blender TODO
     * @param structureAccessor
     * @param chunk
     * @return A completed chunk.
     */
    public abstract CompletableFuture<Chunk> provideChunk(Executor executor, Blender blender, StructureAccessor structureAccessor, Chunk chunk);
    
    /**
     * Generates biome-specific surface for given chunk.
     * 
     * @param region
     * @param chunk
     * @param biomeSource
     */
    public abstract void provideSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource);
    
    /**
     * Sample height at given x/z coordinate. Initially generates heightmap for entire chunk, 
     * if chunk containing x/z coordinates has never been sampled.
     *
     * @param x x-coordinate in block coordinates.
     * @param z z-coordinate in block coordinates.
     * @param type Vanilla heightmap type.
     * 
     * @return The y-coordinate of top block at x/z.
     */
    public abstract int getHeight(int x, int z, Heightmap.Type heightmap);
    
    /**
     * Determines whether to skip the chunk for some chunk generation step, depending on the x/z chunk coordinates.
     * 
     * @param chunkX x-coordinate in chunk coordinates.
     * @param chunkZ z-coordinate in chunk coordinates.
     * @param chunkStatus Chunk generation step used for skip context.
     * 
     * @return Whether to skip the chunk.
     */
    public boolean skipChunk(int chunkX, int chunkZ, ChunkStatus chunkStatus) {
        return false;
    }
    
    /**
     * Get total world height in blocks, including minimum Y. 
     * (i.e. Returns 320 if bottomY is -64 and topY is 256.)
     * 
     * @return Total world height in blocks.
     */
    public int getWorldHeight() {
        return this.generatorSettings.value().generationShapeConfig().height();
    }
    
    /**
     * @return Minimum Y coordinate in block coordinates.
     */
    public int getWorldMinY() {
        return this.generatorSettings.value().generationShapeConfig().minimumY();
    }
    
    /**
     * @return World sea level in block coordinates.
     */
    public int getSeaLevel() {
        return this.generatorSettings.value().seaLevel();
    }
    
    /**
     * Get aquifer sampler, for carving for now.
     * 
     * @return An aquifer sampler.
     */
    public AquiferSampler getAquiferSampler(Chunk chunk) {
        return AquiferSampler.seaLevel(this.emptyFluidLevelSampler);
    }
    
    public FluidLevelSampler getFluidLevelSampler() {
        return this.emptyFluidLevelSampler;
    }
    
    /**
     * Locate initial spawn position for players.
     * If optional left empty, then vanilla spawn position is used.
     * 
     * @return Optional BlockPos player spawn position.
     */
    public Optional<BlockPos> locateSpawn() {
        return this.spawnLocator.locateSpawn();
    }
    
    /**
     * @return Chunk provider's spawn locator.
     */
    public SpawnLocator getSpawnLocator() {
        return this.spawnLocator;
    }
    
    /**
     * @return Parent OldChunkGenerator.
     */
    public OldChunkGenerator getChunkGenerator() {
        return this.chunkGenerator;
    }
    
    /**
     * Samples biome at given biome coordinates.
     * 
     * @param biomeX x-coordinate in biome coordinates.
     * @param biomeY y-coordinate in biome coordinates.
     * @param biomeZ z-coordinate in biome coordinates.
     * @param noiseSampler
     * 
     * @return A biome.
     */
    public RegistryEntry<Biome> getBiome(int biomeX, int biomeY, int biomeZ, MultiNoiseUtil.MultiNoiseSampler noiseSampler) {
        return this.chunkGenerator.getBiomeSource().getBiome(biomeX, biomeY, biomeZ, noiseSampler);
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
     * Samples blockstate for placing deepslate gradient during surface generation.
     * 
     * @param x x-coordinate in block coordinates.
     * @param y y-coordinate in block coordinates.
     * @param z z-coordinate in block coordinates.
     * 
     * @return Null or deepslate blockstate.
     */
    protected BlockState sampleDeepslateState(int x, int y, int z) {
        int minY = 0;
        int maxY = 8;
        
        if (!this.generateDeepslate || y >= maxY)
            return null;
        
        if (y <= minY)
            return BlockStates.DEEPSLATE;
        
        double yThreshold = MathHelper.lerpFromProgress(y, minY, maxY, 1.0, 0.0);
        AbstractRandom random = this.randomDeriver.createRandom(x, y, z);
        
        return (double)random.nextFloat() < yThreshold ? BlockStates.DEEPSLATE : null;
    }

    /**
     * Sets forest density using PerlinOctaveNoise sampler created with world seed.
     * Checks every placed feature in the biome source feature list,
     * and if it uses OldNoiseBasedCountPlacementModifier, replaces the noise sampler.
     * 
     * @param forestOctaves PerlinOctaveNoise object used to set forest octaves.
     */
    protected void setForestOctaves(PerlinOctaveNoise forestOctaves) {
        List<IndexedFeatures> generationSteps = this.chunkGenerator.getBiomeSource().getIndexedFeatures();
        
        for (IndexedFeatures step : generationSteps) {
            List<PlacedFeature> featureList = step.features();
            
            for (PlacedFeature placedFeature : featureList) {
                MixinPlacedFeatureAccessor accessor = (MixinPlacedFeatureAccessor)(Object)placedFeature;
                List<PlacementModifier> modifiers = accessor.getPlacementModifiers();
                
                for (PlacementModifier modifier : modifiers) {
                    if (modifier instanceof OldNoiseBasedCountPlacementModifier noiseModifier) {
                        noiseModifier.setOctaves(forestOctaves);
                    }
                }
            }
        }
    }
}