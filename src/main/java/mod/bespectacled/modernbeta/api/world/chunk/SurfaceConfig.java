package mod.bespectacled.modernbeta.api.world.chunk;

import mod.bespectacled.modernbeta.util.BlockStates;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;

public record SurfaceConfig(BlockState topBlock, BlockState fillerBlock) {
    private static final SurfaceConfig DESERT = new SurfaceConfig(BlockStates.SAND, BlockStates.SAND);
    private static final SurfaceConfig DEFAULT = new SurfaceConfig(BlockStates.GRASS_BLOCK, BlockStates.DIRT);
    private static final SurfaceConfig BADLANDS = new SurfaceConfig(Blocks.RED_SAND.getDefaultState(), Blocks.WHITE_TERRACOTTA.getDefaultState());
    private static final SurfaceConfig NETHER = new SurfaceConfig(Blocks.NETHERRACK.getDefaultState(), Blocks.NETHERRACK.getDefaultState());
    private static final SurfaceConfig THEEND = new SurfaceConfig(Blocks.END_STONE.getDefaultState(), Blocks.END_STONE.getDefaultState());
    
    public static SurfaceConfig getSurfaceConfig(RegistryEntry<Biome> biome) {
        @SuppressWarnings("deprecation")
        Category category = Biome.getCategory(biome);
        
        return switch(category) {
            case DESERT -> DESERT;
            case MESA -> BADLANDS;
            case NETHER -> NETHER;
            case THEEND -> THEEND;
            default -> DEFAULT;
        };
    }
}