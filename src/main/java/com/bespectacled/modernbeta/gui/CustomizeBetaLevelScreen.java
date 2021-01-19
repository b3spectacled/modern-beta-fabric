package com.bespectacled.modernbeta.gui;

import com.bespectacled.modernbeta.gen.settings.OldGeneratorSettings;
import com.bespectacled.modernbeta.util.WorldEnum.BiomeType;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;

public class CustomizeBetaLevelScreen extends AbstractCustomizeLevelScreen {
    public CustomizeBetaLevelScreen(CreateWorldScreen parent, OldGeneratorSettings generatorSettings) {
        super(parent, generatorSettings, "createWorld.customize.beta.title", BiomeType.BETA);
    }
    
    @Override
    protected void init() {
        super.init();
        super.initInf();
        
        this.children.add(this.buttonList);
    }
}
