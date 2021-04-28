package com.bespectacled.modernbeta.gui.screen.world;

import java.util.function.BiConsumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.gui.AbstractWorldScreenProvider;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.options.BooleanOption;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.registry.DynamicRegistryManager;

public class InfWorldScreenProvider extends AbstractWorldScreenProvider {
    
    private boolean generateOceans;
    
    public InfWorldScreenProvider(
        CreateWorldScreen parent, 
        DynamicRegistryManager registryManager, 
        CompoundTag biomeProviderSettings, 
        CompoundTag chunkProviderSettings, 
        BiConsumer<CompoundTag, CompoundTag> consumer
    ) {
        super(parent, registryManager, biomeProviderSettings, chunkProviderSettings, consumer);
        
        this.generateOceans = this.chunkProviderSettings.contains("generateOceans") ? 
            this.chunkProviderSettings.getBoolean("generateOceans") : 
            ModernBeta.BETA_CONFIG.generation_config.generateOceans;
    }
    
    @Override
    protected void init() {
        super.init();
        
        if (!this.biomeType.equals(BuiltInTypes.Biome.SINGLE.name)) {
            this.buttonList.addSingleOptionEntry(
                new BooleanOption(
                    "createWorld.customize.inf.generateOceans", 
                    (gameOptions) -> { return generateOceans; }, // Getter
                    (gameOptions, value) -> { // Setter
                        generateOceans = value;
                        this.chunkProviderSettings.putBoolean("generateOceans", value);
                    }
            ));
        }
    }
}
