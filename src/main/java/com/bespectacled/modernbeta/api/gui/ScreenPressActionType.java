package com.bespectacled.modernbeta.api.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.client.gui.widget.ButtonWidget;

public class ScreenPressActionType {
    private static final Map<String, Function<AbstractScreenProvider, ButtonWidget.PressAction>> REGISTRY = new HashMap<>();
    
    public static void registerProvider(String name, Function<AbstractScreenProvider, ButtonWidget.PressAction> action) {
        if (REGISTRY.containsKey(name)) 
            throw new IllegalArgumentException("[Modern Beta] Registry already contains button action named " + name);
        
        REGISTRY.put(name, action);
    }
    
    /*
     * Here, if an entry is not present, we should just return a null action, as to not require an action for a biome type.
     */
    public static Function<AbstractScreenProvider, ButtonWidget.PressAction> getAction(String name) {
        if (!REGISTRY.containsKey(name))
            return screen -> null;
        
        return REGISTRY.get(name);
    }

}
