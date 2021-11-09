package com.bespectacled.modernbeta.client.gui.screen.world;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.client.gui.wrapper.BooleanCyclingOptionWrapper;
import com.bespectacled.modernbeta.client.gui.WorldSettings;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.nbt.NbtByte;

public class Infdev227WorldScreen extends InfWorldScreen {
    private static final String INFDEV_PYRAMID_DISPLAY_STRING = "createWorld.customize.infdev.generateInfdevPyramid";
    private static final String INFDEV_WALL_DISPLAY_STRING = "createWorld.customize.infdev.generateInfdevWall";
    
    public Infdev227WorldScreen(
        CreateWorldScreen parent,
        WorldSettings worldSettings,
        Consumer<WorldSettings> consumer
    ) {
        super(parent, worldSettings, consumer);
    }
    
    @Override
    protected void init() {
        super.init();
        
        BooleanCyclingOptionWrapper generateInfdevPyramid = new BooleanCyclingOptionWrapper(
            INFDEV_PYRAMID_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getChunkSetting(NbtTags.GEN_INFDEV_PYRAMID)),
            value -> this.putChunkSetting(NbtTags.GEN_INFDEV_PYRAMID, NbtByte.of(value))
        );
        
        BooleanCyclingOptionWrapper generateInfdevWall = new BooleanCyclingOptionWrapper(
            INFDEV_WALL_DISPLAY_STRING, 
            () -> NbtUtil.toBooleanOrThrow(this.getChunkSetting(NbtTags.GEN_INFDEV_WALL)),
            value -> this.putChunkSetting(NbtTags.GEN_INFDEV_WALL, NbtByte.of(value))
        );

        this.addOption(generateInfdevPyramid);
        this.addOption(generateInfdevWall);
    }
}
