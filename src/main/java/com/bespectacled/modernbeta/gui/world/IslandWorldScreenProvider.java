package com.bespectacled.modernbeta.gui.world;

import java.util.function.BiConsumer;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.options.DoubleOption;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.DynamicRegistryManager;

public class IslandWorldScreenProvider extends InfWorldScreenProvider {

    private int centerOceanLerpDistance;
    private int centerOceanRadius;
    private float centerIslandFalloff;
    
    private float outerIslandNoiseScale;
    private float outerIslandNoiseOffset;
    
    public IslandWorldScreenProvider(
        CreateWorldScreen parent, 
        DynamicRegistryManager registryManager, 
        CompoundTag biomeProviderSettings, 
        CompoundTag chunkProviderSettings, 
        BiConsumer<CompoundTag, CompoundTag> consumer
    ) {
        super(parent, registryManager, biomeProviderSettings, chunkProviderSettings, consumer);
        
        this.centerOceanLerpDistance = this.chunkProviderSettings.contains("centerOceanLerpDistance") ?
            this.chunkProviderSettings.getInt("centerOceanLerpDistance") :
            ModernBeta.BETA_CONFIG.generation_config.centerOceanLerpDistance;
        
        this.centerOceanRadius = this.chunkProviderSettings.contains("centerOceanRadius") ?
            this.chunkProviderSettings.getInt("centerOceanRadius") :
            ModernBeta.BETA_CONFIG.generation_config.centerOceanRadius;
        
        this.centerIslandFalloff = this.chunkProviderSettings.contains("centerIslandFalloff") ?
            this.chunkProviderSettings.getFloat("centerIslandFalloff") :
            ModernBeta.BETA_CONFIG.generation_config.centerIslandFalloff;
        
        this.outerIslandNoiseScale = this.chunkProviderSettings.contains("outerIslandNoiseScale") ?
            this.chunkProviderSettings.getFloat("outerIslandNoiseScale") :
            ModernBeta.BETA_CONFIG.generation_config.outerIslandNoiseScale;
        
        this.outerIslandNoiseOffset = this.chunkProviderSettings.contains("outerIslandNoiseOffset") ?
            this.chunkProviderSettings.getFloat("outerIslandNoiseOffset") :
            ModernBeta.BETA_CONFIG.generation_config.outerIslandNoiseOffset;
    }

    @Override
    protected void init() {
        super.init();
        
        this.buttonList.addSingleOptionEntry(
            new DoubleOption(
                "createWorld.customize.island.centerOceanLerpDistanceSlider", 
                1D, 32D, 1F,
                (gameOptions) -> { return (double) this.centerOceanLerpDistance; }, // Getter
                (gameOptions, value) -> { // Setter
                    this.centerOceanLerpDistance = value.intValue();
                    this.chunkProviderSettings.putInt("centerOceanLerpDistance", this.centerOceanLerpDistance);
                },
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.island.centerOceanLerpDistance"), 
                            Text.of(String.valueOf(this.centerOceanLerpDistance) + " chunks") 
                    });
                }
        ));
        
        this.buttonList.addSingleOptionEntry(
            new DoubleOption(
                "createWorld.customize.island.centerOceanRadiusSlider", 
                8D, 256D, 8F,
                (gameOptions) -> { return (double) this.centerOceanRadius; }, // Getter
                (gameOptions, value) -> { // Setter
                    this.centerOceanRadius = value.intValue();
                    this.chunkProviderSettings.putInt("centerOceanRadius", this.centerOceanRadius);
                },
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.island.centerOceanRadius"), 
                            Text.of(String.valueOf(this.centerOceanRadius) + " chunks") 
                    });
                }
        ));
        
        this.buttonList.addSingleOptionEntry(
            new DoubleOption(
                "createWorld.customize.island.centerIslandFalloffSlider", 
                1D, 8D, 1f,
                (gameOptions) -> { return (double) this.centerIslandFalloff; }, // Getter
                (gameOptions, value) -> { // Setter
                    this.centerIslandFalloff = value.floatValue();
                    this.chunkProviderSettings.putFloat("centerIslandFalloff", this.centerIslandFalloff);
                },
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.island.centerIslandFalloff"), 
                            Text.of(String.valueOf(this.centerIslandFalloff)) 
                    });
                }
        ));
    
        this.buttonList.addSingleOptionEntry(
            new DoubleOption(
                "createWorld.customize.island.outerIslandNoiseScaleSlider", 
                1D, 1000D, 50f,
                (gameOptions) -> { return (double) this.outerIslandNoiseScale; }, // Getter
                (gameOptions, value) -> { // Setter
                    this.outerIslandNoiseScale = value.floatValue();
                    this.chunkProviderSettings.putFloat("outerIslandNoiseScale", this.outerIslandNoiseScale);
                },
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.island.outerIslandNoiseScale"), 
                            Text.of(String.valueOf(this.outerIslandNoiseScale)) 
                    });
                }
        ));
        
        this.buttonList.addSingleOptionEntry(
            new DoubleOption(
                "createWorld.customize.island.outerIslandNoiseOffsetSlider", 
                -1.0D, 1.0D, 0.25f,
                (gameOptions) -> { return (double) this.outerIslandNoiseOffset; }, // Getter
                (gameOptions, value) -> { // Setter
                    this.outerIslandNoiseOffset = value.floatValue();
                    this.chunkProviderSettings.putFloat("outerIslandNoiseOffset", this.outerIslandNoiseOffset);
                },
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.island.outerIslandNoiseOffset"), 
                            Text.of(String.valueOf(this.outerIslandNoiseOffset)) 
                    });
                }
        ));
        
    }
}
