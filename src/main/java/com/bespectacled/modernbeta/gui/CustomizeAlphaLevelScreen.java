package com.bespectacled.modernbeta.gui;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.gen.settings.OldGeneratorSettings;
import com.bespectacled.modernbeta.util.GUIUtil;
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
    
    private int biomeType = ModernBeta.BETA_CONFIG.preBetaBiomeType;
    
    private ButtonListWidget buttonList;

    public CustomizeAlphaLevelScreen(CreateWorldScreen parent, OldGeneratorSettings generatorSettings) {
        super(new TranslatableText("createWorld.customize.alpha.title"));
        
        this.parent = parent;
        this.generatorSettings = generatorSettings;
        
        if (generatorSettings.settings.contains("preBetaBiomeType"))
            this.biomeType = generatorSettings.settings.getInt("preBetaBiomeType");
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
                "createWorld.customize.preBeta.typeButton",
                (gameOptions, value) -> {
                    this.biomeType++;
                    if (this.biomeType > WorldEnum.PreBetaBiomeType.values().length - 1) this.biomeType = 0;
                    generatorSettings.settings.putInt("preBetaBiomeType", this.biomeType);
                    
                    return;
                },
                (gameOptions, cyclingOptions) -> {
                    Text typeText = GUIUtil.TEXT_CLASSIC;
                    
                    switch(this.biomeType) {
                        case 0:
                            typeText = GUIUtil.TEXT_CLASSIC;
                            break;
                        case 1:
                            typeText = GUIUtil.TEXT_WINTER;
                            break;
                        case 2:
                            typeText = GUIUtil.TEXT_PLUS;
                            break;
                        case 3:
                            typeText = GUIUtil.TEXT_VANILLA;
                            break;
                    }
                    
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            GUIUtil.TEXT_BIOME_TYPE, 
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
