package mod.bespectacled.modernbeta.api.world.chunk;

import mod.bespectacled.modernbeta.util.BlockStates;
import net.minecraft.block.BlockState;

public record SurfaceBlocks(BlockState topBlock, BlockState fillerBlock) {
    public static final SurfaceBlocks GRASS = new SurfaceBlocks(BlockStates.GRASS_BLOCK, BlockStates.DIRT);
    public static final SurfaceBlocks SAND = new SurfaceBlocks(BlockStates.SAND, BlockStates.SAND);
    public static final SurfaceBlocks RED_SAND = new SurfaceBlocks(BlockStates.RED_SAND, BlockStates.RED_SAND);
    public static final SurfaceBlocks BADLANDS = new SurfaceBlocks(BlockStates.RED_SAND, BlockStates.WHITE_TERRACOTTA);
    public static final SurfaceBlocks STONE = new SurfaceBlocks(BlockStates.STONE, BlockStates.STONE);
    public static final SurfaceBlocks SNOW = new SurfaceBlocks(BlockStates.SNOW_BLOCK, BlockStates.SNOW_BLOCK);
    public static final SurfaceBlocks SNOW_DIRT = new SurfaceBlocks(BlockStates.SNOW_BLOCK, BlockStates.DIRT);
    public static final SurfaceBlocks SNOW_PACKED_ICE = new SurfaceBlocks(BlockStates.SNOW_BLOCK, BlockStates.PACKED_ICE);
    
    public static final SurfaceBlocks GRAVEL = new SurfaceBlocks(BlockStates.AIR, BlockStates.GRAVEL);
    public static final SurfaceBlocks NETHER_SOUL_SAND = new SurfaceBlocks(BlockStates.SOUL_SAND, BlockStates.SOUL_SAND);
    public static final SurfaceBlocks NETHER_GRAVEL = new SurfaceBlocks(BlockStates.GRAVEL, BlockStates.NETHERRACK);
    
    public static final SurfaceBlocks MUD = new SurfaceBlocks(BlockStates.MUD, BlockStates.MUD);
    public static final SurfaceBlocks MYCELIUM = new SurfaceBlocks(BlockStates.MYCELIUM, BlockStates.DIRT);
    public static final SurfaceBlocks PODZOL = new SurfaceBlocks(BlockStates.PODZOL, BlockStates.DIRT);
    
    public static final SurfaceBlocks NETHER = new SurfaceBlocks(BlockStates.NETHERRACK, BlockStates.NETHERRACK);
    public static final SurfaceBlocks WARPED_NYLIUM = new SurfaceBlocks(BlockStates.WARPED_NYLIUM, BlockStates.NETHERRACK);
    public static final SurfaceBlocks CRIMSON_NYLIUM = new SurfaceBlocks(BlockStates.CRIMSON_NYLIUM, BlockStates.NETHERRACK);
    public static final SurfaceBlocks BASALT = new SurfaceBlocks(BlockStates.BASALT, BlockStates.BASALT);
    public static final SurfaceBlocks SOUL_SOIL = new SurfaceBlocks(BlockStates.SOUL_SOIL, BlockStates.SOUL_SOIL);
    public static final SurfaceBlocks THEEND = new SurfaceBlocks(BlockStates.END_STONE, BlockStates.END_STONE);
    
}