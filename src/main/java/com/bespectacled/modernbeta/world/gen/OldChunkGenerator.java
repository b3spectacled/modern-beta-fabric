package com.bespectacled.modernbeta.world.gen;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.registry.Registries;
import com.bespectacled.modernbeta.api.world.gen.ChunkProvider;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.util.settings.ImmutableSettings;
import com.bespectacled.modernbeta.util.settings.Settings;
import com.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import com.bespectacled.modernbeta.world.biome.injector.BiomeInjectionRules.BiomeInjectionContext;
import com.bespectacled.modernbeta.world.biome.injector.BiomeInjector;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.Util;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeCoords;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.StructureWeightSampler;
import net.minecraft.world.gen.carver.CarverContext;
import net.minecraft.world.gen.carver.CarvingMask;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;

public class OldChunkGenerator extends NoiseChunkGenerator {
    public static final Codec<OldChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> NoiseChunkGenerator.method_41042(instance).and(
        instance.group(
            RegistryOps.createRegistryCodec(Registry.NOISE_WORLDGEN).forGetter(generator -> generator.noiseRegistry),
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
            Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.seed),
            ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("settings").forGetter(generator -> generator.settings),
            ImmutableSettings.CODEC.fieldOf("provider_settings").forGetter(generator -> generator.chunkProviderSettings),
            Codec.INT.optionalFieldOf("version").forGetter(generator -> generator.version))
        ).apply(instance, instance.stable(OldChunkGenerator::new)));

    private final Registry<StructureSet> structuresRegistry;
    private final Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry;
    private final ImmutableSettings chunkProviderSettings;
    private final ChunkProvider chunkProvider;
    private final String chunkProviderType;
    private final Optional<Integer> version;

    private final BiomeInjector biomeInjector;
    
    public OldChunkGenerator(
        Registry<StructureSet> structuresRegistry,
        Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry,
        BiomeSource biomeSource,
        long seed,
        RegistryEntry<ChunkGeneratorSettings> settings,
        ImmutableSettings chunkProviderSettings,
        Optional<Integer> version
    ) {
        super(structuresRegistry, noiseRegistry, biomeSource, seed, settings);
        
        // Validate mod version
        ModernBeta.validateVersion(version);
        
        this.structuresRegistry = structuresRegistry;
        this.noiseRegistry = noiseRegistry;
        this.chunkProviderSettings = chunkProviderSettings;
        this.chunkProviderType = NbtUtil.toStringOrThrow(chunkProviderSettings.get(NbtTags.WORLD_TYPE));
        this.chunkProvider = Registries.CHUNK.get(this.chunkProviderType).apply(this);
        this.version = version;
    
        this.biomeInjector = this.biomeSource instanceof ModernBetaBiomeSource oldBiomeSource ?
            new BiomeInjector(this, oldBiomeSource) : 
            null;
    }

    @Override
    public CompletableFuture<Chunk> populateBiomes(Registry<Biome> biomeRegistry, Executor executor, Blender blender, StructureAccessor accessor, Chunk chunk) {
        return CompletableFuture.<Chunk>supplyAsync(Util.debugSupplier("init_biomes", () -> {
            chunk.populateBiomes(this.biomeSource, this.getMultiNoiseSampler());
            return chunk;
            
        }), Util.getMainWorkerExecutor());
    }
    
    @Override
    public CompletableFuture<Chunk> populateNoise(Executor executor, Blender blender, StructureAccessor accessor, Chunk chunk) {
        CompletableFuture<Chunk> completedChunk = this.chunkProvider.provideChunk(executor, Blender.getNoBlending(), accessor, chunk);
        
        return completedChunk;
    }
        
    @Override
    public void buildSurface(ChunkRegion region, StructureAccessor accessor, Chunk chunk) {
        if (!this.chunkProvider.skipChunk(chunk.getPos().x, chunk.getPos().z, ChunkStatus.SURFACE)) {
            if (this.biomeSource instanceof ModernBetaBiomeSource oldBiomeSource) {
                this.chunkProvider.provideSurface(region, chunk, oldBiomeSource);
            } else {
                super.buildSurface(region, accessor, chunk);
            }
        }
        
        // Inject cave, ocean, etc. biomes
        if (this.biomeInjector != null) {
            this.biomeInjector.injectBiomes(chunk);
        }
    }
    
    @Override
    public void carve(ChunkRegion region, long seed, BiomeAccess access, StructureAccessor accessor, Chunk chunk, GenerationStep.Carver genStep) {
        if (this.chunkProvider.skipChunk(chunk.getPos().x, chunk.getPos().z, ChunkStatus.CARVERS) || 
            this.chunkProvider.skipChunk(chunk.getPos().x, chunk.getPos().z, ChunkStatus.LIQUID_CARVERS)) return;

        BiomeAccess biomeAcc = access.withSource((biomeX, biomeY, biomeZ) -> this.biomeSource.getBiome(biomeX, biomeY, biomeZ, this.getMultiNoiseSampler()));
        ChunkPos chunkPos = chunk.getPos();

        int mainChunkX = chunkPos.x;
        int mainChunkZ = chunkPos.z;
        
        AquiferSampler aquiferSampler = this.chunkProvider.getAquiferSampler(chunk);
        
        // Chunk Noise Sampler used to sample surface level
        ChunkNoiseSampler chunkNoiseSampler = chunk.getOrCreateChunkNoiseSampler(
            this.getNoiseRouter(),
            () -> new StructureWeightSampler(accessor, chunk),
            this.settings.value(),
            this.chunkProvider.getFluidLevelSampler(),
            Blender.getBlender(region)
        );
        
        CarverContext carverContext = new CarverContext(this, region.getRegistryManager(), chunk.getHeightLimitView(), chunkNoiseSampler);
        CarvingMask carvingMask = ((ProtoChunk)chunk).getOrCreateCarvingMask(genStep);
        
        Random random = new Random(seed);
        long l = (random.nextLong() / 2L) * 2L + 1L;
        long l1 = (random.nextLong() / 2L) * 2L + 1L;

        for (int chunkX = mainChunkX - 8; chunkX <= mainChunkX + 8; ++chunkX) {
            for (int chunkZ = mainChunkZ - 8; chunkZ <= mainChunkZ + 8; ++chunkZ) {
                ChunkPos pos = new ChunkPos(chunkX, chunkZ);
                Chunk carverChunk = region.getChunk(pos.x, pos.z);
                
                int startX = pos.getStartX();
                int startZ = pos.getStartZ();
                
                @SuppressWarnings("deprecation")
                GenerationSettings genSettings = carverChunk.setBiomeIfAbsent(
                    () -> this.getInjectedBiomeAtBlock(
                        startX,
                        this.getHeight(startX, startZ, Heightmap.Type.OCEAN_FLOOR_WG),
                        startZ
                )).value().getGenerationSettings();
                Iterable<RegistryEntry<ConfiguredCarver<?>>> carverList = genSettings.getCarversForStep(genStep);

                for(RegistryEntry<ConfiguredCarver<?>> carverEntry : carverList) {
                    ConfiguredCarver<?> configuredCarver = carverEntry.value();
                    random.setSeed((long) chunkX * l + (long) chunkZ * l1 ^ seed);
                    
                    if (configuredCarver.shouldCarve(random)) {
                        configuredCarver.carve(carverContext, chunk, biomeAcc::getBiome, random, aquiferSampler, pos, carvingMask);
                    }
                }
            }
        }
    }
    
    @Override
    public void generateFeatures(StructureWorldAccess world, Chunk chunk, StructureAccessor structureAccessor) {
        ChunkPos pos = chunk.getPos();
        
        if (this.chunkProvider.skipChunk(pos.x, pos.z, ChunkStatus.FEATURES)) 
            return;

        super.generateFeatures(world, chunk, structureAccessor);
    }
    
    @Override
    public int getHeight(int x, int z, Heightmap.Type type, HeightLimitView world) {
        return this.chunkProvider.getHeight(x, z, type);
    }
    
    public int getHeight(int x, int z, Heightmap.Type type) {
        return this.chunkProvider.getHeight(x, z, type);
    }
  
    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world) {
        int height = this.chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG);
        int worldHeight = this.chunkProvider.getWorldHeight();
        int minY = this.chunkProvider.getWorldMinY();
        
        BlockState[] column = new BlockState[worldHeight];
        
        for (int y = worldHeight - 1; y >= 0; --y) {
            int worldY = y + minY;
            
            if (worldY > height) {
                if (worldY > this.getSeaLevel())
                    column[y] = BlockStates.AIR;
                else
                    column[y] = this.settings.value().defaultFluid();
            } else {
                column[y] = this.defaultBlock;
            }
        }
        
        return new VerticalBlockSample(minY, column);
    }

    @Override
    public int getWorldHeight() {
        // TODO: Causes issue with YOffset.BelowTop decorator (i.e. ORE_COAL_UPPER), find some workaround.
        // Affects both getWorldHeight() and getMinimumY().
        // See: MC-236933 and MC-236723
        if (this.chunkProvider == null)
            return this.getGeneratorSettings().value().generationShapeConfig().height();
       
        return this.chunkProvider.getWorldHeight();
    }
    
    @Override
    public int getMinimumY() {
        if (this.chunkProvider == null)
            return this.getGeneratorSettings().value().generationShapeConfig().minimumY();
        
        return this.chunkProvider.getWorldMinY();
    }

    @Override
    public int getSeaLevel() {
        return this.chunkProvider.getSeaLevel();
    }
    
    @Override
    public ChunkGenerator withSeed(long seed) {
        return new OldChunkGenerator(
            this.structuresRegistry,
            this.noiseRegistry,
            this.biomeSource.withSeed(seed),
            seed,
            this.settings,
            this.chunkProviderSettings,
            this.version
        );
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public Optional<BlockState> applyMaterialRule(CarverContext context, Function<BlockPos, RegistryEntry<Biome>> function, Chunk chunk, ChunkNoiseSampler arg3, BlockPos arg4, boolean bl) {
        // TODO: Look more closely into this
        return this.chunkProvider.getSurfaceBuilder().applyMaterialRule(this.settings.value().surfaceRule(), context, function, chunk, arg3, arg4, bl);
    }
    
    public long getWorldSeed() {
        return this.seed;
    }
    
    public RegistryEntry<ChunkGeneratorSettings> getGeneratorSettings() {
        return this.settings;
    }
    
    public ChunkProvider getChunkProvider() {
        return this.chunkProvider;
    }
    
    public Settings getChunkSettings() {
        return this.chunkProviderSettings;
    }
    
    public Registry<DoublePerlinNoiseSampler.NoiseParameters> getNoiseRegistry() {
        return this.noiseRegistry;
    }
    
    public BiomeInjector getBiomeInjector() {
        return this.biomeInjector;
    }
    
    public boolean generatesOceanShrines() {
        return NbtUtil.toBoolean(this.chunkProviderSettings.get(NbtTags.GEN_OCEAN_SHRINES), ModernBeta.GEN_CONFIG.generateOceanShrines);
    }
    
    public boolean generatesMonuments() {
        return NbtUtil.toBoolean(this.chunkProviderSettings.get(NbtTags.GEN_MONUMENTS), ModernBeta.GEN_CONFIG.generateMonuments);
    }
    
    public String getChunkProviderType() {
        return this.chunkProviderType;
    }
    
    public RegistryEntry<Biome> getInjectedBiomeAtBlock(int x, int y, int z) {
        int biomeX = x >> 2;
        int biomeY = y >> 2;
        int biomeZ = z >> 2;
        
        int worldMinY = this.chunkProvider.getWorldMinY();
        int topHeight = this.biomeInjector.getCenteredHeight(biomeX, biomeZ);
        int minHeight = this.biomeInjector.sampleMinHeightAround(biomeX, biomeZ);
        BlockState state = y < this.getSeaLevel() ? 
            this.settings.value().defaultFluid() :
            BlockStates.AIR;
        
        BiomeInjectionContext context = new BiomeInjectionContext(worldMinY, topHeight, minHeight, state, state).setY(y);
        RegistryEntry<Biome> biome = this.biomeInjector.sample(context, biomeX, biomeY, biomeZ);
        
        return biome != null ? biome : this.getBiomeForNoiseGen(biomeX, biomeY, biomeZ);
    }
    
    public Set<RegistryEntry<Biome>> getBiomesInArea(int startX, int startY, int startZ, int radius) {
        int minX = BiomeCoords.fromBlock(startX - radius);
        int minZ = BiomeCoords.fromBlock(startZ - radius);
        
        int maxX = BiomeCoords.fromBlock(startX + radius);
        int maxZ = BiomeCoords.fromBlock(startZ + radius);
        
        int rangeX = maxX - minX + 1;
        int rangeZ = maxZ - minZ + 1;
        
        HashSet<RegistryEntry<Biome>> set = Sets.newHashSet();
        for (int localZ = 0; localZ < rangeZ; ++localZ) {
            for (int localX = 0; localX < rangeX; ++localX) {
                int biomeX = minX + localX;
                int biomeZ = minZ + localZ;
                
                int x = biomeX << 2;
                int z = biomeZ << 2;
                int y = this.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG);
                
                set.add(this.getInjectedBiomeAtBlock(x, y, z));
            }
        }
        
        return set;
    }

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR, ModernBeta.createId("old"), CODEC);
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return OldChunkGenerator.CODEC;
    }
}
