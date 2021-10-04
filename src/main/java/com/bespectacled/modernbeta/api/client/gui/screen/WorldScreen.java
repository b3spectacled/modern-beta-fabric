package com.bespectacled.modernbeta.api.client.gui.screen;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.client.gui.wrapper.ActionOptionWrapper;
import com.bespectacled.modernbeta.api.client.gui.wrapper.CyclingOptionWrapper;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.api.registry.Registries;
import com.bespectacled.modernbeta.api.world.WorldProvider;
import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.api.world.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.util.GuiUtil;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.world.biome.provider.settings.BiomeProviderSettings;
import com.bespectacled.modernbeta.world.cavebiome.provider.settings.CaveBiomeProviderSettings;
import com.bespectacled.modernbeta.world.gen.provider.settings.ChunkProviderSettings;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.registry.DynamicRegistryManager;

public abstract class WorldScreen extends GUIScreen {
    private final WorldSettings worldSettings;
    protected final Consumer<WorldSettings> consumer;
    
    protected final DynamicRegistryManager registryManager;
    protected final WorldProvider worldProvider;

    protected WorldScreen(CreateWorldScreen parent, WorldSettings worldSettings, Consumer<WorldSettings> consumer) {
        super("createWorld.customize.worldType.title", parent);
        
        this.worldSettings = worldSettings;
        this.consumer = consumer;
        
        this.registryManager = parent.moreOptionsDialog.getRegistryManager();
        this.worldProvider = Registries.WORLD.get(NbtUtil.toStringOrThrow(this.getChunkSetting(NbtTags.WORLD_TYPE)));
        
        // Replace single biome if applicable
        String biomeType = NbtUtil.toStringOrThrow(this.getBiomeSetting(NbtTags.BIOME_TYPE));
        
        if (biomeType.equals(BuiltInTypes.Biome.SINGLE.name))
            this.putBiomeSetting(NbtTags.SINGLE_BIOME, NbtString.of(this.worldProvider.getSingleBiome()));
        
        // Replace ocean structure options if applicable
        String worldType = NbtUtil.toStringOrThrow(this.getChunkSetting(NbtTags.WORLD_TYPE));
        WorldProvider worldProvider = Registries.WORLD.get(worldType);
        
        this.putChunkSetting(NbtTags.GEN_OCEAN_SHRINES, NbtByte.of(worldProvider.generateOceanShrines()));
        this.putChunkSetting(NbtTags.GEN_MONUMENTS, NbtByte.of(worldProvider.generateMonuments()));
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
        
        String biomeType = NbtUtil.toStringOrThrow(this.getBiomeSetting(NbtTags.BIOME_TYPE));
        String caveBiomeType = NbtUtil.toStringOrThrow(this.getCaveBiomeSetting(NbtTags.CAVE_BIOME_TYPE));
        
        CyclingOptionWrapper<WorldProvider> worldTypeOption = new CyclingOptionWrapper<>(
            "createWorld.customize.worldType", 
            Registries.WORLD.getEntries().stream().toArray(WorldProvider[]::new),
            () -> this.worldProvider,
            value -> {
                // Queue world type changes
                this.worldSettings.clearChanges();
                this.worldSettings.putChanges(
                    WorldSetting.CHUNK, 
                    Registries.CHUNK_SETTINGS.getOrElse(
                        value.getChunkProvider(), 
                        () -> ChunkProviderSettings.createSettingsBase(value.getChunkProvider())
                    ).get()
                );
                this.worldSettings.putChanges(
                    WorldSetting.BIOME, 
                    Registries.BIOME_SETTINGS.getOrElse(
                        value.getBiomeProvider(),
                        () -> BiomeProviderSettings.createSettingsBase(value.getBiomeProvider())
                    ).get()
                );
                this.worldSettings.putChanges(
                    WorldSetting.CAVE_BIOME, 
                    Registries.CAVE_BIOME_SETTINGS.getOrElse(
                        value.getCaveBiomeProvider(), 
                        () -> CaveBiomeProviderSettings.createSettingsBase(value.getCaveBiomeProvider())
                    ).get()
                );
                
                // Create new world screen
                this.client.setScreen(
                    value.createWorldScreen(
                        (CreateWorldScreen)this.parent,
                        this.worldSettings,
                        this.consumer
                ));
            }
        );
        
        CyclingOptionWrapper<String> biomeTypeOption = new CyclingOptionWrapper<>(
            "createWorld.customize.biomeType",
            Registries.BIOME.getKeySet().stream().toArray(String[]::new),
            () -> NbtUtil.toStringOrThrow(this.worldSettings.getSetting(WorldSetting.BIOME, NbtTags.BIOME_TYPE)),
            value -> {
                // Queue biome settings changes
                this.worldSettings.clearChanges(WorldSetting.BIOME);
                this.worldSettings.putChanges(
                    WorldSetting.BIOME,
                    Registries.BIOME_SETTINGS.getOrElse(
                        value,
                        () -> BiomeProviderSettings.createSettingsBase(value)
                    ).get()
                );
                
                this.resetWorldScreen();
            }
        );
        
        CyclingOptionWrapper<String> caveBiomeTypeOption = new CyclingOptionWrapper<>(
            "createWorld.customize.caveBiomeType",
            Registries.CAVE_BIOME.getKeySet().stream().toArray(String[]::new),
            () -> NbtUtil.toStringOrThrow(this.worldSettings.getSetting(WorldSetting.CAVE_BIOME, NbtTags.CAVE_BIOME_TYPE)),
            value -> {
                // Queue cave biome settings changes
                this.worldSettings.clearChanges(WorldSetting.CAVE_BIOME);
                this.worldSettings.putChanges(
                    WorldSetting.CAVE_BIOME, 
                    Registries.CAVE_BIOME_SETTINGS.getOrElse(
                        value, 
                        () -> CaveBiomeProviderSettings.createSettingsBase(value)
                    ).get()
                );
            
                this.resetWorldScreen();
            }
        );
        
        Screen biomeSettingsScreen = Registries.BIOME_SCREEN
            .getOrDefault(NbtUtil.toStringOrThrow(this.worldSettings.getSetting(WorldSetting.BIOME, NbtTags.BIOME_TYPE)))
            .apply(this, WorldSetting.BIOME); 
        
        ActionOptionWrapper biomeSettingsOption = new ActionOptionWrapper(
            biomeType.equals(BuiltInTypes.Biome.SINGLE.name) ?
                "createWorld.customize.biomeType.biome" : 
                "createWorld.customize.biomeType.settings", // Key
            biomeType.equals(BuiltInTypes.Biome.SINGLE.name) ? 
                GuiUtil.createTranslatableBiomeStringFromId(NbtUtil.toStringOrThrow(this.getBiomeSetting(NbtTags.SINGLE_BIOME))) : 
                "",
            biomeSettingsScreen != null ? 
                widget -> this.client.setScreen(biomeSettingsScreen) : 
                null
        );
        
        Screen caveBiomeSettingsScreen = Registries.BIOME_SCREEN
            .getOrDefault(NbtUtil.toStringOrThrow(this.worldSettings.getSetting(WorldSetting.CAVE_BIOME, NbtTags.CAVE_BIOME_TYPE)))
            .apply(this, WorldSetting.CAVE_BIOME);
        
        ActionOptionWrapper caveBiomeSettingsOption = new ActionOptionWrapper(
            caveBiomeType.equals(BuiltInTypes.CaveBiome.SINGLE.name) ?
                "createWorld.customize.biomeType.biome" :
                "createWorld.customize.biomeType.settings",
            caveBiomeType.equals(BuiltInTypes.CaveBiome.SINGLE.name) ?
                GuiUtil.createTranslatableBiomeStringFromId(NbtUtil.toStringOrThrow(this.getCaveBiomeSetting(NbtTags.SINGLE_BIOME))) : 
                "",
            caveBiomeSettingsScreen != null ?
                widget -> this.client.setScreen(caveBiomeSettingsScreen) :
                null
        );
        
        this.addOption(worldTypeOption);
        this.addDualOption(biomeTypeOption, biomeSettingsOption);
        this.addDualOption(caveBiomeTypeOption, caveBiomeSettingsOption);
    }
    
