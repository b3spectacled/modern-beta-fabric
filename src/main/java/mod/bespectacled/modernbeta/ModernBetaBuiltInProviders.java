package mod.bespectacled.modernbeta;

import mod.bespectacled.modernbeta.api.registry.Registries;
import mod.bespectacled.modernbeta.api.world.gen.noise.NoisePostProcessor;
import mod.bespectacled.modernbeta.util.settings.ImmutableSettings;
import mod.bespectacled.modernbeta.world.biome.provider.BetaBiomeProvider;
import mod.bespectacled.modernbeta.world.biome.provider.PEBiomeProvider;
import mod.bespectacled.modernbeta.world.biome.provider.SingleBiomeProvider;
import mod.bespectacled.modernbeta.world.biome.provider.settings.BiomeProviderSettings;
import mod.bespectacled.modernbeta.world.cavebiome.provider.NoCaveBiomeProvider;
import mod.bespectacled.modernbeta.world.cavebiome.provider.SingleCaveBiomeProvider;
import mod.bespectacled.modernbeta.world.cavebiome.provider.VoronoiCaveBiomeProvider;
import mod.bespectacled.modernbeta.world.cavebiome.provider.settings.CaveBiomeProviderSettings;
import mod.bespectacled.modernbeta.world.gen.provider.AlphaChunkProvider;
import mod.bespectacled.modernbeta.world.gen.provider.BetaChunkProvider;
import mod.bespectacled.modernbeta.world.gen.provider.BetaIslandsChunkProvider;
import mod.bespectacled.modernbeta.world.gen.provider.Classic030ChunkProvider;
import mod.bespectacled.modernbeta.world.gen.provider.IndevChunkProvider;
import mod.bespectacled.modernbeta.world.gen.provider.Infdev227ChunkProvider;
import mod.bespectacled.modernbeta.world.gen.provider.Infdev415ChunkProvider;
import mod.bespectacled.modernbeta.world.gen.provider.Infdev420ChunkProvider;
import mod.bespectacled.modernbeta.world.gen.provider.Infdev611ChunkProvider;
import mod.bespectacled.modernbeta.world.gen.provider.PEChunkProvider;
import mod.bespectacled.modernbeta.world.gen.provider.SkylandsChunkProvider;
import mod.bespectacled.modernbeta.world.gen.provider.settings.ChunkProviderSettings;

/*
 * Registration of built-in providers for various things.
 *  
 */
public class ModernBetaBuiltInProviders {
    
