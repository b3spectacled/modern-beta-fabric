package com.bespectacled.modernbeta.gui;

import com.bespectacled.modernbeta.gen.OldGeneratorSettings;
import com.bespectacled.modernbeta.util.WorldEnum.BiomeType;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;

public class CustomizeBetaLevelScreen extends AbstractCustomizeLevelScreen {
    public CustomizeBetaLevelScreen(CreateWorldScreen parent, OldGeneratorSettings generatorSettings) {
        super(parent, generatorSettings, "createWorld.customize.beta.title", BiomeType.BETA);
    }
}
