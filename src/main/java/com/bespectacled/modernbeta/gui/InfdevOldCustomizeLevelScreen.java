package com.bespectacled.modernbeta.gui;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.nbt.CompoundTag;

public class InfdevOldCustomizeLevelScreen extends InfCustomizeLevelScreen {
    private boolean generateInfdevPyramid;
    private boolean generateInfdevWall;
    
    public InfdevOldCustomizeLevelScreen(CreateWorldScreen parent, CompoundTag providerSettings, Consumer<CompoundTag> consumer) {
        super(parent, providerSettings, consumer);
        
        this.generateInfdevPyramid = ModernBeta.BETA_CONFIG.generateInfdevPyramid;
        this.generateInfdevWall = ModernBeta.BETA_CONFIG.generateInfdevWall;
        
        this.providerSettings.putBoolean("generateInfdevPyramid", this.generateInfdevPyramid);
        this.providerSettings.putBoolean("generateInfdevWall", this.generateInfdevWall);
        
        consumer.accept(this.providerSettings);
    }
    
    @Override
    protected void init() {
        super.init();
        
        this.buttonList.addSingleOptionEntry(
           CyclingOption.create("createWorld.customize.infdev.generateInfdevPyramid", 
               (gameOptions) -> { return generateInfdevPyramid; }, 
               (gameOptions, option, value) -> { // Setter
                   this.generateInfdevPyramid = value;
                   this.providerSettings.putBoolean("generateInfdevPyramid", this.generateInfdevPyramid);
                   
                   consumer.accept(this.providerSettings);
       }));
       
       this.buttonList.addSingleOptionEntry(
           CyclingOption.create("createWorld.customize.infdev.generateInfdevWall", 
               (gameOptions) -> { return generateInfdevWall; }, 
               (gameOptions, option, value) -> { // Setter
                   this.generateInfdevWall = value;
                   this.providerSettings.putBoolean("generateInfdevWall", this.generateInfdevWall);
                   
                   consumer.accept(this.providerSettings);
       }));
    }
}
