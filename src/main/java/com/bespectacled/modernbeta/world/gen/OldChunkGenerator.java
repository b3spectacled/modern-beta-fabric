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
import com.bespectacled.modernbeta.util.GenUtil;
import com.bespectacled.modernbeta.util.mutable.MutableBiomeArray;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.feature.OldFeatures;
import com.bespectacled.modernbeta.world.structure.OldStructures;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.Util;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
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
            CompoundTag.CODEC.fieldOf("provider_settings").forGetter(generator -> generator.chunkProviderSettings))
        .apply(instance, instance.stable(OldChunkGenerator::new)));
    
    private static final int OCEAN_MIN_DEPTH = 4;
    
    private final BiomeSource biomeSource;
    private final CompoundTag chunkProviderSettings;
    
    private final String chunkProviderType;
    private final boolean generateOceans;
    
    private final ChunkProvider chunkProvider;
    
    public OldChunkGenerator(BiomeSource biomeSource, long seed, Supplier<ChunkGeneratorSettings> settings, CompoundTag providerSettings) {
        super(biomeSource, seed, settings);
        
        this.biomeSource = biomeSource;

        this.chunkProviderSettings = providerSettings;
        this.chunkProviderType = NBTUtil.readStringOrThrow(WorldSettings.TAG_WORLD, providerSettings);
        this.chunkProvider = Registries.CHUNK.get(this.chunkProviderType).apply(this);
        
        this.generateOceans = NBTUtil.readBoolean("generateOceans", providerSettings, ModernBeta.GEN_CONFIG.generateOceans);
    }

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR, ModernBeta.createId("old"), CODEC);
    }
    
    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return OldChunkGenerator.CODEC;
    }
    
    @Override
    public void populateNoise(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk)  {   
        this.chunkProvider.provideChunk(worldAccess, structureAccessor, chunk);
    }
        
    @Override
    public void buildSurface(ChunkRegion region, Chunk chunk) {
        if (!this.chunkProvider.skipChunk(chunk.getPos().x, chunk.getPos().z, ChunkStatus.SURFACE))  
            if (this.biomeSource instanceof OldBiomeSource)
                this.chunkProvider.provideSurface(region, chunk, (OldBiomeSource)this.biomeSource);
            else
                super.buildSurface(region, chunk);
        
        if (this.generateOceans)
            this.replaceOceansInChunk(chunk);
    }

    @Override
    public void generateFeatures(ChunkRegion region, StructureAccessor accessor) {
        OldFeatures.OLD_FANCY_OAK.chunkReset();
        if (this.chunkProvider.skipChunk(region.getCenterChunkX(), region.getCenterChunkZ(), ChunkStatus.FEATURES)) return;
        
        int ctrX = region.getCenterChunkX();
        int ctrZ = region.getCenterChunkZ();
        int startX = ctrX * 16;
        int startZ = ctrZ * 16;
        
        Biome biome = this.getBiomeAt(startX, 0, startZ);

        // TODO: Remove chunkRandom at some point
        ChunkRandom chunkRandom = new ChunkRandom();
        long popSeed = chunkRandom.setPopulationSeed(region.getSeed(), startX, startZ);

        try {
            biome.generateFeatureStep(accessor, this, region, popSeed, chunkRandom, new BlockPos(startX, 0, startZ));
        } catch (Exception exception) {
            CrashReport report = CrashReport.create(exception, "Biome decoration");
            report.addElement("Generation").add("CenterX", ctrX).add("CenterZ", ctrZ).add("Seed", popSeed).add("Biome", biome);
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

            Biome biome = this.getBiomeAt(chunkPos.getStartX(), 0, chunkPos.getStartZ());
            GenerationSettings genSettings = biome.getGenerationSettings();
            
            BitSet bitSet = ((ProtoChunk)chunk).getOrCreateCarvingMask(genCarver);
            
            random.setSeed(seed);
            long l = (random.nextLong() / 2L) * 2L + 1L;
            long l1 = (random.nextLong() / 2L) * 2L + 1L;
            

            for (int chunkX = mainChunkX - 8; chunkX <= mainChunkX + 8; ++chunkX) {
                for (int chunkZ = mainChunkZ - 8; chunkZ <= mainChunkZ + 8; ++chunkZ) {
                    List<Supplier<ConfiguredCarver<?>>> carverList = genSettings.getCarversForStep(genCarver);
                    ListIterator<Supplier<ConfiguredCarver<?>>> carverIterator = carverList.listIterator();

                    while (carverIterator.hasNext()) {
                        ConfiguredCarver<?> configuredCarver = carverIterator.next().get();
                        
                        random.setSeed((long) chunkX * l + (long) chunkZ * l1 ^ seed);
                        
                        if (configuredCarver.shouldCarve(random, chunkX, chunkZ)) {
                            configuredCarver.carve(chunk, biomeAcc::getBiome, random, this.getSeaLevel(), chunkX, chunkZ,
                                    mainChunkX, mainChunkZ, bitSet);

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
        
        ChunkPos chunkPos = chunk.getPos();
        Biome biome = this.getBiomeAt(chunk.getPos().getStartX(), 0, chunk.getPos().getStartZ());

        ((MixinChunkGeneratorInvoker)this).invokeSetStructureStart(
            ConfiguredStructureFeatures.STRONGHOLD, 
            dynamicRegistryManager, 
            structureAccessor, 
            chunk,
            structureManager, 
            seed,
            chunkPos,
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
                chunkPos,
                biome
            );
        }
    }
    
    @Override
    public int getHeight(int x, int z, Heightmap.Type type) {
        return this.chunkProvider.getHeight(x, z, type);
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
    public List<SpawnSettings.SpawnEntry> getEntitySpawnList(Biome biome, StructureAccessor structureAccessor, SpawnGroup spawnGroup, BlockPos blockPos) {
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
    
    public boolean isProviderInstanceOf(Class<?> c) {
        return c.isInstance(this.chunkProvider);
    }
    
    public ChunkProvider getChunkProvider() {
        return this.chunkProvider;
    }
    
    public CompoundTag getProviderSettings() {
        return new CompoundTag().copyFrom(this.chunkProviderSettings);
    }
    
    /*
    public static void export() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path dir = Paths.get("..\\src\\main\\resources\\data\\modern_beta\\dimension");
        
        CompoundTag chunkSettings = OldGeneratorSettings.createInfSettings(OldChunkProviderType.BETA);
        CompoundTag biomeSettings = OldGeneratorSettings.createBiomeSettings(OldBiomeProviderType.BETA, CaveBiomeType.VANILLA, BetaBiomes.FOREST_ID);
        
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
    private void replaceOceansInChunk(Chunk chunk) {
        if (this.biomeSource instanceof OldBiomeSource) {
            OldBiomeSource biomeSource = (OldBiomeSource)this.biomeSource;
            MutableBiomeArray mutableBiomes = MutableBiomeArray.inject(chunk.getBiomeArray());
            ChunkPos pos = chunk.getPos();
            Biome biome;
            
            // Replace biomes in bodies of water at least four deep with ocean biomes
            for (int x = 0; x < 4; x++) {
                
                for (int z = 0; z < 4; z++) {
                    int absX = pos.getStartX() + (x << 2);
                    int absZ = pos.getStartZ() + (z << 2);
                    
                    int y = GenUtil.getSolidHeight(chunk, absX, absZ, this.getWorldHeight(), this.defaultFluid);

                    if (y < this.getSeaLevel() - 4) {
                        biome = biomeSource.getOceanBiomeForNoiseGen(absX >> 2, 0, absZ >> 2);
                        
                        mutableBiomes.setBiome(absX, 0, absZ, biome);
                    }
                }   
            }
        }
    }
    
    private Biome getBiomeAt(int x, int y, int z) {
        int seaLevel = this.getSeaLevel();
        
        int biomeX = x >> 2;
        int biomeZ = z >> 2;
        
        Biome biome;
        if (this.generateOceans && this.biomeSource instanceof OldBiomeSource && this.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG) < seaLevel - OCEAN_MIN_DEPTH) {
            biome = ((OldBiomeSource)this.biomeSource).getOceanBiomeForNoiseGen(biomeX, 0, biomeZ);
        } else {
            biome = this.biomeSource.getBiomeForNoiseGen(biomeX, 0, biomeZ);
        }
        
        return biome;
    }
}
