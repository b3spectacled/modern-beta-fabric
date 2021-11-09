package com.bespectacled.modernbeta.client.gui.screen.world;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.client.gui.WorldSettings;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;

public class BaseWorldScreen extends WorldScreen {
    public BaseWorldScreen(
        CreateWorldScreen parent,
        WorldSettings worldSettings,
        Consumer<WorldSettings> consumer
    ) {
        super(parent, worldSettings, consumer);
    }
    
    @Override
    protected void init() {
        super.init();
    }

}
