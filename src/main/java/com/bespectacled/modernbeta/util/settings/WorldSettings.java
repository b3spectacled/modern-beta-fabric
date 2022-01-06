package com.bespectacled.modernbeta.util.settings;

import java.util.LinkedHashMap;
import java.util.Map;

import com.bespectacled.modernbeta.util.NbtTags;

import net.minecraft.nbt.NbtElement;

public final class WorldSettings {
    public enum WorldSetting {
        CHUNK(NbtTags.WORLD_TYPE),
        BIOME(NbtTags.BIOME_TYPE),
        CAVE_BIOME(NbtTags.CAVE_BIOME_TYPE);
        
        public final String tag;
        
        private WorldSetting(String tag) {
            this.tag = tag;
        }
    }
    
    private final Map<WorldSetting, MutableSettings> settings = new LinkedHashMap<>();
    
    public WorldSettings(Settings chunkSettings, Settings biomeSettings, Settings caveBiomeSettings) {
        this.settings.put(WorldSetting.CHUNK, MutableSettings.copyOf(chunkSettings));
        this.settings.put(WorldSetting.BIOME, MutableSettings.copyOf(biomeSettings));
        this.settings.put(WorldSetting.CAVE_BIOME, MutableSettings.copyOf(caveBiomeSettings));
    }
    
    public void put(WorldSetting settingsKey, String key, NbtElement element) {
        this.settings.get(settingsKey).put(key, element);
    }
    
    public void replace(WorldSetting settingsKey, Settings settings) {
        this.settings.put(settingsKey, MutableSettings.copyOf(settings));
    }
    
    public void remove(WorldSetting settingsKey, String key) {
        this.settings.get(settingsKey).remove(key);
    }
    
    public void clear(WorldSetting settingsKey) {
        this.settings.get(settingsKey).clear();
    }
    
    public void clear() {
        for (MutableSettings s : this.settings.values()) {
            s.clear();
        }
    }
    
    public Settings get(WorldSetting settingsKey) {
        return this.settings.get(settingsKey);
    }
    
    public NbtElement get(WorldSetting settingsKey, String key) {
        return this.settings.get(settingsKey).get(key);
    }
    
    public boolean containsKey(WorldSetting settingsKey, String key) {
        return this.settings.get(settingsKey).containsKey(key);
    }
}
