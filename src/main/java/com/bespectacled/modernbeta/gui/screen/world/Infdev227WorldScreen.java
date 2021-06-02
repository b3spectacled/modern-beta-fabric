package com.bespectacled.modernbeta.gui.screen.world;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.api.world.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.util.NBTUtil;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.options.BooleanOption;
import net.minecraft.nbt.ByteTag;

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
        
        BooleanOption generateInfdevPyramid = 
            new BooleanOption(
                "createWorld.customize.infdev.generateInfdevPyramid", 
                (gameOptions) -> NBTUtil.readBoolean("generateInfdevPyramid", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.generateInfdevPyramid), 
                (gameOptions, value) -> this.worldSettings.putSetting(WorldSetting.CHUNK, "generateInfdevPyramid",  ByteTag.of(value))
            );
        
        BooleanOption generateInfdevWall = 
            new BooleanOption(
                "createWorld.customize.infdev.generateInfdevWall", 
                (gameOptions) -> NBTUtil.readBoolean("generateInfdevWall", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.generateInfdevWall), 
                (gameOptions, value) -> this.worldSettings.putSetting(WorldSetting.CHUNK, "generateInfdevWall",  ByteTag.of(value))
            );
      
       this.buttonList.addSingleOptionEntry(generateInfdevPyramid);
       this.buttonList.addSingleOptionEntry(generateInfdevWall);
    }
}
