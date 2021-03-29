package com.bespectacled.modernbeta.gui.provider;

import java.util.function.BiConsumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.AbstractScreenProvider;
import com.bespectacled.modernbeta.api.BiomeProviderType;
import com.bespectacled.modernbeta.gen.OldGeneratorSettings;
import com.bespectacled.modernbeta.gui.ScreenButtonOption;
import com.bespectacled.modernbeta.gui.TextOption;
import com.bespectacled.modernbeta.util.GUIUtil;

import net.minecraft.client.gui.screen.CustomizeBuffetLevelScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class InfCustomizeLevelScreen extends AbstractScreenProvider {
    
    private boolean generateOceans;
    private boolean generateNoiseCaves;
    private boolean generateAquifers;
    private boolean generateDeepslate;
    
    private final boolean showOceansOption;
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
        
        this.generateNoiseCaves = this.chunkProviderSettings.contains("generateNoiseCaves") ? 
            this.chunkProviderSettings.getBoolean("generateNoiseCaves") :
            ModernBeta.BETA_CONFIG.generationConfig.generateNoiseCaves;
        
        this.generateAquifers = this.chunkProviderSettings.contains("generateAquifers") ? 
            this.chunkProviderSettings.getBoolean("generateAquifers") :
            ModernBeta.BETA_CONFIG.generationConfig.generateAquifers;
        
        this.generateDeepslate = this.chunkProviderSettings.contains("generateDeepslate") ? 
            this.chunkProviderSettings.getBoolean("generateDeepslate") :
            ModernBeta.BETA_CONFIG.generationConfig.generateDeepslate;
        
        this.showOceansOption = this.worldProvider.showOceansOption();
        this.showNoiseOptions = this.worldProvider.showNoiseOptions();
    }
    
    @Override
    protected void init() {
        super.init();
        
        // Get biome type list, sans legacy types
        String[] biomeProviderTypes = BiomeProviderType
            .getBiomeProviderKeys()
            .stream()
            .filter(str -> !BiomeProviderType.getLegacyTypes().contains(str))
            .toArray(String[]::new);
        
        this.biomeOption = new ScreenButtonOption(
            this.biomeType == BiomeProviderType.SINGLE ? "createWorld.customize.biomeType.biome" : "createWorld.customize.biomeType.settings", // Key
            this.biomeType == BiomeProviderType.SINGLE ? GUIUtil.createTranslatableBiomeStringFromId(this.singleBiome) : null,
            type -> true, // Active Predicate
            this.getOnPress(this.biomeType) // On Press Action
        );
        
        this.buttonList.addOptionEntry(
            CyclingOption.create(
                "createWorld.customize.biomeType",
                biomeProviderTypes, 
                (value) -> new TranslatableText("createWorld.customize.biomeType." + value), 
                (gameOptions) -> { return this.biomeType; },
                (gameOptions, option, value) -> {
                    this.biomeType = value;
                    
                    NbtCompound newBiomeProviderSettings = OldGeneratorSettings.createBiomeSettings(
                        this.biomeType, 
                        this.caveBiomeType, 
                        this.worldProvider.getDefaultBiome()
                    );
                    
                    this.client.openScreen(
                        this.worldProvider.createLevelScreen(
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
        
        if (this.showOceansOption && this.biomeType != BiomeProviderType.SINGLE) {
            buttonList.addSingleOptionEntry(
                CyclingOption.create("createWorld.customize.inf.generateOceans",
                (gameOptions) -> { return this.generateOceans; }, 
                (gameOptions, option, value) -> { // Setter
                    this.generateOceans = value;
                    this.chunkProviderSettings.putBoolean("generateOceans", this.generateOceans);
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
    
    private ButtonWidget.PressAction getOnPress(String biomeType) {
        ButtonWidget.PressAction action;
        
        switch(biomeType) {
            case BiomeProviderType.BETA:
                action = buttonWidget -> this.client.openScreen(new BetaCustomizeBiomesScreen(
                    this, 
                    this.registryManager,
                    this.biomeProviderSettings,
                    betaBiomeSettings -> this.biomeProviderSettings.copyFrom(betaBiomeSettings)
                ));
                break;
            case BiomeProviderType.SINGLE:
                action = buttonWidget -> this.client.openScreen(new CustomizeBuffetLevelScreen(
                    this, 
                    this.registryManager,
                    (biome) -> {
                        this.singleBiome = this.registryManager.<Biome>get(Registry.BIOME_KEY).getId(biome).toString();
                        this.biomeProviderSettings.putString("singleBiome", this.singleBiome);
                    },
                    this.registryManager.<Biome>get(Registry.BIOME_KEY).get(new Identifier(this.singleBiome))  
                ));
                break;
            case BiomeProviderType.VANILLA:
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
