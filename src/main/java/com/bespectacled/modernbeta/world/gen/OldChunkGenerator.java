package com.bespectacled.modernbeta.world.gen;

import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.registry.Registries;
import com.bespectacled.modernbeta.api.world.gen.ChunkProvider;
import com.bespectacled.modernbeta.compat.Compat;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.GenUtil;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.structure.OceanShrineStructure;
import com.bespectacled.modernbeta.world.structure.OldStructures;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.class_6643;
import net.minecraft.block.BlockState;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Util;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeCoords;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.PalettedContainer;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.carver.CarverContext;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.AquiferSampler.FluidLevel;
import net.minecraft.world.gen.chunk.AquiferSampler.FluidLevelSampler;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;

public class OldChunkGenerator extends NoiseChunkGenerator {
    public static final Codec<OldChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
            Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.worldSeed),
            ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("settings").forGetter(generator -> generator.settings),
            NbtCompound.CODEC.fieldOf("provider_settings").forGetter(generator -> generator.chunkProviderSettings))
        .apply(instance, instance.stable(OldChunkGenerator::new)));
    
    public static final int OCEAN_MIN_DEPTH = 4;
    
    private final NbtCompound chunkProviderSettings;
    private final ChunkProvider chunkProvider;
    private final String chunkProviderType;
    private final FluidLevelSampler carverFluidLevelSampler;

    private final boolean generateOceans;
    private final boolean generateOceanShrines;
    
    public OldChunkGenerator(BiomeSource biomeSource, long seed, Supplier<ChunkGeneratorSettings> settings, NbtCompound providerSettings) {
        super(biomeSource, seed, settings);
        
        this.chunkProviderSettings = providerSettings;
        this.chunkProviderType = NbtUtil.readStringOrThrow(NbtTags.WORLD_TYPE, providerSettings);
        this.chunkProvider = Registries.CHUNK.get(this.chunkProviderType).apply(this);
        this.carverFluidLevelSampler = (x, y, z) -> new FluidLevel(this.chunkProvider.getSeaLevel(), BlockStates.AIR);
        
        this.generateOceans = !Compat.isLoaded("hydrogen") ? 
            NbtUtil.readBoolean(NbtTags.GEN_OCEANS, providerSettings, ModernBeta.GEN_CONFIG.infGenConfig.generateOceans) : 
            false;
        
        this.generateOceanShrines = NbtUtil.readBoolean(NbtTags.GEN_OCEAN_SHRINES, providerSettings, ModernBeta.GEN_CONFIG.infGenConfig.generateOceanShrines);
    }

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR, ModernBeta.createId("old"), CODEC);
    }
    
    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return OldChunkGenerator.CODEC;
    }
    
    @Override
    public CompletableFuture<Chunk> populateBiomes(Executor executor, Registry<Biome> biomeRegistry, StructureAccessor accessor, Chunk chunk) {
        return CompletableFuture.<Chunk>supplyAsync(Util.debugSupplier("init_biomes", () -> {
            chunk.method_38257(this.biomeSource, this.getMultiNoiseSampler());
            return chunk;
        }), Util.getMainWorkerExecutor());
    }
    
    @Override
    public CompletableFuture<Chunk> populateNoise(Executor executor, StructureAccessor accessor, Chunk chunk) {
        return this.chunkProvider.provideChunk(executor, accessor, chunk);
    }
        
    @Override
    public void buildSurface(ChunkRegion region, StructureAccessor accessor, Chunk chunk) {
        if (!this.chunkProvider.skipChunk(chunk.getPos().x, chunk.getPos().z, ChunkStatus.SURFACE))  
            if (this.biomeSource instanceof OldBiomeSource oldBiomeSource)
                this.chunkProvider.provideSurface(region, chunk, oldBiomeSource);
            else
                super.buildSurface(region, accessor, chunk);
        
        if (this.generateOceans && this.biomeSource instanceof OldBiomeSource oldBiomeSource)
            this.replaceOceansInChunk(oldBiomeSource, chunk);
    }
    
    @Override
    public void carve(ChunkRegion region, long seed, BiomeAccess access, StructureAccessor accessor, Chunk chunk, GenerationStep.Carver genCarver) {
        if (this.chunkProvider.skipChunk(chunk.getPos().x, chunk.getPos().z, ChunkStatus.CARVERS) || 
            this.chunkProvider.skipChunk(chunk.getPos().x, chunk.getPos().z, ChunkStatus.LIQUID_CARVERS)) return;
        
        BiomeAccess biomeAcc = access.withSource((biomeX, biomeY, biomeZ) -> this.biomeSource.getBiome(biomeX, biomeY, biomeZ, this.getMultiNoiseSampler()));
        ChunkPos chunkPos = chunk.getPos();

        int mainChunkX = chunkPos.x;
        int mainChunkZ = chunkPos.z;
        
        CarverContext heightContext = new CarverContext(this, chunk);
        AquiferSampler aquiferSampler = AquiferSampler.seaLevel(this.carverFluidLevelSampler);
        class_6643 carvingMask = ((ProtoChunk)chunk).getOrCreateCarvingMask(genCarver);
        
        Random random = new Random(seed);
        long l = (random.nextLong() / 2L) * 2L + 1L;
        long l1 = (random.nextLong() / 2L) * 2L + 1L;

        for (int chunkX = mainChunkX - 8; chunkX <= mainChunkX + 8; ++chunkX) {
            for (int chunkZ = mainChunkZ - 8; chunkZ <= mainChunkZ + 8; ++chunkZ) {
                ChunkPos pos = new ChunkPos(chunkX, chunkZ);
                Chunk curChunk = region.getChunk(pos.x, pos.z);
                GenerationSettings genSettings = curChunk.method_38258(
                    () -> this.biomeSource.getBiome(
                        BiomeCoords.fromBlock(pos.getStartX()), 
                        0, 
                        BiomeCoords.fromBlock(pos.getStartZ()), 
                        this.getMultiNoiseSampler())
                ).getGenerationSettings();
                
                List<Supplier<ConfiguredCarver<?>>> carverList = genSettings.getCarversForStep(genCarver);
                ListIterator<Supplier<ConfiguredCarver<?>>> carverIterator = carverList.listIterator();

                while (carverIterator.hasNext()) {
                    ConfiguredCarver<?> configuredCarver = carverIterator.next().get();
                    random.setSeed((long) chunkX * l + (long) chunkZ * l1 ^ seed);
                    
                    if (configuredCarver.shouldCarve(random)) {
                        configuredCarver.carve(heightContext, chunk, biomeAcc::getBiome, random, aquiferSampler, pos, carvingMask);

                    }
                }
            }
        }
    }
    
    /*
    
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
    
    */
    
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
                    column[y] = this.settings.get().getDefaultFluid();
            } else {
                column[y] = this.defaultBlock;
            }
        }
        
        return new VerticalBlockSample(minY, column);
    }
    
    /*
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
    */
    
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
        int height = this.chunkProvider.getWorldHeight();
        
        if (height >= 320)
            return height;
        
        return 320;
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
    
    public Biome getInjectedBiomeAtBlock(int x, int y, int z) {
        int biomeX = x >> 2;
        int biomeY = y >> 2;
        int biomeZ = z >> 2;
        
        if (this.generateOceans && this.biomeSource instanceof OldBiomeSource oldBiomeSource && this.atOceanDepth(y)) {
            return oldBiomeSource.getOceanBiome(biomeX, 0, biomeZ);
        }
        
        return this.biomeSource.getBiome(biomeX, biomeY, biomeZ, this.getMultiNoiseSampler());
    }

    private void replaceOceansInChunk(OldBiomeSource oldBiomeSource, Chunk chunk) {
        ChunkPos chunkPos = chunk.getPos();
        BlockPos.Mutable pos = new BlockPos.Mutable();
        
        int worldHeight = this.getWorldHeight();
        int minY = this.getMinimumY();
        int containerLen = 4;
        
        BlockState defaultFluid = this.settings.get().getDefaultFluid();
        Biome[] biomeArr = new Biome[16];
        
        // Determine ocean biome positions
        for (int biomeX = 0; biomeX < containerLen; ++biomeX) {
            for (int biomeZ = 0; biomeZ < containerLen; ++biomeZ) {
                Biome biome = null;
                
                int x = chunkPos.getStartX() + (biomeX << 2);
                int z = chunkPos.getStartZ() + (biomeZ << 2);
                
                // Offset by 2 to get center of biome coordinate section,
                // to sample overall ocean depth as accurately as possible.
                int offsetX = x + 2;
                int offsetZ = z + 2;
                
                int height = GenUtil.getLowestSolidHeight(chunk, worldHeight, minY, offsetX, offsetZ, defaultFluid);
                
                if (this.atOceanDepth(height) && chunk.getBlockState(pos.set(offsetX, height + 1, offsetZ)).equals(defaultFluid)) {
                    biome = oldBiomeSource.getOceanBiome(x >> 2, 0, z >> 2);
                }
                
                biomeArr[biomeX + biomeZ * containerLen] = biome;
            }
        }
        
        // Replace biomes from biome array
        for (int sectionY = 0; sectionY < chunk.countVerticalSections(); ++sectionY) {
            ChunkSection section = chunk.getSection(sectionY);
            PalettedContainer<Biome> container = section.method_38294();
            
            container.lock();
            try {
                for (int biomeX = 0; biomeX < containerLen; ++biomeX) {
                    for (int biomeZ = 0; biomeZ < containerLen; ++biomeZ) {
                        Biome biome = biomeArr[biomeX + biomeZ * containerLen];
                        
                        for (int biomeY = 0; biomeY < containerLen; ++biomeY) {
                            if (biome != null) {
                                container.swapUnsafe(biomeX, biomeY, biomeZ, biome);
                            }
                        }   
                    }
                }
                
            } catch (Exception e) {
                ModernBeta.log(Level.ERROR, "Unable to replace biomes!");
                e.printStackTrace();
                
            } finally {
                container.unlock();
            }
        }
    }
    
    private boolean atOceanDepth(int height) {
        return height < this.getSeaLevel() - OCEAN_MIN_DEPTH;
    }
}
