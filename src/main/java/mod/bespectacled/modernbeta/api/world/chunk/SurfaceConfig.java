package mod.bespectacled.modernbeta.api.world.chunk;

import java.util.Optional;

import mod.bespectacled.modernbeta.api.registry.ModernBetaRegistries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public record SurfaceConfig(SurfaceBlocks normal, SurfaceBlocks beachSand, SurfaceBlocks beachGravel) {
    public static final SurfaceConfig DEFAULT = new SurfaceConfig(SurfaceBlocks.DEFAULT, SurfaceBlocks.SAND, SurfaceBlocks.GRAVEL);
    public static final SurfaceConfig SAND = new SurfaceConfig(SurfaceBlocks.SAND, SurfaceBlocks.SAND, SurfaceBlocks.GRAVEL);
    public static final SurfaceConfig RED_SAND = new SurfaceConfig(SurfaceBlocks.RED_SAND, SurfaceBlocks.RED_SAND, SurfaceBlocks.GRAVEL);
    public static final SurfaceConfig BADLANDS = new SurfaceConfig(SurfaceBlocks.BADLANDS, SurfaceBlocks.RED_SAND, SurfaceBlocks.GRAVEL);
    public static final SurfaceConfig NETHER = new SurfaceConfig(SurfaceBlocks.NETHER, SurfaceBlocks.NETHER_SOUL_SAND, SurfaceBlocks.NETHER_GRAVEL);
    public static final SurfaceConfig THEEND = new SurfaceConfig(SurfaceBlocks.THEEND, SurfaceBlocks.THEEND, SurfaceBlocks.THEEND);
    public static final SurfaceConfig SWAMP = new SurfaceConfig(SurfaceBlocks.DEFAULT, SurfaceBlocks.DEFAULT, SurfaceBlocks.DEFAULT);
    
    public static SurfaceConfig getSurfaceConfig(RegistryEntry<Biome> biome) {
        SurfaceConfig config = DEFAULT;
        Optional<String> optionalKey = ModernBetaRegistries.SURFACE_CONFIG.getKeySet()
            .stream()
            .filter(id -> biome.isIn(keyOf(id)))
            .findFirst();
        
        if (optionalKey.isPresent()) {
            return ModernBetaRegistries.SURFACE_CONFIG.get(optionalKey.get());
        }
        
        return config;
    }
    
    private static TagKey<Biome> keyOf(String id) {
        return TagKey.of(RegistryKeys.BIOME, new Identifier(id));
    }
}