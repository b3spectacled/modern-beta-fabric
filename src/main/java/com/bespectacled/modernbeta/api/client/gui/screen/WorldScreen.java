package com.bespectacled.modernbeta.api.client.gui.screen;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.client.gui.wrapper.ActionOptionWrapper;
import com.bespectacled.modernbeta.api.client.gui.wrapper.CyclingOptionWrapper;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.api.registry.Registries;
import com.bespectacled.modernbeta.api.world.WorldProvider;
import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.api.world.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.util.GUIUtil;
import com.bespectacled.modernbeta.util.NBTUtil;
import com.bespectacled.modernbeta.world.biome.provider.settings.BiomeProviderSettings;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.registry.DynamicRegistryManager;

public abstract class WorldScreen extends GUIScreen {
    protected final WorldSettings worldSettings;
    protected final Consumer<WorldSettings> consumer;
    
    protected final DynamicRegistryManager registryManager;
    protected final WorldProvider worldProvider;

    protected WorldScreen(CreateWorldScreen parent, WorldSettings worldSettings, Consumer<WorldSettings> consumer) {
        super("createWorld.customize.worldType.title", parent);
        
        this.worldSettings = worldSettings;
        this.consumer = consumer;
        
        this.registryManager = parent.moreOptionsDialog.getRegistryManager();
        this.worldProvider = Registries.WORLD.get(NBTUtil.toStringOrThrow(this.worldSettings.getSetting(WorldSetting.CHUNK, WorldSettings.TAG_WORLD)));
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
                this.consumer.accept(this.worldSettings);
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
        
        this.addButton(doneButton);
        this.addButton(cancelButton);
        
        String biomeType = NBTUtil.toStringOrThrow(this.worldSettings.getSetting(WorldSetting.BIOME, WorldSettings.TAG_BIOME));
        String singleBiome = NBTUtil.toStringOrThrow(this.worldSettings.getSetting(WorldSetting.BIOME, WorldSettings.TAG_SINGLE_BIOME));
        
        CyclingOptionWrapper<WorldProvider> worldTypeOption = new CyclingOptionWrapper<>(
            "createWorld.customize.worldType", 
            Registries.WORLD.getEntries().stream().toArray(WorldProvider[]::new),
            () -> this.worldProvider,
            value -> {
                // Queue world type changes
                this.worldSettings.clearChanges();
                this.worldSettings.putChanges(value);
                
                this.client.openScreen(value.createWorldScreen(
                    (CreateWorldScreen)this.parent,
                    this.worldSettings,
                    this.consumer
                ));
            },
            this.worldProvider
        );
        
        CyclingOptionWrapper<String> biomeTypeOption = new CyclingOptionWrapper<>(
            "createWorld.customize.biomeType",
            Registries.BIOME.getKeySet().stream().toArray(String[]::new),
            () -> NBTUtil.toStringOrThrow(this.worldSettings.getSetting(WorldSetting.BIOME, WorldSettings.TAG_BIOME)),
            value -> {
                // Queue biome settings changes
                this.worldSettings.clearChanges(WorldSetting.BIOME);
                this.worldSettings.putChanges(WorldSetting.BIOME, BiomeProviderSettings.createSettingsBase(value, this.worldProvider.getSingleBiome()));
                
                this.client.openScreen(
                    this.worldProvider.createWorldScreen(
                        (CreateWorldScreen)this.parent,
                        this.worldSettings,
                        this.consumer
                ));
            },
            biomeType
        );
        
        Screen biomeSettingsScreen = Registries.BIOME_SCREEN
            .getOrDefault(NBTUtil.toStringOrThrow(this.worldSettings.getSetting(WorldSetting.BIOME, WorldSettings.TAG_BIOME)))
            .apply(this); 
        
        ActionOptionWrapper biomeSettingsOption = new ActionOptionWrapper(
            biomeType.equals(BuiltInTypes.Biome.SINGLE.name) ? "createWorld.customize.biomeType.biome" : "createWorld.customize.biomeType.settings", // Key
            biomeType.equals(BuiltInTypes.Biome.SINGLE.name) ? GUIUtil.createTranslatableBiomeStringFromId(singleBiome) : "",
            biomeSettingsScreen != null ? widget -> this.client.openScreen(biomeSettingsScreen) : null
        );
        
        this.addOption(worldTypeOption);
        this.addDualOption(biomeTypeOption, biomeSettingsOption);
    }
    
    protected void setDefaultSingleBiome(String defaultBiome) {
        // Replace default single biome with one supplied by world provider, if switching to Single biome type
        if (NBTUtil.toStringOrThrow(this.worldSettings.getSetting(WorldSetting.BIOME, WorldSettings.TAG_BIOME)).equals(BuiltInTypes.Biome.SINGLE.name)) { 
            this.worldSettings.putChange(WorldSetting.BIOME, WorldSettings.TAG_SINGLE_BIOME, NbtString.of(defaultBiome));
        }
    }
    
    public DynamicRegistryManager getRegistryManager() {
        return this.registryManager;
    }
    
    public WorldSettings getWorldSettings() {
        return this.worldSettings;
    }
}
