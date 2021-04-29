package com.bespectacled.modernbeta.gui.screen.world;

import java.util.function.BiConsumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.gui.TextOption;
import com.bespectacled.modernbeta.util.NBTUtil;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.DoubleOption;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.DynamicRegistryManager;

public class IslandWorldScreenProvider extends InfWorldScreenProvider {

    private boolean generateOuterIslands;
    private int centerOceanLerpDistance;
    private int centerOceanRadius;
    private float centerIslandFalloff;
    private float outerIslandNoiseScale;
    private float outerIslandNoiseOffset;
    
    public IslandWorldScreenProvider(
        CreateWorldScreen parent, 
        DynamicRegistryManager registryManager, 
        NbtCompound biomeProviderSettings, 
        NbtCompound chunkProviderSettings, 
        BiConsumer<NbtCompound, NbtCompound> consumer
    ) {
        super(parent, registryManager, biomeProviderSettings, chunkProviderSettings, consumer);
       
        this.generateOuterIslands = NBTUtil.readBoolean("generateOuterIslands", chunkProviderSettings, ModernBeta.GEN_CONFIG.generateOuterIslands);
        this.centerOceanLerpDistance = NBTUtil.readInt("centerOceanLerpDistance", chunkProviderSettings, ModernBeta.GEN_CONFIG.centerOceanLerpDistance);
        this.centerOceanRadius = NBTUtil.readInt("centerOceanRadius", chunkProviderSettings, ModernBeta.GEN_CONFIG.centerOceanRadius);
        this.centerIslandFalloff = NBTUtil.readFloat("centerIslandFalloff", chunkProviderSettings, ModernBeta.GEN_CONFIG.centerIslandFalloff);
        this.outerIslandNoiseScale = NBTUtil.readFloat("outerIslandNoiseScale", chunkProviderSettings, ModernBeta.GEN_CONFIG.outerIslandNoiseScale);
        this.outerIslandNoiseOffset = NBTUtil.readFloat("outerIslandNoiseOffset", chunkProviderSettings, ModernBeta.GEN_CONFIG.outerIslandNoiseOffset);
    }

    @Override
    protected void init() {
        super.init();
        
        buttonList.addSingleOptionEntry(
            CyclingOption.create("createWorld.customize.island.generateOuterIslands",
            (gameOptions) -> { return this.generateOuterIslands; }, 
            (gameOptions, option, value) -> { // Setter
                this.generateOuterIslands = value;
        }));
    
        this.buttonList.addSingleOptionEntry(
            new DoubleOption(
                "createWorld.customize.island.centerOceanLerpDistanceSlider", 
                1D, 32D, 1F,
                (gameOptions) -> { return (double) this.centerOceanLerpDistance; }, // Getter
                (gameOptions, value) -> { // Setter
                    this.centerOceanLerpDistance = value.intValue();
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
        
        this.buttonList.addSingleOptionEntry(new TextOption("Note: Settings are not final and may change."));
    }
    
    @Override
    protected void setChunkProviderSettings() {
        super.setChunkProviderSettings();

        this.chunkProviderSettings.putBoolean("generateOuterIslands", this.generateOuterIslands);
        this.chunkProviderSettings.putInt("centerOceanLerpDistance", this.centerOceanLerpDistance);
        this.chunkProviderSettings.putInt("centerOceanRadius", this.centerOceanRadius);
        this.chunkProviderSettings.putFloat("centerIslandFalloff", this.centerIslandFalloff);
        this.chunkProviderSettings.putFloat("outerIslandNoiseScale", this.outerIslandNoiseScale);
        this.chunkProviderSettings.putFloat("outerIslandNoiseOffset", this.outerIslandNoiseOffset);
    }
}
