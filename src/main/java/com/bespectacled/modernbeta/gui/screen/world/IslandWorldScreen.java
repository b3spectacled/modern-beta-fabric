package com.bespectacled.modernbeta.gui.screen.world;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.gui.wrapper.BooleanCyclingOptionWrapper;
import com.bespectacled.modernbeta.api.gui.wrapper.DoubleOptionWrapper;
import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.api.world.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.util.NBTUtil;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtInt;

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
        
        BooleanCyclingOptionWrapper generateOuterIslands = new BooleanCyclingOptionWrapper(
            "createWorld.customize.island.generateOuterIslands",
            () -> NBTUtil.toBoolean(this.worldSettings.getSetting(WorldSetting.CHUNK, "generateOuterIslands"), ModernBeta.GEN_CONFIG.generateOuterIslands),
            value -> {
                // Queue change
                this.worldSettings.putChange(WorldSetting.CHUNK, "generateOuterIslands", NbtByte.of(value));
                
                // Reset screen, to hide outer islands options, if generateOuterIslands set to false.
                this.client.openScreen(
                    this.worldProvider.createWorldScreen(
                        (CreateWorldScreen)this.parent,
                        this.worldSettings,
                        this.consumer
                ));
            }
        );
        
        DoubleOptionWrapper<Integer> centerIslandRadius = new DoubleOptionWrapper<>(
            "createWorld.customize.island.centerIslandRadius",
            "chunks",
            1D, 32D, 1f,
            () -> NBTUtil.toInt(this.worldSettings.getSetting(WorldSetting.CHUNK, "centerIslandRadius"), ModernBeta.GEN_CONFIG.centerIslandRadius),
            value -> this.worldSettings.putChange(WorldSetting.CHUNK, "centerIslandRadius", NbtInt.of(value.intValue()))
        );
        
        DoubleOptionWrapper<Float> centerIslandFalloff = new DoubleOptionWrapper<>(
            "createWorld.customize.island.centerIslandFalloff",
            "",
            1D, 8D, 1f,
            () -> NBTUtil.toFloat(this.worldSettings.getSetting(WorldSetting.CHUNK, "centerIslandFalloff"), ModernBeta.GEN_CONFIG.centerIslandFalloff),
            value -> this.worldSettings.putChange(WorldSetting.CHUNK, "centerIslandFalloff", NbtFloat.of(value.floatValue()))
        );
        
        DoubleOptionWrapper<Integer> centerOceanLerpDistance = new DoubleOptionWrapper<>(
            "createWorld.customize.island.centerOceanLerpDistance",
            "chunks",
            1D, 32D, 1F,
            () -> NBTUtil.toInt(this.worldSettings.getSetting(WorldSetting.CHUNK, "centerOceanLerpDistance"), ModernBeta.GEN_CONFIG.centerOceanLerpDistance),
            value -> this.worldSettings.putChange(WorldSetting.CHUNK, "centerOceanLerpDistance", NbtInt.of(value.intValue()))
        );
        
        DoubleOptionWrapper<Integer> centerOceanRadius = new DoubleOptionWrapper<>(
            "createWorld.customize.island.centerOceanRadius",
            "chunks",
            8D, 256D, 8F,
            () -> NBTUtil.toInt(this.worldSettings.getSetting(WorldSetting.CHUNK, "centerOceanRadius"), ModernBeta.GEN_CONFIG.centerOceanRadius),
            value -> this.worldSettings.putChange(WorldSetting.CHUNK, "centerOceanRadius", NbtInt.of(value.intValue()))
        );
        
        DoubleOptionWrapper<Float> outerIslandNoiseScale = new DoubleOptionWrapper<>(
            "createWorld.customize.island.outerIslandNoiseScale",
            "",
            1D, 1000D, 50f,
            () -> NBTUtil.toFloat(this.worldSettings.getSetting(WorldSetting.CHUNK, "outerIslandNoiseScale"), ModernBeta.GEN_CONFIG.outerIslandNoiseScale),
            value -> this.worldSettings.putChange(WorldSetting.CHUNK, "outerIslandNoiseScale", NbtFloat.of(value.floatValue()))
        );
        
        DoubleOptionWrapper<Float> outerIslandNoiseOffset = new DoubleOptionWrapper<>(
            "createWorld.customize.island.outerIslandNoiseOffset",
            "",
            -1.0D, 1.0D, 0.25f,
            () -> NBTUtil.toFloat(this.worldSettings.getSetting(WorldSetting.CHUNK, "outerIslandNoiseOffset"), ModernBeta.GEN_CONFIG.outerIslandNoiseOffset),
            value -> this.worldSettings.putChange(WorldSetting.CHUNK, "outerIslandNoiseOffset", NbtFloat.of(value.floatValue()))
        );
        
        boolean generatesOuterIslands = NBTUtil.toBoolean(
            this.worldSettings.getSetting(WorldSetting.CHUNK, "generateOuterIslands"), 
            ModernBeta.GEN_CONFIG.generateOuterIslands
        );
        
        this.addOption(generateOuterIslands);
        this.addOption(centerIslandRadius);
        this.addOption(centerIslandFalloff);
        
        if (generatesOuterIslands) {
            this.addOption(centerOceanRadius);
            this.addOption(centerOceanLerpDistance);
            this.addOption(outerIslandNoiseScale);
            this.addOption(outerIslandNoiseOffset);
        }
    }

}
