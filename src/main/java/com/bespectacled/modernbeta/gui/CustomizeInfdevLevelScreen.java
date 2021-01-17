package com.bespectacled.modernbeta.gui;

import com.bespectacled.modernbeta.gen.settings.OldGeneratorSettings;
import com.bespectacled.modernbeta.util.WorldEnum.BiomeType;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.options.CyclingOption;
import net.minecraft.text.TranslatableText;

public class CustomizeInfdevLevelScreen extends AbstractCustomizeLevelScreen {
    public CustomizeInfdevLevelScreen(CreateWorldScreen parent, OldGeneratorSettings generatorSettings) {
        super(parent, generatorSettings, "createWorld.customize.infdev.title", BiomeType.CLASSIC);
    }
    
    @Override
    protected void init() {
        super.init();
        super.initInf();
        
        this.children.add(this.buttonList);
    }
}
