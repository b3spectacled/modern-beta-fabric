package com.bespectacled.modernbeta.api.world.gen;

import java.util.Iterator;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.structure.JigsawJunction;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.feature.StructureFeature;

public class StructureWeightSampler {
    private final ObjectList<StructurePiece> pieces;
    private final ObjectList<JigsawJunction> junctions;
    
    private final ObjectListIterator<StructurePiece> pieceIterator;
    private final ObjectListIterator<JigsawJunction> junctionIterator;
    
    public StructureWeightSampler(StructureAccessor accessor, Chunk chunk) {
        this.junctions = new ObjectArrayList<>(32);
        this.pieces = new ObjectArrayList<>(10);
        
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
                                pieces.add(poolStructurePiece);
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
                                        junctions.add(jigsawJunction);
                                    }
                                }
                            }
                        } else {
                            pieces.add(structurePiece);
                        }
                    }
                    return;
            });
        }
        
        this.pieceIterator = pieces.iterator();
        this.junctionIterator = junctions.iterator();
    }

    public double sample(int x, int y, int z, double density) {
        double clampedDensity = MathHelper.clamp(density / 200.0, -1.0, 1.0);
        clampedDensity = clampedDensity / 2.0 - clampedDensity * clampedDensity * clampedDensity / 24.0;
        
        while (pieceIterator.hasNext()) {
            StructurePiece curStructurePiece = (StructurePiece) pieceIterator.next();
            BlockBox blockBox = curStructurePiece.getBoundingBox();
            
            int sX = Math.max(0, Math.max(blockBox.minX - x, x - blockBox.maxX));
            int sY = y - (blockBox.minY + ((curStructurePiece instanceof PoolStructurePiece) ? 
                    ((PoolStructurePiece) curStructurePiece).getGroundLevelDelta() : 0));
            int sZ = Math.max(0, Math.max(blockBox.minZ - z, z - blockBox.maxZ));

            clampedDensity += NoiseChunkGenerator.getNoiseWeight(sX, sY, sZ) * 0.8;
        }
        pieceIterator.back(this.pieces.size());

        while (junctionIterator.hasNext()) {
            JigsawJunction curJigsawJunction = (JigsawJunction) junctionIterator.next();

            int jX = x - curJigsawJunction.getSourceX();
            int jY = y - curJigsawJunction.getSourceGroundY();
            int jZ = z - curJigsawJunction.getSourceZ();

            clampedDensity += NoiseChunkGenerator.getNoiseWeight(jX, jY, jZ) * 0.4;
        }
        junctionIterator.back(this.junctions.size());
        
        return clampedDensity;
    }
}
