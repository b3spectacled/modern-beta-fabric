package com.bespectacled.modernbeta.gui;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.gen.settings.AlphaGeneratorSettings;
import com.bespectacled.modernbeta.gen.settings.InfdevGeneratorSettings;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.BooleanOption;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.gen.GeneratorOptions;

public class CustomizeInfdevLevelScreen extends Screen {
    private CreateWorldScreen parent;
    private InfdevGeneratorSettings generatorSettings;
    
    private boolean infdevWinterMode = ModernBeta.BETA_CONFIG.infdevWinterMode;
    private boolean infdevPlus = ModernBeta.BETA_CONFIG.infdevPlus;
    private boolean generateVanillaBiomesInfdev = ModernBeta.BETA_CONFIG.generateVanillaBiomesInfdev;
    
    private ButtonListWidget buttonList;

    public CustomizeInfdevLevelScreen(CreateWorldScreen parent, InfdevGeneratorSettings generatorSettings) {
        super(new TranslatableText("createWorld.customize.infdev.title"));
        
        this.parent = parent;
        this.generatorSettings = generatorSettings;
        
        if (generatorSettings.settings.contains("infdevWinterMode"))
            infdevWinterMode = generatorSettings.settings.getBoolean("infdevWinterMode");
        if (generatorSettings.settings.contains("infdevPlus"))
            infdevPlus = generatorSettings.settings.getBoolean("infdevPlus");
        if (generatorSettings.settings.contains("generateVanillaBiomesInfdev"))
            generateVanillaBiomesInfdev = generatorSettings.settings.getBoolean("generateVanillaBiomesInfdev");
      
    }
    
    @Override
    protected void init() {
        this.addButton(new ButtonWidget(
            this.width / 2 - 155, this.height - 28, 150, 20, 
            ScreenTexts.DONE, 
            (buttonWidget) -> {
                this.client.openScreen(this.parent);
                return;
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
            new BooleanOption(
                "createWorld.customize.infdev.infdevWinterMode", 
                (gameOptions) -> { return infdevWinterMode; }, // Getter
                (gameOptions, value) -> { // Setter
                    infdevWinterMode = value;
                    generatorSettings.settings.putBoolean("infdevWinterMode", value);
                }
        ));
        
        this.buttonList.addSingleOptionEntry(
            new BooleanOption(
                "createWorld.customize.infdev.infdevPlus", 
                (gameOptions) -> { return infdevPlus; }, 
                (gameOptions, value) -> {
                    infdevPlus = value;
                    generatorSettings.settings.putBoolean("infdevPlus", value);
                }
        ));
        
        this.buttonList.addSingleOptionEntry(
            new BooleanOption(
                "createWorld.customize.infdev.generateVanillaBiomesInfdev", 
                (gameOptions) -> { return generateVanillaBiomesInfdev; }, 
                (gameOptions, value) -> {
                    generateVanillaBiomesInfdev = value;
                    generatorSettings.settings.putBoolean("generateVanillaBiomesInfdev", value);
                }
        ));
        
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
