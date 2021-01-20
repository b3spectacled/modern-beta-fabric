package com.bespectacled.modernbeta.gui;

import com.bespectacled.modernbeta.gen.OldGeneratorSettings;
import com.bespectacled.modernbeta.util.WorldEnum.BiomeType;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;

public class CustomizeAlphaLevelScreen extends AbstractCustomizeLevelScreen {
    public CustomizeAlphaLevelScreen(CreateWorldScreen parent, OldGeneratorSettings generatorSettings) {
        super(parent, generatorSettings, "createWorld.customize.alpha.title", BiomeType.CLASSIC);
    }
}
