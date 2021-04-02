package com.bespectacled.modernbeta.api;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.api.registry.ChunkProviderRegistry;
import com.bespectacled.modernbeta.api.registry.ScreenProviderRegistry;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class WorldProvider {
    private final String chunkProvider;
    private final String chunkProviderSettings;
    private final String chunkGenSettings;
    private final String guiProvider;

    private final String defaultBiomeProvider;
    private final String defaultCaveBiomeProvider;
    private final String defaultBiome;

    private final boolean showNoiseOptions; // TODO: Temporary probably, should remove in favor of data pack noise settings.

    public WorldProvider(
        String chunkProvider,
        String chunkProviderSettings,
        String chunkGenSettings,
        String guiProvider,
        String defaultBiomeProvider,
        String defaultCaveBiomeProvider,
        String defaultBiome,
        boolean showNoiseOptions
    ) {
        this.chunkProvider = chunkProvider;
        this.chunkProviderSettings = chunkProviderSettings;
        this.chunkGenSettings = chunkGenSettings;
        this.guiProvider = guiProvider;
        
        this.defaultBiomeProvider = defaultBiomeProvider;
        this.defaultCaveBiomeProvider = defaultCaveBiomeProvider;
        this.defaultBiome = defaultBiome;
        
        this.showNoiseOptions = showNoiseOptions;
    }
    
    public String getName() {
        return this.chunkProvider;
    }
    
    public String getChunkProviderSettings() {
        return this.chunkProviderSettings;
    }
    
    public String getChunkGenSettings() {
        return this.chunkGenSettings;
    }
    
    public boolean showNoiseOptions() {
        return this.showNoiseOptions;
    }
    
    public String getDefaultBiomeProvider() {
        return this.defaultBiomeProvider;
    }
    
    public String getDefaultCaveBiomeProvider() {
        return this.defaultCaveBiomeProvider;
    }
    
    public String getDefaultBiome() {
        return this.defaultBiome;
    }
    
    public Identifier getDefaultBiomeId() {
        return new Identifier(this.defaultBiome);
    }
    
    public AbstractChunkProvider createChunkProvider(long seed, AbstractBiomeProvider biomeProvider, Supplier<ChunkGeneratorSettings> generatorSettings, NbtCompound providerSettings) {
        return ChunkProviderRegistry.get(this.chunkProvider).apply(seed, biomeProvider, generatorSettings, providerSettings);
    }
    
    public AbstractLevelScreenProvider createLevelScreen(
        CreateWorldScreen parent, 
        DynamicRegistryManager registryManager, 
        NbtCompound biomeProviderSettings,
        NbtCompound chunkProviderSettings, 
        BiConsumer<NbtCompound, NbtCompound> consumer
    ) {
        return ScreenProviderRegistry.get(this.guiProvider).apply(parent, registryManager, biomeProviderSettings, chunkProviderSettings, consumer);
    }
}
