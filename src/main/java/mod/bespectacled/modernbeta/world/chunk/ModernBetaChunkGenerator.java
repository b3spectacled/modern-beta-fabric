package mod.bespectacled.modernbeta.world.chunk;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.api.registry.ModernBetaRegistries;
import mod.bespectacled.modernbeta.api.world.chunk.ChunkProvider;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsChunk;
import mod.bespectacled.modernbeta.util.BlockStates;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.biome.injector.BiomeInjector;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Util;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
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
import net.minecraft.world.gen.noise.NoiseConfig;

public class ModernBetaChunkGenerator extends NoiseChunkGenerator {
    public static final Codec<ModernBetaChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
            ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("settings").forGetter(generator -> generator.settings),
            NbtCompound.CODEC.fieldOf("provider_settings").forGetter(generator -> generator.chunkSettings)
        ).apply(instance, instance.stable(ModernBetaChunkGenerator::new)));

    private final RegistryEntry<ChunkGeneratorSettings> settings;
    private final NbtCompound chunkSettings;
    private final BiomeInjector biomeInjector;
    
    private ChunkProvider chunkProvider;
    
    public ModernBetaChunkGenerator(
        BiomeSource biomeSource,
        RegistryEntry<ChunkGeneratorSettings> settings,
        NbtCompound chunkProviderSettings
    ) {
        super(biomeSource, settings);
        
        this.settings = settings;
        this.chunkSettings = chunkProviderSettings;
        this.biomeInjector = this.biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource ?
            new BiomeInjector(this, modernBetaBiomeSource) : 
            null;
        
        if (this.biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
            modernBetaBiomeSource.setChunkGenerator(this);
        }
    }

    public void initProvider(long seed) {
        ModernBetaSettingsChunk chunkSettings = new ModernBetaSettingsChunk.Builder(this.chunkSettings).build();
        
        this.chunkProvider = ModernBetaRegistries.CHUNK
            .get(chunkSettings.chunkProvider)
            .apply(this, seed);
    }

    @Override
    public CompletableFuture<Chunk> populateBiomes(Executor executor, NoiseConfig noiseConfig, Blender blender, StructureAccessor structureAccessor, Chunk chunk) {
        return CompletableFuture.<Chunk>supplyAsync(Util.debugSupplier("init_biomes", () -> {
            ChunkNoiseSampler noiseSampler = chunk.getOrCreateChunkNoiseSampler(c -> this.createChunkNoiseSampler(c, structureAccessor, blender, noiseConfig));
            chunk.populateBiomes(this.biomeSource, noiseSampler.createMultiNoiseSampler(noiseConfig.getNoiseRouter(), this.settings.value().spawnTarget()));
            
            return chunk;
            
        }), Util.getMainWorkerExecutor());
    }
    
    @Override
    public CompletableFuture<Chunk> populateNoise(Executor executor, Blender blender, NoiseConfig noiseConfig, StructureAccessor structureAccessor, Chunk chunk) {
        CompletableFuture<Chunk> completedChunk = this.chunkProvider.provideChunk(executor, Blender.getNoBlending(), structureAccessor, chunk, noiseConfig);
        
        return completedChunk;
    }
        
    @Override
    public void buildSurface(ChunkRegion chunkRegion, StructureAccessor structureAccessor, NoiseConfig noiseConfig, Chunk chunk) {
        if (!this.chunkProvider.skipChunk(chunk.getPos().x, chunk.getPos().z, ChunkStatus.SURFACE)) {
            if (this.biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
                this.chunkProvider.provideSurface(chunkRegion, chunk, modernBetaBiomeSource, noiseConfig);
            } else {
                super.buildSurface(chunkRegion, structureAccessor, noiseConfig, chunk);
            }
        }
        
        // Inject cave, ocean, etc. biomes
        if (this.biomeInjector != null) {
            this.biomeInjector.injectBiomes(chunk);
        }
    }
    
    @Override
    public void carve(ChunkRegion chunkRegion, long seed, NoiseConfig noiseConfig, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk, GenerationStep.Carver carverStep) {
        if (this.chunkProvider.skipChunk(chunk.getPos().x, chunk.getPos().z, ChunkStatus.CARVERS) || 
            this.chunkProvider.skipChunk(chunk.getPos().x, chunk.getPos().z, ChunkStatus.LIQUID_CARVERS)) return;

        BiomeAccess biomeAccessWithSource = biomeAccess.withSource((biomeX, biomeY, biomeZ) -> this.biomeSource.getBiome(biomeX, biomeY, biomeZ, noiseConfig.getMultiNoiseSampler()));
        ChunkPos chunkPos = chunk.getPos();

        int mainChunkX = chunkPos.x;
        int mainChunkZ = chunkPos.z;
        
        AquiferSampler aquiferSampler = this.chunkProvider.getAquiferSampler(chunk, noiseConfig);
        
        // Chunk Noise Sampler used to sample surface level
        ChunkNoiseSampler chunkNoiseSampler = chunk.getOrCreateChunkNoiseSampler(c -> this.createChunkNoiseSampler(c, structureAccessor, Blender.getBlender(chunkRegion), noiseConfig));
        
        CarverContext carverContext = new CarverContext(this, chunkRegion.getRegistryManager(), chunk.getHeightLimitView(), chunkNoiseSampler, noiseConfig, this.settings.value().surfaceRule());
        CarvingMask carvingMask = ((ProtoChunk)chunk).getOrCreateCarvingMask(carverStep);
        
        LocalRandom random = new LocalRandom(seed);
        long l = (random.nextLong() / 2L) * 2L + 1L;
        long l1 = (random.nextLong() / 2L) * 2L + 1L;

        for (int chunkX = mainChunkX - 8; chunkX <= mainChunkX + 8; ++chunkX) {
            for (int chunkZ = mainChunkZ - 8; chunkZ <= mainChunkZ + 8; ++chunkZ) {
                ChunkPos carverPos = new ChunkPos(chunkX, chunkZ);
                Chunk carverChunk = chunkRegion.getChunk(carverPos.x, carverPos.z);
                
                @SuppressWarnings("deprecation")
                GenerationSettings genSettings = carverChunk.getOrCreateGenerationSettings(() -> this.getGenerationSettings(
                    this.biomeSource.getBiome(BiomeCoords.fromBlock(carverPos.getStartX()), 0, BiomeCoords.fromBlock(carverPos.getStartZ()), noiseConfig.getMultiNoiseSampler()))
                );
                Iterable<RegistryEntry<ConfiguredCarver<?>>> carverList = genSettings.getCarversForStep(carverStep);

                for(RegistryEntry<ConfiguredCarver<?>> carverEntry : carverList) {
                    ConfiguredCarver<?> configuredCarver = carverEntry.value();
                    random.setSeed((long) chunkX * l + (long) chunkZ * l1 ^ seed);
                    
                    if (configuredCarver.shouldCarve(random)) {
                        configuredCarver.carve(carverContext, chunk, biomeAccessWithSource::getBiome, random, aquiferSampler, carverPos, carvingMask);
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
    public int getHeight(int x, int z, Heightmap.Type type, HeightLimitView world, NoiseConfig noiseConfig) {
        return this.chunkProvider.getHeight(x, z, type);
    }
    
    public int getHeight(int x, int z, Heightmap.Type type) {
        return this.chunkProvider.getHeight(x, z, type);
    }
  
    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world, NoiseConfig noiseConfig) {
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
                column[y] = this.settings.value().defaultBlock();
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
    
    public RegistryEntry<ChunkGeneratorSettings> getGeneratorSettings() {
        return this.settings;
    }
    
    public ChunkProvider getChunkProvider() {
        return this.chunkProvider;
    }
    
    public NbtCompound getChunkSettings() {
        return this.chunkSettings;
    }
    
    public BiomeInjector getBiomeInjector() {
        return this.biomeInjector;
    }

    public static void register() {
        Registry.register(net.minecraft.registry.Registries.CHUNK_GENERATOR, ModernBeta.createId(ModernBeta.MOD_ID), CODEC);
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return ModernBetaChunkGenerator.CODEC;
    }
    
    public ChunkNoiseSampler createChunkNoiseSampler(Chunk chunk, StructureAccessor world, Blender blender, NoiseConfig noiseConfig) {
        return ChunkNoiseSampler.create(
            chunk,
            noiseConfig,
            StructureWeightSampler.createStructureWeightSampler(world, chunk.getPos()),
            this.settings.value(),
            this.chunkProvider.getFluidLevelSampler(),
            blender
        );
    }
}
