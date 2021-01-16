package com.bespectacled.modernbeta.structure;

import java.util.List;
import java.util.Random;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.structure.IndevHouseGenerator.HousePiece;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import net.minecraft.structure.SimpleStructurePiece;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.structure.processor.BlockRotStructureProcessor;
import net.minecraft.tag.BlockTags;
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
import net.minecraft.world.BlockView;
import net.minecraft.world.Heightmap;

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
        
        public Piece(StructureManager manager, CompoundTag tag) {
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
        
        protected void toNbt(CompoundTag tag) {
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
            
            int y = world.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, this.pos.getX(), this.pos.getZ());
            this.pos = new BlockPos(this.pos.getX(), y, this.pos.getZ());
            
            BlockPos structPos = Structure.transformAround(new BlockPos(this.structure.getSize().getX() - 1, 0, this.structure.getSize().getZ() - 1), BlockMirror.NONE, this.rot, BlockPos.ORIGIN).add(this.pos);
            this.pos = new BlockPos(this.pos.getX(), this.method_14829(this.pos, world, structPos), this.pos.getZ());
            return super.generate(world, accessor, chunkGenerator, random, blockBox, chunkPos, blockPos);
        }
        
        private int method_14829(BlockPos blockPos2, BlockView blockView, BlockPos blockPos4) {
            int integer5 = blockPos2.getY();
            int integer6 = 512;
            int integer7 = integer5 - 1;
            int integer8 = 0;
            for (final BlockPos lv : BlockPos.iterate(blockPos2, blockPos4)) {
                int integer11 = lv.getX();
                int integer12 = lv.getZ();
                int integer13 = blockPos2.getY() - 1;
                BlockPos.Mutable mutable14 = new BlockPos.Mutable(integer11, integer13, integer12);
                BlockState blockState15 = blockView.getBlockState(mutable14);
                for (FluidState lv4 = blockView.getFluidState(mutable14); (blockState15.isAir() || lv4.isIn(FluidTags.WATER) || blockState15.isIn(BlockTags.ICE)) && integer13 > blockView.getSectionCount() + 1; blockState15 = blockView.getBlockState(mutable14), lv4 = blockView.getFluidState(mutable14)) {
                    --integer13;
                    mutable14.set(integer11, integer13, integer12);
                }
                integer6 = Math.min(integer6, integer13);
                if (integer13 < integer7 - 2) {
                    ++integer8;
                }
            }
            int integer9 = Math.abs(blockPos2.getX() - blockPos4.getX());
            if (integer7 - integer6 > 2 && integer8 > integer9 - 2) {
                integer5 = integer6 + 1;
            }
            return integer5;
        }
    }
}
