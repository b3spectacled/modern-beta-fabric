package com.bespectacled.modernbeta.api;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import com.google.common.collect.ImmutableList;

public class WorldProviderType {
    private static final List<WorldProvider> WORLD_PROVIDERS = new ArrayList<WorldProvider>();
    
    public static void addWorldProvider(WorldProvider worldProvider) {
        for (WorldProvider w : WORLD_PROVIDERS) {
            if (w.getName().equalsIgnoreCase(worldProvider.getName()))
                throw new IllegalArgumentException("[Modern Beta] Registry already contains world provider named " + w.getName());
        }
        
        WORLD_PROVIDERS.add(worldProvider);
    }
    
    public static WorldProvider getWorldProvider(String name) {
        for (WorldProvider w : WORLD_PROVIDERS) {
            if (w.getName().equalsIgnoreCase(name))
                return w;
        }
        
        throw new NoSuchElementException("[Modern Beta] Registry does not contain world provider named " + name);
    }
    
    public static List<WorldProvider> getWorldProviders() {
        return ImmutableList.copyOf(WORLD_PROVIDERS);
    }
}
