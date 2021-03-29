package com.bespectacled.modernbeta.api;

import java.util.List;
import java.util.function.BiConsumer;

import com.bespectacled.modernbeta.gen.OldGeneratorSettings;
import com.bespectacled.modernbeta.gui.ScreenButtonOption;

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
import net.minecraft.text.OrderedText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.DynamicRegistryManager;

public abstract class AbstractScreenProvider extends Screen {
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
    
    public AbstractScreenProvider(
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
        
        this.worldProvider = WorldProviderType.getWorldProvider(this.chunkProviderSettings.getString("worldType"));
        
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
                WorldProviderType.getWorldProviders().toArray(WorldProvider[]::new),
                (value) -> new TranslatableText("createWorld.customize.worldType." + value.getName()), 
                (gameOptions) -> { return this.worldProvider; }, 
                (gameOptions, option, value) -> {
                    NbtCompound newBiomeProviderSettings = OldGeneratorSettings.createBiomeSettings(value.getDefaultBiomeProvider(), value.getDefaultCaveBiomeProvider(), value.getDefaultBiome());
                    NbtCompound newChunkProviderSettings = value.getName() == ChunkProviderType.INDEV ? OldGeneratorSettings.createIndevSettings() : OldGeneratorSettings.createInfSettings(value.getName());
                    
                    this.client.openScreen(value.createLevelScreen(
                        this.parent, 
                        this.registryManager,
                        newBiomeProviderSettings,
                        newChunkProviderSettings,
                        this.consumer
                    ));
            })
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
    
    protected abstract void updateButtonActive(ScreenButtonOption option);
}
