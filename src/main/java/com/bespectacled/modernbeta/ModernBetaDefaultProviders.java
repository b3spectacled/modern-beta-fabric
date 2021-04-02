package com.bespectacled.modernbeta;

import com.bespectacled.modernbeta.api.WorldProvider;
import com.bespectacled.modernbeta.api.registry.BiomeProviderRegistry;
import com.bespectacled.modernbeta.api.registry.CaveBiomeProviderRegistry;
import com.bespectacled.modernbeta.api.registry.ChunkProviderRegistry;
import com.bespectacled.modernbeta.api.registry.ScreenPressActionRegistry;
import com.bespectacled.modernbeta.api.registry.ScreenProviderRegistry;
import com.bespectacled.modernbeta.api.registry.WorldProviderRegistry;
import com.bespectacled.modernbeta.api.registry.BiomeProviderRegistry.BuiltInBiomeType;
import com.bespectacled.modernbeta.api.registry.CaveBiomeProviderRegistry.BuiltInCaveBiomeType;
import com.bespectacled.modernbeta.api.registry.ChunkProviderRegistry.BuiltInChunkType;
import com.bespectacled.modernbeta.api.registry.ChunkProviderSettingsRegistry.BuiltInChunkSettingsType;
import com.bespectacled.modernbeta.api.registry.ChunkProviderSettingsRegistry;
import com.bespectacled.modernbeta.api.registry.ScreenProviderRegistry.BuiltInScreenType;
import com.bespectacled.modernbeta.gen.provider.*;
import com.bespectacled.modernbeta.gen.provider.settings.*;
import com.bespectacled.modernbeta.gui.ScreenPressActions;
import com.bespectacled.modernbeta.gui.provider.*;
import com.bespectacled.modernbeta.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.biome.classic.ClassicBiomes;
import com.bespectacled.modernbeta.biome.indev.IndevBiomes;
import com.bespectacled.modernbeta.biome.provider.*;
import com.bespectacled.modernbeta.cavebiome.provider.*;

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
        String betaSettings = ModernBeta.createId(BuiltInChunkType.BETA.id).toString();
        String skylandsSettings = ModernBeta.createId(BuiltInChunkType.SKYLANDS.id).toString();
        String alphaSettings = ModernBeta.createId(BuiltInChunkType.ALPHA.id).toString();
        String infdev415Settings = ModernBeta.createId(BuiltInChunkType.INFDEV_415.id).toString();
        String infdev227Settings = ModernBeta.createId(BuiltInChunkType.INFDEV_227.id).toString();
        String indevSettings = ModernBeta.createId(BuiltInChunkType.INDEV.id).toString();
        
        WorldProviderRegistry.add(new WorldProvider(
            BuiltInChunkType.BETA.id, 
            BuiltInChunkSettingsType.BETA.id,
            betaSettings, 
            BuiltInScreenType.INF.id,
            BuiltInBiomeType.BETA.id, 
            BuiltInCaveBiomeType.VANILLA.id, 
            BetaBiomes.FOREST_ID.toString(),
            true
        ));
        
        WorldProviderRegistry.add(new WorldProvider(
            BuiltInChunkType.SKYLANDS.id, 
            BuiltInChunkSettingsType.SKYLANDS.id,
            skylandsSettings, 
            BuiltInScreenType.SKYLANDS.id, 
            BuiltInBiomeType.SINGLE.id, 
            BuiltInCaveBiomeType.NONE.id, 
            BetaBiomes.SKY_ID.toString(),
            true
        ));
        
        WorldProviderRegistry.add(new WorldProvider(
            BuiltInChunkType.ALPHA.id,
            BuiltInChunkSettingsType.ALPHA.id,
            alphaSettings, 
            BuiltInScreenType.INF.id, 
            BuiltInBiomeType.SINGLE.id, 
            BuiltInCaveBiomeType.NONE.id, 
            ClassicBiomes.ALPHA_ID.toString(),
            true
        ));
        
        WorldProviderRegistry.add(new WorldProvider(
            BuiltInChunkType.INFDEV_415.id,
            BuiltInChunkSettingsType.INFDEV_415.id,
            infdev415Settings, 
            BuiltInScreenType.INF.id, 
            BuiltInBiomeType.SINGLE.id, 
            BuiltInCaveBiomeType.NONE.id, 
            ClassicBiomes.INFDEV_415_ID.toString(),
            true
        ));
        
        WorldProviderRegistry.add(new WorldProvider(
            BuiltInChunkType.INFDEV_227.id,
            BuiltInChunkSettingsType.INFDEV_227.id,
            infdev227Settings, 
            BuiltInScreenType.INFDEV_OLD.id, 
            BuiltInBiomeType.SINGLE.id, 
            BuiltInCaveBiomeType.NONE.id, 
            ClassicBiomes.INFDEV_227_ID.toString(),
            false
        ));
        
        WorldProviderRegistry.add(new WorldProvider(
            BuiltInChunkType.INDEV.id,
            BuiltInChunkSettingsType.INDEV.id,
            indevSettings, 
            BuiltInScreenType.INDEV.id, 
            BuiltInBiomeType.SINGLE.id, 
            BuiltInCaveBiomeType.NONE.id, 
            IndevBiomes.INDEV_NORMAL_ID.toString(),
            false
        ));
    }
}
