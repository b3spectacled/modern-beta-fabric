package com.bespectacled.modernbeta.api.gui.screen;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.api.world.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.gui.Settings;

import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.registry.DynamicRegistryManager;

public class BiomeScreen extends GUIScreen {
    protected final WorldSettings worldSettings;
    protected final DynamicRegistryManager registryManager;
    protected final Consumer<Settings> consumer;
    
    protected final Settings biomeSettings;
    
    protected BiomeScreen(WorldScreen parent, Consumer<Settings> consumer) {
        super("createWorld.customize.biomeType.title", parent);

        this.worldSettings = parent.getWorldSettings();
        this.registryManager = parent.getRegistryManager();
        this.consumer = consumer;
        
        // Create new settings independent of main world settings storage.
        // Will only be applied to main world settings when 'Done'.
        this.biomeSettings = new Settings(this.worldSettings.getStoredAndQueuedSettings(WorldSetting.BIOME));
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
                // Apply all settings change only on done!
                this.biomeSettings.applyChanges();
                
                this.consumer.accept(this.biomeSettings);
                this.client.openScreen(this.parent);
            }
        );
        
        cancelButton = new ButtonWidget(
            this.width / 2 + 5, this.height - 28, 150, 20, 
            ScreenTexts.CANCEL,
            buttonWidget -> {
                this.client.openScreen(this.parent);
            }
        );
        
        this.addDrawableChild(doneButton);
        this.addDrawableChild(cancelButton);
    }

}
