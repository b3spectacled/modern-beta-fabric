package com.bespectacled.modernbeta;

import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.api.registry.Registries;
import com.bespectacled.modernbeta.client.gui.screen.biome.BetaBiomeScreen;
import com.bespectacled.modernbeta.client.gui.screen.biome.SingleBiomeScreen;
import com.bespectacled.modernbeta.client.gui.screen.biome.VanillaBiomeScreen;
import com.bespectacled.modernbeta.client.gui.screen.world.BaseWorldScreen;
import com.bespectacled.modernbeta.client.gui.screen.world.IndevWorldScreen;
import com.bespectacled.modernbeta.client.gui.screen.world.InfWorldScreen;
import com.bespectacled.modernbeta.client.gui.screen.world.Infdev227WorldScreen;
import com.bespectacled.modernbeta.client.gui.screen.world.IslandWorldScreen;
import com.bespectacled.modernbeta.world.BuiltInWorldProviders;
import com.bespectacled.modernbeta.world.biome.provider.BetaBiomeProvider;
import com.bespectacled.modernbeta.world.biome.provider.PEBiomeProvider;
import com.bespectacled.modernbeta.world.biome.provider.SingleBiomeProvider;
import com.bespectacled.modernbeta.world.biome.provider.VanillaBiomeProvider;
import com.bespectacled.modernbeta.world.biome.provider.settings.BiomeProviderSettings;
import com.bespectacled.modernbeta.world.cavebiome.provider.BetaCaveBiomeProvider;
import com.bespectacled.modernbeta.world.cavebiome.provider.NoCaveBiomeProvider;
import com.bespectacled.modernbeta.world.cavebiome.provider.SingleCaveBiomeProvider;
import com.bespectacled.modernbeta.world.cavebiome.provider.VanillaCaveBiomeProvider;
import com.bespectacled.modernbeta.world.gen.provider.AlphaChunkProvider;
import com.bespectacled.modernbeta.world.gen.provider.BetaChunkProvider;
import com.bespectacled.modernbeta.world.gen.provider.BetaIslandsChunkProvider;
import com.bespectacled.modernbeta.world.gen.provider.IndevChunkProvider;
import com.bespectacled.modernbeta.world.gen.provider.Infdev227ChunkProvider;
import com.bespectacled.modernbeta.world.gen.provider.Infdev415ChunkProvider;
import com.bespectacled.modernbeta.world.gen.provider.Infdev611ChunkProvider;
import com.bespectacled.modernbeta.world.gen.provider.PEChunkProvider;
import com.bespectacled.modernbeta.world.gen.provider.SkylandsChunkProvider;
import com.bespectacled.modernbeta.world.gen.provider.settings.ChunkProviderSettings;

