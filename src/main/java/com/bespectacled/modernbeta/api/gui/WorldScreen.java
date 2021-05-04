package com.bespectacled.modernbeta.api.gui;

import java.util.List;
import java.util.function.BiConsumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.api.registry.ProviderRegistries;
import com.bespectacled.modernbeta.api.world.WorldProvider;
import com.bespectacled.modernbeta.gui.ActionButtonOption;
import com.bespectacled.modernbeta.util.GUIUtil;
import com.bespectacled.modernbeta.util.NBTUtil;
import com.bespectacled.modernbeta.world.biome.provider.settings.BiomeProviderSettings;
import com.bespectacled.modernbeta.world.gen.provider.settings.ChunkProviderSettings;

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

public abstract class WorldScreen extends Screen {
    protected final CreateWorldScreen parent;
    protected final DynamicRegistryManager registryManager;
    protected final WorldProvider worldProvider;
    protected final BiConsumer<NbtCompound, NbtCompound> consumer;
    
    protected final NbtCompound biomeProviderSettings;
    protected final NbtCompound chunkProviderSettings;
    
    protected ButtonListWidget buttonList;
    
    public WorldScreen(
        CreateWorldScreen parent, 
        DynamicRegistryManager registryManager,
        NbtCompound chunkProviderSettings,
        NbtCompound biomeProviderSettings,
        BiConsumer<NbtCompound, NbtCompound> consumer       
    ) {
        super(new TranslatableText("createWorld.customize.worldType.title"));
        
        this.parent = parent;
        this.registryManager = registryManager;
        this.worldProvider = ProviderRegistries.WORLD.get(NBTUtil.readStringOrThrow("worldType", chunkProviderSettings));
        this.chunkProviderSettings = chunkProviderSettings;
        this.biomeProviderSettings = biomeProviderSettings;
        this.consumer = consumer;
    }
    
