package com.bespectacled.modernbeta.gui;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.indev.IndevUtil;
import com.bespectacled.modernbeta.biome.indev.IndevUtil.IndevTheme;
import com.bespectacled.modernbeta.biome.indev.IndevUtil.IndevType;
import com.bespectacled.modernbeta.gen.OldGeneratorSettings;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.options.CyclingOption;
import net.minecraft.client.options.DoubleOption;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class IndevCustomizeLevelScreen extends AbstractCustomizeLevelScreen {
    private int levelType;
    private int levelTheme;

    private int levelWidth;
    private int levelLength;
    private int levelHeight;
    
    private float caveRadius;
    
    public IndevCustomizeLevelScreen(CreateWorldScreen parent, OldGeneratorSettings generatorSettings, String title) {
        super(parent, generatorSettings, title);
        
        this.levelType = IndevUtil.IndevType.fromString(ModernBeta.BETA_CONFIG.indevLevelType).getId();
        this.levelTheme = IndevUtil.IndevTheme.fromString(ModernBeta.BETA_CONFIG.indevLevelTheme).getId();

        this.levelWidth = ModernBeta.BETA_CONFIG.indevLevelWidth;
        this.levelLength = ModernBeta.BETA_CONFIG.indevLevelLength;
        this.levelHeight = ModernBeta.BETA_CONFIG.indevLevelHeight;
        
        this.caveRadius = ModernBeta.BETA_CONFIG.indevCaveRadius;
    }
    
    @Override
    protected void init() {
        super.init();
        
        this.buttonList.addSingleOptionEntry(
            new CyclingOption(
                "createWorld.customize.indev.typeButton",
                (gameOptions, value) -> {
                    this.levelType++;
                    if (this.levelType > IndevType.values().length - 1) this.levelType = 0;
                    generatorSettings.providerSettings.putString("levelType", IndevType.fromId(this.levelType).getName());
                    
                    return;
                },
                (gameOptions, cyclingOptions) -> {
                    Text typeText = GUIUtil.TEXT_ISLAND;
                    IndevType type = IndevType.fromId(this.levelType);
                    
                    switch(type) {
                        case ISLAND:
                            typeText = GUIUtil.TEXT_ISLAND;
                            break;
                        case FLOATING:
                            typeText = GUIUtil.TEXT_FLOATING;
                            break;
                        case INLAND:
                            typeText = GUIUtil.TEXT_INLAND;
                            break;
                        default:
                            typeText = GUIUtil.TEXT_UNKNOWN;
                    }
                    
                    return new TranslatableText(
                            "options.generic_value", 
                            new Object[] { 
                                new TranslatableText("createWorld.customize.indev.levelType"), 
                                typeText
                        });
                }
            ));
            
            this.buttonList.addSingleOptionEntry(
                new CyclingOption(
                    "createWorld.customize.indev.themeButton",
                    (gameOptions, value) -> {
                        this.levelTheme++;
                        if (this.levelTheme > IndevTheme.values().length - 1) this.levelTheme = 0;
                        generatorSettings.providerSettings.putString("levelTheme", IndevTheme.fromId(this.levelTheme).getName());
                        
                        return;
                    },
                    (gameOptions, cyclingOptions) -> {
                        Text themeText = GUIUtil.TEXT_NORMAL;
                        IndevTheme theme = IndevTheme.fromId(this.levelTheme);
                        
                        switch(theme) {
                            case NORMAL:
                                themeText = GUIUtil.TEXT_NORMAL;
                                break;
                            case HELL:
                                themeText = GUIUtil.TEXT_HELL;
                                break;
                            case PARADISE:
                                themeText = GUIUtil.TEXT_PARADISE;
                                break;
                            case WOODS:
                                themeText = GUIUtil.TEXT_WOODS;
                                break;
                            case SNOWY:
                                themeText = GUIUtil.TEXT_SNOWY;
                                break;
                            default:
                                themeText = GUIUtil.TEXT_UNKNOWN;
                        }
                        
                        return new TranslatableText(
                            "options.generic_value", 
                            new Object[] { 
                                new TranslatableText("createWorld.customize.indev.levelTheme"), 
                                themeText 
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
                        generatorSettings.providerSettings.putInt("levelWidth", value.intValue());
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
                        generatorSettings.providerSettings.putInt("levelLength", value.intValue());
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
                        generatorSettings.providerSettings.putInt("levelHeight", value.intValue());
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
                        generatorSettings.providerSettings.putFloat("caveRadius", value.floatValue());
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
    }
}