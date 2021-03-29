package com.bespectacled.modernbeta;

import com.bespectacled.modernbeta.api.BiomeProviderType;
import com.bespectacled.modernbeta.api.ChunkProviderType;
import com.bespectacled.modernbeta.api.ScreenProviderType;
import com.bespectacled.modernbeta.api.WorldProvider;
import com.bespectacled.modernbeta.api.WorldProviderType;
import com.bespectacled.modernbeta.gen.provider.*;
import com.bespectacled.modernbeta.gui.provider.*;
import com.bespectacled.modernbeta.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.biome.classic.ClassicBiomes;
import com.bespectacled.modernbeta.biome.indev.IndevBiomes;
import com.bespectacled.modernbeta.biome.provider.*;

public class ModernBetaDefaultProviders {
    private static final String BETA = "beta";
    private static final String SKYLANDS = "skylands";
    private static final String ALPHA = "alpha";
    private static final String INFDEV_415 = "infdev_415";
    private static final String INFDEV_227 = "infdev_227";
    private static final String INDEV = "indev";
    
    private static final String NONE = "none";
    private static final String SINGLE = "single";
    private static final String VANILLA = "vanilla";
    
    // Legacy biome provider types
    private static final String SKY = "sky";
    private static final String CLASSIC = "classic";
    private static final String WINTER = "winter";
    private static final String PLUS = "plus";
    
    private static final String INF = "inf";
    private static final String INFDEV_OLD = "infdev_old";
    
    // Register default chunk providers
    public static void registerChunkProviders() {
        ChunkProviderType.registerChunkProvider(BETA, BetaChunkProvider::new);
        ChunkProviderType.registerChunkProvider(SKYLANDS, SkylandsChunkProvider::new);
        ChunkProviderType.registerChunkProvider(ALPHA, AlphaChunkProvider::new);
        ChunkProviderType.registerChunkProvider(INFDEV_415, Infdev415ChunkProvider::new);
        ChunkProviderType.registerChunkProvider(INFDEV_227, Infdev227ChunkProvider::new);
        ChunkProviderType.registerChunkProvider(INDEV, IndevChunkProvider::new);
    }
    
    // Register default biome providers
    public static void registerBiomeProviders() {
        BiomeProviderType.registerBiomeProvider(BETA, BetaBiomeProvider::new);
        BiomeProviderType.registerBiomeProvider(SINGLE, SingleBiomeProvider::new);
        BiomeProviderType.registerBiomeProvider(VANILLA, VanillaBiomeProvider::new);
        
        // Register legacy biome providers
        BiomeProviderType.registerBiomeProvider(SKY, SingleBiomeProvider::new);
        BiomeProviderType.registerBiomeProvider(CLASSIC, SingleBiomeProvider::new);
        BiomeProviderType.registerBiomeProvider(WINTER, SingleBiomeProvider::new);
        BiomeProviderType.registerBiomeProvider(PLUS, SingleBiomeProvider::new);
    }
    
    // Register default screen providers
    public static void registerScreenProviders() {
        ScreenProviderType.registerScreenProvider(INF, InfCustomizeLevelScreen::new);
        ScreenProviderType.registerScreenProvider(INFDEV_OLD, InfdevOldCustomizeLevelScreen::new);
        ScreenProviderType.registerScreenProvider(INDEV, IndevCustomizeLevelScreen::new);
    }
    
    // Register default world providers
    public static void registerWorldProviders() {
        WorldProviderType.addWorldProvider(new WorldProvider(BETA, INF, BETA, VANILLA, BetaBiomes.FOREST_ID.toString(), true, true));
        WorldProviderType.addWorldProvider(new WorldProvider(SKYLANDS, INF, SINGLE, NONE, BetaBiomes.SKY_ID.toString(), false, true));
        WorldProviderType.addWorldProvider(new WorldProvider(ALPHA, INF, SINGLE, NONE, ClassicBiomes.ALPHA_ID.toString(), true, true));
        WorldProviderType.addWorldProvider(new WorldProvider(INFDEV_415, INF, SINGLE, NONE, ClassicBiomes.INFDEV_415_ID.toString(), true, true));
        WorldProviderType.addWorldProvider(new WorldProvider(INFDEV_227, INFDEV_OLD, SINGLE, NONE, ClassicBiomes.INFDEV_227_ID.toString(), true, false));
        WorldProviderType.addWorldProvider(new WorldProvider(INDEV, INDEV, SINGLE, NONE, IndevBiomes.INDEV_NORMAL_ID.toString(), true, false));
    }
}
