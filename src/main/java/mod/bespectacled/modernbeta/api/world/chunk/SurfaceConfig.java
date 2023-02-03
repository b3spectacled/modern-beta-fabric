package mod.bespectacled.modernbeta.api.world.chunk;

import mod.bespectacled.modernbeta.util.BlockStates;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public record SurfaceConfig(BlockState topBlock, BlockState fillerBlock) {
    private static final SurfaceConfig DESERT = new SurfaceConfig(BlockStates.SAND, BlockStates.SAND);
    private static final SurfaceConfig DEFAULT = new SurfaceConfig(BlockStates.GRASS_BLOCK, BlockStates.DIRT);
    private static final SurfaceConfig BADLANDS = new SurfaceConfig(Blocks.RED_SAND.getDefaultState(), Blocks.WHITE_TERRACOTTA.getDefaultState());
    private static final SurfaceConfig NETHER = new SurfaceConfig(Blocks.NETHERRACK.getDefaultState(), Blocks.NETHERRACK.getDefaultState());
    private static final SurfaceConfig THEEND = new SurfaceConfig(Blocks.END_STONE.getDefaultState(), Blocks.END_STONE.getDefaultState());
    
    public static SurfaceConfig getSurfaceConfig(RegistryEntry<Biome> biome) {
        SurfaceConfig config = DEFAULT;
        
        if (biome.isIn(TagKey.of(RegistryKeys.BIOME, new Identifier("is_desert"))))
            config = DESERT;
        else if (biome.isIn(BiomeTags.IS_BADLANDS))
            config = BADLANDS;
        else if (biome.isIn(BiomeTags.IS_NETHER))
            config = NETHER;
        else if (biome.isIn(BiomeTags.IS_END))
            config = THEEND;
        
        return config;
    }
}