package com.bespectacled.modernbeta.client.gui;

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
    
    private final Map<WorldSetting, Settings> settings = new LinkedHashMap<>();
    
    public WorldSettings() {
        for (WorldSetting w : WorldSetting.values()) {
            this.settings.put(w, new Settings());
        }
    }
    
    public WorldSettings(WorldSettings worldSettings) {
        for (WorldSetting w : WorldSetting.values()) {
            this.settings.put(w, new Settings(worldSettings.getNbt(w)));
        }
    }
    
    public WorldSettings(NbtCompound chunkSettings, NbtCompound biomeSettings, NbtCompound caveBiomeSettings) {
        this.settings.put(WorldSetting.CHUNK, new Settings(chunkSettings));
        this.settings.put(WorldSetting.BIOME, new Settings(biomeSettings));
        this.settings.put(WorldSetting.CAVE_BIOME, new Settings(caveBiomeSettings));
    }
    
    public void putElement(WorldSetting settingsKey, String key, NbtElement element) {
        this.settings.get(settingsKey).putElement(key, element);
    }
    
    public void putCompound(WorldSetting settingsKey, NbtCompound compound) {
        this.settings.get(settingsKey).putCompound(compound);
    }
    
    public void removeElement(WorldSetting settingsKey, String key) {
        this.settings.get(settingsKey).removeElement(key);
    }
    
    public void clearSettings(WorldSetting settingsKey) {
        this.settings.get(settingsKey).clearAll();
    }
    
    public void clearAll() {
        for (Settings s : this.settings.values()) {
            s.clearAll();
        }
    }
    
    public NbtElement getElement(WorldSetting settingsKey, String key) {
        return this.settings.get(settingsKey).getElement(key);
    }
    
    public boolean hasElement(WorldSetting settingsKey, String key) {
        return this.settings.get(settingsKey).hasElement(key);
    }
    
    public NbtCompound getNbt(WorldSetting settingsKey) {
        return this.settings.get(settingsKey).getNbt();
    }
}