import net.minecraft.nbt.NbtCompound;

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
        Registries.CHUNK.register(BuiltInTypes.Chunk.PE.name, PEChunkProvider::new);
    }
    
    // Register default chunk settings
    public static void registerChunkSettings() {
        Registries.CHUNK_SETTINGS.register(BuiltInTypes.DEFAULT_ID, () -> new NbtCompound());
        Registries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.BETA.name, ChunkProviderSettings::createSettingsBeta);
        Registries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.SKYLANDS.name, ChunkProviderSettings::createSettingsSkylands);
        Registries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.ALPHA.name, ChunkProviderSettings::createSettingsAlpha);
        Registries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.INFDEV_611.name, ChunkProviderSettings::createSettingsInfdev611);
        Registries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.INFDEV_415.name, ChunkProviderSettings::createSettingsInfdev415);
        Registries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.INFDEV_227.name, ChunkProviderSettings::createSettingsInfdev227);
        Registries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.INDEV.name, ChunkProviderSettings::createSettingsIndev);
        Registries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.BETA_ISLANDS.name, ChunkProviderSettings::createSettingsIslands);
        Registries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.PE.name, ChunkProviderSettings::createSettingsPE);
    }
    
    // Register default biome providers
    public static void registerBiomeProviders() {
        Registries.BIOME.register(BuiltInTypes.DEFAULT_ID, BetaBiomeProvider::new);
        Registries.BIOME.register(BuiltInTypes.Biome.BETA.name, BetaBiomeProvider::new);
        Registries.BIOME.register(BuiltInTypes.Biome.SINGLE.name, SingleBiomeProvider::new);
        Registries.BIOME.register(BuiltInTypes.Biome.VANILLA.name, VanillaBiomeProvider::new);
        Registries.BIOME.register(BuiltInTypes.Biome.PE.name, PEBiomeProvider::new);
    }
    
    // Register default biome settings
    public static void registerBiomeSettings() {
        Registries.BIOME_SETTINGS.register(BuiltInTypes.DEFAULT_ID, () -> new NbtCompound());
        Registries.BIOME_SETTINGS.register(BuiltInTypes.Biome.BETA.name, BiomeProviderSettings::createSettingsBeta);
        Registries.BIOME_SETTINGS.register(BuiltInTypes.Biome.SINGLE.name, BiomeProviderSettings::createSettingsSingle);
        Registries.BIOME_SETTINGS.register(BuiltInTypes.Biome.VANILLA.name, BiomeProviderSettings::createSettingsVanilla);
        Registries.BIOME_SETTINGS.register(BuiltInTypes.Biome.PE.name, BiomeProviderSettings::createSettingsPE);
    }
    
    // Register default cave biome providers
    public static void registerCaveBiomeProvider() {
        Registries.CAVE_BIOME.register(BuiltInTypes.DEFAULT_ID, VanillaCaveBiomeProvider::new);
        Registries.CAVE_BIOME.register(BuiltInTypes.CaveBiome.NONE.name, NoCaveBiomeProvider::new);
        Registries.CAVE_BIOME.register(BuiltInTypes.CaveBiome.BETA.name, BetaCaveBiomeProvider::new);
        Registries.CAVE_BIOME.register(BuiltInTypes.CaveBiome.SINGLE.name, SingleCaveBiomeProvider::new);
        Registries.CAVE_BIOME.register(BuiltInTypes.CaveBiome.VANILLA.name, VanillaCaveBiomeProvider::new);
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
        Registries.BIOME_SCREEN.register(BuiltInTypes.Biome.PE.name, BetaBiomeScreen::create);
    }
    
    // Register default world providers
    public static void registerWorldProviders() {
        Registries.WORLD.register(BuiltInTypes.DEFAULT_ID, BuiltInWorldProviders.DEFAULT);
        Registries.WORLD.register(BuiltInTypes.Chunk.BETA.name, BuiltInWorldProviders.BETA);
        Registries.WORLD.register(BuiltInTypes.Chunk.SKYLANDS.name, BuiltInWorldProviders.SKYLANDS);
        Registries.WORLD.register(BuiltInTypes.Chunk.ALPHA.name, BuiltInWorldProviders.ALPHA);
        Registries.WORLD.register(BuiltInTypes.Chunk.INFDEV_611.name, BuiltInWorldProviders.INFDEV_611);
        Registries.WORLD.register(BuiltInTypes.Chunk.INFDEV_415.name, BuiltInWorldProviders.INFDEV_415);
        Registries.WORLD.register(BuiltInTypes.Chunk.INFDEV_227.name, BuiltInWorldProviders.INFDEV_227);
        Registries.WORLD.register(BuiltInTypes.Chunk.INDEV.name, BuiltInWorldProviders.INDEV);
        Registries.WORLD.register(BuiltInTypes.Chunk.BETA_ISLANDS.name, BuiltInWorldProviders.BETA_ISLANDS);
        Registries.WORLD.register(BuiltInTypes.Chunk.PE.name, BuiltInWorldProviders.PE);
    }
}
