package com.bespectacled.modernbeta.gui;

import java.util.function.Consumer;

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

public abstract class AbstractCustomizeLevelScreen extends Screen {
    private final CreateWorldScreen parent;
    protected final CompoundTag settings;
    protected final WorldType worldType;
    protected final Consumer<CompoundTag> consumer;
    
    protected ButtonListWidget buttonList;
    
    public AbstractCustomizeLevelScreen(CreateWorldScreen parent, CompoundTag settings, Consumer<CompoundTag> consumer) {
        super(new TranslatableText("createWorld.customize.old.title"));
        
        this.parent = parent;
        this.settings = settings;
        this.consumer = consumer;
        
        this.worldType = WorldType.fromName(this.settings.getString("worldType"));
    }
    
    @Override
    protected void init() {
        this.addButton(new ButtonWidget(
            this.width / 2 - 155, this.height - 28, 150, 20, 
            ScreenTexts.DONE, 
            (buttonWidget) -> {
                this.consumer.accept(this.settings);
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
                    CompoundTag newProviderSettings = new CompoundTag();
                    newProviderSettings.putString("worldType", value.getName());
                    
                    this.client.openScreen(value.createLevelScreen(this.parent, newProviderSettings, this.consumer));
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
}
