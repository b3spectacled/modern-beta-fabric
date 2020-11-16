package com.bespectacled.modernbeta.util;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.biome.IOldBiomeSource;
import com.bespectacled.modernbeta.mixin.MixinChunkGeneratorInvoker;

import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.JigsawJunction;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.StructureFeature;

public class GenUtil {
    private static final BlockPos.Mutable POS = new BlockPos.Mutable();
    
    public static int getSolidHeight(Chunk chunk, int x, int z) {
        for (int y = 127; y >= 0; y--) {
            BlockState someBlock = chunk.getBlockState(POS.set(x, y, z));
            if (!(someBlock.equals(BlockStates.AIR) || someBlock.equals(BlockStates.WATER)))
                return y;
        }
        return 0;
    }
    
    public static Biome getOceanBiome(Chunk chunk, ChunkGenerator gen, BiomeSource biomeSource, boolean vanillaGen) {
        int biomeX = (chunk.getPos().x << 2) + 2;
        int biomeZ = (chunk.getPos().z << 2) + 2;
        
        int x = chunk.getPos().x << 4;
        int z = chunk.getPos().z << 4;
        
        Biome biome;

        if (vanillaGen && gen.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG) < 60) {
            biome = ((IOldBiomeSource)biomeSource).getOceanBiomeForNoiseGen(biomeX, biomeZ);
        } else {
            biome = biomeSource.getBiomeForNoiseGen(biomeX, 2, biomeZ);
        }

