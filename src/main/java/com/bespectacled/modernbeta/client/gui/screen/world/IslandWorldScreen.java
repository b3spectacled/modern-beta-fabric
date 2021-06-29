package com.bespectacled.modernbeta.client.gui.screen.world;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.client.gui.wrapper.BooleanCyclingOptionWrapper;
import com.bespectacled.modernbeta.api.client.gui.wrapper.DoubleOptionWrapper;
import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.api.world.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.util.NBTUtil;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtInt;
import net.minecraft.text.TranslatableText;

public class IslandWorldScreen extends InfWorldScreen {
    private static final String GENERATE_OUTER_ISLANDS_DISPLAY_STRING = "createWorld.customize.island.generateOuterIslands";
    private static final String CENTER_ISLAND_RADIUS_DISPLAY_STRING = "createWorld.customize.island.centerIslandRadius";
    private static final String CENTER_ISLAND_FALLOFF_DISPLAY_STRING = "createWorld.customize.island.centerIslandFalloff";
    private static final String CENTER_ISLAND_LERP_DIST_DISPLAY_STRING = "createWorld.customize.island.centerOceanLerpDistance";
    private static final String CENTER_OCEAN_RADIUS_DISPLAY_STRING = "createWorld.customize.island.centerOceanRadius";
    private static final String OUTER_ISLAND_NOISE_SCALE_DISPLAY_STRING = "createWorld.customize.island.outerIslandNoiseScale";
    private static final String OUTER_ISLAND_NOISE_OFFSET_DISPLAY_STRING = "createWorld.customize.island.outerIslandNoiseOffset";

    private static final String CENTER_ISLAND_RADIUS_TOOLTIP = "createWorld.customize.island.centerIslandRadius.tooltip";
    private static final String CENTER_ISLAND_FALLOFF_TOOLTIP = "createWorld.customize.island.centerIslandFalloff.tooltip";
    private static final String CENTER_ISLAND_LERP_DIST_TOOLTIP = "createWorld.customize.island.centerOceanLerpDistance.tooltip";
    private static final String CENTER_OCEAN_RADIUS_TOOLTIP = "createWorld.customize.island.centerOceanRadius.tooltip";
    private static final String OUTER_ISLAND_NOISE_SCALE_TOOLTIP = "createWorld.customize.island.outerIslandNoiseScale.tooltip";
    private static final String OUTER_ISLAND_NOISE_OFFSET_TOOLTIP = "createWorld.customize.island.outerIslandNoiseOffset.tooltip";
    
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
            GENERATE_OUTER_ISLANDS_DISPLAY_STRING,
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
            CENTER_ISLAND_RADIUS_DISPLAY_STRING,
            "chunks",
            1D, 32D, 1f,
            () -> NBTUtil.toInt(this.worldSettings.getSetting(WorldSetting.CHUNK, "centerIslandRadius"), ModernBeta.GEN_CONFIG.centerIslandRadius),
            value -> this.worldSettings.putChange(WorldSetting.CHUNK, "centerIslandRadius", NbtInt.of(value.intValue())),
            this.client.textRenderer.wrapLines(new TranslatableText(CENTER_ISLAND_RADIUS_TOOLTIP), 200)
        );
        
        DoubleOptionWrapper<Float> centerIslandFalloff = new DoubleOptionWrapper<>(
            CENTER_ISLAND_FALLOFF_DISPLAY_STRING,
            "",
            1D, 8D, 1f,
            () -> NBTUtil.toFloat(this.worldSettings.getSetting(WorldSetting.CHUNK, "centerIslandFalloff"), ModernBeta.GEN_CONFIG.centerIslandFalloff),
            value -> this.worldSettings.putChange(WorldSetting.CHUNK, "centerIslandFalloff", NbtFloat.of(value.floatValue())),
            this.client.textRenderer.wrapLines(new TranslatableText(CENTER_ISLAND_FALLOFF_TOOLTIP), 200)
        );
        
        DoubleOptionWrapper<Integer> centerOceanLerpDistance = new DoubleOptionWrapper<>(
            CENTER_ISLAND_LERP_DIST_DISPLAY_STRING,
            "chunks",
            1D, 32D, 1F,
            () -> NBTUtil.toInt(this.worldSettings.getSetting(WorldSetting.CHUNK, "centerOceanLerpDistance"), ModernBeta.GEN_CONFIG.centerOceanLerpDistance),
            value -> this.worldSettings.putChange(WorldSetting.CHUNK, "centerOceanLerpDistance", NbtInt.of(value.intValue())),
            this.client.textRenderer.wrapLines(new TranslatableText(CENTER_ISLAND_LERP_DIST_TOOLTIP), 200)
        );
        
        DoubleOptionWrapper<Integer> centerOceanRadius = new DoubleOptionWrapper<>(
            CENTER_OCEAN_RADIUS_DISPLAY_STRING,
            "chunks",
            8D, 256D, 8F,
            () -> NBTUtil.toInt(this.worldSettings.getSetting(WorldSetting.CHUNK, "centerOceanRadius"), ModernBeta.GEN_CONFIG.centerOceanRadius),
            value -> this.worldSettings.putChange(WorldSetting.CHUNK, "centerOceanRadius", NbtInt.of(value.intValue())),
            this.client.textRenderer.wrapLines(new TranslatableText(CENTER_OCEAN_RADIUS_TOOLTIP), 200)
        );
        
        DoubleOptionWrapper<Float> outerIslandNoiseScale = new DoubleOptionWrapper<>(
            OUTER_ISLAND_NOISE_SCALE_DISPLAY_STRING,
            "",
            1D, 1000D, 50f,
            () -> NBTUtil.toFloat(this.worldSettings.getSetting(WorldSetting.CHUNK, "outerIslandNoiseScale"), ModernBeta.GEN_CONFIG.outerIslandNoiseScale),
            value -> this.worldSettings.putChange(WorldSetting.CHUNK, "outerIslandNoiseScale", NbtFloat.of(value.floatValue())),
            this.client.textRenderer.wrapLines(new TranslatableText(OUTER_ISLAND_NOISE_SCALE_TOOLTIP), 200)
        );
        
        DoubleOptionWrapper<Float> outerIslandNoiseOffset = new DoubleOptionWrapper<>(
            OUTER_ISLAND_NOISE_OFFSET_DISPLAY_STRING,
            "",
            -1.0D, 1.0D, 0.25f,
            () -> NBTUtil.toFloat(this.worldSettings.getSetting(WorldSetting.CHUNK, "outerIslandNoiseOffset"), ModernBeta.GEN_CONFIG.outerIslandNoiseOffset),
            value -> this.worldSettings.putChange(WorldSetting.CHUNK, "outerIslandNoiseOffset", NbtFloat.of(value.floatValue())),
            this.client.textRenderer.wrapLines(new TranslatableText(OUTER_ISLAND_NOISE_OFFSET_TOOLTIP), 200)
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
