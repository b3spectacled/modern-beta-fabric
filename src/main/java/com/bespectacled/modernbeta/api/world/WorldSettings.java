package com.bespectacled.modernbeta.api.world;

import java.util.LinkedHashMap;
import java.util.Map;

import com.bespectacled.modernbeta.world.biome.provider.settings.BiomeProviderSettings;
import com.bespectacled.modernbeta.world.gen.provider.settings.ChunkProviderSettings;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public final class WorldSettings {
    public static final String TAG_WORLD = "worldType";
    public static final String TAG_BIOME = "biomeType";
    public static final String TAG_CAVE_BIOME = "caveBiomeType";
    public static final String TAG_SINGLE_BIOME = "singleBiome";
    
    public enum WorldSetting {
        CHUNK,
        BIOME,
        CAVE_BIOME
    }
    
    private final Map<WorldSetting, NbtCompound> settings;
    
    public WorldSettings() {
        this.settings = new LinkedHashMap<WorldSetting, NbtCompound>();
        
        for (WorldSetting w : WorldSetting.values()) {
            this.settings.put(w, new NbtCompound());
        }
    }
    
    public WorldSettings(
        NbtCompound chunkProviderSettings, 
        NbtCompound biomeProviderSettings,
        NbtCompound caveBiomeProviderSettings
    ) {
        this(); // Ensure settings are initialized with something.
        
        this.settings.put(WorldSetting.CHUNK, new NbtCompound().copyFrom(chunkProviderSettings));
        this.settings.put(WorldSetting.BIOME, new NbtCompound().copyFrom(biomeProviderSettings));
        this.settings.put(WorldSetting.CAVE_BIOME, new NbtCompound().copyFrom(caveBiomeProviderSettings));
    }
    
    public WorldSettings(WorldProvider worldProvider) {
        this();
        
        this.settings.put(WorldSetting.CHUNK, ChunkProviderSettings.createSettingsBase(worldProvider.getChunkProvider()));
        this.settings.put(WorldSetting.BIOME, BiomeProviderSettings.createSettingsBase(worldProvider.getBiomeProvider(), worldProvider.getSingleBiome()));
        this.settings.put(WorldSetting.CAVE_BIOME, new NbtCompound());
    }
    
    public NbtCompound getSettings(WorldSetting settingsKey) {
        return new NbtCompound().copyFrom(this.settings.get(settingsKey));
    }
    
    public NbtElement getSetting(WorldSetting settingsKey, String key) {
        return this.settings.get(settingsKey).get(key);
    }
    
    public void putSetting(WorldSetting settingsKey, String key, NbtElement element) {
        this.settings.get(settingsKey).put(key, element);
    }
    
    public void copySettingsFrom(WorldSetting settingsKey, NbtCompound settings) {
        this.settings.get(settingsKey).copyFrom(settings);
    }
}
