package com.bespectacled.modernbeta.gui.screen.world;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.api.world.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.util.NBTUtil;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.DoubleOption;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtInt;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class IslandWorldScreen extends InfWorldScreen {
    public IslandWorldScreen(
        CreateWorldScreen parent,
        WorldSettings worldSettings,
        Consumer<WorldSettings> consumer
    ) {
        super(parent, worldSettings, consumer);
    }
    
    @Override
    protected void init() {
        super.init();
        
        CyclingOption<Boolean> generateOuterIslands = 
            CyclingOption.create("createWorld.customize.island.generateOuterIslands",
                (gameOptions) -> NBTUtil.readBoolean("generateOuterIslands", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.generateOuterIslands), 
                (gameOptions, option, value) -> this.worldSettings.putSetting(WorldSetting.CHUNK, "generateOuterIslands", NbtByte.of(value))
            );
        
        DoubleOption centerIslandRadius =
            new DoubleOption(
                "createWorld.customize.island.centerIslandRadiusSlider", 
                1D, 32D, 1f,
                (gameOptions) -> (double)NBTUtil.readInt("centerIslandRadius", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.centerIslandRadius), // Getter
                (gameOptions, value) -> this.worldSettings.putSetting(WorldSetting.CHUNK, "centerIslandRadius", NbtInt.of(value.intValue())),
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.island.centerIslandRadius"), 
                            Text.of(String.valueOf(NBTUtil.readInt("centerIslandRadius", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.centerIslandRadius) + " chunks")) 
                    });
                }
            );
        
        DoubleOption centerIslandFalloff =
                new DoubleOption(
                "createWorld.customize.island.centerIslandFalloffSlider", 
                1D, 8D, 1f,
                (gameOptions) -> (double)NBTUtil.readFloat("centerIslandFalloff", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.centerIslandFalloff), // Getter
                (gameOptions, value) -> this.worldSettings.putSetting(WorldSetting.CHUNK, "centerIslandFalloff", NbtFloat.of(value.floatValue())),
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.island.centerIslandFalloff"), 
                            Text.of(String.valueOf(NBTUtil.readFloat("centerIslandFalloff", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.centerIslandFalloff))) 
                    });
                }
            );
            
        DoubleOption centerOceanLerpDistance =
            new DoubleOption(
                "createWorld.customize.island.centerOceanLerpDistanceSlider", 
                1D, 32D, 1F,
                (gameOptions) -> (double)NBTUtil.readInt("centerOceanLerpDistance", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.centerOceanLerpDistance), // Getter
                (gameOptions, value) -> this.worldSettings.putSetting(WorldSetting.CHUNK, "centerOceanLerpDistance", NbtInt.of(value.intValue())),
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.island.centerOceanLerpDistance"), 
                            Text.of(String.valueOf(NBTUtil.readInt("centerOceanLerpDistance", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.centerOceanLerpDistance)) + " chunks") 
                    });
                }
            );
        
        DoubleOption centerOceanRadius =
            new DoubleOption(
                "createWorld.customize.island.centerOceanRadiusSlider", 
                8D, 256D, 8F,
                (gameOptions) -> (double)NBTUtil.readInt("centerOceanRadius", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.centerOceanRadius), // Getter
                (gameOptions, value) -> this.worldSettings.putSetting(WorldSetting.CHUNK, "centerOceanRadius", NbtInt.of(value.intValue())),
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.island.centerOceanRadius"), 
                            Text.of(String.valueOf(NBTUtil.readInt("centerOceanRadius", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.centerOceanRadius)) + " chunks") 
                    });
                }
            );
        
        DoubleOption outerIslandNoiseScale = 
            new DoubleOption(
                "createWorld.customize.island.outerIslandNoiseScaleSlider", 
                1D, 1000D, 50f,
                (gameOptions) -> (double)NBTUtil.readFloat("outerIslandNoiseScale", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.outerIslandNoiseScale), // Getter
                (gameOptions, value) -> this.worldSettings.putSetting(WorldSetting.CHUNK, "outerIslandNoiseScale", NbtFloat.of(value.floatValue())),
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.island.outerIslandNoiseScale"), 
                            Text.of(String.valueOf(NBTUtil.readFloat("outerIslandNoiseScale", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.outerIslandNoiseScale))) 
                    });
                }
            );
    
        DoubleOption outerIslandNoiseOffset = 
            new DoubleOption(
                "createWorld.customize.island.outerIslandNoiseOffsetSlider", 
                -1.0D, 1.0D, 0.25f,
                (gameOptions) -> (double)NBTUtil.readFloat("outerIslandNoiseOffset", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.outerIslandNoiseOffset), // Getter
                (gameOptions, value) -> this.worldSettings.putSetting(WorldSetting.CHUNK, "outerIslandNoiseOffset", NbtFloat.of(value.floatValue())),
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.island.outerIslandNoiseOffset"), 
                            Text.of(String.valueOf(NBTUtil.readFloat("outerIslandNoiseOffset", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.outerIslandNoiseOffset))) 
                    });
                }
            );

        this.buttonList.addSingleOptionEntry(generateOuterIslands);
        this.buttonList.addSingleOptionEntry(centerIslandRadius);
        this.buttonList.addSingleOptionEntry(centerIslandFalloff);
        this.buttonList.addSingleOptionEntry(centerOceanRadius);
        this.buttonList.addSingleOptionEntry(centerOceanLerpDistance);
        this.buttonList.addSingleOptionEntry(outerIslandNoiseScale);
        this.buttonList.addSingleOptionEntry(outerIslandNoiseOffset);
    }

}
