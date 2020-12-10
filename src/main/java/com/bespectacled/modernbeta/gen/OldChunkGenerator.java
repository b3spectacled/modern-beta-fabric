package com.bespectacled.modernbeta.gen;

import java.util.List;
import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.feature.BetaFeature;
import com.bespectacled.modernbeta.gen.provider.AbstractChunkProvider;
import com.bespectacled.modernbeta.gen.settings.OldGeneratorSettings;
import com.bespectacled.modernbeta.structure.BetaStructure;
import com.bespectacled.modernbeta.util.GenUtil;
import com.bespectacled.modernbeta.util.WorldEnum.WorldType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.feature.StructureFeature;

public class OldChunkGenerator extends NoiseChunkGenerator {
    
    public static final Codec<OldChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
                    Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.worldSeed),
                    OldGeneratorSettings.CODEC.fieldOf("settings").forGetter(generator -> generator.settings))
            .apply(instance, instance.stable(OldChunkGenerator::new)));
    
    private final long seed;
    private final OldGeneratorSettings settings;
    private final WorldType worldType;
    private final boolean genOceans;
    
    private final OldBiomeSource biomeSource;
    private final AbstractChunkProvider chunkProvider;
    
    private static final ChunkRandom FEATURE_RAND = new ChunkRandom();

    public OldChunkGenerator(BiomeSource biomeSource, long seed, OldGeneratorSettings settings) {
        super(biomeSource, seed, () -> settings.chunkGenSettings);
        
        this.seed = seed;
        this.biomeSource = (OldBiomeSource)biomeSource;
        this.settings = settings;
        
        this.worldType = WorldType.getWorldType(settings.providerSettings);
        this.chunkProvider = AbstractChunkProvider.getChunkProvider(seed, this.worldType, settings);
        this.genOceans = this.worldType != WorldType.SKYLANDS && this.biomeSource.generateOceans();
    }

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR, new Identifier(ModernBeta.ID, "old"), CODEC);
    }
    
    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return OldChunkGenerator.CODEC;
    }
    
    @Override
    public void populateNoise(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk) {
        this.chunkProvider.makeChunk(worldAccess, structureAccessor, chunk, this.biomeSource);
    }
    
    @Override
    public void buildSurface(ChunkRegion region, Chunk chunk) {
        this.chunkProvider.makeSurface(region, chunk, this.biomeSource);

        if (this.genOceans) {
            GenUtil.injectOceanBiomes(chunk, biomeSource);
        }
    }

    @Override
    public void generateFeatures(ChunkRegion chunkRegion, StructureAccessor structureAccessor) {
        BetaFeature.OLD_FANCY_OAK.chunkReset();
        
        GenUtil.generateFeaturesWithOcean(chunkRegion, structureAccessor, this, FEATURE_RAND, this.genOceans);
    }
    
    @Override
    public void carve(long seed, BiomeAccess biomeAccess, Chunk chunk, GenerationStep.Carver carver) {
        GenUtil.carveWithOcean(this.seed, biomeAccess, chunk, carver, this, this.biomeSource, FEATURE_RAND, this.getSeaLevel(), this.genOceans);
    }
    
    @Override
    public void setStructureStarts(
        DynamicRegistryManager dynamicRegistryManager, 
        StructureAccessor structureAccessor,   
        Chunk chunk, 
        StructureManager structureManager, 
        long seed
    ) {
        
        GenUtil.setStructureStartsWithOcean(dynamicRegistryManager, structureAccessor, chunk, structureManager, seed, this, this.biomeSource, this.genOceans);
    }
    
    @Override
    public int getHeight(int x, int z, Heightmap.Type type) {
        return this.chunkProvider.getHeight(x, z, type);
    }
    
    @Override
    public BlockPos locateStructure(ServerWorld world, StructureFeature<?> feature, BlockPos center, int radius,
            boolean skipExistingChunks) {
        if (!this.genOceans)
            if (feature.equals(StructureFeature.OCEAN_RUIN) || 
                feature.equals(StructureFeature.SHIPWRECK) || 
                feature.equals(StructureFeature.BURIED_TREASURE) ||
                feature.equals(BetaStructure.OCEAN_SHRINE_STRUCTURE)) {
                return null;
            }

        return super.locateStructure(world, feature, center, radius, skipExistingChunks);
    }
    
    @Override
    public List<SpawnSettings.SpawnEntry> getEntitySpawnList(Biome biome, StructureAccessor structureAccessor, SpawnGroup spawnGroup, BlockPos blockPos) {
        if (spawnGroup == SpawnGroup.MONSTER) {
            if (structureAccessor.getStructureAt(blockPos, false, BetaStructure.OCEAN_SHRINE_STRUCTURE).hasChildren()) {
                return BetaStructure.OCEAN_SHRINE_STRUCTURE.getMonsterSpawns();
            }
        }

        return super.getEntitySpawnList(biome, structureAccessor, spawnGroup, blockPos);
    }

    @Override
    public int getWorldHeight() {
        return 128;
    }

    @Override
    public int getSeaLevel() {
        return 64;
    }
    
    @Override
    public ChunkGenerator withSeed(long seed) {
        return new OldChunkGenerator(this.biomeSource.withSeed(seed), seed, this.settings);
    }
    
    public WorldType getWorldType() {
        return this.worldType;
    }
    
    public AbstractChunkProvider getChunkProvider() {
        return this.chunkProvider;
    }
    
}
