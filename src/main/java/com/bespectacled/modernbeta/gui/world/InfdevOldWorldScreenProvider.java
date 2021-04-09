package com.bespectacled.modernbeta.gui.world;

import java.util.function.BiConsumer;
import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.options.BooleanOption;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.registry.DynamicRegistryManager;

public class InfdevOldWorldScreenProvider extends InfWorldScreenProvider {
    private boolean generateInfdevPyramid;
    private boolean generateInfdevWall;
    
    public InfdevOldWorldScreenProvider(
        CreateWorldScreen parent, 
        DynamicRegistryManager registryManager, 
        CompoundTag biomeProviderSettings, 
        CompoundTag chunkProviderSettings, 
        BiConsumer<CompoundTag, CompoundTag> consumer
    ) {
        super(parent, registryManager, biomeProviderSettings, chunkProviderSettings, consumer);
        
        this.generateInfdevPyramid = this.chunkProviderSettings.contains("generateInfdevPyramid") ? 
            this.chunkProviderSettings.getBoolean("generateInfdevPyramid") :    
            ModernBeta.BETA_CONFIG.generationConfig.generateInfdevPyramid;
        
        this.generateInfdevWall = this.chunkProviderSettings.contains("generateInfdevWall") ? 
            this.chunkProviderSettings.getBoolean("generateInfdevWall") :
            ModernBeta.BETA_CONFIG.generationConfig.generateInfdevWall;
    }
    
    @Override
    protected void init() {
        super.init();
        
        this.buttonList.addSingleOptionEntry(
            new BooleanOption(
                "createWorld.customize.infdev.generateInfdevPyramid", 
                (gameOptions) -> { return generateInfdevPyramid; }, 
                (gameOptions, value) -> {
                    generateInfdevPyramid = value;
                    this.chunkProviderSettings.putBoolean("generateInfdevPyramid", value);
                }
        ));
       
        this.buttonList.addSingleOptionEntry(
            new BooleanOption(
                "createWorld.customize.infdev.generateInfdevWall", 
                (gameOptions) -> { return generateInfdevWall; }, 
                (gameOptions, value) -> {
                    generateInfdevWall = value;
                    this.chunkProviderSettings.putBoolean("generateInfdevWall", value);
                }
        ));
    }
}
