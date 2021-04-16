package com.bespectacled.modernbeta.world.gen;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.AbstractChunkProvider;
import com.bespectacled.modernbeta.api.registry.ChunkProviderRegistry;
import com.bespectacled.modernbeta.api.registry.ChunkProviderSettingsRegistry;
import com.bespectacled.modernbeta.api.registry.WorldProviderRegistry;
import com.bespectacled.modernbeta.api.registry.BiomeProviderRegistry.BuiltInBiomeType;
import com.bespectacled.modernbeta.api.registry.ChunkProviderRegistry.BuiltInChunkType;
import com.bespectacled.modernbeta.api.registry.ChunkProviderSettingsRegistry.BuiltInChunkSettingsType;
import com.bespectacled.modernbeta.mixin.MixinChunkGeneratorInvoker;
import com.bespectacled.modernbeta.util.MutableBiomeArray;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.biome.provider.settings.BiomeProviderSettings;
import com.bespectacled.modernbeta.world.feature.OldFeatures;
import com.bespectacled.modernbeta.world.structure.OldStructures;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.BuiltinRegistries;
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
    
    private final OldBiomeSource biomeSource;
    private final CompoundTag chunkProviderSettings;
    
    private final String chunkProviderType;
    private final boolean generateOceans;
    
    private final AbstractChunkProvider chunkProvider;
    private final Random random;
    
    public OldChunkGenerator(BiomeSource biomeSource, long seed, Supplier<ChunkGeneratorSettings> settings, CompoundTag providerSettings) {
        super(biomeSource, seed, settings);
        
        this.random = new Random(seed);
        
        this.biomeSource = (OldBiomeSource)biomeSource;

        this.chunkProviderSettings = providerSettings;
        this.chunkProviderType = ChunkProviderRegistry.getChunkProviderType(providerSettings);
        this.chunkProvider = ChunkProviderRegistry.get(this.chunkProviderType).apply(
            seed, 
            this.biomeSource.getBiomeProvider(), 
            settings, 
            providerSettings
        );
        
        this.generateOceans = providerSettings.contains("generateOceans") ? providerSettings.getBoolean("generateOceans") : false;
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
            this.chunkProvider.provideSurface(region, chunk, this.biomeSource);
        
        if (this.generateOceans) {
            MutableBiomeArray mutableBiomes = MutableBiomeArray.inject(chunk.getBiomeArray());
            ChunkPos pos = chunk.getPos();
            Biome biome;
            
            // Replace biomes in bodies of water at least four deep with ocean biomes
            for (int x = 0; x < 4; x++) {
                
                for (int z = 0; z < 4; z++) {
                    int absX = pos.getStartX() + (x << 2);
                    int absZ = pos.getStartZ() + (z << 2);
                    
                    int y = OldGeneratorUtil.getSolidHeight(chunk, absX, absZ, this.getWorldHeight());

                    if (y < this.getSeaLevel() - 4) {
                        biome = biomeSource.getOceanBiomeForNoiseGen(absX >> 2, 0, absZ >> 2);
                        
                        mutableBiomes.setBiome(absX, 0, absZ, biome);
                    }
                }   
            }
        }
    }

    @Override
    public void generateFeatures(ChunkRegion region, StructureAccessor accessor) {
        OldFeatures.OLD_FANCY_OAK.chunkReset();
        if (this.chunkProvider.skipChunk(region.getCenterChunkX(), region.getCenterChunkZ(), ChunkStatus.FEATURES)) return;
        
        int ctrX = region.getCenterChunkX();
        int ctrZ = region.getCenterChunkZ();
        int ctrAbsX = ctrX * 16;
        int ctrAbsZ = ctrZ * 16;
        
        Biome biome = OldGeneratorUtil.getOceanBiome(region.getChunk(ctrX, ctrZ), this, biomeSource, generateOceans, this.getSeaLevel());

        // TODO: Remove chunkRandom at some point
        ChunkRandom chunkRandom = new ChunkRandom();
        long popSeed = chunkRandom.setPopulationSeed(region.getSeed(), ctrAbsX, ctrAbsZ);

        try {
            biome.generateFeatureStep(accessor, this, region, popSeed, chunkRandom, new BlockPos(ctrAbsX, 0, ctrAbsZ));
        } catch (Exception exception) {
            CrashReport report = CrashReport.create(exception, "Biome decoration");
            report.addElement("Generation").add("CenterX", ctrX).add("CenterZ", ctrZ).add("Seed", popSeed).add("Biome", biome);
            throw new CrashException(report);
        }
    }
    
    @Override
    public void carve(long seed, BiomeAccess access, Chunk chunk, GenerationStep.Carver carver) {
        if (this.chunkProvider.skipChunk(chunk.getPos().x, chunk.getPos().z, ChunkStatus.CARVERS) || 
            this.chunkProvider.skipChunk(chunk.getPos().x, chunk.getPos().z, ChunkStatus.LIQUID_CARVERS)) return;
        
        BiomeAccess biomeAcc = access.withSource(this.biomeSource);
        ChunkPos chunkPos = chunk.getPos();

        int mainChunkX = chunkPos.x;
        int mainChunkZ = chunkPos.z;

        Biome biome = OldGeneratorUtil.getOceanBiome(chunk, this, this.biomeSource, this.generateOceans, this.getSeaLevel());
        GenerationSettings genSettings = biome.getGenerationSettings();
        
        BitSet bitSet = ((ProtoChunk)chunk).getOrCreateCarvingMask(carver);

        
        random.setSeed(seed);
        long l = (random.nextLong() / 2L) * 2L + 1L;
        long l1 = (random.nextLong() / 2L) * 2L + 1L;
        

        for (int chunkX = mainChunkX - 8; chunkX <= mainChunkX + 8; ++chunkX) {
            for (int chunkZ = mainChunkZ - 8; chunkZ <= mainChunkZ + 8; ++chunkZ) {
                List<Supplier<ConfiguredCarver<?>>> carverList = genSettings.getCarversForStep(carver);
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
        Biome biome = OldGeneratorUtil.getOceanBiome(chunk, this, this.biomeSource, this.generateOceans, this.getSeaLevel());

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
        return this.chunkProvider.getWorldHeight();
    }

    @Override
    public int getSeaLevel() {
        return chunkProvider.getSeaLevel();
    }
    
    @Override
    public ChunkGenerator withSeed(long seed) {
        return new OldChunkGenerator(this.biomeSource.withSeed(seed), seed, this.settings, this.chunkProviderSettings);
    }
    
    public String getChunkProviderType() {
        return this.chunkProviderType;
    }
    
    public AbstractChunkProvider getChunkProvider() {
        return this.chunkProvider;
    }
    
    public CompoundTag getProviderSettings() {
        return this.chunkProviderSettings;
    }
    
    /*
    public static void export() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path dir = Paths.get("..\\src\\main\\resources\\data\\modern_beta\\dimension");
        
        CompoundTag biomeProviderSettings = BiomeProviderSettings.createBiomeSettings(BuiltInBiomeType.BETA.name, WorldProviderRegistry.get(BuiltInChunkType.BETA.name).getDefaultBiome());
        CompoundTag chunkProviderSettings = ChunkProviderSettingsRegistry.get(BuiltInChunkSettingsType.BETA.name).get();
        OldChunkGeneratorSettings settings = new OldChunkGeneratorSettings(OldChunkGeneratorSettings.BETA_GENERATOR_SETTINGS, chunkProviderSettings);
        
        OldChunkGenerator chunkGenerator = new OldChunkGenerator(new OldBiomeSource(0, BuiltinRegistries.BIOME, biomeProviderSettings), 0, settings);
        Function<OldChunkGenerator, DataResult<JsonElement>> toJson = JsonOps.INSTANCE.withEncoder(OldChunkGenerator.CODEC);
        
        try {
            JsonElement json = toJson.apply(chunkGenerator).result().get();
            Files.write(dir.resolve(ModernBeta.createId("old").getPath() + ".json"), gson.toJson(json).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            ModernBeta.LOGGER.error("[Modern Beta] Couldn't serialize old chunk generator!");
            e.printStackTrace();
        }
    }*/
}
