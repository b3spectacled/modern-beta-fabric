package mod.bespectacled.modernbeta.api.world.chunk;

import mod.bespectacled.modernbeta.util.BlockStates;
import net.minecraft.block.BlockState;

public record SurfaceBlocks(BlockState topBlock, BlockState fillerBlock) {
    public static final SurfaceBlocks DEFAULT = new SurfaceBlocks(BlockStates.GRASS_BLOCK, BlockStates.DIRT);
    public static final SurfaceBlocks SAND = new SurfaceBlocks(BlockStates.SAND, BlockStates.SAND);
    public static final SurfaceBlocks RED_SAND = new SurfaceBlocks(BlockStates.RED_SAND, BlockStates.RED_SAND);
    public static final SurfaceBlocks BADLANDS = new SurfaceBlocks(BlockStates.RED_SAND, BlockStates.WHITE_TERRACOTTA);
    public static final SurfaceBlocks NETHER = new SurfaceBlocks(BlockStates.NETHERRACK, BlockStates.NETHERRACK);
    public static final SurfaceBlocks THEEND = new SurfaceBlocks(BlockStates.END_STONE, BlockStates.END_STONE);
    
    public static final SurfaceBlocks GRAVEL = new SurfaceBlocks(BlockStates.AIR, BlockStates.GRAVEL);
    public static final SurfaceBlocks NETHER_SOUL_SAND = new SurfaceBlocks(BlockStates.SOUL_SAND, BlockStates.SOUL_SAND);
    public static final SurfaceBlocks NETHER_GRAVEL = new SurfaceBlocks(BlockStates.GRAVEL, BlockStates.NETHERRACK);
}