package com.bespectacled.modernbeta.gui;

import java.util.function.BiConsumer;
import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.BiomeType;
import com.bespectacled.modernbeta.biome.CaveBiomeType;
import com.bespectacled.modernbeta.gui.option.ScreenButtonOption;

import net.minecraft.client.gui.screen.CustomizeBuffetLevelScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class InfCustomizeLevelScreen extends AbstractCustomizeLevelScreen {
    
    private BiomeType biomeType;
    private CaveBiomeType caveBiomeType;
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
        CompoundTag biomeProviderSettings, 
        CompoundTag chunkProviderSettings, 
        BiConsumer<CompoundTag, CompoundTag> consumer
    ) {
        super(parent, registryManager, biomeProviderSettings, chunkProviderSettings, consumer);
        
        this.biomeType = this.worldType.getDefaultBiomeType();
        this.caveBiomeType = this.worldType.getDefaultCaveBiomeType();
        this.generateOceans = ModernBeta.BETA_CONFIG.generateOceans;
        this.generateDeepOceans = ModernBeta.BETA_CONFIG.generateDeepOceans;
        this.generateNoiseCaves = ModernBeta.BETA_CONFIG.generateNoiseCaves;
        this.generateAquifers = ModernBeta.BETA_CONFIG.generateAquifers;
        this.generateDeepslate = ModernBeta.BETA_CONFIG.generateDeepslate;
        
        this.showOceansOption = this.worldType.showOceansOption();
        this.showDeepOceansOption = this.worldType.showDeepOceansOption();
        this.showNoiseOptions = this.worldType.showNoiseOptions();
        
        this.biomeProviderSettings.putString("biomeType", this.biomeType.getName());
        this.biomeProviderSettings.putString("caveBiomeType", this.caveBiomeType.getName());
        this.biomeProviderSettings.putString("singleBiome", this.worldType.getDefaultBiome().toString());
        
        if (this.showOceansOption)
            this.chunkProviderSettings.putBoolean("generateOceans", this.generateOceans);
        
        if (this.showDeepOceansOption)
            this.chunkProviderSettings.putBoolean("generateDeepOceans", this.generateDeepOceans);
        
        if (this.showNoiseOptions) 
            this.chunkProviderSettings.putBoolean("generateNoiseCaves", this.generateNoiseCaves);
        
        if (this.showNoiseOptions && this.showOceansOption) 
            this.chunkProviderSettings.putBoolean("generateAquifers", this.generateAquifers);
        
        this.chunkProviderSettings.putBoolean("generateDeepslate", this.generateDeepslate);
    }
    
    @Override
    protected void init() {
        super.init();

        this.biomeOption = new ScreenButtonOption(
            "createWorld.customize.biomeType.biomes",
            biomeType -> ((BiomeType)biomeType) == BiomeType.SINGLE,
            buttonWidget -> this.client.openScreen(new CustomizeBuffetLevelScreen(
              this, 
              this.registryManager,
              (biome) -> {
                  this.biomeProviderSettings.putString("singleBiome", this.registryManager.<Biome>get(Registry.BIOME_KEY).getId(biome).toString());
              }, 
              this.registryManager.<Biome>get(Registry.BIOME_KEY).get(this.worldType.getDefaultBiome())  
            ))
        );
        
        buttonList.addOptionEntry(
            CyclingOption.create(
                "createWorld.customize.biomeType",
                BiomeType.values(), 
                (value) -> new TranslatableText("createWorld.customize.biomeType." + value.getName()), 
                (gameOptions) -> { return this.biomeType; }, 
                (gameOptions, option, value) -> {
                    this.biomeType = value;
                    this.biomeProviderSettings.putString("biomeType", this.biomeType.getName());
                    
                    // Update biome button and active state based on current biomeType.
                    this.updateBiomeButton();
                    this.updateBiomeButtonActive();
                }
            ),
            this.biomeOption
        );
        
        // Set single biome active state right after it's added to button list.
        this.updateBiomeButton();
        this.updateBiomeButtonActive();
        
        /*
        buttonList.addSingleOptionEntry(
            CyclingOption.create(
                "createWorld.customize.caveBiomeType",
                CaveBiomeType.values(), 
                (value) -> new TranslatableText("createWorld.customize.caveBiomeType." + value.getName()), 
                (gameOptions) -> { return this.caveBiomeType; }, 
                (gameOptions, option, value) -> {
                    this.caveBiomeType = value;
                    this.biomeProviderSettings.putString("caveBiomeType", this.caveBiomeType.getName());
                }
            )
        );*/
        
        if (this.showOceansOption) {
            buttonList.addSingleOptionEntry(
                CyclingOption.create("createWorld.customize.inf.generateOceans", 
                (gameOptions) -> { return this.generateOceans; }, 
                (gameOptions, option, value) -> { // Setter
                    this.generateOceans = value;
                    this.chunkProviderSettings.putBoolean("generateOceans", this.generateOceans);
            }));
        }
        
        if (this.showDeepOceansOption) {
            buttonList.addSingleOptionEntry(
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
            buttonList.addSingleOptionEntry(
                CyclingOption.create("createWorld.customize.inf.generateAquifers", 
                (gameOptions) -> { return this.generateAquifers; }, 
                (gameOptions, option, value) -> { // Setter
                    this.generateAquifers = value;
                    this.chunkProviderSettings.putBoolean("generateAquifers", this.generateAquifers);
            }));
        }
        
        buttonList.addSingleOptionEntry(
            CyclingOption.create("createWorld.customize.inf.generateDeepslate", 
            (gameOptions) -> { return this.generateNoiseCaves; }, 
            (gameOptions, option, value) -> { // Setter
                this.generateDeepslate = value;
                this.chunkProviderSettings.putBoolean("generateDeepslate", this.generateDeepslate);
        }));
    }

    @Override
    protected void updateBiomeButtonActive() {
        this.biomeOption.setButtonActive(this.biomeType);
    }
    
    private void updateBiomeButton() {
        if (this.biomeType == BiomeType.BETA) {
            this.biomeOption.updateActivePredicate(biomeType -> ((BiomeType)biomeType) == BiomeType.BETA);
            this.biomeOption.updateOnPressAction(
                buttonWidget -> this.client.openScreen(new BetaCustomizeBiomesScreen(
                    this, 
                    this.registryManager,
                    this.biomeProviderSettings, 
                    biomeProviderSettings -> { this.consumer.accept(biomeProviderSettings, this.chunkProviderSettings); }
            )));
        } else {
            this.biomeOption.updateActivePredicate(biomeType -> ((BiomeType)biomeType) == BiomeType.SINGLE);
            this.biomeOption.updateOnPressAction(
                buttonWidget -> this.client.openScreen(new CustomizeBuffetLevelScreen(
                    this, 
                    this.registryManager,
                    (biome) -> {
                        this.biomeProviderSettings.putString("singleBiome", this.registryManager.<Biome>get(Registry.BIOME_KEY).getId(biome).toString());
                    }, 
                    this.registryManager.<Biome>get(Registry.BIOME_KEY).get(this.worldType.getDefaultBiome())  
            )));
        }
    }
}
