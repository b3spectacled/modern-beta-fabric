package com.bespectacled.modernbeta;

import com.bespectacled.modernbeta.api.registry.*;
import com.bespectacled.modernbeta.api.registry.BiomeProviderRegistry.BuiltInBiomeType;
import com.bespectacled.modernbeta.api.registry.CaveBiomeProviderRegistry.BuiltInCaveBiomeType;
import com.bespectacled.modernbeta.api.registry.ChunkProviderRegistry.BuiltInChunkType;
import com.bespectacled.modernbeta.api.registry.ChunkProviderSettingsRegistry.BuiltInChunkSettingsType;
import com.bespectacled.modernbeta.api.registry.WorldScreenProviderRegistry.BuiltInWorldScreenType;
import com.bespectacled.modernbeta.gui.biome.*;
import com.bespectacled.modernbeta.gui.world.*;
import com.bespectacled.modernbeta.world.BuiltInWorldProviders;
import com.bespectacled.modernbeta.world.biome.provider.*;
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
        ChunkProviderRegistry.register(BuiltInChunkType.BETA.id, BetaChunkProvider::new);
        ChunkProviderRegistry.register(BuiltInChunkType.SKYLANDS.id, SkylandsChunkProvider::new);
        ChunkProviderRegistry.register(BuiltInChunkType.ALPHA.id, AlphaChunkProvider::new);
        ChunkProviderRegistry.register(BuiltInChunkType.INFDEV_415.id, Infdev415ChunkProvider::new);
        ChunkProviderRegistry.register(BuiltInChunkType.INFDEV_227.id, Infdev227ChunkProvider::new);
        ChunkProviderRegistry.register(BuiltInChunkType.INDEV.id, IndevChunkProvider::new);
        ChunkProviderRegistry.register(BuiltInChunkType.BETA_ISLANDS.id, BetaIslandsChunkProvider::new);
    }
    
    // Register default chunk settings
    public static void registerChunkProviderSettings() {
        ChunkProviderSettingsRegistry.register(BuiltInChunkSettingsType.BETA.id, ChunkProviderSettings::createSettingsBeta);
        ChunkProviderSettingsRegistry.register(BuiltInChunkSettingsType.SKYLANDS.id, ChunkProviderSettings::createSettingsSkylands);
        ChunkProviderSettingsRegistry.register(BuiltInChunkSettingsType.ALPHA.id, ChunkProviderSettings::createSettingsAlpha);
        ChunkProviderSettingsRegistry.register(BuiltInChunkSettingsType.INFDEV_415.id, ChunkProviderSettings::createSettingsInfdev415);
        ChunkProviderSettingsRegistry.register(BuiltInChunkSettingsType.INFDEV_227.id, ChunkProviderSettings::createSettingsInfdev227);
        ChunkProviderSettingsRegistry.register(BuiltInChunkSettingsType.INDEV.id, ChunkProviderSettings::createSettingsIndev);
        ChunkProviderSettingsRegistry.register(BuiltInChunkSettingsType.BETA_ISLANDS.id, ChunkProviderSettings::createSettingsBetaIslands);
    }
    
    // Register default biome providers
    public static void registerBiomeProviders() {
        BiomeProviderRegistry.register(BuiltInBiomeType.BETA.id, BetaBiomeProvider::new);
        BiomeProviderRegistry.register(BuiltInBiomeType.SINGLE.id, SingleBiomeProvider::new);
        BiomeProviderRegistry.register(BuiltInBiomeType.VANILLA.id, VanillaBiomeProvider::new);
        
        // Register legacy biome providers
        BiomeProviderRegistry.register(BuiltInBiomeType.CLASSIC.id, SingleBiomeProvider::new);
        BiomeProviderRegistry.register(BuiltInBiomeType.WINTER.id, SingleBiomeProvider::new);
        BiomeProviderRegistry.register(BuiltInBiomeType.SKY.id, SingleBiomeProvider::new);
        BiomeProviderRegistry.register(BuiltInBiomeType.PLUS.id, SingleBiomeProvider::new);
    }
    
    // Register default cave biome providers
    public static void registerCaveBiomeProvider() {
        CaveBiomeProviderRegistry.register(BuiltInCaveBiomeType.VANILLA.id, VanillaCaveBiomeProvider::new);
        CaveBiomeProviderRegistry.register(BuiltInCaveBiomeType.NONE.id, NoCaveBiomeProvider::new);
    }
    
    // Register default screen providers
    public static void registerWorldScreenProviders() {
        WorldScreenProviderRegistry.register(BuiltInWorldScreenType.INF.id, InfWorldScreenProvider::new);
        WorldScreenProviderRegistry.register(BuiltInWorldScreenType.INFDEV_OLD.id, InfdevOldWorldScreenProvider::new);
        WorldScreenProviderRegistry.register(BuiltInWorldScreenType.INDEV.id, IndevWorldScreenProvider::new);
        WorldScreenProviderRegistry.register(BuiltInWorldScreenType.SKYLANDS.id, SkylandsWorldScreenProvider::new);
        WorldScreenProviderRegistry.register(BuiltInWorldScreenType.ISLAND.id, IslandWorldScreenProvider::new);
    }
    
    // Register default settings screen actions (Note: Match identifiers with biome ids!)
    public static void registerBiomeScreenProviders() {
        BiomeScreenProviderRegistry.register(BuiltInBiomeType.BETA.id, BetaBiomeScreenProvider::create);
        BiomeScreenProviderRegistry.register(BuiltInBiomeType.SINGLE.id, SingleBiomeScreenProvider::create);
        BiomeScreenProviderRegistry.register(BuiltInBiomeType.VANILLA.id, VanillaBiomeScreenProvider::create);
    }
    
    // Register default world providers
    public static void registerWorldProviders() {
        WorldProviderRegistry.add(BuiltInWorldProviders.BETA);
        WorldProviderRegistry.add(BuiltInWorldProviders.SKYLANDS);
        WorldProviderRegistry.add(BuiltInWorldProviders.ALPHA);
        WorldProviderRegistry.add(BuiltInWorldProviders.INFDEV_415);
        WorldProviderRegistry.add(BuiltInWorldProviders.INFDEV_227);
        WorldProviderRegistry.add(BuiltInWorldProviders.INDEV);
        WorldProviderRegistry.add(BuiltInWorldProviders.BETA_ISLANDS);
    }
}
