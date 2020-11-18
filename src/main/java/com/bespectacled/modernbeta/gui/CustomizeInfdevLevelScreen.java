package com.bespectacled.modernbeta.gui;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.gen.settings.OldGeneratorSettings;
import com.bespectacled.modernbeta.util.GUIUtil;
import com.bespectacled.modernbeta.util.WorldEnum;
import com.bespectacled.modernbeta.util.WorldEnum.PreBetaBiomeType;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.BooleanOption;
import net.minecraft.client.options.CyclingOption;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class CustomizeInfdevLevelScreen extends Screen {
    private CreateWorldScreen parent;
    private OldGeneratorSettings generatorSettings;
    
    private int biomeType = WorldEnum.PreBetaBiomeType.fromName(ModernBeta.BETA_CONFIG.preBetaBiomeType).getId();
    
    private ButtonListWidget buttonList;

    public CustomizeInfdevLevelScreen(CreateWorldScreen parent, OldGeneratorSettings generatorSettings) {
        super(new TranslatableText("createWorld.customize.infdev.title"));
        
        this.parent = parent;
        this.generatorSettings = generatorSettings;
        
        if (generatorSettings.settings.contains("infdevBiomeType"))
            this.biomeType = generatorSettings.settings.getInt("infdevBiomeType");
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
                        generatorSettings.settings.putString("preBetaBiomeType", PreBetaBiomeType.fromId(this.biomeType).getName());
                        
                        return;
                    },
                    (gameOptions, cyclingOptions) -> {
                        Text typeText = GUIUtil.TEXT_CLASSIC;
                        PreBetaBiomeType type = PreBetaBiomeType.fromId(this.biomeType);
                        
                        switch(type) {
                            case CLASSIC:
                                typeText = GUIUtil.TEXT_CLASSIC;
                                break;
                            case WINTER:
                                typeText = GUIUtil.TEXT_WINTER;
                                break;
                            case PLUS:
                                typeText = GUIUtil.TEXT_PLUS;
                                break;
                            case VANILLA:
                                typeText = GUIUtil.TEXT_VANILLA;
                                break;
                            default:
                                typeText = GUIUtil.TEXT_UNKNOWN;
                        }
                        
                        return new TranslatableText(
                            "options.generic_value", 
                            new Object[] { 
                                GUIUtil.TEXT_BIOME_TYPE, 
                                typeText
                        });
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
