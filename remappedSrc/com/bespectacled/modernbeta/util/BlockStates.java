package com.bespectacled.modernbeta.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

public class BlockStates {
    public static BlockState GRASS_BLOCK = Blocks.GRASS_BLOCK.getDefaultState();
    public static BlockState PODZOL = Blocks.PODZOL.getDefaultState();
    public static BlockState DIRT = Blocks.DIRT.getDefaultState();
    
    public static BlockState GRAVEL = Blocks.GRAVEL.getDefaultState();
    public static BlockState SAND = Blocks.SAND.getDefaultState();
    public static BlockState SANDSTONE = Blocks.SANDSTONE.getDefaultState();
    
    public static BlockState STONE = Blocks.STONE.getDefaultState();
    public static BlockState AIR = Blocks.AIR.getDefaultState();
    public static BlockState WATER = Blocks.WATER.getDefaultState();
    public static BlockState ICE = Blocks.ICE.getDefaultState();
    public static BlockState LAVA = Blocks.LAVA.getDefaultState();
    public static BlockState BEDROCK = Blocks.BEDROCK.getDefaultState();
    
    public static BlockState getBlockState(Block b) {
        BlockState state;
        
        if (b == Blocks.GRASS_BLOCK) state = GRASS_BLOCK;
        else if (b == Blocks.PODZOL) state = PODZOL;
        else if (b == Blocks.DIRT) state = DIRT;
        else if (b == Blocks.GRAVEL) state = GRAVEL;
        else if (b == Blocks.SAND) state = SAND;
        else if (b == Blocks.SANDSTONE) state = SANDSTONE;
        else if (b == Blocks.STONE) state = STONE;
        else if (b == Blocks.AIR) state = AIR;
        else if (b == Blocks.WATER) state = WATER;
        else if (b == Blocks.ICE) state = ICE;
        else if (b == Blocks.LAVA) state = LAVA;
        else if (b == Blocks.BEDROCK) state = BEDROCK;
        else state = b.getDefaultState();
        
        return state;
    }
    
}
