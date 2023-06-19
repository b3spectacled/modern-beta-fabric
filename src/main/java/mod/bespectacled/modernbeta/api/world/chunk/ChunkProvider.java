package mod.bespectacled.modernbeta.api.world.chunk;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import mod.bespectacled.modernbeta.api.registry.ModernBetaRegistries;
import mod.bespectacled.modernbeta.api.world.blocksource.BlockSource;
import mod.bespectacled.modernbeta.api.world.chunk.surface.SurfaceBuilder;
import mod.bespectacled.modernbeta.api.world.spawn.SpawnLocator;
import mod.bespectacled.modernbeta.mixin.AccessorChunkGenerator;
import mod.bespectacled.modernbeta.mixin.AccessorPlacedFeature;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsChunk;
import mod.bespectacled.modernbeta.util.BlockStates;
import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.blocksource.BlockSourceDeepslate;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaGenerationStep;
import mod.bespectacled.modernbeta.world.feature.placement.NoiseBasedCountPlacementModifier;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.util.math.random.RandomSplitter;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.StructureWeightSampler;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.AquiferSampler.FluidLevel;
import net.minecraft.world.gen.chunk.AquiferSampler.FluidLevelSampler;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.util.PlacedFeatureIndexer.IndexedFeatures;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;

public abstract class ChunkProvider {    
    private final FluidLevelSampler defaultFluidLevelSampler;
    
    protected final ModernBetaChunkGenerator chunkGenerator;
    protected final long seed;
    
    protected final RegistryEntry<ChunkGeneratorSettings> generatorSettings;
    protected final ModernBetaSettingsChunk chunkSettings;
    protected final Random random;
    
    protected final ChunkRandom.RandomProvider randomProvider;
    protected final RandomSplitter randomSplitter;
    protected final BlockSourceDeepslate blockSourceDeepslate;
    
    protected final List<BlockSource> blockSources;
    protected final SurfaceBuilder surfaceBuilder;
    
    /**
     * Construct a Modern Beta chunk provider with seed and settings.
     * 
     * @param chunkGenerator Parent ModernBetaChunkGenerator object used to initialize fields.
     */
    public ChunkProvider(ModernBetaChunkGenerator chunkGenerator, long seed) {
        this.chunkGenerator = chunkGenerator;
        this.seed = seed;
        
        this.generatorSettings = chunkGenerator.getGeneratorSettings();
        this.chunkSettings = ModernBetaSettingsChunk.fromCompound(chunkGenerator.getChunkSettings());
        this.random = new Random(this.seed);

        this.defaultFluidLevelSampler = (x, y, z) -> new FluidLevel(this.getSeaLevel(), BlockStates.AIR);
        this.randomProvider = chunkGenerator.getGeneratorSettings().value().getRandomProvider();
        this.randomSplitter = this.randomProvider.create(this.seed).nextSplitter();
        this.blockSourceDeepslate = new BlockSourceDeepslate(this.chunkSettings, this.randomSplitter);
        
        this.blockSources = ModernBetaRegistries.BLOCKSOURCE
            .getEntries()
            .stream()
            .map(func -> func.apply(this.chunkSettings, this.randomSplitter))
            .toList();
        
        this.surfaceBuilder = new SurfaceBuilder(this.chunkGenerator.getBiomeSource());
    }
    
    /**
     * Generates base terrain for given chunk and returns it.
     * 
     * @param executor
     * @param blender TODO
     * @param structureAccessor
     * @param chunk
     * @param noiseConfig TODO
     * @return A completed chunk.
     */
    public abstract CompletableFuture<Chunk> provideChunk(Executor executor, Blender blender, StructureAccessor structureAccessor, Chunk chunk, NoiseConfig noiseConfig);
    
    /**
     * Generates biome-specific surface for given chunk.
     * 
     * @param region
     * @param structureAccessor TODO
     * @param chunk
     * @param biomeSource
     * @param noiseConfig TODO
     */
    public abstract void provideSurface(ChunkRegion region, StructureAccessor structureAccessor, Chunk chunk, ModernBetaBiomeSource biomeSource, NoiseConfig noiseConfig);
    
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
    public boolean skipChunk(int chunkX, int chunkZ, ModernBetaGenerationStep step) {
        if (step == ModernBetaGenerationStep.CARVERS) {
            return !this.chunkSettings.useCaves;
        }
        
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
    public AquiferSampler getAquiferSampler(Chunk chunk, NoiseConfig noiseConfig) {
        return AquiferSampler.seaLevel(this.defaultFluidLevelSampler);
    }
    
    /**
     * Get empty fluid level sampler.
     * 
     * @return Empty FluidLevelSampler.
     */
    public FluidLevelSampler getFluidLevelSampler() {
        return this.defaultFluidLevelSampler;
    }
    
    /**
     * @return Parent ModernBetaChunkGenerator.
     */
    public ModernBetaChunkGenerator getChunkGenerator() {
        return this.chunkGenerator;
    }
    
    /**
     * @return Chunk provider's spawn locator.
     */
    public SpawnLocator getSpawnLocator() {
        return SpawnLocator.DEFAULT;
    }

    /**
     * Sets forest density using PerlinOctaveNoise sampler created with world seed.
     * Checks every placed feature in the biome source feature list,
     * and if it uses ModernBetaNoiseBasedCountPlacementModifier, replaces the noise sampler.
     * 
     * @param forestOctaves PerlinOctaveNoise object used to set forest octaves.
     */
    public void initForestOctaveNoise() {
        List<IndexedFeatures> generationSteps = ((AccessorChunkGenerator)this.chunkGenerator).getIndexedFeaturesListSupplier().get();
        
        for (IndexedFeatures step : generationSteps) {
            List<PlacedFeature> featureList = step.features();
            
            for (PlacedFeature placedFeature : featureList) {
                AccessorPlacedFeature accessor = (AccessorPlacedFeature)(Object)placedFeature;
                List<PlacementModifier> modifiers = accessor.getPlacementModifiers();
                
                for (PlacementModifier modifier : modifiers) {
                    if (modifier instanceof NoiseBasedCountPlacementModifier noiseModifier) {
                        noiseModifier.setOctaves(this.getForestOctaveNoise());
                    }
                }
            }
        }
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
     * Creates a ModernBetaChunkNoiseSampler
     *
     */
    public ChunkNoiseSampler createChunkNoiseSampler(Chunk chunk, StructureAccessor world, Blender blender, NoiseConfig noiseConfig) {
        return ChunkNoiseSampler.create(
            chunk,
            noiseConfig,
            StructureWeightSampler.createStructureWeightSampler(world, chunk.getPos()),
            this.generatorSettings.value(),
            this.getFluidLevelSampler(),
            blender
        );
    }
    
    public ModernBetaSettingsChunk getChunkSettings() {
        return this.chunkSettings;
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
     * Get Perlin octave noise sampler for tree placement.
     * 
     * @return Perlin octave noise sampler.
     */
    protected PerlinOctaveNoise getForestOctaveNoise() {
        return new PerlinOctaveNoise(new Random(this.seed), 8, true);
    }
}