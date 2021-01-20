package com.bespectacled.modernbeta.gui;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.indev.IndevUtil.IndevTheme;
import com.bespectacled.modernbeta.biome.indev.IndevUtil.IndevType;
import com.bespectacled.modernbeta.gen.settings.OldGeneratorSettings;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.options.CyclingOption;
import net.minecraft.client.options.DoubleOption;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class CustomizeIndevLevelScreen extends AbstractCustomizeLevelScreen {
   
    private IndevType levelType = IndevType.fromName(ModernBeta.BETA_CONFIG.indevLevelType);
    private IndevTheme levelTheme = IndevTheme.fromName(ModernBeta.BETA_CONFIG.indevLevelTheme);

    private int levelWidth = ModernBeta.BETA_CONFIG.indevLevelWidth;
    private int levelLength = ModernBeta.BETA_CONFIG.indevLevelLength;
    private int levelHeight = ModernBeta.BETA_CONFIG.indevLevelHeight;
    
    private float caveRadius = ModernBeta.BETA_CONFIG.indevCaveRadius;

    public CustomizeIndevLevelScreen(CreateWorldScreen parent, OldGeneratorSettings generatorSettings) {
        super(parent, generatorSettings, "createWorld.customize.indev.title");
        
        if (generatorSettings.providerSettings.contains("levelType")) 
            this.levelType = IndevType.fromName(generatorSettings.providerSettings.getString("levelType"));
        if (generatorSettings.providerSettings.contains("levelTheme")) 
            this.levelTheme = IndevTheme.fromName(generatorSettings.providerSettings.getString("levelTheme"));

        if (generatorSettings.providerSettings.contains("levelWidth")) this.levelWidth = generatorSettings.providerSettings.getInt("levelWidth");
        if (generatorSettings.providerSettings.contains("levelLength")) this.levelLength = generatorSettings.providerSettings.getInt("levelLength");
        if (generatorSettings.providerSettings.contains("levelHeight")) this.levelHeight = generatorSettings.providerSettings.getInt("levelHeight");
        if (generatorSettings.providerSettings.contains("caveRadius")) this.caveRadius = generatorSettings.providerSettings.getFloat("caveRadius");
    }
    
    @Override
    protected void initButtonList() {
        this.buttonList.addSingleOptionEntry(
            CyclingOption.create(
                "createWorld.customize.indev.levelType", 
                IndevType.values(), 
                (value) -> new TranslatableText("createWorld.customize.indev.type." + value.getName()), 
                (gameOptions) -> { return this.levelType; }, 
                (gameOptions, option, value) -> {
                    this.levelType = value;
                    generatorSettings.providerSettings.putString("levelType", this.levelType.getName());
                    
                    return;
                })
        );
        
        this.buttonList.addSingleOptionEntry(
            CyclingOption.create(
                "createWorld.customize.indev.levelTheme", 
                IndevTheme.values(), 
                (value) -> new TranslatableText("createWorld.customize.indev.theme." + value.getName()), 
                (gameOptions) -> { return this.levelTheme; }, 
                (gameOptions, option, value) -> {
                    this.levelTheme = value;
                    generatorSettings.providerSettings.putString("levelTheme", this.levelTheme.getName());
                    
                    return;
                })
        );
        
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
