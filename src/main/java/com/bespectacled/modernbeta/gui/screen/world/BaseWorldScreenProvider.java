package com.bespectacled.modernbeta.gui.screen.world;

import java.util.function.BiConsumer;

import com.bespectacled.modernbeta.api.gui.WorldScreenProvider;
import com.bespectacled.modernbeta.gui.TextOption;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.DynamicRegistryManager;

public class BaseWorldScreenProvider extends WorldScreenProvider {
    public BaseWorldScreenProvider(
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
        this.buttonList.addSingleOptionEntry(new TextOption("Note: Settings are not final and may change."));
    }

}
