package com.bespectacled.modernbeta.api;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class WorldProvider {
    private final String chunkProvider;
    private final String guiProvider;
    
    private final String defaultBiomeProvider;
    private final String defaultCaveBiomeProvider;
    private final String defaultBiome;

    private final boolean showOceansOption;
    private final boolean showNoiseOptions;

    public WorldProvider(
        String chunkProvider,
        String guiProvider,
        String defaultBiomeProvider,
        String defaultCaveBiomeProvider,
        String defaultBiome,
        boolean showOceansOption,
        boolean showNoiseOptions
    ) {
        this.chunkProvider = chunkProvider;
        this.guiProvider = guiProvider;
        
        this.defaultBiomeProvider = defaultBiomeProvider;
        this.defaultCaveBiomeProvider = defaultCaveBiomeProvider;
        this.defaultBiome = defaultBiome;
        
        this.showOceansOption = showOceansOption;
        this.showNoiseOptions = showNoiseOptions;
    }
    
    public String getName() {
        return this.chunkProvider;
    }
    
    public boolean showOceansOption() {
        return this.showOceansOption;
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
    
    public AbstractChunkProvider createChunkProvider(long seed, Supplier<ChunkGeneratorSettings> generatorSettings, NbtCompound providerSettings) {
        return ChunkProviderType.getChunkProvider(this.chunkProvider).apply(seed, generatorSettings, providerSettings);
    }
    
    public AbstractScreenProvider createLevelScreen(
        CreateWorldScreen parent, 
        DynamicRegistryManager registryManager, 
        NbtCompound biomeProviderSettings,
        NbtCompound chunkProviderSettings, 
        BiConsumer<NbtCompound, NbtCompound> consumer
    ) {
        return ScreenProviderType.getScreenProvider(this.guiProvider).apply(parent, registryManager, biomeProviderSettings, chunkProviderSettings, consumer);
    }
}
