package com.bespectacled.modernbeta.api.world.gen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

public record OreVeinType (
    BlockState stoneOreBlock,
    BlockState deepslateOreBlock,
    BlockState rawBlock,
    BlockState stoneFillerBlock,
    BlockState deepslateFillerBlock,
    int minY,
    int maxY
) {
    public static final OreVeinType COPPER_UPPER = new OreVeinType(
        Blocks.COPPER_ORE.getDefaultState(),
        Blocks.DEEPSLATE_COPPER_ORE.getDefaultState(),
        Blocks.RAW_COPPER_BLOCK.getDefaultState(),
        Blocks.GRANITE.getDefaultState(),
        Blocks.GRANITE.getDefaultState(),
        0,
        50
    );
    
    public static final OreVeinType IRON_LOWER = new OreVeinType(
        Blocks.IRON_ORE.getDefaultState(),
        Blocks.DEEPSLATE_IRON_ORE.getDefaultState(),
        Blocks.RAW_IRON_BLOCK.getDefaultState(),
        Blocks.COBBLESTONE.getDefaultState(),
        Blocks.TUFF.getDefaultState(),
        -60,
        -8
    );
    
    public static final OreVeinType IRON_UPPER = new OreVeinType(
        Blocks.IRON_ORE.getDefaultState(),
        Blocks.DEEPSLATE_IRON_ORE.getDefaultState(),
        Blocks.RAW_IRON_BLOCK.getDefaultState(),
        Blocks.COBBLESTONE.getDefaultState(),
        Blocks.TUFF.getDefaultState(),
        0,
        50  
    );
    
    public static final OreVeinType COAL_UPPER = new OreVeinType(
        Blocks.COAL_ORE.getDefaultState(),
        Blocks.DEEPSLATE_COAL_ORE.getDefaultState(),
        Blocks.COAL_BLOCK.getDefaultState(),
        Blocks.COBBLESTONE.getDefaultState(),
        Blocks.TUFF.getDefaultState(),
        0,
        50
    );
}
