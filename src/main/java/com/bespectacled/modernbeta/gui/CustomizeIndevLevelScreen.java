package com.bespectacled.modernbeta.gui;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.gen.settings.IndevGeneratorSettings;
import com.bespectacled.modernbeta.util.IndevUtil.Theme;
import com.bespectacled.modernbeta.util.IndevUtil.Type;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.options.BooleanOption;
import net.minecraft.client.options.CyclingOption;
import net.minecraft.client.options.DoubleOption;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class CustomizeIndevLevelScreen extends Screen {
    private CreateWorldScreen parent;
    private IndevGeneratorSettings generatorSettings;
    
    private boolean alphaWinterMode = false;
    private boolean alphaPlus = false;
    
    private int levelType = ModernBeta.BETA_CONFIG.indevLevelType;
    private int levelTheme = ModernBeta.BETA_CONFIG.indevLevelTheme;
   
    private int levelWidth = ModernBeta.BETA_CONFIG.indevLevelWidth;
    private int levelLength = ModernBeta.BETA_CONFIG.indevLevelLength;
    private int levelHeight = ModernBeta.BETA_CONFIG.indevLevelHeight;
    
    private float caveRadius = ModernBeta.BETA_CONFIG.indevCaveRadius;
    
    private ButtonListWidget buttonList;
   
    private final Text themeNormal = new TranslatableText("createWorld.customize.indev.theme.normal");
    private final Text themeHell = new TranslatableText("createWorld.customize.indev.theme.hell");
    private final Text themeParadise = new TranslatableText("createWorld.customize.indev.theme.paradise");
    private final Text themeWoods = new TranslatableText("createWorld.customize.indev.theme.woods");
    private final Text themeSnowy = new TranslatableText("createWorld.customize.indev.theme.snowy");
    
    private final Text typeIsland = new TranslatableText("createWorld.customize.indev.type.island");
    private final Text typeFloating = new TranslatableText("createWorld.customize.indev.type.floating");
    private final Text typeInland = new TranslatableText("createWorld.customize.indev.type.inland");

    public CustomizeIndevLevelScreen(CreateWorldScreen parent, IndevGeneratorSettings generatorSettings) {
        super(new TranslatableText("createWorld.customize.indev.title"));
        
        this.parent = parent;
        this.generatorSettings = generatorSettings;
        
        if (generatorSettings.settings.contains("levelType")) this.levelType = generatorSettings.settings.getInt("levelType");
        if (generatorSettings.settings.contains("levelTheme")) this.levelTheme = generatorSettings.settings.getInt("levelTheme");
        
        if (generatorSettings.settings.contains("levelWidth")) this.levelWidth = generatorSettings.settings.getInt("levelWidth");
        if (generatorSettings.settings.contains("levelLength")) this.levelLength = generatorSettings.settings.getInt("levelLength");
        if (generatorSettings.settings.contains("levelHeight")) this.levelHeight = generatorSettings.settings.getInt("levelHeight");
        if (generatorSettings.settings.contains("caveRadius")) this.caveRadius = generatorSettings.settings.getFloat("caveRadius");
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
                "createWorld.customize.indev.typeButton",
                (gameOptions, value) -> {
                    this.levelType++;
                    if (this.levelType > 2) this.levelType = 0;
                    generatorSettings.settings.putInt("levelType", this.levelType);
                    
                    return;
                },
                (gameOptions, cyclingOptions) -> {
                    Text type = typeIsland;
                    
                    switch(this.levelType) {
                        case 0:
                            type = typeIsland;
                            break;
                        case 1:
                            type = typeFloating;
                            break;
                        case 2:
                            type = typeInland;
                            break;
                    }
                    
                    return new TranslatableText(
                            "options.generic_value", 
                            new Object[] { 
                                new TranslatableText("createWorld.customize.indev.levelType"), 
                                type
                        });
                }
        ));
        
        this.buttonList.addSingleOptionEntry(
                new CyclingOption(
                    "createWorld.customize.indev.themeButton",
                    (gameOptions, value) -> {
                        this.levelTheme++;
                        if (this.levelTheme > 4) this.levelTheme = 0;
                        generatorSettings.settings.putInt("levelTheme", this.levelTheme);
                        
                        return;
                    },
                    (gameOptions, cyclingOptions) -> {
                        Text theme = themeNormal;
                        
                        switch(this.levelTheme) {
                            case 0:
                                theme = themeNormal;
                                break;
                            case 1:
                                theme = themeHell;
                                break;
                            case 2:
                                theme = themeParadise;
                                break;
                            case 3:
                                theme = themeWoods;
                                break;
                            case 4:
                                theme = themeSnowy;
                                break;
                        }
                        
                        return new TranslatableText(
                            "options.generic_value", 
                            new Object[] { 
                                new TranslatableText("createWorld.customize.indev.levelTheme"), 
                                theme 
                        });
                    }
            ));
        
        
        this.buttonList.addSingleOptionEntry(
            new DoubleOption(
                "createWorld.customize.indev.widthSlider", 
                128D, 1024D, 128f,
                (gameOptions) -> { return (double) this.levelWidth; }, // Getter
                (gameOptions, value) -> { // Setter
                    this.levelWidth = value.intValue();
                    generatorSettings.settings.putInt("levelWidth", value.intValue());
                    return;
                },
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.indev.levelWidth"), 
                            Text.of(String.valueOf(this.levelWidth)) 
                    });
                }
        ));
        
        this.buttonList.addSingleOptionEntry(
            new DoubleOption(
                "createWorld.customize.indev.lengthSlider", 
                128D, 1024D, 128f,
                (gameOptions) -> { return (double) this.levelLength; }, // Getter
                (gameOptions, value) -> { // Setter
                    this.levelLength = value.intValue();
                    generatorSettings.settings.putInt("levelLength", value.intValue());
                    return;
                },
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.indev.levelLength"), 
                            Text.of(String.valueOf(this.levelLength)) 
                    });
                }
        ));
        
        this.buttonList.addSingleOptionEntry(
            new DoubleOption(
                "createWorld.customize.indev.heightSlider", 
                64D, 256D, 64F,
                (gameOptions) -> { return (double) this.levelHeight; }, // Getter
                (gameOptions, value) -> { // Setter
                    this.levelHeight = value.intValue();
                    generatorSettings.settings.putInt("levelHeight", value.intValue());
                    return;
                },
                (gameOptions, doubleOptions) -> {
                    int seaLevel = this.levelHeight / 2;
                    String heightString = String.valueOf(this.levelHeight) + " (Sea Level: " + String.valueOf(seaLevel) + ")";
                    
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.indev.levelHeight"), 
                            Text.of(heightString) 
                    });
                }
        ));
        
        this.buttonList.addSingleOptionEntry(
                new DoubleOption(
                    "createWorld.customize.indev.caveRadiusSlider", 
                    1D, 3D, 0.1f,
                    (gameOptions) -> { return (double) this.caveRadius; }, // Getter
                    (gameOptions, value) -> { // Setter
                        this.caveRadius = value.floatValue();
                        generatorSettings.settings.putFloat("caveRadius", value.floatValue());
                        return;
                    },
                    (gameOptions, doubleOptions) -> {
                        return new TranslatableText(
                            "options.generic_value", 
                            new Object[] { 
                                new TranslatableText("createWorld.customize.indev.caveRadius"), 
                                Text.of(String.format("%.01f", this.caveRadius)) 
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
