package com.bespectacled.modernbeta.world.gen;

import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.registry.Registries;
import com.bespectacled.modernbeta.api.world.gen.ChunkProvider;
import com.bespectacled.modernbeta.compat.Compat;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.biome.injector.BiomeInjector;
import com.bespectacled.modernbeta.world.structure.OceanShrineStructure;
import com.bespectacled.modernbeta.world.structure.OldStructures;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.class_6748;
import net.minecraft.block.BlockState;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Util;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.dynamic.RegistryLookupCodec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeCoords;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.carver.CarverContext;
import net.minecraft.world.gen.carver.CarvingMask;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;

public class OldChunkGenerator extends NoiseChunkGenerator {
    public static final Codec<OldChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            RegistryLookupCodec.of(Registry.NOISE_WORLDGEN).forGetter(generator -> generator.noiseRegistry),
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
            Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.worldSeed),
            ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("settings").forGetter(generator -> generator.settings),
            NbtCompound.CODEC.fieldOf("provider_settings").forGetter(generator -> generator.chunkProviderSettings))
        .apply(instance, instance.stable(OldChunkGenerator::new)));

    private final Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry;
    private final NbtCompound chunkProviderSettings;
    private final ChunkProvider chunkProvider;
    private final String chunkProviderType;
    
    private final boolean generateOceans;
    private final boolean generateOceanShrines;
    private final boolean generateMonuments;

    private final BiomeInjector biomeInjector;
    
    public OldChunkGenerator(Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry, BiomeSource biomeSource, long seed, Supplier<ChunkGeneratorSettings> settings, NbtCompound providerSettings) {
        super(noiseRegistry, biomeSource, seed, settings);
        
        this.noiseRegistry = noiseRegistry;
        this.chunkProviderSettings = providerSettings;
        this.chunkProviderType = NbtUtil.readStringOrThrow(NbtTags.WORLD_TYPE, providerSettings);
        this.chunkProvider = Registries.CHUNK.get(this.chunkProviderType).apply(this);

        this.generateOceans = !Compat.isLoaded("hydrogen") ? 
            NbtUtil.readBoolean(NbtTags.GEN_OCEANS, providerSettings, ModernBeta.GEN_CONFIG.infGenConfig.generateOceans) : 
            false;
        
        this.generateOceanShrines = NbtUtil.readBoolean(NbtTags.GEN_OCEAN_SHRINES, providerSettings, ModernBeta.GEN_CONFIG.infGenConfig.generateOceanShrines);
        this.generateMonuments = NbtUtil.readBoolean(NbtTags.GEN_MONUMENTS, providerSettings, ModernBeta.GEN_CONFIG.infGenConfig.generateMonuments);
        
        this.biomeInjector = this.biomeSource instanceof OldBiomeSource oldBiomeSource ?
            new BiomeInjector(this, oldBiomeSource) : 
            null;
    }

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR, ModernBeta.createId("old"), CODEC);
    }
    
    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return OldChunkGenerator.CODEC;
    }
    
    @Override
    public CompletableFuture<Chunk> populateBiomes(Registry<Biome> biomeRegistry, Executor executor, class_6748 blender, StructureAccessor accessor, Chunk chunk) {
        return CompletableFuture.<Chunk>supplyAsync(Util.debugSupplier("init_biomes", () -> {
            chunk.method_38257(this.biomeSource, this.getMultiNoiseSampler());
            return chunk;
            
        }), Util.getMainWorkerExecutor());
    }
    
    @Override
    public CompletableFuture<Chunk> populateNoise(Executor executor, class_6748 blender, StructureAccessor accessor, Chunk chunk) {
        CompletableFuture<Chunk> completedChunk = this.chunkProvider.provideChunk(executor, class_6748.method_39336(), accessor, chunk);
        
        return completedChunk;
    }
        
    @Override
    public void buildSurface(ChunkRegion region, StructureAccessor accessor, Chunk chunk) {
        if (!this.chunkProvider.skipChunk(chunk.getPos().x, chunk.getPos().z, ChunkStatus.SURFACE)) {
            if (this.biomeSource instanceof OldBiomeSource oldBiomeSource) {
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
    public void carve(ChunkRegion region, long seed, BiomeAccess access, StructureAccessor accessor, Chunk chunk, GenerationStep.Carver genCarver) {
        if (this.chunkProvider.skipChunk(chunk.getPos().x, chunk.getPos().z, ChunkStatus.CARVERS) || 
            this.chunkProvider.skipChunk(chunk.getPos().x, chunk.getPos().z, ChunkStatus.LIQUID_CARVERS)) return;

        BiomeAccess biomeAcc = access.withSource((biomeX, biomeY, biomeZ) -> this.biomeSource.getBiome(biomeX, biomeY, biomeZ, this.getMultiNoiseSampler()));
        ChunkPos chunkPos = chunk.getPos();

        int mainChunkX = chunkPos.x;
        int mainChunkZ = chunkPos.z;
        
        AquiferSampler aquiferSampler = this.chunkProvider.getAquiferSampler(chunk);
        
        // Chunk Noise Sampler used to sample surface level
        ChunkNoiseSampler chunkNoiseSampler = this.chunkProvider.getChunkNoiseSampler();
        
        CarverContext carverContext = new CarverContext(this, region.getRegistryManager(), chunk.getHeightLimitView(), chunkNoiseSampler);
        CarvingMask carvingMask = ((ProtoChunk)chunk).getOrCreateCarvingMask(genCarver);
        
        Random random = new Random(seed);
        long l = (random.nextLong() / 2L) * 2L + 1L;
        long l1 = (random.nextLong() / 2L) * 2L + 1L;

        for (int chunkX = mainChunkX - 8; chunkX <= mainChunkX + 8; ++chunkX) {
            for (int chunkZ = mainChunkZ - 8; chunkZ <= mainChunkZ + 8; ++chunkZ) {
                ChunkPos pos = new ChunkPos(chunkX, chunkZ);
                Chunk curChunk = region.getChunk(pos.x, pos.z);
                
                int startX = pos.getStartX();
                int startZ = pos.getStartZ();
                
                GenerationSettings genSettings = curChunk.method_38258(
                    () -> this.getInjectedBiomeAtBlock(
                            startX,
                            this.getHeight(startX, startZ, Heightmap.Type.OCEAN_FLOOR_WG),
                            startZ
                )).getGenerationSettings();
                
                List<Supplier<ConfiguredCarver<?>>> carverList = genSettings.getCarversForStep(genCarver);
                ListIterator<Supplier<ConfiguredCarver<?>>> carverIterator = carverList.listIterator();

                while (carverIterator.hasNext()) {
                    ConfiguredCarver<?> configuredCarver = carverIterator.next().get();
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
                    column[y] = this.settings.get().getDefaultFluid();
            } else {
                column[y] = this.defaultBlock;
            }
        }
        
        return new VerticalBlockSample(minY, column);
    }
    
    @Override
    public Pool<SpawnSettings.SpawnEntry> getEntitySpawnList(Biome biome, StructureAccessor structureAccessor, SpawnGroup spawnGroup, BlockPos blockPos) {
        if (spawnGroup == SpawnGroup.MONSTER) {
            if (structureAccessor.getStructureAt(blockPos, OldStructures.OCEAN_SHRINE_STRUCTURE).hasChildren()) {
                return OceanShrineStructure.getMonsterSpawns();
            }
        }

        return super.getEntitySpawnList(biome, structureAccessor, spawnGroup, blockPos);
    }

    @Override
    public int getWorldHeight() {
        // TODO: Causes issue with YOffset.BelowTop decorator (i.e. ORE_COAL_UPPER), find some workaround.
        // Affects both getWorldHeight() and getMinimumY().
        // See: MC-236933 and MC-236723
        if (this.chunkProvider == null)
            return this.getGeneratorSettings().get().getGenerationShapeConfig().height();
       
        return this.chunkProvider.getWorldHeight();
    }
    
    @Override
    public int getMinimumY() {
        if (this.chunkProvider == null)
            return this.getGeneratorSettings().get().getGenerationShapeConfig().minimumY();
        
        return this.chunkProvider.getWorldMinY();
    }

    @Override
    public int getSeaLevel() {
        return this.chunkProvider.getSeaLevel();
    }
    
    @Override
    public ChunkGenerator withSeed(long seed) {
        return new OldChunkGenerator(this.noiseRegistry, this.biomeSource.withSeed(seed), seed, this.settings, this.chunkProviderSettings);
    }
    
    @Override
    public Optional<BlockState> method_39041(CarverContext context, Function<BlockPos, Biome> function, Chunk chunk, ChunkNoiseSampler arg3, BlockPos arg4, boolean bl) {
        // TODO: Look more closely into this
        return this.chunkProvider.getSurfaceBuilder().method_39110(this.settings.get().getSurfaceRule(), context, function, chunk, arg3, arg4, bl);
    }
    
    public long getWorldSeed() {
        return this.worldSeed;
    }
    
    public Supplier<ChunkGeneratorSettings> getGeneratorSettings() {
        return this.settings;
    }
    
    public ChunkProvider getChunkProvider() {
        return this.chunkProvider;
    }
    
    public NbtCompound getProviderSettings() {
        return new NbtCompound().copyFrom(this.chunkProviderSettings);
    }
    
    public Registry<DoublePerlinNoiseSampler.NoiseParameters> getNoiseRegistry() {
        return this.noiseRegistry;
    }
    
    public BiomeInjector getBiomeInjector() {
        return this.biomeInjector;
    }
    
    public boolean generatesOceans() {
        return this.generateOceans;
    }
    
    public boolean generatesOceanShrines() {
        return this.generateOceanShrines;
    }
    
    public boolean generatesMonuments() {
        return this.generateMonuments;
    }
    
    public String getChunkProviderType() {
        return this.chunkProviderType;
    }
    
    public Biome getInjectedBiomeAtBlock(int x, int y, int z) {
        int biomeX = x >> 2;
        int biomeY = y >> 2;
        int biomeZ = z >> 2;
        
        int topHeight = this.biomeInjector.getCenteredHeight(biomeX, biomeZ);
        int minHeight = this.biomeInjector.sampleMinHeightAround(biomeX, biomeZ, topHeight);
        BlockState state = y < this.getSeaLevel() ? 
            this.settings.get().getDefaultFluid() :
            BlockStates.AIR;
        
        Biome biome = this.biomeInjector.test(
            y,
            topHeight,
            minHeight,
            state,
            biomeX, biomeY, biomeZ
        );
        
        return biome != null ? biome : this.getBiomeForNoiseGen(biomeX, biomeY, biomeZ);
    }
    
    public Set<Biome> getBiomesInArea(int startX, int startY, int startZ, int radius) {
        int minX = BiomeCoords.fromBlock(startX - radius);
        int minZ = BiomeCoords.fromBlock(startZ - radius);
        
        int maxX = BiomeCoords.fromBlock(startX + radius);
        int maxZ = BiomeCoords.fromBlock(startZ + radius);
        
        int rangeX = maxX - minX + 1;
        int rangeZ = maxZ - minZ + 1;
        
        HashSet<Biome> set = Sets.newHashSet();
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
}
