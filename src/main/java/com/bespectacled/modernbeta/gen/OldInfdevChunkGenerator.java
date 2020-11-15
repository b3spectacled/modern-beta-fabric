package com.bespectacled.modernbeta.gen;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import org.apache.logging.log4j.Level;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.JigsawJunction;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructureStart;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.Util;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceConfig;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.AlphaBiomeSource;
import com.bespectacled.modernbeta.biome.BetaBiomeSource;
import com.bespectacled.modernbeta.biome.InfdevBiomeSource;
import com.bespectacled.modernbeta.decorator.BetaDecorator;
import com.bespectacled.modernbeta.feature.BetaFeature;
import com.bespectacled.modernbeta.gen.settings.AlphaGeneratorSettings;
import com.bespectacled.modernbeta.gen.settings.InfdevGeneratorSettings;
import com.bespectacled.modernbeta.noise.*;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.GenUtil;
import com.bespectacled.modernbeta.util.MutableBiomeArray;
import com.bespectacled.modernbeta.util.IndevUtil.Type;

//private final BetaGeneratorSettings settings;

public class OldInfdevChunkGenerator extends NoiseChunkGenerator implements IOldChunkGenerator {

    public static final Codec<OldInfdevChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
                    Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.worldSeed),
                    AlphaGeneratorSettings.CODEC.fieldOf("settings").forGetter(generator -> generator.settings))
            .apply(instance, instance.stable(OldInfdevChunkGenerator::new)));

    private final AlphaGeneratorSettings settings;
    private final AlphaBiomeSource biomeSource;
    private final long seed;

    private final PerlinOctaveNoise noiseOctavesA;
    private final PerlinOctaveNoise noiseOctavesB;
    private final PerlinOctaveNoise noiseOctavesC;
    private final PerlinOctaveNoise beachNoiseOctaves;
    private final PerlinOctaveNoise stoneNoiseOctaves;
    private final PerlinOctaveNoise forestNoiseOctaves;

    // Block Y-height cache, from Beta+
    private static final Map<BlockPos, Integer> GROUND_CACHE_Y = new HashMap<>();
    private static final int[][] CHUNK_Y = new int[16][16];
    
    private static final double HEIGHTMAP[][] = new double[33][4];
    
    private static final Mutable POS = new Mutable();
    
    private static final Random RAND = new Random();
    private static final ChunkRandom FEATURE_RAND = new ChunkRandom();
    
    private static final ObjectList<StructurePiece> STRUCTURE_LIST = (ObjectList<StructurePiece>) new ObjectArrayList<StructurePiece>(10);
    private static final ObjectList<JigsawJunction> JIGSAW_LIST = (ObjectList<JigsawJunction>) new ObjectArrayList<JigsawJunction>(32);

    public OldInfdevChunkGenerator(BiomeSource biomes, long seed, AlphaGeneratorSettings alphasettings) {
        super(biomes, seed, () -> alphasettings.wrapped);
        this.settings = alphasettings;
        this.biomeSource = (AlphaBiomeSource) biomes;
        this.seed = seed;
        
        RAND.setSeed(seed);
        
        // Noise Generators
        noiseOctavesA = new PerlinOctaveNoise(RAND, 16, false);
        noiseOctavesB = new PerlinOctaveNoise(RAND, 16, false);
        noiseOctavesC = new PerlinOctaveNoise(RAND, 8, false);
        beachNoiseOctaves = new PerlinOctaveNoise(RAND, 4, false);
        stoneNoiseOctaves = new PerlinOctaveNoise(RAND, 4, false);

        new PerlinOctaveNoise(RAND, 5, false); // Unused in original source
        
        forestNoiseOctaves = new PerlinOctaveNoise(RAND, 5, false);

        // Yes this is messy. What else am I supposed to do?
        BetaDecorator.COUNT_INFDEV_NOISE_DECORATOR.setOctaves(forestNoiseOctaves);
        
        GROUND_CACHE_Y.clear();
    }

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR, new Identifier(ModernBeta.ID, "infdev_old"), CODEC);
        //ModernBeta.LOGGER.log(Level.INFO, "Registered Infdev chunk generator.");
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return OldInfdevChunkGenerator.CODEC;
    }

    @Override
    public void populateNoise(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk) {
        RAND.setSeed((long) chunk.getPos().x * 341873128712L + (long) chunk.getPos().z * 132897987541L);

        generateTerrain(chunk, structureAccessor);  
        
        BetaFeature.OLD_FANCY_OAK.chunkReset();
    }
  
    private void generateTerrain(Chunk chunk, StructureAccessor structureAccessor) {
        
    }
    
    @Override
    public int getWorldHeight() {
        return 128;
    }

    @Override
    public int getSeaLevel() {
        return this.settings.wrapped.getSeaLevel();
    }

    @Override
    public ChunkGenerator withSeed(long seed) {
        return new OldInfdevChunkGenerator(this.biomeSource.withSeed(seed), seed, this.settings);
    }
}
