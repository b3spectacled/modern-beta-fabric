package com.bespectacled.modernbeta.gui;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.BiomeType;
import com.bespectacled.modernbeta.gen.OldGeneratorSettings;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.options.BooleanOption;

public class InfdevOldCustomizeLevelScreen extends InfCustomizeLevelScreen {
    private boolean generateInfdevPyramid;
    private boolean generateInfdevWall;
    
    public InfdevOldCustomizeLevelScreen(CreateWorldScreen parent, OldGeneratorSettings generatorSettings, String title, BiomeType biomeType, boolean showOceansOption) {
        super(parent, generatorSettings, title, biomeType, showOceansOption);
        
        this.generateInfdevPyramid = ModernBeta.BETA_CONFIG.generateInfdevPyramid;
        this.generateInfdevWall = ModernBeta.BETA_CONFIG.generateInfdevWall;
        
        this.generatorSettings.providerSettings.putBoolean("generateInfdevPyramid", this.generateInfdevPyramid);
        this.generatorSettings.providerSettings.putBoolean("generateInfdevWall", this.generateInfdevWall);
    }
    
    @Override
    protected void init() {
        super.init();
        
        this.buttonList.addSingleOptionEntry(
            new BooleanOption(
                "createWorld.customize.infdev.generateInfdevPyramid", 
                (gameOptions) -> { return generateInfdevPyramid; }, 
                (gameOptions, value) -> {
                    generateInfdevPyramid = value;
                    generatorSettings.providerSettings.putBoolean("generateInfdevPyramid", value);
                }
        ));
       
        this.buttonList.addSingleOptionEntry(
            new BooleanOption(
                "createWorld.customize.infdev.generateInfdevWall", 
                (gameOptions) -> { return generateInfdevWall; }, 
                (gameOptions, value) -> {
                    generateInfdevWall = value;
                    generatorSettings.providerSettings.putBoolean("generateInfdevWall", value);
                }
        ));
    }
    

}
