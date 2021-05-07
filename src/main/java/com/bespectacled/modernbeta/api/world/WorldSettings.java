package com.bespectacled.modernbeta.api.world;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public final class WorldSettings {
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
