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
        ChunkProviderRegistry.register(BuiltInChunkType.BETA.name, BetaChunkProvider::new);
        ChunkProviderRegistry.register(BuiltInChunkType.SKYLANDS.name, SkylandsChunkProvider::new);
        ChunkProviderRegistry.register(BuiltInChunkType.ALPHA.name, AlphaChunkProvider::new);
        ChunkProviderRegistry.register(BuiltInChunkType.INFDEV_415.name, Infdev415ChunkProvider::new);
        ChunkProviderRegistry.register(BuiltInChunkType.INFDEV_227.name, Infdev227ChunkProvider::new);
        ChunkProviderRegistry.register(BuiltInChunkType.INDEV.name, IndevChunkProvider::new);
        ChunkProviderRegistry.register(BuiltInChunkType.BETA_ISLANDS.name, BetaIslandsChunkProvider::new);
    }
    
    // Register default chunk settings
    public static void registerChunkProviderSettings() {
        ChunkProviderSettingsRegistry.register(BuiltInChunkSettingsType.BETA.name, ChunkProviderSettings::createSettingsBeta);
        ChunkProviderSettingsRegistry.register(BuiltInChunkSettingsType.SKYLANDS.name, ChunkProviderSettings::createSettingsSkylands);
        ChunkProviderSettingsRegistry.register(BuiltInChunkSettingsType.ALPHA.name, ChunkProviderSettings::createSettingsAlpha);
        ChunkProviderSettingsRegistry.register(BuiltInChunkSettingsType.INFDEV_415.name, ChunkProviderSettings::createSettingsInfdev415);
        ChunkProviderSettingsRegistry.register(BuiltInChunkSettingsType.INFDEV_227.name, ChunkProviderSettings::createSettingsInfdev227);
        ChunkProviderSettingsRegistry.register(BuiltInChunkSettingsType.INDEV.name, ChunkProviderSettings::createSettingsIndev);
        ChunkProviderSettingsRegistry.register(BuiltInChunkSettingsType.BETA_ISLANDS.name, ChunkProviderSettings::createSettingsBetaIslands);
    }
    
    // Register default biome providers
    public static void registerBiomeProviders() {
        BiomeProviderRegistry.register(BuiltInBiomeType.BETA.name, BetaBiomeProvider::new);
        BiomeProviderRegistry.register(BuiltInBiomeType.SINGLE.name, SingleBiomeProvider::new);
        BiomeProviderRegistry.register(BuiltInBiomeType.VANILLA.name, VanillaBiomeProvider::new);
        
        // Register legacy biome providers
        BiomeProviderRegistry.register(BuiltInBiomeType.CLASSIC.name, SingleBiomeProvider::new);
        BiomeProviderRegistry.register(BuiltInBiomeType.WINTER.name, SingleBiomeProvider::new);
        BiomeProviderRegistry.register(BuiltInBiomeType.SKY.name, SingleBiomeProvider::new);
        BiomeProviderRegistry.register(BuiltInBiomeType.PLUS.name, SingleBiomeProvider::new);
    }
    
    // Register default cave biome providers
    public static void registerCaveBiomeProvider() {
        CaveBiomeProviderRegistry.register(BuiltInCaveBiomeType.VANILLA.name, VanillaCaveBiomeProvider::new);
        CaveBiomeProviderRegistry.register(BuiltInCaveBiomeType.NONE.name, NoCaveBiomeProvider::new);
    }
    
    // Register default screen providers
    public static void registerWorldScreenProviders() {
        WorldScreenProviderRegistry.register(BuiltInWorldScreenType.INF.name, InfWorldScreenProvider::new);
        WorldScreenProviderRegistry.register(BuiltInWorldScreenType.INFDEV_OLD.name, InfdevOldWorldScreenProvider::new);
        WorldScreenProviderRegistry.register(BuiltInWorldScreenType.INDEV.name, IndevWorldScreenProvider::new);
        WorldScreenProviderRegistry.register(BuiltInWorldScreenType.SKYLANDS.name, SkylandsWorldScreenProvider::new);
        WorldScreenProviderRegistry.register(BuiltInWorldScreenType.ISLAND.name, IslandWorldScreenProvider::new);
    }
    
    // Register default settings screen actions (Note: Match identifiers with biome ids!)
    public static void registerBiomeScreenProviders() {
        BiomeScreenProviderRegistry.register(BuiltInBiomeType.BETA.name, BetaBiomeScreenProvider::create);
        BiomeScreenProviderRegistry.register(BuiltInBiomeType.SINGLE.name, SingleBiomeScreenProvider::create);
        BiomeScreenProviderRegistry.register(BuiltInBiomeType.VANILLA.name, VanillaBiomeScreenProvider::create);
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
