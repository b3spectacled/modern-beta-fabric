package com.bespectacled.modernbeta.util;

import java.util.Map;
import java.util.Optional;

import net.minecraft.client.world.GeneratorType;
import net.minecraft.client.world.GeneratorType.ScreenProvider;

public interface MutableGeneratorType {
    static MutableGeneratorType injectGeneratorType(GeneratorType generatorType) {
        return (MutableGeneratorType) generatorType;
    }
    
    Map<Optional<GeneratorType>, ScreenProvider> getScreenProviders();
    void setScreenProviders(Map<Optional<GeneratorType>, ScreenProvider> newScreenProviders);
}
