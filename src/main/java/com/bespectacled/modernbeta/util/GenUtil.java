package com.bespectacled.modernbeta.util;

import java.util.Iterator;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.structure.JigsawJunction;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.Util;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.feature.StructureFeature;

public class GenUtil {
    private static final float[] NOISE_WEIGHT_TABLE;
    private static final int NOISE_MAX_LIM = 24; 
    
    static {
        NOISE_WEIGHT_TABLE = Util.<float[]>make(new float[NOISE_MAX_LIM * NOISE_MAX_LIM * NOISE_MAX_LIM], arr -> {
            for (int x = 0; x < NOISE_MAX_LIM; ++x) {
                for (int z = 0; z < NOISE_MAX_LIM; ++z) {
                    for (int y = 0; y < NOISE_MAX_LIM; ++y) {
                        arr[x * NOISE_MAX_LIM * NOISE_MAX_LIM + z * NOISE_MAX_LIM + y] = 
                            (float) calculateNoiseWeight(z - NOISE_MAX_LIM / 2, y - NOISE_MAX_LIM / 2, x - NOISE_MAX_LIM / 2);
                    }
                }
            }
            return;
        });
    }
    
    private static double calculateNoiseWeight(int x, int y, int z) {
        double double4 = x * x + z * z;
        double double6 = y + 0.5;
        double double8 = double6 * double6;
        double double10 = Math.pow(2.718281828459045, -(double8 / 16.0 + double4 / 16.0));
        double double12 = -double6 * MathHelper.fastInverseSqrt(double8 / 2.0 + double4 / 2.0) / 2.0;
        return double12 * double10;
    }
    
    public static double getNoiseWeight(int integer1, int integer2, int integer3) {
        int integer4 = integer1 + NOISE_MAX_LIM / 2;
        int integer5 = integer2 + NOISE_MAX_LIM / 2;
        int integer6 = integer3 + NOISE_MAX_LIM / 2;
        if (integer4 < 0 || integer4 >= NOISE_MAX_LIM) {
            return 0.0;
        }
        if (integer5 < 0 || integer5 >= NOISE_MAX_LIM) {
            return 0.0;
        }
        if (integer6 < 0 || integer6 >= NOISE_MAX_LIM) {
            return 0.0;
        }
        return NOISE_WEIGHT_TABLE[integer6 * NOISE_MAX_LIM * NOISE_MAX_LIM + integer4 * NOISE_MAX_LIM + integer5];
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
}
