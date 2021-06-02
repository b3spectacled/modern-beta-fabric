package com.bespectacled.modernbeta.api.gui;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.world.WorldSettings.WorldSetting;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.DynamicRegistryManager;

public abstract class BiomeScreen extends Screen {
    protected final WorldScreen parent;    
    protected final DynamicRegistryManager registryManager;
    protected final CompoundTag parentProviderSettings;
    protected final Consumer<CompoundTag> consumer;
    
    protected CompoundTag biomeProviderSettings;
    
    protected ButtonListWidget buttonList;
    
    protected BiomeScreen(WorldScreen parent, Consumer<CompoundTag> consumer) {
        super(new TranslatableText("createWorld.customize.biomeType.title"));
        
        this.parent = parent;
        this.registryManager = parent.getRegistryManager();
        this.parentProviderSettings = parent.getWorldSettings().getSettings(WorldSetting.BIOME);
        this.consumer = consumer;
        
        // Make copy as to not modify original biome settings (if cancelled)
        this.biomeProviderSettings = new CompoundTag().copyFrom(parentProviderSettings);
    }
    
    @Override
    protected void init() {
        this.buttonList = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        this.children.add(this.buttonList);

        ButtonWidget doneButton;
        ButtonWidget cancelButton;
        
        doneButton = new ButtonWidget(
            this.width / 2 - 155, this.height - 28, 150, 20, 
            ScreenTexts.DONE, 
            (buttonWidget) -> {
                this.consumer.accept(this.biomeProviderSettings);
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
        
        this.addButton(doneButton);
        this.addButton(cancelButton);
    }
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float tickDelta) {
        this.renderBackground(matrixStack);
        
        this.buttonList.render(matrixStack, mouseX, mouseY, tickDelta);
        DrawableHelper.drawCenteredText(matrixStack, this.textRenderer, this.title, this.width / 2, 16, 16777215);
        
        super.render(matrixStack, mouseX, mouseY, tickDelta);
    }
}   


