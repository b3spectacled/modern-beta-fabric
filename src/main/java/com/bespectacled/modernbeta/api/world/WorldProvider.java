package com.bespectacled.modernbeta.api.world;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.api.gui.WorldScreenProvider;
import com.bespectacled.modernbeta.api.registry.ProviderRegistries;
import com.bespectacled.modernbeta.api.world.gen.ChunkProvider;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public final class WorldProvider {
    private final String chunkProvider;
    private final String chunkGenSettings;
    private final String guiProvider;

    private final String defaultBiomeProvider;
    private final String defaultCaveBiomeProvider;
    private final String defaultBiome;

    public WorldProvider(
        String chunkProvider,
        String chunkGenSettings,
        String guiProvider,
        String defaultBiomeProvider,
        String defaultCaveBiomeProvider,
        String defaultBiome
    ) {
        this.chunkProvider = chunkProvider;
        this.chunkGenSettings = chunkGenSettings;
        this.guiProvider = guiProvider;
        
        this.defaultBiomeProvider = defaultBiomeProvider;
        this.defaultCaveBiomeProvider = defaultCaveBiomeProvider;
        this.defaultBiome = defaultBiome;
    }
    
    public String getChunkProvider() {
        return this.chunkProvider;
    }
    
    public String getChunkProviderSettings() {
        return this.chunkProvider;
    }
    
    public String getChunkGenSettings() {
        return this.chunkGenSettings;
    }
    
    public String getDefaultBiomeProvider() {
        return this.defaultBiomeProvider;
    }
    
    public String getDefaultBiomeProviderSettings() {
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
    
    public ChunkProvider createChunkProvider(long seed, ChunkGenerator chunkGenerator, Supplier<ChunkGeneratorSettings> generatorSettings, NbtCompound providerSettings) {
        return ProviderRegistries.CHUNK.get(this.chunkProvider).apply(seed, chunkGenerator, generatorSettings, providerSettings);
    }
    
    public WorldScreenProvider createLevelScreen(
        CreateWorldScreen parent, 
        DynamicRegistryManager registryManager,
        NbtCompound chunkProviderSettings,
        NbtCompound biomeProviderSettings,
        BiConsumer<NbtCompound, NbtCompound> consumer
    ) {
        return ProviderRegistries.WORLD_SCREEN.get(this.guiProvider).apply(parent, registryManager, chunkProviderSettings, biomeProviderSettings, consumer);
    }
}
