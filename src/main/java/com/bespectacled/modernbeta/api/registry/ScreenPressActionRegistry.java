package com.bespectacled.modernbeta.api.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.bespectacled.modernbeta.api.AbstractLevelScreenProvider;

import net.minecraft.client.gui.widget.ButtonWidget;

public class ScreenPressActionRegistry {
    private static final Map<String, Function<AbstractLevelScreenProvider, ButtonWidget.PressAction>> REGISTRY = new HashMap<>();
    
    public static void register(String name, Function<AbstractLevelScreenProvider, ButtonWidget.PressAction> action) {
        if (REGISTRY.containsKey(name)) 
            throw new IllegalArgumentException("[Modern Beta] Registry already contains button action named " + name);
        
        REGISTRY.put(name, action);
    }
    
    /*
     * Here, if an entry is not present, we should just return a null action, as to not require an action for a biome type.
     */
    public static Function<AbstractLevelScreenProvider, ButtonWidget.PressAction> get(String name) {
        if (!REGISTRY.containsKey(name))
            return screen -> null;
        
        return REGISTRY.get(name);
    }

}
