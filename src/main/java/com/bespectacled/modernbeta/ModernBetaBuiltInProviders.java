package com.bespectacled.modernbeta;

import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.api.registry.Registries;
import com.bespectacled.modernbeta.client.gui.screen.biome.ClimateBiomeScreen;
import com.bespectacled.modernbeta.client.gui.screen.biome.SingleBiomeScreen;
import com.bespectacled.modernbeta.client.gui.screen.biome.VanillaBiomeScreen;
import com.bespectacled.modernbeta.client.gui.screen.world.IndevWorldScreen;
import com.bespectacled.modernbeta.client.gui.screen.world.InfClimateWorldScreen;
import com.bespectacled.modernbeta.client.gui.screen.world.InfWorldScreen;
import com.bespectacled.modernbeta.client.gui.screen.world.Infdev227WorldScreen;
import com.bespectacled.modernbeta.client.gui.screen.world.IslandWorldScreen;
import com.bespectacled.modernbeta.client.gui.screen.world.PreInfWorldScreen;
import com.bespectacled.modernbeta.world.biome.provider.BetaBiomeProvider;
import com.bespectacled.modernbeta.world.biome.provider.PEBiomeProvider;
import com.bespectacled.modernbeta.world.biome.provider.SingleBiomeProvider;
import com.bespectacled.modernbeta.world.biome.provider.VanillaBiomeProvider;
import com.bespectacled.modernbeta.world.biome.provider.settings.BiomeProviderSettings;
import com.bespectacled.modernbeta.world.gen.provider.AlphaChunkProvider;
import com.bespectacled.modernbeta.world.gen.provider.BetaChunkProvider;
import com.bespectacled.modernbeta.world.gen.provider.BetaIslandsChunkProvider;
import com.bespectacled.modernbeta.world.gen.provider.Classic030ChunkProvider;
import com.bespectacled.modernbeta.world.gen.provider.IndevChunkProvider;
import com.bespectacled.modernbeta.world.gen.provider.Infdev227ChunkProvider;
import com.bespectacled.modernbeta.world.gen.provider.Infdev415ChunkProvider;
import com.bespectacled.modernbeta.world.gen.provider.Infdev420ChunkProvider;
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
        Registries.CHUNK.register(BuiltInTypes.Chunk.INFDEV_420.name, Infdev420ChunkProvider::new);
        Registries.CHUNK.register(BuiltInTypes.Chunk.INFDEV_415.name, Infdev415ChunkProvider::new);
        Registries.CHUNK.register(BuiltInTypes.Chunk.INFDEV_227.name, Infdev227ChunkProvider::new);
        Registries.CHUNK.register(BuiltInTypes.Chunk.INDEV.name, IndevChunkProvider::new);
        Registries.CHUNK.register(BuiltInTypes.Chunk.CLASSIC_0_30.name, Classic030ChunkProvider::new);
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
        Registries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.INFDEV_420.name, ChunkProviderSettings::createSettingsInfdev420);
        Registries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.INFDEV_415.name, ChunkProviderSettings::createSettingsInfdev415);
        Registries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.INFDEV_227.name, ChunkProviderSettings::createSettingsInfdev227);
        Registries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.INDEV.name, ChunkProviderSettings::createSettingsIndev);
        Registries.CHUNK_SETTINGS.register(BuiltInTypes.Chunk.CLASSIC_0_30.name, ChunkProviderSettings::createSettingsClassic030);
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
    
    // Register default world screens
    public static void registerWorldScreens() {
        /*
        Registries.WORLD_SCREEN.register(BuiltInTypes.DEFAULT_ID, BaseWorldScreen::new);
        Registries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.BASE.name, BaseWorldScreen::new);
        Registries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.INF.name, InfWorldScreen::new);
        Registries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.INFDEV_227.name, Infdev227WorldScreen::new);
        Registries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.PRE_INF.name, PreInfWorldScreen::new);
        Registries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.INDEV.name, IndevWorldScreen::new);
        Registries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.ISLAND.name, IslandWorldScreen::new);
        Registries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.INF_CLIMATE.name, InfClimateWorldScreen::new);
        */
        
        Registries.WORLD_SCREEN.register(BuiltInTypes.DEFAULT_ID, (screen, worldSetting) -> null);
        Registries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.INF.name, InfWorldScreen::create);
        Registries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.INF_CLIMATE.name, InfClimateWorldScreen::create);
        Registries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.INFDEV_227.name, Infdev227WorldScreen::create);
        Registries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.ISLAND.name, IslandWorldScreen::create);
        Registries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.PRE_INF.name, PreInfWorldScreen::create);
        Registries.WORLD_SCREEN.register(BuiltInTypes.WorldScreen.INDEV.name, IndevWorldScreen::create);
    }
    
    // Register default biome settings screens (Note: Match identifiers with biome ids!)
    public static void registerBiomeScreens() {
        Registries.BIOME_SCREEN.register(BuiltInTypes.DEFAULT_ID, (screen, worldSetting) -> null);
        Registries.BIOME_SCREEN.register(BuiltInTypes.Biome.BETA.name, ClimateBiomeScreen::create);
        Registries.BIOME_SCREEN.register(BuiltInTypes.Biome.SINGLE.name, SingleBiomeScreen::create);
        Registries.BIOME_SCREEN.register(BuiltInTypes.Biome.VANILLA.name, VanillaBiomeScreen::create);
        Registries.BIOME_SCREEN.register(BuiltInTypes.Biome.PE.name, ClimateBiomeScreen::create);
    }
    
    // Register default world providers
    public static void registerWorldProviders() {
        Registries.WORLD.register(BuiltInTypes.DEFAULT_ID, ModernBetaBuiltInWorldProviders.DEFAULT);
        Registries.WORLD.register(BuiltInTypes.Chunk.BETA.name, ModernBetaBuiltInWorldProviders.BETA);
        Registries.WORLD.register(BuiltInTypes.Chunk.SKYLANDS.name, ModernBetaBuiltInWorldProviders.SKYLANDS);
        Registries.WORLD.register(BuiltInTypes.Chunk.ALPHA.name, ModernBetaBuiltInWorldProviders.ALPHA);
        Registries.WORLD.register(BuiltInTypes.Chunk.INFDEV_611.name, ModernBetaBuiltInWorldProviders.INFDEV_611);
        Registries.WORLD.register(BuiltInTypes.Chunk.INFDEV_420.name, ModernBetaBuiltInWorldProviders.INFDEV_420);
        Registries.WORLD.register(BuiltInTypes.Chunk.INFDEV_415.name, ModernBetaBuiltInWorldProviders.INFDEV_415);
        Registries.WORLD.register(BuiltInTypes.Chunk.INFDEV_227.name, ModernBetaBuiltInWorldProviders.INFDEV_227);
        Registries.WORLD.register(BuiltInTypes.Chunk.INDEV.name, ModernBetaBuiltInWorldProviders.INDEV);
        Registries.WORLD.register(BuiltInTypes.Chunk.CLASSIC_0_30.name, ModernBetaBuiltInWorldProviders.CLASSIC_0_30);
        Registries.WORLD.register(BuiltInTypes.Chunk.BETA_ISLANDS.name, ModernBetaBuiltInWorldProviders.BETA_ISLANDS);
        Registries.WORLD.register(BuiltInTypes.Chunk.PE.name, ModernBetaBuiltInWorldProviders.PE);
    }
}
