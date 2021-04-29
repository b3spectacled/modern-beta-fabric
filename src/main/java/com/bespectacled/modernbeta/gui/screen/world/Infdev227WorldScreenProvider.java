package com.bespectacled.modernbeta.gui.screen.world;

import java.util.function.BiConsumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.gui.TextOption;
import com.bespectacled.modernbeta.util.NBTUtil;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.DynamicRegistryManager;

public class Infdev227WorldScreenProvider extends InfWorldScreenProvider {
    private boolean generateInfdevPyramid;
    private boolean generateInfdevWall;
    
    public Infdev227WorldScreenProvider(
        CreateWorldScreen parent, 
        DynamicRegistryManager registryManager, 
        NbtCompound biomeProviderSettings, 
        NbtCompound chunkProviderSettings, 
        BiConsumer<NbtCompound, NbtCompound> consumer
    ) {
        super(parent, registryManager, biomeProviderSettings, chunkProviderSettings, consumer);
        
        this.generateInfdevPyramid = NBTUtil.readBoolean("generateInfdevPyramid", chunkProviderSettings, ModernBeta.GEN_CONFIG.generateInfdevPyramid);
        this.generateInfdevWall = NBTUtil.readBoolean("generateInfdevWall", chunkProviderSettings, ModernBeta.GEN_CONFIG.generateInfdevWall);
    }
    
    @Override
    protected void init() {
        super.init();
        
        this.buttonList.addSingleOptionEntry(
           CyclingOption.create("createWorld.customize.infdev.generateInfdevPyramid", 
               (gameOptions) -> { return generateInfdevPyramid; }, 
               (gameOptions, option, value) -> { // Setter
                   this.generateInfdevPyramid = value;
       }));
       
       this.buttonList.addSingleOptionEntry(
           CyclingOption.create("createWorld.customize.infdev.generateInfdevWall", 
               (gameOptions) -> { return generateInfdevWall; }, 
               (gameOptions, option, value) -> { // Setter
                   this.generateInfdevWall = value;
       }));
       
       this.buttonList.addSingleOptionEntry(new TextOption("Note: Settings are not final and may change."));
    }
    
    @Override
    protected void setChunkProviderSettings() {
        super.setChunkProviderSettings();

        this.chunkProviderSettings.putBoolean("generateInfdevPyramid", this.generateInfdevPyramid);
        this.chunkProviderSettings.putBoolean("generateInfdevWall", this.generateInfdevWall);
    }
}
