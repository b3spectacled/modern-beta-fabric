package com.bespectacled.modernbeta.gen;

import java.util.Iterator;
import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.util.BlockStates;

import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.JigsawJunction;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.feature.StructureFeature;

public class GenUtil {
    private static final BlockPos.Mutable POS = new BlockPos.Mutable();
    
    public static int getSolidHeight(Chunk chunk, int worldHeight, int x, int z) {
        for (int y = worldHeight - 1; y >= 0; y--) {
            BlockState someBlock = chunk.getBlockState(POS.set(x, y, z));
            if (!(someBlock.equals(BlockStates.AIR) || someBlock.equals(BlockStates.WATER)))
                return y;
        }
        return 0;
    }
    
    public static Biome getOceanBiome(Chunk chunk, ChunkGenerator gen, BiomeSource biomeSource, boolean generateOceans, int seaLevel) {
        int x = chunk.getPos().getStartX();
        int z = chunk.getPos().getStartZ();
        
        int biomeX = x >> 2;
        int biomeZ = z >> 2;
        
        Biome biome;

        if (generateOceans && gen.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG) < seaLevel - 4) {
            biome = ((OldBiomeSource)biomeSource).getOceanBiomeForNoiseGen(biomeX, 0, biomeZ);
        } else {
            biome = biomeSource.getBiomeForNoiseGen(biomeX, 2, biomeZ);
        }

        return biome;
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
