package com.bespectacled.modernbeta;

import com.bespectacled.modernbeta.api.registry.*;
import com.bespectacled.modernbeta.gui.screen.biome.*;
import com.bespectacled.modernbeta.gui.screen.world.*;
import com.bespectacled.modernbeta.world.BuiltInWorldProviders;
import com.bespectacled.modernbeta.world.biome.provider.*;
import com.bespectacled.modernbeta.world.biome.provider.settings.BiomeProviderSettings;
import com.bespectacled.modernbeta.world.cavebiome.provider.*;
import com.bespectacled.modernbeta.world.gen.provider.*;
import com.bespectacled.modernbeta.world.gen.provider.settings.*;

/*
 * Registration of built-in providers for various things.
 * 
 * For new chunk generators, generally the following should be registered:
 *  - Chunk Provider, Chunk Provider Settings, Chunk Generator Settings (register with MC registry, technically optional), and World Provider.
 *  
 * For new biome providers, generally the following should be registered:
 *  - Biome Provider or Cave Biome Provider, Biome Screen Press Action (optional, for customizing biome provider).
 *  
 * A screen provider should be registered if you want to add additional buttons for new settings for a world type.
 *  
 */
public class ModernBetaDefaultProviders {
    // Register default chunk providers
    public static void registerChunkProviders() {
        ProviderRegistries.CHUNK.register(BuiltInTypes.Chunk.BETA.name, BetaChunkProvider::new);
        ProviderRegistries.CHUNK.register(BuiltInTypes.Chunk.SKYLANDS.name, SkylandsChunkProvider::new);
        ProviderRegistries.CHUNK.register(BuiltInTypes.Chunk.ALPHA.name, AlphaChunkProvider::new);
        ProviderRegistries.CHUNK.register(BuiltInTypes.Chunk.INFDEV_415.name, Infdev415ChunkProvider::new);
        ProviderRegistries.CHUNK.register(BuiltInTypes.Chunk.INFDEV_227.name, Infdev227ChunkProvider::new);
        ProviderRegistries.CHUNK.register(BuiltInTypes.Chunk.INDEV.name, IndevChunkProvider::new);
        ProviderRegistries.CHUNK.register(BuiltInTypes.Chunk.BETA_ISLANDS.name, BetaIslandsChunkProvider::new);
    }
    
    // Register default chunk settings
    public static void registerChunkProviderSettings() {
        ProviderRegistries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.BETA.name, ChunkProviderSettings::createSettingsBeta);
        ProviderRegistries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.SKYLANDS.name, ChunkProviderSettings::createSettingsSkylands);
        ProviderRegistries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.ALPHA.name, ChunkProviderSettings::createSettingsAlpha);
        ProviderRegistries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.INFDEV_415.name, ChunkProviderSettings::createSettingsInfdev415);
        ProviderRegistries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.INFDEV_227.name, ChunkProviderSettings::createSettingsInfdev227);
        ProviderRegistries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.INDEV.name, ChunkProviderSettings::createSettingsIndev);
        ProviderRegistries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.BETA_ISLANDS.name, ChunkProviderSettings::createSettingsBetaIslands);
    }
    
    // Register default biome providers
    public static void registerBiomeProviders() {
        ProviderRegistries.BIOME.register(BuiltInTypes.Biome.BETA.name, BetaBiomeProvider::new);
        ProviderRegistries.BIOME.register(BuiltInTypes.Biome.SINGLE.name, SingleBiomeProvider::new);
        ProviderRegistries.BIOME.register(BuiltInTypes.Biome.VANILLA.name, VanillaBiomeProvider::new);
    }
    
    // Register default biome settings
    public static void registerBiomeProviderSettings() {
        ProviderRegistries.BIOME_SETTINGS.register(BuiltInTypes.Biome.BETA.name, BiomeProviderSettings::createBiomeSettingsBeta);
        ProviderRegistries.BIOME_SETTINGS.register(BuiltInTypes.Biome.SINGLE.name, BiomeProviderSettings::createBiomeSettingsSingle);
        ProviderRegistries.BIOME_SETTINGS.register(BuiltInTypes.Biome.VANILLA.name, BiomeProviderSettings::createBiomeSettingsVanilla);
    }
    
    // Register default cave biome providers
    public static void registerCaveBiomeProvider() {
        ProviderRegistries.CAVE_BIOME.register(BuiltInTypes.CaveBiome.VANILLA.name, VanillaCaveBiomeProvider::new);
        ProviderRegistries.CAVE_BIOME.register(BuiltInTypes.CaveBiome.NONE.name, NoCaveBiomeProvider::new);
        ProviderRegistries.CAVE_BIOME.register(BuiltInTypes.CaveBiome.SINGLE.name, SingleCaveBiomeProvider::new);
    }
    
    // Register default screen providers
    public static void registerWorldScreenProviders() {
        ProviderRegistries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.BASE.name, BaseWorldScreenProvider::new);
        ProviderRegistries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.INF.name, InfWorldScreenProvider::new);
        ProviderRegistries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.INFDEV_227.name, Infdev227WorldScreenProvider::new);
        ProviderRegistries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.INDEV.name, IndevWorldScreenProvider::new);
        ProviderRegistries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.ISLAND.name, IslandWorldScreenProvider::new);
    }
    
    // Register default settings screen actions (Note: Match identifiers with biome ids!)
    public static void registerBiomeScreenProviders() {
        ProviderRegistries.BIOME_SCREEN.register(BuiltInTypes.Biome.BETA.name, BetaBiomeScreenProvider::create);
        ProviderRegistries.BIOME_SCREEN.register(BuiltInTypes.Biome.SINGLE.name, SingleBiomeScreenProvider::create);
        ProviderRegistries.BIOME_SCREEN.register(BuiltInTypes.Biome.VANILLA.name, VanillaBiomeScreenProvider::create);
    }
    
    // Register default world providers
    public static void registerWorldProviders() {
        ProviderRegistries.WORLD.register(BuiltInTypes.Chunk.BETA.name, BuiltInWorldProviders.BETA);
        ProviderRegistries.WORLD.register(BuiltInTypes.Chunk.SKYLANDS.name, BuiltInWorldProviders.SKYLANDS);
        ProviderRegistries.WORLD.register(BuiltInTypes.Chunk.ALPHA.name, BuiltInWorldProviders.ALPHA);
        ProviderRegistries.WORLD.register(BuiltInTypes.Chunk.INFDEV_415.name, BuiltInWorldProviders.INFDEV_415);
        ProviderRegistries.WORLD.register(BuiltInTypes.Chunk.INFDEV_227.name, BuiltInWorldProviders.INFDEV_227);
        ProviderRegistries.WORLD.register(BuiltInTypes.Chunk.INDEV.name, BuiltInWorldProviders.INDEV);
        ProviderRegistries.WORLD.register(BuiltInTypes.Chunk.BETA_ISLANDS.name, BuiltInWorldProviders.BETA_ISLANDS);
    }
}
