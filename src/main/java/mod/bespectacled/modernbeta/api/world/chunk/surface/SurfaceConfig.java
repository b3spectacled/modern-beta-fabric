package mod.bespectacled.modernbeta.api.world.chunk.surface;

import java.util.Optional;

import mod.bespectacled.modernbeta.api.registry.ModernBetaRegistries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public record SurfaceConfig(SurfaceBlocks normal, SurfaceBlocks beachSand, SurfaceBlocks beachGravel) {
    private SurfaceConfig(SurfaceBlocks surfaceBlocks) {
        this(surfaceBlocks, surfaceBlocks, surfaceBlocks);
    }
    
    public static final SurfaceConfig DEFAULT = new SurfaceConfig(SurfaceBlocks.GRASS, SurfaceBlocks.SAND, SurfaceBlocks.GRAVEL);
    public static final SurfaceConfig SAND = new SurfaceConfig(SurfaceBlocks.SAND, SurfaceBlocks.SAND, SurfaceBlocks.GRAVEL);
    public static final SurfaceConfig RED_SAND = new SurfaceConfig(SurfaceBlocks.RED_SAND, SurfaceBlocks.RED_SAND, SurfaceBlocks.GRAVEL);
    public static final SurfaceConfig BADLANDS = new SurfaceConfig(SurfaceBlocks.BADLANDS, SurfaceBlocks.RED_SAND, SurfaceBlocks.GRAVEL);
    public static final SurfaceConfig NETHER = new SurfaceConfig(SurfaceBlocks.NETHER, SurfaceBlocks.NETHER_SOUL_SAND, SurfaceBlocks.NETHER_GRAVEL);
    public static final SurfaceConfig WARPED_NYLIUM = new SurfaceConfig(SurfaceBlocks.WARPED_NYLIUM, SurfaceBlocks.NETHER_SOUL_SAND, SurfaceBlocks.NETHER_GRAVEL);
    public static final SurfaceConfig CRIMSON_NYLIUM = new SurfaceConfig(SurfaceBlocks.CRIMSON_NYLIUM, SurfaceBlocks.NETHER_SOUL_SAND, SurfaceBlocks.NETHER_GRAVEL);
    public static final SurfaceConfig BASALT = new SurfaceConfig(SurfaceBlocks.BASALT);
    public static final SurfaceConfig SOUL_SOIL = new SurfaceConfig(SurfaceBlocks.SOUL_SOIL);
    public static final SurfaceConfig THEEND = new SurfaceConfig(SurfaceBlocks.THEEND);
    public static final SurfaceConfig GRASS = new SurfaceConfig(SurfaceBlocks.GRASS);
    public static final SurfaceConfig MUD = new SurfaceConfig(SurfaceBlocks.MUD);
    public static final SurfaceConfig MYCELIUM = new SurfaceConfig(SurfaceBlocks.MYCELIUM);
    public static final SurfaceConfig PODZOL = new SurfaceConfig(SurfaceBlocks.PODZOL);
    public static final SurfaceConfig STONE = new SurfaceConfig(SurfaceBlocks.STONE);
    public static final SurfaceConfig SNOW = new SurfaceConfig(SurfaceBlocks.SNOW);
    public static final SurfaceConfig SNOW_DIRT = new SurfaceConfig(SurfaceBlocks.SNOW_DIRT);
    public static final SurfaceConfig SNOW_PACKED_ICE = new SurfaceConfig(SurfaceBlocks.SNOW_PACKED_ICE);
    public static final SurfaceConfig SNOW_STONE = new SurfaceConfig(SurfaceBlocks.SNOW_STONE);
    
    public static SurfaceConfig getSurfaceConfig(RegistryEntry<Biome> biome) {
        Optional<String> optionalKey = ModernBetaRegistries.SURFACE_CONFIG.getKeySet()
            .stream()
            .filter(id -> biome.isIn(keyOf(id)))
            .findFirst();
        
        if (optionalKey.isPresent()) {
            return ModernBetaRegistries.SURFACE_CONFIG.get(optionalKey.get());
        }
        
        return DEFAULT;
    }
    
    private static TagKey<Biome> keyOf(String id) {
        return TagKey.of(RegistryKeys.BIOME, new Identifier(id));
    }
}