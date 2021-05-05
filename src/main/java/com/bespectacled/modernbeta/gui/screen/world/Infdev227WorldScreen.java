package com.bespectacled.modernbeta.gui.screen.world;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.gui.TextOption;
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
                (gameOptions) -> NBTUtil.readBoolean("generateInfdevPyramid", this.worldSettings.getChunkSettings(), ModernBeta.GEN_CONFIG.generateInfdevPyramid), 
                (gameOptions, option, value) -> this.worldSettings.putChunkSetting("generateInfdevPyramid",  NbtByte.of(value))
            );
        
        CyclingOption<Boolean> generateInfdevWall = 
            CyclingOption.create("createWorld.customize.infdev.generateInfdevWall", 
                (gameOptions) -> NBTUtil.readBoolean("generateInfdevWall", this.worldSettings.getChunkSettings(), ModernBeta.GEN_CONFIG.generateInfdevWall), 
                (gameOptions, option, value) -> this.worldSettings.putChunkSetting("generateInfdevWall",  NbtByte.of(value))
            );
      
       this.buttonList.addSingleOptionEntry(generateInfdevPyramid);
       this.buttonList.addSingleOptionEntry(generateInfdevWall);
       
       this.buttonList.addSingleOptionEntry(new TextOption("Note: Settings are not final and may change."));
    }
}
