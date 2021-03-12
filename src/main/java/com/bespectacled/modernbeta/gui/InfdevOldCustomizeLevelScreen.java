package com.bespectacled.modernbeta.gui;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.nbt.CompoundTag;

public class InfdevOldCustomizeLevelScreen extends InfCustomizeLevelScreen {
    private boolean generateInfdevPyramid;
    private boolean generateInfdevWall;
    
    public InfdevOldCustomizeLevelScreen(CreateWorldScreen parent, CompoundTag settings, Consumer<CompoundTag> consumer) {
        super(parent, settings, consumer);
        
        this.generateInfdevPyramid = ModernBeta.BETA_CONFIG.generateInfdevPyramid;
        this.generateInfdevWall = ModernBeta.BETA_CONFIG.generateInfdevWall;
        
        this.settings.putBoolean("generateInfdevPyramid", this.generateInfdevPyramid);
        this.settings.putBoolean("generateInfdevWall", this.generateInfdevWall);
    }
    
    @Override
    protected void init() {
        super.init();
        
        this.buttonList.addSingleOptionEntry(
           CyclingOption.create("createWorld.customize.infdev.generateInfdevPyramid", 
               (gameOptions) -> { return generateInfdevPyramid; }, 
               (gameOptions, option, value) -> { // Setter
                   this.generateInfdevPyramid = value;
                   this.settings.putBoolean("generateInfdevPyramid", this.generateInfdevPyramid);
       }));
       
       this.buttonList.addSingleOptionEntry(
           CyclingOption.create("createWorld.customize.infdev.generateInfdevWall", 
               (gameOptions) -> { return generateInfdevWall; }, 
               (gameOptions, option, value) -> { // Setter
                   this.generateInfdevWall = value;
                   this.settings.putBoolean("generateInfdevWall", this.generateInfdevWall);
       }));
    }
}
