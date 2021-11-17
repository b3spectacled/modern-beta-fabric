package com.bespectacled.modernbeta.world.structure;

import java.util.Random;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.SimpleStructurePiece;
import net.minecraft.structure.StructureContext;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiecesHolder;
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
import net.minecraft.world.gen.feature.FeatureConfig;

public class OceanShrineGenerator {
    private static final Identifier SHRINE_BASE = ModernBeta.createId("ocean_shrine/base");
    
    /*
    public static void addPieces(StructureManager manager, BlockPos pos, BlockRotation rot, StructurePiecesHolder structurePiecesHolder, List<StructurePiece> pieces) {
        pieces.add(new Piece(manager, pos, structurePiecesHolder, SHRINE_BASE, rot));
    }
    */
    
    public static void addPieces(
        StructureManager manager, 
        BlockPos pos, 
        BlockRotation rot, 
        StructurePiecesHolder structurePiecesHolder, 
        Random random, 
        FeatureConfig config
    ) {
        structurePiecesHolder.addPiece(new Piece(manager, pos, structurePiecesHolder, SHRINE_BASE, rot));
    }
    
    public static class Piece extends SimpleStructurePiece {
        public Piece(StructureManager manager, BlockPos pos, StructurePiecesHolder structurePiecesHolder, Identifier template, BlockRotation rot) {
            super(OldStructures.OCEAN_SHRINE_PIECE, 0, manager, template, template.toString(), getPlacementData(rot), pos);
        }
        
        public Piece(StructureManager manager, NbtCompound tag) {
            super(OldStructures.OCEAN_SHRINE_PIECE, tag, manager, identifier -> getPlacementData(BlockRotation.valueOf(tag.getString("Rot"))));
        }
        
        private static StructurePlacementData getPlacementData(BlockRotation rotation) {
            return new StructurePlacementData().setRotation(rotation).setMirror(BlockMirror.NONE).addProcessor(BlockIgnoreStructureProcessor.IGNORE_AIR_AND_STRUCTURE_BLOCKS);
        }
        
        @Override
        protected void writeNbt(StructureContext context, NbtCompound nbtCompound) {
            super.writeNbt(context, nbtCompound);
            nbtCompound.putString("Rot", this.placementData.getRotation().name());
        }
        
        @Override
        protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox boundingBox) {
            if ("chest".equals(metadata)) {
                world.setBlockState(pos, Blocks.CHEST.getDefaultState().with(ChestBlock.WATERLOGGED, world.getFluidState(pos).isIn(FluidTags.WATER)), 2);
                
                if (world.getBlockEntity(pos) instanceof ChestBlockEntity chestBlockEntity) {
                    chestBlockEntity.setLootTable(LootTables.BURIED_TREASURE_CHEST, random.nextLong());
                }
            }
        }
        
        @Override
        public void generate(
            StructureWorldAccess world, 
            StructureAccessor accessor, 
            ChunkGenerator chunkGenerator, 
            Random random, 
            BlockBox blockBox, 
            ChunkPos chunkPos, 
            BlockPos blockPos
        ) {
            this.placementData.clearProcessors().addProcessor(new BlockRotStructureProcessor(1.0f)).addProcessor(BlockIgnoreStructureProcessor.IGNORE_AIR_AND_STRUCTURE_BLOCKS);
            
            super.generate(world, accessor, chunkGenerator, random, blockBox, chunkPos, blockPos);
        }
    }
}
