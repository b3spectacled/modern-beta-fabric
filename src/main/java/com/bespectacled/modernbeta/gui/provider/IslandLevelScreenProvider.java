package com.bespectacled.modernbeta.gui.provider;

import java.util.function.BiConsumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.gui.TextOption;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.option.DoubleOption;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.DynamicRegistryManager;

public class IslandLevelScreenProvider extends InfLevelScreenProvider {

    private float islandNoiseScale;
    private float islandNoiseOffset;
    
    public IslandLevelScreenProvider(
        CreateWorldScreen parent, 
        DynamicRegistryManager registryManager, 
        NbtCompound biomeProviderSettings, 
        NbtCompound chunkProviderSettings, 
        BiConsumer<NbtCompound, NbtCompound> consumer
    ) {
        super(parent, registryManager, biomeProviderSettings, chunkProviderSettings, consumer);
        
        this.islandNoiseScale = this.chunkProviderSettings.contains("islandNoiseScale") ?
            this.chunkProviderSettings.getFloat("islandNoiseScale") :
            ModernBeta.BETA_CONFIG.generationConfig.islandNoiseScale;
        
        this.islandNoiseOffset = this.chunkProviderSettings.contains("islandNoiseOffset") ?
            this.chunkProviderSettings.getFloat("islandNoiseOffset") :
            ModernBeta.BETA_CONFIG.generationConfig.islandNoiseOffset;
    }

    @Override
    protected void init() {
        super.init();
        
        this.buttonList.addSingleOptionEntry(
            new DoubleOption(
                "createWorld.customize.island.islandNoiseScaleSlider", 
                1D, 1000D, 50f,
                (gameOptions) -> { return (double) this.islandNoiseScale; }, // Getter
                (gameOptions, value) -> { // Setter
                    this.islandNoiseScale = value.floatValue();
                    this.chunkProviderSettings.putFloat("islandNoiseScale", this.islandNoiseScale);
                },
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.island.islandNoiseScale"), 
                            Text.of(String.valueOf(this.islandNoiseScale)) 
                    });
                }
        ));
        
        this.buttonList.addSingleOptionEntry(
            new DoubleOption(
                "createWorld.customize.island.islandNoiseOffsetSlider", 
                -1.0D, 1.0D, 0.25f,
                (gameOptions) -> { return (double) this.islandNoiseOffset; }, // Getter
                (gameOptions, value) -> { // Setter
                    this.islandNoiseOffset = value.floatValue();
                    this.chunkProviderSettings.putFloat("islandNoiseOffset", this.islandNoiseOffset);
                },
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.island.islandNoiseOffset"), 
                            Text.of(String.valueOf(this.islandNoiseOffset)) 
                    });
                }
        ));
        
        this.buttonList.addSingleOptionEntry(new TextOption("Note: Settings are not final and may change."));
    }
}