    public DynamicRegistryManager getRegistryManager() {
        return this.registryManager;
    }
    
    public WorldSettings getWorldSettings() {
        return this.worldSettings;
    }
    
    /* Convenience methods */
    
    protected void setDefaultSingleBiome(String defaultBiome) {
        // Replace default single biome with one supplied by world provider, if switching to Single biome type
        if (NbtUtil.toStringOrThrow(this.worldSettings.getSetting(WorldSetting.BIOME, NbtTags.BIOME_TYPE)).equals(BuiltInTypes.Biome.SINGLE.name)) { 
            this.worldSettings.putChange(WorldSetting.BIOME, NbtTags.SINGLE_BIOME, NbtString.of(defaultBiome));
        }
    }
    
    protected void putChunkSetting(String key, NbtElement element) {
       this.worldSettings.putChange(WorldSetting.CHUNK, key, element); 
    }
    
    protected boolean hasChunkSetting(String key) {
        return this.worldSettings.hasSetting(WorldSetting.CHUNK, key);
    }
    
    protected NbtElement getChunkSetting(String key) {
        return this.worldSettings.getSetting(WorldSetting.CHUNK, key);
    }
    
    protected void putBiomeSetting(String key, NbtElement element) {
        this.worldSettings.putChange(WorldSetting.BIOME, key, element); 
     }
    
    protected boolean hasBiomeSetting(String key) {
        return this.worldSettings.hasSetting(WorldSetting.BIOME, key);
    }
    
    protected NbtElement getBiomeSetting(String key) {
        return this.worldSettings.getSetting(WorldSetting.BIOME, key);
    }
    
    protected void putCaveBiomeSetting(String key, NbtElement element) {
        this.worldSettings.putChange(WorldSetting.CAVE_BIOME, key, element); 
     }
    
    protected boolean hasCaveBiomeSetting(String key) {
        return this.worldSettings.hasSetting(WorldSetting.CAVE_BIOME, key);
    }
    
    protected NbtElement getCaveBiomeSetting(String key) {
        return this.worldSettings.getSetting(WorldSetting.CAVE_BIOME, key);
    }
    
    protected void resetWorldScreen() {
        this.client.setScreen(
            this.worldProvider.createWorldScreen(
                (CreateWorldScreen)this.parent,
                this.worldSettings,
                this.consumer
        ));
    }
}
