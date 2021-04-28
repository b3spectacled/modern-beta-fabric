package com.bespectacled.modernbeta.api.gui;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.api.registry.ProviderRegistries;
import com.bespectacled.modernbeta.api.world.WorldProvider;
import com.bespectacled.modernbeta.gui.CyclingOptionWrapper;
import com.bespectacled.modernbeta.gui.ScreenButtonOption;
import com.bespectacled.modernbeta.gui.screen.world.IndevWorldScreenProvider;
import com.bespectacled.modernbeta.util.GUIUtil;
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
import net.minecraft.nbt.Tag;
import net.minecraft.text.OrderedText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.DynamicRegistryManager;

public abstract class AbstractWorldScreenProvider extends Screen {
    protected final CreateWorldScreen parent;
    protected final DynamicRegistryManager registryManager;
    protected final CompoundTag biomeProviderSettings;
    protected final CompoundTag chunkProviderSettings;
    protected final BiConsumer<CompoundTag, CompoundTag> consumer;
    
    protected final String worldProvider;
    
    protected String biomeType;
    protected String caveBiomeType;
    protected String singleBiome;
    
    protected ButtonListWidget buttonList;
    protected ScreenButtonOption biomeSettingsOption;
    
    public AbstractWorldScreenProvider(
        CreateWorldScreen parent, 
        DynamicRegistryManager registryManager, 
        CompoundTag biomeProviderSettings, 
        CompoundTag chunkProviderSettings, 
        BiConsumer<CompoundTag, CompoundTag> consumer
    ) {
        super(new TranslatableText("createWorld.customize.old.title"));
        
        this.parent = parent;
        this.registryManager = registryManager;
        this.biomeProviderSettings = biomeProviderSettings;
        this.chunkProviderSettings = chunkProviderSettings;
        this.consumer = consumer;
        
        this.worldProvider = this.chunkProviderSettings.getString("worldType");
        
        this.biomeType = this.biomeProviderSettings.getString("biomeType");
        this.caveBiomeType = this.biomeProviderSettings.getString("caveBiomeType");
        this.singleBiome = this.biomeProviderSettings.getString("singleBiome");
        
    }
    
    /*
     * Note: Remember that this is called every time a screen is switched!
     */
    @Override
    protected void init() {
        this.addButton(new ButtonWidget(
            this.width / 2 - 155, this.height - 28, 150, 20, 
            ScreenTexts.DONE, 
            (buttonWidget) -> {
                this.consumer.accept(this.biomeProviderSettings, this.chunkProviderSettings);
                this.client.openScreen(this.parent);
            }
        ));

        this.addButton(new ButtonWidget(
            this.width / 2 + 5, this.height - 28, 150, 20, 
            ScreenTexts.CANCEL,
            (buttonWidget) -> {
                this.client.openScreen(this.parent);
            }
        ));
        
        CyclingOptionWrapper<String> worldProviderOption = new CyclingOptionWrapper<>(
            "createWorld.customize.worldType",
            ProviderRegistries.WORLD.getKeys().stream().collect(Collectors.toUnmodifiableList()),
            this.worldProvider,
            value -> {
                WorldProvider worldProvider = ProviderRegistries.WORLD.get(value);
                
                CompoundTag newBiomeProviderSettings = BiomeProviderSettings.createBiomeSettings(worldProvider.getDefaultBiomeProvider(), worldProvider.getDefaultBiome());
                CompoundTag newChunkProviderSettings = ProviderRegistries.CHUNK_SETTINGS.get(worldProvider.getChunkProviderSettings()).get();
                
                this.client.openScreen(worldProvider.createLevelScreen(
                    this.parent, 
                    this.registryManager,
                    newBiomeProviderSettings,
                    newChunkProviderSettings,
                    this.consumer
                ));
            }
        );
        
        Function<AbstractWorldScreenProvider, Screen> biomeScreenFunction = ProviderRegistries.BIOME_SCREEN.get(this.biomeType);
        Screen biomeScreen = biomeScreenFunction != null ? biomeScreenFunction.apply(this) : null;
        
        this.biomeSettingsOption = new ScreenButtonOption(
            this.biomeType.equals(BuiltInTypes.Biome.SINGLE.name) ? "createWorld.customize.biomeType.biome" : "createWorld.customize.biomeType.settings", // Key
            this.biomeType.equals(BuiltInTypes.Biome.SINGLE.name) ? GUIUtil.createTranslatableBiomeStringFromId(this.singleBiome) : "",
            buttonWidget -> this.client.openScreen(biomeScreen)
        );
        
        CyclingOptionWrapper<String> biomeProviderOption = new CyclingOptionWrapper<>(
            "createWorld.customize.biomeType",
            ProviderRegistries.BIOME.getKeys().stream().collect(Collectors.toUnmodifiableList()),
            this.biomeType,
            value -> {
                this.biomeType = value;
                
                WorldProvider worldProvider = ProviderRegistries.WORLD.get(this.worldProvider);
                String defaultBiome = worldProvider.getDefaultBiome();
                
                // Change default biome if on Indev world type
                if (this instanceof IndevWorldScreenProvider) {
                    defaultBiome = ((IndevWorldScreenProvider)this).getTheme().getDefaultBiome().toString();
                }

                CompoundTag newBiomeProviderSettings = BiomeProviderSettings.createBiomeSettings(
                    this.biomeType,
                    defaultBiome
                );
                
                this.client.openScreen(
                    worldProvider.createLevelScreen(
                        this.parent, 
                        this.registryManager, 
                        newBiomeProviderSettings, 
                        this.chunkProviderSettings, 
                        this.consumer
                ));
            }
        );
        
        this.buttonList = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        
        this.buttonList.addSingleOptionEntry(worldProviderOption.create());
        this.buttonList.addOptionEntry(biomeProviderOption.create(), this.biomeSettingsOption);
        
        this.children.add(this.buttonList);
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
    
    public DynamicRegistryManager getRegistryManager() {
        return this.registryManager;
    }
    
    public CompoundTag getBiomeProviderSettings() {
        return (new CompoundTag()).copyFrom(this.biomeProviderSettings);
    }
    
    public void setBiomeProviderSettings(String name, Tag element) {
        this.biomeProviderSettings.put(name, element);
    }
    
    public void setBiomeProviderSettings(CompoundTag settings) {
        this.biomeProviderSettings.copyFrom(settings);
    }
    
    public CompoundTag getChunkProviderSettings() {
        return (new CompoundTag()).copyFrom(this.chunkProviderSettings);
    }
    
    public void setChunkProviderSettings(String name, Tag element) {
        this.chunkProviderSettings.put(name, element);
    }
    
    public void setChunkProviderSettings(CompoundTag settings) {
        this.chunkProviderSettings.copyFrom(settings);
    }
    
    public String getSingleBiome() {
        return this.singleBiome;
    }
    
    public void setSingleBiome(String singleBiome) {
        this.singleBiome = singleBiome;
    }
}
