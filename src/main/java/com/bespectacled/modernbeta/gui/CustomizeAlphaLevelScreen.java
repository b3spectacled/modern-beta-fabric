package com.bespectacled.modernbeta.gui;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.gen.settings.AlphaGeneratorSettings;

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

public class CustomizeAlphaLevelScreen extends Screen {
    private CreateWorldScreen parent;
    private AlphaGeneratorSettings generatorSettings;
    
    private boolean alphaWinterMode = ModernBeta.BETA_CONFIG.alphaWinterMode;
    private boolean alphaPlus = ModernBeta.BETA_CONFIG.alphaPlus;
    private boolean generateVanillaBiomesAlpha = ModernBeta.BETA_CONFIG.generateVanillaBiomesAlpha;
    
    private ButtonListWidget buttonList;

    public CustomizeAlphaLevelScreen(CreateWorldScreen parent, AlphaGeneratorSettings generatorSettings) {
        super(new TranslatableText("createWorld.customize.alpha.title"));
        
        this.parent = parent;
        this.generatorSettings = generatorSettings;
        
        if (generatorSettings.settings.contains("alphaWinterMode"))
            alphaWinterMode = generatorSettings.settings.getBoolean("alphaWinterMode");
        if (generatorSettings.settings.contains("alphaPlus"))
            alphaPlus = generatorSettings.settings.getBoolean("alphaPlus");
        if (generatorSettings.settings.contains("generateVanillaBiomesAlpha"))
            generateVanillaBiomesAlpha = generatorSettings.settings.getBoolean("generateVanillaBiomesAlpha");
      
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
                "createWorld.customize.alpha.alphaWinterMode", 
                (gameOptions) -> { return alphaWinterMode; }, // Getter
                (gameOptions, value) -> { // Setter
                    alphaWinterMode = value;
                    generatorSettings.settings.putBoolean("alphaWinterMode", value);
                }
        ));
        
        this.buttonList.addSingleOptionEntry(
            new BooleanOption(
                "createWorld.customize.alpha.alphaPlus", 
                (gameOptions) -> { return alphaPlus; }, 
                (gameOptions, value) -> {
                    alphaPlus = value;
                    generatorSettings.settings.putBoolean("alphaPlus", value);
                }
        ));
        
        this.buttonList.addSingleOptionEntry(
            new BooleanOption(
                "createWorld.customize.alpha.generateVanillaBiomesAlpha", 
                (gameOptions) -> { return generateVanillaBiomesAlpha; }, 
                (gameOptions, value) -> {
                    generateVanillaBiomesAlpha = value;
                    generatorSettings.settings.putBoolean("generateVanillaBiomesAlpha", value);
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
