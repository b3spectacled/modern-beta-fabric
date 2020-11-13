package com.bespectacled.modernbeta.util;

import java.util.Iterator;

import com.bespectacled.modernbeta.biome.BetaBiomes.BiomeType;
import com.bespectacled.modernbeta.biome.IOldBiomeSource;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.JigsawJunction;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
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
    
    public static Biome getOceanBiome(Chunk chunk, ChunkGenerator gen, BiomeSource biomeSource) {
        int biomeX = (chunk.getPos().x << 2) + 2;
        int biomeZ = (chunk.getPos().z << 2) + 2;
        
        int x = chunk.getPos().x << 4;
        int z = chunk.getPos().z << 4;
        
        Biome biome = biomeSource.getBiomeForNoiseGen(biomeX, 2, biomeZ);

        if (gen.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG) < 60) {
            biome = ((IOldBiomeSource)biomeSource).getOceanBiomeForNoiseGen(biomeX, biomeZ);
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
