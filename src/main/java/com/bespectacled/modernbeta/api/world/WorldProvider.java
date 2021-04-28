package com.bespectacled.modernbeta.api.world;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.api.gui.AbstractWorldScreenProvider;
import com.bespectacled.modernbeta.api.registry.ProviderRegistries;
import com.bespectacled.modernbeta.api.world.gen.AbstractChunkProvider;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public final class WorldProvider {
    private final String chunkProvider;
    private final String chunkProviderSettings;
    private final String chunkGenSettings;
    private final String guiProvider;

    private final String defaultBiomeProvider;
    private final String defaultBiome;

    public WorldProvider(
        String chunkProvider,
        String chunkProviderSettings,
        String chunkGenSettings,
        String guiProvider,
        String defaultBiomeProvider,
        String defaultBiome
    ) {
        this.chunkProvider = chunkProvider;
        this.chunkProviderSettings = chunkProviderSettings;
        this.chunkGenSettings = chunkGenSettings;
        this.guiProvider = guiProvider;
        
        this.defaultBiomeProvider = defaultBiomeProvider;
        this.defaultBiome = defaultBiome;
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
    
    public String getDefaultBiomeProvider() {
        return this.defaultBiomeProvider;
    }
    
    public String getDefaultBiome() {
        return this.defaultBiome;
    }
    
    public Identifier getDefaultBiomeId() {
        return new Identifier(this.defaultBiome);
    }
    
    public AbstractChunkProvider createChunkProvider(long seed, ChunkGenerator chunkGenerator, Supplier<ChunkGeneratorSettings> generatorSettings, CompoundTag providerSettings) {
        return ProviderRegistries.CHUNK.get(this.chunkProvider).apply(seed, chunkGenerator, generatorSettings, providerSettings);
    }
    
    public AbstractWorldScreenProvider createLevelScreen(
        CreateWorldScreen parent, 
        DynamicRegistryManager registryManager, 
        CompoundTag biomeProviderSettings,
        CompoundTag chunkProviderSettings, 
        BiConsumer<CompoundTag, CompoundTag> consumer
    ) {
        return ProviderRegistries.WORLD_SCREEN.get(this.guiProvider).apply(parent, registryManager, biomeProviderSettings, chunkProviderSettings, consumer);
    }
}
