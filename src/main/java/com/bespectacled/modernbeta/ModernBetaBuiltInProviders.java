package com.bespectacled.modernbeta;

import com.bespectacled.modernbeta.api.registry.*;
import com.bespectacled.modernbeta.gui.screen.world.*;
import com.bespectacled.modernbeta.gui.screen.biome.*;
import com.bespectacled.modernbeta.world.BuiltInWorldProviders;
import com.bespectacled.modernbeta.world.biome.provider.*;
import com.bespectacled.modernbeta.world.gen.provider.*;

/*
 * Registration of built-in providers for various things.
 *  
 */
public class ModernBetaBuiltInProviders {
    
    // Register default chunk providers
    public static void registerChunkProviders() {
        Registries.CHUNK.register(BuiltInTypes.DEFAULT_ID, BetaChunkProvider::new);
        Registries.CHUNK.register(BuiltInTypes.Chunk.BETA.name, BetaChunkProvider::new);
        Registries.CHUNK.register(BuiltInTypes.Chunk.SKYLANDS.name, SkylandsChunkProvider::new);
        Registries.CHUNK.register(BuiltInTypes.Chunk.ALPHA.name, AlphaChunkProvider::new);
        Registries.CHUNK.register(BuiltInTypes.Chunk.INFDEV_611.name, Infdev611ChunkProvider::new);
        Registries.CHUNK.register(BuiltInTypes.Chunk.INFDEV_415.name, Infdev415ChunkProvider::new);
        Registries.CHUNK.register(BuiltInTypes.Chunk.INFDEV_227.name, Infdev227ChunkProvider::new);
        Registries.CHUNK.register(BuiltInTypes.Chunk.INDEV.name, IndevChunkProvider::new);
        Registries.CHUNK.register(BuiltInTypes.Chunk.BETA_ISLANDS.name, BetaIslandsChunkProvider::new);
    }
    
    // Register default biome providers
    public static void registerBiomeProviders() {
        Registries.BIOME.register(BuiltInTypes.DEFAULT_ID, BetaBiomeProvider::new);
        Registries.BIOME.register(BuiltInTypes.Biome.BETA.name, BetaBiomeProvider::new);
        Registries.BIOME.register(BuiltInTypes.Biome.SINGLE.name, SingleBiomeProvider::new);
        Registries.BIOME.register(BuiltInTypes.Biome.VANILLA.name, VanillaBiomeProvider::new);
    }
    
    // Register default world screens
    public static void registerWorldScreens() {
        Registries.WORLD_SCREEN.register(BuiltInTypes.DEFAULT_ID, BaseWorldScreen::new);
        Registries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.BASE.name, BaseWorldScreen::new);
        Registries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.INF.name, InfWorldScreen::new);
        Registries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.INFDEV_227.name, Infdev227WorldScreen::new);
        Registries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.INDEV.name, IndevWorldScreen::new);
        Registries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.ISLAND.name, IslandWorldScreen::new);
    }
    
    // Register default biome settings screens (Note: Match identifiers with biome ids!)
    public static void registerBiomeScreens() {
        Registries.BIOME_SCREEN.register(BuiltInTypes.DEFAULT_ID, screen -> null);
        Registries.BIOME_SCREEN.register(BuiltInTypes.Biome.BETA.name, BetaBiomeScreen::create);
        Registries.BIOME_SCREEN.register(BuiltInTypes.Biome.SINGLE.name, SingleBiomeScreen::create);
        Registries.BIOME_SCREEN.register(BuiltInTypes.Biome.VANILLA.name, VanillaBiomeScreen::create);
    }
    
    // Register default world providers
    public static void registerWorldProviders() {
        Registries.WORLD.register(BuiltInTypes.DEFAULT_ID, BuiltInWorldProviders.BETA);
        Registries.WORLD.register(BuiltInTypes.Chunk.BETA.name, BuiltInWorldProviders.BETA);
        Registries.WORLD.register(BuiltInTypes.Chunk.SKYLANDS.name, BuiltInWorldProviders.SKYLANDS);
        Registries.WORLD.register(BuiltInTypes.Chunk.ALPHA.name, BuiltInWorldProviders.ALPHA);
        Registries.WORLD.register(BuiltInTypes.Chunk.INFDEV_611.name, BuiltInWorldProviders.INFDEV_611);
        Registries.WORLD.register(BuiltInTypes.Chunk.INFDEV_415.name, BuiltInWorldProviders.INFDEV_415);
        Registries.WORLD.register(BuiltInTypes.Chunk.INFDEV_227.name, BuiltInWorldProviders.INFDEV_227);
        Registries.WORLD.register(BuiltInTypes.Chunk.INDEV.name, BuiltInWorldProviders.INDEV);
        Registries.WORLD.register(BuiltInTypes.Chunk.BETA_ISLANDS.name, BuiltInWorldProviders.BETA_ISLANDS);
    }
}