        return biome;
    }
    
    public static Biome getOceanBiomeAt(int biomeX, int biomeZ, ChunkGenerator gen, BiomeSource biomeSource) {
        int x = biomeX << 2;
        int z = biomeZ << 2;
        
        Biome biome = biomeSource.getBiomeForNoiseGen(biomeX, 2, biomeZ);

        if (gen.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG) < 60) {
            biome = ((IOldBiomeSource)biomeSource).getOceanBiomeForNoiseGen(biomeX, biomeZ);
        }

        return biome;
    }
    
    public static void generateFeaturesWithOcean(
        ChunkRegion chunkRegion, 
        StructureAccessor structureAccessor, 
        ChunkGenerator gen, 
        ChunkRandom random, 
        boolean genOceans
    ) {
        int ctrX = chunkRegion.getCenterChunkX();
        int ctrZ = chunkRegion.getCenterChunkZ();
        int ctrAbsX = ctrX * 16;
        int ctrAbsZ = ctrZ * 16;

        Chunk ctrChunk = chunkRegion.getChunk(ctrX, ctrZ);
        
        Biome biome = GenUtil.getOceanBiome(ctrChunk, gen, gen.getBiomeSource(), genOceans);

        long popSeed = random.setPopulationSeed(chunkRegion.getSeed(), ctrAbsX, ctrAbsZ);
        
        try {
            biome.generateFeatureStep(structureAccessor, (ChunkGenerator)gen, chunkRegion, popSeed, random, new BlockPos(ctrAbsX, 0, ctrAbsZ));
        } catch (Exception exception) {
            CrashReport report = CrashReport.create(exception, "Biome decoration");
            report.addElement("Generation").add("CenterX", ctrX).add("CenterZ", ctrZ).add("Seed", popSeed).add("Biome", biome);
            throw new CrashException(report);
        }
    }
    
    public static void carveWithOcean(
        long seed, 
        BiomeAccess biomeAccess, 
        Chunk chunk, 
        GenerationStep.Carver carver, 
        ChunkGenerator gen, 
        BiomeSource biomeSource,
        Random random,
        int seaLevel,
        boolean genOceans
    ) {
        BiomeAccess biomeAcc = biomeAccess.withSource(biomeSource);
        ChunkPos chunkPos = chunk.getPos();

        int mainChunkX = chunkPos.x;
        int mainChunkZ = chunkPos.z;

        Biome biome = GenUtil.getOceanBiome(chunk, gen, biomeSource, genOceans);
        GenerationSettings genSettings = biome.getGenerationSettings();
        
        BitSet bitSet = ((ProtoChunk) chunk).getOrCreateCarvingMask(carver);

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
                        configuredCarver.carve(chunk, biomeAcc::getBiome, random, seaLevel, chunkX, chunkZ,
                                mainChunkX, mainChunkZ, bitSet);

                    }
                }
            }
        }
    }
    
    public static void setStructureStartsWithOcean(
        DynamicRegistryManager dynamicRegistryManager, 
        StructureAccessor structureAccessor,   
        Chunk chunk, 
        StructureManager structureManager, 
        long seed,
        ChunkGenerator gen,
        BiomeSource biomeSource,
        boolean genOceans
    ) {
        ChunkPos chunkPos = chunk.getPos();
        
        Biome biome = GenUtil.getOceanBiome(chunk, gen, biomeSource, genOceans);

        ((MixinChunkGeneratorInvoker)gen).invokeSetStructureStart(
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
            ((MixinChunkGeneratorInvoker)gen).invokeSetStructureStart(
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
    
    public static void collectStructures(
        Chunk chunk, 
        StructureAccessor accessor, 
        ObjectList<StructurePiece> structureList, 
        ObjectList<JigsawJunction> jigsawList
    ) {
        structureList.clear();
        jigsawList.clear();
        
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
                                structureList.add(poolStructurePiece);
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
    }
    
    public static double addStructDensity(
        ObjectListIterator<StructurePiece> structureListIterator, 
        ObjectListIterator<JigsawJunction> jigsawListIterator,
        int structureListSize, int jigsawListSize,
        int x, int y, int z) {
        double density = 0;
        
        while (structureListIterator.hasNext()) {
            StructurePiece curStructurePiece = (StructurePiece) structureListIterator.next();
            BlockBox blockBox = curStructurePiece.getBoundingBox();
            
            
            int sX = Math.max(0, Math.max(blockBox.minX - x, x - blockBox.maxX));
            int sY = y - (blockBox.minY + ((curStructurePiece instanceof PoolStructurePiece) ? 
                    ((PoolStructurePiece) curStructurePiece).getGroundLevelDelta() : 0));
            int sZ = Math.max(0, Math.max(blockBox.minZ - z, z - blockBox.maxZ));

            density += NoiseChunkGenerator.getNoiseWeight(sX, sY, sZ) * 0.8;
        }
        structureListIterator.back(structureListSize);

        while (jigsawListIterator.hasNext()) {
            JigsawJunction curJigsawJunction = (JigsawJunction) jigsawListIterator.next();

            int jX = x - curJigsawJunction.getSourceX();
            int jY = y - curJigsawJunction.getSourceGroundY();
            int jZ = z - curJigsawJunction.getSourceZ();

            density += NoiseChunkGenerator.getNoiseWeight(jX, jY, jZ) * 0.4;
        }
        jigsawListIterator.back(jigsawListSize);
        
        return density;
    }
    
    public static Block getStructBlock(
        ObjectListIterator<StructurePiece> structureListIterator, 
        ObjectListIterator<JigsawJunction> jigsawListIterator,
        int structureListSize, int jigsawListSize,
        int x, int y, int z, Block blockToSet) {
        
        while (structureListIterator.hasNext()) {
            StructurePiece curStructurePiece = (StructurePiece) structureListIterator.next();
            BlockBox blockBox = curStructurePiece.getBoundingBox();

            int sX = Math.max(0, Math.max(blockBox.minX - x, x - blockBox.maxX));
            int sY = y - (blockBox.minY + ((curStructurePiece instanceof PoolStructurePiece)
                    ? ((PoolStructurePiece) curStructurePiece).getGroundLevelDelta() : 0));
            int sZ = Math.max(0, Math.max(blockBox.minZ - z, z - blockBox.maxZ));

            if (sY < 0 && sX == 0 && sZ == 0) {
                if (sY == -1) blockToSet = Blocks.GRASS_BLOCK;
                else if (sY >= -2) blockToSet = Blocks.DIRT;
                else if (sY >= -4) blockToSet = Blocks.STONE;
            }
                
        }
        structureListIterator.back(structureListSize);

        while (jigsawListIterator.hasNext()) {
            JigsawJunction curJigsawJunction = (JigsawJunction) jigsawListIterator.next();

            int jX = x - curJigsawJunction.getSourceX();
            int jY = y - curJigsawJunction.getSourceGroundY();
            int jZ = z - curJigsawJunction.getSourceZ();

            if (jY < 0 && jX == 0 && jZ == 0) {
                if (jY == -1) blockToSet = Blocks.GRASS_BLOCK;
                else if (jY >= -2) blockToSet = Blocks.DIRT;
                else if (jY >= -4) blockToSet = Blocks.STONE;
            }
        }
        jigsawListIterator.back(jigsawListSize);
        
        return blockToSet;
    }
}
