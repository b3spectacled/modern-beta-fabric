package com.bespectacled.modernbeta.gui.screen.world;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.gui.WorldScreen;
import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.gui.TextOption;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.util.registry.DynamicRegistryManager;

public class BaseWorldScreen extends WorldScreen {
    public BaseWorldScreen(
        CreateWorldScreen parent, 
        DynamicRegistryManager registryManager,
        WorldSettings worldSettings,
        Consumer<WorldSettings> consumer
    ) {
        super(parent, registryManager, worldSettings, consumer);
    }
    
    @Override
    protected void init() {
        super.init();
        this.buttonList.addSingleOptionEntry(new TextOption("Note: Settings are not final and may change."));
    }

}
