package com.bespectacled.modernbeta.gui;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.BiomeType;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.TranslatableText;

public class InfCustomizeLevelScreen extends AbstractCustomizeLevelScreen {
    
    private BiomeType biomeType;
    private boolean generateOceans;
    private boolean generateDeepOceans;
    private boolean generateNoiseCaves;
    private boolean generateAquifers;
    private boolean generateDeepslate;
    
    private final boolean showOceansOption;
    private final boolean showDeepOceansOption;
    private final boolean showNoiseOptions;
    
    public InfCustomizeLevelScreen(CreateWorldScreen parent, CompoundTag settings, Consumer<CompoundTag> consumer) {
        super(parent, settings, consumer);
        
        this.biomeType = this.worldType.getDefaultBiomeType();
        this.generateOceans = ModernBeta.BETA_CONFIG.generateOceans;
        this.generateDeepOceans = ModernBeta.BETA_CONFIG.generateDeepOceans;
        this.generateNoiseCaves = ModernBeta.BETA_CONFIG.generateNoiseCaves;
        this.generateAquifers = ModernBeta.BETA_CONFIG.generateAquifers;
        this.generateDeepslate = ModernBeta.BETA_CONFIG.generateDeepslate;
        
        this.showOceansOption = this.worldType.showOceansOption();
        this.showDeepOceansOption = this.worldType.showDeepOceansOption();
        this.showNoiseOptions = this.worldType.showNoiseOptions();
        
        this.settings.putString("biomeType", this.biomeType.getName());
        
        if (this.showOceansOption)
            this.settings.putBoolean("generateOceans", this.generateOceans);
        
        if (this.showDeepOceansOption)
            this.settings.putBoolean("generateDeepOceans", this.generateDeepOceans);
        
        if (this.showNoiseOptions) 
            this.settings.putBoolean("generateNoiseCaves", this.generateNoiseCaves);
        
        if (this.showNoiseOptions && this.showOceansOption) 
            this.settings.putBoolean("generateAquifers", this.generateAquifers);
        
        this.settings.putBoolean("generateDeepslate", this.generateDeepslate);
    }
    
    @Override
    protected void init() {
        super.init();
        /*
        this.addButton(new ButtonWidget(
            this.width - 155, 8, 150, 20, 
            new TranslatableText("createworld.customize.inf.biomes"),
            (buttonWidget) -> {
                this.client.openScreen(new CustomizeBiomesScreen(this, this.settings, this.consumer));
            }
        ));*/
        
        buttonList.addSingleOptionEntry(
            CyclingOption.create(
                "createWorld.customize.biomeType",
                BiomeType.values(), 
                (value) -> new TranslatableText("createWorld.customize.biomeType." + value.getName()), 
                (gameOptions) -> { return this.biomeType; }, 
                (gameOptions, option, value) -> {
                    this.biomeType = value;
                    this.settings.putString("biomeType", this.biomeType.getName());
                })
        );
            
   
        if (this.showOceansOption) {
            buttonList.addSingleOptionEntry(
                CyclingOption.create("createWorld.customize.inf.generateOceans", 
                (gameOptions) -> { return this.generateOceans; }, 
                (gameOptions, option, value) -> { // Setter
                    this.generateOceans = value;
                    this.settings.putBoolean("generateOceans", this.generateOceans);
            }));
        }
        
        if (this.showDeepOceansOption) {
            buttonList.addSingleOptionEntry(
                CyclingOption.create("createWorld.customize.inf.generateDeepOceans", 
                (gameOptions) -> { return this.generateDeepOceans; }, 
                (gameOptions, option, value) -> { // Setter
                    this.generateDeepOceans = value;
                    this.settings.putBoolean("generateDeepOceans", this.generateDeepOceans);
            }));
        }
        
        if (this.showNoiseOptions) {
            buttonList.addSingleOptionEntry(
                CyclingOption.create("createWorld.customize.inf.generateNoiseCaves", 
                (gameOptions) -> { return this.generateNoiseCaves; }, 
                (gameOptions, option, value) -> { // Setter
                    this.generateNoiseCaves = value;
                    this.settings.putBoolean("generateNoiseCaves", this.generateNoiseCaves);
            }));
        }
        
        if (this.showNoiseOptions && this.showOceansOption) {
            buttonList.addSingleOptionEntry(
                CyclingOption.create("createWorld.customize.inf.generateAquifers", 
                (gameOptions) -> { return this.generateAquifers; }, 
                (gameOptions, option, value) -> { // Setter
                    this.generateAquifers = value;
                    this.settings.putBoolean("generateAquifers", this.generateAquifers);
            }));
        }
        
        buttonList.addSingleOptionEntry(
            CyclingOption.create("createWorld.customize.inf.generateDeepslate", 
            (gameOptions) -> { return this.generateNoiseCaves; }, 
            (gameOptions, option, value) -> { // Setter
                this.generateDeepslate = value;
                this.settings.putBoolean("generateDeepslate", this.generateDeepslate);
        }));
        
    }
}
