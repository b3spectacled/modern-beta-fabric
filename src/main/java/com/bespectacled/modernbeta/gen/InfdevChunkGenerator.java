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
import com.bespectacled.modernbeta.biome.InfdevBiomeSource;
import com.bespectacled.modernbeta.decorator.BetaDecorator;
import com.bespectacled.modernbeta.feature.BetaFeature;
import com.bespectacled.modernbeta.gen.settings.AlphaGeneratorSettings;
import com.bespectacled.modernbeta.gen.settings.InfdevGeneratorSettings;
import com.bespectacled.modernbeta.noise.*;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.MutableBiomeArray;
import com.bespectacled.modernbeta.util.IndevUtil.Type;

//private final BetaGeneratorSettings settings;

public class InfdevChunkGenerator extends NoiseChunkGenerator {

    public static final Codec<InfdevChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
                    Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.worldSeed),
                    InfdevGeneratorSettings.CODEC.fieldOf("settings").forGetter(generator -> generator.settings))
            .apply(instance, instance.stable(InfdevChunkGenerator::new)));

    private final InfdevGeneratorSettings settings;
    private final InfdevBiomeSource biomeSource;
    private final long seed;

    private final OldNoiseGeneratorOctaves noiseOctavesA;
    private final OldNoiseGeneratorOctaves noiseOctavesB;
    private final OldNoiseGeneratorOctaves noiseOctavesC;
    private final OldNoiseGeneratorOctaves beachNoiseOctaves;
    private final OldNoiseGeneratorOctaves stoneNoiseOctaves;
    private final OldNoiseGeneratorOctaves forestNoiseOctaves;

    // Block Y-height cache, from Beta+
    private static final Map<BlockPos, Integer> GROUND_CACHE_Y = new HashMap<>();
    private static final int[][] CHUNK_Y = new int[16][16];
    
    private static final double HEIGHTMAP[][] = new double[33][4];
    private static final double HEIGHTMAP_STRUCT[][] = new double[33][4];
    
    private static final Mutable POS = new Mutable();
    
    private static final Random RAND = new Random();
    private static final ChunkRandom FEATURE_RAND = new ChunkRandom();
    
    private static final ObjectList<StructurePiece> STRUCTURE_LIST = (ObjectList<StructurePiece>) new ObjectArrayList(10);
    private static final ObjectList<JigsawJunction> JIGSAW_LIST = (ObjectList<JigsawJunction>) new ObjectArrayList(32);

    public InfdevChunkGenerator(BiomeSource biomes, long seed, InfdevGeneratorSettings settings) {
        super(biomes, seed, () -> settings.wrapped);
        this.settings = settings;
        this.biomeSource = (InfdevBiomeSource) biomes;
        this.seed = seed;
        
        RAND.setSeed(seed);
        
        // Noise Generators
        noiseOctavesA = new OldNoiseGeneratorOctaves(RAND, 16, false);
        noiseOctavesB = new OldNoiseGeneratorOctaves(RAND, 16, false);
        noiseOctavesC = new OldNoiseGeneratorOctaves(RAND, 8, false);
        beachNoiseOctaves = new OldNoiseGeneratorOctaves(RAND, 4, false);
        stoneNoiseOctaves = new OldNoiseGeneratorOctaves(RAND, 4, false);

        new OldNoiseGeneratorOctaves(RAND, 5, false); // Unused in original source
        
        forestNoiseOctaves = new OldNoiseGeneratorOctaves(RAND, 5, false);

        // Yes this is messy. What else am I supposed to do?
        BetaDecorator.COUNT_INFDEV_NOISE_DECORATOR.setOctaves(forestNoiseOctaves);
    }

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR, new Identifier(ModernBeta.ID, "infdev"), CODEC);
        //ModernBeta.LOGGER.log(Level.INFO, "Registered Infdev chunk generator.");
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return InfdevChunkGenerator.CODEC;
    }

    @Override
    public void populateNoise(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk) {
        RAND.setSeed((long) chunk.getPos().x * 341873128712L + (long) chunk.getPos().z * 132897987541L);

        generateTerrain(chunk, structureAccessor);
        
        BetaFeature.OLD_FANCY_OAK.chunkReset();
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
        buildInfdevSurface(chunk);
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
        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        Heightmap heightmapOCEAN = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSURFACE = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
        
        this.collectStructures(chunk, structureAccessor);
        
        ObjectListIterator<StructurePiece> structureListIterator = (ObjectListIterator<StructurePiece>) STRUCTURE_LIST.iterator();
        ObjectListIterator<JigsawJunction> jigsawListIterator = (ObjectListIterator<JigsawJunction>) JIGSAW_LIST.iterator();
        
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++ j) {
                int bX = (chunkX << 2) + i;
                int bZ = (chunkZ << 2) + j;
                
                for (int bY = 0; bY < HEIGHTMAP.length; ++bY) {
                    HEIGHTMAP[bY][0] = this.generateHeightmap(bX, bY, bZ);
                    HEIGHTMAP[bY][1] = this.generateHeightmap(bX, bY, bZ + 1);
                    HEIGHTMAP[bY][2] = this.generateHeightmap(bX + 1, bY, bZ);
                    HEIGHTMAP[bY][3] = this.generateHeightmap(bX + 1, bY, bZ + 1);
                }
                
                for (int bY = 0; bY < 32; ++bY) {
                    double n1 = HEIGHTMAP[bY][0];
                    double n2 = HEIGHTMAP[bY][1];
                    double n3 = HEIGHTMAP[bY][2];
                    double n4 = HEIGHTMAP[bY][3];
                    double n5 = HEIGHTMAP[bY + 1][0];
                    double n7 = HEIGHTMAP[bY + 1][1];
                    double n8 = HEIGHTMAP[bY + 1][2];
                    double n9 = HEIGHTMAP[bY + 1][3];
                    
                    for (int pY = 0; pY < 4; ++pY) {
                        double mixY = pY / 4.0;
                        
                        double nx1 = n1 + (n5 - n1) * mixY;
                        double nx2 = n2 + (n7 - n2) * mixY;
                        double nx3 = n3 + (n8 - n3) * mixY;
                        double nx4 = n4 + (n9 - n4) * mixY;
                        
                        int y = (bY << 2) + pY;
                        
                        for (int pX = 0; pX < 4; ++pX) {
                            double mixX = pX / 4.0;
                            
                            double nz1 = nx1 + (nx3 - nx1) * mixX;
                            double nz2 = nx2 + (nx4 - nx2) * mixX;
                            
                            int x = pX + (i << 2);
                            int z = 0 + (j << 2);
                            
                            for (int pZ = 0; pZ < 4; ++pZ) {
                                double mixZ = pZ / 4.0;
                                double density = nz1 + (nz2 - nz1) * mixZ;
                                
                                int absX = (chunk.getPos().x << 4) + x;
                                int absZ = (chunk.getPos().z << 4) + z;
                                
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
                                
                                BlockState blockToSet = this.getBlockState(density, y);
                                chunk.setBlockState(POS.set(x, y, z), blockToSet, false);
                                
                                heightmapOCEAN.trackUpdate(x, y, z, blockToSet);
                                heightmapSURFACE.trackUpdate(x, y, z, blockToSet);

                                ++z;
                            }
                        }
                    }
                }
            }
        }
    }

    private double generateHeightmap(double x, double y, double z) {
        double elevGrad;
        if ((elevGrad = y * 4.0 - 64.0) < 0.0) {
            elevGrad *= 3.0;
        }
        
        double noise;
        double res;
        
        if ((noise = this.noiseOctavesC.generateInfdevOctaves(x * 8.55515, y * 1.71103, z * 8.55515) / 2.0) < -1) {
            res = MathHelper.clamp(
                this.noiseOctavesA.generateInfdevOctaves(x * 684.412, y * 984.412, z * 684.412) / 512.0 - elevGrad, 
                -10.0, 
                10.0
            );
            
        } else if (noise > 1.0) {
            res = MathHelper.clamp(
                this.noiseOctavesB.generateInfdevOctaves(x * 684.412, y * 984.412, z * 684.412) / 512.0 - elevGrad, 
                -10.0, 
                10.0
            );
            
        } else {
            double noise2 = MathHelper.clamp(
                this.noiseOctavesA.generateInfdevOctaves(x * 684.412, y * 984.412, z * 684.412) / 512.0 - elevGrad, 
                -10.0, 
                10.0
            );
            
            double noise3 = MathHelper.clamp(
                this.noiseOctavesB.generateInfdevOctaves(x * 684.412, y * 984.412, z * 684.412) / 512.0 - elevGrad, 
                -10.0, 
                10.0
            );
            
            double mix = (noise + 1.0) / 2.0;
            
            res = noise2 + (noise3 - noise2) * mix;
        }
        
        return res;
    }

    private void buildInfdevSurface(Chunk chunk) {
        double thirtysecond = 0.03125D; // eighth
        
        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                double absX = (chunkX << 4) + x;
                double absZ = (chunkZ << 4) + z;
                
                boolean genSandBeach = this.beachNoiseOctaves.generateInfdevOctaves(
                    absX * thirtysecond, 
                    absZ * thirtysecond, 
                    0.0) + RAND.nextDouble() * 0.2 > 0.0;
                
                boolean genGravelBeach = this.beachNoiseOctaves.generateInfdevOctaves(
                    absZ * thirtysecond, 
                    109.0134,
                    absX * thirtysecond) + RAND.nextDouble() * 0.2 > 3.0;
                
                int genStone = (int)(this.stoneNoiseOctaves.generateInfdevOctaves(
                    absX * thirtysecond * 2.0, 
                    absZ * thirtysecond * 2.0) / 3.0 + 3.0 + this.random.nextDouble() * 0.25);
                
                int flag = -1;
                
                BlockState biomeTopBlock = BlockStates.GRASS_BLOCK;
                BlockState biomeFillerBlock = BlockStates.DIRT;

                BlockState topBlock = biomeTopBlock;
                BlockState fillerBlock = biomeFillerBlock;
                
                for (int y = 127; y >= 0; --y) {
                    
                    // Randomly place bedrock from y=0 to y=5
                    if (y <= 0 + RAND.nextInt(5)) {
                        chunk.setBlockState(POS.set(x, y, z), Blocks.BEDROCK.getDefaultState(), false);
                        continue;
                    }
                    
                    POS.set(x, y, z);
                    Block someBlock = chunk.getBlockState(POS).getBlock();
                    
                    if (someBlock.equals(Blocks.AIR)) {
                        flag = -1;
                        
                    } else if (someBlock.equals(Blocks.STONE)) {
                        if (flag == -1) {
                            if (genStone <= 0) {
                                topBlock = BlockStates.AIR;
                                fillerBlock = BlockStates.STONE;
                            } else if (y >= 60 && y <= 65) {
                                topBlock = biomeTopBlock;
                                fillerBlock = biomeFillerBlock;
                                
                                if (genGravelBeach) {
                                    topBlock = BlockStates.AIR;
                                    fillerBlock = BlockStates.GRAVEL;
                                }
                                
                                if (genSandBeach) {
                                    topBlock = BlockStates.SAND;
                                    fillerBlock = BlockStates.SAND;
                                }
                            }
                            
                            if (y < this.getSeaLevel() && topBlock.equals(BlockStates.AIR)) {
                                topBlock = BlockStates.WATER; // Will this ever happen?
                            }
                            
                            flag = genStone;
                            
                            if (y >= this.getSeaLevel() - 1) {
                                chunk.setBlockState(POS, topBlock, false);
                            } else {
                                chunk.setBlockState(POS, fillerBlock, false);
                            }
                            
                        } else if (flag > 0) {
                            --flag;
                            chunk.setBlockState(POS, fillerBlock, false);
                            
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
        }
    }

    
    protected BlockState getBlockState(double density, int y) {
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
        BlockPos structPos = new BlockPos(x, 0, z);

        if (GROUND_CACHE_Y.get(structPos) == null) {
            sampleHeightmap(x, z, HEIGHTMAP_STRUCT);
        }

        int groundHeight = GROUND_CACHE_Y.get(structPos);

        // Not ideal
        if (type == Heightmap.Type.WORLD_SURFACE_WG && groundHeight < this.getSeaLevel())
            groundHeight = this.getSeaLevel();

        return groundHeight;
    }

    private void sampleHeightmap(int absX, int absZ, double[][] heightmap) {
        
        int chunkX = absX >> 4;
        int chunkZ = absZ >> 4;
        
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++ j) {
                int bX = (chunkX << 2) + i;
                int bZ = (chunkZ << 2) + j;
                
                for (int bY = 0; bY < HEIGHTMAP.length; ++bY) {
                    heightmap[bY][0] = this.generateHeightmap(bX, bY, bZ);
                    heightmap[bY][1] = this.generateHeightmap(bX, bY, bZ + 1);
                    heightmap[bY][2] = this.generateHeightmap(bX + 1, bY, bZ);
                    heightmap[bY][3] = this.generateHeightmap(bX + 1, bY, bZ + 1);
                }
                
                for (int bY = 0; bY < 32; ++bY) {
                    double n1 = heightmap[bY][0];
                    double n2 = heightmap[bY][1];
                    double n3 = heightmap[bY][2];
                    double n4 = heightmap[bY][3];
                    double n5 = heightmap[bY + 1][0];
                    double n7 = heightmap[bY + 1][1];
                    double n8 = heightmap[bY + 1][2];
                    double n9 = heightmap[bY + 1][3];
                    
                    for (int pY = 0; pY < 4; ++pY) {
                        double mixY = pY / 4.0;
                        
                        double nx1 = n1 + (n5 - n1) * mixY;
                        double nx2 = n2 + (n7 - n2) * mixY;
                        double nx3 = n3 + (n8 - n3) * mixY;
                        double nx4 = n4 + (n9 - n4) * mixY;
                        
                        for (int pX = 0; pX < 4; ++pX) {
                            double mixX = pX / 4.0;
                            
                            double nz1 = nx1 + (nx3 - nx1) * mixX;
                            double nz2 = nx2 + (nx4 - nx2) * mixX;
                            
                            int x = pX + (i << 2);
                            int z = 0 + (j << 2);
                            int y = (bY << 2) + pY;
                            
                            for (int pZ = 0; pZ < 4; ++pZ) {
                                double mixZ = pZ / 4.0;
                                double noiseValue = nz1 + (nz2 - nz1) * mixZ;
                                if (noiseValue > 0.0D) {
                                    CHUNK_Y[x][z] = y;
                                }
                                
                                z++;
                            }
                        }
                    }
                }
            }
        }
        

        for (int pX = 0; pX < CHUNK_Y.length; pX++) {
            for (int pZ = 0; pZ < CHUNK_Y[pX].length; pZ++) {
                BlockPos cachedPos = new BlockPos((chunkX << 4) + pX, 0, (chunkZ << 4) + pZ);
                //POS.set((chunkX << 4) + pX, 0, (chunkZ << 4) + pZ);
                GROUND_CACHE_Y.put(cachedPos, CHUNK_Y[pX][pZ] + 1); // +1 because it is one above the ground
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
    public int getWorldHeight() {
        return 128;
    }

    @Override
    public int getSeaLevel() {
        return this.settings.wrapped.getSeaLevel();
    }

    @Override
    public ChunkGenerator withSeed(long seed) {
        return new InfdevChunkGenerator(this.biomeSource.withSeed(seed), seed, this.settings);
    }
}
