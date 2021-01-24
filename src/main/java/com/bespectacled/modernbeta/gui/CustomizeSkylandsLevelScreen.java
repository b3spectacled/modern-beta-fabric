package com.bespectacled.modernbeta.gui;

import com.bespectacled.modernbeta.gen.OldGeneratorSettings;
import com.bespectacled.modernbeta.util.WorldEnum.BiomeType;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.text.TranslatableText;

public class CustomizeSkylandsLevelScreen extends AbstractCustomizeLevelScreen {

    public CustomizeSkylandsLevelScreen(CreateWorldScreen parent, OldGeneratorSettings generatorSettings) {
        super(parent, generatorSettings, "createWorld.customize.skylands.title", BiomeType.SKY);
    }
    
    @Override
    protected void addButtons() {
        this.buttonList.addSingleOptionEntry(
            CyclingOption.create(
                "createWorld.customize.biomeType", 
                BiomeType.values(), 
                (value) -> new TranslatableText("createWorld.customize.biomeType." + value.getName()), 
                (gameOptions) -> { return this.biomeType; }, 
                (gameOptions, option, value) -> {
                    this.biomeType = value;
                    generatorSettings.providerSettings.putString("biomeType", this.biomeType.getName());
                    
                    return;
                })
        );
    }    
}
