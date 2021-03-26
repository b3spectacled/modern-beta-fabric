package com.bespectacled.modernbeta.gui;

import java.util.Arrays;
import java.util.function.BiConsumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.BiomeType;
import com.bespectacled.modernbeta.gen.OldGeneratorSettings;
import com.bespectacled.modernbeta.gui.option.ScreenButtonOption;
import com.bespectacled.modernbeta.gui.option.TextOption;
import com.bespectacled.modernbeta.util.GUIUtil;

import net.minecraft.client.gui.screen.CustomizeBuffetLevelScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class InfCustomizeLevelScreen extends AbstractCustomizeLevelScreen {
    
    private boolean generateOceans;
    private boolean generateDeepOceans;
    private boolean generateNoiseCaves;
    private boolean generateAquifers;
    private boolean generateDeepslate;
    
    private final boolean showOceansOption;
    private final boolean showDeepOceansOption;
    private final boolean showNoiseOptions;
    
    public InfCustomizeLevelScreen(
        CreateWorldScreen parent, 
        DynamicRegistryManager registryManager, 
        NbtCompound biomeProviderSettings, 
        NbtCompound chunkProviderSettings, 
        BiConsumer<NbtCompound, NbtCompound> consumer
    ) {
        super(parent, registryManager, biomeProviderSettings, chunkProviderSettings, consumer);
        
        this.generateOceans = this.chunkProviderSettings.contains("generateOceans") ? 
            this.chunkProviderSettings.getBoolean("generateOceans") : 
            ModernBeta.BETA_CONFIG.generationConfig.generateOceans;
        
        /*
        this.generateDeepOceans = this.chunkProviderSettings.contains("generateDeepOceans") ? 
            this.chunkProviderSettings.getBoolean("generateDeepOceans") :
            ModernBeta.BETA_CONFIG.generateDeepOceans;
        */
        
        this.generateNoiseCaves = this.chunkProviderSettings.contains("generateNoiseCaves") ? 
            this.chunkProviderSettings.getBoolean("generateNoiseCaves") :
            ModernBeta.BETA_CONFIG.generationConfig.generateNoiseCaves;
        
        this.generateAquifers = this.chunkProviderSettings.contains("generateAquifers") ? 
            this.chunkProviderSettings.getBoolean("generateAquifers") :
            ModernBeta.BETA_CONFIG.generationConfig.generateAquifers;
        
        this.generateDeepslate = this.chunkProviderSettings.contains("generateDeepslate") ? 
            this.chunkProviderSettings.getBoolean("generateDeepslate") :
            ModernBeta.BETA_CONFIG.generationConfig.generateDeepslate;
        
        this.showOceansOption = this.worldType.showOceansOption();
        this.showDeepOceansOption = this.worldType.showDeepOceansOption();
        this.showNoiseOptions = this.worldType.showNoiseOptions();
    }
    
    @Override
    protected void init() {
        super.init();
        
        // Get biome type list, sans legacy types
        BiomeType[] biomeTypes = Arrays.stream(BiomeType.values()).filter(type -> BiomeType.getExclusions(type)).toArray(BiomeType[]::new);
        
        this.biomeOption = new ScreenButtonOption(
            this.biomeType == BiomeType.SINGLE ? "createWorld.customize.biomeType.biome" : "createWorld.customize.biomeType.settings", // Key
            this.biomeType == BiomeType.SINGLE ? GUIUtil.createTranslatableBiomeString(this.singleBiome) : null,
            type -> true, // Active Predicate
            this.getOnPress(this.biomeType) // On Press Action
        );
        
        this.buttonList.addOptionEntry(
            CyclingOption.create(
                "createWorld.customize.biomeType",
                biomeTypes, 
                (value) -> new TranslatableText("createWorld.customize.biomeType." + value.getName()), 
                (gameOptions) -> { return this.biomeType; },
                (gameOptions, option, value) -> {
                    this.biomeType = value;
                    
                    NbtCompound newBiomeProviderSettings = OldGeneratorSettings.createBiomeSettings(
                        this.biomeType, 
                        this.caveBiomeType, 
                        this.worldType.getDefaultBiome()
                    );
                    
                    this.client.openScreen(
                        this.worldType.createLevelScreen(
                            this.parent, 
                            this.registryManager, 
                            newBiomeProviderSettings, 
                            this.chunkProviderSettings, 
                            this.consumer
                    ));
                }
            ),
            this.biomeOption
        );
        this.updateButtonActive(this.biomeOption);
        
        if (this.showOceansOption && this.biomeType != BiomeType.SINGLE) {
            buttonList.addSingleOptionEntry(
                CyclingOption.create("createWorld.customize.inf.generateOceans",
                (gameOptions) -> { return this.generateOceans; }, 
                (gameOptions, option, value) -> { // Setter
                    this.generateOceans = value;
                    this.chunkProviderSettings.putBoolean("generateOceans", this.generateOceans);
            }));
        }
        
        if (this.showDeepOceansOption) {
            this.buttonList.addSingleOptionEntry(
                CyclingOption.create("createWorld.customize.inf.generateDeepOceans", 
                (gameOptions) -> { return this.generateDeepOceans; }, 
                (gameOptions, option, value) -> { // Setter
                    this.generateDeepOceans = value;
                    this.chunkProviderSettings.putBoolean("generateDeepOceans", this.generateDeepOceans);
            }));
        }
        
        if (this.showNoiseOptions) { 
            buttonList.addSingleOptionEntry( 
                CyclingOption.create("createWorld.customize.inf.generateNoiseCaves", 
                (gameOptions) -> { return this.generateNoiseCaves; }, 
                (gameOptions, option, value) -> { // Setter
                    this.generateNoiseCaves = value;
                    this.chunkProviderSettings.putBoolean("generateNoiseCaves", this.generateNoiseCaves);
            }));
        }
        
        if (this.showNoiseOptions && this.showOceansOption) {
            this.buttonList.addSingleOptionEntry(
                CyclingOption.create("createWorld.customize.inf.generateAquifers", 
                (gameOptions) -> { return this.generateAquifers; }, 
                (gameOptions, option, value) -> { // Setter
                    this.generateAquifers = value;
                    this.chunkProviderSettings.putBoolean("generateAquifers", this.generateAquifers);
            }));
        }
        
        this.buttonList.addSingleOptionEntry(
            CyclingOption.create("createWorld.customize.inf.generateDeepslate", 
            (gameOptions) -> { return this.generateDeepslate; }, 
            (gameOptions, option, value) -> { // Setter
                this.generateDeepslate = value;
                this.chunkProviderSettings.putBoolean("generateDeepslate", this.generateDeepslate);
        }));
        
        this.buttonList.addSingleOptionEntry(new TextOption("Note: Settings are not final and may change."));
    }
    
    private ButtonWidget.PressAction getOnPress(BiomeType biomeType) {
        ButtonWidget.PressAction action;
        
        switch(biomeType) {
            case BETA:
                action = buttonWidget -> this.client.openScreen(new BetaCustomizeBiomesScreen(
                    this, 
                    this.registryManager,
                    this.biomeProviderSettings,
                    betaBiomeSettings -> this.biomeProviderSettings.copyFrom(betaBiomeSettings)
                ));
                break;
            case SINGLE:
                action = buttonWidget -> this.client.openScreen(new CustomizeBuffetLevelScreen(
                    this, 
                    this.registryManager,
                    (biome) -> {
                        this.singleBiome = this.registryManager.<Biome>get(Registry.BIOME_KEY).getId(biome);
                        this.biomeProviderSettings.putString("singleBiome", this.singleBiome.toString());
                    },
                    this.registryManager.<Biome>get(Registry.BIOME_KEY).get(this.singleBiome)  
                ));
                break;
            case VANILLA:
                action = buttonWidget -> this.client.openScreen(new VanillaCustomizeBiomesScreen(
                    this,
                    this.registryManager,
                    this.biomeProviderSettings,
                    vanillaBiomeSettings -> this.biomeProviderSettings.copyFrom(vanillaBiomeSettings)
                ));
                break;
            default:
                action = buttonWidget -> {};
        }
        
        return action;
    }
    
    @Override
    protected void updateButtonActive(ScreenButtonOption option) {
        option.setButtonActive(this.biomeType);
    }
}
