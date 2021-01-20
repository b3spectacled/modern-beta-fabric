package com.bespectacled.modernbeta.gui;

import com.bespectacled.modernbeta.gen.settings.OldGeneratorSettings;
import com.bespectacled.modernbeta.util.WorldEnum.BiomeType;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;

public class CustomizeInfdevLevelScreen extends AbstractCustomizeLevelScreen {
    public CustomizeInfdevLevelScreen(CreateWorldScreen parent, OldGeneratorSettings generatorSettings) {
        super(parent, generatorSettings, "createWorld.customize.infdev.title", BiomeType.CLASSIC);
    }
}
