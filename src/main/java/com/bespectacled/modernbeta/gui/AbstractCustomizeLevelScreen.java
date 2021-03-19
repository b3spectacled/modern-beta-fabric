package com.bespectacled.modernbeta.gui;

import java.util.function.BiConsumer;
import com.bespectacled.modernbeta.gen.WorldType;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.DynamicRegistryManager;

public abstract class AbstractCustomizeLevelScreen extends Screen {
    private final CreateWorldScreen parent;
    protected final DynamicRegistryManager registryManager;
    protected final CompoundTag biomeProviderSettings;
    protected final CompoundTag chunkProviderSettings;
    protected final BiConsumer<CompoundTag, CompoundTag> consumer;
    
    protected final WorldType worldType;
    
    protected ButtonListWidget buttonList;
    protected ButtonWidget buttonSingleBiome;
    
    protected ScreenButtonOption biomeButton;
    
    public AbstractCustomizeLevelScreen(
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
        
        this.worldType = WorldType.fromName(this.chunkProviderSettings.getString("worldType"));
    }
    
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
                WorldType.values(),
                (value) -> new TranslatableText("createWorld.customize.worldType." + value.getName()), 
                (gameOptions) -> { return this.worldType; }, // Sets default value?
                (gameOptions, option, value) -> {
                    CompoundTag newBiomeProviderSettings = new CompoundTag();
                    CompoundTag newChunkProviderSettings = new CompoundTag();
                    newChunkProviderSettings.putString("worldType", value.getName());
                    
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
    }
    
    protected abstract void setSingleBiomeButtonVisibility();
}
