package com.bespectacled.modernbeta.world.gen;

import java.util.BitSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.registry.Registries;
import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.api.world.gen.ChunkProvider;
import com.bespectacled.modernbeta.mixin.MixinChunkGeneratorInvoker;
import com.bespectacled.modernbeta.util.NBTUtil;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.GenUtil;
import com.bespectacled.modernbeta.util.mutable.MutableBiomeArray;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.structure.OldStructures;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
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
import net.minecraft.world.gen.carver.CarverContext;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
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
    private static final int OCEAN_MIN_DEPTH = 4;
    
    private final BiomeSource biomeSource;
    
    private final NbtCompound chunkProviderSettings;
    private final ChunkProvider chunkProvider;
    private final String chunkProviderType;

    private final boolean generateOceans;
    private final boolean generateOceanShrines;
    
    public OldChunkGenerator(BiomeSource biomeSource, long seed, Supplier<ChunkGeneratorSettings> settings, NbtCompound providerSettings) {
        super(biomeSource, seed, settings);
        
        this.biomeSource = biomeSource;
        
        this.chunkProviderSettings = providerSettings;
        this.chunkProviderType = NBTUtil.readStringOrThrow(WorldSettings.TAG_WORLD, providerSettings);
        this.chunkProvider = Registries.CHUNK.get(this.chunkProviderType).apply(this);
        
        this.generateOceans = NBTUtil.readBoolean("generateOceans", providerSettings, ModernBeta.GEN_CONFIG.generateOceans);
        this.generateOceanShrines = NBTUtil.readBoolean("generateOceanShrines", providerSettings, ModernBeta.GEN_CONFIG.generateOceanShrines);
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
            if (this.biomeSource instanceof OldBiomeSource oldBiomeSource)
                this.chunkProvider.provideSurface(region, chunk, oldBiomeSource);
            else
                super.buildSurface(region, chunk);
        
        if (this.generateOceans && this.biomeSource instanceof OldBiomeSource oldBiomeSource)
            this.replaceOceansInChunk(oldBiomeSource, chunk);
    }

    @Override
    public void generateFeatures(ChunkRegion region, StructureAccessor accessor) {
        if (this.chunkProvider.skipChunk(region.getCenterPos().x, region.getCenterPos().z, ChunkStatus.FEATURES)) return;
        
        ChunkPos chunkPos = region.getCenterPos();
        int startX = chunkPos.getStartX();
        int startZ = chunkPos.getStartZ();
        
        Biome biome = this.getBiomeAt(startX, 0, startZ, region.getChunk(chunkPos.x, chunkPos.z));
        
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

        Biome biome = this.getBiomeAt(chunkPos.getStartX(), 0, chunkPos.getStartZ(), chunk);
        GenerationSettings genSettings = biome.getGenerationSettings();
        CarverContext heightContext = new CarverContext(this, chunk);
        
        AquiferSampler aquiferSampler = this.createAquiferSampler(chunk);
        BitSet bitSet = ((ProtoChunk)chunk).getOrCreateCarvingMask(genCarver);

        Random random = new Random(seed);
        long l = (random.nextLong() / 2L) * 2L + 1L;
        long l1 = (random.nextLong() / 2L) * 2L + 1L;

        for (int chunkX = mainChunkX - 8; chunkX <= mainChunkX + 8; ++chunkX) {
            for (int chunkZ = mainChunkZ - 8; chunkZ <= mainChunkZ + 8; ++chunkZ) {
                List<Supplier<ConfiguredCarver<?>>> carverList = genSettings.getCarversForStep(genCarver);
                ListIterator<Supplier<ConfiguredCarver<?>>> carverIterator = carverList.listIterator();
                ChunkPos pos = new ChunkPos(chunkX, chunkZ);

                while (carverIterator.hasNext()) {
                    ConfiguredCarver<?> configuredCarver = carverIterator.next().get();
                    random.setSeed((long) chunkX * l + (long) chunkZ * l1 ^ seed);
                    
                    if (configuredCarver.shouldCarve(random)) {
                        configuredCarver.carve(heightContext, chunk, biomeAcc::getBiome, random, aquiferSampler, pos, bitSet);

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
        
        Biome biome = this.getBiomeAt(chunk.getPos().getStartX(), 0, chunk.getPos().getStartZ(), chunk);

        ((MixinChunkGeneratorInvoker)this).invokeSetStructureStart(
            ConfiguredStructureFeatures.STRONGHOLD, 
            dynamicRegistryManager, 
            structureAccessor, 
            chunk,
            structureManager, 
            seed, 
            biome
        );
        
        for (final Supplier<ConfiguredStructureFeature<?, ?>> supplier : biome.getGenerationSettings().getStructureFeatures()) {
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
        return this.chunkProvider.getHeight(x, z, type, world);
    }
  
    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world) {
        int height = this.chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG, world);
        int worldHeight = this.chunkProvider.getWorldHeight();
        int minY = this.chunkProvider.getMinimumY();
        
        BlockState[] column = new BlockState[worldHeight];
        
        for (int y = worldHeight - 1; y >= 0; --y) {
            // Offset y by minY to get actual current height.
            int actualY = y + minY;
            
            if (actualY > height) {
                if (actualY > this.getSeaLevel())
                    column[y] = BlockStates.AIR;
                else
                    column[y] = this.defaultFluid;
            } else {
                column[y] = this.defaultBlock;
            }
        }
        
        return new VerticalBlockSample(minY, column);
    }
    
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
        return this.chunkProvider.getSeaLevel();
    }
    
    @Override
    public ChunkGenerator withSeed(long seed) {
        return new OldChunkGenerator(this.biomeSource.withSeed(seed), seed, this.settings, this.chunkProviderSettings);
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
    
    public boolean generatesOceanShrines() {
        return this.generateOceanShrines;
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
    
    @SuppressWarnings("unused")
    private void replaceOceansInChunk(OldBiomeSource oldBiomeSource, Chunk chunk) {
        MutableBiomeArray mutableBiomeArray = MutableBiomeArray.inject(chunk.getBiomeArray());
        
        ChunkPos chunkPos = chunk.getPos();
        BlockPos.Mutable pos = new BlockPos.Mutable();
        
        int worldHeight = this.getWorldHeight();
        int minY = this.getMinimumY();
        int seaLevel = this.getSeaLevel();
        
        int biomeHeight = worldHeight >> 2;
        
        for (int biomeX = 0; biomeX < 4; ++biomeX) {
            for (int biomeZ = 0; biomeZ < 4; ++biomeZ) {
                int absX = chunkPos.getStartX() + (biomeX << 2);
                int absZ = chunkPos.getStartZ() + (biomeZ << 2);
                    
                // Offset by 2 to get center of biome coordinate section,
                // to sample overall ocean depth as accurately as possible.
                int offsetX = absX + 2;
                int offsetZ = absZ + 2;
                
                int height = GenUtil.getSolidHeight(chunk, worldHeight, minY, offsetX, offsetZ, this.defaultFluid);
                
                if (height < seaLevel - OCEAN_MIN_DEPTH && chunk.getBlockState(pos.set(offsetX, height + 1, offsetZ)).equals(this.defaultFluid)) {
                    Biome oceanBiome = oldBiomeSource.getOceanBiomeForNoiseGen(absX >> 2, 0, absZ >> 2);
                    
                    // Fill biome column
                    for (int biomeY = 0; biomeY < biomeHeight; ++biomeY) {
                        int absY = biomeY << 2;
                        int actualY = absY + this.getMinimumY();
                        
                        // TODO: Remove true when cave biomes are in.
                        if (true || actualY >= OCEAN_Y_CUT_OFF)
                            mutableBiomeArray.setBiome(absX, absY, absZ, oceanBiome, this.getMinimumY(), this.getWorldHeight());
                    }
                }
            }
        }
    }
    
    private Biome getBiomeAt(int x, int y, int z, HeightLimitView world) {
        int seaLevel = this.getSeaLevel();
        
        int biomeX = x >> 2;
        int biomeY = y >> 2;
        int biomeZ = z >> 2;
        
        Biome biome;
        if (this.generateOceans && this.biomeSource instanceof OldBiomeSource oldBiomeSource && 
            this.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG, world) < seaLevel - OCEAN_MIN_DEPTH
        ) {
            biome = oldBiomeSource.getOceanBiomeForNoiseGen(biomeX, 0, biomeZ);
        } else {
            biome = this.biomeSource.getBiomeForNoiseGen(biomeX, biomeY, biomeZ);
        }
        
        return biome;
    }
}
