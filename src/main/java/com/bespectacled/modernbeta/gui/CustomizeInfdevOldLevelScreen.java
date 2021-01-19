package com.bespectacled.modernbeta.gui;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.gen.settings.OldGeneratorSettings;
import com.bespectacled.modernbeta.util.WorldEnum.BiomeType;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.options.CyclingOption;

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
        super.initInf();
        
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
