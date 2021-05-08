package com.bespectacled.modernbeta.gui.screen.world;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.api.world.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.util.NBTUtil;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.nbt.NbtByte;
import net.minecraft.util.registry.DynamicRegistryManager;

public class Infdev227WorldScreen extends InfWorldScreen {
    public Infdev227WorldScreen(
        CreateWorldScreen parent, 
        DynamicRegistryManager registryManager,
        WorldSettings worldSettings,
        Consumer<WorldSettings> consumer
    ) {
        super(parent, registryManager, worldSettings, consumer);
    }
    
    @Override
    protected void init() {
        super.init();
        
        CyclingOption<Boolean> generateInfdevPyramid = 
            CyclingOption.create("createWorld.customize.infdev.generateInfdevPyramid", 
                (gameOptions) -> NBTUtil.readBoolean("generateInfdevPyramid", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.generateInfdevPyramid), 
                (gameOptions, option, value) -> this.worldSettings.putSetting(WorldSetting.CHUNK, "generateInfdevPyramid",  NbtByte.of(value))
            );
        
        CyclingOption<Boolean> generateInfdevWall = 
            CyclingOption.create("createWorld.customize.infdev.generateInfdevWall", 
                (gameOptions) -> NBTUtil.readBoolean("generateInfdevWall", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.generateInfdevWall), 
                (gameOptions, option, value) -> this.worldSettings.putSetting(WorldSetting.CHUNK, "generateInfdevWall",  NbtByte.of(value))
            );
      
       this.buttonList.addSingleOptionEntry(generateInfdevPyramid);
       this.buttonList.addSingleOptionEntry(generateInfdevWall);
    }
}
