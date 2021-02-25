package com.bespectacled.modernbeta.gui;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.indev.IndevUtil.IndevTheme;
import com.bespectacled.modernbeta.biome.indev.IndevUtil.IndevType;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.DoubleOption;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class IndevCustomizeLevelScreen extends AbstractCustomizeLevelScreen {
    private IndevType levelType;
    private IndevTheme levelTheme;

    private int levelWidth;
    private int levelLength;
    private int levelHeight;
    
    private float caveRadius;
    
    public IndevCustomizeLevelScreen(CreateWorldScreen parent, CompoundTag providerSettings, Consumer<CompoundTag> consumer) {
        super(parent, providerSettings, consumer);
        
        this.levelType = IndevType.fromName(ModernBeta.BETA_CONFIG.indevLevelType);
        this.levelTheme = IndevTheme.fromName(ModernBeta.BETA_CONFIG.indevLevelTheme);
        this.levelWidth = ModernBeta.BETA_CONFIG.indevLevelWidth;
        this.levelLength = ModernBeta.BETA_CONFIG.indevLevelLength;
        this.levelHeight = ModernBeta.BETA_CONFIG.indevLevelHeight;
        this.caveRadius = ModernBeta.BETA_CONFIG.indevCaveRadius;
        
        this.providerSettings.putString("levelType", this.levelType.getName());
        this.providerSettings.putString("levelTheme", this.levelTheme.getName());
        this.providerSettings.putInt("levelWidth", this.levelWidth);
        this.providerSettings.putInt("levelLength", this.levelLength);
        this.providerSettings.putInt("levelHeight", this.levelHeight);
        this.providerSettings.putFloat("caveRadius", this.caveRadius);
        
        consumer.accept(this.providerSettings);
    }
    
    @Override
    protected void init() {
        super.init();
        
        this.buttonList.addSingleOptionEntry(
            CyclingOption.create(
                "createWorld.customize.indev.levelType", 
                IndevType.values(), 
                (value) -> new TranslatableText("createWorld.customize.indev.type." + value.getName()), 
                (gameOptions) -> { return this.levelType; }, 
                (gameOptions, option, value) -> {
                    this.levelType = value;
                    this.providerSettings.putString("levelType", this.levelType.getName());
                    
                    consumer.accept(this.providerSettings);
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
                    this.providerSettings.putString("levelTheme", this.levelTheme.getName());
                    
                    consumer.accept(this.providerSettings);
                })
        );
        
        this.buttonList.addSingleOptionEntry(
            new DoubleOption(
                "createWorld.customize.indev.widthSlider", 
                128D, 1024D, 128f,
                (gameOptions) -> { return (double) this.levelWidth; }, // Getter
                (gameOptions, value) -> { // Setter
                    this.levelWidth = value.intValue();
                    this.providerSettings.putInt("levelWidth", this.levelWidth);
                    
                    consumer.accept(this.providerSettings);
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
                    this.providerSettings.putInt("levelLength", this.levelLength);
                    
                    consumer.accept(this.providerSettings);
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
                64D, 320D, 64F,
                (gameOptions) -> { return (double) this.levelHeight; }, // Getter
                (gameOptions, value) -> { // Setter
                    this.levelHeight = value.intValue();
                    this.providerSettings.putInt("levelHeight", this.levelHeight);
                    
                    consumer.accept(this.providerSettings);
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
                    this.providerSettings.putFloat("caveRadius", this.caveRadius);
                    
                    consumer.accept(this.providerSettings);
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