    @Override
    protected void init() {
        this.buttonList = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        this.children.add(this.buttonList);
        
        String biomeType = NBTUtil.readStringOrThrow("biomeType", this.biomeProviderSettings);
        String singleBiome = NBTUtil.readString("singleBiome", this.biomeProviderSettings, ModernBeta.BIOME_CONFIG.singleBiome);

        ButtonWidget doneButton;
        ButtonWidget cancelButton;
        
        CyclingOption<WorldProvider> worldTypeOption;
        CyclingOption<String> biomeTypeOption;
        
        Screen biomeSettingsScreen;
        ActionButtonOption biomeSettingsOption;
        
        doneButton = new ButtonWidget(
            this.width / 2 - 155, this.height - 28, 150, 20, 
            ScreenTexts.DONE, 
            (buttonWidget) -> {
                this.consumer.accept(this.chunkProviderSettings, this.biomeProviderSettings);
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
            ProviderRegistries.WORLD.getEntries().stream().toArray(WorldProvider[]::new),
            (value) -> new TranslatableText("createWorld.customize.worldType." + value.getChunkProvider()), 
            (gameOptions) -> { return this.worldProvider; }, 
            (gameOptions, option, value) -> {
                // Reset settings when switching to new world type
                /*
                NbtCompound newBiomeProviderSettings = ProviderRegistries.BIOME_SETTINGS
                    .getOrDefault(value.getDefaultBiomeProviderSettings())
                    .apply(value.getDefaultBiomeProvider());
                
                NbtCompound newChunkProviderSettings = ProviderRegistries.CHUNK_SETTINGS
                    .getOrDefault(value.getChunkProviderSettings())
                    .apply(value.getChunkProvider());
                    */
                NbtCompound newChunkProviderSettings = ChunkProviderSettings.createSettingsBase(value.getChunkProvider());
                NbtCompound newBiomeProviderSettings = BiomeProviderSettings.createSettingsBase(value.getDefaultBiomeProvider());
                
                this.setDefaultSingleBiome(newBiomeProviderSettings, value.getDefaultBiome());
                
                this.client.openScreen(value.createLevelScreen(
                    this.parent, 
                    this.registryManager,
                    newChunkProviderSettings,
                    newBiomeProviderSettings,
                    this.consumer
                ));
        });
        
        biomeTypeOption = CyclingOption.create(
            "createWorld.customize.biomeType",
            ProviderRegistries.BIOME.getKeys().stream().toArray(String[]::new), 
            (value) -> new TranslatableText("createWorld.customize.biomeType." + value), 
            (gameOptions) -> NBTUtil.readStringOrThrow("biomeType", this.biomeProviderSettings),
            (gameOptions, option, value) -> {
                // Reset biome settings when switching to new biome type
                /*
                NbtCompound newBiomeProviderSettings = ProviderRegistries.BIOME_SETTINGS
                    .getOrDefault(value)
                    .apply(value);
                    */

                NbtCompound newBiomeProviderSettings = BiomeProviderSettings.createSettingsBase(value);
                this.setDefaultSingleBiome(newBiomeProviderSettings, this.worldProvider.getDefaultBiome());                
                
                this.client.openScreen(
                    this.worldProvider.createLevelScreen(
                        this.parent, 
                        this.registryManager,
                        this.chunkProviderSettings, 
                        newBiomeProviderSettings,
                        this.consumer
                ));
            }
        );
        
        biomeSettingsScreen = ProviderRegistries.BIOME_SCREEN
            .getOrDefault(NBTUtil.readStringOrThrow("biomeType", this.biomeProviderSettings))
            .apply(this); 
        
        biomeSettingsOption = new ActionButtonOption(
            biomeType.equals(BuiltInTypes.Biome.SINGLE.name) ? "createWorld.customize.biomeType.biome" : "createWorld.customize.biomeType.settings", // Key
            biomeType.equals(BuiltInTypes.Biome.SINGLE.name) ? GUIUtil.createTranslatableBiomeStringFromId(singleBiome) : "",
            biomeSettingsScreen != null ? widget -> this.client.openScreen(biomeSettingsScreen) : null
        );
        
        this.addButton(doneButton);
        this.addButton(cancelButton);

        this.buttonList.addSingleOptionEntry(worldTypeOption);
        this.buttonList.addOptionEntry(biomeTypeOption, biomeSettingsOption);
    }
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float tickDelta) {
        this.renderBackground(matrixStack);
        
        this.buttonList.render(matrixStack, mouseX, mouseY, tickDelta);
        DrawableHelper.drawCenteredText(matrixStack, this.textRenderer, this.title, this.width / 2, 16, 16777215);
        
        super.render(matrixStack, mouseX, mouseY, tickDelta);
        
        // Render tooltips, if present
        List<OrderedText> tooltips = GameOptionsScreen.getHoveredButtonTooltip(this.buttonList, mouseX, mouseY);
        if (tooltips != null) {
            this.renderOrderedTooltip(matrixStack, tooltips, mouseX, mouseY);
        }
    }
    
    protected void setDefaultSingleBiome(NbtCompound biomeProviderSettings, String defaultBiome) {
     // Replace default single biome with one supplied by world provider, if switching to Single biome type
        if (NBTUtil.readStringOrThrow("biomeType", biomeProviderSettings).equals(BuiltInTypes.Biome.SINGLE.name))
            biomeProviderSettings.putString("singleBiome", defaultBiome);
    }
    
    public DynamicRegistryManager getRegistryManager() {
        return this.registryManager;
    }
    
    public NbtCompound getBiomeProviderSettings() {
        return new NbtCompound().copyFrom(this.biomeProviderSettings);
    }
    
    public void setBiomeProviderSettings(NbtCompound newBiomeProviderSettings) {
        this.biomeProviderSettings.copyFrom(newBiomeProviderSettings);
    }
    
    public void setBiomeProviderSettings(String key, NbtElement element) {
        this.biomeProviderSettings.put(key, element);
    }
}
