package com.bespectacled.modernbeta.api.world;

import java.util.LinkedHashMap;
import java.util.Map;

import com.bespectacled.modernbeta.client.gui.Settings;
import com.bespectacled.modernbeta.world.biome.provider.settings.BiomeProviderSettings;
import com.bespectacled.modernbeta.world.gen.provider.settings.ChunkProviderSettings;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public final class WorldSettings {
    public static final String TAG_WORLD = "worldType";
    public static final String TAG_BIOME = "biomeType";
    public static final String TAG_SINGLE_BIOME = "singleBiome";
    
    public enum WorldSetting {
        CHUNK,
        BIOME
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
    
    public WorldSettings(NbtCompound chunkSettings, NbtCompound biomeSettings) {
        this.settings.put(WorldSetting.CHUNK, new Settings(chunkSettings));
        this.settings.put(WorldSetting.BIOME, new Settings(biomeSettings));
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
    
    public NbtCompound getNbt(WorldSetting settingsKey) {
        return this.settings.get(settingsKey).getNbt();
    }
}
