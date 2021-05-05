package com.bespectacled.modernbeta.api.world;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public final class WorldSettings {
    private NbtCompound chunkProviderSettings;
    private NbtCompound biomeProviderSettings;
    @SuppressWarnings("unused")
    private NbtCompound caveBiomeProviderSettings;
    
    public WorldSettings() {
        this.chunkProviderSettings = new NbtCompound();
        this.biomeProviderSettings = new NbtCompound();
        this.caveBiomeProviderSettings = new NbtCompound();
    }
    
    public WorldSettings(NbtCompound chunkProviderSettings, NbtCompound biomeProviderSettings) {
        this.chunkProviderSettings = new NbtCompound().copyFrom(chunkProviderSettings);
        this.biomeProviderSettings = new NbtCompound().copyFrom(biomeProviderSettings);
        this.caveBiomeProviderSettings = new NbtCompound();
    }
    
    public NbtCompound getChunkSettings() {
        return new NbtCompound().copyFrom(this.chunkProviderSettings);
    }
    
    public NbtCompound getBiomeSettings() {
        return new NbtCompound().copyFrom(this.biomeProviderSettings);
    }
    
    public NbtElement getChunkSetting(String key) {
        return this.chunkProviderSettings.get(key);
    }
    
    public NbtElement getBiomeSetting(String key) {
        return this.biomeProviderSettings.get(key);
    }
    
    public void putChunkSetting(String key, NbtElement element) {
        this.chunkProviderSettings.put(key, element);
    }
    
    public void putBiomeSetting(String key, NbtElement element) {
        this.biomeProviderSettings.put(key, element);
    }
    
    public void copyChunkSettingsFrom(NbtCompound chunkProviderSettings) {
        this.chunkProviderSettings = this.chunkProviderSettings.copyFrom(chunkProviderSettings);
    }
    
    public void copyBiomeSettingsFrom(NbtCompound biomeProviderSettings) {
        this.biomeProviderSettings = this.biomeProviderSettings.copyFrom(biomeProviderSettings);
    }
}
