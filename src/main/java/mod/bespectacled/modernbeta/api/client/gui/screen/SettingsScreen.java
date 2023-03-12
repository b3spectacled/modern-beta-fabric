package mod.bespectacled.modernbeta.api.client.gui.screen;

import java.util.function.Consumer;

import mod.bespectacled.modernbeta.client.gui.screen.GUIScreen;
import mod.bespectacled.modernbeta.client.gui.screen.WorldScreen;
import mod.bespectacled.modernbeta.util.settings.MutableSettings;
import mod.bespectacled.modernbeta.util.settings.Settings;
import mod.bespectacled.modernbeta.util.settings.WorldSettings.WorldSetting;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.registry.DynamicRegistryManager;

public abstract class SettingsScreen extends GUIScreen {
    protected final WorldSetting worldSetting;
    protected final Consumer<Settings> consumer;
    protected final Settings settings;
    
    protected final DynamicRegistryManager registryManager;
    
    protected SettingsScreen(WorldScreen worldScreen, WorldSetting worldSetting, Consumer<Settings> consumer, Settings settings) {
        super("createWorld.customize.settings.title", worldScreen);

        this.worldSetting = worldSetting;
        this.consumer = consumer;
        this.settings = settings;
        
        this.registryManager = worldScreen.getRegistryManager();
    }
    
    protected SettingsScreen(WorldScreen worldScreen, WorldSetting worldSetting, Consumer<Settings> consumer) {
        // Create new settings independent of main world settings storage.
        // Will only be applied to main world settings when 'Done'.
        this(worldScreen, worldSetting, consumer, MutableSettings.copyOf(worldScreen.getWorldSettings().get(worldSetting)));
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
                this.consumer.accept(this.settings);
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
    
    protected void putSetting(String key, NbtElement element) {
        this.settings.put(key, element);
    }
    
    protected boolean hasSetting(String key) {
        return this.settings.containsKey(key);
    }
    
    protected NbtElement getSetting(String key) {
        return this.settings.get(key);
    }
}
