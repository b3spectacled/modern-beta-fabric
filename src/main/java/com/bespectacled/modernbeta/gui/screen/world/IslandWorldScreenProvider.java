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
    public IslandWorldScreenProvider(
        CreateWorldScreen parent, 
        DynamicRegistryManager registryManager,
        NbtCompound chunkProviderSettings,
        NbtCompound biomeProviderSettings,
        BiConsumer<NbtCompound, NbtCompound> consumer
    ) {
        super(parent, registryManager, chunkProviderSettings, biomeProviderSettings, consumer);
    }
    
    @Override
    protected void init() {
        super.init();
        
        CyclingOption<Boolean> generateOuterIslands = 
            CyclingOption.create("createWorld.customize.island.generateOuterIslands",
                (gameOptions) -> NBTUtil.readBoolean("generateOuterIslands", this.chunkProviderSettings, ModernBeta.GEN_CONFIG.generateOuterIslands), 
                (gameOptions, option, value) -> this.chunkProviderSettings.putBoolean("generateOuterIslands", value)
            );
            
        DoubleOption centerOceanLerpDistance =
            new DoubleOption(
                "createWorld.customize.island.centerOceanLerpDistanceSlider", 
                1D, 32D, 1F,
                (gameOptions) -> (double)NBTUtil.readInt("centerOceanLerpDistance", this.chunkProviderSettings, ModernBeta.GEN_CONFIG.centerOceanLerpDistance), // Getter
                (gameOptions, value) -> this.chunkProviderSettings.putInt("centerOceanLerpDistance", value.intValue()),
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.island.centerOceanLerpDistance"), 
                            Text.of(String.valueOf(NBTUtil.readInt("centerOceanLerpDistance", this.chunkProviderSettings, ModernBeta.GEN_CONFIG.centerOceanLerpDistance)) + " chunks") 
                    });
                }
            );
        
        DoubleOption centerOceanRadius =
            new DoubleOption(
                "createWorld.customize.island.centerOceanRadiusSlider", 
                8D, 256D, 8F,
                (gameOptions) -> (double)NBTUtil.readInt("centerOceanRadius", this.chunkProviderSettings, ModernBeta.GEN_CONFIG.centerOceanRadius), // Getter
                (gameOptions, value) -> this.chunkProviderSettings.putInt("centerOceanRadius", value.intValue()),
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.island.centerOceanRadius"), 
                            Text.of(String.valueOf(NBTUtil.readInt("centerOceanRadius", this.chunkProviderSettings, ModernBeta.GEN_CONFIG.centerOceanRadius)) + " chunks") 
                    });
                }
            );
        
        DoubleOption centerIslandFalloff =
            new DoubleOption(
                "createWorld.customize.island.centerIslandFalloffSlider", 
                1D, 8D, 1f,
                (gameOptions) -> (double)NBTUtil.readFloat("centerIslandFalloff", this.chunkProviderSettings, ModernBeta.GEN_CONFIG.centerIslandFalloff), // Getter
                (gameOptions, value) -> this.chunkProviderSettings.putFloat("centerIslandFalloff", value.floatValue()),
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.island.centerIslandFalloff"), 
                            Text.of(String.valueOf(NBTUtil.readFloat("centerIslandFalloff", this.chunkProviderSettings, ModernBeta.GEN_CONFIG.centerIslandFalloff))) 
                    });
                }
            );
        
        DoubleOption outerIslandNoiseScale = 
            new DoubleOption(
                "createWorld.customize.island.outerIslandNoiseScaleSlider", 
                1D, 1000D, 50f,
                (gameOptions) -> (double)NBTUtil.readFloat("outerIslandNoiseScale", this.chunkProviderSettings, ModernBeta.GEN_CONFIG.outerIslandNoiseScale), // Getter
                (gameOptions, value) -> this.chunkProviderSettings.putFloat("outerIslandNoiseScale", value.floatValue()),
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.island.outerIslandNoiseScale"), 
                            Text.of(String.valueOf(NBTUtil.readFloat("outerIslandNoiseScale", this.chunkProviderSettings, ModernBeta.GEN_CONFIG.outerIslandNoiseScale))) 
                    });
                }
            );
    
        DoubleOption outerIslandNoiseOffset = 
            new DoubleOption(
                "createWorld.customize.island.outerIslandNoiseOffsetSlider", 
                -1.0D, 1.0D, 0.25f,
                (gameOptions) -> (double)NBTUtil.readFloat("outerIslandNoiseOffset", chunkProviderSettings, ModernBeta.GEN_CONFIG.outerIslandNoiseOffset), // Getter
                (gameOptions, value) -> this.chunkProviderSettings.putFloat("outerIslandNoiseScale", value.floatValue()),
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.island.outerIslandNoiseOffset"), 
                            Text.of(String.valueOf(NBTUtil.readFloat("outerIslandNoiseOffset", chunkProviderSettings, ModernBeta.GEN_CONFIG.outerIslandNoiseOffset))) 
                    });
                }
            );

        this.buttonList.addSingleOptionEntry(generateOuterIslands);
        this.buttonList.addSingleOptionEntry(centerOceanLerpDistance);
        this.buttonList.addSingleOptionEntry(centerOceanRadius);
        this.buttonList.addSingleOptionEntry(centerIslandFalloff);
        this.buttonList.addSingleOptionEntry(outerIslandNoiseScale);
        this.buttonList.addSingleOptionEntry(outerIslandNoiseOffset);
        
        this.buttonList.addSingleOptionEntry(new TextOption("Note: Settings are not final and may change."));
    }

}
