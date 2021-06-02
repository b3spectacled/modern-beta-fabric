package com.bespectacled.modernbeta.api.world;

import java.util.LinkedHashMap;
import java.util.Map;

import com.bespectacled.modernbeta.world.biome.provider.settings.BiomeProviderSettings;
import com.bespectacled.modernbeta.world.gen.provider.settings.ChunkProviderSettings;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

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
    
    private final Map<WorldSetting, CompoundTag> settings;
    
    public WorldSettings() {
        this.settings = new LinkedHashMap<WorldSetting, CompoundTag>();
        
        for (WorldSetting w : WorldSetting.values()) {
            this.settings.put(w, new CompoundTag());
        }
    }
    
    public WorldSettings(
        CompoundTag chunkProviderSettings, 
        CompoundTag biomeProviderSettings,
        CompoundTag caveBiomeProviderSettings
    ) {
        this(); // Ensure settings are initialized with something.
        
        this.settings.put(WorldSetting.CHUNK, new CompoundTag().copyFrom(chunkProviderSettings));
        this.settings.put(WorldSetting.BIOME, new CompoundTag().copyFrom(biomeProviderSettings));
        this.settings.put(WorldSetting.CAVE_BIOME, new CompoundTag().copyFrom(caveBiomeProviderSettings));
    }
    
    public WorldSettings(WorldProvider worldProvider) {
        this();
        
        this.settings.put(WorldSetting.CHUNK, ChunkProviderSettings.createSettingsBase(worldProvider.getChunkProvider()));
        this.settings.put(WorldSetting.BIOME, BiomeProviderSettings.createSettingsBase(worldProvider.getBiomeProvider(), worldProvider.getSingleBiome()));
        this.settings.put(WorldSetting.CAVE_BIOME, new CompoundTag());
    }
    
    public CompoundTag getSettings(WorldSetting settingsKey) {
        return new CompoundTag().copyFrom(this.settings.get(settingsKey));
    }
    
    public Tag getSetting(WorldSetting settingsKey, String key) {
        return this.settings.get(settingsKey).get(key);
    }
    
    public void putSetting(WorldSetting settingsKey, String key, Tag element) {
        this.settings.get(settingsKey).put(key, element);
    }
    
    public void copySettingsFrom(WorldSetting settingsKey, CompoundTag settings) {
        this.settings.get(settingsKey).copyFrom(settings);
    }
}
