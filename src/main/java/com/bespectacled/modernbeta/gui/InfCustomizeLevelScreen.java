package com.bespectacled.modernbeta.gui;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.BiomeType;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.TranslatableText;

public class InfCustomizeLevelScreen extends AbstractCustomizeLevelScreen {
    
    private BiomeType biomeType;
    private boolean generateOceans;
    private boolean generateNoiseCaves;
    private boolean generateAquifers;
    
    private final boolean showOceansOption;
    
    public InfCustomizeLevelScreen(CreateWorldScreen parent, CompoundTag providerSettings, Consumer<CompoundTag> consumer) {
        super(parent, providerSettings, consumer);
        
        this.showOceansOption = this.worldType.hasOceans();
        this.biomeType = this.worldType.getDefaultBiomeType();
        
        this.generateOceans = ModernBeta.BETA_CONFIG.generateOceans;
        this.generateNoiseCaves = false;
        this.generateAquifers = false;
        
        this.providerSettings.putString("biomeType", this.biomeType.getName());
        this.providerSettings.putBoolean("generateOceans", this.generateOceans);
        
        consumer.accept(this.providerSettings);
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
                    this.providerSettings.putString("biomeType", this.biomeType.getName());
                    
                    consumer.accept(this.providerSettings);
                })
        );
            
   
        if (this.showOceansOption) {
            buttonList.addSingleOptionEntry(
                CyclingOption.create("createWorld.customize.inf.generateOceans", 
                (gameOptions) -> { return this.generateOceans; }, 
                (gameOptions, option, value) -> { // Setter
                    this.generateOceans = value;
                    this.providerSettings.putBoolean("generateOceans", this.generateOceans);
                    
                    consumer.accept(this.providerSettings);
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
