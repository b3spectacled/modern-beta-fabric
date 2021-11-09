package com.bespectacled.modernbeta.api.client.gui.screen;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.client.gui.Settings;
import com.bespectacled.modernbeta.client.gui.WorldSettings;
import com.bespectacled.modernbeta.client.gui.WorldSettings.WorldSetting;

import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.registry.DynamicRegistryManager;

public abstract class BiomeScreen extends GUIScreen {
    @SuppressWarnings("unused")
    private final WorldSettings worldSettings;
    protected final Consumer<Settings> consumer;
    
    protected final DynamicRegistryManager registryManager;
    protected final Settings biomeSettings;
    protected final WorldSetting worldSetting;
    
    protected BiomeScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer, Settings biomeSettings) {
        super("createWorld.customize.biomeType.title", parent);

        this.worldSettings = parent.getWorldSettings();
        this.worldSetting = worldSetting;
        this.consumer = consumer;
        this.biomeSettings = biomeSettings;
        
        this.registryManager = parent.getRegistryManager();
    }
    
    protected BiomeScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer) {
        // Create new settings independent of main world settings storage.
        // Will only be applied to main world settings when 'Done'.
        this(parent, worldSetting, consumer, new Settings(parent.getWorldSettings().getNbt(worldSetting)));
    }
    
    @Override
    protected void init() {
        super.init();
        
        ButtonWidget doneButton;
        ButtonWidget cancelButton;
        
        doneButton = new ButtonWidget(
            this.width / 2 - 155, this.height - 28, 150, 20, 
            ScreenTexts.DONE, 
            buttonWidget -> {
                this.consumer.accept(this.biomeSettings);
                this.client.setScreen(this.parent);
            }
        );
        
        cancelButton = new ButtonWidget(
            this.width / 2 + 5, this.height - 28, 150, 20, 
            ScreenTexts.CANCEL,
            buttonWidget -> {
                this.client.setScreen(this.parent);
            }
        );
        
        this.addDrawableChild(doneButton);
        this.addDrawableChild(cancelButton);
    }
    
    /* Convenience methods */
    
    protected void putBiomeSetting(String key, NbtElement element) {
        this.biomeSettings.putChange(key, element);
    }
    
    protected boolean hasBiomeSetting(String key) {
        return this.biomeSettings.hasSetting(key);
    }
    
    protected NbtElement getBiomeSetting(String key) {
        return this.biomeSettings.getSetting(key);
    }
}
