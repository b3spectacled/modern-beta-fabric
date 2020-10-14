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
import net.minecraft.util.registry.BuiltinRegistries;
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

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.BetaBiomeSource;
import com.bespectacled.modernbeta.carver.BetaCarver;
import com.bespectacled.modernbeta.decorator.BetaDecorator;
import com.bespectacled.modernbeta.feature.BetaFeature;
import com.bespectacled.modernbeta.gen.settings.BetaGeneratorSettings;
import com.bespectacled.modernbeta.mixin.MixinBlockColors;
import com.bespectacled.modernbeta.noise.*;
import com.bespectacled.modernbeta.util.BiomeMath;
import com.bespectacled.modernbeta.util.MutableBiomeArray;

//private final BetaGeneratorSettings settings;

public class BetaChunkGenerator extends NoiseChunkGenerator {

    static int noiseWeightX;
    static int noiseWeightY;
    static int noiseWeightZ;

    private static final float[] NOISE_WEIGHT_TABLE = Util.<float[]>make(new float[13824], arr -> {
        for (noiseWeightX = 0; noiseWeightX < 24; ++noiseWeightX) {
            for (noiseWeightY = 0; noiseWeightY < 24; ++noiseWeightY) {
                for (noiseWeightZ = 0; noiseWeightZ < 24; ++noiseWeightZ) {
                    arr[noiseWeightX * 24 * 24 + noiseWeightY * 24 + noiseWeightZ] = (float) calculateNoiseWeight(
                            noiseWeightY - 12, noiseWeightZ - 12, noiseWeightX - 12);
                }
            }
        }
        return;
    });

