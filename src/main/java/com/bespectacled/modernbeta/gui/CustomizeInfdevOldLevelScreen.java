package com.bespectacled.modernbeta.gui;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.gen.settings.OldGeneratorSettings;
import com.bespectacled.modernbeta.util.WorldEnum.BiomeType;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.CyclingOption;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

public class CustomizeInfdevOldLevelScreen extends AbstractCustomizeLevelScreen {
    
    private boolean generateInfdevPyramid = ModernBeta.BETA_CONFIG.generateInfdevPyramid;
    private boolean generateInfdevWall = ModernBeta.BETA_CONFIG.generateInfdevWall;

    public CustomizeInfdevOldLevelScreen(CreateWorldScreen parent, OldGeneratorSettings generatorSettings) {
        super(parent, generatorSettings, "createWorld.customize.infdev.title", BiomeType.CLASSIC);
        
        if (generatorSettings.providerSettings.contains("generateInfdevPyramid"))
            generateInfdevPyramid = generatorSettings.providerSettings.getBoolean("generateInfdevPyramid");
        
        if (generatorSettings.providerSettings.contains("generateInfdevWall"))
            generateInfdevWall = generatorSettings.providerSettings.getBoolean("generateInfdevWall");
    }
    
    @Override
    protected void init() {
        super.init();
        
        this.buttonList.addSingleOptionEntry(
            CyclingOption.create(
                "createWorld.customize.type.biomeType", 
                BiomeType.values(), 
                (value) -> new TranslatableText("createWorld.customize.type." + value.getName()), 
                (gameOptions) -> { return this.biomeType; }, 
                (gameOptions, option, value) -> {
                    this.biomeType = value;
                    generatorSettings.providerSettings.putString("biomeType", this.biomeType.getName());
                    
                    return;
                })
        );
        
        this.buttonList.addSingleOptionEntry(
            CyclingOption.create("createWorld.customize.inf.generateOceans", 
            (gameOptions) -> { return generateOceans; }, 
            (gameOptions, option, value) -> { // Setter
                generateOceans = value;
                generatorSettings.providerSettings.putBoolean("generateOceans", value);
        }));
        
        this.buttonList.addSingleOptionEntry(
            CyclingOption.create("createWorld.customize.infdev.generateInfdevPyramid", 
            (gameOptions) -> { return generateInfdevPyramid; }, 
            (gameOptions, option, value) -> { // Setter
                generateInfdevPyramid = value;
                generatorSettings.providerSettings.putBoolean("generateInfdevPyramid", value);
        }));
        
        this.buttonList.addSingleOptionEntry(
            CyclingOption.create("createWorld.customize.infdev.generateInfdevWall", 
            (gameOptions) -> { return generateInfdevWall; }, 
            (gameOptions, option, value) -> { // Setter
                generateInfdevWall = value;
                generatorSettings.providerSettings.putBoolean("generateInfdevWall", value);
        }));
        
        
        this.children.add(this.buttonList);
    }
}
