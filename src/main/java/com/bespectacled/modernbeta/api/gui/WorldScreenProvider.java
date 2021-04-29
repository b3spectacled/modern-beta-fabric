package com.bespectacled.modernbeta.api.gui;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.api.registry.ProviderRegistries;
import com.bespectacled.modernbeta.api.world.WorldProvider;
import com.bespectacled.modernbeta.gui.ScreenButtonOption;
import com.bespectacled.modernbeta.util.GUIUtil;
import com.bespectacled.modernbeta.util.NBTUtil;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.OrderedText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.DynamicRegistryManager;

public abstract class WorldScreenProvider extends Screen {
    protected final CreateWorldScreen parent;
    protected final DynamicRegistryManager registryManager;
    protected final NbtCompound biomeProviderSettings;
    protected final NbtCompound chunkProviderSettings;
    protected final BiConsumer<NbtCompound, NbtCompound> consumer;
    
    protected final WorldProvider worldProvider;
    
    protected String biomeType;
    protected String singleBiome;
    
    protected ButtonListWidget buttonList;
    
    public WorldScreenProvider(
        CreateWorldScreen parent, 
        DynamicRegistryManager registryManager, 
        NbtCompound biomeProviderSettings, 
        NbtCompound chunkProviderSettings, 
        BiConsumer<NbtCompound, NbtCompound> consumer
    ) {
        super(new TranslatableText("createWorld.customize.old.title"));
        
        this.parent = parent;
        this.registryManager = registryManager;
        this.biomeProviderSettings = biomeProviderSettings;
        this.chunkProviderSettings = chunkProviderSettings;
        this.consumer = consumer;
        
        // Assume only default chunk and biome provider settings have been supplied at this point.
        this.worldProvider = ProviderRegistries.WORLD.get(NBTUtil.readStringOrThrow("worldType", this.chunkProviderSettings));
        this.biomeType = NBTUtil.readStringOrThrow("biomeType", this.biomeProviderSettings);
        this.singleBiome = this.worldProvider.getDefaultBiome();
    }
    
    /*
     * Note: Remember that this is called every time a screen is switched!
     */
    @Override
    protected void init() {
        this.buttonList = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        
        Function<WorldScreenProvider, Screen> biomeScreenFunction = ProviderRegistries.BIOME_SCREEN.get(this.biomeType);
        Screen biomeScreen = biomeScreenFunction != null ? biomeScreenFunction.apply(this) : null;
        
        ButtonWidget doneButton;
        ButtonWidget cancelButton;
        
        CyclingOption<WorldProvider> worldTypeOption;
        CyclingOption<String> biomeTypeOption;
        
        ScreenButtonOption biomeSettingsOption;
        
        doneButton = new ButtonWidget(
            this.width / 2 - 155, this.height - 28, 150, 20, 
            ScreenTexts.DONE, 
            (buttonWidget) -> {
                // Set generation options to NBT compounds ONLY when user presses Done.
                this.setBiomeProviderSettings();
                this.setChunkProviderSettings();
                
                this.consumer.accept(this.biomeProviderSettings, this.chunkProviderSettings);
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
        
        worldTypeOption = CyclingOption.create(
            "createWorld.customize.worldType", 
            ProviderRegistries.WORLD.getEntries().toArray(WorldProvider[]::new),
            (value) -> new TranslatableText("createWorld.customize.worldType." + value.getChunkProvider()), 
            (gameOptions) -> { return this.worldProvider; }, 
            (gameOptions, option, value) -> {
                
                // Reset settings when switching to new world type
                this.client.openScreen(value.createLevelScreen(
                    this.parent, 
                    this.registryManager,
                    ProviderRegistries.BIOME_SETTINGS.get(value.getDefaultBiomeProviderSettings()).get(),
                    ProviderRegistries.CHUNK_SETTINGS.get(value.getChunkProviderSettings()).get(),
                    this.consumer
                ));
        });
        
        biomeTypeOption = CyclingOption.create(
            "createWorld.customize.biomeType",
            ProviderRegistries.BIOME.getKeys().stream().toArray(String[]::new), 
            (value) -> new TranslatableText("createWorld.customize.biomeType." + value), 
            (gameOptions) -> { return this.biomeType; },
            (gameOptions, option, value) -> {
                
                // Reset biome settings when switching to new biome type
                this.client.openScreen(
                    this.worldProvider.createLevelScreen(
                        this.parent, 
                        this.registryManager, 
                        ProviderRegistries.BIOME_SETTINGS.get(value).get(), 
                        this.chunkProviderSettings, 
                        this.consumer
                ));
            }
        );
        
        biomeSettingsOption = new ScreenButtonOption(
            this.biomeType.equals(BuiltInTypes.Biome.SINGLE.name) ? "createWorld.customize.biomeType.biome" : "createWorld.customize.biomeType.settings", // Key
            this.biomeType.equals(BuiltInTypes.Biome.SINGLE.name) ? GUIUtil.createTranslatableBiomeStringFromId(this.singleBiome) : "",
            buttonWidget -> this.client.openScreen(biomeScreen)
        );
    
        this.addButton(doneButton);
        this.addButton(cancelButton);

        this.buttonList.addSingleOptionEntry(worldTypeOption);
        this.buttonList.addOptionEntry(biomeTypeOption, biomeSettingsOption);
        
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
    
    protected abstract void setChunkProviderSettings();
    
    private void setBiomeProviderSettings() {
        this.biomeProviderSettings.putString("biomeType", this.biomeType);
        this.biomeProviderSettings.putString("singleBiome", this.singleBiome);
    }
    
    public DynamicRegistryManager getRegistryManager() {
        return this.registryManager;
    }
    
    public NbtCompound getBiomeProviderSettings() {
        return (new NbtCompound()).copyFrom(this.biomeProviderSettings);
    }
    
    public void setBiomeProviderSettings(String name, NbtElement element) {
        this.biomeProviderSettings.put(name, element);
    }
    
    public void setBiomeProviderSettings(NbtCompound settings) {
        this.biomeProviderSettings.copyFrom(settings);
    }
    
    public NbtCompound getChunkProviderSettings() {
        return (new NbtCompound()).copyFrom(this.chunkProviderSettings);
    }
    
    public String getSingleBiome() {
        return this.singleBiome;
    }
    
    public void setSingleBiome(String singleBiome) {
        this.singleBiome = singleBiome;
    }
}
