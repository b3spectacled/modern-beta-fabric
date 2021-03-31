package com.bespectacled.modernbeta;

import com.bespectacled.modernbeta.api.WorldProvider;
import com.bespectacled.modernbeta.api.WorldProviderType;
import com.bespectacled.modernbeta.api.biome.BiomeProviderType;
import com.bespectacled.modernbeta.api.biome.CaveBiomeProviderType;
import com.bespectacled.modernbeta.api.biome.BiomeProviderType.BuiltInBiomeType;
import com.bespectacled.modernbeta.api.biome.CaveBiomeProviderType.BuiltInCaveBiomeType;
import com.bespectacled.modernbeta.api.gen.ChunkProviderType;
import com.bespectacled.modernbeta.api.gen.ChunkProviderType.BuiltInChunkType;
import com.bespectacled.modernbeta.api.gui.ScreenPressActionType;
import com.bespectacled.modernbeta.api.gui.ScreenProviderType;
import com.bespectacled.modernbeta.api.gui.ScreenProviderType.BuiltInScreenType;
import com.bespectacled.modernbeta.gen.provider.*;
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
        ChunkProviderType.registerProvider(BuiltInChunkType.BETA.id, BetaChunkProvider::new);
        ChunkProviderType.registerProvider(BuiltInChunkType.SKYLANDS.id, SkylandsChunkProvider::new);
        ChunkProviderType.registerProvider(BuiltInChunkType.ALPHA.id, AlphaChunkProvider::new);
        ChunkProviderType.registerProvider(BuiltInChunkType.INFDEV_415.id, Infdev415ChunkProvider::new);
        ChunkProviderType.registerProvider(BuiltInChunkType.INFDEV_227.id, Infdev227ChunkProvider::new);
        ChunkProviderType.registerProvider(BuiltInChunkType.INDEV.id, IndevChunkProvider::new);
    }
    
    // Register default biome providers
    public static void registerBiomeProviders() {
        BiomeProviderType.registerProvider(BuiltInBiomeType.BETA.id, BetaBiomeProvider::new);
        BiomeProviderType.registerProvider(BuiltInBiomeType.SINGLE.id, SingleBiomeProvider::new);
        BiomeProviderType.registerProvider(BuiltInBiomeType.VANILLA.id, VanillaBiomeProvider::new);
        
        // Register legacy biome providers
        BiomeProviderType.registerProvider(BuiltInBiomeType.CLASSIC.id, SingleBiomeProvider::new);
        BiomeProviderType.registerProvider(BuiltInBiomeType.WINTER.id, SingleBiomeProvider::new);
        BiomeProviderType.registerProvider(BuiltInBiomeType.SKY.id, SingleBiomeProvider::new);
        BiomeProviderType.registerProvider(BuiltInBiomeType.PLUS.id, SingleBiomeProvider::new);
    }
    
    // Register default cave biome providers
    public static void registerCaveBiomeProvider() {
        CaveBiomeProviderType.registerProvider(BuiltInCaveBiomeType.VANILLA.id, VanillaCaveBiomeProvider::new);
        CaveBiomeProviderType.registerProvider(BuiltInCaveBiomeType.NONE.id, NoCaveBiomeProvider::new);
    }
    
    // Register default screen providers
    public static void registerScreenProviders() {
        ScreenProviderType.registerProvider(BuiltInScreenType.INF.id, InfCustomizeLevelScreen::new);
        ScreenProviderType.registerProvider(BuiltInScreenType.INFDEV_OLD.id, InfdevOldCustomizeLevelScreen::new);
        ScreenProviderType.registerProvider(BuiltInScreenType.INDEV.id, IndevCustomizeLevelScreen::new);
    }
    
    // Register default settings screen actions (Note: Match identifiers with biome ids!)
    public static void registerScreenPressActions() {
        ScreenPressActionType.registerProvider(BuiltInBiomeType.BETA.id, ScreenPressActions.BETA);
        ScreenPressActionType.registerProvider(BuiltInBiomeType.SINGLE.id, ScreenPressActions.SINGLE);
        ScreenPressActionType.registerProvider(BuiltInBiomeType.VANILLA.id, ScreenPressActions.VANILLA);
    }
    
    // Register default world providers
    public static void registerWorldProviders() {
        String betaSettings = ModernBeta.createId(BuiltInChunkType.BETA.id).toString();
        String skylandsSettings = ModernBeta.createId(BuiltInChunkType.SKYLANDS.id).toString();
        String alphaSettings = ModernBeta.createId(BuiltInChunkType.ALPHA.id).toString();
        String infdev415Settings = ModernBeta.createId(BuiltInChunkType.INFDEV_415.id).toString();
        String infdev227Settings = ModernBeta.createId(BuiltInChunkType.INFDEV_227.id).toString();
        String indevSettings = ModernBeta.createId(BuiltInChunkType.INDEV.id).toString();
        
        WorldProviderType.addProvider(new WorldProvider(
            BuiltInChunkType.BETA.id, 
            betaSettings, 
            BuiltInScreenType.INF.id, 
            BuiltInBiomeType.BETA.id, 
            BuiltInCaveBiomeType.VANILLA.id, 
            BetaBiomes.FOREST_ID.toString(), 
            true, 
            true
        ));
        
        WorldProviderType.addProvider(new WorldProvider(
            BuiltInChunkType.SKYLANDS.id, 
            skylandsSettings, 
            BuiltInScreenType.INF.id, 
            BuiltInBiomeType.SINGLE.id, 
            BuiltInCaveBiomeType.NONE.id, 
            BetaBiomes.SKY_ID.toString(), 
            false, 
            true
        ));
        
        WorldProviderType.addProvider(new WorldProvider(
            BuiltInChunkType.ALPHA.id, 
            alphaSettings, 
            BuiltInScreenType.INF.id, 
            BuiltInBiomeType.SINGLE.id, 
            BuiltInCaveBiomeType.NONE.id, 
            ClassicBiomes.ALPHA_ID.toString(), 
            true, 
            true
        ));
        
        WorldProviderType.addProvider(new WorldProvider(
            BuiltInChunkType.INFDEV_415.id, 
            infdev415Settings, 
            BuiltInScreenType.INF.id, 
            BuiltInBiomeType.SINGLE.id, 
            BuiltInCaveBiomeType.NONE.id, 
            ClassicBiomes.INFDEV_415_ID.toString(), 
            true, 
            true
        ));
        
        WorldProviderType.addProvider(new WorldProvider(
            BuiltInChunkType.INFDEV_227.id, 
            infdev227Settings, 
            BuiltInScreenType.INFDEV_OLD.id, 
            BuiltInBiomeType.SINGLE.id, 
            BuiltInCaveBiomeType.NONE.id, 
            ClassicBiomes.INFDEV_227_ID.toString(), 
            true, 
            false
        ));
        
        WorldProviderType.addProvider(new WorldProvider(
            BuiltInChunkType.INDEV.id, 
            indevSettings, 
            BuiltInScreenType.INDEV.id, 
            BuiltInBiomeType.SINGLE.id, 
            BuiltInCaveBiomeType.NONE.id, 
            IndevBiomes.INDEV_NORMAL_ID.toString(), 
            true, 
            false
        ));
    }
}
