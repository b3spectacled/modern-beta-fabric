package mod.bespectacled.modernbeta.api.world.chunk;

import java.util.Optional;

import mod.bespectacled.modernbeta.api.registry.ModernBetaRegistries;
import mod.bespectacled.modernbeta.util.BlockStates;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public record SurfaceConfig(BlockState topBlock, BlockState fillerBlock) {
    public static final SurfaceConfig DEFAULT = new SurfaceConfig(BlockStates.GRASS_BLOCK, BlockStates.DIRT);
    public static final SurfaceConfig DESERT = new SurfaceConfig(BlockStates.SAND, BlockStates.SAND);
    public static final SurfaceConfig BADLANDS = new SurfaceConfig(Blocks.RED_SAND.getDefaultState(), Blocks.WHITE_TERRACOTTA.getDefaultState());
    public static final SurfaceConfig NETHER = new SurfaceConfig(Blocks.NETHERRACK.getDefaultState(), Blocks.NETHERRACK.getDefaultState());
    public static final SurfaceConfig THEEND = new SurfaceConfig(Blocks.END_STONE.getDefaultState(), Blocks.END_STONE.getDefaultState());
    
    public static SurfaceConfig getSurfaceConfig(RegistryEntry<Biome> biome) {
        SurfaceConfig config = DEFAULT;
        Optional<String> optionalKey = ModernBetaRegistries.SURFACE_CONFIG.getKeySet()
            .stream()
            .filter(key -> biome.isIn(tag(key)))
            .findFirst();
        
        if (optionalKey.isPresent()) {
            return ModernBetaRegistries.SURFACE_CONFIG.get(optionalKey.get());
        }
        
        return config;
    }
    
    private static TagKey<Biome> tag(String id) {
        return TagKey.of(RegistryKeys.BIOME, new Identifier(id));
    }
}