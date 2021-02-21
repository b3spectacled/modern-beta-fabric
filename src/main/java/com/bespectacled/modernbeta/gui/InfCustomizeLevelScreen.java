package com.bespectacled.modernbeta.gui;

import java.util.Arrays;
import java.util.Iterator;

import com.bespectacled.modernbeta.biome.BiomeType;
import com.bespectacled.modernbeta.gen.OldGeneratorSettings;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.options.BooleanOption;
import net.minecraft.client.options.CyclingOption;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class InfCustomizeLevelScreen extends AbstractCustomizeLevelScreen {
    
    private BiomeType biomeType;
    private Iterator<BiomeType> typeIterator;
    
    private boolean generateOceans;
    
    private final boolean showOceansOption;
    
    public InfCustomizeLevelScreen(CreateWorldScreen parent, OldGeneratorSettings generatorSettings, String title, BiomeType biomeType, boolean showOceansOption) {
        super(parent, generatorSettings, title);
        
        this.showOceansOption = showOceansOption;
        
        this.typeIterator = Arrays.asList(BiomeType.values()).iterator();
        this.biomeType = GUIUtil.iterateToBiomeType(biomeType, this.typeIterator);
        
        this.generatorSettings.providerSettings.putString("biomeType", this.biomeType.getName());
        this.generatorSettings.providerSettings.putBoolean("generateOceans", this.generateOceans);
    }
    
    @Override
    protected void init() {
        super.init();
        
        this.buttonList.addSingleOptionEntry(
            new CyclingOption(
                "createWorld.customize.preBeta.typeButton",
                (gameOptions, value) -> {
                    if (this.typeIterator.hasNext())
                        this.biomeType = typeIterator.next();
                    else {
                        typeIterator = Arrays.asList(BiomeType.values()).iterator();
                        this.biomeType = typeIterator.next();
                    }
                    
                    generatorSettings.providerSettings.putString("biomeType", this.biomeType.getName());
                    
                    return;
                },
                (gameOptions, cyclingOptions) -> {
                    Text typeText = GUIUtil.getBiomeTypeText(this.biomeType);
                    
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            GUIUtil.TEXT_BIOME_TYPE, 
                            typeText
                    });
                }
        ));
        
            
   
        if (this.showOceansOption) {
            this.buttonList.addSingleOptionEntry(
                new BooleanOption(
                    "createWorld.customize.inf.generateOceans", 
                    (gameOptions) -> { return generateOceans; }, // Getter
                    (gameOptions, value) -> { // Setter
                        generateOceans = value;
                        generatorSettings.providerSettings.putBoolean("generateOceans", value);
                    }
            ));
        }
    }
}