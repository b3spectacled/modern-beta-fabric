package com.bespectacled.modernbeta.gui.world;

import java.util.function.BiConsumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.AbstractWorldScreenProvider;
import com.bespectacled.modernbeta.api.registry.BiomeProviderRegistry.BuiltInBiomeType;
import com.bespectacled.modernbeta.gui.TextOption;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.DynamicRegistryManager;

public class InfWorldScreenProvider extends AbstractWorldScreenProvider {
    
    private boolean generateOceans;
    private boolean generateNoiseCaves;
    private boolean generateAquifers;
    private boolean generateDeepslate;
    
    private final boolean showNoiseOptions;
    
    public InfWorldScreenProvider(
        CreateWorldScreen parent, 
        DynamicRegistryManager registryManager, 
        NbtCompound biomeProviderSettings, 
        NbtCompound chunkProviderSettings, 
        BiConsumer<NbtCompound, NbtCompound> consumer
    ) {
        super(parent, registryManager, biomeProviderSettings, chunkProviderSettings, consumer);
        
        this.generateOceans = this.chunkProviderSettings.contains("generateOceans") ? 
            this.chunkProviderSettings.getBoolean("generateOceans") : 
            ModernBeta.BETA_CONFIG.generation_config.generateOceans;
        
        this.generateNoiseCaves = this.chunkProviderSettings.contains("generateNoiseCaves") ? 
            this.chunkProviderSettings.getBoolean("generateNoiseCaves") :
            ModernBeta.BETA_CONFIG.generation_config.generateNoiseCaves;
        
        this.generateAquifers = this.chunkProviderSettings.contains("generateAquifers") ? 
            this.chunkProviderSettings.getBoolean("generateAquifers") :
            ModernBeta.BETA_CONFIG.generation_config.generateAquifers;
        
        this.generateDeepslate = this.chunkProviderSettings.contains("generateDeepslate") ? 
            this.chunkProviderSettings.getBoolean("generateDeepslate") :
            ModernBeta.BETA_CONFIG.generation_config.generateDeepslate;
        
        this.showNoiseOptions = this.worldProvider.showNoiseOptions();
    }
    
    @Override
    protected void init() {
        super.init();
        
        if (!this.biomeType.equals(BuiltInBiomeType.SINGLE.name)) {
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
        
        if (this.showNoiseOptions) {
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
        
        if (!(this instanceof InfdevOldWorldScreenProvider) && !(this instanceof IslandWorldScreenProvider))
            this.buttonList.addSingleOptionEntry(new TextOption("Note: Settings are not final and may change."));
    }
}
