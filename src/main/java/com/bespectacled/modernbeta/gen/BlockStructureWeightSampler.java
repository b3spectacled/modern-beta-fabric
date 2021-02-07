package com.bespectacled.modernbeta.gen;

import com.bespectacled.modernbeta.mixin.MixinStructureWeightSampler;

import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.structure.JigsawJunction;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockBox;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.StructureWeightSampler;

public class BlockStructureWeightSampler extends StructureWeightSampler {
    public BlockStructureWeightSampler(StructureAccessor accessor, Chunk chunk) {
        super(accessor, chunk);
    }
    
    public Block getBlockWeight(int x, int y, int z, Block blockToSet) {
        ObjectList<StructurePiece> pieces = ((MixinStructureWeightSampler)this).getPieces();
        ObjectList<JigsawJunction> junctions = ((MixinStructureWeightSampler)this).getJunctions();
        
        ObjectListIterator<StructurePiece> pieceIterator = ((MixinStructureWeightSampler)this).getPieceIterator();
        ObjectListIterator<JigsawJunction> junctionIterator = ((MixinStructureWeightSampler)this).getJunctionIterator();
        
        while (pieceIterator.hasNext()) {
            StructurePiece curStructurePiece = (StructurePiece) pieceIterator.next();
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
        pieceIterator.back(pieces.size());

        while (junctionIterator.hasNext()) {
            JigsawJunction curJigsawJunction = (JigsawJunction) junctionIterator.next();

            int jX = x - curJigsawJunction.getSourceX();
            int jY = y - curJigsawJunction.getSourceGroundY();
            int jZ = z - curJigsawJunction.getSourceZ();

            if (jY < 0 && jX == 0 && jZ == 0) {
                if (jY == -1) blockToSet = Blocks.GRASS_BLOCK;
                else if (jY >= -2) blockToSet = Blocks.DIRT;
                else if (jY >= -4) blockToSet = Blocks.STONE;
            }
        }
        junctionIterator.back(junctions.size());
        
        return blockToSet;
    }
}
