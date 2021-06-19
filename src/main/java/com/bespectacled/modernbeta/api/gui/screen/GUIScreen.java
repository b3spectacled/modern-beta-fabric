package com.bespectacled.modernbeta.api.gui.screen;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.gui.wrapper.OptionWrapper;
import com.bespectacled.modernbeta.api.world.WorldSettings;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

public abstract class GUIScreen extends Screen {
    protected final Screen parent;
    protected final Consumer<WorldSettings> consumer;
    protected final WorldSettings worldSettings;

    protected ButtonListWidget buttonList;
    
    protected GUIScreen(String title, Screen parent, WorldSettings worldSettings, Consumer<WorldSettings> consumer) {
        super(new TranslatableText(title));
        
        this.parent = parent;
        this.worldSettings = worldSettings;
        this.consumer = consumer;
    }
    
    @Override
    protected void init() {
        this.buttonList = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        this.addSelectableChild(this.buttonList);
        
        ButtonWidget doneButton;
        ButtonWidget cancelButton;
        
        doneButton = new ButtonWidget(
            this.width / 2 - 155, this.height - 28, 150, 20, 
            ScreenTexts.DONE, 
            buttonWidget -> {
                // Apply all settings change only on done!
                this.worldSettings.applyChanges();
                
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
        
        this.addDrawableChild(doneButton);
        this.addDrawableChild(cancelButton);
    }
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float tickDelta) {
        this.renderBackground(matrixStack);
        
        this.buttonList.render(matrixStack, mouseX, mouseY, tickDelta);
        DrawableHelper.drawCenteredText(matrixStack, this.textRenderer, this.title, this.width / 2, 16, 16777215);
        
        super.render(matrixStack, mouseX, mouseY, tickDelta);
    }
    
    public void addOption(OptionWrapper option) {
        this.buttonList.addSingleOptionEntry(option.create());
    }
    
    public void addDualOption(OptionWrapper firstOption, OptionWrapper secondOption) {
        this.buttonList.addOptionEntry(firstOption.create(), secondOption.create());
    }
}
