package com.bespectacled.modernbeta.gui.world;

import java.util.function.BiConsumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.AbstractWorldScreenProvider;
import com.bespectacled.modernbeta.gui.TextOption;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.DynamicRegistryManager;

public class SkylandsWorldScreenProvider extends AbstractWorldScreenProvider {

    private boolean generateNoiseCaves;
    private boolean generateDeepslate;

    private final boolean showNoiseOptions;
    
    public SkylandsWorldScreenProvider(
        CreateWorldScreen parent, 
        DynamicRegistryManager registryManager, 
        NbtCompound biomeProviderSettings, 
        NbtCompound chunkProviderSettings, 
        BiConsumer<NbtCompound, NbtCompound> consumer
    ) {
        super(parent, registryManager, biomeProviderSettings, chunkProviderSettings, consumer);
        
        this.generateNoiseCaves = this.chunkProviderSettings.contains("generateNoiseCaves") ? 
            this.chunkProviderSettings.getBoolean("generateNoiseCaves") :
            ModernBeta.BETA_CONFIG.generation_config.generateNoiseCaves;
            
        this.generateDeepslate = this.chunkProviderSettings.contains("generateDeepslate") ? 
            this.chunkProviderSettings.getBoolean("generateDeepslate") :
            ModernBeta.BETA_CONFIG.generation_config.generateDeepslate;
        
        this.showNoiseOptions = this.worldProvider.showNoiseOptions();
    }

    @Override
    protected void init() {
        super.init();
        
        if (this.showNoiseOptions) { 
            buttonList.addSingleOptionEntry( 
                CyclingOption.create("createWorld.customize.inf.generateNoiseCaves", 
                (gameOptions) -> { return this.generateNoiseCaves; }, 
                (gameOptions, option, value) -> { // Setter
                    this.generateNoiseCaves = value;
                    this.chunkProviderSettings.putBoolean("generateNoiseCaves", this.generateNoiseCaves);
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
}
