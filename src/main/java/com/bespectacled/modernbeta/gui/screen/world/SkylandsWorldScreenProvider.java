package com.bespectacled.modernbeta.gui.screen.world;

import java.util.function.BiConsumer;

import com.bespectacled.modernbeta.api.gui.AbstractWorldScreenProvider;
import com.bespectacled.modernbeta.gui.TextOption;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.DynamicRegistryManager;

public class SkylandsWorldScreenProvider extends AbstractWorldScreenProvider {
    
    public SkylandsWorldScreenProvider(
        CreateWorldScreen parent, 
        DynamicRegistryManager registryManager, 
        NbtCompound biomeProviderSettings, 
        NbtCompound chunkProviderSettings, 
        BiConsumer<NbtCompound, NbtCompound> consumer
    ) {
        super(parent, registryManager, biomeProviderSettings, chunkProviderSettings, consumer);
    }

    @Override
    protected void init() {
        super.init();
        this.buttonList.addSingleOptionEntry(new TextOption("Note: Settings are not final and may change."));
    }
}
