package com.bespectacled.modernbeta.api;

import java.util.List;
import java.util.function.BiConsumer;

import com.bespectacled.modernbeta.api.registry.BiomeProviderRegistry;
import com.bespectacled.modernbeta.api.registry.ChunkProviderSettingsRegistry;
import com.bespectacled.modernbeta.api.registry.ScreenPressActionRegistry;
import com.bespectacled.modernbeta.api.registry.WorldProviderRegistry;
import com.bespectacled.modernbeta.api.registry.BiomeProviderRegistry.BuiltInBiomeType;
import com.bespectacled.modernbeta.gui.ScreenButtonOption;
import com.bespectacled.modernbeta.gui.provider.IndevLevelScreenProvider;
import com.bespectacled.modernbeta.util.GUIUtil;
import com.bespectacled.modernbeta.world.biome.provider.settings.BiomeProviderSettings;

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

public abstract class AbstractLevelScreenProvider extends Screen {
    protected final CreateWorldScreen parent;
    protected final DynamicRegistryManager registryManager;
    protected final NbtCompound biomeProviderSettings;
    protected final NbtCompound chunkProviderSettings;
    protected final BiConsumer<NbtCompound, NbtCompound> consumer;
    
    protected final WorldProvider worldProvider;
    
    protected String biomeType;
    protected String caveBiomeType;
    protected String singleBiome;
    
    protected ButtonListWidget buttonList;
    protected ScreenButtonOption biomeOption;
    
    public AbstractLevelScreenProvider(
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
        
        this.worldProvider = WorldProviderRegistry.get(this.chunkProviderSettings.getString("worldType"));
        
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
        
        this.buttonList = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        
        this.buttonList.addSingleOptionEntry(
            CyclingOption.create(
                "createWorld.customize.worldType", 
                WorldProviderRegistry.getWorldProviders().stream().toArray(WorldProvider[]::new),
                (value) -> new TranslatableText("createWorld.customize.worldType." + value.getName()), 
                (gameOptions) -> { return this.worldProvider; }, 
                (gameOptions, option, value) -> {
                    NbtCompound newBiomeProviderSettings = BiomeProviderSettings.createBiomeSettings(value.getDefaultBiomeProvider(), value.getDefaultCaveBiomeProvider(), value.getDefaultBiome());
                    NbtCompound newChunkProviderSettings = ChunkProviderSettingsRegistry.get(value.getChunkProviderSettings()).get();
                    
                    this.client.openScreen(value.createLevelScreen(
                        this.parent, 
                        this.registryManager,
                        newBiomeProviderSettings,
                        newChunkProviderSettings,
                        this.consumer
                    ));
            })
        );
        
        // Get biome type list, sans legacy types
        String[] biomeProviderTypes = BiomeProviderRegistry
            .getBiomeProviderKeys()
            .stream()
            .filter(str -> !BiomeProviderRegistry.getLegacyTypes().contains(str))
            .toArray(String[]::new);
        
        this.biomeOption = new ScreenButtonOption(
            this.biomeType.equals(BuiltInBiomeType.SINGLE.id) ? "createWorld.customize.biomeType.biome" : "createWorld.customize.biomeType.settings", // Key
            this.biomeType.equals(BuiltInBiomeType.SINGLE.id) ? GUIUtil.createTranslatableBiomeStringFromId(this.singleBiome) : "",
            ScreenPressActionRegistry.get(this.biomeType).apply(this)
        );
        
        this.buttonList.addOptionEntry(
            CyclingOption.create(
                "createWorld.customize.biomeType",
                biomeProviderTypes, 
                (value) -> new TranslatableText("createWorld.customize.biomeType." + value), 
                (gameOptions) -> { return this.biomeType; },
                (gameOptions, option, value) -> {
                    this.biomeType = value;
                    
                    String defaultBiome = this.worldProvider.getDefaultBiome();
                    
                    // Change default biome if on Indev world type
                    if (this instanceof IndevLevelScreenProvider) {
                        defaultBiome = ((IndevLevelScreenProvider)this).getTheme().getDefaultBiome().toString();
                    }

                    NbtCompound newBiomeProviderSettings = BiomeProviderSettings.createBiomeSettings(
                        this.biomeType, 
                        this.caveBiomeType, 
                        defaultBiome
                    );
                    
                    this.client.openScreen(
                        this.worldProvider.createLevelScreen(
                            this.parent, 
                            this.registryManager, 
                            newBiomeProviderSettings, 
                            this.chunkProviderSettings, 
                            this.consumer
                    ));
                }
            ),
            this.biomeOption
        );
       
        
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
    
    public void setChunkProviderSettings(String name, NbtElement element) {
        this.chunkProviderSettings.put(name, element);
    }
    
    public void setChunkProviderSettings(NbtCompound settings) {
        this.chunkProviderSettings.copyFrom(settings);
    }
    
    public String getSingleBiome() {
        return this.singleBiome;
    }
    
    public void setSingleBiome(String singleBiome) {
        this.singleBiome = singleBiome;
    }
}
