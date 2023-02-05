package mod.bespectacled.modernbeta;

import mod.bespectacled.modernbeta.api.registry.ModernBetaRegistries;
import mod.bespectacled.modernbeta.api.world.chunk.SurfaceConfig;
import mod.bespectacled.modernbeta.api.world.chunk.noise.NoisePostProcessor;
import mod.bespectacled.modernbeta.world.biome.provider.BiomeProviderBeta;
import mod.bespectacled.modernbeta.world.biome.provider.BiomeProviderPE;
import mod.bespectacled.modernbeta.world.biome.provider.BiomeProviderSingle;
import mod.bespectacled.modernbeta.world.biome.provider.BiomeProviderVoronoi;
import mod.bespectacled.modernbeta.world.cavebiome.provider.CaveBiomeProviderNone;
import mod.bespectacled.modernbeta.world.cavebiome.provider.CaveBiomeProviderSingle;
import mod.bespectacled.modernbeta.world.cavebiome.provider.CaveBiomeProviderVoronoi;
import mod.bespectacled.modernbeta.world.chunk.provider.ChunkProviderAlpha;
import mod.bespectacled.modernbeta.world.chunk.provider.ChunkProviderBeta;
import mod.bespectacled.modernbeta.world.chunk.provider.ChunkProviderClassic030;
import mod.bespectacled.modernbeta.world.chunk.provider.ChunkProviderIndev;
import mod.bespectacled.modernbeta.world.chunk.provider.ChunkProviderInfdev227;
import mod.bespectacled.modernbeta.world.chunk.provider.ChunkProviderInfdev415;
import mod.bespectacled.modernbeta.world.chunk.provider.ChunkProviderInfdev420;
import mod.bespectacled.modernbeta.world.chunk.provider.ChunkProviderInfdev611;
import mod.bespectacled.modernbeta.world.chunk.provider.ChunkProviderPE;
import mod.bespectacled.modernbeta.world.chunk.provider.ChunkProviderSkylands;

/*
 * Registration of built-in providers for various things.
 *  
 */
public class ModernBetaBuiltInProviders {
    
    // Register default chunk providers
    public static void registerChunkProviders() {
        ModernBetaRegistries.CHUNK.register(ModernBetaBuiltInTypes.DEFAULT_ID, ChunkProviderBeta::new);
        ModernBetaRegistries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.BETA.name, ChunkProviderBeta::new);
        ModernBetaRegistries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.SKYLANDS.name, ChunkProviderSkylands::new);
        ModernBetaRegistries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.ALPHA.name, ChunkProviderAlpha::new);
        ModernBetaRegistries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.INFDEV_611.name, ChunkProviderInfdev611::new);
        ModernBetaRegistries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.INFDEV_420.name, ChunkProviderInfdev420::new);
        ModernBetaRegistries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.INFDEV_415.name, ChunkProviderInfdev415::new);
        ModernBetaRegistries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.INFDEV_227.name, ChunkProviderInfdev227::new);
        ModernBetaRegistries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.INDEV.name, ChunkProviderIndev::new);
        ModernBetaRegistries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.CLASSIC_0_30.name, ChunkProviderClassic030::new);
        ModernBetaRegistries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.PE.name, ChunkProviderPE::new);
    }
    
    // Register default biome providers
    public static void registerBiomeProviders() {
        ModernBetaRegistries.BIOME.register(ModernBetaBuiltInTypes.DEFAULT_ID, BiomeProviderBeta::new);
        ModernBetaRegistries.BIOME.register(ModernBetaBuiltInTypes.Biome.BETA.name, BiomeProviderBeta::new);
        ModernBetaRegistries.BIOME.register(ModernBetaBuiltInTypes.Biome.SINGLE.name, BiomeProviderSingle::new);
        ModernBetaRegistries.BIOME.register(ModernBetaBuiltInTypes.Biome.PE.name, BiomeProviderPE::new);
        ModernBetaRegistries.BIOME.register(ModernBetaBuiltInTypes.Biome.VORONOI.name, BiomeProviderVoronoi::new);
    }
    
    // Register default cave biome providers
    public static void registerCaveBiomeProviders() {
        ModernBetaRegistries.CAVE_BIOME.register(ModernBetaBuiltInTypes.DEFAULT_ID, CaveBiomeProviderNone::new);
        ModernBetaRegistries.CAVE_BIOME.register(ModernBetaBuiltInTypes.CaveBiome.NONE.name, CaveBiomeProviderNone::new);
        ModernBetaRegistries.CAVE_BIOME.register(ModernBetaBuiltInTypes.CaveBiome.SINGLE.name, CaveBiomeProviderSingle::new);
        ModernBetaRegistries.CAVE_BIOME.register(ModernBetaBuiltInTypes.CaveBiome.VORONOI.name, CaveBiomeProviderVoronoi::new);
    }
    
    public static void registerNoisePostProcessors() {
        ModernBetaRegistries.NOISE_POST_PROCESSOR.register(ModernBetaBuiltInTypes.NoisePostProcessor.NONE.name, NoisePostProcessor.DEFAULT);
    }
    
    public static void registerSurfaceConfigs() {
        ModernBetaRegistries.SURFACE_CONFIG.register(ModernBetaBuiltInTypes.SurfaceConfig.DESERT.id, SurfaceConfig.DESERT);
        ModernBetaRegistries.SURFACE_CONFIG.register(ModernBetaBuiltInTypes.SurfaceConfig.BADLANDS.id, SurfaceConfig.BADLANDS);
        ModernBetaRegistries.SURFACE_CONFIG.register(ModernBetaBuiltInTypes.SurfaceConfig.NETHER.id, SurfaceConfig.NETHER);
        ModernBetaRegistries.SURFACE_CONFIG.register(ModernBetaBuiltInTypes.SurfaceConfig.THEEND.id, SurfaceConfig.THEEND);
    }
}