    // Register default chunk providers
    public static void registerChunkProviders() {
        Registries.CHUNK.register(ModernBetaBuiltInTypes.DEFAULT_ID, BetaChunkProvider::new);
        Registries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.BETA.name, BetaChunkProvider::new);
        Registries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.SKYLANDS.name, SkylandsChunkProvider::new);
        Registries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.ALPHA.name, AlphaChunkProvider::new);
        Registries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.INFDEV_611.name, Infdev611ChunkProvider::new);
        Registries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.INFDEV_420.name, Infdev420ChunkProvider::new);
        Registries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.INFDEV_415.name, Infdev415ChunkProvider::new);
        Registries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.INFDEV_227.name, Infdev227ChunkProvider::new);
        Registries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.INDEV.name, IndevChunkProvider::new);
        Registries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.CLASSIC_0_30.name, Classic030ChunkProvider::new);
        Registries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.BETA_ISLANDS.name, BetaIslandsChunkProvider::new);
        Registries.CHUNK.register(ModernBetaBuiltInTypes.Chunk.PE.name, PEChunkProvider::new);
    }
    
    // Register default chunk settings
    public static void registerChunkSettings() {
        Registries.CHUNK_SETTINGS.register(ModernBetaBuiltInTypes.DEFAULT_ID, () -> new ImmutableSettings());
        Registries.CHUNK_SETTINGS.register(ModernBetaBuiltInTypes.Chunk.BETA.name, ChunkProviderSettings::createSettingsBeta);
        Registries.CHUNK_SETTINGS.register(ModernBetaBuiltInTypes.Chunk.SKYLANDS.name, ChunkProviderSettings::createSettingsSkylands);
        Registries.CHUNK_SETTINGS.register(ModernBetaBuiltInTypes.Chunk.ALPHA.name, ChunkProviderSettings::createSettingsAlpha);
        Registries.CHUNK_SETTINGS.register(ModernBetaBuiltInTypes.Chunk.INFDEV_611.name, ChunkProviderSettings::createSettingsInfdev611);
        Registries.CHUNK_SETTINGS.register(ModernBetaBuiltInTypes.Chunk.INFDEV_420.name, ChunkProviderSettings::createSettingsInfdev420);
        Registries.CHUNK_SETTINGS.register(ModernBetaBuiltInTypes.Chunk.INFDEV_415.name, ChunkProviderSettings::createSettingsInfdev415);
        Registries.CHUNK_SETTINGS.register(ModernBetaBuiltInTypes.Chunk.INFDEV_227.name, ChunkProviderSettings::createSettingsInfdev227);
        Registries.CHUNK_SETTINGS.register(ModernBetaBuiltInTypes.Chunk.INDEV.name, ChunkProviderSettings::createSettingsIndev);
        Registries.CHUNK_SETTINGS.register(ModernBetaBuiltInTypes.Chunk.CLASSIC_0_30.name, ChunkProviderSettings::createSettingsClassic030);
        Registries.CHUNK_SETTINGS.register(ModernBetaBuiltInTypes.Chunk.BETA_ISLANDS.name, ChunkProviderSettings::createSettingsIslands);
        Registries.CHUNK_SETTINGS.register(ModernBetaBuiltInTypes.Chunk.PE.name, ChunkProviderSettings::createSettingsPE);
    }
    
    // Register default biome providers
    public static void registerBiomeProviders() {
        Registries.BIOME.register(ModernBetaBuiltInTypes.DEFAULT_ID, BetaBiomeProvider::new);
        Registries.BIOME.register(ModernBetaBuiltInTypes.Biome.BETA.name, BetaBiomeProvider::new);
        Registries.BIOME.register(ModernBetaBuiltInTypes.Biome.SINGLE.name, SingleBiomeProvider::new);
        Registries.BIOME.register(ModernBetaBuiltInTypes.Biome.PE.name, PEBiomeProvider::new);
    }
    
    // Register default biome settings
    public static void registerBiomeSettings() {
        Registries.BIOME_SETTINGS.register(ModernBetaBuiltInTypes.DEFAULT_ID, () -> new ImmutableSettings());
        Registries.BIOME_SETTINGS.register(ModernBetaBuiltInTypes.Biome.BETA.name, BiomeProviderSettings::createSettingsBeta);
        Registries.BIOME_SETTINGS.register(ModernBetaBuiltInTypes.Biome.SINGLE.name, BiomeProviderSettings::createSettingsSingle);
        Registries.BIOME_SETTINGS.register(ModernBetaBuiltInTypes.Biome.PE.name, BiomeProviderSettings::createSettingsPE);
    }
    
    // Register default cave biome providers
    public static void registerCaveBiomeProvider() {
        Registries.CAVE_BIOME.register(ModernBetaBuiltInTypes.DEFAULT_ID, NoCaveBiomeProvider::new);
        Registries.CAVE_BIOME.register(ModernBetaBuiltInTypes.CaveBiome.NONE.name, NoCaveBiomeProvider::new);
        Registries.CAVE_BIOME.register(ModernBetaBuiltInTypes.CaveBiome.SINGLE.name, SingleCaveBiomeProvider::new);
        Registries.CAVE_BIOME.register(ModernBetaBuiltInTypes.CaveBiome.VORONOI.name, VoronoiCaveBiomeProvider::new);
    }
    
    // Registry default cave biome settings
    public static void registerCaveBiomeSettings() {
        Registries.CAVE_BIOME_SETTINGS.register(ModernBetaBuiltInTypes.DEFAULT_ID, () -> new ImmutableSettings());
        Registries.CAVE_BIOME_SETTINGS.register(ModernBetaBuiltInTypes.CaveBiome.NONE.name, CaveBiomeProviderSettings::createSettingsNone);
        Registries.CAVE_BIOME_SETTINGS.register(ModernBetaBuiltInTypes.CaveBiome.SINGLE.name, CaveBiomeProviderSettings::createSettingsSingle);
        Registries.CAVE_BIOME_SETTINGS.register(ModernBetaBuiltInTypes.CaveBiome.VORONOI.name, CaveBiomeProviderSettings::createSettingsVoronoi);
    }
    
    public static void registerNoisePostProcessors() {
        Registries.NOISE_POST_PROCESSORS.register(ModernBetaBuiltInTypes.NoisePostProcessor.NONE.name, NoisePostProcessor.DEFAULT);
    }
}
