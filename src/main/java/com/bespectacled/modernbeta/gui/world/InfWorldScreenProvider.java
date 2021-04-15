package com.bespectacled.modernbeta.gui.world;

import java.util.function.BiConsumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.AbstractWorldScreenProvider;
import com.bespectacled.modernbeta.api.registry.BiomeProviderRegistry.BuiltInBiomeType;
import com.bespectacled.modernbeta.gui.TextOption;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.DynamicRegistryManager;

public class InfWorldScreenProvider extends AbstractWorldScreenProvider {
    
    private boolean generateOceans;
    
    public InfWorldScreenProvider(
        CreateWorldScreen parent, 
        DynamicRegistryManager registryManager, 
        NbtCompound biomeProviderSettings, 
        NbtCompound chunkProviderSettings, 
        BiConsumer<NbtCompound, NbtCompound> consumer
    ) {
        super(parent, registryManager, biomeProviderSettings, chunkProviderSettings, consumer);
        
        this.generateOceans = this.chunkProviderSettings.contains("generateOceans") ? 
            this.chunkProviderSettings.getBoolean("generateOceans") : 
            ModernBeta.BETA_CONFIG.generation_config.generateOceans;
    }
    
    @Override
    protected void init() {
        super.init();
        
        if (!this.biomeType.equals(BuiltInBiomeType.SINGLE.name)) {
            buttonList.addSingleOptionEntry(
                CyclingOption.create("createWorld.customize.inf.generateOceans",
                (gameOptions) -> { return this.generateOceans; }, 
                (gameOptions, option, value) -> { // Setter
                    this.generateOceans = value;
                    this.chunkProviderSettings.putBoolean("generateOceans", this.generateOceans);
            }));
        }
        
        if (!(this instanceof InfdevOldWorldScreenProvider) && !(this instanceof IslandWorldScreenProvider))
            this.buttonList.addSingleOptionEntry(new TextOption("Note: Settings are not final and may change."));
    }
}
