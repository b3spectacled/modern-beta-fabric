package com.bespectacled.modernbeta.api.gui;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.api.registry.Registries;
import com.bespectacled.modernbeta.api.world.WorldProvider;
import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.api.world.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.gui.ActionButtonOption;
import com.bespectacled.modernbeta.gui.CyclingOptionWrapper;
import com.bespectacled.modernbeta.util.GUIUtil;
import com.bespectacled.modernbeta.util.NBTUtil;
import com.bespectacled.modernbeta.world.biome.provider.settings.BiomeProviderSettings;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.options.GameOptionsScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.text.OrderedText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.DynamicRegistryManager;

public abstract class WorldScreen extends Screen {
    protected final CreateWorldScreen parent;
    protected final DynamicRegistryManager registryManager;
    protected final WorldSettings worldSettings;
    protected final Consumer<WorldSettings> consumer;
    
    protected final WorldProvider worldProvider;
    
    protected ButtonListWidget buttonList;
    
    public WorldScreen(
        CreateWorldScreen parent,
        WorldSettings worldSettings,
        Consumer<WorldSettings> consumer       
    ) {
        super(new TranslatableText("createWorld.customize.worldType.title"));
        
        this.parent = parent;
        this.registryManager = parent.moreOptionsDialog.getRegistryManager();
        this.worldSettings = worldSettings;
        this.consumer = consumer;
        
        this.worldProvider = Registries.WORLD.get(NBTUtil.readStringOrThrow(
            WorldSettings.TAG_WORLD, 
            this.worldSettings.getSettings(WorldSetting.CHUNK)
        ));
    }
    
    @Override
    protected void init() {
        this.buttonList = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        this.children.add(this.buttonList);
        
        String biomeType = NBTUtil.readStringOrThrow(WorldSettings.TAG_BIOME, this.worldSettings.getSettings(WorldSetting.BIOME));
        String singleBiome = NBTUtil.readString(WorldSettings.TAG_SINGLE_BIOME, this.worldSettings.getSettings(WorldSetting.BIOME), ModernBeta.BIOME_CONFIG.singleBiome);

        ButtonWidget doneButton;
        ButtonWidget cancelButton;
        
        CyclingOptionWrapper<String> worldTypeOption;
        CyclingOptionWrapper<String> biomeTypeOption;
        
        Screen biomeSettingsScreen;
        ActionButtonOption biomeSettingsOption;
        
        doneButton = new ButtonWidget(
            this.width / 2 - 155, this.height - 28, 150, 20, 
            ScreenTexts.DONE, 
            (buttonWidget) -> {
                this.consumer.accept(this.worldSettings);
                this.client.openScreen(this.parent);
            }
        );
        
        cancelButton = new ButtonWidget(
            this.width / 2 + 5, this.height - 28, 150, 20, 
            ScreenTexts.CANCEL,
            (buttonWidget) -> {
                this.client.openScreen(this.parent);
            }
        );
        
        String worldTypeString = this.worldProvider.getChunkProvider();
        worldTypeOption = new CyclingOptionWrapper<>(
            "createWorld.customize.worldType",
            Registries.WORLD.getKeySet().stream().collect(Collectors.toList()),
            worldTypeString,
            value -> {
                WorldProvider newWorldProvider = Registries.WORLD.get(value);
                
                // Reset settings when switching to new world type
                this.client.openScreen(newWorldProvider.createWorldScreen(
                    this.parent,
                    new WorldSettings(newWorldProvider),
                    this.consumer
                ));
            }
        );
        
        biomeTypeOption = new CyclingOptionWrapper<>(
            "createWorld.customize.biomeType",
            Registries.BIOME.getKeySet().stream().collect(Collectors.toList()),
            NBTUtil.readStringOrThrow(WorldSettings.TAG_BIOME, this.worldSettings.getSettings(WorldSetting.BIOME)),
            value -> {
             // Reset biome settings when switching to new biome type
                CompoundTag biomeProviderSettings = BiomeProviderSettings.createSettingsBase(value, this.worldProvider.getSingleBiome());
                
                this.client.openScreen(
                    this.worldProvider.createWorldScreen(
                        this.parent,
                        new WorldSettings(
                            this.worldSettings.getSettings(WorldSetting.CHUNK), 
                            biomeProviderSettings
                        ),
                        this.consumer
                ));
            }
        );
        
        
        biomeSettingsScreen = Registries.BIOME_SCREEN
            .getOrDefault(NBTUtil.readStringOrThrow(WorldSettings.TAG_BIOME, this.worldSettings.getSettings(WorldSetting.BIOME)))
            .apply(this); 
        
        biomeSettingsOption = new ActionButtonOption(
            biomeType.equals(BuiltInTypes.Biome.SINGLE.name) ? "createWorld.customize.biomeType.biome" : "createWorld.customize.biomeType.settings", // Key
            biomeType.equals(BuiltInTypes.Biome.SINGLE.name) ? GUIUtil.createTranslatableBiomeStringFromId(singleBiome) : "",
            biomeSettingsScreen != null ? widget -> this.client.openScreen(biomeSettingsScreen) : null
        );
        
        this.addButton(doneButton);
        this.addButton(cancelButton);

        this.buttonList.addSingleOptionEntry(worldTypeOption.create());
        this.buttonList.addOptionEntry(biomeTypeOption.create(), biomeSettingsOption);
    }
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float tickDelta) {
        this.renderBackground(matrixStack);
        
        this.buttonList.render(matrixStack, mouseX, mouseY, tickDelta);
        DrawableHelper.drawCenteredText(matrixStack, this.textRenderer, this.title, this.width / 2, 16, 16777215);
        
        super.render(matrixStack, mouseX, mouseY, tickDelta);
        
        // Render tooltips
        List<OrderedText> tooltips = GameOptionsScreen.getHoveredButtonTooltip(this.buttonList, mouseX, mouseY);
        if (tooltips != null) {
            this.renderOrderedTooltip(matrixStack, tooltips, mouseX, mouseY);
        }
    }
    
    protected void setDefaultSingleBiome(String defaultBiome) {
        // Replace default single biome with one supplied by world provider, if switching to Single biome type
        if (NBTUtil.readStringOrThrow(WorldSettings.TAG_BIOME, this.worldSettings.getSettings(WorldSetting.BIOME)).equals(BuiltInTypes.Biome.SINGLE.name))
            this.worldSettings.putSetting(WorldSetting.BIOME, WorldSettings.TAG_SINGLE_BIOME, StringTag.of(defaultBiome));
    }
    
    public DynamicRegistryManager getRegistryManager() {
        return this.registryManager;
    }
    
    public WorldSettings getWorldSettings() {
        return this.worldSettings;
    }
}
