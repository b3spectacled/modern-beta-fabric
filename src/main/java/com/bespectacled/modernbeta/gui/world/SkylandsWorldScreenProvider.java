package com.bespectacled.modernbeta.gui.world;

import java.util.function.BiConsumer;

import com.bespectacled.modernbeta.api.AbstractWorldScreenProvider;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.registry.DynamicRegistryManager;

public class SkylandsWorldScreenProvider extends AbstractWorldScreenProvider {
    public SkylandsWorldScreenProvider(
        CreateWorldScreen parent, 
        DynamicRegistryManager registryManager, 
        CompoundTag biomeProviderSettings, 
        CompoundTag chunkProviderSettings, 
        BiConsumer<CompoundTag, CompoundTag> consumer
    ) {
        super(parent, registryManager, biomeProviderSettings, chunkProviderSettings, consumer);
    }
}
