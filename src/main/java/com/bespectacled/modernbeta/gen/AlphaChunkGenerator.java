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

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.AlphaBiomeSource;
import com.bespectacled.modernbeta.biome.BetaBiomeSource;
import com.bespectacled.modernbeta.decorator.BetaDecorator;
import com.bespectacled.modernbeta.gen.settings.AlphaGeneratorSettings;
import com.bespectacled.modernbeta.noise.*;
import com.bespectacled.modernbeta.util.MutableBiomeArray;
import com.bespectacled.modernbeta.util.BiomeMath;
import com.bespectacled.modernbeta.util.BlockStates;

//private final BetaGeneratorSettings settings;

public class AlphaChunkGenerator extends NoiseChunkGenerator {

    public static final Codec<AlphaChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
                    Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.worldSeed),
                    AlphaGeneratorSettings.CODEC.fieldOf("settings").forGetter(generator -> generator.settings))
            .apply(instance, instance.stable(AlphaChunkGenerator::new)));

    private final AlphaGeneratorSettings settings;
    private final AlphaBiomeSource biomeSource;
    private final long seed;

    private final OldNoiseGeneratorOctaves minLimitNoiseOctaves;
    private final OldNoiseGeneratorOctaves maxLimitNoiseOctaves;
    private final OldNoiseGeneratorOctaves mainNoiseOctaves;
    private final OldNoiseGeneratorOctaves beachNoiseOctaves;
    private final OldNoiseGeneratorOctaves stoneNoiseOctaves;
    private final OldNoiseGeneratorOctaves scaleNoiseOctaves;
    private final OldNoiseGeneratorOctaves depthNoiseOctaves;
    private final OldNoiseGeneratorOctaves forestNoiseOctaves;

    private double sandNoise[];
    private double gravelNoise[];
    private double stoneNoise[];

    private double mainNoise[]; 
    private double minLimitNoise[];
    private double maxLimitNoise[];

    private double scaleNoise[];
    private double depthNoise[];

    // Block Y-height cache, from Beta+
    private static final Map<BlockPos, Integer> GROUND_CACHE_Y = new HashMap<>();
    private static final int[][] CHUNK_Y = new int[16][16];
    
    private static final double HEIGHTMAP[] = new double[425];
    
    private static final Mutable POS = new Mutable();
    
    private static final Random RAND = new Random();
    private static final ChunkRandom FEATURE_RAND = new ChunkRandom();
    
    private static final ObjectList<StructurePiece> STRUCTURE_LIST = (ObjectList<StructurePiece>) new ObjectArrayList(10);
    private static final ObjectList<JigsawJunction> JIGSAW_LIST = (ObjectList<JigsawJunction>) new ObjectArrayList(32);
    
    private static final double[] TEMPS = new double[256];
    private static final double[] HUMIDS = new double[256];
    
    private static final Biome[] BIOMES = new Biome[256];

    public AlphaChunkGenerator(BiomeSource biomes, long seed, AlphaGeneratorSettings settings) {
        super(biomes, seed, () -> settings.wrapped);
        this.settings = settings;
        this.biomeSource = (AlphaBiomeSource) biomes;
        this.seed = seed;
        
        RAND.setSeed(seed);
        
        // Noise Generators
        minLimitNoiseOctaves = new OldNoiseGeneratorOctaves(RAND, 16, false);
        maxLimitNoiseOctaves = new OldNoiseGeneratorOctaves(RAND, 16, false);
        mainNoiseOctaves = new OldNoiseGeneratorOctaves(RAND, 8, false);
        beachNoiseOctaves = new OldNoiseGeneratorOctaves(RAND, 4, false);
        stoneNoiseOctaves = new OldNoiseGeneratorOctaves(RAND, 4, false);
        scaleNoiseOctaves = new OldNoiseGeneratorOctaves(RAND, 10, false);
        depthNoiseOctaves = new OldNoiseGeneratorOctaves(RAND, 16, false);
        forestNoiseOctaves = new OldNoiseGeneratorOctaves(RAND, 8, false);

        // Yes this is messy. What else am I supposed to do?
        BetaDecorator.COUNT_ALPHA_NOISE_DECORATOR.setOctaves(forestNoiseOctaves);
        ModernBeta.setBlockColorsSeed(0L, true);
    }

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR, new Identifier(ModernBeta.ID, "alpha"), CODEC);
        ModernBeta.LOGGER.log(Level.INFO, "Registered Alpha chunk generator.");
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return AlphaChunkGenerator.CODEC;
    }

    @Override
    public void populateNoise(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk) {
        ChunkPos pos = chunk.getPos();

        RAND.setSeed((long) chunk.getPos().x * 341873128712L + (long) chunk.getPos().z * 132897987541L);

        generateTerrain(chunk, structureAccessor);

        /*
         * MutableBiomeArray mutableBiomes =
         * MutableBiomeArray.inject(chunk.getBiomeArray()); BlockPos.Mutable
         * 
         * // Replace biomes in bodies of water at least four deep with ocean biomes for
         * (int x = 0; x < 4; x++) { for (int z = 0; z < 4; z++) { int absX =
         * pos.getStartX() + (x * 4); int absZ = pos.getStartZ() + (z * 4);
         * 
         * mutableBlock.set(absX, this.getSeaLevel() - 4, absZ); BlockState blockstate =
         * chunk.getBlockState(mutableBlock);
         * 
         * if (blockstate.isOf(Blocks.WATER)) { Biome oceanBiome =
         * biomeSource.getOceanBiomeForNoiseGen(absX, 0, absZ);
         * 
         * mutableBiomes.setBiome(absX, 0, absZ, oceanBiome); }
         * 
         * } }
         * 
         */
    }

    // Modified to accommodate additional ocean biome replacements
    @Override
    public void generateFeatures(ChunkRegion chunkRegion, StructureAccessor structureAccessor) {
        int ctrX = chunkRegion.getCenterChunkX();
        int ctrZ = chunkRegion.getCenterChunkZ();
        int ctrAbsX = ctrX * 16;
        int ctrAbsZ = ctrZ * 16;
        
        Chunk ctrChunk = chunkRegion.getChunk(ctrX, ctrZ);

        int biomeX = (ctrX << 2) + 2;
        int biomeZ = (ctrZ << 2) + 2;

        int absX = biomeX << 2;
        int absZ = biomeZ << 2;

        Biome biome = this.biomeSource.getBiomeForNoiseGen(biomeX, 2, biomeZ);

        /*
         * mutableBlock.set(absX, 62, absZ); BlockState blockstate =
         * ctrChunk.getBlockState(mutableBlock);
         * 
         * if (blockstate.isOf(Blocks.WATER)) { biome =
         * this.biomeSource.getOceanBiomeForNoiseGen(absX, 2, absZ); }
         */

        long popSeed = FEATURE_RAND.setPopulationSeed(chunkRegion.getSeed(), ctrAbsX, ctrAbsZ);
        
        try {
            biome.generateFeatureStep(structureAccessor, this, chunkRegion, popSeed, FEATURE_RAND, POS.set(ctrAbsX, 0, ctrAbsZ));
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

        /*
        biomeSource.fetchTempHumid(chunkPos.x * 16, chunkPos.z * 16, 16, 16);
        sampleHeightmap(chunkPos.getStartX(), chunkPos.getStartZ());

        int thisY = CHUNK_Y[Math.abs(absX % 16)][Math.abs(absZ % 16)];

        if (thisY <= this.getSeaLevel() - 4) {
            biome = this.biomeSource.getOceanBiomeForNoiseGen(absX, 0, absZ);
        }
         */

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

        /*
         POS.set(absX, 62, absZ);
        BlockState blockstate = chunk.getBlockState(POS);

        if (blockstate.isOf(Blocks.WATER)) {
            generationSettings = this.biomeSource.getOceanBiomeForNoiseGen(absX, 0, absZ).getGenerationSettings();
        }

         */

        RAND.setSeed(this.seed);
        long l = (RAND.nextLong() / 2L) * 2L + 1L;
        long l1 = (RAND.nextLong() / 2L) * 2L + 1L;
        
        for (int chunkX = mainChunkX - 8; chunkX <= mainChunkX + 8; ++chunkX) {
            for (int chunkZ = mainChunkZ - 8; chunkZ <= mainChunkZ + 8; ++chunkZ) {
                List<Supplier<ConfiguredCarver<?>>> carverList = generationSettings.getCarversForStep(carver);
                ListIterator<Supplier<ConfiguredCarver<?>>> carverIterator = carverList.listIterator();

                while (carverIterator.hasNext()) {
                    ConfiguredCarver<?> configuredCarver = carverIterator.next().get();

                    RAND.setSeed((long) chunkX * l + (long) chunkZ * l1 ^ seed);

                    if (configuredCarver.shouldCarve(RAND, chunkX, chunkZ)) {
                        configuredCarver.carve(chunk, biomeAcc::getBiome, RAND, this.getSeaLevel(), chunkX, chunkZ,
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
        buildAlphaSurface(chunk);
    }

    @Override 
    public BlockPos locateStructure(ServerWorld world,  StructureFeature<?> feature, BlockPos center, int radius, boolean skipExistingChunks) { 
        if ((feature.equals(StructureFeature.OCEAN_RUIN) || 
            feature.equals(StructureFeature.SHIPWRECK)) || 
            feature.equals(StructureFeature.BURIED_TREASURE)) { 
            return null; 
        }
    
        return super.locateStructure(world, feature, center, radius, skipExistingChunks); 
    }
    
    public void generateTerrain(Chunk chunk, StructureAccessor structureAccessor) {
        byte byte4 = 4;
        // byte seaLevel = (byte)this.getSeaLevel();
        byte byte17 = 17;

        int int5_0 = byte4 + 1;
        int int5_1 = byte4 + 1;

        Heightmap heightmapOCEAN = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSURFACE = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);

        this.collectStructures(chunk, structureAccessor);

        ObjectListIterator<StructurePiece> structureListIterator = (ObjectListIterator<StructurePiece>) STRUCTURE_LIST.iterator();
        ObjectListIterator<JigsawJunction> jigsawListIterator = (ObjectListIterator<JigsawJunction>) JIGSAW_LIST.iterator();

        generateHeightmap(chunk.getPos().x * byte4, 0, chunk.getPos().z * byte4);

        for (int i = 0; i < byte4; i++) {
            for (int j = 0; j < byte4; j++) {
                for (int k = 0; k < 16; k++) {
                    
                    double eighth = 0.125D;

                    double var1 = HEIGHTMAP[((i + 0) * int5_1 + (j + 0)) * byte17 + (k + 0)];
                    double var2 = HEIGHTMAP[((i + 0) * int5_1 + (j + 1)) * byte17 + (k + 0)];
                    double var3 = HEIGHTMAP[((i + 1) * int5_1 + (j + 0)) * byte17 + (k + 0)];
                    double var4 = HEIGHTMAP[((i + 1) * int5_1 + (j + 1)) * byte17 + (k + 0)];

                    double var5 = (HEIGHTMAP[((i + 0) * int5_1 + (j + 0)) * byte17 + (k + 1)] - var1) * eighth;
                    double var6 = (HEIGHTMAP[((i + 0) * int5_1 + (j + 1)) * byte17 + (k + 1)] - var2) * eighth;
                    double var7 = (HEIGHTMAP[((i + 1) * int5_1 + (j + 0)) * byte17 + (k + 1)] - var3) * eighth;
                    double var8 = (HEIGHTMAP[((i + 1) * int5_1 + (j + 1)) * byte17 + (k + 1)] - var4) * eighth;

                    for (int l = 0; l < 8; l++) {
                        double quarter = 0.25D;
                        double var10 = var1;
                        double var11 = var2;
                        double var12 = (var3 - var1) * quarter; // Lerp
                        double var13 = (var4 - var2) * quarter;

                        for (int m = 0; m < 4; m++) {
                            int x = (m + i * 4);
                            int y = k * 8 + l;
                            int z = (j * 4);

                            double var14 = 0.25D;
                            double density = var10; // var15
                            double var16 = (var11 - var10) * var14;

                            int absX = (chunk.getPos().x << 4) + i * 4 + m;

                            for (int n = 0; n < 4; n++) { 
                                int absZ = (chunk.getPos().z << 4) + j * 4 + n;

                                while (structureListIterator.hasNext()) {
                                    StructurePiece curStructurePiece = (StructurePiece) structureListIterator.next();
                                    BlockBox blockBox = curStructurePiece.getBoundingBox();

                                    int sX = Math.max(0, Math.max(blockBox.minX - absX, absX - blockBox.maxX));
                                    int sY = y - (blockBox.minY + ((curStructurePiece instanceof PoolStructurePiece)
                                            ? ((PoolStructurePiece) curStructurePiece).getGroundLevelDelta() : 0));
                                    int sZ = Math.max(0, Math.max(blockBox.minZ - absZ, absZ - blockBox.maxZ));

                                    if (sY < 0 && sX == 0 && sZ == 0)
                                        density += density * density / 0.1;
                                }
                                structureListIterator.back(STRUCTURE_LIST.size());

                                while (jigsawListIterator.hasNext()) {
                                    JigsawJunction curJigsawJunction = (JigsawJunction) jigsawListIterator.next();

                                    int jX = absX - curJigsawJunction.getSourceX();
                                    int jY = y - curJigsawJunction.getSourceGroundY();
                                    int jZ = absZ - curJigsawJunction.getSourceZ();

                                    if (jY < 0 && jX == 0 && jZ == 0)
                                        density += density * density / 0.1;
                                }
                                jigsawListIterator.back(JIGSAW_LIST.size());

                                BlockState blockToSet = this.getBlockState(density, y, 0);

                                chunk.setBlockState(POS.set(x, y, z), blockToSet, false);

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

    private void generateHeightmap(int x, int y, int z) {
        byte byte4 = 4;
        // byte seaLevel = (byte)this.getSeaLevel();
        byte byte17 = 17;

        int int5_0 = byte4 + 1;
        int int5_1 = byte4 + 1;

        double coordinateScale = 684.41200000000003D;
        double heightScale = 684.41200000000003D;

        double depthNoiseScaleX = 100D;
        double depthNoiseScaleZ = 100D;

        double mainNoiseScaleX = 80D;
        double mainNoiseScaleY = 160D;
        double mainNoiseScaleZ = 80D;

        double lowerLimitScale = 512D;
        double upperLimitScale = 512D;

        scaleNoise = scaleNoiseOctaves.generateAlphaNoiseOctaves(scaleNoise, x, y, z, int5_0, 1, int5_1, 1.0D, 0.0D, 1.0D);
        depthNoise = depthNoiseOctaves.generateAlphaNoiseOctaves(depthNoise, x, y, z, int5_0, 1, int5_1, depthNoiseScaleX, 0.0D, depthNoiseScaleZ);

        mainNoise = mainNoiseOctaves.generateAlphaNoiseOctaves(
            mainNoise, 
            x, y, z, 
            int5_0, byte17, int5_1,
            coordinateScale / mainNoiseScaleX, 
            heightScale / mainNoiseScaleY, 
            coordinateScale / mainNoiseScaleZ
        );

        minLimitNoise = minLimitNoiseOctaves.generateAlphaNoiseOctaves(
            minLimitNoise, 
            x, y, z, 
            int5_0, byte17, int5_1,
            coordinateScale, 
            heightScale, 
            coordinateScale
        );

        maxLimitNoise = maxLimitNoiseOctaves.generateAlphaNoiseOctaves(
            maxLimitNoise, 
            x, y, z, 
            int5_0, byte17, int5_1,
            coordinateScale,
            heightScale,
            coordinateScale
        );

        int i = 0;
        int j = 0;
        for (int l = 0; l < int5_0; l++) {
            for (int m = 0; m < int5_1; m++) {

                double scaleMod = (scaleNoise[j] + 256D) / 512D;
                if (scaleMod > 1.0D) {
                    scaleMod = 1.0D;
                }

                double d3 = 0.0D;

                double depthMod = depthNoise[j] / 8000D;
                if (depthMod < 0.0D) {
                    depthMod = -depthMod;
                }

                depthMod = depthMod * 3D - 3D;

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
                    depthMod /= 6D;
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
                    double mainLimitMod = (mainNoise[i] / 10D + 1.0D) / 2D;

                    if (mainLimitMod < 0.0D) {
                        heightVal = minLimitMod;
                    } else if (mainLimitMod > 1.0D) {
                        heightVal = maxLimitMod;
                    } else {
                        heightVal = minLimitMod + (maxLimitMod - minLimitMod) * mainLimitMod;
                    }
                    heightVal -= scaleMod2;

                    if (n > byte17 - 4) {
                        double d11 = (float) (n - (byte17 - 4)) / 3F;
                        heightVal = heightVal * (1.0D - d11) + -10D * d11;
                    }

                    if ((double) n < d3) {
                        double d12 = (d3 - (double) n) / 4D;
                        if (d12 < 0.0D) {
                            d12 = 0.0D;
                        }
                        if (d12 > 1.0D) {
                            d12 = 1.0D;
                        }
                        heightVal = heightVal * (1.0D - d12) + -10D * d12;
                    }

                    HEIGHTMAP[i] = heightVal;
                    i++;
                }
            }
        }
    }

    private void buildAlphaSurface(Chunk chunk) {
        byte seaLevel = (byte) this.getSeaLevel();
        double thirtysecond = 0.03125D; // eighth

        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;

        BiomeMath.fetchTempHumid(chunkX << 4, chunkZ << 4, TEMPS, HUMIDS);
        biomeSource.fetchBiomes(TEMPS, HUMIDS, BIOMES);
        
        Biome curBiome;

        sandNoise = beachNoiseOctaves.generateAlphaNoiseOctaves(sandNoise, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1,
                thirtysecond, thirtysecond, 1.0D);
        gravelNoise = beachNoiseOctaves.generateAlphaNoiseOctaves(gravelNoise, chunkX * 16, 109.0134D, chunkZ * 16, 16, 1,
                16, thirtysecond, 1.0D, thirtysecond);
        stoneNoise = stoneNoiseOctaves.generateAlphaNoiseOctaves(stoneNoise, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1,
                thirtysecond * 2D, thirtysecond * 2D, thirtysecond * 2D);

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {

                boolean genSandBeach = sandNoise[i + j * 16] + RAND.nextDouble() * 0.20000000000000001D > 0.0D;
                boolean genGravelBeach = gravelNoise[i + j * 16] + RAND.nextDouble() * 0.20000000000000001D > 3D;

                int genStone = (int) (stoneNoise[i + j * 16] / 3D + 3D + RAND.nextDouble() * 0.25D);
                int flag = -1;

                curBiome = BIOMES[i + j * 16];

                BlockState biomeTopBlock = curBiome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                BlockState biomeFillerBlock = curBiome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();

                BlockState topBlock = biomeTopBlock;
                BlockState fillerBlock = biomeFillerBlock;

                // Generate from top to bottom of world
                for (int y = 127; y >= 0; y--) {

                    // Randomly place bedrock from y=0 to y=5
                    if (y <= (0 + RAND.nextInt(6)) - 1) {
                        chunk.setBlockState(POS.set(j, y, i), BlockStates.BEDROCK, false);
                        continue;
                    }

                    Block someBlock = chunk.getBlockState(POS.set(j, y, i)).getBlock();

                    if (someBlock.equals(Blocks.AIR)) { // Skip if air block
                        flag = -1;
                        continue;
                    }

                    if (!someBlock.equals(Blocks.STONE)) { // Skip if not stone
                        continue;
                    }

                    if (flag == -1) {
                        if (genStone <= 0) { // Generate stone basin if noise permits
                            topBlock = BlockStates.AIR;
                            fillerBlock = BlockStates.STONE;
                        } else if (y >= seaLevel - 4 && y <= seaLevel + 1) { // Generate beaches at this y range
                            topBlock = biomeTopBlock;
                            fillerBlock = biomeFillerBlock;

                            if (genGravelBeach) {
                                topBlock = BlockStates.AIR; // This reduces gravel beach height by 1
                                fillerBlock = BlockStates.GRAVEL;
                            }

                            if (genSandBeach) {
                                topBlock = BlockStates.SAND;
                                fillerBlock = BlockStates.SAND;
                            }
                        }

                        if (y < seaLevel && topBlock.equals(BlockStates.AIR)) { // Generate water bodies
                            topBlock = BlockStates.WATER;
                        }

                        // Main surface builder section
                        flag = genStone;
                        if (y >= seaLevel - 1) {
                            chunk.setBlockState(POS.set(j, y, i), topBlock, false);
                        } else {
                            chunk.setBlockState(POS.set(j, y, i), fillerBlock, false);
                        }

                        continue;
                    }

                    if (flag <= 0) {
                        continue;
                    }

                    flag--;
                    chunk.setBlockState(POS.set(j, y, i), fillerBlock, false);

                    // Gens layer of sandstone starting at lowest block of sand, of height 1 to 4.
                    // Beta backport.
                    if (flag == 0 && fillerBlock.equals(BlockStates.SAND)) {
                        flag = RAND.nextInt(4);
                        fillerBlock = BlockStates.SANDSTONE;
                    }
                }
            }
        }
    }

    protected BlockState getBlockState(double density, int y, double temp) {
        BlockState blockStateToSet = BlockStates.AIR;
        if (density > 0.0) {
            blockStateToSet = this.settings.wrapped.getDefaultBlock();
        } else if (y < this.getSeaLevel()) {
            blockStateToSet = this.settings.wrapped.getDefaultFluid();

        }
        return blockStateToSet;
    }

    // Called only when generating structures
    @Override
    public int getHeight(int x, int z, Heightmap.Type type) {

        POS.set(x, 0, z);

        if (GROUND_CACHE_Y.get(POS) == null) {
            sampleHeightmap(x, z);
        }

        int groundHeight = GROUND_CACHE_Y.get(POS);

        // Not ideal
        if (type == Heightmap.Type.WORLD_SURFACE_WG && groundHeight < this.getSeaLevel())
            groundHeight = this.getSeaLevel();

        return groundHeight;
    }

    private void sampleHeightmap(int absX, int absZ) {
        byte byte4 = 4;
        // byte seaLevel = (byte)this.getSeaLevel();
        byte byte17 = 17;

        int int5_0 = byte4 + 1;
        int int5_1 = byte4 + 1;
        
        int chunkX = absX >> 4;
        int chunkZ = absZ >> 4;

        generateHeightmap(chunkX * byte4, 0, chunkZ * byte4);

        for (int i = 0; i < byte4; i++) {
            for (int j = 0; j < byte4; j++) {
                for (int k = 0; k < 16; k++) {
                    double eighth = 0.125D;

                    double var1 = HEIGHTMAP[((i + 0) * int5_1 + (j + 0)) * byte17 + (k + 0)];
                    double var2 = HEIGHTMAP[((i + 0) * int5_1 + (j + 1)) * byte17 + (k + 0)];
                    double var3 = HEIGHTMAP[((i + 1) * int5_1 + (j + 0)) * byte17 + (k + 0)];
                    double var4 = HEIGHTMAP[((i + 1) * int5_1 + (j + 1)) * byte17 + (k + 0)];

                    double var5 = (HEIGHTMAP[((i + 0) * int5_1 + (j + 0)) * byte17 + (k + 1)] - var1) * eighth;
                    double var6 = (HEIGHTMAP[((i + 0) * int5_1 + (j + 1)) * byte17 + (k + 1)] - var2) * eighth;
                    double var7 = (HEIGHTMAP[((i + 1) * int5_1 + (j + 0)) * byte17 + (k + 1)] - var3) * eighth;
                    double var8 = (HEIGHTMAP[((i + 1) * int5_1 + (j + 1)) * byte17 + (k + 1)] - var4) * eighth;

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
                                    CHUNK_Y[x][z] = y;
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

        for (int pX = 0; pX < CHUNK_Y.length; pX++) {
            for (int pZ = 0; pZ < CHUNK_Y[pX].length; pZ++) {
                POS.set((chunkX << 4) + pX, 0, (chunkZ << 4) + pZ);
                GROUND_CACHE_Y.put(POS, CHUNK_Y[pX][pZ] + 1); // +1 because it is one above the ground
            }
        }
    }
    
    private void collectStructures(Chunk chunk, StructureAccessor accessor) {
        STRUCTURE_LIST.clear();
        JIGSAW_LIST.clear();
        
        for (final StructureFeature<?> s : StructureFeature.JIGSAW_STRUCTURES) {

            accessor.getStructuresWithChildren(ChunkSectionPos.from(chunk.getPos(), 0), s)
                .forEach(structureStart -> {
                    Iterator<StructurePiece> structurePieceIterator;
                    StructurePiece structurePiece;

                    Iterator<JigsawJunction> jigsawJunctionIterator;
                    JigsawJunction jigsawJunction;

                    ChunkPos arg2 = chunk.getPos();

                    PoolStructurePiece poolStructurePiece;
                    StructurePool.Projection structureProjection;

                    int jigsawX;
                    int jigsawZ;
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
                                STRUCTURE_LIST.add(poolStructurePiece);
                            }
                            jigsawJunctionIterator = poolStructurePiece.getJunctions().iterator();
                            while (jigsawJunctionIterator.hasNext()) {
                                jigsawJunction = jigsawJunctionIterator.next();
                                jigsawX = jigsawJunction.getSourceX();
                                jigsawZ = jigsawJunction.getSourceZ();
                                if (jigsawX > n2 - 12 && jigsawZ > n3 - 12 && jigsawX < n2 + 15 + 12) {
                                    if (jigsawZ >= n3 + 15 + 12) {
                                        continue;
                                    } else {
                                        JIGSAW_LIST.add(jigsawJunction);
                                    }
                                }
                            }
                        } else {
                            STRUCTURE_LIST.add(structurePiece);
                        }
                    }
                    return;
            });
        }
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
        return new AlphaChunkGenerator(this.biomeSource.withSeed(seed), seed, this.settings);
    }

}
