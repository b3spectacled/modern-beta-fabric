package com.bespectacled.modernbeta.gui.screen.world;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.gui.wrapper.BooleanCyclingOptionWrapper;
import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.api.world.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.util.NBTUtil;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.nbt.NbtByte;

public class Infdev227WorldScreen extends InfWorldScreen {
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
            "createWorld.customize.infdev.generateInfdevPyramid",
            () -> NBTUtil.toBoolean(this.worldSettings.getSetting(WorldSetting.CHUNK, "generateInfdevPyramid"), ModernBeta.GEN_CONFIG.generateInfdevPyramid),
            value -> this.worldSettings.putChange(WorldSetting.CHUNK, "generateInfdevPyramid",  NbtByte.of(value))
        );
        
        BooleanCyclingOptionWrapper generateInfdevWall = new BooleanCyclingOptionWrapper(
            "createWorld.customize.infdev.generateInfdevWall", 
            () -> NBTUtil.toBoolean(this.worldSettings.getSetting(WorldSetting.CHUNK, "generateInfdevWall"), ModernBeta.GEN_CONFIG.generateInfdevWall),
            value -> this.worldSettings.putChange(WorldSetting.CHUNK, "generateInfdevWall",  NbtByte.of(value))
        );

        this.addOption(generateInfdevPyramid);
        this.addOption(generateInfdevWall);
    }
}
