package com.bespectacled.modernbeta.gui.screen.world;

import java.util.function.BiConsumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.gui.AbstractWorldScreenProvider;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.gui.TextOption;
import com.bespectacled.modernbeta.util.NBTUtil;

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
        
        this.generateOceans = NBTUtil.readBoolean("generateOceans", chunkProviderSettings, ModernBeta.BETA_CONFIG.generation_config.generateOceans);
    }
    
    @Override
    protected void init() {
        super.init();
        
        if (!this.biomeType.equals(BuiltInTypes.Biome.SINGLE.name)) {
            buttonList.addSingleOptionEntry(
                CyclingOption.create("createWorld.customize.inf.generateOceans",
                (gameOptions) -> { return this.generateOceans; }, 
                (gameOptions, option, value) -> { // Setter
                    this.generateOceans = value;
                    this.chunkProviderSettings.putBoolean("generateOceans", this.generateOceans);
            }));
        }
        
        if (!(this instanceof Infdev227WorldScreenProvider) && !(this instanceof IslandWorldScreenProvider))
            this.buttonList.addSingleOptionEntry(new TextOption("Note: Settings are not final and may change."));
    }
}
