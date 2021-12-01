package com.bespectacled.modernbeta.world.gen;

import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.registry.Registries;
import com.bespectacled.modernbeta.api.world.gen.ChunkProvider;
import com.bespectacled.modernbeta.mixin.MixinChunkGeneratorInvoker;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.biome.injector.BiomeInjectionRules.BiomeInjectionContext;
import com.bespectacled.modernbeta.world.biome.injector.BiomeInjector;
import com.bespectacled.modernbeta.world.structure.OldStructures;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.StructureManager;
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
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;

public class OldChunkGenerator extends NoiseChunkGenerator {
    public static final Codec<OldChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
            Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.seed),
            ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("settings").forGetter(generator -> generator.settings),
            NbtCompound.CODEC.fieldOf("provider_settings").forGetter(generator -> generator.chunkProviderSettings))
        .apply(instance, instance.stable(OldChunkGenerator::new)));
    
    private final NbtCompound chunkProviderSettings;
    private final ChunkProvider chunkProvider;
    private final String chunkProviderType;

    private final boolean generateOceanShrines;
    private final boolean generateMonuments;

    private final BiomeInjector biomeInjector;
    
    public OldChunkGenerator(BiomeSource biomeSource, long seed, Supplier<ChunkGeneratorSettings> settings, NbtCompound providerSettings) {
        super(biomeSource, seed, settings);
        
        this.chunkProviderSettings = providerSettings;
        this.chunkProviderType = NbtUtil.readStringOrThrow(NbtTags.WORLD_TYPE, providerSettings);
        this.chunkProvider = Registries.CHUNK.get(this.chunkProviderType).apply(this);
        
        this.generateOceanShrines = NbtUtil.readBoolean(
            NbtTags.GEN_OCEAN_SHRINES,
            providerSettings,
            ModernBeta.GEN_CONFIG.generateOceanShrines
        );
        
        this.generateMonuments = NbtUtil.readBoolean(
            NbtTags.GEN_MONUMENTS,
            providerSettings,
            ModernBeta.GEN_CONFIG.generateMonuments
        );
    
        this.biomeInjector = this.biomeSource instanceof OldBiomeSource ?
            new BiomeInjector(this, (OldBiomeSource)this.biomeSource) : 
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
        
        // Inject ocean, etc. biomes
        if (this.biomeInjector != null) {
            this.biomeInjector.injectBiomes(chunk);
        }
    }
    
    @Override
    public void generateFeatures(ChunkRegion region, StructureAccessor accessor) {
        if (this.chunkProvider.skipChunk(region.getCenterChunkX(), region.getCenterChunkZ(), ChunkStatus.FEATURES)) return;
        
        int ctrX = region.getCenterChunkX();
        int ctrZ = region.getCenterChunkZ();
        int startX = ctrX * 16;
        int startZ = ctrZ * 16;
        
        Biome biome = this.getInjectedBiomeAtBlock(startX, 0, startZ);

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

        Biome biome = this.getInjectedBiomeAtBlock(chunkPos.getStartX(), 0, chunkPos.getStartZ());
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
        Biome biome = this.getInjectedBiomeAtBlock(chunk.getPos().getStartX(), 0, chunk.getPos().getStartZ());

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
    public VerticalBlockSample getColumnSample(int x, int z) {
        int height = this.chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG);
        int worldHeight = this.chunkProvider.getWorldHeight();
        int minY = this.chunkProvider.getWorldMinY();
        
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
        
        return new VerticalBlockSample(column);
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
        return this.chunkProvider.getSeaLevel();
    }
    
    @Override
    public ChunkGenerator withSeed(long seed) {
        return new OldChunkGenerator(this.biomeSource.withSeed(seed), seed, this.settings, this.chunkProviderSettings);
    }
    
    public long getWorldSeed() {
        return this.seed;
    }
    
    public Supplier<ChunkGeneratorSettings> getGeneratorSettings() {
        return this.settings;
    }
    
    public ChunkProvider getChunkProvider() {
        return this.chunkProvider;
    }
    
    public NbtCompound getChunkSettings() {
        return new NbtCompound().copyFrom(this.chunkProviderSettings);
    }
    
    public boolean generatesOceanShrines() {
        return this.generateOceanShrines;
    }
    
    public boolean generatesMonuments() {
        return this.generateMonuments;
    }
    
    private Biome getInjectedBiomeAtBlock(int x, int y, int z) {
        int biomeX = x >> 2;
        int biomeY = y >> 2;
        int biomeZ = z >> 2;
        
        int topHeight = this.biomeInjector.getCenteredHeight(biomeX, biomeZ);
        BlockState topState = y < this.getSeaLevel() ? 
            this.settings.get().getDefaultFluid() :
            BlockStates.AIR;
        
        BiomeInjectionContext context = new BiomeInjectionContext(topHeight, topState);
        Biome biome = this.biomeInjector.sample(context, biomeX, biomeY, biomeZ);
        
        return biome != null ? biome : this.biomeSource.getBiomeForNoiseGen(biomeX, biomeY, biomeZ);
    }
    
    public Set<Biome> getBiomesInArea(int startX, int startY, int startZ, int radius) {
        int minX = (startX - radius) >> 2;
        int minZ = (startZ - radius) >> 2;
        
        int maxX = (startX + radius) >> 2;
        int maxZ = (startZ + radius) >> 2;
        
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
