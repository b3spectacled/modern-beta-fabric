package com.bespectacled.modernbeta;

import com.bespectacled.modernbeta.api.registry.*;
import com.bespectacled.modernbeta.api.registry.BiomeProviderRegistry.BuiltInBiomeType;
import com.bespectacled.modernbeta.api.registry.CaveBiomeProviderRegistry.BuiltInCaveBiomeType;
import com.bespectacled.modernbeta.api.registry.ChunkProviderRegistry.BuiltInChunkType;
import com.bespectacled.modernbeta.api.registry.ChunkProviderSettingsRegistry.BuiltInChunkSettingsType;
import com.bespectacled.modernbeta.api.registry.ScreenProviderRegistry.BuiltInScreenType;
import com.bespectacled.modernbeta.gui.ScreenPressActions;
import com.bespectacled.modernbeta.gui.provider.*;
import com.bespectacled.modernbeta.world.BuiltInWorldProviders;
import com.bespectacled.modernbeta.world.biome.provider.*;
import com.bespectacled.modernbeta.world.cavebiome.provider.*;
import com.bespectacled.modernbeta.world.gen.provider.*;
import com.bespectacled.modernbeta.world.gen.provider.settings.*;

public class ModernBetaDefaultProviders {
    // Register default chunk providers
    public static void registerChunkProviders() {
        ChunkProviderRegistry.register(BuiltInChunkType.BETA.id, BetaChunkProvider::new);
        ChunkProviderRegistry.register(BuiltInChunkType.SKYLANDS.id, SkylandsChunkProvider::new);
        ChunkProviderRegistry.register(BuiltInChunkType.ALPHA.id, AlphaChunkProvider::new);
        ChunkProviderRegistry.register(BuiltInChunkType.INFDEV_415.id, Infdev415ChunkProvider::new);
        ChunkProviderRegistry.register(BuiltInChunkType.INFDEV_227.id, Infdev227ChunkProvider::new);
        ChunkProviderRegistry.register(BuiltInChunkType.INDEV.id, IndevChunkProvider::new);
    }
    
    // Register default chunk settings
    public static void registerChunkProviderSettings() {
        ChunkProviderSettingsRegistry.register(BuiltInChunkSettingsType.BETA.id, ChunkProviderSettings::createSettingsBeta);
        ChunkProviderSettingsRegistry.register(BuiltInChunkSettingsType.SKYLANDS.id, ChunkProviderSettings::createSettingsSkylands);
        ChunkProviderSettingsRegistry.register(BuiltInChunkSettingsType.ALPHA.id, ChunkProviderSettings::createSettingsAlpha);
        ChunkProviderSettingsRegistry.register(BuiltInChunkSettingsType.INFDEV_415.id, ChunkProviderSettings::createSettingsInfdev415);
        ChunkProviderSettingsRegistry.register(BuiltInChunkSettingsType.INFDEV_227.id, ChunkProviderSettings::createSettingsInfdev227);
        ChunkProviderSettingsRegistry.register(BuiltInChunkSettingsType.INDEV.id, ChunkProviderSettings::createSettingsIndev);
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
    public static void registerScreenProviders() {
        ScreenProviderRegistry.register(BuiltInScreenType.INF.id, InfLevelScreenProvider::new);
        ScreenProviderRegistry.register(BuiltInScreenType.INFDEV_OLD.id, InfdevOldLevelScreenProvider::new);
        ScreenProviderRegistry.register(BuiltInScreenType.INDEV.id, IndevLevelScreenProvider::new);
        ScreenProviderRegistry.register(BuiltInScreenType.SKYLANDS.id, SkylandsLevelScreenProvider::new);
    }
    
    // Register default settings screen actions (Note: Match identifiers with biome ids!)
    public static void registerScreenPressActions() {
        ScreenPressActionRegistry.register(BuiltInBiomeType.BETA.id, ScreenPressActions.BETA);
        ScreenPressActionRegistry.register(BuiltInBiomeType.SINGLE.id, ScreenPressActions.SINGLE);
        ScreenPressActionRegistry.register(BuiltInBiomeType.VANILLA.id, ScreenPressActions.VANILLA);
    }
    
    // Register default world providers
    public static void registerWorldProviders() {
        WorldProviderRegistry.add(BuiltInWorldProviders.BETA);
        WorldProviderRegistry.add(BuiltInWorldProviders.SKYLANDS);
        WorldProviderRegistry.add(BuiltInWorldProviders.ALPHA);
        WorldProviderRegistry.add(BuiltInWorldProviders.INFDEV_415);
        WorldProviderRegistry.add(BuiltInWorldProviders.INFDEV_227);
        WorldProviderRegistry.add(BuiltInWorldProviders.INDEV);
    }
}
