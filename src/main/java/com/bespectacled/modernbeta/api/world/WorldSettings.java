package com.bespectacled.modernbeta.api.world;

import java.util.LinkedHashMap;
import java.util.Map;

import com.bespectacled.modernbeta.gui.Settings;
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
    
    private final Map<WorldSetting, Settings> settings = new LinkedHashMap<>();
    
    public WorldSettings() {
        for (WorldSetting w : WorldSetting.values()) {
            this.settings.put(w, new Settings());
        }
    }
    
    public WorldSettings(WorldSettings worldSettings) {
        for (WorldSetting w : WorldSetting.values()) {
            this.settings.put(w, new Settings(worldSettings.getSettings(w)));
        }
    }
    
    public WorldSettings(NbtCompound chunkSettings, NbtCompound biomeSettings, NbtCompound caveBiomeSettings) {
        this.settings.put(WorldSetting.CHUNK, new Settings(chunkSettings));
        this.settings.put(WorldSetting.BIOME, new Settings(biomeSettings));
        this.settings.put(WorldSetting.CAVE_BIOME, new Settings(caveBiomeSettings));
    }
    
    public void putChange(WorldSetting settingsKey, String key, NbtElement element) {
        this.settings.get(settingsKey).putChange(key, element);
    }
    
    public void putChanges(WorldSetting settingsKey, NbtCompound compound) {
        this.settings.get(settingsKey).putChanges(compound);
    }
    
    public void putChanges(WorldProvider worldProvider) {
        this.putChanges(WorldSetting.CHUNK, ChunkProviderSettings.createSettingsBase(worldProvider.getChunkProvider()));
        this.putChanges(WorldSetting.BIOME, BiomeProviderSettings.createSettingsBase(worldProvider.getBiomeProvider(), worldProvider.getSingleBiome()));
        this.putChanges(WorldSetting.CAVE_BIOME, new NbtCompound());
    }
    
    public void clearChanges(WorldSetting settingsKey) {
        this.settings.get(settingsKey).clearChanges();
    }
    
    public void clearChanges() {
        for (Settings s : this.settings.values()) {
            s.clearChanges();
        }
    }
    
    public void applyChanges() {
        for (Settings s : this.settings.values()) {
            s.applyChanges();
        }
    }
    
    public NbtElement getSetting(WorldSetting settingsKey, String key) {
        return this.settings.get(settingsKey).getSetting(key);
    }
    
    public NbtCompound getSettings(WorldSetting settingsKey) {
        return this.settings.get(settingsKey).getNbt();
    }
    
    /*
    private final Map<WorldSetting, NbtCompound> settings;
    
    public WorldSettings() {
        this.settings = new LinkedHashMap<WorldSetting, NbtCompound>();
        
        for (WorldSetting w : WorldSetting.values()) {
            this.settings.put(w, new NbtCompound());
        }
    }
    
    public WorldSettings(WorldSettings worldSettings) {
        this();
        
        this.settings.put(WorldSetting.CHUNK, new NbtCompound().copyFrom(worldSettings.getSettings(WorldSetting.CHUNK)));
        this.settings.put(WorldSetting.BIOME, new NbtCompound().copyFrom(worldSettings.getSettings(WorldSetting.BIOME)));
        this.settings.put(WorldSetting.CAVE_BIOME, new NbtCompound().copyFrom(worldSettings.getSettings(WorldSetting.CAVE_BIOME)));
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
    */
}