    public static final Codec<BetaChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
                    Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.worldSeed),
                    BetaGeneratorSettings.CODEC.fieldOf("settings").forGetter(generator -> generator.settings))
            .apply(instance, instance.stable(BetaChunkGenerator::new)));

    private final BetaGeneratorSettings settings;

    private BetaNoiseGeneratorOctaves minLimitNoiseOctaves;
    private BetaNoiseGeneratorOctaves maxLimitNoiseOctaves;
    private BetaNoiseGeneratorOctaves mainNoiseOctaves;
    private BetaNoiseGeneratorOctaves beachNoiseOctaves;
    private BetaNoiseGeneratorOctaves stoneNoiseOctaves;
    public BetaNoiseGeneratorOctaves scaleNoiseOctaves;
    public BetaNoiseGeneratorOctaves depthNoiseOctaves;

    private double heightmap[]; // field_4180_q
    private static double heightmapCache[];

    private double sandNoise[];
    private double gravelNoise[];
    private double stoneNoise[];

    double mainNoise[]; // field_4185_d
    double minLimitNoise[]; // field_4184_e
    double maxLimitNoise[]; // field_4183_f

    double scaleNoise[]; // field_4182_g
    double depthNoise[]; // field_4181_h

    private Random rand;

    BetaBiomeSource biomeSource;
    private double temps[];

    public static long seed;
    // private boolean generateOceans;

    // Block Y-height cache, taken from Beta+
    public Map<BlockPos, Integer> groundCacheY = new HashMap<>();

    public BetaChunkGenerator(BiomeSource biomes, long seed, BetaGeneratorSettings settings) {
        super(biomes, seed, () -> settings.wrapped);
        this.settings = settings;
        this.seed = seed;
        this.rand = new Random(seed);
        this.biomeSource = (BetaBiomeSource) biomes;

        // Noise Generators
        minLimitNoiseOctaves = new BetaNoiseGeneratorOctaves(rand, 16);
        maxLimitNoiseOctaves = new BetaNoiseGeneratorOctaves(rand, 16);
        mainNoiseOctaves = new BetaNoiseGeneratorOctaves(rand, 8);
        beachNoiseOctaves = new BetaNoiseGeneratorOctaves(rand, 4);
        stoneNoiseOctaves = new BetaNoiseGeneratorOctaves(rand, 4);
        scaleNoiseOctaves = new BetaNoiseGeneratorOctaves(rand, 10);
        depthNoiseOctaves = new BetaNoiseGeneratorOctaves(rand, 16);

        // Yes this is messy. What else am I supposed to do?
        BetaDecorator.COUNT_BETA_NOISE_DECORATOR.setSeed(seed);
        ModernBeta.setBlockColorsSeed(seed, false);
        ModernBeta.SEED = seed;
        
        
    }

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR, new Identifier(ModernBeta.ID, "beta"), CODEC);
        ModernBeta.LOGGER.log(Level.INFO, "Registered Beta chunk generator.");
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return BetaChunkGenerator.CODEC;
    }

    @Override
    public void populateNoise(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk) {
        ChunkPos pos = chunk.getPos();

        rand.setSeed((long) chunk.getPos().x * 0x4f9939f508L + (long) chunk.getPos().z * 0x1ef1565bd5L);

        biomeSource.fetchTempHumid(chunk.getPos().x * 16, chunk.getPos().z * 16, 16, 16);
        temps = biomeSource.temps;
        generateTerrain(chunk, temps, structureAccessor);

        MutableBiomeArray mutableBiomes = MutableBiomeArray.inject(chunk.getBiomeArray());
        BlockPos.Mutable mutableBlock = new BlockPos.Mutable();

        // Replace biomes in bodies of water at least four deep with ocean biomes
        for (int x = 0; x < 4; x++) {
            for (int z = 0; z < 4; z++) {
                int absX = pos.getStartX() + (x * 4);
                int absZ = pos.getStartZ() + (z * 4);

                mutableBlock.set(absX, this.getSeaLevel() - 4, absZ);
                BlockState blockstate = chunk.getBlockState(mutableBlock);

                if (blockstate.isOf(Blocks.WATER)) {
                    Biome oceanBiome = biomeSource.getOceanBiomeForNoiseGen(absX, 0, absZ);

                    mutableBiomes.setBiome(absX, 0, absZ, oceanBiome);
                }

            }
        }

    }

    // Modified to accommodate additional ocean biome replacements
    @Override
    public void generateFeatures(ChunkRegion chunkRegion, StructureAccessor structureAccessor) {
        int ctrX = chunkRegion.getCenterChunkX();
        int ctrZ = chunkRegion.getCenterChunkZ();
        int ctrAbsX = ctrX * 16;
        int ctrAbsZ = ctrZ * 16;

        BlockPos pos = new BlockPos(ctrAbsX, 0, ctrAbsZ);
        BlockPos.Mutable mutableBlock = new BlockPos.Mutable();

        Chunk ctrChunk = chunkRegion.getChunk(ctrX, ctrZ);

        int biomeX = (ctrX << 2) + 2;
        int biomeZ = (ctrZ << 2) + 2;

        int absX = biomeX << 2;
        int absZ = biomeZ << 2;

        Biome biome = this.biomeSource.getBiomeForNoiseGen(biomeX, 2, biomeZ);

        mutableBlock.set(absX, 62, absZ);
        BlockState blockstate = ctrChunk.getBlockState(mutableBlock);

        if (blockstate.isOf(Blocks.WATER)) {
            biome = this.biomeSource.getOceanBiomeForNoiseGen(absX, 2, absZ);
        }

        ChunkRandom chunkRand = new ChunkRandom();
        long popSeed = chunkRand.setPopulationSeed(chunkRegion.getSeed(), ctrAbsX, ctrAbsZ);
        try {
            biome.generateFeatureStep(structureAccessor, this, chunkRegion, popSeed, chunkRand, pos);
        } catch (Exception exception) {
            CrashReport report = CrashReport.create(exception, "Biome decoration");
            report.addElement("Generation").add("CenterX", ctrX).add("CenterZ", ctrZ).add("Seed", popSeed).add("Biome",
                    biome);
            throw new CrashException(report);
        }
    }

    // Modified to accommodate additional ocean biome replacements
    @Override
    public void setStructureStarts(DynamicRegistryManager dynamicRegistryManager, StructureAccessor structureAccessor,
            Chunk chunk, StructureManager structureManager, long seed) {
        ChunkPos chunkPos = chunk.getPos();

        int biomeX = (chunkPos.x << 2) + 2;
        int biomeZ = (chunkPos.z << 2) + 2;

        int absX = biomeX << 2;
        int absZ = biomeZ << 2;

        Biome biome = this.biomeSource.getBiomeForNoiseGen(biomeX, 0, biomeZ);

        // Cannot simply just check blockstate for chunks that do not yet exist...
        // Will have to simulate heightmap for some distant chunk

        biomeSource.fetchTempHumid(chunkPos.x * 16, chunkPos.z * 16, 16, 16);
        int[][] chunkY = sampleHeightmap(chunkPos);

        int thisY = chunkY[Math.abs(absX % 16)][Math.abs(absZ % 16)];

        if (thisY <= this.getSeaLevel() - 4) {
            biome = this.biomeSource.getOceanBiomeForNoiseGen(absX, 0, absZ);
        }

        this.setStructureStart(ConfiguredStructureFeatures.STRONGHOLD, dynamicRegistryManager, structureAccessor, chunk,
                structureManager, seed, chunkPos, biome);
        for (final Supplier<ConfiguredStructureFeature<?, ?>> supplier : biome.getGenerationSettings()
                .getStructureFeatures()) {
            this.setStructureStart(supplier.get(), dynamicRegistryManager, structureAccessor, chunk, structureManager,
                    seed, chunkPos, biome);
        }
    }

    // Modified to accommodate additional ocean biome replacements
    private void setStructureStart(ConfiguredStructureFeature<?, ?> configuredStructureFeature,
            DynamicRegistryManager dynamicRegistryManager, StructureAccessor structureAccessor, Chunk chunk,
            StructureManager structureManager, long long7, ChunkPos chunkPos, Biome biome) {
        StructureStart<?> structureStart = structureAccessor.getStructureStart(ChunkSectionPos.from(chunk.getPos(), 0),
                configuredStructureFeature.feature, chunk);
        int refs = (structureStart != null) ? structureStart.getReferences() : 0;

        // StructureConfig structureConfig13 =
        // this.structuresConfig.getForType(configuredStructureFeature.feature);
        StructureConfig structureConfig = this.settings.wrapped.getStructuresConfig()
                .getForType(configuredStructureFeature.feature);

        if (structureConfig != null) {
            StructureStart<?> gotStart = configuredStructureFeature.tryPlaceStart(dynamicRegistryManager, this,
                    this.biomeSource, structureManager, long7, chunkPos, biome, refs, structureConfig);
            structureAccessor.setStructureStart(ChunkSectionPos.from(chunk.getPos(), 0),
                    configuredStructureFeature.feature, gotStart, chunk);
        }
    }

    @Override
    public void carve(long seed, BiomeAccess biomeAccess, Chunk chunk, GenerationStep.Carver carver) {
        BiomeAccess biomeAcc = biomeAccess.withSource(this.biomeSource);
        ChunkPos chunkPos = chunk.getPos();

        int mainChunkX = chunkPos.x;
        int mainChunkZ = chunkPos.z;

        int biomeX = mainChunkX << 2;
        int biomeZ = mainChunkZ << 2;

        int absX = biomeX << 2;
        int absZ = biomeZ << 2;

        GenerationSettings generationSettings = this.biomeSource.getBiomeForNoiseGen(biomeX, 0, biomeZ)
                .getGenerationSettings();
        BitSet bitSet = ((ProtoChunk) chunk).getOrCreateCarvingMask(carver);

        BlockPos.Mutable mutableBlock = new BlockPos.Mutable();

        mutableBlock.set(absX, 62, absZ);
        BlockState blockstate = chunk.getBlockState(mutableBlock);

        if (blockstate.isOf(Blocks.WATER)) {
            generationSettings = this.biomeSource.getOceanBiomeForNoiseGen(absX, 0, absZ).getGenerationSettings();
        }

        Random rand = new Random(seed);
        long l = (rand.nextLong() / 2L) * 2L + 1L;
        long l1 = (rand.nextLong() / 2L) * 2L + 1L;

        for (int chunkX = mainChunkX - 8; chunkX <= mainChunkX + 8; ++chunkX) {
            for (int chunkZ = mainChunkZ - 8; chunkZ <= mainChunkZ + 8; ++chunkZ) {
                List<Supplier<ConfiguredCarver<?>>> carverList = generationSettings.getCarversForStep(carver);
                ListIterator<Supplier<ConfiguredCarver<?>>> carverIterator = carverList.listIterator();

                while (carverIterator.hasNext()) {
                    // int carverNextIdx = carverIterator.nextIndex();

                    ConfiguredCarver<?> configuredCarver = carverIterator.next().get();

                    rand.setSeed((long) chunkX * l + (long) chunkZ * l1 ^ seed);

                    if (configuredCarver.shouldCarve(rand, chunkX, chunkZ)) {
                        configuredCarver.carve(chunk, biomeAcc::getBiome, rand, this.getSeaLevel(), chunkX, chunkZ,
                                mainChunkX, mainChunkZ, bitSet);

                    }
                }
            }
        }
    }

    @Override
    public void buildSurface(ChunkRegion chunkRegion, Chunk chunk) {

        // Do not use the built-in surface builders..
        // This works better for Beta-accurate surface generation anyway.
        buildBetaSurface(chunk);
    }

    /*
     * @Override public BlockPos locateStructure(ServerWorld world,
     * StructureFeature<?> feature, BlockPos center, int radius, boolean
     * skipExistingChunks) { if ((feature.equals(StructureFeature.OCEAN_RUIN) ||
     * feature.equals(StructureFeature.SHIPWRECK)) && !generateOceans) { return
     * null; }
     * 
     * return super.locateStructure(world, feature, center, radius,
     * skipExistingChunks); }
     */

    public void generateTerrain(Chunk chunk, double[] temps, StructureAccessor structureAccessor) {
        byte byte4 = 4;
        // byte seaLevel = (byte)this.getSeaLevel();
        byte byte17 = 17;

        int int5_0 = byte4 + 1;
        int int5_1 = byte4 + 1;

        BlockPos.Mutable mutableBlock = new BlockPos.Mutable();
        Heightmap heightmapOCEAN = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSURFACE = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);

        // Not working, densities are calculated differently now.
        ObjectList<StructurePiece> structureList = (ObjectList<StructurePiece>) new ObjectArrayList(10);
        ObjectList<JigsawJunction> jigsawList = (ObjectList<JigsawJunction>) new ObjectArrayList(32);

        for (final StructureFeature<?> s : StructureFeature.JIGSAW_STRUCTURES) {

            structureAccessor.getStructuresWithChildren(ChunkSectionPos.from(chunk.getPos(), 0), s)
                    .forEach(structureStart -> {
                        Iterator<StructurePiece> structurePieceIterator;
                        StructurePiece structurePiece;

                        Iterator<JigsawJunction> jigsawJunctionIterator;
                        JigsawJunction jigsawJunction;

                        ChunkPos arg2 = chunk.getPos();

                        PoolStructurePiece poolStructurePiece;
                        StructurePool.Projection structureProjection;

                        int integer13;
                        int integer14;
                        int n2 = arg2.x;
                        int n3 = arg2.z;

                        structurePieceIterator = structureStart.getChildren().iterator();
                        while (structurePieceIterator.hasNext()) {
                            structurePiece = structurePieceIterator.next();
                            if (!structurePiece.intersectsChunk(arg2, 12)) {
                                continue;
                            } else if (structurePiece instanceof PoolStructurePiece) {
                                poolStructurePiece = (PoolStructurePiece) structurePiece;
                                structureProjection = poolStructurePiece.getPoolElement().getProjection();

                                if (structureProjection == StructurePool.Projection.RIGID) {
                                    structureList.add(poolStructurePiece);
                                }
                                jigsawJunctionIterator = poolStructurePiece.getJunctions().iterator();
                                while (jigsawJunctionIterator.hasNext()) {
                                    jigsawJunction = jigsawJunctionIterator.next();
                                    integer13 = jigsawJunction.getSourceX();
                                    integer14 = jigsawJunction.getSourceZ();
                                    if (integer13 > n2 - 12 && integer14 > n3 - 12 && integer13 < n2 + 15 + 12) {
                                        if (integer14 >= n3 + 15 + 12) {
                                            continue;
                                        } else {
                                            jigsawList.add(jigsawJunction);
                                        }
                                    }
                                }
                            } else {
                                structureList.add(structurePiece);
                            }
                        }
                        return;
                    });
        }

        ObjectListIterator<StructurePiece> structureListIterator = (ObjectListIterator<StructurePiece>) structureList
                .iterator();
        ObjectListIterator<JigsawJunction> jigsawListIterator = (ObjectListIterator<JigsawJunction>) jigsawList
                .iterator();

        heightmap = generateHeightmap(heightmap, chunk.getPos().x * byte4, 0, chunk.getPos().z * byte4, int5_0, byte17,
                int5_1);

        // Noise is sampled in 4x16x4 sections?
        for (int i = 0; i < byte4; i++) { // [1.16] Limit appears to be equivalent to noiseSizeX, equal to 16 /
                                          // horizontalNoiseResolution // 16 / 1 * 4
            for (int j = 0; j < byte4; j++) { // [1.16] Limit appears to be equivalent to noiseSizeZ, equal to 16 /
                                              // horizontalNoiseResolution // 16 / 1 * 4
                for (int k = 0; k < 16; k++) { // [1.16] Appears to be similar to 'for (int q = this.noiseSizeY - 1; q
                                               // >= 0; --q) {'
                                               // where noiseSizeY is equal to generationShapeConfig.getHeight() /
                                               // this.verticalNoiseResolution // 128 [for Beta] / (2 * 4)
                    double eighth = 0.125D;

                    double var1 = heightmap[((i + 0) * int5_1 + (j + 0)) * byte17 + (k + 0)];
                    double var2 = heightmap[((i + 0) * int5_1 + (j + 1)) * byte17 + (k + 0)];
                    double var3 = heightmap[((i + 1) * int5_1 + (j + 0)) * byte17 + (k + 0)];
                    double var4 = heightmap[((i + 1) * int5_1 + (j + 1)) * byte17 + (k + 0)];

                    double var5 = (heightmap[((i + 0) * int5_1 + (j + 0)) * byte17 + (k + 1)] - var1) * eighth; // Lerped
                                                                                                                // by
                                                                                                                // this
                                                                                                                // amount,
                                                                                                                // (var5
                                                                                                                // -
                                                                                                                // var1)
                                                                                                                // *
                                                                                                                // 0.125D
                    double var6 = (heightmap[((i + 0) * int5_1 + (j + 1)) * byte17 + (k + 1)] - var2) * eighth;
                    double var7 = (heightmap[((i + 1) * int5_1 + (j + 0)) * byte17 + (k + 1)] - var3) * eighth;
                    double var8 = (heightmap[((i + 1) * int5_1 + (j + 1)) * byte17 + (k + 1)] - var4) * eighth;

                    for (int l = 0; l < 8; l++) { // [1.16] Limit appears to be equivalent to verticalNoiseResolution,
                                                  // equal to getSizeVertical() * 4 // 2 * 4
                        double quarter = 0.25D;
                        double var10 = var1;
                        double var11 = var2;
                        double var12 = (var3 - var1) * quarter; // Lerp
                        double var13 = (var4 - var2) * quarter;

                        int integer40 = k * 8 + l;

                        for (int m = 0; m < 4; m++) { // [1.16] Limit appears to be equivalent to
                                                      // horizontalNoiseResolution, equal to getSizeHorizontal() * 4 //
                                                      // 1 * 4
                            int x = (m + i * 4);
                            int y = k * 8 + l;
                            int z = (j * 4);

                            double var14 = 0.25D;
                            double density = var10; // var15
                            double var16 = (var11 - var10) * var14; // More lerp

                            int integer54 = (chunk.getPos().x << 4) + i * 4 + m;

                            for (int n = 0; n < 4; n++) { // [1.16] Limit appears to be equivalent to
                                                          // horizontalNoiseResolution, equal to getSizeHorizontal() * 4
                                                          // // 1 * 4

                                int integer63 = (chunk.getPos().z << 4) + j * 4 + n;

                                double temp = temps[(i * 4 + m) * 16 + (j * 4 + n)];

                                double noiseWeight;

                                while (structureListIterator.hasNext()) {
                                    StructurePiece curStructurePiece = (StructurePiece) structureListIterator.next();
                                    BlockBox blockBox = curStructurePiece.getBoundingBox();

                                    int sX = Math.max(0,
                                            Math.max(blockBox.minX - integer54, integer54 - blockBox.maxX));
                                    int sY = y - (blockBox.minY + ((curStructurePiece instanceof PoolStructurePiece)
                                            ? ((PoolStructurePiece) curStructurePiece).getGroundLevelDelta()
                                            : 0));
                                    int sZ = Math.max(0,
                                            Math.max(blockBox.minZ - integer63, integer63 - blockBox.maxZ));

                                    //density += getNoiseWeight(sX, sY, sZ) * 0.8;
                                    // Temporary fix
                                    if (sY < 0 && sX == 0 && sZ == 0)
                                        density += density * density / 0.1;
                                }
                                structureListIterator.back(structureList.size());

                                while (jigsawListIterator.hasNext()) {
                                    JigsawJunction curJigsawJunction = (JigsawJunction) jigsawListIterator.next();

                                    int jX = integer54 - curJigsawJunction.getSourceX();
                                    int jY = y - curJigsawJunction.getSourceGroundY();
                                    int jZ = integer63 - curJigsawJunction.getSourceZ();

                                    //density += getNoiseWeight(jX, jY, jZ) * 0.4;
                                    // Temporary fix
                                    if (jY < 0 && jX == 0 && jZ == 0)
                                        density += density * density / 0.1;
                                }
                                jigsawListIterator.back(jigsawList.size());

                                BlockState blockToSet = this.getBlockState(density, y, temp);

                                chunk.setBlockState(mutableBlock.set(x, y, z), blockToSet, false);

                                heightmapOCEAN.trackUpdate(x, y, z, blockToSet);
                                heightmapSURFACE.trackUpdate(x, y, z, blockToSet);

                                ++z;
                                density += var16;
                            }

                            var10 += var12;
                            var11 += var13;
                        }

                        var1 += var5;
                        var2 += var6;
                        var3 += var7;
                        var4 += var8;
                    }
                }
            }
        }
    }

    private double[] generateHeightmap(double heightmap[], int x, int y, int z, int int5_0, int byte17, int int5_1) {
        if (heightmap == null) {
            heightmap = new double[int5_0 * byte17 * int5_1];
        }

        // Var names taken from old customized preset names
        double coordinateScale = 684.41200000000003D; // d
        double heightScale = 684.41200000000003D; // d1

        double depthNoiseScaleX = 200D;
        double depthNoiseScaleZ = 200D;
        double depthNoiseScaleExponent = 0.5D;

        double mainNoiseScaleX = 80D;
        double mainNoiseScaleY = 160D;
        double mainNoiseScaleZ = 80D;

        double lowerLimitScale = 512D;
        double upperLimitScale = 512D;

        double temps[] = biomeSource.temps;
        double humids[] = biomeSource.humids;

        scaleNoise = scaleNoiseOctaves.func_4109_a(scaleNoise, x, z, int5_0, int5_1, 1.121D, 1.121D, 0.5D);
        depthNoise = depthNoiseOctaves.func_4109_a(depthNoise, x, z, int5_0, int5_1, depthNoiseScaleX, depthNoiseScaleZ,
                depthNoiseScaleExponent);

        mainNoise = mainNoiseOctaves.generateNoiseOctaves(mainNoise, x, y, z, int5_0, byte17, int5_1,
                coordinateScale / mainNoiseScaleX, heightScale / mainNoiseScaleY, coordinateScale / mainNoiseScaleZ);

        minLimitNoise = minLimitNoiseOctaves.generateNoiseOctaves(minLimitNoise, x, y, z, int5_0, byte17, int5_1,
                coordinateScale, heightScale, coordinateScale);

        maxLimitNoise = maxLimitNoiseOctaves.generateNoiseOctaves(maxLimitNoise, x, y, z, int5_0, byte17, int5_1,
                coordinateScale, heightScale, coordinateScale);

        int i = 0;
        int j = 0;
        int k = 16 / int5_0;

        for (int l = 0; l < int5_0; l++) {
            int idx0 = l * k + k / 2;

            for (int m = 0; m < int5_1; m++) {
                int idx1 = m * k + k / 2;

                double curTemp = temps[idx0 * 16 + idx1];
                double curHumid = humids[idx0 * 16 + idx1] * curTemp;

                double humidMod = 1.0D - curHumid;
                humidMod *= humidMod;
                humidMod *= humidMod;
                humidMod = 1.0D - humidMod;

                double scaleMod = (scaleNoise[j] + 256D) / 512D;
                scaleMod *= humidMod;

                if (scaleMod > 1.0D) {
                    scaleMod = 1.0D;
                }

                double depthMod = depthNoise[j] / 8000D;

                if (depthMod < 0.0D) {
                    depthMod = -depthMod * 0.29999999999999999D;
                }

                depthMod = depthMod * 3D - 2D;

                if (depthMod < 0.0D) {
                    depthMod /= 2D;

                    if (depthMod < -1D) {
                        depthMod = -1D;
                    }

                    depthMod /= 1.3999999999999999D;
                    depthMod /= 2D;

                    scaleMod = 0.0D;

                } else {
                    if (depthMod > 1.0D) {
                        depthMod = 1.0D;
                    }
                    depthMod /= 8D;
                }

                if (scaleMod < 0.0D) {
                    scaleMod = 0.0D;
                }

                scaleMod += 0.5D;
                depthMod = (depthMod * (double) byte17) / 16D;

                double depthMod2 = (double) byte17 / 2D + depthMod * 4D;

                j++;

                for (int n = 0; n < byte17; n++) {
                    double heightVal = 0.0D;
                    double scaleMod2 = (((double) n - depthMod2) * 12D) / scaleMod;

                    if (scaleMod2 < 0.0D) {
                        scaleMod2 *= 4D;
                    }

                    double minLimitMod = minLimitNoise[i] / lowerLimitScale;
                    double maxLimitMod = maxLimitNoise[i] / upperLimitScale;
                    double mainNoiseMod = (mainNoise[i] / 10D + 1.0D) / 2D;

                    if (mainNoiseMod < 0.0D) {
                        heightVal = minLimitMod;
                    } else if (mainNoiseMod > 1.0D) {
                        heightVal = maxLimitMod;
                    } else {
                        heightVal = minLimitMod + (maxLimitMod - minLimitMod) * mainNoiseMod;
                    }
                    heightVal -= scaleMod2;

                    if (n > byte17 - 4) {
                        double d13 = (float) (n - (byte17 - 4)) / 3F;
                        heightVal = heightVal * (1.0D - d13) + -10D * d13;
                    }
                    heightmap[i] = heightVal;
                    i++;
                }

            }

        }

        return heightmap;
    }

    private void buildBetaSurface(Chunk chunk) {
        byte seaLevel = (byte) this.getSeaLevel();
        double thirtysecond = 0.03125D; // eighth

        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;

        biomeSource.fetchTempHumid(chunkX * 16, chunkZ * 16, 16, 16);
        BlockPos.Mutable mutableBlock = new BlockPos.Mutable();

        Biome curBiome;

        sandNoise = beachNoiseOctaves.generateNoiseOctaves(sandNoise, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1,
                thirtysecond, thirtysecond, 1.0D);
        gravelNoise = beachNoiseOctaves.generateNoiseOctaves(gravelNoise, chunkX * 16, 109.0134D, chunkZ * 16, 16, 1,
                16, thirtysecond, 1.0D, thirtysecond);
        stoneNoise = stoneNoiseOctaves.generateNoiseOctaves(stoneNoise, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1,
                thirtysecond * 2D, thirtysecond * 2D, thirtysecond * 2D);

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {

                boolean genSandBeach = sandNoise[i + j * 16] + rand.nextDouble() * 0.20000000000000001D > 0.0D;
                boolean genGravelBeach = gravelNoise[i + j * 16] + rand.nextDouble() * 0.20000000000000001D > 3D;

                int genStone = (int) (stoneNoise[i + j * 16] / 3D + 3D + rand.nextDouble() * 0.25D);
                int flag = -1;

                curBiome = biomeSource.biomesInChunk[i + j * 16];

                Block biomeTopBlock = curBiome.getGenerationSettings().getSurfaceConfig().getTopMaterial().getBlock();
                Block biomeFillerBlock = curBiome.getGenerationSettings().getSurfaceConfig().getUnderMaterial().getBlock();

                Block topBlock = biomeTopBlock;
                Block fillerBlock = biomeFillerBlock;

                // Generate from top to bottom of world
                for (int y = 127; y >= 0; y--) {

                    // Randomly place bedrock from y=0 to y=5
                    if (y <= 0 + rand.nextInt(5)) {
                        chunk.setBlockState(mutableBlock.set(j, y, i), Blocks.BEDROCK.getDefaultState(), false);
                        continue;
                    }

                    Block someBlock = chunk.getBlockState(mutableBlock.set(j, y, i)).getBlock();

                    if (someBlock.equals(Blocks.AIR)) { // Skip if air block
                        flag = -1;
                        continue;
                    }

                    if (!someBlock.equals(Blocks.STONE)) { // Skip if not stone
                        continue;
                    }

                    if (flag == -1) {
                        if (genStone <= 0) { // Generate stone basin if noise permits
                            topBlock = Blocks.AIR;
                            fillerBlock = Blocks.STONE;
                        } else if (y >= seaLevel - 4 && y <= seaLevel + 1) { // Generate beaches at this y range
                            topBlock = biomeTopBlock;
                            fillerBlock = biomeFillerBlock;

                            if (genGravelBeach) {
                                topBlock = Blocks.AIR; // This reduces gravel beach height by 1
                                fillerBlock = Blocks.GRAVEL;
                            }

                            if (genSandBeach) {
                                topBlock = Blocks.SAND;
                                fillerBlock = Blocks.SAND;
                            }
                        }

                        if (y < seaLevel && topBlock.equals(Blocks.AIR)) { // Generate water bodies
                            topBlock = Blocks.WATER;
                        }

                        // Main surface builder section
                        flag = genStone;
                        if (y >= seaLevel - 1) {
                            chunk.setBlockState(mutableBlock.set(j, y, i), topBlock.getDefaultState(), false);
                        } else {
                            chunk.setBlockState(mutableBlock.set(j, y, i), fillerBlock.getDefaultState(), false);
                        }

                        continue;
                    }

                    if (flag <= 0) {
                        continue;
                    }

                    flag--;
                    chunk.setBlockState(mutableBlock.set(j, y, i), fillerBlock.getDefaultState(), false);

                    // Generates layer of sandstone starting at lowest block of sand, of height 1 to
                    // 4.
                    if (flag == 0 && fillerBlock.equals(Blocks.SAND)) {
                        flag = rand.nextInt(4);
                        fillerBlock = Blocks.SANDSTONE;
                    }
                }
            }
        }
    }

    protected BlockState getBlockState(double density, int y, double temp) {
        BlockState blockStateToSet = Blocks.AIR.getDefaultState();
        if (density > 0.0) {
            blockStateToSet = this.settings.wrapped.getDefaultBlock();
        } else if (y < this.getSeaLevel()) {
            if (temp < 0.5D && y >= this.getSeaLevel() - 1) {
                // blockStateToSet = Blocks.ICE.getDefaultState(); // Get chunk errors so
                // disabled for now.
                blockStateToSet = this.settings.wrapped.getDefaultFluid();
            } else {
                blockStateToSet = this.settings.wrapped.getDefaultFluid();
            }

        }
        return blockStateToSet;
    }

    // From NoiseChunkGenerator
    private static double getNoiseWeight(int x, int y, int z) {
        int i = x + 12;
        int j = y + 12;
        int k = z + 12;
        if (i < 0 || i >= 24) {
            return 0.0;
        }
        if (j < 0 || j >= 24) {
            return 0.0;
        }
        if (k < 0 || k >= 24) {
            return 0.0;
        }

        double weight = NOISE_WEIGHT_TABLE[k * 24 * 24 + i * 24 + j];

        return weight;
    }

    // From NoiseChunkGenerator
    private static double calculateNoiseWeight(int x, int y, int z) {
        double var1 = x * x + z * z;
        double var2 = y + 0.5;
        double var3 = var2 * var2;
        double var4 = Math.pow(2.718281828459045, -(var3 / 16.0 + var1 / 16.0));
        double var5 = -var2 * MathHelper.fastInverseSqrt(var3 / 2.0 + var1 / 2.0) / 2.0;
        return var5 * var4;
    }

    // Called only when generating structures
    @Override
    public int getHeight(int x, int z, Heightmap.Type type) {

        BlockPos blockPos = new BlockPos(x, 0, z);
        ChunkPos chunkPos = new ChunkPos(blockPos);

        if (groundCacheY.get(blockPos) == null) {
            biomeSource.fetchTempHumid(chunkPos.x * 16, chunkPos.z * 16, 16, 16);
            sampleHeightmap(chunkPos);
        }

        int groundHeight = groundCacheY.get(blockPos);

        // Not ideal
        if (type == Heightmap.Type.WORLD_SURFACE_WG && groundHeight < this.getSeaLevel())
            groundHeight = this.getSeaLevel();

        return groundHeight;
    }

    private int[][] sampleHeightmap(ChunkPos chunkPos) {
        byte byte4 = 4;
        // byte seaLevel = (byte)this.getSeaLevel();
        byte byte17 = 17;

        int int5_0 = byte4 + 1;
        int int5_1 = byte4 + 1;

        heightmapCache = generateHeightmap(heightmapCache, chunkPos.x * byte4, 0, chunkPos.z * byte4, int5_0, byte17,
                int5_1);

        int[][] chunkY = new int[16][16];

        for (int i = 0; i < byte4; i++) {
            for (int j = 0; j < byte4; j++) {
                for (int k = 0; k < 16; k++) {
                    double eighth = 0.125D;

                    double var1 = heightmapCache[((i + 0) * int5_1 + (j + 0)) * byte17 + (k + 0)];
                    double var2 = heightmapCache[((i + 0) * int5_1 + (j + 1)) * byte17 + (k + 0)];
                    double var3 = heightmapCache[((i + 1) * int5_1 + (j + 0)) * byte17 + (k + 0)];
                    double var4 = heightmapCache[((i + 1) * int5_1 + (j + 1)) * byte17 + (k + 0)];

                    double var5 = (heightmapCache[((i + 0) * int5_1 + (j + 0)) * byte17 + (k + 1)] - var1) * eighth;
                    double var6 = (heightmapCache[((i + 0) * int5_1 + (j + 1)) * byte17 + (k + 1)] - var2) * eighth;
                    double var7 = (heightmapCache[((i + 1) * int5_1 + (j + 0)) * byte17 + (k + 1)] - var3) * eighth;
                    double var8 = (heightmapCache[((i + 1) * int5_1 + (j + 1)) * byte17 + (k + 1)] - var4) * eighth;

                    for (int l = 0; l < 8; l++) {
                        double var9 = 0.25D;
                        double var10 = var1;
                        double var11 = var2;
                        double var12 = (var3 - var1) * var9;
                        double var13 = (var4 - var2) * var9;

                        for (int m = 0; m < 4; m++) {
                            int x = (m + i * 4);
                            int y = k * 8 + l;
                            int z = (j * 4);

                            double var14 = 0.25D;
                            double density = var10; // var15
                            double var16 = (var11 - var10) * var14;

                            for (int n = 0; n < 4; n++) {
                                if (density > 0.0) {
                                    chunkY[x][z] = y;
                                }

                                ++z;
                                density += var16;
                            }

                            var10 += var12;
                            var11 += var13;
                        }

                        var1 += var5;
                        var2 += var6;
                        var3 += var7;
                        var4 += var8;
                    }
                }
            }
        }

        for (int pX = 0; pX < chunkY.length; pX++) {
            for (int pZ = 0; pZ < chunkY[pX].length; pZ++) {
                BlockPos pos = new BlockPos(chunkPos.getStartX() + pX, 0, chunkPos.getStartZ() + pZ);
                groundCacheY.put(pos, chunkY[pX][pZ] + 1); // +1 because it is one above the ground
            }
        }

        return chunkY;
    }

    @Override
    public int getMaxY() {
        return 128;
    }

    @Override
    public int getSeaLevel() {
        return 64;
    }

    @Override
    public ChunkGenerator withSeed(long seed) {
        return new BetaChunkGenerator(this.biomeSource.withSeed(seed), seed, this.settings);
    }

}
