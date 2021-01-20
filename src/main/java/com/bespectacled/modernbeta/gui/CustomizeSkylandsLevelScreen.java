package com.bespectacled.modernbeta.gui;

import com.bespectacled.modernbeta.gen.settings.OldGeneratorSettings;
import com.bespectacled.modernbeta.util.WorldEnum.BiomeType;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.options.CyclingOption;
import net.minecraft.text.TranslatableText;

public class CustomizeSkylandsLevelScreen extends AbstractCustomizeLevelScreen {

    public CustomizeSkylandsLevelScreen(CreateWorldScreen parent, OldGeneratorSettings generatorSettings) {
        super(parent, generatorSettings, "createWorld.customize.skylands.title", BiomeType.SKY);
    }
    
    @Override
    protected void initButtonList() {
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
    }    
}
