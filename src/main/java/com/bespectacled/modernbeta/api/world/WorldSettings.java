package com.bespectacled.modernbeta.api.world;

import java.util.LinkedHashMap;
import java.util.Map;

import com.bespectacled.modernbeta.client.gui.Settings;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public final class WorldSettings {
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
            this.settings.put(w, new Settings(worldSettings.getNbt(w)));
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
    
    public void clearChanges(WorldSetting settingsKey) {
        this.settings.get(settingsKey).clearChanges();
    }
    
    public void clearChanges() {
        for (Settings s : this.settings.values()) {
            s.clearChanges();
        }
    }
    
    public NbtElement getSetting(WorldSetting settingsKey, String key) {
        return this.settings.get(settingsKey).getSetting(key);
    }
    
    public boolean hasSetting(WorldSetting settingsKey, String key) {
        return this.settings.get(settingsKey).hasSetting(key);
    }
    
    public NbtCompound getNbt(WorldSetting settingsKey) {
        return this.settings.get(settingsKey).getNbt();
    }
}
