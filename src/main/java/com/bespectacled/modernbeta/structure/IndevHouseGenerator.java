package com.bespectacled.modernbeta.structure;

import java.util.List;
import java.util.Random;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.SimpleStructurePiece;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;

public class IndevHouseGenerator {
    private static final Identifier HOUSE = ModernBeta.createId("indev_house");
    
    public static void addPieces(StructureManager manager, BlockPos pos, BlockRotation rot, List<StructurePiece> pieces) {
        pieces.add(new HousePiece(manager, pos, HOUSE, rot));
    }
    
    public static class HousePiece extends SimpleStructurePiece {
        private final BlockRotation rot;
        private final Identifier template;
        
        public HousePiece(StructureManager manager, BlockPos pos, Identifier template, BlockRotation rot) {
            super(OldStructures.HOUSE_PIECE, 0);
            this.pos = pos;
            this.rot = rot;
            this.template = template;
            
            this.initializeStructureData(manager);
        }
        
        public HousePiece(StructureManager manager, CompoundTag tag) {
            super(OldStructures.HOUSE_PIECE, tag);
            this.template = new Identifier(tag.getString("Template"));
            this.rot = BlockRotation.valueOf(tag.getString("Rot"));
            
            this.initializeStructureData(manager);
        }
       
        
        private void initializeStructureData(StructureManager manager) {
            Structure structure = manager.getStructureOrBlank(this.template);
            StructurePlacementData placementData = (new StructurePlacementData())
                .setRotation(this.rot)
                .setMirror(BlockMirror.NONE)
                .addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
            this.setStructureData(structure, this.pos, placementData);      
        }
        
        protected void toNbt(CompoundTag tag) {
            super.toNbt(tag);
            tag.putString("Template", this.template.toString());
            tag.putString("Rot", this.rot.name());
        }
        
        @Override
        protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess serverWorldAccess, Random random,
            BlockBox boundingBox) {
            // Handle stuff like loot in chests
        }
    }
}
