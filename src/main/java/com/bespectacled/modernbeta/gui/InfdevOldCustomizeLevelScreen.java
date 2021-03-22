package com.bespectacled.modernbeta.gui;

import java.util.function.BiConsumer;
import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.registry.DynamicRegistryManager;

public class InfdevOldCustomizeLevelScreen extends InfCustomizeLevelScreen {
    private boolean generateInfdevPyramid;
    private boolean generateInfdevWall;
    
    public InfdevOldCustomizeLevelScreen(
        CreateWorldScreen parent, 
        DynamicRegistryManager registryManager, 
        CompoundTag biomeProviderSettings, 
        CompoundTag chunkProviderSettings, 
        BiConsumer<CompoundTag, CompoundTag> consumer
    ) {
        super(parent, registryManager, biomeProviderSettings, chunkProviderSettings, consumer);
        
        this.generateInfdevPyramid = this.chunkProviderSettings.contains("generateInfdevPyramid") ? 
            this.chunkProviderSettings.getBoolean("generateInfdevPyramid") :    
            ModernBeta.BETA_CONFIG.generateInfdevPyramid;
        
        this.generateInfdevWall = this.chunkProviderSettings.contains("generateInfdevWall") ? 
            this.chunkProviderSettings.getBoolean("generateInfdevWall") :
            ModernBeta.BETA_CONFIG.generateInfdevWall;
        
        this.chunkProviderSettings.putBoolean("generateInfdevPyramid", this.generateInfdevPyramid);
        this.chunkProviderSettings.putBoolean("generateInfdevWall", this.generateInfdevWall);
    }
    
    @Override
    protected void init() {
        super.init();
        
        this.buttonList.addSingleOptionEntry(
           CyclingOption.create("createWorld.customize.infdev.generateInfdevPyramid", 
               (gameOptions) -> { return generateInfdevPyramid; }, 
               (gameOptions, option, value) -> { // Setter
                   this.generateInfdevPyramid = value;
                   this.chunkProviderSettings.putBoolean("generateInfdevPyramid", this.generateInfdevPyramid);
       }));
       
       this.buttonList.addSingleOptionEntry(
           CyclingOption.create("createWorld.customize.infdev.generateInfdevWall", 
               (gameOptions) -> { return generateInfdevWall; }, 
               (gameOptions, option, value) -> { // Setter
                   this.generateInfdevWall = value;
                   this.chunkProviderSettings.putBoolean("generateInfdevWall", this.generateInfdevWall);
       }));
    }
}
