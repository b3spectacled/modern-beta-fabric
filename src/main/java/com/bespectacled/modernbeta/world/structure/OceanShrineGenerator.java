package com.bespectacled.modernbeta.world.structure;

import java.util.List;
import java.util.Random;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.SimpleStructurePiece;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.structure.processor.BlockRotStructureProcessor;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class OceanShrineGenerator {
    private static final Identifier SHRINE_BASE = ModernBeta.createId("ocean_shrine/base");
    
    public static void addPieces(StructureManager manager, BlockPos pos, BlockRotation rot, List<StructurePiece> pieces) {
        pieces.add(new Piece(manager, pos, SHRINE_BASE, rot));
    }
    
    public static class Piece extends SimpleStructurePiece {
        private final BlockRotation rot;
        private final Identifier template;
        
        public Piece(StructureManager manager, BlockPos pos, Identifier template, BlockRotation rot) {
            super(OldStructures.OCEAN_SHRINE_PIECE, 0);
            this.pos = pos;
            this.rot = rot;
            this.template = template;
            
            this.initializeStructureData(manager);
        }
        
        public Piece(StructureManager manager, NbtCompound tag) {
            super(OldStructures.OCEAN_SHRINE_PIECE, tag);
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
        
        protected void toNbt(NbtCompound tag) {
            super.toNbt(tag);
            tag.putString("Template", this.template.toString());
            tag.putString("Rot", this.rot.name());
        }
        
        @Override
        protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess serverWorldAccess, Random random,
            BlockBox boundingBox) {
            if ("chest".equals(metadata)) {
                serverWorldAccess.setBlockState(
                    pos, 
                    Blocks.CHEST.getDefaultState().with(
                        ChestBlock.WATERLOGGED, 
                        serverWorldAccess.getFluidState(pos).isIn(FluidTags.WATER)), 
                    2);
                
                BlockEntity blockEnt = serverWorldAccess.getBlockEntity(pos);
                if (blockEnt instanceof ChestBlockEntity) {
                    ((ChestBlockEntity)blockEnt).setLootTable(LootTables.BURIED_TREASURE_CHEST, random.nextLong());
                }
            }
        }
        
        @Override
        public boolean generate(
            StructureWorldAccess world, 
            StructureAccessor accessor, 
            ChunkGenerator chunkGenerator, 
            Random random, 
            BlockBox blockBox, 
            ChunkPos chunkPos, 
            BlockPos blockPos
        ) {
            this.placementData.clearProcessors().addProcessor(new BlockRotStructureProcessor(1.0f)).addProcessor(BlockIgnoreStructureProcessor.IGNORE_AIR_AND_STRUCTURE_BLOCKS);

            return super.generate(world, accessor, chunkGenerator, random, blockBox, chunkPos, blockPos);
        }
    }
}
