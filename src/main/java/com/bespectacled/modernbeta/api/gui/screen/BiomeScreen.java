package com.bespectacled.modernbeta.api.gui.screen;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.world.WorldSettings;
import net.minecraft.util.registry.DynamicRegistryManager;

public class BiomeScreen extends GUIScreen {
    protected final DynamicRegistryManager registryManager;
    
    protected BiomeScreen(WorldScreen parent, WorldSettings worldSettings, Consumer<WorldSettings> consumer) {
        super("createWorld.customize.biomeType.title", parent, worldSettings, consumer);

        this.registryManager = parent.getRegistryManager();
    }

}
