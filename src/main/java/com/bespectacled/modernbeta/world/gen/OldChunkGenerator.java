package com.bespectacled.modernbeta.world.gen;

import java.util.BitSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.registry.ProviderRegistries;
import com.bespectacled.modernbeta.api.world.gen.ChunkProvider;
import com.bespectacled.modernbeta.mixin.MixinChunkGeneratorInvoker;
import com.bespectacled.modernbeta.mixin.MixinConfiguredCarverAccessor;
import com.bespectacled.modernbeta.util.NBTUtil;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.carver.IOldCaveCarver;
import com.bespectacled.modernbeta.world.feature.OldFeatures;
import com.bespectacled.modernbeta.world.structure.OldStructures;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.class_6350;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.Util;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverContext;
import net.minecraft.world.gen.carver.CaveCarverConfig;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.StructureFeature;

public class OldChunkGenerator extends NoiseChunkGenerator {
    public static final Codec<OldChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
            Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.worldSeed),
            ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("settings").forGetter(generator -> generator.settings),
            NbtCompound.CODEC.fieldOf("provider_settings").forGetter(generator -> generator.chunkProviderSettings))
        .apply(instance, instance.stable(OldChunkGenerator::new)));
    
    private static final int OCEAN_Y_CUT_OFF = 40;
    
    private final OldBiomeSource biomeSource;
    private final NbtCompound chunkProviderSettings;
    
    private final String chunkProviderType;
    private final boolean generateOceans;
    
    private final ChunkProvider chunkProvider;
    private final Random random;
    
    public OldChunkGenerator(BiomeSource biomeSource, long seed, Supplier<ChunkGeneratorSettings> settings, NbtCompound providerSettings) {
        super(biomeSource, seed, settings);
        
        this.random = new Random(seed);
        
        this.biomeSource = (OldBiomeSource)biomeSource;

        this.chunkProviderSettings = providerSettings;
        this.chunkProviderType = NBTUtil.readStringOrThrow("worldType", providerSettings);
        this.chunkProvider = ProviderRegistries.CHUNK.get(this.chunkProviderType).apply(seed, this, settings, providerSettings);
        
        this.generateOceans = NBTUtil.readBoolean("generateOceans", providerSettings, true);
    }

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR, ModernBeta.createId("old"), CODEC);
    }
    
    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return OldChunkGenerator.CODEC;
    }
    
    @Override
    public CompletableFuture<Chunk> populateNoise(Executor executor, StructureAccessor accessor, Chunk chunk) {   
        return CompletableFuture.<Chunk>supplyAsync(
            () -> this.chunkProvider.provideChunk(accessor, chunk), Util.getMainWorkerExecutor()
        );
    }
        
    @Override
    public void buildSurface(ChunkRegion region, Chunk chunk) {
        if (!this.chunkProvider.skipChunk(chunk.getPos().x, chunk.getPos().z, ChunkStatus.SURFACE))  
            this.chunkProvider.provideSurface(region, chunk, this.biomeSource);
        
        if (this.generateOceans) {
            OldGeneratorUtil.replaceOceansInChunk(chunk, this.biomeSource, this.getWorldHeight(), this.getMinimumY(), this.getSeaLevel(), OCEAN_Y_CUT_OFF);
        }
    }

    @Override
    public void generateFeatures(ChunkRegion region, StructureAccessor accessor) {
        OldFeatures.OLD_FANCY_OAK.chunkReset();
        if (this.chunkProvider.skipChunk(region.getCenterPos().x, region.getCenterPos().z, ChunkStatus.FEATURES)) return;
        
        ChunkPos chunkPos = region.getCenterPos();
        int startX = chunkPos.getStartX();
        int startZ = chunkPos.getStartZ();
        
        Biome biome = OldGeneratorUtil.getOceanBiome(region.getChunk(chunkPos.x, chunkPos.z), this, biomeSource, generateOceans, this.getSeaLevel());
        
        
        // TODO: Remove chunkRandom at some point
        ChunkRandom chunkRandom = new ChunkRandom();
        long popSeed = chunkRandom.setPopulationSeed(region.getSeed(), startX, startZ);

        try {
            biome.generateFeatureStep(accessor, this, region, popSeed, chunkRandom, new BlockPos(startX, region.getBottomY(), startZ));
        } catch (Exception exception) {
            CrashReport report = CrashReport.create(exception, "Biome decoration");
            report.addElement("Generation").add("CenterX", chunkPos.x).add("CenterZ", chunkPos.z).add("Seed", popSeed).add("Biome", biome);
            throw new CrashException(report);
        }
    }
    
    @Override
    public void carve(long seed, BiomeAccess access, Chunk chunk, GenerationStep.Carver genCarver) {
        if (this.chunkProvider.skipChunk(chunk.getPos().x, chunk.getPos().z, ChunkStatus.CARVERS) || 
            this.chunkProvider.skipChunk(chunk.getPos().x, chunk.getPos().z, ChunkStatus.LIQUID_CARVERS)) return;
        
        BiomeAccess biomeAcc = access.withSource(this.biomeSource);
        ChunkPos chunkPos = chunk.getPos();

        int mainChunkX = chunkPos.x;
        int mainChunkZ = chunkPos.z;

        Biome biome = OldGeneratorUtil.getOceanBiome(chunk, this, this.biomeSource, this.generateOceans, this.getSeaLevel());
        GenerationSettings genSettings = biome.getGenerationSettings();
        CarverContext heightContext = new CarverContext(this);
        
        class_6350 class_635013 = this.method_36380(chunk);
        BitSet bitSet = ((ProtoChunk)chunk).getOrCreateCarvingMask(genCarver);

        this.random.setSeed(seed);
        long l = (this.random.nextLong() / 2L) * 2L + 1L;
        long l1 = (this.random.nextLong() / 2L) * 2L + 1L;

        for (int chunkX = mainChunkX - 8; chunkX <= mainChunkX + 8; ++chunkX) {
            for (int chunkZ = mainChunkZ - 8; chunkZ <= mainChunkZ + 8; ++chunkZ) {
                List<Supplier<ConfiguredCarver<?>>> carverList = genSettings.getCarversForStep(genCarver);
                ListIterator<Supplier<ConfiguredCarver<?>>> carverIterator = carverList.listIterator();
                ChunkPos caveChunkPos = new ChunkPos(chunkX, chunkZ);

                while (carverIterator.hasNext()) {
                    ConfiguredCarver<?> configuredCarver = carverIterator.next().get();
                    Carver<?> carver = ((MixinConfiguredCarverAccessor)configuredCarver).getCarver();
                    
                    this.random.setSeed((long) chunkX * l + (long) chunkZ * l1 ^ seed);
                    
                    // Special case for old Beta carvers.
                    if (carver instanceof IOldCaveCarver) {
                        ((IOldCaveCarver)carver).carve(heightContext, (CaveCarverConfig)configuredCarver.getConfig(), chunk, this.random, chunkX, chunkZ, mainChunkX, mainChunkZ);
                        
                    } else if (configuredCarver.shouldCarve(random)) {
                        configuredCarver.carve(heightContext, chunk, biomeAcc::getBiome, this.random, class_635013, caveChunkPos, bitSet);

                    }
                }
            }
        }
    }
    
    @Override
    public void setStructureStarts(
        DynamicRegistryManager dynamicRegistryManager, 
        StructureAccessor structureAccessor,   
        Chunk chunk, 
        StructureManager structureManager, 
        long seed
    ) {
        if (this.chunkProvider.skipChunk(chunk.getPos().x, chunk.getPos().z, ChunkStatus.STRUCTURE_STARTS)) return;
        
        Biome biome = OldGeneratorUtil.getOceanBiome(chunk, this, this.biomeSource, this.generateOceans, this.getSeaLevel());

        ((MixinChunkGeneratorInvoker)this).invokeSetStructureStart(
            ConfiguredStructureFeatures.STRONGHOLD, 
            dynamicRegistryManager, 
            structureAccessor, 
            chunk,
            structureManager, 
            seed, 
            biome
        );
        
        for (final Supplier<ConfiguredStructureFeature<?, ?>> supplier : biome.getGenerationSettings()
                .getStructureFeatures()) {
            ((MixinChunkGeneratorInvoker)this).invokeSetStructureStart(
                supplier.get(),
                dynamicRegistryManager, 
                structureAccessor,
                chunk, 
                structureManager,
                seed,
                biome
            );
        }
    }
    
    @Override
    public int getHeight(int x, int z, Heightmap.Type type, HeightLimitView world) {
        return this.chunkProvider.getHeight(x, z, type);
    }
    
    /*
    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world) {
        int bottomY = Math.max(this.settings.generatorSettings.get().getGenerationShapeConfig().getMinimumY(), world.getBottomY());
        int topY = Math.min(this.settings.generatorSettings.get().getGenerationShapeConfig().getMinimumY() + this.settings.generatorSettings.get().getGenerationShapeConfig().getHeight(), world.getTopY());
        
        //int noiseBottomY = MathHelper.floorDiv(bottomY, this.chunkProvider.getVerticalNoiseResolution());
        int noiseTopY = MathHelper.floorDiv(topY - bottomY, this.chunkProvider.getVerticalNoiseResolution());
        
        if (noiseTopY <= 0) {
            return new VerticalBlockSample(bottomY, new BlockState[0]);
        }
        
        BlockState[] states = new BlockState[noiseTopY * this.chunkProvider.getVerticalNoiseResolution()];
        int sampledHeight = this.chunkProvider.getHeight(x, z, null);
        
        for (int y = 0; y < noiseTopY * this.chunkProvider.getVerticalNoiseResolution(); ++y) {
            y += bottomY;
            
            BlockState state = BlockStates.AIR;
            if (y < sampledHeight) {
                state = BlockStates.STONE;
            }
            
            states[y] = state;
        }
        
        return new VerticalBlockSample(bottomY, states);
    }*/
    
    @Override
    public BlockPos locateStructure(ServerWorld world, StructureFeature<?> feature, BlockPos center, int radius, boolean skipExistingChunks) {
        if (!this.generateOceans)
            if (feature.equals(StructureFeature.OCEAN_RUIN) || 
                feature.equals(StructureFeature.SHIPWRECK) || 
                feature.equals(StructureFeature.BURIED_TREASURE) ||
                feature.equals(OldStructures.OCEAN_SHRINE_STRUCTURE)) {
                return null;
            }

        return super.locateStructure(world, feature, center, radius, skipExistingChunks);
    }
    
    @Override
    public Pool<SpawnSettings.SpawnEntry> getEntitySpawnList(Biome biome, StructureAccessor structureAccessor, SpawnGroup spawnGroup, BlockPos blockPos) {
        if (spawnGroup == SpawnGroup.MONSTER) {
            if (structureAccessor.getStructureAt(blockPos, false, OldStructures.OCEAN_SHRINE_STRUCTURE).hasChildren()) {
                return OldStructures.OCEAN_SHRINE_STRUCTURE.getMonsterSpawns();
            }
        }

        return super.getEntitySpawnList(biome, structureAccessor, spawnGroup, blockPos);
    }

    @Override
    public int getWorldHeight() {
        // TODO: Causes issue with YOffset.BelowTop decorator (i.e. ORE_COAL_UPPER), find some workaround.
        return this.chunkProvider.getWorldHeight();
    }
    
    @Override
    public int getMinimumY() {
        return this.chunkProvider.getMinimumY();
    }

    @Override
    public int getSeaLevel() {
        return chunkProvider.getSeaLevel();
    }
    
    @Override
    public ChunkGenerator withSeed(long seed) {
        return new OldChunkGenerator(this.biomeSource.withSeed(seed), seed, this.settings, this.chunkProviderSettings);
    }
    
    public boolean isProviderInstanceOf(Class<?> c) {
        return c.isInstance(this.chunkProvider);
    }
    
    public ChunkProvider getChunkProvider() {
        return this.chunkProvider;
    }
    
    public NbtCompound getProviderSettings() {
        return this.chunkProviderSettings;
    }
    
    /*
    public static void export() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path dir = Paths.get("..\\src\\main\\resources\\data\\modern_beta\\dimension");
        
        NbtCompound chunkSettings = OldGeneratorSettings.createInfSettings(OldChunkProviderType.BETA);
        NbtCompound biomeSettings = OldGeneratorSettings.createBiomeSettings(OldBiomeProviderType.BETA, CaveBiomeType.VANILLA, BetaBiomes.FOREST_ID);
        
        OldBiomeSource biomeSource = new OldBiomeSource(0, BuiltinRegistries.BIOME, biomeSettings);
        OldChunkGenerator chunkGenerator = new OldChunkGenerator(biomeSource, 0, () -> OldGeneratorSettings.BETA_GENERATOR_SETTINGS, chunkSettings);
        Function<OldChunkGenerator, DataResult<JsonElement>> toJson = JsonOps.INSTANCE.withEncoder(OldChunkGenerator.CODEC);
        
        try {
            JsonElement json = toJson.apply(chunkGenerator).result().get();
            Files.write(dir.resolve(ModernBeta.createId("old").getPath() + ".json"), gson.toJson(json).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            ModernBeta.LOGGER.error("[Modern Beta] Couldn't serialize old chunk generator!");
            e.printStackTrace();
        }
    }
    */
}
