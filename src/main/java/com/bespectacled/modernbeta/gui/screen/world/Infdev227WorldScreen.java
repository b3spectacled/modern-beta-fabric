package com.bespectacled.modernbeta.gui.screen.world;

import java.util.function.BiConsumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.gui.TextOption;
import com.bespectacled.modernbeta.util.NBTUtil;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.DynamicRegistryManager;

public class Infdev227WorldScreen extends InfWorldScreen {
    public Infdev227WorldScreen(
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
        
        CyclingOption<Boolean> generateInfdevPyramid = 
            CyclingOption.create("createWorld.customize.infdev.generateInfdevPyramid", 
                (gameOptions) -> NBTUtil.readBoolean("generateInfdevPyramid", this.chunkProviderSettings, ModernBeta.GEN_CONFIG.generateInfdevPyramid), 
                (gameOptions, option, value) -> this.chunkProviderSettings.putBoolean("generateInfdevPyramid", value)
            );
        
        CyclingOption<Boolean> generateInfdevWall = 
            CyclingOption.create("createWorld.customize.infdev.generateInfdevWall", 
                (gameOptions) -> NBTUtil.readBoolean("generateInfdevWall", this.chunkProviderSettings, ModernBeta.GEN_CONFIG.generateInfdevWall), 
                (gameOptions, option, value) -> this.chunkProviderSettings.putBoolean("generateInfdevWall", value)
            );
      
       this.buttonList.addSingleOptionEntry(generateInfdevPyramid);
       this.buttonList.addSingleOptionEntry(generateInfdevWall);
       
       this.buttonList.addSingleOptionEntry(new TextOption("Note: Settings are not final and may change."));
    }
}
