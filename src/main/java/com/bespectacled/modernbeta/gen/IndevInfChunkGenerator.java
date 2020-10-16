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

//private final BetaGeneratorSettings settings;

public class IndevInfChunkGenerator extends NoiseChunkGenerator {

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

    public static final Codec<IndevInfChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
                    Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.worldSeed),
                    AlphaGeneratorSettings.CODEC.fieldOf("settings").forGetter(generator -> generator.settings))
            .apply(instance, instance.stable(IndevInfChunkGenerator::new)));

    private final AlphaGeneratorSettings settings;

    private AlphaNoiseGeneratorOctaves minLimitNoiseOctaves;
    private AlphaNoiseGeneratorOctaves maxLimitNoiseOctaves;
    private AlphaNoiseGeneratorOctaves mainNoiseOctaves;
    //private AlphaNoiseGeneratorOctaves beachNoiseOctaves;
    //private AlphaNoiseGeneratorOctaves stoneNoiseOctaves;
    public AlphaNoiseGeneratorOctaves scaleNoiseOctaves;
    public AlphaNoiseGeneratorOctaves depthNoiseOctaves;
    
    
    private IndevNoiseGeneratorCombined noise1;
    private IndevNoiseGeneratorCombined noise2;
    
    private IndevNoiseGeneratorOctaves noise3;
    private IndevNoiseGeneratorOctaves noise4;
    
    private IndevNoiseGeneratorOctaves noise5;
    private IndevNoiseGeneratorOctaves noise6;
    
    private IndevNoiseGeneratorOctaves sandNoiseOctaves;
    private IndevNoiseGeneratorOctaves gravelNoiseOctaves;
    

    // private final NoiseSampler surfaceDepthNoise;

    private int heightmap[]; // field_4180_q
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

    AlphaBiomeSource biomeSource;
    private double temps[];

    public static long seed;
    // private boolean generateOceans;

    // Block Y-height cache, taken from Beta+
    public Map<BlockPos, Integer> groundCacheY = new HashMap<>();

    public IndevInfChunkGenerator(BiomeSource biomes, long seed, AlphaGeneratorSettings settings) {
        super(biomes, seed, () -> settings.wrapped);
        this.settings = settings;
        this.seed = seed;
        this.rand = new Random(seed);
        this.biomeSource = (AlphaBiomeSource) biomes;
        // this.generateOceans = ModernBetaConfig.loadConfig().generate_oceans;

        // Noise Generators
        minLimitNoiseOctaves = new AlphaNoiseGeneratorOctaves(rand, 16);
        maxLimitNoiseOctaves = new AlphaNoiseGeneratorOctaves(rand, 16);
        mainNoiseOctaves = new AlphaNoiseGeneratorOctaves(rand, 8);
        //beachNoiseOctaves = new AlphaNoiseGeneratorOctaves(rand, 4);
        //stoneNoiseOctaves = new AlphaNoiseGeneratorOctaves(rand, 4);
        scaleNoiseOctaves = new AlphaNoiseGeneratorOctaves(rand, 10);
        depthNoiseOctaves = new AlphaNoiseGeneratorOctaves(rand, 16);
        
        
        noise1 = new IndevNoiseGeneratorCombined(new IndevNoiseGeneratorOctaves(this.rand, 8), new IndevNoiseGeneratorOctaves(this.rand, 8));
        noise2 = new IndevNoiseGeneratorCombined(new IndevNoiseGeneratorOctaves(this.rand, 8), new IndevNoiseGeneratorOctaves(this.rand, 8));
        
        noise3 = new IndevNoiseGeneratorOctaves(this.rand, 6);
        noise4 = new IndevNoiseGeneratorOctaves(this.rand, 2);
        
        noise5 = new IndevNoiseGeneratorOctaves(this.rand, 8);
        noise6 = new IndevNoiseGeneratorOctaves(this.rand, 8);
        
        sandNoiseOctaves = new IndevNoiseGeneratorOctaves(this.rand, 8);
        gravelNoiseOctaves = new IndevNoiseGeneratorOctaves(this.rand, 8);

        // Yes this is messy. What else am I supposed to do?
        BetaDecorator.COUNT_ALPHA_NOISE_DECORATOR.setSeed(seed);
        ModernBeta.setBlockColorsSeed(0L, true);
        ModernBeta.SEED = seed;
    }

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR, new Identifier(ModernBeta.ID, "indev"), CODEC);
        ModernBeta.LOGGER.log(Level.INFO, "Registered Indev chunk generator.");
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return IndevInfChunkGenerator.CODEC;
    }
    
    private boolean inIndevRegion(int chunkX, int chunkZ) {
        int width = 256;
        int height = 256;
        
        int chunkLenX = width / 16;
        int chunkLenZ = height / 16;
        
        int chunkXStart = -chunkLenX / 2;
        int chunkZStart = -chunkLenZ / 2;
        
        int chunkXEnd = chunkLenX / 2;
        int chunkZEnd = chunkLenZ / 2;
        
        System.out.println("ChunkX: " + chunkX + ", chunkStartX: " + chunkXStart + ", chunkEndX: " + chunkXEnd);
        
        if (chunkX >= chunkXStart && chunkX < chunkXEnd && chunkZ >= chunkZStart && chunkZ < chunkZEnd) {
            return true;
        }
        
        return false;
    }

    @Override
    public void populateNoise(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk) {
        ChunkPos pos = chunk.getPos();

        /*
        rand.setSeed((long) chunk.getPos().x * 341873128712L + (long) chunk.getPos().z * 132897987541L);

        generateTerrain(chunk, temps, structureAccessor);
        
        
        if (inIndevRegion(pos.x, pos.z)) {
            System.out.println("Flooding...");
            flood(worldAccess, chunk, rand.nextInt(16) + pos.getStartX(), -1, rand.nextInt(16) + pos.getStartZ());
        }*/
        
        if (pos.x == 0 && pos.z == 0) {
            //pregenerate();
        }
        /*
         * MutableBiomeArray mutableBiomes =
         * MutableBiomeArray.inject(chunk.getBiomeArray()); BlockPos.Mutable
         * mutableBlock = new BlockPos.Mutable();
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

        BlockPos pos = new BlockPos(ctrAbsX, 0, ctrAbsZ);
        BlockPos.Mutable mutableBlock = new BlockPos.Mutable();

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

        /*
         * int[][] chunkY = sampleHeightmap(chunkPos);
         * 
         * int thisY = chunkY[Math.abs(absX % 16)][Math.abs(absZ % 16)];
         * 
         * if (thisY <= this.getSeaLevel() - 4) { biome =
         * this.biomeSource.getOceanBiomeForNoiseGen(absX, 0, absZ); }
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
         * BlockPos.Mutable mutableBlock = new BlockPos.Mutable();
         * 
         * mutableBlock.set(absX, 62, absZ); BlockState blockstate =
         * chunk.getBlockState(mutableBlock);
         * 
         * if (blockstate.isOf(Blocks.WATER)) { generationSettings =
         * this.biomeSource.getOceanBiomeForNoiseGen(absX, 0,
         * absZ).getGenerationSettings(); }
         */

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
        int startX = chunk.getPos().getStartX();
        int startZ = chunk.getPos().getStartZ();
        
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        BlockPos.Mutable mutableBelow = new BlockPos.Mutable();
        
        int width = 256;
        int length = 256;
        
        int height = 128;
        
        int waterLevel = 64;
        
        // "Growing..."
        
        for (int x = 0; x < 16; ++x) {
            int absX = x + startX;
            
            for (int z = 0; z <  16; ++z) {
                int absZ = z + startZ;
                
                boolean genSand = sandNoiseOctaves.IndevNoiseGenerator(absX, absZ) > 8.0;
                boolean genGravel = gravelNoiseOctaves.IndevNoiseGenerator(absX, absZ) > 12.0;
                
                /*
                for (int y = height - 1; y >= 0; --y) {
                    Block someBlock = chunk.getBlockState(mutable.set(x, y, z)).getBlock();
                    Block someBlockBelow = chunk.getBlockState(mutableBelow.set(x, y - 1, z)).getBlock();
                    
                    if (someBlock.equals(Blocks.WATER) && genGravel) {
                        chunk.setBlockState(mutable.set(x, y - 1, z), Blocks.GRAVEL.getDefaultState(), false);
                    }
                    
                    if (someBlock.equals(Blocks.AIR)) {
                        if (y <= waterLevel && genSand) {
                            chunk.setBlockState(mutable.set(x, y - 1, z), Blocks.SAND.getDefaultState(), false);
                        } else {
                            chunk.setBlockState(mutable.set(x, y - 1, z), Blocks.GRASS_BLOCK.getDefaultState(), false);
                        }
                    }
                }
                */
                int topBlock = 0;
                
                for (int y = 0; y < height - 1; ++y) {
                    Block someBlock = chunk.getBlockState(mutable.set(x, y, z)).getBlock();
                    Block someBlockAbove = chunk.getBlockState(mutable.set(x, y + 1, z)).getBlock();
                    
                    if (someBlock.equals(Blocks.AIR) && someBlockAbove.equals(Blocks.AIR) || someBlockAbove.equals(Blocks.WATER)) break;
                    
                    topBlock = y;
                }
                
                Block someBlock = chunk.getBlockState(mutable.set(x, topBlock, z)).getBlock();
                Block someBlockAbove = chunk.getBlockState(mutable.set(x, topBlock + 1, z)).getBlock();

                if ((someBlockAbove.equals(Blocks.WATER)) && genGravel) {
                    System.out.println("Gen gravel at: " + absX + ", " + absZ);
                    chunk.setBlockState(mutable.set(x, topBlock, z), Blocks.GRAVEL.getDefaultState(), false);
                }
                
                else if (someBlockAbove.equals(Blocks.AIR)) {
                    if (topBlock <= waterLevel && genSand) {
                        chunk.setBlockState(mutable.set(x, topBlock, z), Blocks.SAND.getDefaultState(), false);
                    } else {
                        chunk.setBlockState(mutable.set(x, topBlock, z), Blocks.GRASS_BLOCK.getDefaultState(), false);
                    }
                }
                
            }
            
        }
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
        int startX = chunk.getPos().getStartX();
        int startZ = chunk.getPos().getStartZ();
        
        BlockPos.Mutable mutableBlock = new BlockPos.Mutable();
        Heightmap heightmapOCEAN = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSURFACE = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);

        heightmap = generateHeightmap(heightmap, startX, startZ);
        
        int width = 256;
        int length = 256;
        
        int height = 128;
        
        int waterLevel = 64;
        
        // Soiling...
        
       // int relX = 0;
       // int relZ = 0;
        
        for (int x = 0; x < 16; ++x) {
            
            int absX = x + startX;
            double var1 = Math.abs((absX / (width - 1.0) - 0.5) * 2.0);
            
            //relZ = 0;
            for (int z = 0; z < 16; ++z) {
                
                int absZ = z + startZ;
                double var2 = Math.max(var1, Math.abs(absZ / (length - 1.0) - 0.5) * 2.0);
                var2 = var2 * var2 * var2;
                
                int dirtTransition = heightmap[x + z * 16] + waterLevel;
                int dirtThickness = (int)(noise5.IndevNoiseGenerator(x, z) / 24.0) - 4;
            
                int stoneTransition = dirtTransition + dirtThickness;
                heightmap[x + z * 16] = Math.max(dirtTransition, stoneTransition);
                
                if (heightmap[x + z * 16] > height - 2) {
                    heightmap[x + z * 16] = height - 2;
                }
                
                if (heightmap[x + z * 16] <= 0) {
                    heightmap[x + z * 16] = 1;
                }
                
                double var4 = noise6.IndevNoiseGenerator(x * 2.3, z * 2.3) / 24.0;
                int var5 = (int)(Math.sqrt(Math.abs(var4)) * Math.signum(var4) * 20.0) + waterLevel;
                var5 = (int)(var5 * (1.0 - var2) + var2 * height);
                
                if (var5 > waterLevel) {
                    var5 = height;
                }
                
                for (int y = 0; y < height; ++y) {
                    Block blockToSet = Blocks.AIR;
                    
                    if (y <= dirtTransition)
                        blockToSet = Blocks.DIRT;
                    
                    if (y <= stoneTransition)
                        blockToSet = Blocks.STONE;
                    
                    // if (floatingGen && y < var5)
                    //      blockToSet = Blocks.AIR;

                    Block someBlock = chunk.getBlockState(mutableBlock.set(x, y, z)).getBlock();
                    
                    if (someBlock.equals(Blocks.AIR)) {
                        chunk.setBlockState(mutableBlock.set(x, y, z), blockToSet.getDefaultState(), false);
                    }
                    
                    //if (y < waterLevel && blockToSet.equals(Blocks.AIR))
                      // chunk.setBlockState(mutableBlock.set(x, y, z), Blocks.WATER.getDefaultState(), false);
                }
                //relZ++;
            }
            
           // relX++;
        }
        
    }

    private int[] generateHeightmap(int heightmap[], int absX, int absZ) {
        // Placeholder stuff
        int width = 256;
        int length = 256;
        
        int chunkSizeX = 16;
        int chunkSizeZ = 16;
        
        if (heightmap == null) {
            heightmap = new int[chunkSizeX * chunkSizeZ]; // For entire chunk
        }
        
        // Raising...
        
        int relX = 0;
        int relZ = 0;
        
        for (int x = absX; x < absX + 16; ++x) {
            double islandMod1 = Math.abs((1 / (chunkSizeX - 1.0) - 0.5) * 2.0);
            
            relZ = 0;
            for (int z = absZ; z < absZ + 16; ++z) {
                double islandMod2 = Math.abs((1 / (chunkSizeZ - 1.0) - 0.5) * 2.0);
                
                double heightLow = noise1.IndevNoiseGenerator(x * 1.3f, z * 1.3f) / 6.0 - 4.0;
                double heightHigh = noise2.IndevNoiseGenerator(x * 1.3f, z * 1.3f) / 5.0 + 10.0 - 4.0;
                
                double heightCheck = noise3.IndevNoiseGenerator(x, z) / 8.0;
                
                if (heightCheck > 0.0) {
                    heightHigh = heightLow;
                }
                
                double heightResult = Math.max(heightLow, heightHigh) / 2.0;
                
                //if (islandGen) {
                if (false) {}
                else if (heightResult < 0.0) {
                    heightResult *= 0.8;
                }
                
                //System.out.println("RelZ: " + relZ);
                
                heightmap[relX + relZ * chunkSizeZ] = (int)heightResult;
                relZ++;
            }
            
            relX++;
        }
       
        return heightmap;
        
    }
    
    private void flood(WorldAccess world, Chunk chunk, int absX, int y, int absZ) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        int waterLevel = 64;
        
        if (y < 0) { // If < 0, then this is the flood start
            y = rand.nextBoolean() ? waterLevel - 1 : waterLevel - 2;
        }
        
        Block block = world.getBlockState(mutable.set(absX, y, absZ)).getBlock();
        
        if (block.equals(Blocks.AIR)) {
            world.setBlockState(mutable.set(absX, y, absZ), Blocks.WATER.getDefaultState(), 0);
            
            flood(world, chunk, absX + 1, y, absZ);
            flood(world, chunk, absX - 1, y, absZ);
            flood(world, chunk, absX, y, absZ + 1);
            flood(world, chunk, absX, y, absZ - 1);
            flood(world, chunk, absX, y - 1, absZ);
        }
        
        
        
        //chunk.get
        
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
    /*
    @Override
    public int getHeight(int x, int z, Heightmap.Type type) {

        BlockPos blockPos = new BlockPos(x, 0, z);
        ChunkPos chunkPos = new ChunkPos(blockPos);

        if (groundCacheY.get(blockPos) == null) {
            // biomeSource.fetchTempHumid(chunkPos.x * 16, chunkPos.z * 16, 16, 16);
            sampleHeightmap(chunkPos);
        }

        int groundHeight = groundCacheY.get(blockPos);

        // Not ideal
        if (type == Heightmap.Type.WORLD_SURFACE_WG && groundHeight < this.getSeaLevel())
            groundHeight = this.getSeaLevel();

        return groundHeight;
    }
*/
    /*
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
    */

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
        return new IndevInfChunkGenerator(this.biomeSource.withSeed(seed), seed, this.settings);
    }

}
