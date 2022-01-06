package com.bespectacled.modernbeta.util.settings;

import java.util.LinkedHashMap;
import java.util.Map;

import com.bespectacled.modernbeta.util.NbtTags;

import net.minecraft.nbt.NbtCompound;
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
    
    public WorldSettings() {
        for (WorldSetting w : WorldSetting.values()) {
            this.settings.put(w, new MutableSettings());
        }
    }
    
    public WorldSettings(WorldSettings worldSettings) {
        for (WorldSetting w : WorldSetting.values()) {
            this.settings.put(w, new MutableSettings(worldSettings.getNbt(w)));
        }
    }
    
    public WorldSettings(NbtCompound chunkSettings, NbtCompound biomeSettings, NbtCompound caveBiomeSettings) {
        this.settings.put(WorldSetting.CHUNK, new MutableSettings(chunkSettings));
        this.settings.put(WorldSetting.BIOME, new MutableSettings(biomeSettings));
        this.settings.put(WorldSetting.CAVE_BIOME, new MutableSettings(caveBiomeSettings));
    }
    
    public void put(WorldSetting settingsKey, String key, NbtElement element) {
        this.settings.get(settingsKey).put(key, element);
    }
    
    public void putCompound(WorldSetting settingsKey, NbtCompound compound) {
        this.settings.get(settingsKey).putCompound(compound);
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
    
    public NbtElement get(WorldSetting settingsKey, String key) {
        return this.settings.get(settingsKey).get(key);
    }
    
    public boolean containsKey(WorldSetting settingsKey, String key) {
        return this.settings.get(settingsKey).containsKey(key);
    }
    
    public NbtCompound getNbt(WorldSetting settingsKey) {
        return this.settings.get(settingsKey).getNbt();
    }
}
