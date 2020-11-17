package com.bespectacled.modernbeta.gui;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.gen.settings.OldGeneratorSettings;
import com.bespectacled.modernbeta.util.WorldEnum;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.CyclingOption;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class CustomizeAlphaLevelScreen extends Screen {
    private CreateWorldScreen parent;
    private OldGeneratorSettings generatorSettings;
    
    private int biomeType = ModernBeta.BETA_CONFIG.alphaBiomeType;
    
    private ButtonListWidget buttonList;
    
    private final Text textBiomeType = new TranslatableText("createWorld.customize.type.biomeType");
    private final Text textClassic = new TranslatableText("createWorld.customize.type.classic");
    private final Text textWinter = new TranslatableText("createWorld.customize.type.winter");
    private final Text textPlus = new TranslatableText("createWorld.customize.type.plus");
    private final Text textVanilla = new TranslatableText("createWorld.customize.type.vanilla");

    public CustomizeAlphaLevelScreen(CreateWorldScreen parent, OldGeneratorSettings generatorSettings) {
        super(new TranslatableText("createWorld.customize.alpha.title"));
        
        this.parent = parent;
        this.generatorSettings = generatorSettings;
        
        if (generatorSettings.settings.contains("alphaBiomeType"))
            this.biomeType = generatorSettings.settings.getInt("alphaBiomeType");
      
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
            new CyclingOption(
                "createWorld.customize.alpha.typeButton",
                (gameOptions, value) -> {
                    this.biomeType++;
                    if (this.biomeType > WorldEnum.PreBetaBiomeType.values().length - 1) this.biomeType = 0;
                    generatorSettings.settings.putInt("alphaBiomeType", this.biomeType);
                    
                    return;
                },
                (gameOptions, cyclingOptions) -> {
                    Text typeText = textClassic;
                    
                    switch(this.biomeType) {
                        case 0:
                            typeText = textClassic;
                            break;
                        case 1:
                            typeText = textWinter;
                            break;
                        case 2:
                            typeText = textPlus;
                            break;
                        case 3:
                            typeText = textVanilla;
                            break;
                    }
                    
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            textBiomeType, 
                            typeText
                    });
                }
        ));
        
        /*
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
        */
        
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
