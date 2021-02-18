package com.bespectacled.modernbeta.gui;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.BiomeType;
import com.bespectacled.modernbeta.gen.OldGeneratorSettings;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.text.TranslatableText;

public class InfCustomizeLevelScreen extends AbstractCustomizeLevelScreen {
    
    private BiomeType biomeType;
    private boolean generateOceans;
    private boolean generateNoiseCaves;
    private boolean generateAquifers;
    
    private final boolean showOceansOption;
    
    public InfCustomizeLevelScreen(CreateWorldScreen parent, OldGeneratorSettings generatorSettings, String title, BiomeType biomeType, boolean showOceansOption) {
        super(parent, generatorSettings, title);
        
        this.showOceansOption = showOceansOption;
        
        this.biomeType = biomeType;
        this.generateOceans = ModernBeta.BETA_CONFIG.generateOceans;
        this.generateNoiseCaves = false;
        this.generateAquifers = false;
    }
    
    @Override
    protected void init() {
        super.init();
        
        buttonList.addSingleOptionEntry(
            CyclingOption.create(
                "createWorld.customize.biomeType", 
                BiomeType.values(), 
                (value) -> new TranslatableText("createWorld.customize.biomeType." + value.getName()), 
                (gameOptions) -> { return this.biomeType; }, 
                (gameOptions, option, value) -> {
                    this.biomeType = value;
                    this.generatorSettings.providerSettings.putString("biomeType", this.biomeType.getName());
                })
        );
            
   
        if (this.showOceansOption) {
            buttonList.addSingleOptionEntry(
                CyclingOption.create("createWorld.customize.inf.generateOceans", 
                (gameOptions) -> { return this.generateOceans; }, 
                (gameOptions, option, value) -> { // Setter
                    this.generateOceans = value;
                    this.generatorSettings.providerSettings.putBoolean("generateOceans", this.generateOceans);
            }));
        }
        
        /*
        buttonList.addSingleOptionEntry(
            CyclingOption.create("createWorld.customize.inf.generateNoiseCaves", 
            (gameOptions) -> { return this.generateNoiseCaves; }, 
            (gameOptions, option, value) -> { // Setter
                this.generateNoiseCaves = value;
                //this.generatorSettings.providerSettings.putBoolean("generateOceans", this.generateOceans);
        }));
        
        buttonList.addSingleOptionEntry(
            CyclingOption.create("createWorld.customize.inf.generateAquifers", 
            (gameOptions) -> { return this.generateAquifers; }, 
            (gameOptions, option, value) -> { // Setter
                this.generateAquifers = value;
                //this.generatorSettings.providerSettings.putBoolean("generateOceans", this.generateOceans);
        }));
        */
    }
}
