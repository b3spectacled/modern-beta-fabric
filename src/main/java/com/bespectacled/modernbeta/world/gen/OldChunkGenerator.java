package com.bespectacled.modernbeta.world.gen;

import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Set;
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
import com.google.common.collect.Sets;
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
    private final boolean generateMonuments;
    
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
        this.generateMonuments = NbtUtil.readBoolean(NbtTags.GEN_MONUMENTS, providerSettings, ModernBeta.GEN_CONFIG.infGenConfig.generateMonuments);
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
            this.injectBiomesInChunk(oldBiomeSource, chunk);
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
                
                int startX = pos.getStartX();
                int startZ = pos.getStartZ();
                
                GenerationSettings genSettings = curChunk.method_38258(
                    () -> this.getInjectedBiomeAtBlock(
                            startX,
                            this.getHeight(startX, startZ, Heightmap.Type.OCEAN_FLOOR_WG, curChunk),
                            startZ
                )).getGenerationSettings();
                
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
    
    public boolean generatesMonuments() {
        return this.generateMonuments;
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
                int y = this.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG, null);
                
                set.add(this.getInjectedBiomeAtBlock(x, y, z));
            }
        }
        
        return set;
    } 

    private void injectBiomesInChunk(OldBiomeSource oldBiomeSource, Chunk chunk) {
        ChunkPos chunkPos = chunk.getPos();
        BlockPos.Mutable pos = new BlockPos.Mutable();
        
        int startX = chunkPos.getStartX();
        int startZ = chunkPos.getStartZ();
        
        int biomeStartX = startX >> 2;
        int biomeStartZ = startZ >> 2;
        
        int worldHeight = this.getWorldHeight();
        int minY = this.getMinimumY();
        int caveStartOffset = 8;
        int caveLowerCutoff = this.getMinimumY() + 16;
        
        int containerLen = 4;
        
        BlockState defaultFluid = this.settings.get().getDefaultFluid();
        
        int[] heights = new int[16];
        boolean[] oceans = new boolean[16];
        
        // Collect height values at biome coordinates
        for (int biomeX = 0; biomeX < containerLen; ++biomeX) {
            for (int biomeZ = 0; biomeZ < containerLen; ++biomeZ) {
                int x = startX + (biomeX << 2);
                int z = startZ + (biomeZ << 2);
                
                // Offset by 2 to get center of biome coordinate section,
                // to sample overall ocean depth as accurately as possible.
                int offsetX = x + 2;
                int offsetZ = z + 2;
                
                heights[biomeX + biomeZ * containerLen] = GenUtil.getLowestSolidHeight(
                    chunk,
                    worldHeight,
                    minY,
                    offsetX,
                    offsetZ,
                    defaultFluid
                );
            }
        }
        
        // Determine ocean biome positions
        for (int biomeX = 0; biomeX < containerLen; ++biomeX) {
            for (int biomeZ = 0; biomeZ < containerLen; ++biomeZ) {
                boolean hasOcean = false;
                
                int x = startX + (biomeX << 2);
                int z = startZ + (biomeZ << 2);
                
                // Offset by 2 to get center of biome coordinate section,
                // to sample overall ocean depth as accurately as possible.
                int offsetX = x + 2;
                int offsetZ = z + 2;
                int height = heights[biomeX + biomeZ * containerLen];
                pos.set(offsetX, height + 1, offsetZ);
                
                if (this.atOceanDepth(height) && chunk.getBlockState(pos).equals(defaultFluid)) {
                    hasOcean = true;
                }
                
                oceans[biomeX + biomeZ * containerLen] = hasOcean;
            }
        }
        
        // Replace biomes from biome array
        for (int sectionY = 0; sectionY < chunk.countVerticalSections(); ++sectionY) {
            ChunkSection section = chunk.getSection(sectionY);
            PalettedContainer<Biome> container = section.method_38294();
            
            container.lock();
            try {
                int yOffset = section.getYOffset() >> 2;
                
                for (int biomeX = 0; biomeX < containerLen; ++biomeX) {
                    for (int biomeZ = 0; biomeZ < containerLen; ++biomeZ) {
                        int topY = Math.min(heights[biomeX + biomeZ * containerLen], this.getSeaLevel());
                        boolean hasOcean = oceans[biomeX + biomeZ * containerLen];
                       
                        for (int biomeY = 0; biomeY < containerLen; ++biomeY) {
                            int y = (biomeY + yOffset) << 2;
                            
                            Biome biome = null;
                            
                            // Replace with oceans
                            if (hasOcean) {
                                biome = oldBiomeSource.getOceanBiome(biomeX + biomeStartX, 0, biomeZ + biomeStartZ);
                            }
                            
                            // Replace with cave biomes
                            if (y + caveStartOffset < topY && y > caveLowerCutoff) {
                                biome = oldBiomeSource.getCaveBiome(biomeX + biomeStartX, biomeY + yOffset, biomeZ + biomeStartZ);
                            }
                            
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
